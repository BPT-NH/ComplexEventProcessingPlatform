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
import com.espertech.esper.epl.spec.ContextDetailNested;
import com.espertech.esper.epl.spec.CreateContextDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextManagementServiceImpl implements ContextManagementService {
    private static final Log log = LogFactory.getLog(ContextManagementServiceImpl.class);

    private final Map<String, ContextManagerEntry> contexts;
    private final Set<String> destroyedContexts = new HashSet<String>();

    public ContextManagementServiceImpl() {
        contexts = new HashMap<String, ContextManagerEntry>();
    }

    public void addContextSpec(EPServicesContext servicesContext, AgentInstanceContext agentInstanceContext, CreateContextDesc contextDesc, boolean isRecoveringResilient) throws ExprValidationException {

        ContextManagerEntry mgr = contexts.get(contextDesc.getContextName());
        if (mgr != null) {
            if (destroyedContexts.contains(contextDesc.getContextName())) {
                throw new ExprValidationException("Context by name '" + contextDesc.getContextName() + "' is still referenced by statements and may not be changed");
            }
            throw new ExprValidationException("Context by name '" + contextDesc.getContextName() + "' already exists");
        }

        ContextControllerFactoryServiceContext factoryServiceContext = new ContextControllerFactoryServiceContext(contextDesc.getContextName(), servicesContext, contextDesc.getContextDetail(), agentInstanceContext, isRecoveringResilient);
        ContextManager contextManager;
        if (contextDesc.getContextDetail() instanceof ContextDetailNested) {
            contextManager = new ContextManagerNested(factoryServiceContext);
        }
        else {
            contextManager = new ContextManagerImpl(factoryServiceContext);
        }

        factoryServiceContext.getAgentInstanceContextCreate().getEpStatementAgentInstanceHandle().setFilterFaultHandler(contextManager);

        contexts.put(contextDesc.getContextName(), new ContextManagerEntry(contextManager));
    }

    public int getContextCount() {
        return contexts.size();
    }

    public ContextDescriptor getContextDescriptor(String contextName) {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            return null;
        }
        return entry.getContextManager().getContextDescriptor();
    }

    public ContextManager getContextManager(String contextName) {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            return null;
        }
        return entry.getContextManager();
    }

    public void addStatement(String contextName, ContextControllerStatementBase statement, boolean isRecoveringResilient) throws ExprValidationException {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            throw new ExprValidationException(getNotDecaredText(contextName));
        }
        entry.addStatement(statement.getStatementContext().getStatementId());
        entry.getContextManager().addStatement(statement, isRecoveringResilient);
    }

    public void destroyedStatement(String contextName, String statementName, String statementId) {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            log.warn("Destroy statement for statement '" + statementName + "' failed to locate corresponding context manager '" + contextName + "'");
            return;
        }
        entry.removeStatement(statementId);
        entry.getContextManager().destroyStatement(statementName, statementId);
        
        if (entry.getStatementCount() == 0 && destroyedContexts.contains(contextName)) {
            destroyContext(contextName, entry);
        }
    }

    public void stoppedStatement(String contextName, String statementName, String statementId) {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            log.warn("Stop statement for statement '" + statementName + "' failed to locate corresponding context manager '" + contextName + "'");
            return;
        }
        try {
            entry.getContextManager().stopStatement(statementName, statementId);
        }
        catch (RuntimeException ex) {
            log.warn("Failed to stop statement '" + statementName + "' as related to context '" + contextName + "': " + ex.getMessage(), ex);
        }
    }

    public void destroyedContext(String contextName) {
        ContextManagerEntry entry = contexts.get(contextName);
        if (entry == null) {
            log.warn("Destroy for context '" + contextName + "' failed to locate corresponding context manager '" + contextName + "'");
            return;
        }
        if (entry.getStatementCount() == 0) {
            destroyContext(contextName, entry);
        }
        else {
            // some remaining statements have references
            destroyedContexts.add(contextName);
        }
    }

    private void destroyContext(String contextName, ContextManagerEntry entry) {
        entry.getContextManager().safeDestroy();
        contexts.remove(contextName);
        destroyedContexts.remove(contextName);
    }

    private String getNotDecaredText(String contextName) {
        return "Context by name '" + contextName + "' has not been declared";
    }

    public static class ContextManagerEntry {
        private final ContextManager contextManager;
        private final Set<String> referringStatements;

        public ContextManagerEntry(ContextManager contextManager) {
            this.contextManager = contextManager;
            this.referringStatements = new HashSet<String>();
        }

        public ContextManager getContextManager() {
            return contextManager;
        }

        public void addStatement(String statementId) {
            referringStatements.add(statementId);
        }

        public int getStatementCount() {
            return referringStatements.size();
        }

        public void removeStatement(String statementId) {
            referringStatements.remove(statementId);
        }
    }
}
