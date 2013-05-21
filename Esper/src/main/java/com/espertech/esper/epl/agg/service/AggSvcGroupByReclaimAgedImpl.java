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
import com.espertech.esper.util.ExecutionPathDebugLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Implementation for handling aggregation with grouping by group-keys.
 */
public class AggSvcGroupByReclaimAgedImpl extends AggregationServiceBaseGrouped
{
    private static final Log log = LogFactory.getLog(AggSvcGroupByReclaimAgedImpl.class);

    private static final long DEFAULT_MAX_AGE_MSEC = 60000L;

    private final AggregationAccessorSlotPair[] accessors;
    protected final AggregationStateFactory[] accessAggregations;
    protected final boolean isJoin;

    private final AggSvcGroupByReclaimAgedEvalFunc evaluationFunctionMaxAge;
    private final AggSvcGroupByReclaimAgedEvalFunc evaluationFunctionFrequency;
    private final MethodResolutionService methodResolutionService;

    // maintain for each group a row of aggregator states that the expression node canb pull the data from via index
    protected Map<Object, AggregationMethodRowAged> aggregatorsPerGroup;

    // maintain a current row for random access into the aggregator state table
    // (row=groups, columns=expression nodes that have aggregation functions)
    private AggregationMethod[] currentAggregatorMethods;
    private AggregationState[] currentAggregatorStates;

    private List<Object> removedKeys;
    private Long nextSweepTime = null;
    private AggregationRowRemovedCallback removedCallback;
    private volatile long currentMaxAge = DEFAULT_MAX_AGE_MSEC;
    private volatile long currentReclaimFrequency = DEFAULT_MAX_AGE_MSEC;

    public AggSvcGroupByReclaimAgedImpl(ExprEvaluator evaluators[], AggregationMethodFactory aggregators[], AggregationAccessorSlotPair[] accessors, AggregationStateFactory[] accessAggregations, boolean join, AggSvcGroupByReclaimAgedEvalFunc evaluationFunctionMaxAge, AggSvcGroupByReclaimAgedEvalFunc evaluationFunctionFrequency, MethodResolutionService methodResolutionService) {
        super(evaluators, aggregators);
        this.accessors = accessors;
        this.accessAggregations = accessAggregations;
        isJoin = join;
        this.evaluationFunctionMaxAge = evaluationFunctionMaxAge;
        this.evaluationFunctionFrequency = evaluationFunctionFrequency;
        this.methodResolutionService = methodResolutionService;
        this.aggregatorsPerGroup = new HashMap<Object, AggregationMethodRowAged>();
        removedKeys = new ArrayList<Object>();
    }

    public void clearResults(ExprEvaluatorContext exprEvaluatorContext)
    {
        aggregatorsPerGroup.clear();
    }

    public void applyEnter(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        long currentTime = exprEvaluatorContext.getTimeProvider().getTime();
        if ((nextSweepTime == null) || (nextSweepTime <= currentTime))
        {
            currentMaxAge = getMaxAge(currentMaxAge);
            currentReclaimFrequency = getReclaimFrequency(currentReclaimFrequency);
            if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
            {
                log.debug("Reclaiming groups older then " + currentMaxAge + " msec and every " + currentReclaimFrequency + "msec in frequency");
            }
            nextSweepTime = currentTime + currentReclaimFrequency;
            sweep(currentTime, currentMaxAge);
        }

        handleRemovedKeys(); // we collect removed keys lazily on the next enter to reduce the chance of empty-group queries creating empty aggregators temporarily

        AggregationMethodRowAged row = aggregatorsPerGroup.get(groupByKey);

        // The aggregators for this group do not exist, need to create them from the prototypes
        AggregationMethod[] groupAggregators;
        AggregationState[] groupStates;
        if (row == null)
        {
            groupAggregators = methodResolutionService.newAggregators(aggregators, exprEvaluatorContext.getAgentInstanceId(), groupByKey);
            groupStates = methodResolutionService.newAccesses(exprEvaluatorContext.getAgentInstanceId(), isJoin, accessAggregations, groupByKey);
            row = new AggregationMethodRowAged(methodResolutionService.getCurrentRowCount(groupAggregators, groupStates) + 1, currentTime, groupAggregators, groupStates);
            aggregatorsPerGroup.put(groupByKey, row);
        }
        else
        {
            groupAggregators = row.getMethods();
            groupStates = row.getStates();
            row.increaseRefcount();
            row.setLastUpdateTime(currentTime);
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
        internalHandleUpdated(groupByKey, row);
    }

    private void sweep(long currentTime, long currentMaxAge)
    {
        ArrayDeque<Object> removed = new ArrayDeque<Object>();
        for (Map.Entry<Object, AggregationMethodRowAged> entry : aggregatorsPerGroup.entrySet())
        {
            long age = currentTime - entry.getValue().getLastUpdateTime();
            if (age > currentMaxAge)
            {
                removed.add(entry.getKey());
            }
        }

        for (Object key : removed)
        {
            aggregatorsPerGroup.remove(key);
            internalHandleRemoved(key);
            removedCallback.removed(key);
        }
    }

    private long getMaxAge(long currentMaxAge)
    {
        Double maxAge = evaluationFunctionMaxAge.getLongValue();
        if ((maxAge == null) || (maxAge <= 0))
        {
            return currentMaxAge;
        }
        return Math.round(maxAge * 1000d);
    }

    private long getReclaimFrequency(long currentReclaimFrequency)
    {
        Double frequency = evaluationFunctionFrequency.getLongValue();
        if ((frequency == null) || (frequency <= 0))
        {
            return currentReclaimFrequency;
        }
        return Math.round(frequency * 1000d);
    }

    public void applyLeave(EventBean[] eventsPerStream, Object groupByKey, ExprEvaluatorContext exprEvaluatorContext)
    {
        AggregationMethodRowAged row = aggregatorsPerGroup.get(groupByKey);
        long currentTime = exprEvaluatorContext.getTimeProvider().getTime();

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
            row = new AggregationMethodRowAged(methodResolutionService.getCurrentRowCount(groupAggregators, groupStates) + 1, currentTime, groupAggregators, groupStates);
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
        row.setLastUpdateTime(currentTime);
        if (row.getRefcount() <= 0)
        {
            removedKeys.add(groupByKey);
            methodResolutionService.removeAggregators(exprEvaluatorContext.getAgentInstanceId(), groupByKey);  // allow persistence to remove keys already
        }
        internalHandleUpdated(groupByKey, row);
    }

    public void setCurrentAccess(Object groupByKey, int agentInstanceId)
    {
        AggregationMethodRowAged row = aggregatorsPerGroup.get(groupByKey);

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
        this.removedCallback = callback;
    }

    public void internalHandleUpdated(Object groupByKey, AggregationMethodRowAged row) {
        // no action required
    }

    public void internalHandleRemoved(Object key) {
        // no action required
    }

    protected void handleRemovedKeys() {
        if (!removedKeys.isEmpty())     // we collect removed keys lazily on the next enter to reduce the chance of empty-group queries creating empty aggregators temporarily
        {
            for (Object removedKey : removedKeys)
            {
                aggregatorsPerGroup.remove(removedKey);
                internalHandleRemoved(removedKey);
            }
            removedKeys.clear();
        }
    }
}