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

package com.espertech.esper.epl.script;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.script.mvel.MVELInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExprNodeScriptEvalMVEL extends ExprNodeScriptEvalBase {

    private static final Log log = LogFactory.getLog(ExprNodeScriptEvalMVEL.class);

    private final Object executable;

    public ExprNodeScriptEvalMVEL(String scriptName, String statementName, String[] names, ExprEvaluator[] parameters, Class returnType, Object executable) {
        super(scriptName, statementName, names, parameters, returnType);
        this.executable = executable;
    }

    public Object evaluate(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        Map<String, Object> paramsList = new HashMap<String, Object>();
        for (int i = 0; i < names.length; i++) {
            paramsList.put(names[i], parameters[i].evaluate(eventsPerStream, isNewData, context));
        }
        paramsList.put(ExprNodeScript.CONTEXT_BINDING_NAME, context.getAgentInstanceScriptContext());

        try {
            Object result = MVELInvoker.executeExpression(executable, paramsList);

            if (coercer != null) {
                return coercer.coerceBoxed((Number) result);
            }

            return result;
        }
        catch (InvocationTargetException ex) {
            Throwable mvelException = ex.getCause();
            String message = "Unexpected exception executing script '" + scriptName + "' for statement '" + statementName + "' : " + mvelException.getMessage();
            log.error(message, mvelException);
            throw new EPException(message, ex);
        }
    }
}
