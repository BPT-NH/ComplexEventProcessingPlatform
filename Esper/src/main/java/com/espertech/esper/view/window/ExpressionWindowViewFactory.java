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
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.event.map.MapEventBean;
import com.espertech.esper.view.View;
import com.espertech.esper.view.ViewFactoryContext;
import com.espertech.esper.view.ViewParameterException;
import com.espertech.esper.view.ViewServiceHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Factory for {@link com.espertech.esper.view.window.ExpressionWindowView}.
 */
public class ExpressionWindowViewFactory extends ExpressionViewFactoryBase
{
    public void setViewParameters(ViewFactoryContext viewFactoryContext, List<ExprNode> expressionParameters) throws ViewParameterException
    {
        if (expressionParameters.size() != 1) {
            String errorMessage = "Expression window view requires a single expression as a parameter";
            throw new ViewParameterException(errorMessage);
        }
        expiryExpression = expressionParameters.get(0);
    }

    public View makeView(final AgentInstanceViewFactoryChainContext agentInstanceViewFactoryContext)
    {
        MapEventBean builtinMapBean = new MapEventBean(new HashMap<String, Object>(), builtinMapType);
        IStreamRandomAccess randomAccess = ViewServiceHelper.getOptPreviousExprRandomAccess(agentInstanceViewFactoryContext);
        return new ExpressionWindowView(this, randomAccess, expiryExpression.getExprEvaluator(), aggregationServiceFactoryDesc, builtinMapBean, variableNames, agentInstanceViewFactoryContext);
    }

    public Object makePreviousGetter() {
        return new RandomAccessByIndexGetter();
    }
}
