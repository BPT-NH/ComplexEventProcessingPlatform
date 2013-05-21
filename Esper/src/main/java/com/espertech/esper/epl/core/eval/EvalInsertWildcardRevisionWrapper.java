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

package com.espertech.esper.epl.core.eval;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.core.SelectExprProcessor;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.event.vaevent.ValueAddEventProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class EvalInsertWildcardRevisionWrapper extends EvalBaseMap implements SelectExprProcessor {

    private static final Log log = LogFactory.getLog(EvalInsertWildcardRevisionWrapper.class);

    private final ValueAddEventProcessor vaeProcessor;
    private final EventType wrappingEventType;

    public EvalInsertWildcardRevisionWrapper(SelectExprContext selectExprContext, EventType resultEventType, ValueAddEventProcessor vaeProcessor, EventType wrappingEventType) {
        super(selectExprContext, resultEventType);
        this.vaeProcessor = vaeProcessor;
        this.wrappingEventType = wrappingEventType;
    }

    public EventBean processSpecific(Map<String, Object> props, EventBean[] eventsPerStream, boolean isNewData, boolean isSynthesize, ExprEvaluatorContext exprEvaluatorContext)
    {
        EventBean underlying = eventsPerStream[0];
        EventBean wrapped = super.getEventAdapterService().adapterForTypedWrapper(underlying, props, wrappingEventType);
        return vaeProcessor.getValueAddEventBean(wrapped);
    }
}