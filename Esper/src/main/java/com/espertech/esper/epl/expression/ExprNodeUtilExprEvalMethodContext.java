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
import com.espertech.esper.client.hook.EPLMethodInvocationContext;

import java.util.Map;

public class ExprNodeUtilExprEvalMethodContext implements ExprEvaluator {

    private final String functionName;

    public ExprNodeUtilExprEvalMethodContext(String functionName) {
        this.functionName = functionName;
    }

    public Object evaluate(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        return new EPLMethodInvocationContext(context.getStatementName(),
                context.getAgentInstanceId(), context.getEngineURI(), functionName);
    }

    public Class getType() {
        return EPLMethodInvocationContext.class;
    }

    public Map<String, Object> getEventType() throws ExprValidationException {
        return null;
    }
}
