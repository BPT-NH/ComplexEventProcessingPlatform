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
import com.espertech.esper.dataflow.annotations.DataFlowContext;
import com.espertech.esper.dataflow.annotations.DataFlowOpParameter;
import com.espertech.esper.dataflow.annotations.DataFlowOperator;
import com.espertech.esper.dataflow.interfaces.*;
import com.espertech.esper.dataflow.util.GraphTypeDesc;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprNodeUtility;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.event.EventBeanSPI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;

@DataFlowOperator
public class Filter implements DataFlowOpLifecycle {

    private static final Log log = LogFactory.getLog(Filter.class);

    @DataFlowOpParameter
    private ExprNode filter;
    
    private ExprEvaluator evaluator;
    private EventBeanSPI theEvent;
    private EventBean[] eventsPerStream = new EventBean[1];
    private boolean singleOutputPort;

    @DataFlowContext
    private EPDataFlowEmitter graphContext;

    public DataFlowOpInitializeResult initialize(DataFlowOpInitializateContext prepareContext) throws Exception {

        if (prepareContext.getInputPorts().size() != 1) {
            throw new ExprValidationException("Filter requires single input port");
        }
        if (filter == null) {
            throw new ExprValidationException("Required parameter 'filter' providing the filter expression is not provided");
        }
        if (prepareContext.getOutputPorts().isEmpty() || prepareContext.getOutputPorts().size() > 2) {
            throw new IllegalArgumentException("Filter operator requires one or two output stream(s) but produces " + prepareContext.getOutputPorts().size() + " streams");
        }

        EventType eventType = prepareContext.getInputPorts().get(0).getTypeDesc().getEventType();
        singleOutputPort = prepareContext.getOutputPorts().size() == 1;

        ExprNode validated = ExprNodeUtility.validateSimpleGetSubtree(filter, prepareContext.getStatementContext(), eventType);
        evaluator = validated.getExprEvaluator();
        theEvent = prepareContext.getServicesContext().getEventAdapterService().getShellForType(eventType);
        eventsPerStream[0] = theEvent;

        GraphTypeDesc[] typesPerPort = new GraphTypeDesc[prepareContext.getOutputPorts().size()];
        for (int i = 0; i < typesPerPort.length; i++) {
            typesPerPort[i] = new GraphTypeDesc(false, true, eventType);
        }
        return new DataFlowOpInitializeResult(typesPerPort);
    }

    public void onInput(Object row) {
        if (log.isDebugEnabled()) {
            log.debug("Received row for filtering: " + Arrays.toString((Object[]) row));
        }

        if (!(row instanceof EventBean)) {
            theEvent.setUnderlying(row);
        }
        else {
            theEvent = (EventBeanSPI) row;
        }

        Boolean pass = (Boolean) evaluator.evaluate(eventsPerStream, true, null);
        if (pass != null && pass) {
            if (log.isDebugEnabled()) {
                log.debug("Submitting row " + Arrays.toString((Object[]) row));
            }

            if (singleOutputPort) {
                graphContext.submit(row);
            }
            else {
                graphContext.submitPort(0, row);
            }
        }
        else {
            if (!singleOutputPort) {
                graphContext.submitPort(1, row);
            }
        }
    }

    public void open(DataFlowOpOpenContext openContext) {
        // no action
    }

    public void close(DataFlowOpCloseContext openContext) {
        // no action
    }
}
