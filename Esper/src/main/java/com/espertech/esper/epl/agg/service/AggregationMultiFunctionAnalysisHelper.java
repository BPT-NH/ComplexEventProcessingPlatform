/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.service;

import com.espertech.esper.epl.agg.access.AggregationAccessor;
import com.espertech.esper.epl.agg.access.AggregationAccessorSlotPair;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.expression.ExprAggregateNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AggregationMultiFunctionAnalysisHelper
{
    // handle accessor aggregation (direct data window by-group access to properties)
    public static AggregationMultiFunctionAnalysisResult analyzeAccessAggregations(List<AggregationServiceAggExpressionDesc> aggregations) {
        int currentSlot = 0;
        Map<AggregationStateKey, Integer> accessProviderSlots = new LinkedHashMap<AggregationStateKey, Integer>();
        List<AggregationAccessorSlotPair> accessorPairs = new ArrayList<AggregationAccessorSlotPair>();
        List<AggregationStateFactory> stateFactories = new ArrayList<AggregationStateFactory>();

        for (AggregationServiceAggExpressionDesc aggregation : aggregations)
        {
            ExprAggregateNode aggregateNode = aggregation.getAggregationNode();
            if (!aggregateNode.getFactory().isAccessAggregation()) {
                continue;
            }

            AggregationStateKey providerKey = aggregateNode.getFactory().getAggregationStateKey(false);
            AggregationAccessor accessor = aggregateNode.getFactory().getAccessor();

            Integer slot = accessProviderSlots.get(providerKey);
            if (slot == null) {
                accessProviderSlots.put(providerKey, currentSlot);
                slot = currentSlot++;
                AggregationStateFactory providerFactory = aggregateNode.getFactory().getAggregationStateFactory(false);
                stateFactories.add(providerFactory);
            }

            accessorPairs.add(new AggregationAccessorSlotPair(slot, accessor));
        }

        // handle no group-by clause cases
        AggregationAccessorSlotPair[] pairs = accessorPairs.toArray(new AggregationAccessorSlotPair[accessorPairs.size()]);
        AggregationStateFactory[] accessAggregations = stateFactories.toArray(new AggregationStateFactory[stateFactories.size()]);
        return new AggregationMultiFunctionAnalysisResult(pairs, accessAggregations);
    }
}
