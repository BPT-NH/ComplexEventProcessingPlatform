/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.pattern;

import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.StatementContext;

/**
 * Contains handles to implementations of services needed by evaluation nodes.
 */
public class PatternAgentInstanceContext
{
    private final PatternContext patternContext;
    private final AgentInstanceContext agentInstanceContext;
    private final EvalFilterConsumptionHandler consumptionHandler;

    public PatternAgentInstanceContext(PatternContext patternContext, AgentInstanceContext agentInstanceContext, boolean hasConsumingFilter) {
        this.patternContext = patternContext;
        this.agentInstanceContext = agentInstanceContext;

        if (hasConsumingFilter) {
            consumptionHandler = new EvalFilterConsumptionHandler();
        }
        else {
            consumptionHandler = null;
        }
    }

    public PatternContext getPatternContext() {
        return patternContext;
    }

    public AgentInstanceContext getAgentInstanceContext() {
        return agentInstanceContext;
    }

    public EvalFilterConsumptionHandler getConsumptionHandler() {
        return consumptionHandler;
    }

    public StatementContext getStatementContext() {
        return agentInstanceContext.getStatementContext();
    }
}
