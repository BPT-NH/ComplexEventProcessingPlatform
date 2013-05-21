/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.context.ContextPartitionSelector;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.core.service.EPPreparedQueryResult;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.core.StreamTypeServiceImpl;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.named.NamedWindowProcessor;
import com.espertech.esper.epl.named.NamedWindowProcessorInstance;
import com.espertech.esper.epl.spec.NamedWindowConsumerStreamSpec;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import com.espertech.esper.epl.spec.StreamSpecCompiled;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecCompiler;
import com.espertech.esper.util.AuditPath;
import com.espertech.esper.util.CollectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Starts and provides the stop method for EPL statements.
 */
public abstract class EPPreparedExecuteSingleStream implements EPPreparedExecuteMethod
{
    private static final Log queryPlanLog = LogFactory.getLog(AuditPath.QUERYPLAN_LOG);
    private static final Log log = LogFactory.getLog(EPPreparedExecuteSingleStream.class);

    protected final StatementSpecCompiled statementSpec;
    protected final NamedWindowProcessor processor;
    protected final EPServicesContext services;
    protected final EPPreparedExecuteSingleStreamExec executor;
    protected final StatementContext statementContext;

    public abstract EPPreparedExecuteSingleStreamExec getExecutor(FilterSpecCompiled filter, String aliasName);

    /**
     * Ctor.
     * @param statementSpec is a container for the definition of all statement constructs that
     * may have been used in the statement, i.e. if defines the select clauses, insert into, outer joins etc.
     * @param services is the service instances for dependency injection
     * @param statementContext is statement-level information and statement services
     * @throws com.espertech.esper.epl.expression.ExprValidationException if the preparation failed
     */
    public EPPreparedExecuteSingleStream(StatementSpecCompiled statementSpec,
                                         EPServicesContext services,
                                         StatementContext statementContext)
            throws ExprValidationException
    {
        boolean queryPlanLogging = services.getConfigSnapshot().getEngineDefaults().getLogging().isEnableQueryPlan();
        if (queryPlanLogging) {
            queryPlanLog.info("Query plans for Fire-and-forget query '" + statementContext.getExpression() + "'");
        }

        this.statementSpec = statementSpec;
        this.services = services;
        this.statementContext = statementContext;

        EPPreparedExecuteMethodHelper.validateFAFQuery(statementSpec);

        // resolve named window processor
        final StreamSpecCompiled streamSpec = statementSpec.getStreamSpecs()[0];
        NamedWindowConsumerStreamSpec namedSpec = (NamedWindowConsumerStreamSpec) streamSpec;

        String aliasName = namedSpec.getWindowName();
        if (namedSpec.getOptionalStreamName() != null) {
            aliasName = namedSpec.getOptionalStreamName();
        }

        processor = services.getNamedWindowService().getProcessor(namedSpec.getWindowName());
        if (processor == null) {
            throw new ExprValidationException("A named window by name '" + namedSpec.getWindowName() + "' does not exist");
        }
        EventType eventType = processor.getTailView().getEventType();

        // compile filter to optimize access to named window
        StreamTypeServiceImpl typeService = new StreamTypeServiceImpl(new EventType[] {eventType}, new String[] {aliasName}, new boolean[] {true}, services.getEngineURI(), true);
        FilterSpecCompiled filter;
        if (statementSpec.getFilterRootNode() != null) {
            LinkedHashMap<String, Pair<EventType, String>> tagged = new LinkedHashMap<String, Pair<EventType, String>>();
            FilterSpecCompiled filterCompiled;
            try {
                filterCompiled = FilterSpecCompiler.makeFilterSpec(eventType, aliasName,
                        Collections.singletonList(statementSpec.getFilterRootNode()), null,
                        tagged, tagged, typeService,
                        null, statementContext, Collections.singleton(0));
            }
            catch (Exception ex) {
                log.warn("Unexpected exception analyzing filter paths: " + ex.getMessage(), ex);
                filterCompiled = null;
            }
            filter = filterCompiled;
        }
        else {
            filter = null;
        }

        // validate expressions
        EPStatementStartMethodHelperValidate.validateNodes(statementSpec, statementContext, typeService, null);

        // get executor
        executor = getExecutor(filter, aliasName);
    }

    /**
     * Returns the event type of the prepared statement.
     * @return event type
     */
    public EventType getEventType()
    {
        return processor.getNamedWindowType();
    }

    /**
     * Executes the prepared query.
     * @return query results
     */
    public EPPreparedQueryResult execute(ContextPartitionSelector[] contextPartitionSelectors)
    {
        if (contextPartitionSelectors != null && contextPartitionSelectors.length != 1) {
            throw new IllegalArgumentException("Number of context partition selectors must be one");
        }
        ContextPartitionSelector optionalSingleSelector = contextPartitionSelectors != null && contextPartitionSelectors.length > 0 ? contextPartitionSelectors[0] : null;

        // validate context
        if (processor.getContextName() != null &&
            statementSpec.getOptionalContextName() != null &&
            !processor.getContextName().equals(statementSpec.getOptionalContextName())) {
            throw new EPException("Context for named window is '" + processor.getContextName() + "' and query specifies context '" + statementSpec.getOptionalContextName() + "'");
        }

        // handle non-specified context
        if (statementSpec.getOptionalContextName() == null) {
            NamedWindowProcessorInstance processorInstance = processor.getProcessorInstanceNoContext();
            if (processorInstance != null) {
                EventBean[] rows = executor.execute(processorInstance);
                if (rows.length > 0) {
                    dispatch();
                }
                return new EPPreparedQueryResult(processor.getNamedWindowType(), rows);
            }
        }

        // context partition runtime query
        Collection<Integer> agentInstanceIds = EPPreparedExecuteMethodHelper.getAgentInstanceIds(processor, optionalSingleSelector, services.getContextManagementService(), processor.getContextName());

        // collect events and agent instances
        if (agentInstanceIds.isEmpty()) {
            return new EPPreparedQueryResult(processor.getNamedWindowType(), CollectionUtil.EVENT_PER_STREAM_EMPTY);
        }

        if (agentInstanceIds.size() == 1) {
            int agentInstanceId = agentInstanceIds.iterator().next();
            NamedWindowProcessorInstance processorInstance = processor.getProcessorInstance(agentInstanceId);
            EventBean[] rows = executor.execute(processorInstance);
            if (rows.length > 0) {
                dispatch();
            }
            return new EPPreparedQueryResult(processor.getNamedWindowType(), rows);
        }

        ArrayDeque<EventBean> allRows = new ArrayDeque<EventBean>();
        for (int agentInstanceId : agentInstanceIds) {
            NamedWindowProcessorInstance processorInstance = processor.getProcessorInstance(agentInstanceId);
            if (processorInstance != null) {
                EventBean[] rows = executor.execute(processorInstance);
                allRows.addAll(Arrays.asList(rows));
            }
        }
        if (allRows.size() > 0) {
            dispatch();
        }
        return new EPPreparedQueryResult(processor.getNamedWindowType(), allRows.toArray(new EventBean[allRows.size()]));
    }

    protected void dispatch() {
        services.getInternalEventEngineRouteDest().processThreadWorkQueue();
    }
}
