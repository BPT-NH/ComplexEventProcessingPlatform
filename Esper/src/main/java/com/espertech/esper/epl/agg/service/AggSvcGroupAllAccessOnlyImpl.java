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
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Collection;

/**
 * Aggregation service for use when only first/last/window aggregation functions are used an none other.
 */
public class AggSvcGroupAllAccessOnlyImpl implements AggregationService, AggregationResultFuture
{
    private final AggregationAccessorSlotPair[] accessors;
    protected final AggregationState[] states;

    public AggSvcGroupAllAccessOnlyImpl(AggregationAccessorSlotPair[] accessors, AggregationState[] states) {
        this.accessors = accessors;
        this.states = states;
    }

    public void applyEnter(EventBean[] eventsPerStream, Object groupKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        for (AggregationState state : states) {
            state.applyEnter(eventsPerStream, exprEvaluatorContext);
        }
    }

    public void applyLeave(EventBean[] eventsPerStream, Object groupKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        for (AggregationState state : states) {
            state.applyLeave(eventsPerStream, exprEvaluatorContext);
        }
    }

    public void setCurrentAccess(Object groupKey, int agentInstanceId)
    {
        // no implementation required
    }

    public Object getValue(int column, int agentInstanceId)
    {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getValue(states[pair.getSlot()]);
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getEnumerableEvent(states[pair.getSlot()]);
    }

    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context)
    {
        AggregationAccessorSlotPair pair = accessors[column];
        return pair.getAccessor().getEnumerableEvents(states[pair.getSlot()]);
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        for (AggregationState state : states) {
            state.clear();
        }
    }

    public void setRemovedCallback(AggregationRowRemovedCallback callback) {
        // not applicable
    }

}