/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.window;

import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.core.service.ExprEvaluatorContextStatement;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.event.map.MapEventBean;
import com.espertech.esper.view.*;

import java.util.HashMap;
import java.util.List;

/**
 * Factory for {@link ExpressionBatchView}.
 */
public class ExpressionBatchViewFactory extends ExpressionViewFactoryBase implements DataWindowBatchingViewFactory
{
    private boolean includeTriggeringEvent = true;

    public void setViewParameters(ViewFactoryContext viewFactoryContext, List<ExprNode> expressionParameters) throws ViewParameterException
    {
        if (expressionParameters.size() != 1 && expressionParameters.size() != 2) {
            String errorMessage = "Expression window view requires a single expression as a parameter, or an expression and boolean flag";
            throw new ViewParameterException(errorMessage);
        }
        expiryExpression = expressionParameters.get(0);

        if (expressionParameters.size() > 1) {
            Object result = ViewFactorySupport.evaluateAssertNoProperties("expression batch window", expressionParameters.get(1), 1, new ExprEvaluatorContextStatement(viewFactoryContext.getStatementContext()));
            includeTriggeringEvent = (Boolean) result;
        }
    }

    public View makeView(final AgentInstanceViewFactoryChainContext agentInstanceViewFactoryContext)
    {
        MapEventBean builtinMapBean = new MapEventBean(new HashMap<String, Object>(), builtinMapType);
        IStreamRelativeAccess relativeAccessByEvent = ViewServiceHelper.getOptPreviousExprRelativeAccess(agentInstanceViewFactoryContext);
        return new ExpressionBatchView(this, relativeAccessByEvent, expiryExpression.getExprEvaluator(), aggregationServiceFactoryDesc, builtinMapBean, variableNames, agentInstanceViewFactoryContext);
    }

    public Object makePreviousGetter() {
        return new RelativeAccessByEventNIndexMap();
    }

    public boolean isIncludeTriggeringEvent() {
        return includeTriggeringEvent;
    }
}
