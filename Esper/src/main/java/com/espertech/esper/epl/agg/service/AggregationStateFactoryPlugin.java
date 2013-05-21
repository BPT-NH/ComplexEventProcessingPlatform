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

import com.espertech.esper.plugin.PlugInAggregationMultiFunctionStateFactory;
import com.espertech.esper.epl.agg.access.AggregationState;
import com.espertech.esper.epl.core.MethodResolutionService;

public class AggregationStateFactoryPlugin implements AggregationStateFactory {

    private final PlugInAggregationMultiFunctionStateFactory providerFactory;

    public AggregationStateFactoryPlugin(PlugInAggregationMultiFunctionStateFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    public AggregationState createAccess(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId, boolean join, Object groupBy) {
        return methodResolutionService.makeAccessAggPlugin(agentInstanceId, groupId, aggregationId, join, providerFactory, groupBy);
    }
}
