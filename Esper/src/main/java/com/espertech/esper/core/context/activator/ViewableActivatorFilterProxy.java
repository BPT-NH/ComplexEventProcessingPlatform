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
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.EPStatementHandleCallback;
import com.espertech.esper.filter.FilterHandleCallback;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterValueSet;
import com.espertech.esper.filter.FilterValueSetParam;
import com.espertech.esper.view.EventStream;
import com.espertech.esper.view.ZeroDepthStream;
import com.espertech.esper.view.stream.EventStreamProxy;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class ViewableActivatorFilterProxy implements ViewableActivator {

    private final EPServicesContext services;
    private final FilterSpecCompiled filterSpec;
    private final Annotation annotations[];
    private final boolean isSubSelect;

    public ViewableActivatorFilterProxy(EPServicesContext services, FilterSpecCompiled filterSpec, Annotation[] annotations, boolean subSelect) {
        this.services = services;
        this.filterSpec = filterSpec;
        this.annotations = annotations;
        isSubSelect = subSelect;
    }

    public ViewableActivationResult activate(final AgentInstanceContext agentInstanceContext, boolean isSubselect, boolean isRecoveringResilient) {

        // New event stream
        EventType resultEventType = filterSpec.getResultEventType();
        EventStream zeroDepthStream = new ZeroDepthStream(resultEventType);

        // audit proxy
        EventStream inputStream = EventStreamProxy.getAuditProxy(agentInstanceContext.getStatementContext().getEngineURI(), agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementHandle().getStatementName(), annotations, filterSpec, zeroDepthStream);

        final EventStream eventStream = inputStream;
        final String statementId = agentInstanceContext.getStatementContext().getStatementId();
        
        FilterHandleCallback filterCallback;
        if (filterSpec.getOptionalPropertyEvaluator() != null)
        {
            filterCallback = new FilterHandleCallback()
            {
                public String getStatementId()
                {
                    return statementId;
                }

                public void matchFound(EventBean theEvent, Collection<FilterHandleCallback> allStmtMatches)
                {
                    EventBean[] result = filterSpec.getOptionalPropertyEvaluator().getProperty(theEvent, agentInstanceContext);
                    if (result == null)
                    {
                        return;
                    }
                    eventStream.insert(result);
                }

                public boolean isSubSelect()
                {
                    return isSubSelect;
                }
            };
        }
        else
        {
            filterCallback = new FilterHandleCallback()
            {
                public String getStatementId()
                {
                    return statementId;
                }

                public void matchFound(EventBean theEvent, Collection<FilterHandleCallback> allStmtMatches)
                {
                    eventStream.insert(theEvent);
                }
                public boolean isSubSelect()
                {
                    return isSubSelect;
                }
            };
        }
        EPStatementHandleCallback filterHandle = new EPStatementHandleCallback(agentInstanceContext.getEpStatementAgentInstanceHandle(), filterCallback);

        FilterValueSetParam[] addendum = null;
        if (agentInstanceContext.getAgentInstanceFilterProxy() != null) {
            addendum = agentInstanceContext.getAgentInstanceFilterProxy().getAddendumFilters(filterSpec);
        }
        FilterValueSet filterValueSet = filterSpec.getValueSet(null, agentInstanceContext, addendum);
        services.getFilterService().add(filterValueSet, filterHandle);

        ViewableActivatorFilterProxyStopCallback stopCallback = new ViewableActivatorFilterProxyStopCallback(this, filterHandle);
        return new ViewableActivationResult(inputStream, stopCallback, null, null);
    }

    public EPServicesContext getServices() {
        return services;
    }
}
