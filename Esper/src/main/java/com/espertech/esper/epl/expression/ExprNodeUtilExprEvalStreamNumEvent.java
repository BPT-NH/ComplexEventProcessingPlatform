/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.EventBean;

import java.util.Map;

public class ExprNodeUtilExprEvalStreamNumEvent implements ExprEvaluator {
    private final int streamNum;

    public ExprNodeUtilExprEvalStreamNumEvent(int streamNum) {
        this.streamNum = streamNum;
    }

    public Object evaluate(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        return eventsPerStream[streamNum];
    }

    public Class getType() {
        return EventBean.class;
    }

    public Map<String, Object> getEventType() throws ExprValidationException {
        return null;
    }
}
