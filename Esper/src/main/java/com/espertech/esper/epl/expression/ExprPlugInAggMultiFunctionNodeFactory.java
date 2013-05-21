/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.EventType;
import com.espertech.esper.client.util.ExpressionReturnType;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.plugin.PlugInAggregationMultiFunctionHandler;
import com.espertech.esper.epl.agg.access.AggregationAccessor;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.*;
import com.espertech.esper.epl.agg.service.AggregationStateFactoryPlugin;
import com.espertech.esper.epl.agg.service.AggregationStateFactory;
import com.espertech.esper.epl.core.MethodResolutionService;
import com.espertech.esper.util.JavaClassHelper;

public class ExprPlugInAggMultiFunctionNodeFactory implements AggregationMethodFactory
{
    private final PlugInAggregationMultiFunctionHandler handlerPlugin;
    private ExpressionReturnType returnType;

    public ExprPlugInAggMultiFunctionNodeFactory(PlugInAggregationMultiFunctionHandler handlerPlugin) {
        this.handlerPlugin = handlerPlugin;
    }

    public boolean isAccessAggregation() {
        return true;
    }

    public AggregationMethod make(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId) {
        return null;
    }

    public AggregationStateKey getAggregationStateKey(boolean isMatchRecognize) {
        return handlerPlugin.getAggregationStateUniqueKey();
    }

    public AggregationStateFactory getAggregationStateFactory(boolean isMatchRecognize) {
        return new AggregationStateFactoryPlugin(handlerPlugin.getStateFactory());
    }

    public AggregationAccessor getAccessor() {
        return handlerPlugin.getAccessor();
    }

    public Class getResultType() {
        obtainReturnType();
        if (returnType.getCollOfEventEventType() != null) {
            return JavaClassHelper.getArrayType(returnType.getCollOfEventEventType().getUnderlyingType());
        }
        return returnType.getSingleValueType();
    }

    public Class getComponentTypeCollection() {
        obtainReturnType();
        return returnType.getComponentType();
    }

    public EventType getEventTypeSingle() {
        obtainReturnType();
        return returnType.getSingleEventEventType();
    }

    public EventType getEventTypeCollection() {
        obtainReturnType();
        return returnType.getCollOfEventEventType();
    }

    private void obtainReturnType() {
        if (returnType == null) {
            returnType = handlerPlugin.getReturnType();
        }
    }

}
