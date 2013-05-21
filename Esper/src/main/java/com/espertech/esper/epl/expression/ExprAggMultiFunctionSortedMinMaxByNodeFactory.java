/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.epl.agg.access.*;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.*;
import com.espertech.esper.epl.core.MethodResolutionService;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.util.JavaClassHelper;

import java.util.Comparator;

public class ExprAggMultiFunctionSortedMinMaxByNodeFactory implements AggregationMethodFactory
{
    private final int streamNum;
    private final Class resultType;
    private final ExprNode[] criteriaExpressions;
    private final MethodResolutionService methodResolutionService;
    private final boolean[] sortDescending;
    private final ExprEvaluator[] evaluators;
    private final boolean max;
    private final boolean ever;
    private final boolean sortedwin;

    public ExprAggMultiFunctionSortedMinMaxByNodeFactory(int streamNum, Class resultType, ExprNode[] criteriaExpressions, MethodResolutionService methodResolutionService, boolean[] sortDescending, ExprEvaluator[] evaluators, boolean max, boolean ever, boolean sortedwin) {
        this.streamNum = streamNum;
        this.resultType = resultType;
        this.criteriaExpressions = criteriaExpressions;
        this.methodResolutionService = methodResolutionService;
        this.sortDescending = sortDescending;
        this.evaluators = evaluators;
        this.max = max;
        this.ever = ever;
        this.sortedwin = sortedwin;
    }

    public boolean isAccessAggregation() {
        return true;
    }

    public AggregationMethod make(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId) {
        throw new IllegalStateException("Aggregation function is not available");
    }

    public Class getResultType() {
        if (sortedwin) {
            return JavaClassHelper.getArrayType(resultType);
        }
        else {
            return resultType;
        }
    }

    public AggregationStateKey getAggregationStateKey(boolean isMatchRecognize) {
        // For match-recognize we don't use the access functions
        if (isMatchRecognize) {
            return null;
        }

        AggregationStateTypeWStream type;
        if (ever) {
            type = max ? AggregationStateTypeWStream.MAXEVER : AggregationStateTypeWStream.MINEVER;
        }
        else {
            type = AggregationStateTypeWStream.SORTED;
        }
        return new AggregationStateKeyWStream(streamNum, type, criteriaExpressions);
    }

    public AggregationStateFactory getAggregationStateFactory(boolean isMatchRecognize) {
        // For match-recognize we don't use the access functions
        if (isMatchRecognize) {
            return null;
        }

        boolean sortUsingCollator = methodResolutionService.isSortUsingCollator();
        Comparator<Object> comparator = CollectionUtil.getComparator(evaluators, sortUsingCollator, sortDescending);
        Object criteriaKeyBinding = methodResolutionService.getCriteriaKeyBinding(evaluators);

        AggregationStateFactory factory;
        if (ever) {
            final AggregationStateMinMaxByEverSpec spec = new AggregationStateMinMaxByEverSpec(streamNum, evaluators, max, comparator, criteriaKeyBinding);
            factory = new AggregationStateFactory() {
                public AggregationState createAccess(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId, boolean join, Object groupKey) {
                    return methodResolutionService.makeAccessAggMinMaxEver(agentInstanceId, groupId, aggregationId, spec);
                }
            };
        }
        else {
            final AggregationStateSortedSpec spec = new AggregationStateSortedSpec(streamNum, evaluators, comparator, criteriaKeyBinding);
            factory = new AggregationStateFactory() {
                public AggregationState createAccess(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId, boolean join, Object groupKey) {
                    if (join) {
                        return methodResolutionService.makeAccessAggSortedJoin(agentInstanceId, groupId, aggregationId, spec);
                    }
                    return methodResolutionService.makeAccessAggSortedNonJoin(agentInstanceId, groupId, aggregationId, spec);
                }
            };
        }
        return factory;
    }

    public AggregationAccessor getAccessor() {
        if (!sortedwin) {
            return new AggregationAccessorMinMaxBy(max);
        }
        else {
            return new AggregationAccessorSorted(max, resultType);
        }
    }
}