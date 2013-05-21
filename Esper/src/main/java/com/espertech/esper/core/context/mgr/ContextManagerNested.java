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

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.context.*;
import com.espertech.esper.core.context.factory.StatementAgentInstanceFactoryResult;
import com.espertech.esper.core.context.stmt.AIRegistryAggregationMap;
import com.espertech.esper.core.context.stmt.AIRegistryExprMap;
import com.espertech.esper.core.context.stmt.StatementAIResourceRegistry;
import com.espertech.esper.core.context.stmt.StatementAIResourceRegistryFactory;
import com.espertech.esper.core.context.util.ContextControllerSelectorUtil;
import com.espertech.esper.core.context.util.ContextDescriptor;
import com.espertech.esper.core.context.util.ContextIteratorHandler;
import com.espertech.esper.core.context.util.StatementAgentInstanceUtil;
import com.espertech.esper.client.context.ContextPartitionDescriptor;
import com.espertech.esper.client.context.ContextPartitionState;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.ContextDetailPartitionItem;
import com.espertech.esper.event.MappedEventBean;
import com.espertech.esper.filter.FilterFaultHandler;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecLookupable;
import com.espertech.esper.filter.FilterValueSetParam;
import com.espertech.esper.type.NumberSetParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class ContextManagerNested implements ContextManager, ContextControllerLifecycleCallback, ContextIteratorHandler, FilterFaultHandler {
    private static final Log log = LogFactory.getLog(ContextManagerNested.class);

    private final String contextName;
    private final EPServicesContext servicesContext;

    private final ContextControllerFactory[] nestedContextFactories;
    private final Map<String, ContextControllerStatementDesc> statements = new LinkedHashMap<String, ContextControllerStatementDesc>(); // retain order of statement creation
    private final ContextDescriptor contextDescriptor;

    /**
     * The single root context.
     * This represents the context declared first.
     */
    private ContextController rootContext;

    /**
     * Double-linked tree of sub-contexts.
     * An entry exists for all branches including the root. For example with 2 contexts declared this map has entries representing the root and all second-level sub-contexts.
     * For example with 3 contexts declared this map has entries for the root, second and third-level contexts.
     */
    private final Map<ContextController, ContextControllerTreeEntry> subcontexts = new HashMap<ContextController, ContextControllerTreeEntry>();

    private final ContextPartitionIdManager contextPartitionIdManager;

    public ContextManagerNested(ContextControllerFactoryServiceContext factoryServiceContext)
        throws ExprValidationException
    {
        this.contextName = factoryServiceContext.getContextName();
        this.servicesContext = factoryServiceContext.getServicesContext();
        this.contextPartitionIdManager = factoryServiceContext.getAgentInstanceContextCreate().getStatementContext().getContextControllerFactoryService().allocatePartitionIdMgr(contextName, factoryServiceContext.getAgentInstanceContextCreate().getStatementContext().getStatementId());

        nestedContextFactories = factoryServiceContext.getAgentInstanceContextCreate().getStatementContext().getContextControllerFactoryService().getFactory(factoryServiceContext);

        StatementAIResourceRegistryFactory resourceRegistryFactory = new StatementAIResourceRegistryFactory() {
            public StatementAIResourceRegistry make() {
                return new StatementAIResourceRegistry(new AIRegistryAggregationMap(), new AIRegistryExprMap());
            }
        };

        Map<String, Object> contextProps = ContextPropertyEventType.getNestedTypeBase();
        for (ContextControllerFactory factory : nestedContextFactories) {
            contextProps.put(factory.getFactoryContext().getContextName(), factory.getContextBuiltinProps());
        }
        EventType contextPropsType = servicesContext.getEventAdapterService().createAnonymousMapType(contextName, contextProps);
        ContextPropertyRegistryImpl registry = new ContextPropertyRegistryImpl(Collections.<ContextDetailPartitionItem>emptyList(), contextPropsType);
        contextDescriptor = new ContextDescriptor(contextName, false, registry, resourceRegistryFactory, this, factoryServiceContext.getDetail());
    }

    public Map<String, ContextControllerStatementDesc> getStatements() {
        return statements;
    }

    public ContextDescriptor getContextDescriptor() {
        return contextDescriptor;
    }

    public int getNumNestingLevels() {
        return nestedContextFactories.length;
    }

    public synchronized Iterator<EventBean> iterator(String statementId, ContextPartitionSelector selector) {
        AgentInstance[] instances = getAgentInstancesForStmt(statementId, selector);
        return new AgentInstanceArrayIterator(instances);
    }

    public synchronized SafeIterator<EventBean> safeIterator(String statementId, ContextPartitionSelector selector) {
        AgentInstance[] instances = getAgentInstancesForStmt(statementId, selector);
        return new AgentInstanceArraySafeIterator(instances);
    }

    public Collection<Integer> getAgentInstanceIds(ContextPartitionSelector contextPartitionSelector) {
        return getAgentInstancesForSelector(contextPartitionSelector);
    }

    public void importStartPaths(ContextControllerState state, AgentInstanceSelector agentInstanceSelector) {
        rootContext.importContextPartitions(state, 0, null, agentInstanceSelector);
    }

    protected static ContextPartitionIdentifier[] getTreeCompositeKey(
                                                  ContextControllerFactory[] nestedContextFactories,
                                                  Object initPartitionKey,
                                                  ContextControllerTreeEntry treeEntry,
                                                  Map<ContextController, ContextControllerTreeEntry> subcontexts) {
        int length = nestedContextFactories.length;
        ContextPartitionIdentifier[] keys = new ContextPartitionIdentifier[length];
        keys[length - 1] = nestedContextFactories[length - 1].keyPayloadToIdentifier(initPartitionKey);
        keys[length - 2] = nestedContextFactories[length - 2].keyPayloadToIdentifier(treeEntry.getInitPartitionKey());

        // get parent's parent
        if (length > 2) {
            ContextController parent = treeEntry.getParent();
            ContextControllerTreeEntry parentEntry = subcontexts.get(parent);
            for (int i = 0; i < length - 2; i++) {
                keys[length - 2 - i] = nestedContextFactories[length - 2 - i].keyPayloadToIdentifier(parentEntry.getInitPartitionKey());
                parent = parentEntry.getParent();
                parentEntry = subcontexts.get(parent);
            }
        }

        return keys;
    }

    public ContextStatePathDescriptor extractPaths(ContextPartitionSelector selector) {
        ContextPartitionVisitorStateWithPath visitor = getContextPartitionPathsInternal(selector);
        return new ContextStatePathDescriptor(visitor.getStates(), visitor.getAgentInstanceInfo());
    }

    public ContextStatePathDescriptor extractStopPaths(ContextPartitionSelector selector) {
        ContextPartitionVisitorStateWithPath visitor = getContextPartitionPathsInternal(selector);
        for (Map.Entry<ContextController, List<ContextPartitionVisitorStateWithPath.LeafDesc>> entry : visitor.getControllerAgentInstances().entrySet()) {
            ContextControllerTreeEntry treeEntry = subcontexts.get(entry.getKey());
            for (ContextPartitionVisitorStateWithPath.LeafDesc leaf : entry.getValue()) {
                int agentInstanceId = leaf.getValue().getOptionalContextPartitionId();
                ContextControllerTreeAgentInstanceList list = treeEntry.getAgentInstances().get(agentInstanceId);
                list.setState(ContextPartitionState.STOPPED);
                StatementAgentInstanceUtil.stopAgentInstances(list.getAgentInstances(), null, servicesContext, false);
                list.clearAgentInstances();
                leaf.getValue().setState(ContextPartitionState.STOPPED);
                rootContext.getFactory().getStateCache().updateContextPath(contextName, leaf.getKey(), leaf.getValue());
            }
        }
        return new ContextStatePathDescriptor(visitor.getStates(), visitor.getAgentInstanceInfo());
    }

    public ContextStatePathDescriptor extractDestroyPaths(ContextPartitionSelector selector) {
        ContextPartitionVisitorStateWithPath visitor = getContextPartitionPathsInternal(selector);
        for (Map.Entry<ContextController, List<ContextPartitionVisitorStateWithPath.LeafDesc>> entry : visitor.getControllerAgentInstances().entrySet()) {
            ContextControllerTreeEntry treeEntry = subcontexts.get(entry.getKey());
            for (ContextPartitionVisitorStateWithPath.LeafDesc leaf : entry.getValue()) {
                int agentInstanceId = leaf.getValue().getOptionalContextPartitionId();
                ContextControllerTreeAgentInstanceList list = treeEntry.getAgentInstances().get(agentInstanceId);
                StatementAgentInstanceUtil.stopAgentInstances(list.getAgentInstances(), null, servicesContext, false);
                rootContext.getFactory().getStateCache().removeContextPath(contextName, leaf.getKey().getLevel(), leaf.getKey().getParentPath(), leaf.getKey().getSubPath());
                ContextPartitionDescriptor descriptor = visitor.getAgentInstanceInfo().get(agentInstanceId);
                ContextPartitionIdentifierNested nestedIdent = (ContextPartitionIdentifierNested) descriptor.getIdentifier();
                entry.getKey().deletePath(nestedIdent.getIdentifiers()[nestedContextFactories.length - 1]);
            }
        }
        return new ContextStatePathDescriptor(visitor.getStates(), visitor.getAgentInstanceInfo());
    }

    public Map<Integer,ContextPartitionDescriptor> startPaths(ContextPartitionSelector selector) {
        ContextPartitionVisitorStateWithPath visitor = getContextPartitionPathsInternal(selector);
        for (Map.Entry<ContextController, List<ContextPartitionVisitorStateWithPath.LeafDesc>> entry : visitor.getControllerAgentInstances().entrySet()) {
            ContextControllerTreeEntry treeEntry = subcontexts.get(entry.getKey());

            for (ContextPartitionVisitorStateWithPath.LeafDesc leaf : entry.getValue()) {
                int agentInstanceId = leaf.getValue().getOptionalContextPartitionId();
                ContextControllerTreeAgentInstanceList list = treeEntry.getAgentInstances().get(agentInstanceId);
                if (list.getState() == ContextPartitionState.STARTED) {
                    continue;
                }
                for (Map.Entry<String, ContextControllerStatementDesc> statement : statements.entrySet()) {
                    AgentInstance instance = startStatement(agentInstanceId, statement.getValue(), rootContext, list.getInitPartitionKey(), list.getInitContextProperties(), false);
                    list.getAgentInstances().add(instance);
                }
                list.setState(ContextPartitionState.STARTED);
                leaf.getValue().setState(ContextPartitionState.STARTED);
                rootContext.getFactory().getStateCache().updateContextPath(contextName, leaf.getKey(), leaf.getValue());
            }
        }
        ContextManagerImpl.setState(visitor.getAgentInstanceInfo(), ContextPartitionState.STARTED);
        return visitor.getAgentInstanceInfo();
    }

    public ContextPartitionVisitorStateWithPath getContextPartitionPathsInternal(ContextPartitionSelector selector) {
        ContextPartitionVisitorStateWithPath visitor = new ContextPartitionVisitorStateWithPath(nestedContextFactories, subcontexts);
        ContextPartitionSelectorNested nested = (ContextPartitionSelectorNested) selector;
        for (ContextPartitionSelector[] item : nested.getSelectors()) {
            recursivePopulateSelector(rootContext, 1, item, visitor);
        }
        return visitor;
    }

    public void addStatement(ContextControllerStatementBase statement, boolean isRecoveringResilient) throws ExprValidationException {

        // validation down the hierarchy
        ContextControllerStatementCtxCache[] caches = new ContextControllerStatementCtxCache[nestedContextFactories.length];
        for (int i = 0; i < nestedContextFactories.length; i++) {
            ContextControllerFactory nested = nestedContextFactories[i];
            caches[i] = nested.validateStatement(statement);
        }

        // save statement
        ContextControllerStatementDesc desc = new ContextControllerStatementDesc(statement, caches);
        statements.put(statement.getStatementContext().getStatementId(), desc);

        // activate if this is the first statement
        if (statements.size() == 1) {
            activate();     // this may itself trigger a callback
        }
        // activate statement in respect to existing context partitions
        else {
            for (Map.Entry<ContextController, ContextControllerTreeEntry> subcontext : subcontexts.entrySet()) {
                if (subcontext.getKey().getFactory().getFactoryContext().getNestingLevel() != nestedContextFactories.length) {
                    continue;
                }
                if (subcontext.getValue().getAgentInstances() == null || subcontext.getValue().getAgentInstances().isEmpty()) {
                    continue;
                }

                for (Map.Entry<Integer, ContextControllerTreeAgentInstanceList> entry : subcontext.getValue().getAgentInstances().entrySet()) {
                    if (entry.getValue().getState() == ContextPartitionState.STARTED) {
                        AgentInstance agentInstance = startStatement(entry.getKey(), desc, subcontext.getKey(), entry.getValue().getInitPartitionKey(), entry.getValue().getInitContextProperties(), isRecoveringResilient);
                        entry.getValue().getAgentInstances().add(agentInstance);
                    }
                }
            }
        }
    }

    public synchronized void stopStatement(String statementName, String statementId) {
        destroyStatement(statementName, statementId);
    }

    public synchronized void destroyStatement(String statementName, String statementId) {
        if (!statements.containsKey(statementId)) {
            return;
        }
        if (statements.size() == 1) {
            safeDestroy();
        }
        else {
            removeStatement(statementId);
        }
    }

    public void safeDestroy() {
        if (rootContext != null) {
            recursiveDeactivateStop(rootContext);
            nestedContextFactories[0].getStateCache().removeContext(contextName);
            rootContext = null;
            statements.clear();
            subcontexts.clear();
            contextPartitionIdManager.clear();
        }
    }

    public void setContextPartitionRange(List<NumberSetParameter> partitionRanges) {
        throw new UnsupportedOperationException();
    }

    public FilterSpecLookupable getFilterLookupable(EventType eventType) {
        throw new UnsupportedOperationException();
    }

    public void contextPartitionNavigate(ContextControllerInstanceHandle existingHandle, ContextController originator, ContextControllerState controllerState, int exportedCPOrPathId, ContextInternalFilterAddendum filterAddendum, AgentInstanceSelector agentInstanceSelector, byte[] payload) {
        ContextManagerNestedInstanceHandle nestedHandle = (ContextManagerNestedInstanceHandle) existingHandle;

        // detect non-leaf
        int nestingLevel = originator.getFactory().getFactoryContext().getNestingLevel();   // starts at 1 for root
        if (nestingLevel < nestedContextFactories.length) {
            nestedHandle.getController().importContextPartitions(controllerState, exportedCPOrPathId, filterAddendum, agentInstanceSelector);
            return;
        }

        ContextControllerTreeEntry entry = subcontexts.get(originator);
        if (entry == null) {
            return;
        }
        for (Map.Entry<Integer, ContextControllerTreeAgentInstanceList> cpEntry : entry.getAgentInstances().entrySet()) {

            if (cpEntry.getValue().getState() == ContextPartitionState.STOPPED) {
                cpEntry.getValue().setState(ContextPartitionState.STARTED);
                entry.getAgentInstances().clear();
                for (Map.Entry<String, ContextControllerStatementDesc> statement : statements.entrySet()) {
                    AgentInstance instance = startStatement(existingHandle.getContextPartitionOrPathId(), statement.getValue(), originator, cpEntry.getValue().getInitPartitionKey(), entry.getInitContextProperties(), false);
                    cpEntry.getValue().getAgentInstances().add(instance);
                }
                ContextStatePathKey key = new ContextStatePathKey(nestedContextFactories.length, originator.getPathId(), existingHandle.getSubPathId());
                ContextStatePathValue value = new ContextStatePathValue(existingHandle.getContextPartitionOrPathId(), payload, ContextPartitionState.STARTED);
                originator.getFactory().getStateCache().updateContextPath(contextName, key, value);
            }
            else {
                List<AgentInstance> removed = new ArrayList<AgentInstance>(2);
                List<AgentInstance> added = new ArrayList<AgentInstance>(2);
                List<AgentInstance> current = cpEntry.getValue().getAgentInstances();

                for (AgentInstance agentInstance : current) {
                    if (!agentInstanceSelector.select(agentInstance)) {
                        continue;
                    }

                    // remove
                    StatementAgentInstanceUtil.stopAgentInstance(agentInstance, null, servicesContext, false);
                    removed.add(agentInstance);

                    // start
                    ContextControllerStatementDesc statementDesc = statements.get(agentInstance.getAgentInstanceContext().getStatementId());
                    AgentInstance instance = startStatement(cpEntry.getKey(), statementDesc, originator, cpEntry.getValue().getInitPartitionKey(), entry.getInitContextProperties(), false);
                    added.add(instance);

                    if (controllerState.getPartitionImportCallback() != null) {
                        controllerState.getPartitionImportCallback().existing(existingHandle.getContextPartitionOrPathId(), exportedCPOrPathId);
                    }
                }
                current.removeAll(removed);
                current.addAll(added);
            }
        }
    }

    public synchronized ContextControllerInstanceHandle contextPartitionInstantiate(
            Integer optionalContextPartitionId,
            int subPathId,
            Integer importSubpathId,
            ContextController originator,
            EventBean optionalTriggeringEvent,
            Map<String, Object> optionalTriggeringPattern,
            Object partitionKey,
            Map<String, Object> contextProperties,
            ContextControllerState states,
            ContextInternalFilterAddendum filterAddendum,
            boolean isRecoveringResilient,
            ContextPartitionState state) {

        // detect non-leaf
        int nestingLevel = originator.getFactory().getFactoryContext().getNestingLevel();   // starts at 1 for root
        if (nestingLevel < nestedContextFactories.length) {

            // next sub-sontext
            ContextControllerFactory nextFactory = nestedContextFactories[originator.getFactory().getFactoryContext().getNestingLevel()];
            ContextController nextContext = nextFactory.createNoCallback(subPathId, this);

            // link current context to sub-context
            ContextControllerTreeEntry branch = subcontexts.get(originator);
            if (branch.getChildContexts() == null) {
                branch.setChildContexts(new HashMap<Integer, ContextController>());
            }
            branch.getChildContexts().put(subPathId, nextContext);

            // save child branch, linking sub-context to its parent
            ContextControllerTreeEntry entry = new ContextControllerTreeEntry(originator, null, partitionKey, contextProperties);
            subcontexts.put(nextContext, entry);

            // now post-initialize, this may actually call back
            nextContext.activate(optionalTriggeringEvent, optionalTriggeringPattern, states, filterAddendum, importSubpathId);

            if (log.isDebugEnabled()) {
                log.debug("Instantiating branch context path for " + contextName +
                        " from level " + originator.getFactory().getFactoryContext().getNestingLevel() +
                        "(" + originator.getFactory().getFactoryContext().getContextName() + ")" +
                        " parentPath " + originator.getPathId() +
                        " for level " + nextContext.getFactory().getFactoryContext().getNestingLevel() +
                        "(" + nextContext.getFactory().getFactoryContext().getContextName() + ")" +
                        " childPath " + subPathId
                        );
            }

            return new ContextManagerNestedInstanceHandle(subPathId, nextContext, subPathId, true, null);
        }

        // assign context id
        int assignedContextId;
        if (optionalContextPartitionId != null && !states.isImported()) {
            assignedContextId = optionalContextPartitionId;
            contextPartitionIdManager.addExisting(optionalContextPartitionId);
        } else {
            assignedContextId = contextPartitionIdManager.allocateId();
            if (states != null && states.getPartitionImportCallback() != null && optionalContextPartitionId != null) {
                states.getPartitionImportCallback().allocated(assignedContextId, optionalContextPartitionId);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Instantiating agent instance for " + contextName +
                    " from level " + originator.getFactory().getFactoryContext().getNestingLevel() +
                    "(" + originator.getFactory().getFactoryContext().getContextName() + ")" +
                    " parentPath " + originator.getPathId() +
                    " contextPartId " + assignedContextId);
        }

        // handle leaf creation
        List<AgentInstance> newInstances = new ArrayList<AgentInstance>();
        if (state == ContextPartitionState.STARTED) {
            for (Map.Entry<String, ContextControllerStatementDesc> statementEntry : statements.entrySet()) {
                ContextControllerStatementDesc statementDesc = statementEntry.getValue();
                AgentInstance instance = startStatement(assignedContextId, statementDesc, originator, partitionKey, contextProperties, isRecoveringResilient);
                newInstances.add(instance);
            }
        }

        // for all new contexts: evaluate this event for this statement
        if (optionalTriggeringEvent != null) {
            for (AgentInstance context : newInstances) {
                StatementAgentInstanceUtil.evaluateEventForStatement(servicesContext, optionalTriggeringEvent, optionalTriggeringPattern, context.getAgentInstanceContext());
            }
        }

        // save leaf
        ContextControllerTreeEntry entry = subcontexts.get(originator);
        if (entry.getAgentInstances() == null) {
            entry.setAgentInstances(new LinkedHashMap<Integer, ContextControllerTreeAgentInstanceList>());
        }

        long filterVersion = servicesContext.getFilterService().getFiltersVersion();
        ContextControllerTreeAgentInstanceList agentInstanceList = new ContextControllerTreeAgentInstanceList(filterVersion, partitionKey, contextProperties, newInstances, state);
        entry.getAgentInstances().put(assignedContextId, agentInstanceList);

        return new ContextManagerNestedInstanceHandle(subPathId, originator, assignedContextId, false, agentInstanceList);
    }

    public synchronized void handleFilterFault(EventBean theEvent, long version) {
        for (Map.Entry<ContextController, ContextControllerTreeEntry> entry : subcontexts.entrySet()) {
            if (entry.getValue().getAgentInstances() != null) {
                StatementAgentInstanceUtil.handleFilterFault(theEvent, version, servicesContext, entry.getValue().getAgentInstances());
            }
        }
    }

    /**
     * Provides the sub-context that ends.
     */
    public synchronized void contextPartitionTerminate(ContextControllerInstanceHandle contextNestedHandle, Map<String, Object> terminationProperties) {
        ContextManagerNestedInstanceHandle handle = (ContextManagerNestedInstanceHandle) contextNestedHandle;
        if (handle.isBranch()) {

            ContextManagerNestedInstanceHandle branchHandle = handle;
            ContextController branch = branchHandle.getController();
            recursiveDeactivateStop(branch);

            if (log.isDebugEnabled()) {
                log.debug("Terminated context branch for " + contextName +
                        " from level " + branch.getFactory().getFactoryContext().getNestingLevel() +
                        "(" + branch.getFactory().getFactoryContext().getContextName() + ")" +
                        " parentPath " + branch.getPathId());
            }
        }
        else {
            ContextManagerNestedInstanceHandle leafHandle = handle;
            ContextController leaf = leafHandle.getController();
            ContextControllerTreeEntry leafEntry = subcontexts.get(leaf);
            if (leafEntry != null) { // could be terminated earlier
                ContextControllerTreeAgentInstanceList ailist = leafEntry.getAgentInstances().get(leafHandle.getContextPartitionOrPathId());
                if (ailist != null) {
                    StatementAgentInstanceUtil.stopAgentInstances(ailist.getAgentInstances(), null, servicesContext, false);
                    contextPartitionIdManager.removeId(leafHandle.getContextPartitionOrPathId());
                    ailist.getAgentInstances().clear();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Terminated context leaf for " + contextName +
                            " from level " + leaf.getFactory().getFactoryContext().getNestingLevel() +
                            "(" + leaf.getFactory().getFactoryContext().getContextName() + ")" +
                            " parentPath " + leaf.getPathId() +
                            " contextPartId " + leafHandle.getContextPartitionOrPathId());
                }
            }
        }
    }

    public synchronized Iterator<EventBean> iterator(String statementId) {
        AgentInstance[] instances = getAgentInstancesForStmt(statementId);
        return new AgentInstanceArrayIterator(instances);
    }

    public synchronized SafeIterator<EventBean> safeIterator(String statementId) {
        AgentInstance[] instances = getAgentInstancesForStmt(statementId);
        return new AgentInstanceArraySafeIterator(instances);
    }

    private AgentInstance startStatement(int contextId, ContextControllerStatementDesc statementDesc, ContextController originator, Object partitionKey, Map<String, Object> contextProperties, boolean isRecoveringResilient) {

        // build filters
        AgentInstanceFilterProxy proxy = getMergedFilterAddendums(statementDesc, originator, partitionKey, contextId);

        // build built-in context properties
        Map<String, Object> properties = ContextPropertyEventType.getNestedBeanBase(contextName, contextId);
        properties.put(nestedContextFactories[nestedContextFactories.length - 1].getFactoryContext().getContextName(), contextProperties);
        recursivePopulateBuiltinProps(originator, properties);
        properties.put(ContextPropertyEventType.PROP_CTX_NAME, contextName);
        properties.put(ContextPropertyEventType.PROP_CTX_ID, contextId);
        MappedEventBean contextBean = (MappedEventBean) servicesContext.getEventAdapterService().adapterForTypedMap(properties, contextDescriptor.getContextPropertyRegistry().getContextEventType());

        // activate
        StatementAgentInstanceFactoryResult result = StatementAgentInstanceUtil.start(servicesContext, statementDesc.getStatement(), false, contextId, contextBean, proxy, isRecoveringResilient);
        return new AgentInstance(result.getStopCallback(), result.getAgentInstanceContext(), result.getFinalView());
    }

    private void recursivePopulateBuiltinProps(ContextController originator, Map<String, Object> properties) {
        ContextControllerTreeEntry entry = subcontexts.get(originator);
        if (entry.getInitContextProperties() != null) {
            properties.put(entry.getParent().getFactory().getFactoryContext().getContextName(), entry.getInitContextProperties());
        }
        if (entry.getParent() != null && entry.getParent().getFactory().getFactoryContext().getNestingLevel() > 1) {
            recursivePopulateBuiltinProps(entry.getParent(), properties);
        }
    }

    private AgentInstanceFilterProxy getMergedFilterAddendums(ContextControllerStatementDesc statement,
                                                              ContextController originator,
                                                              Object partitionKey,
                                                              int contextId) {

        IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> result = new IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]>();
        originator.getFactory().populateFilterAddendums(result, statement, partitionKey, contextId);
        ContextControllerTreeEntry originatorEntry = subcontexts.get(originator);
        if (originatorEntry != null) {
            recursivePopulateFilterAddendum(statement, originatorEntry, contextId, result);
        }
        return new AgentInstanceFilterProxyImpl(result);
    }

    private void recursivePopulateFilterAddendum(ContextControllerStatementDesc statement, ContextControllerTreeEntry originatorEntry, int contextId, IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> result) {

        if (originatorEntry.getParent() == null) {
            return;
        }
        originatorEntry.getParent().getFactory().populateFilterAddendums(result, statement, originatorEntry.getInitPartitionKey(), contextId);

        ContextControllerTreeEntry parentEntry = subcontexts.get(originatorEntry.getParent());
        if (parentEntry != null) {
            recursivePopulateFilterAddendum(statement, parentEntry, contextId, result);
        }
    }

    private void activate() {

        rootContext = nestedContextFactories[0].createNoCallback(0, this);
        subcontexts.put(rootContext, new ContextControllerTreeEntry(null, null, null, null));
        rootContext.activate(null, null, null, null, null);
    }

    private void removeStatement(String statementId) {
        ContextControllerStatementDesc statementDesc = statements.get(statementId);
        if (statementDesc == null) {
            return;
        }

        for (Map.Entry<ContextController, ContextControllerTreeEntry> entry : subcontexts.entrySet()) {
            // ignore branches
            if (entry.getKey().getFactory().getFactoryContext().getNestingLevel() < nestedContextFactories.length) {
                continue;
            }
            if (entry.getValue().getAgentInstances() == null || entry.getValue().getAgentInstances().isEmpty()) {
                continue;
            }

            for (Map.Entry<Integer, ContextControllerTreeAgentInstanceList> contextPartitionEntry : entry.getValue().getAgentInstances().entrySet()) {
                Iterator<AgentInstance> instanceIt = contextPartitionEntry.getValue().getAgentInstances().iterator();
                for (;instanceIt.hasNext();) {
                    AgentInstance instance = instanceIt.next();
                    if (!instance.getAgentInstanceContext().getStatementContext().getStatementId().equals(statementId)) {
                        continue;
                    }
                    StatementAgentInstanceUtil.stop(instance.getStopCallback(), instance.getAgentInstanceContext(), instance.getFinalView(), servicesContext, true);
                    instanceIt.remove();
                }
            }
        }

        statements.remove(statementId);
    }

    private void recursiveDeactivateStop(ContextController currentContext) {

        // deactivate
        currentContext.deactivate();

        // remove state
        ContextControllerTreeEntry entry = subcontexts.remove(currentContext);
        if (entry == null) {
            return;
        }

        // remove from parent
        ContextControllerTreeEntry parent = subcontexts.get(entry.getParent());
        if (parent != null) {
            parent.getChildContexts().remove(currentContext.getPathId());
        }

        // stop instances
        if (entry.getAgentInstances() != null) {
            for (Map.Entry<Integer, ContextControllerTreeAgentInstanceList> entryCP : entry.getAgentInstances().entrySet()) {
                StatementAgentInstanceUtil.stopAgentInstances(entryCP.getValue().getAgentInstances(), null, servicesContext, false);
                contextPartitionIdManager.removeId(entryCP.getKey());
            }
        }

        // deactivate child contexts
        if (entry.getChildContexts() == null || entry.getChildContexts().isEmpty()) {
            return;
        }
        for (ContextController inner : entry.getChildContexts().values()) {
            recursiveDeactivateStop(inner);
        }
    }

    private AgentInstance[] getAgentInstancesForStmt(String statementId) {
        List<AgentInstance> instances = new ArrayList<AgentInstance>();
        for (Map.Entry<ContextController, ContextControllerTreeEntry> subcontext : subcontexts.entrySet()) {
            if (subcontext.getKey().getFactory().getFactoryContext().getNestingLevel() != nestedContextFactories.length) {
                continue;
            }
            if (subcontext.getValue().getAgentInstances() == null || subcontext.getValue().getAgentInstances().isEmpty()) {
                continue;
            }

            for (Map.Entry<Integer, ContextControllerTreeAgentInstanceList> entry : subcontext.getValue().getAgentInstances().entrySet()) {
                for (AgentInstance ai : entry.getValue().getAgentInstances()) {
                    if (ai.getAgentInstanceContext().getStatementContext().getStatementId().equals(statementId)) {
                        instances.add(ai);
                    }
                }
            }
        }
        return instances.toArray(new AgentInstance[instances.size()]);
    }

    private AgentInstance[] getAgentInstancesForStmt(String statementId, ContextPartitionSelector selector) {
        Collection<Integer> agentInstanceIds = getAgentInstancesForSelector(selector);
        if (agentInstanceIds == null || agentInstanceIds.isEmpty()) {
            return new AgentInstance[0];
        }

        List<AgentInstance> instances = new ArrayList<AgentInstance>(agentInstanceIds.size());
        for (Map.Entry<ContextController, ContextControllerTreeEntry> subcontext : subcontexts.entrySet()) {
            if (subcontext.getKey().getFactory().getFactoryContext().getNestingLevel() != nestedContextFactories.length) {
                continue;
            }
            if (subcontext.getValue().getAgentInstances() == null || subcontext.getValue().getAgentInstances().isEmpty()) {
                continue;
            }

            for (Integer agentInstanceId : agentInstanceIds) {
                ContextControllerTreeAgentInstanceList instancesList = subcontext.getValue().getAgentInstances().get(agentInstanceId);
                if (instancesList != null) {
                    Iterator<AgentInstance> instanceIt = instancesList.getAgentInstances().iterator();
                    for (; instanceIt.hasNext(); ) {
                        AgentInstance instance = instanceIt.next();
                        if (instance.getAgentInstanceContext().getStatementContext().getStatementId().equals(statementId)) {
                            instances.add(instance);
                        }
                    }
                }
            }
        }
        return instances.toArray(new AgentInstance[instances.size()]);
    }

    private Collection<Integer> getAgentInstancesForSelector(ContextPartitionSelector selector) {
        if (selector instanceof ContextPartitionSelectorById) {
            ContextPartitionSelectorById byId = (ContextPartitionSelectorById) selector;
            Set<Integer> ids = byId.getContextPartitionIds();
            if (ids == null || ids.isEmpty()) {
                return Collections.emptyList();
            }
            ArrayList agentInstanceIds = new ArrayList<Integer>(ids);
            agentInstanceIds.retainAll(contextPartitionIdManager.getIds());
            return agentInstanceIds;
        }
        else if (selector instanceof ContextPartitionSelectorAll) {
            return new ArrayList<Integer>(contextPartitionIdManager.getIds());
        }
        else if (selector instanceof ContextPartitionSelectorNested) {
            ContextPartitionSelectorNested nested = (ContextPartitionSelectorNested) selector;
            ContextPartitionVisitorAgentInstanceIdWPath visitor = new ContextPartitionVisitorAgentInstanceIdWPath(nestedContextFactories.length);
            for (ContextPartitionSelector[] item : nested.getSelectors()) {
                recursivePopulateSelector(rootContext, 1, item, visitor);
            }
            return visitor.getAgentInstanceIds();
        }
        throw ContextControllerSelectorUtil.getInvalidSelector(new Class[] {ContextPartitionSelectorNested.class}, selector, true);
    }

    private void recursivePopulateSelector(ContextController currentContext, int level, ContextPartitionSelector[] selectorStack, ContextPartitionVisitorWithPath visitor) {

        ContextControllerTreeEntry entry = subcontexts.get(currentContext);
        if (entry == null) {
            return;
        }
        ContextPartitionSelector selector = selectorStack[level - 1];

        // handle branch
        if (level < nestedContextFactories.length) {
            visitor.resetSubPaths();
            currentContext.visitSelectedPartitions(selector, visitor);
            Collection<Integer> selectedPaths = new ArrayList<Integer>(visitor.getSubpaths());
            for (Integer path : selectedPaths) {
                ContextController controller = entry.getChildContexts().get(path);
                if (controller != null) {
                    recursivePopulateSelector(controller, level + 1, selectorStack, visitor);
                }
            }
        }
        // handle leaf
        else {
            currentContext.visitSelectedPartitions(selector, visitor);
        }
    }
}
