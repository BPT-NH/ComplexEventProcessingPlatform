/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.named;

import com.espertech.esper.client.EventType;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.StatementResultService;
import com.espertech.esper.epl.core.ResultSetProcessor;

/**
 * View for the on-delete statement that handles removing events from a named window.
 */
public class NamedWindowOnDeleteViewFactory extends NamedWindowOnExprBaseViewFactory
{
    private final StatementResultService statementResultService;

    public NamedWindowOnDeleteViewFactory(EventType namedWindowEventType, StatementResultService statementResultService) {
        super(namedWindowEventType);
        this.statementResultService = statementResultService;
    }

    public StatementResultService getStatementResultService() {
        return statementResultService;
    }

    public NamedWindowOnExprBaseView make(NamedWindowLookupStrategy lookupStrategy, NamedWindowRootViewInstance namedWindowRootViewInstance, AgentInstanceContext agentInstanceContext, ResultSetProcessor resultSetProcessor) {
        return new NamedWindowOnDeleteView(lookupStrategy, namedWindowRootViewInstance, agentInstanceContext, this);
    }
}
