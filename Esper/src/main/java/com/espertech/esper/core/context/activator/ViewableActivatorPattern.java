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

package com.espertech.esper.core.context.activator;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.pattern.*;
import com.espertech.esper.view.EventStream;
import com.espertech.esper.view.ZeroDepthStream;

import java.util.Map;

public class ViewableActivatorPattern implements ViewableActivator {

    private final PatternContext patternContext;
    private final EvalRootFactoryNode rootFactoryNode;
    private final EventType eventType;
    private final boolean hasConsumingFilter;

    public ViewableActivatorPattern(PatternContext patternContext, EvalRootFactoryNode rootFactoryNode, EventType eventType, boolean hasConsumingFilter) {
        this.patternContext = patternContext;
        this.rootFactoryNode = rootFactoryNode;
        this.eventType = eventType;
        this.hasConsumingFilter = hasConsumingFilter;
    }

    public ViewableActivationResult activate(AgentInstanceContext agentInstanceContext, boolean isSubselect, boolean isRecoveringResilient) {
        PatternAgentInstanceContext patternAgentInstanceContext = agentInstanceContext.getStatementContext().getPatternContextFactory().createPatternAgentContext(patternContext, agentInstanceContext, hasConsumingFilter);
        EvalRootNode rootNode = EvalNodeUtil.makeRootNodeFromFactory(rootFactoryNode, patternAgentInstanceContext);

        final EventStream sourceEventStream = new ZeroDepthStream(eventType);
        final StatementContext statementContext = patternContext.getStatementContext();
        final PatternMatchCallback callback = new PatternMatchCallback() {
            public void matchFound(Map<String, Object> matchEvent)
            {
                EventBean compositeEvent = statementContext.getEventAdapterService().adapterForTypedMap(matchEvent, eventType);
                sourceEventStream.insert(compositeEvent);
            }
        };

        EvalRootState rootState = rootNode.start(callback, patternContext, isRecoveringResilient);
        return new ViewableActivationResult(sourceEventStream, rootState, null, rootState);
    }
}
