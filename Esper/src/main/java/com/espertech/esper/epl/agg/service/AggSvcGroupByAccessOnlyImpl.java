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

package com.espertech.esper.epl.agg.service;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.agg.access.AggregationState;
import com.espertech.esper.epl.agg.access.AggregationAccessorSlotPair;
import com.espertech.esper.epl.core.MethodResolutionService;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Aggregation service for use when only first/last/window aggregation functions are used an none other.
 */
public class AggSvcGroupByAccessOnlyImpl implements AggregationService, AggregationResultFuture
{
    private final MethodResolutionService methodResolutionService;
    private final Map<Object, AggregationState[]> accessMap;
    private final AggregationAccessorSlotPair[] accessors;
    private final AggregationStateFactory[] accessAggSpecs;
    private final boolean isJoin;

    private AggregationState[] currentAcceses;

    /**
     * Ctor.
     * @param methodResolutionService factory service for implementations
     * @param accessors accessor definitions
     * @param accessAggSpecs access agg specs
     * @param isJoin true for join, false for single-stream
     */
    public AggSvcGroupByAccessOnlyImpl(MethodResolutionService methodResolutionService,
                                                   AggregationAccessorSlotPair[] accessors,
                                                   AggregationStateFactory[] accessAggSpecs,
                                                   boolean isJoin)
    {
        this.methodResolutionService = methodResolutionService;
        this.accessMap = new HashMap<Object, AggregationState[]>();
        this.accessors = accessors;
        this.accessAggSpecs = accessAggSpecs;
        this.isJoin = isJoin;
    }

    public void applyEnter(EventBean[] eventsPerStream, Object groupKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationState[] row = getAssertRow(exprEvaluatorContext.getAgentInstanceId(), groupKey);
        for (AggregationState state : row) {
            state.applyEnter(eventsPerStream, exprEvaluatorContext);
        }
    }

    public void applyLeave(EventBean[] eventsPerStream, Object groupKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationState[] row = getAssertRow(exprEvaluatorContext.getAgentInstanceId(), groupKey);
        for (AggregationState state : row) {
            state.applyLeave(eventsPerStream, exprEvaluatorContext);
        }
    }

    public void setCurrentAccess(Object groupKey, int agentInstanceId)
    {
        currentAcceses = getAssertRow(agentInstanceId, groupKey);
    }

    public Object getValue(int column, int agentInstanceId)
    {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getValue(currentAcceses[pair.getSlot()]);
    }

    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context) {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getEnumerableEvents(currentAcceses[pair.getSlot()]);
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getEnumerableEvent(currentAcceses[pair.getSlot()]);
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        accessMap.clear();
    }

    private AggregationState[] getAssertRow(int agentInstanceId, Object groupKey) {
        AggregationState[] row = accessMap.get(groupKey);
        if (row != null) {
            return row;
        }

        row = methodResolutionService.newAccesses(agentInstanceId, isJoin, accessAggSpecs, groupKey);
        accessMap.put(groupKey, row);
        return row;
    }

    public void setRemovedCallback(AggregationRowRemovedCallback callback) {
        // not applicable
    }

}
