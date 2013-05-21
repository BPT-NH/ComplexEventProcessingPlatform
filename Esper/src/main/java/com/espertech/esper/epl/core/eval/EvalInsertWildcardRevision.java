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

public class EvalInsertWildcardRevision extends EvalBase implements SelectExprProcessor {

    private final ValueAddEventProcessor vaeProcessor;

    public EvalInsertWildcardRevision(SelectExprContext selectExprContext, EventType resultEventType, ValueAddEventProcessor vaeProcessor) {
        super(selectExprContext, resultEventType);
        this.vaeProcessor = vaeProcessor;
    }

    public EventBean process(EventBean[] eventsPerStream, boolean isNewData, boolean isSynthesize, ExprEvaluatorContext exprEvaluatorContext) {
        EventBean theEvent = eventsPerStream[0];
        return vaeProcessor.getValueAddEventBean(theEvent);
    }
}