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
import com.espertech.esper.epl.core.MethodResolutionService;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for handling aggregation with grouping by group-keys.
 */
public class AggSvcGroupByMixedAccessImpl extends AggregationServiceBaseGrouped
{
    private final AggregationAccessorSlotPair[] accessorsFactory;
    protected final AggregationStateFactory[] accessAggregations;
    protected final boolean isJoin;

    // maintain for each group a row of aggregator states that the expression node canb pull the data from via index
    protected Map<Object, AggregationRowPair> aggregatorsPerGroup;

    // maintain a current row for random access into the aggregator state table
    // (row=groups, columns=expression nodes that have aggregation functions)
    private AggregationRowPair currentAggregatorRow;

    private MethodResolutionService methodResolutionService;

    /**
     * Ctor.
     * @param evaluators - evaluate the sub-expression within the aggregate function (ie. sum(4*myNum))
     * @param prototypes - collect the aggregation state that evaluators evaluate to, act as prototypes for new aggregations
     * aggregation states for each group
     * @param methodResolutionService - factory for creating additional aggregation method instances per group key
     * @param accessorsFactory accessor definitions
     * @param accessAggregations access aggs
     * @param isJoin true for join, false for single-stream
     */
    public AggSvcGroupByMixedAccessImpl(ExprEvaluator evaluators[],
                                        AggregationMethodFactory prototypes[],
                                        MethodResolutionService methodResolutionService,
                                        AggregationAccessorSlotPair[] accessorsFactory,
                                        AggregationStateFactory[] accessAggregations,
                                        boolean isJoin)
    {
        super(evaluators, prototypes);
        this.accessorsFactory = accessorsFactory;
        this.accessAggregations = accessAggregations;
        this.isJoin = isJoin;
        this.methodResolutionService = methodResolutionService;
        this.aggregatorsPerGroup = new HashMap<Object, AggregationRowPair>();
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        aggregatorsPerGroup.clear();
    }

    public void applyEnter(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationRowPair groupAggregators = aggregatorsPerGroup.get(groupByKey);

        // The aggregators for this group do not exist, need to create them from the prototypes
        if (groupAggregators == null)
        {
            AggregationMethod[] methods = methodResolutionService.newAggregators(aggregators, exprEvaluatorContext.getAgentInstanceId(), groupByKey);
            AggregationState[] states = methodResolutionService.newAccesses(exprEvaluatorContext.getAgentInstanceId(), isJoin, accessAggregations, groupByKey);
            groupAggregators = new AggregationRowPair(methods, states);
            aggregatorsPerGroup.put(groupByKey, groupAggregators);
        }
        currentAggregatorRow = groupAggregators;

        // For this row, evaluate sub-expressions, enter result
        AggregationMethod[] groupAggMethods = groupAggregators.getMethods();
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, true, exprEvaluatorContext);
            groupAggMethods[j].enter(columnResult);
        }
        for (AggregationState state : currentAggregatorRow.getStates()) {
            state.applyEnter(eventsPerStream, exprEvaluatorContext);
        }
        internalHandleUpdated(groupByKey, groupAggregators);
    }

    public void applyLeave(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationRowPair groupAggregators = aggregatorsPerGroup.get(groupByKey);

        // The aggregators for this group do not exist, need to create them from the prototypes
        if (groupAggregators == null)
        {
            AggregationMethod[] methods = methodResolutionService.newAggregators(aggregators, exprEvaluatorContext.getAgentInstanceId(), groupByKey);
            AggregationState[] states = methodResolutionService.newAccesses(exprEvaluatorContext.getAgentInstanceId(), isJoin, accessAggregations, groupByKey);
            groupAggregators = new AggregationRowPair(methods, states);
            aggregatorsPerGroup.put(groupByKey, groupAggregators);
        }
        currentAggregatorRow = groupAggregators;

        // For this row, evaluate sub-expressions, enter result
        AggregationMethod[] groupAggMethods = groupAggregators.getMethods();
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, false, exprEvaluatorContext);
            groupAggMethods[j].leave(columnResult);
        }
        for (AggregationState state : currentAggregatorRow.getStates()) {
            state.applyLeave(eventsPerStream, exprEvaluatorContext);
        }
        internalHandleUpdated(groupByKey, groupAggregators);
    }

    public void setCurrentAccess(Object groupByKey, int agentInstanceId)
    {
        currentAggregatorRow = aggregatorsPerGroup.get(groupByKey);

        if (currentAggregatorRow == null)
        {
            AggregationMethod[] methods = methodResolutionService.newAggregators(aggregators, agentInstanceId, groupByKey);
            AggregationState[] states = methodResolutionService.newAccesses(agentInstanceId, isJoin, accessAggregations, groupByKey);
            currentAggregatorRow = new AggregationRowPair(methods, states);
            aggregatorsPerGroup.put(groupByKey, currentAggregatorRow);
        }
    }

    public Object getValue(int column, int agentInstanceId)
    {
        if (column < aggregators.length) {
            return currentAggregatorRow.getMethods()[column].getValue();
        }
        else {
            AggregationAccessorSlotPair pair = accessorsFactory[column - aggregators.length];
            return pair.getAccessor().getValue(currentAggregatorRow.getStates()[pair.getSlot()]);
        }
    }
    
    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessorsFactory[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvents(currentAggregatorRow.getStates()[pair.getSlot()]);
        }
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessorsFactory[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvent(currentAggregatorRow.getStates()[pair.getSlot()]);
        }
    }

    public void setRemovedCallback(AggregationRowRemovedCallback callback) {
        // not applicable
    }

    public void internalHandleUpdated(Object groupByKey, AggregationRowPair groupAggregators) {
        // no action required
    }
}