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

package com.espertech.esper.core.context.mgr;

import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.epl.spec.ContextDetail;

public class ContextControllerFactoryServiceContext {
    private final String contextName;
    private final EPServicesContext servicesContext;
    private final ContextDetail detail;
    private final AgentInstanceContext agentInstanceContextCreate;
    private final boolean isRecoveringResilient;

    public ContextControllerFactoryServiceContext(String contextName, EPServicesContext servicesContext, ContextDetail detail, AgentInstanceContext agentInstanceContextCreate, boolean isRecoveringResilient) {
        this.contextName = contextName;
        this.servicesContext = servicesContext;
        this.detail = detail;
        this.agentInstanceContextCreate = agentInstanceContextCreate;
        this.isRecoveringResilient = isRecoveringResilient;
    }

    public String getContextName() {
        return contextName;
    }

    public EPServicesContext getServicesContext() {
        return servicesContext;
    }

    public ContextDetail getDetail() {
        return detail;
    }

    public AgentInstanceContext getAgentInstanceContextCreate() {
        return agentInstanceContextCreate;
    }

    public boolean isRecoveringResilient() {
        return isRecoveringResilient;
    }
}
