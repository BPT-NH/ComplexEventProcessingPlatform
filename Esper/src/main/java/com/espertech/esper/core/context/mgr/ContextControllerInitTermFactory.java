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

import com.espertech.esper.client.EventType;
import com.espertech.esper.client.context.ContextPartitionIdentifier;
import com.espertech.esper.client.context.ContextPartitionIdentifierInitiatedTerminated;
import com.espertech.esper.core.context.stmt.*;
import com.espertech.esper.core.service.EPStatementHandle;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.ContextDetailInitiatedTerminated;
import com.espertech.esper.epl.spec.ContextDetailPartitionItem;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecLookupable;
import com.espertech.esper.filter.FilterValueSetParam;
import com.espertech.esper.pattern.MatchedEventMapMeta;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esper.schedule.SchedulingService;
import com.espertech.esper.schedule.TimeProvider;

import java.util.*;

public class ContextControllerInitTermFactory extends ContextControllerFactoryBase implements ContextControllerFactory {

    private final ContextDetailInitiatedTerminated detail;
    private final ContextStateCache stateCache;

    private final ContextStatePathValueBinding binding;

    private Map<String, Object> contextBuiltinProps;
    private MatchedEventMapMeta matchedEventMapMeta;

    public ContextControllerInitTermFactory(ContextControllerFactoryContext factoryContext, ContextDetailInitiatedTerminated detail, ContextStateCache stateCache) {
        super(factoryContext);
        this.detail = detail;
        this.stateCache = stateCache;
        this.binding = stateCache.getBinding(detail);
    }

    public void validateFactory() throws ExprValidationException {
        contextBuiltinProps = ContextPropertyEventType.getInitiatedTerminatedType();
        LinkedHashSet<String> allTags = new LinkedHashSet<String>();
        ContextPropertyEventType.addEndpointTypes(factoryContext.getContextName(), detail.getStart(), contextBuiltinProps, allTags);
        ContextPropertyEventType.addEndpointTypes(factoryContext.getContextName(), detail.getEnd(), contextBuiltinProps, allTags);
        matchedEventMapMeta = new MatchedEventMapMeta(allTags, false);
    }

    public ContextStateCache getStateCache() {
        return stateCache;
    }

    public ContextStatePathValueBinding getBinding() {
        return binding;
    }

    public Map<String, Object> getContextBuiltinProps() {
        return contextBuiltinProps;
    }

    public MatchedEventMapMeta getMatchedEventMapMeta() {
        return matchedEventMapMeta;
    }

    public ContextControllerStatementCtxCache validateStatement(ContextControllerStatementBase statement) {
        return null;
    }

    public ContextController createNoCallback(int pathId, ContextControllerLifecycleCallback callback) {
        return new ContextControllerInitTerm(pathId, callback, this);
    }

    public void populateFilterAddendums(IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> filterAddendum, ContextControllerStatementDesc statement, Object key, int contextId) {
    }

    public FilterSpecLookupable getFilterLookupable(EventType eventType) {
        return null;
    }

    public ContextDetailInitiatedTerminated getContextDetail() {
        return detail;
    }

    public List<ContextDetailPartitionItem> getContextDetailPartitionItems() {
        return Collections.emptyList();
    }

    public StatementAIResourceRegistryFactory getStatementAIResourceRegistryFactory() {
        if (detail.isOverlapping()) {
            return new StatementAIResourceRegistryFactory() {
                public StatementAIResourceRegistry make() {
                    return new StatementAIResourceRegistry(new AIRegistryAggregationMultiPerm(), new AIRegistryExprMultiPerm());
                }
            };
        }
        else {
            return new StatementAIResourceRegistryFactory() {
                public StatementAIResourceRegistry make() {
                    return new StatementAIResourceRegistry(new AIRegistryAggregationSingle(), new AIRegistryExprSingle());
                }
            };
        }
    }

    public boolean isSingleInstanceContext() {
        return true;
    }

    public ScheduleSlot allocateSlot() {
        return factoryContext.getAgentInstanceContextCreate().getStatementContext().getScheduleBucket().allocateSlot();
    }

    public TimeProvider getTimeProvider() {
        return factoryContext.getAgentInstanceContextCreate().getStatementContext().getTimeProvider();
    }

    public SchedulingService getSchedulingService() {
        return factoryContext.getAgentInstanceContextCreate().getStatementContext().getSchedulingService();
    }

    public EPStatementHandle getEpStatementHandle() {
        return factoryContext.getAgentInstanceContextCreate().getStatementContext().getEpStatementHandle();
    }

    public StatementContext getStatementContext() {
        return factoryContext.getAgentInstanceContextCreate().getStatementContext();
    }

    public ContextPartitionIdentifier keyPayloadToIdentifier(Object payload) {
        ContextControllerInitTermState state = (ContextControllerInitTermState) payload;
        return new ContextPartitionIdentifierInitiatedTerminated(state.getPatternData(), state.getStartTime(), null);
    }
}
