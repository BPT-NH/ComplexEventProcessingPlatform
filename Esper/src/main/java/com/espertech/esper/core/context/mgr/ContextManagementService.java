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
import com.espertech.esper.core.context.util.ContextDescriptor;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.CreateContextDesc;

public interface ContextManagementService {
    public void addContextSpec(EPServicesContext servicesContext, AgentInstanceContext agentInstanceContext, CreateContextDesc contextDesc, boolean isRecoveringResilient) throws ExprValidationException;
    public int getContextCount();

    public ContextDescriptor getContextDescriptor(String contextName);

    public void addStatement(String contextName, ContextControllerStatementBase statement, boolean isRecoveringResilient) throws ExprValidationException;
    public void stoppedStatement(String contextName, String statementName, String statementId);
    public void destroyedStatement(String contextName, String statementName, String statementId);

    public void destroyedContext(String contextName);

    public ContextManager getContextManager(String contextName);
}
