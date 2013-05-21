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
import com.espertech.esper.core.service.ExpressionResultCacheService;
import com.espertech.esper.core.service.ExpressionResultCacheServiceThreadlocal;
import com.espertech.esper.epl.script.AgentInstanceScriptContext;
import com.espertech.esper.core.service.StatementAgentInstanceLock;
import com.espertech.esper.schedule.TimeProvider;

/**
 * Represents a minimal enginel-level context for expression evaluation, not allowing for agents instances and result cache.
 */
public class ExprEvaluatorContextTimeOnly implements ExprEvaluatorContext
{
    private final TimeProvider timeProvider;
    private final ExpressionResultCacheService expressionResultCacheService;

    public ExprEvaluatorContextTimeOnly(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.expressionResultCacheService = new ExpressionResultCacheServiceThreadlocal();
    }

    /**
     * Returns the time provider.
     * @return time provider
     */
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public ExpressionResultCacheService getExpressionResultCacheService() {
        return expressionResultCacheService;
    }

    public int getAgentInstanceId() {
        return -1;
    }

    public EventBean getContextProperties() {
        return null;
    }

    public AgentInstanceScriptContext getAgentInstanceScriptContext() {
        return null;
    }

    public String getStatementName() {
        return null;
    }

    public String getEngineURI() {
        return null;
    }

    public String getStatementId() {
        return null;
    }

    public StatementAgentInstanceLock getAgentInstanceLock() {
        return null;
    }
}