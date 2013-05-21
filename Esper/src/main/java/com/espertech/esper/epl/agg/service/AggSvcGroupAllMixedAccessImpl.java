/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.service;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.agg.access.AggregationState;
import com.espertech.esper.epl.agg.access.AggregationAccessorSlotPair;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Collection;

/**
 * Implementation for handling aggregation without any grouping (no group-by).
 */
public class AggSvcGroupAllMixedAccessImpl extends AggregationServiceBaseUngrouped
{
    private final AggregationAccessorSlotPair[] accessors;
    protected AggregationState[] states;

    public AggSvcGroupAllMixedAccessImpl(ExprEvaluator evaluators[], AggregationMethod aggregators[], AggregationAccessorSlotPair[] accessors, AggregationState[] states) {
        super(evaluators, aggregators);
        this.accessors = accessors;
        this.states = states;
    }

    public void applyEnter(EventBean[] eventsPerStream, Object optionalGroupKeyPerRow, ExprEvaluatorContext exprEvaluatorContext)
    {
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, true, exprEvaluatorContext);
            aggregators[j].enter(columnResult);
        }

        for (AggregationState state : states) {
            state.applyEnter(eventsPerStream, exprEvaluatorContext);
        }
    }

    public void applyLeave(EventBean[] eventsPerStream, Object optionalGroupKeyPerRow, ExprEvaluatorContext exprEvaluatorContext)
    {
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, false, exprEvaluatorContext);
            aggregators[j].leave(columnResult);
        }

        for (AggregationState state : states) {
            state.applyLeave(eventsPerStream, exprEvaluatorContext);
        }        
    }

    public void setCurrentAccess(Object groupKey, int agentInstanceId)
    {
        // no action needed - this implementation does not group and the current row is the single group
    }

    public Object getValue(int column, int agentInstanceId)
    {
        if (column < aggregators.length) {
            return aggregators[column].getValue();
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getValue(states[pair.getSlot()]);
        }
    }

    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvents(states[pair.getSlot()]);
        }
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvent(states[pair.getSlot()]);
        }
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        for (AggregationState state : states) {
            state.clear();
        }
        for (AggregationMethod aggregator : aggregators)
        {
            aggregator.clear();
        }
    }

    public void setRemovedCallback(AggregationRowRemovedCallback callback) {
        // not applicable
    }
}