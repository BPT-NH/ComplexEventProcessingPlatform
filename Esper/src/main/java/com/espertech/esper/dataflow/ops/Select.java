/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.dataflow.ops;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.dataflow.EPDataFlowSignal;
import com.espertech.esper.client.dataflow.EPDataFlowSignalFinalMarker;
import com.espertech.esper.collection.UniformPair;
import com.espertech.esper.core.context.activator.ViewableActivationResult;
import com.espertech.esper.core.context.activator.ViewableActivator;
import com.espertech.esper.core.context.activator.ViewableActivatorFactory;
import com.espertech.esper.core.context.factory.StatementAgentInstanceFactorySelectResult;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.context.util.StatementAgentInstanceUtil;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.core.service.StatementLifecycleSvcUtil;
import com.espertech.esper.core.start.EPStatementStartMethodHelperAssignExpr;
import com.espertech.esper.core.start.EPStatementStartMethodSelectDesc;
import com.espertech.esper.core.start.EPStatementStartMethodSelectUtil;
import com.espertech.esper.dataflow.annotations.DataFlowContext;
import com.espertech.esper.dataflow.annotations.DataFlowOpParameter;
import com.espertech.esper.dataflow.annotations.DataFlowOperator;
import com.espertech.esper.dataflow.interfaces.*;
import com.espertech.esper.dataflow.ops.epl.EPLSelectDeliveryCallback;
import com.espertech.esper.dataflow.ops.epl.EPLSelectUpdateDispatchView;
import com.espertech.esper.dataflow.ops.epl.EPLSelectViewable;
import com.espertech.esper.dataflow.util.GraphTypeDesc;
import com.espertech.esper.epl.annotation.AnnotationUtil;
import com.espertech.esper.epl.declexpr.ExprDeclaredNode;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.epl.spec.*;
import com.espertech.esper.epl.spec.util.StatementSpecRawAnalyzer;
import com.espertech.esper.epl.view.OutputProcessViewCallback;
import com.espertech.esper.event.EventBeanAdapterFactory;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecParam;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.util.StopCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.util.*;

@DataFlowOperator
public class Select implements OutputProcessViewCallback, DataFlowOpLifecycle {

    private static final Log log = LogFactory.getLog(Select.class);

    @DataFlowOpParameter
    private StatementSpecRaw select;

    @DataFlowOpParameter
    private boolean iterate;

    private EPLSelectViewable[] viewablesPerPort;
    private EventBeanAdapterFactory[] adapterFactories;
    private AgentInstanceContext agentInstanceContext;
    private EPLSelectDeliveryCallback deliveryCallback;
    private StatementAgentInstanceFactorySelectResult selectResult;
    private boolean isOutputLimited;
    private boolean submitEventBean;

    @DataFlowContext
    private EPDataFlowEmitter graphContext;

    public DataFlowOpInitializeResult initialize(DataFlowOpInitializateContext context) throws ExprValidationException {
        if (context.getInputPorts().isEmpty()) {
            throw new IllegalArgumentException("Select operator requires at least one input stream");
        }
        if (context.getOutputPorts().size() != 1) {
            throw new IllegalArgumentException("Select operator requires one output stream but produces " + context.getOutputPorts().size() + " streams");
        }

        DataFlowOpOutputPort portZero = context.getOutputPorts().get(0);
        if (portZero.getOptionalDeclaredType() != null && !portZero.getOptionalDeclaredType().isUnderlying()) {
            submitEventBean = true;
        }

        // determine adapter factories for each type
        int numStreams = context.getInputPorts().size();
        adapterFactories = new EventBeanAdapterFactory[numStreams];
        for (int i = 0; i < numStreams; i++) {
            EventType eventType = context.getInputPorts().get(i).getTypeDesc().getEventType();
            adapterFactories[i] = context.getStatementContext().getEventAdapterService().getAdapterFactoryForType(eventType);
        }

        // Compile and prepare execution
        //
        final StatementContext statementContext = context.getStatementContext();
        EPServicesContext servicesContext = context.getServicesContext();
        AgentInstanceContext agentInstanceContext = context.getAgentInstanceContext();

        // validate
        if (select.getInsertIntoDesc() != null) {
            throw new ExprValidationException("Insert-into clause is not supported");
        }
        if (select.getSelectStreamSelectorEnum() != SelectClauseStreamSelectorEnum.ISTREAM_ONLY) {
            throw new ExprValidationException("Selecting remove-stream is not supported");
        }
        ExprNodeSubselectDeclaredDotVisitor visitor = StatementSpecRawAnalyzer.walkSubselectAndDeclaredDotExpr(select);
        if (!visitor.getSubselects().isEmpty()) {
            throw new ExprValidationException("Subselects are not supported");
        }

        Map<Integer, FilterStreamSpecRaw> streams = new HashMap<Integer, FilterStreamSpecRaw>();
        for (int streamNum = 0; streamNum < select.getStreamSpecs().size(); streamNum++) {
            StreamSpecRaw rawStreamSpec = select.getStreamSpecs().get(streamNum);
            if (!(rawStreamSpec instanceof FilterStreamSpecRaw)) {
                throw new ExprValidationException("From-clause must contain only streams and cannot contain patterns or other constructs");
            }
            streams.put(streamNum, (FilterStreamSpecRaw) rawStreamSpec);
        }

        // compile offered streams
        List<StreamSpecCompiled> streamSpecCompileds = new ArrayList<StreamSpecCompiled>();
        for (int streamNum = 0; streamNum < select.getStreamSpecs().size(); streamNum++) {
            FilterStreamSpecRaw filter = streams.get(streamNum);
            Map.Entry<Integer, DataFlowOpInputPort> inputPort = findInputPort(filter.getRawFilterSpec().getEventTypeName(), context.getInputPorts());
            if (inputPort == null) {
                throw new ExprValidationException("Failed to find stream '" + filter.getRawFilterSpec().getEventTypeName() + "' among input ports, input ports are " + Arrays.toString(getInputPortNames(context.getInputPorts())));
            }
            EventType eventType = inputPort.getValue().getTypeDesc().getEventType();
            String streamAlias = filter.getOptionalStreamName();
            FilterSpecCompiled filterSpecCompiled = new FilterSpecCompiled(eventType, streamAlias, Collections.<FilterSpecParam>emptyList(), null);
            FilterStreamSpecCompiled filterStreamSpecCompiled = new FilterStreamSpecCompiled(filterSpecCompiled, select.getStreamSpecs().get(0).getViewSpecs(), streamAlias, new StreamSpecOptions());
            streamSpecCompileds.add(filterStreamSpecCompiled);
        }

        // create compiled statement spec
        SelectClauseSpecCompiled selectClauseCompiled = StatementLifecycleSvcUtil.compileSelectClause(select.getSelectClauseSpec());

        // determine if snapshot output is needed
        OutputLimitSpec outputLimitSpec = select.getOutputLimitSpec();
        isOutputLimited = outputLimitSpec != null;
        if (iterate) {
            if (outputLimitSpec != null) {
                throw new ExprValidationException("Output rate limiting is not supported with 'iterate'");
            }
            outputLimitSpec = new OutputLimitSpec(OutputLimitLimitType.SNAPSHOT, OutputLimitRateType.TERM);
        }

        Annotation[] mergedAnnotations = AnnotationUtil.mergeAnnotations(statementContext.getAnnotations(), context.getOperatorAnnotations());
        ExprNode[] groupByExpressions = ExprNodeUtility.toArray(select.getGroupByExpressions());
        OrderByItem[] orderByArray = OrderByItem.toArray(select.getOrderByList());
        OuterJoinDesc[] outerJoinArray = OuterJoinDesc.toArray(select.getOuterJoinDescList());
        StreamSpecCompiled[] streamSpecArray = streamSpecCompileds.toArray(new StreamSpecCompiled[streamSpecCompileds.size()]);
        StatementSpecCompiled compiled = new StatementSpecCompiled(null, null, null, null, null, null, SelectClauseStreamSelectorEnum.ISTREAM_ONLY,
                selectClauseCompiled, streamSpecArray, outerJoinArray, select.getFilterExprRootNode(), groupByExpressions, select.getHavingExprRootNode(), outputLimitSpec,
                orderByArray, ExprSubselectNode.EMPTY_SUBSELECT_ARRAY, ExprNodeUtility.EMPTY_DECLARED_ARR, select.getReferencedVariables(),
                select.getRowLimitSpec(), CollectionUtil.EMPTY_STRING_ARRAY, mergedAnnotations, null, null, null, null, null, null, null, null, null);

        // create viewable per port
        final EPLSelectViewable[] viewables = new EPLSelectViewable[context.getInputPorts().size()];
        this.viewablesPerPort = viewables;
        for (Map.Entry<Integer, DataFlowOpInputPort> entry : context.getInputPorts().entrySet()) {
            EPLSelectViewable viewable = new EPLSelectViewable(entry.getValue().getTypeDesc().getEventType());
            viewables[entry.getKey()] = viewable;
        }

        ViewableActivatorFactory activatorFactory = new ViewableActivatorFactory() {
            public ViewableActivator createActivator(FilterStreamSpecCompiled filterStreamSpec) {

                EPLSelectViewable found = null;
                for (EPLSelectViewable viewable : viewables) {
                    if (viewable.getEventType() == filterStreamSpec.getFilterSpec().getFilterForEventType()) {
                        found = viewable;
                    }
                }
                if (found == null) {
                    throw new IllegalStateException("Failed to find viewable for filter");
                }
                final EPLSelectViewable viewable = found;
                return new ViewableActivator() {
                    public ViewableActivationResult activate(AgentInstanceContext agentInstanceContext, boolean isSubselect, boolean isRecoveringResilient) {
                        return new ViewableActivationResult(viewable, new StopCallback() {public void stop() {}}, null, null);
                    }
                };
            }
        };

        // for per-row deliver, register select expression result callback
        OutputProcessViewCallback optionalOutputProcessViewCallback = null;
        if (!iterate && !isOutputLimited) {
            deliveryCallback = new EPLSelectDeliveryCallback();
            optionalOutputProcessViewCallback = this;
        }

        // prepare
        final EPStatementStartMethodSelectDesc selectDesc = EPStatementStartMethodSelectUtil.prepare(compiled, servicesContext, statementContext, false, agentInstanceContext, false, activatorFactory, optionalOutputProcessViewCallback, deliveryCallback);

        // start
        selectResult = selectDesc.getStatementAgentInstanceFactorySelect().newContext(agentInstanceContext, false);

        // for output-rate-limited, register a dispatch view
        if (isOutputLimited) {
            selectResult.getFinalView().addView(new EPLSelectUpdateDispatchView(this));
        }

        // assign strategies to expression nodes
        EPStatementStartMethodHelperAssignExpr.assignExpressionStrategies(selectDesc, selectResult.getOptionalAggegationService(), selectResult.getSubselectStrategies(), selectResult.getPriorNodeStrategies(), selectResult.getPreviousNodeStrategies());

        EventType outputEventType = selectDesc.getResultSetProcessorPrototypeDesc().getResultSetProcessorFactory().getResultEventType();
        this.agentInstanceContext = agentInstanceContext;
        return new DataFlowOpInitializeResult(new GraphTypeDesc[] {new GraphTypeDesc(false, true, outputEventType)});
    }

    private String[] getInputPortNames(Map<Integer, DataFlowOpInputPort> inputPorts) {
        List<String> portNames = new ArrayList<String>();
        for (Map.Entry<Integer, DataFlowOpInputPort> entry : inputPorts.entrySet()) {
            if (entry.getValue().getOptionalAlias() != null) {
                portNames.add(entry.getValue().getOptionalAlias());
                continue;
            }
            if (entry.getValue().getStreamNames().size() == 1) {
                portNames.add(entry.getValue().getStreamNames().iterator().next());
            }
        }
        return portNames.toArray(new String[portNames.size()]);
    }

    private Map.Entry<Integer, DataFlowOpInputPort> findInputPort(String eventTypeName, Map<Integer, DataFlowOpInputPort> inputPorts) {
        for (Map.Entry<Integer, DataFlowOpInputPort> entry : inputPorts.entrySet()) {
            if (entry.getValue().getOptionalAlias() != null && entry.getValue().getOptionalAlias().equals(eventTypeName)) {
                return entry;
            }
            if (entry.getValue().getStreamNames().size() == 1 && entry.getValue().getStreamNames().iterator().next().equals(eventTypeName)) {
                return entry;
            }
        }
        return null;
    }

    public void onInput(int originatingStream, Object row) {
        if (log.isDebugEnabled()) {
            log.debug("Received row from stream " + originatingStream + " for select, row is " + row);
        }

        EventBean theEvent = adapterFactories[originatingStream].makeAdapter(row);

        agentInstanceContext.getStatementContext().getDefaultAgentInstanceLock().acquireWriteLock(null);
        try {
            viewablesPerPort[originatingStream].process(theEvent);
            if (viewablesPerPort.length > 1) {
                agentInstanceContext.getEpStatementAgentInstanceHandle().getOptionalDispatchable().execute(agentInstanceContext);
            }
        }
        finally {
            agentInstanceContext.getStatementContext().getDefaultAgentInstanceLock().releaseWriteLock(null);
        }
    }

    public void onSignal(EPDataFlowSignal signal) {
        if (iterate && signal instanceof EPDataFlowSignalFinalMarker) {
            Iterator<EventBean> it = selectResult.getFinalView().iterator();
            if (it != null) {
                for (;it.hasNext();) {
                    EventBean event = it.next();
                    if (submitEventBean) {
                        graphContext.submit(event);
                    }
                    else {
                        graphContext.submit(event.getUnderlying());
                    }
                }
            }
        }
    }

    public void open(DataFlowOpOpenContext openContext) {
        // no action
    }

    public void close(DataFlowOpCloseContext openContext) {
        if (selectResult != null) {
            StatementAgentInstanceUtil.stopSafe(selectResult.getStopCallback(), agentInstanceContext.getStatementContext());
        }
    }

    public void outputViaCallback(EventBean[] events) {
        Object[] delivered = deliveryCallback.getDelivered();
        if (log.isDebugEnabled()) {
            log.debug("Submitting select-output row: " + Arrays.toString(delivered));
        }
        graphContext.submit(deliveryCallback.getDelivered());
        deliveryCallback.reset();
    }

    public void outputOutputRateLimited(UniformPair<EventBean[]> result) {
        if (result == null || result.getFirst() == null || result.getFirst().length == 0) {
            return;
        }
        for (EventBean item : result.getFirst()) {
            if (submitEventBean) {
                graphContext.submit(item);
            }
            else {
                graphContext.submit(item.getUnderlying());
            }
        }
    }
}
