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

import java.util.*;

/**
 * Implementation for handling aggregation with grouping by group-keys.
 */
public class AggSvcGroupByRefcountedWAccessImpl extends AggregationServiceBaseGrouped
{
    protected final AggregationAccessorSlotPair[] accessors;
    protected final AggregationStateFactory[] accessAggregations;
    protected final boolean isJoin;

    // maintain for each group a row of aggregator states that the expression node canb pull the data from via index
    protected Map<Object, AggregationMethodPairRow> aggregatorsPerGroup;

    // maintain a current row for random access into the aggregator state table
    // (row=groups, columns=expression nodes that have aggregation functions)
    private AggregationMethod[] currentAggregatorMethods;
    private AggregationState[] currentAggregatorStates;

    private MethodResolutionService methodResolutionService;

    protected List<Object> removedKeys;

    /**
     * Ctor.
     * @param evaluators - evaluate the sub-expression within the aggregate function (ie. sum(4*myNum))
     * @param prototypes - collect the aggregation state that evaluators evaluate to, act as prototypes for new aggregations
     * aggregation states for each group
     * @param methodResolutionService - factory for creating additional aggregation method instances per group key
     * @param accessors accessor definitions
     * @param accessAggregations access aggs
     * @param isJoin true for join, false for single-stream
     */
    public AggSvcGroupByRefcountedWAccessImpl(ExprEvaluator evaluators[],
                                       AggregationMethodFactory prototypes[],
                                       MethodResolutionService methodResolutionService,
                                       AggregationAccessorSlotPair[] accessors,
                                       AggregationStateFactory[] accessAggregations,
                                       boolean isJoin)
    {
        super(evaluators, prototypes);
        this.methodResolutionService = methodResolutionService;
        this.aggregatorsPerGroup = new HashMap<Object, AggregationMethodPairRow>();
        this.accessors = accessors;
        this.accessAggregations = accessAggregations;
        this.isJoin = isJoin;
        removedKeys = new ArrayList<Object>();
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        aggregatorsPerGroup.clear();
    }

    public void applyEnter(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        handleRemovedKeys();

        AggregationMethodPairRow row = aggregatorsPerGroup.get(groupByKey);

        // The aggregators for this group do not exist, need to create them from the prototypes
        AggregationMethod[] groupAggregators;
        AggregationState[] groupStates;
        if (row == null)
        {
            groupAggregators = methodResolutionService.newAggregators(aggregators, exprEvaluatorContext.getAgentInstanceId(), groupByKey);
            groupStates = methodResolutionService.newAccesses(exprEvaluatorContext.getAgentInstanceId(), isJoin, accessAggregations, groupByKey);
            row = new AggregationMethodPairRow(methodResolutionService.getCurrentRowCount(groupAggregators, groupStates) + 1, groupAggregators, groupStates);
            aggregatorsPerGroup.put(groupByKey, row);
        }
        else
        {
            groupAggregators = row.getMethods();
            groupStates = row.getStates();
            row.increaseRefcount();
        }

        currentAggregatorMethods = groupAggregators;
        currentAggregatorStates = groupStates;

        // For this row, evaluate sub-expressions, enter result
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, true, exprEvaluatorContext);
            groupAggregators[j].enter(columnResult);
        }
        for (AggregationState state : currentAggregatorStates) {
            state.applyEnter(eventsPerStream, exprEvaluatorContext);
        }

        internalHandleGroupUpdate(groupByKey, row);
    }

    public void applyLeave(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationMethodPairRow row = aggregatorsPerGroup.get(groupByKey);

        // The aggregators for this group do not exist, need to create them from the prototypes
        AggregationMethod[] groupAggregators;
        AggregationState[] groupStates;
        if (row != null)
        {
            groupAggregators = row.getMethods();
            groupStates = row.getStates();
        }
        else
        {
            groupAggregators = methodResolutionService.newAggregators(aggregators, exprEvaluatorContext.getAgentInstanceId(), groupByKey);
            groupStates = methodResolutionService.newAccesses(exprEvaluatorContext.getAgentInstanceId(), isJoin, accessAggregations, groupByKey);
            row = new AggregationMethodPairRow(methodResolutionService.getCurrentRowCount(groupAggregators, groupStates) + 1, groupAggregators, groupStates);
            aggregatorsPerGroup.put(groupByKey, row);
        }

        currentAggregatorMethods = groupAggregators;
        currentAggregatorStates = groupStates;

        // For this row, evaluate sub-expressions, enter result
        for (int j = 0; j < evaluators.length; j++)
        {
            Object columnResult = evaluators[j].evaluate(eventsPerStream, false, exprEvaluatorContext);
            groupAggregators[j].leave(columnResult);
        }
        for (AggregationState state : currentAggregatorStates) {
            state.applyLeave(eventsPerStream, exprEvaluatorContext);
        }

        row.decreaseRefcount();
        if (row.getRefcount() <= 0)
        {
            removedKeys.add(groupByKey);
            methodResolutionService.removeAggregators(exprEvaluatorContext.getAgentInstanceId(), groupByKey);  // allow persistence to remove keys already
        }

        internalHandleGroupUpdate(groupByKey, row);
    }

    public void setCurrentAccess(Object groupByKey, int agentInstanceId)
    {
        AggregationMethodPairRow row = aggregatorsPerGroup.get(groupByKey);

        if (row != null)
        {
            currentAggregatorMethods = row.getMethods();
            currentAggregatorStates = row.getStates();
        }
        else
        {
            currentAggregatorMethods = null;
        }

        if (currentAggregatorMethods == null)
        {
            currentAggregatorMethods = methodResolutionService.newAggregators(aggregators, agentInstanceId, groupByKey);
            currentAggregatorStates = methodResolutionService.newAccesses(agentInstanceId, isJoin, accessAggregations, groupByKey);
        }
    }

    public Object getValue(int column, int agentInstanceId)
    {
        if (column < aggregators.length) {
            return currentAggregatorMethods[column].getValue();
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getValue(currentAggregatorStates[pair.getSlot()]);
        }
    }

    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvents(currentAggregatorStates[pair.getSlot()]);
        }
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        if (column < aggregators.length) {
            return null;
        }
        else {
            AggregationAccessorSlotPair pair = accessors[column - aggregators.length];
            return pair.getAccessor().getEnumerableEvent(currentAggregatorStates[pair.getSlot()]);
        }
    }

    public void setRemovedCallback(AggregationRowRemovedCallback callback) {
        // not applicable
    }

    public void internalHandleGroupUpdate(Object groupByKey, AggregationMethodPairRow row) {
        // no action required
    }

    public void internalHandleGroupRemove(Object groupByKey) {
        // no action required
    }

    protected void handleRemovedKeys() {
        if (!removedKeys.isEmpty())     // we collect removed keys lazily on the next enter to reduce the chance of empty-group queries creating empty aggregators temporarily
        {
            for (Object removedKey : removedKeys)
            {
                aggregatorsPerGroup.remove(removedKey);
                internalHandleGroupRemove(removedKey);
            }
            removedKeys.clear();
        }
    }
}
