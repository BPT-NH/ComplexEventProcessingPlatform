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

import com.espertech.esper.client.hook.AggregationFunctionFactory;
import com.espertech.esper.epl.agg.access.AggregationAccessor;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.epl.agg.service.AggregationStateFactory;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.core.MethodResolutionService;

public class ExprPlugInAggFunctionFactory implements AggregationMethodFactory
{
    private final AggregationFunctionFactory aggregationFunctionFactory;
    private final boolean distinct;
    private final Class aggregatedValueType;

    public ExprPlugInAggFunctionFactory(AggregationFunctionFactory aggregationFunctionFactory, boolean distinct, Class aggregatedValueType) {
        this.aggregationFunctionFactory = aggregationFunctionFactory;
        this.distinct = distinct;
        this.aggregatedValueType = aggregatedValueType;
    }

    public Class getResultType() {
        return aggregationFunctionFactory.getValueType();
    }

    public boolean isAccessAggregation() {
        return false;
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

        AggregationMethod method = aggregationFunctionFactory.newAggregator();
        if (!distinct) {
            return method;
        }
        return methodResolutionService.makeDistinctAggregator(agentInstanceId, groupId, aggregationId, method, aggregatedValueType,false);
    }

    public AggregationMethodFactory getPrototypeAggregator() {
        return this;
    }
}
