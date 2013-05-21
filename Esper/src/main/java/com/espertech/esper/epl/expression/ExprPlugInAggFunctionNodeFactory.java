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

package com.espertech.esper.epl.expression;

import com.espertech.esper.epl.agg.access.AggregationAccessor;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.AggregationStateFactory;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.epl.agg.service.AggregationSupport;
import com.espertech.esper.epl.core.MethodResolutionService;

@Deprecated
public class ExprPlugInAggFunctionNodeFactory implements AggregationMethodFactory
{
    private final AggregationSupport aggregationSupport;
    private final boolean distinct;
    private final Class aggregatedValueType;

    public ExprPlugInAggFunctionNodeFactory(AggregationSupport aggregationSupport, boolean distinct, Class aggregatedValueType)
    {
        this.aggregationSupport = aggregationSupport;
        this.distinct = distinct;
        this.aggregatedValueType = aggregatedValueType;
    }

    public boolean isAccessAggregation() {
        return false;
    }

    public Class getResultType()
    {
        return aggregationSupport.getValueType();
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

        AggregationMethod method = methodResolutionService.makePlugInAggregator(aggregationSupport.getFunctionName());
        if (!distinct) {
            return method;
        }
        return methodResolutionService.makeDistinctAggregator(agentInstanceId, groupId, aggregationId, method, aggregatedValueType,false);
    }

    public AggregationMethodFactory getPrototypeAggregator() {
        return this;
    }
}
