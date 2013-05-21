/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.epl.agg.access.AggregationAccessor;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.AggregationStateFactory;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.epl.core.MethodResolutionService;

public class ExprAvgNodeFactory implements AggregationMethodFactory
{
    private final Class childType;
    private final Class resultType;
    private final boolean isDistinct;
    private final boolean hasFilter;

    public ExprAvgNodeFactory(Class childType, boolean isDistinct, MethodResolutionService methodResolutionService, boolean hasFilter)
    {
        this.childType = childType;
        this.isDistinct = isDistinct;
        this.resultType = methodResolutionService.getAvgAggregatorType(childType);
        this.hasFilter = hasFilter;
    }

    public boolean isAccessAggregation() {
        return false;
    }

    public Class getResultType()
    {
        return resultType;
    }

    public AggregationStateKey getAggregationStateKey(boolean isMatchRecognize) {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationStateFactory getAggregationStateFactory(boolean isMatchRecognize) {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationAccessor getAccessor() {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationMethod make(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId) {
        AggregationMethod method = methodResolutionService.makeAvgAggregator(agentInstanceId, groupId, aggregationId, childType, hasFilter);
        if (!isDistinct) {
            return method;
        }
        return methodResolutionService.makeDistinctAggregator(agentInstanceId, groupId, aggregationId, method, childType, hasFilter);
    }

    public AggregationMethodFactory getPrototypeAggregator() {
        return this;
    }
}