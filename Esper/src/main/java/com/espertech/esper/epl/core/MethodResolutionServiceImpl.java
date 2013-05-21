/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.core;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.hook.AggregationFunctionFactory;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.epl.agg.access.*;
import com.espertech.esper.epl.agg.aggregator.*;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.epl.agg.service.AggregationStateFactory;
import com.espertech.esper.epl.agg.service.AggregationSupport;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.plugin.PlugInAggregationMultiFunctionStateContext;
import com.espertech.esper.plugin.PlugInAggregationMultiFunctionStateFactory;
import com.espertech.esper.schedule.TimeProvider;
import com.espertech.esper.type.MinMaxTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Implements method resolution.
 */
public class MethodResolutionServiceImpl implements MethodResolutionService
{
    private static final Log log = LogFactory.getLog(MethodResolutionServiceImpl.class);
	private final EngineImportService engineImportService;
    private final TimeProvider timeProvider;

    /**
     * Ctor.
     * @param engineImportService is the engine imports
     * @param timeProvider returns time
     */
    public MethodResolutionServiceImpl(EngineImportService engineImportService,
                                       TimeProvider timeProvider)
	{
        this.engineImportService = engineImportService;
        this.timeProvider = timeProvider;
    }

    public boolean isUdfCache() {
        return engineImportService.isUdfCache();
    }

    public boolean isDuckType() {
        return engineImportService.isDuckType();
    }

    public boolean isSortUsingCollator() {
        return engineImportService.isSortUsingCollator();
    }

    public AggregationSupport makePlugInAggregator(String functionName)
    {
        try
        {
            return engineImportService.resolveAggregation(functionName);
        }
        catch (EngineImportUndefinedException e)
        {
            throw new EPException("Failed to make new aggregation function instance for '" + functionName + "'", e);
        }
        catch (EngineImportException e)
        {
            throw new EPException("Failed to make new aggregation function instance for '" + functionName + "'", e);
        }
    }

    public Method resolveMethod(String className, String methodName, Class[] paramTypes, boolean[] allowEventBeanType, boolean[] allowEventBeanCollType)
			throws EngineImportException
    {
        return engineImportService.resolveMethod(className, methodName, paramTypes, allowEventBeanType, allowEventBeanCollType);
	}

    public Method resolveMethod(String className, String methodName)
			throws EngineImportException
    {
        return engineImportService.resolveMethod(className, methodName);
	}

    public Constructor resolveCtor(Class clazz, Class[] paramTypes) throws EngineImportException {
        return engineImportService.resolveCtor(clazz, paramTypes);
    }

    public Class resolveClass(String className)
			throws EngineImportException
    {
        return engineImportService.resolveClass(className);
	}

    public Method resolveMethod(Class clazz, String methodName, Class[] paramTypes, boolean[] allowEventBeanType, boolean[] allowEventBeanCollType) throws EngineImportException
    {
        return engineImportService.resolveMethod(clazz, methodName, paramTypes, allowEventBeanType, allowEventBeanCollType);
    }

    public AggregationMethod makeCountAggregator(int agentInstanceId, int groupId, int aggregationId, boolean isIgnoreNull, boolean hasFilter)
    {
        if (!hasFilter) {
            if (isIgnoreNull) {
                return new AggregatorCountNonNull();
            }
            return new AggregatorCount();
        }
        else {
            if (isIgnoreNull) {
                return new AggregatorCountNonNullFilter();
            }
            return new AggregatorCountFilter();
        }
    }

    public AggregationSupport resolveAggregation(String functionName) throws EngineImportUndefinedException, EngineImportException
    {
        return engineImportService.resolveAggregation(functionName);
    }

    public AggregationFunctionFactory resolveAggregationFactory(String functionName) throws EngineImportUndefinedException, EngineImportException
    {
        return engineImportService.resolveAggregationFactory(functionName);
    }

    public Pair<Class, EngineImportSingleRowDesc> resolveSingleRow(String functionName) throws EngineImportUndefinedException, EngineImportException
    {
        return engineImportService.resolveSingleRow(functionName);
    }

    public AggregationMethod makeSumAggregator(int agentInstanceId, int groupId, int aggregationId, Class type, boolean hasFilter)
    {
        if (!hasFilter) {
            if (type == BigInteger.class)
            {
                return new AggregatorSumBigInteger();
            }
            if (type == BigDecimal.class)
            {
                return new AggregatorSumBigDecimal();
            }
            if ((type == Long.class) || (type == long.class))
            {
                return new AggregatorSumLong();
            }
            if ((type == Integer.class) || (type == int.class))
            {
                return new AggregatorSumInteger();
            }
            if ((type == Double.class) || (type == double.class))
            {
                return new AggregatorSumDouble();
            }
            if ((type == Float.class) || (type == float.class))
            {
                return new AggregatorSumFloat();
            }
            return new AggregatorSumNumInteger();
        }
        else {
            if (type == BigInteger.class)
            {
                return new AggregatorSumBigIntegerFilter();
            }
            if (type == BigDecimal.class)
            {
                return new AggregatorSumBigDecimalFilter();
            }
            if ((type == Long.class) || (type == long.class))
            {
                return new AggregatorSumLongFilter();
            }
            if ((type == Integer.class) || (type == int.class))
            {
                return new AggregatorSumIntegerFilter();
            }
            if ((type == Double.class) || (type == double.class))
            {
                return new AggregatorSumDoubleFilter();
            }
            if ((type == Float.class) || (type == float.class))
            {
                return new AggregatorSumFloatFilter();
            }
            return new AggregatorSumNumIntegerFilter();
        }
    }

    public Class getSumAggregatorType(Class type)
    {
        if (type == BigInteger.class)
        {
            return BigInteger.class;
        }
        if (type == BigDecimal.class)
        {
            return BigDecimal.class;
        }
        if ((type == Long.class) || (type == long.class))
        {
            return Long.class;
        }
        if ((type == Integer.class) || (type == int.class))
        {
            return Integer.class;
        }
        if ((type == Double.class) || (type == double.class))
        {
            return Double.class;
        }
        if ((type == Float.class) || (type == float.class))
        {
            return Float.class;
        }
        return Integer.class;
    }

    public AggregationMethod makeDistinctAggregator(int agentInstanceId, int groupId, int aggregationId, AggregationMethod aggregationMethod, Class childType, boolean hasFilter)
    {
        if (hasFilter) {
            return new AggregatorDistinctValueFilter(aggregationMethod);
        }
        return new AggregatorDistinctValue(aggregationMethod);
    }

    public AggregationMethod makeAvgAggregator(int agentInstanceId, int groupId, int aggregationId, Class type, boolean hasFilter)
    {
        if (hasFilter) {
            if ((type == BigDecimal.class) || (type == BigInteger.class))
            {
                return new AggregatorAvgBigDecimalFilter(engineImportService.getDefaultMathContext());
            }
            return new AggregatorAvgFilter();
        }
        if ((type == BigDecimal.class) || (type == BigInteger.class))
        {
            return new AggregatorAvgBigDecimal(engineImportService.getDefaultMathContext());
        }
        return new AggregatorAvg();
    }

    public Class getAvgAggregatorType(Class type)
    {
        if ((type == BigDecimal.class) || (type == BigInteger.class))
        {
            return BigDecimal.class;
        }
        return Double.class;
    }

    public AggregationMethod makeAvedevAggregator(int agentInstanceId, int groupId, int aggregationId, boolean hasFilter)
    {
        if (!hasFilter) {
            return new AggregatorAvedev();
        }
        else {
            return new AggregatorAvedevFilter();
        }
    }

    public AggregationMethod makeMedianAggregator(int agentInstanceId, int groupId, int aggregationId, boolean hasFilter)
    {
        if (!hasFilter) {
            return new AggregatorMedian();
        }
        return new AggregatorMedianFilter();
    }

    public AggregationMethod makeMinMaxAggregator(int agentInstanceId, int groupId, int aggregationId, MinMaxTypeEnum minMaxTypeEnum, Class targetType, boolean isHasDataWindows, boolean hasFilter)
    {
        if (!hasFilter) {
            if (!isHasDataWindows) {
                return new AggregatorMinMaxEver(minMaxTypeEnum, targetType);
            }
            return new AggregatorMinMax(minMaxTypeEnum, targetType);
        }
        else {
            if (!isHasDataWindows) {
                return new AggregatorMinMaxEverFilter(minMaxTypeEnum, targetType);
            }
            return new AggregatorMinMaxFilter(minMaxTypeEnum, targetType);
        }
    }

    public AggregationMethod makeStddevAggregator(int agentInstanceId, int groupId, int aggregationId, boolean hasFilter)
    {
        if (!hasFilter) {
            return new AggregatorStddev();
        }
        return new AggregatorStddevFilter();
    }

    public AggregationMethod makeFirstEverValueAggregator(int agentInstanceId, int groupId, int aggregationId, Class type, boolean hasFilter) {
        if (hasFilter) {
            return new AggregatorFirstEverFilter(type);
        }
        return new AggregatorFirstEver(type);
    }

    public AggregationMethod makeLastEverValueAggregator(int agentInstanceId, int groupId, int aggregationId, Class type, boolean hasFilter) {
        if (hasFilter) {
            return new AggregatorLastEverFilter(type);
        }
        return new AggregatorLastEver(type);
    }

    public AggregationMethod makeRateAggregator(int agentInstanceId, int groupId, int aggregationId) {
        return new AggregatorRate();
    }

    public AggregationMethod makeRateEverAggregator(int agentInstanceId, int groupId, int aggregationId, long interval) {
        return new AggregatorRateEver(interval, timeProvider);
    }

    public AggregationMethod makeNthAggregator(int agentInstanceId, int groupId, int aggregationId, Class returnType, int size) {
        return new AggregatorNth(returnType, size);
    }

    public AggregationMethod makeLeavingAggregator(int agentInstanceId, int groupId, int aggregationId) {
        return new AggregatorLeaving();
    }

    public void setGroupKeyTypes(Class[] groupKeyTypes)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Group key typed are " + Arrays.toString(groupKeyTypes));
        }
    }

    public AggregationMethod[] newAggregators(AggregationMethodFactory[] prototypes, int agentInstanceId) {
        return newAggregatorsInternal(prototypes, agentInstanceId);
    }

    public AggregationMethod[] newAggregators(AggregationMethodFactory[] prototypes, int agentInstanceId, Object groupKey) {
        return newAggregatorsInternal(prototypes, agentInstanceId);
    }

    public AggregationMethod[] newAggregatorsInternal(AggregationMethodFactory[] prototypes, int agentInstanceId) {
        AggregationMethod row[] = new AggregationMethod[prototypes.length];
        for (int i = 0; i < prototypes.length; i++)
        {
            row[i] = prototypes[i].make(this, agentInstanceId, -1, i);
        }
        return row;
    }

    public long getCurrentRowCount(AggregationMethod[] aggregators, AggregationState[] groupStates)
    {
        return 0;   // since the aggregators are always fresh ones 
    }

    public void removeAggregators(int agentInstanceId, Object groupKey)
    {
        // To be overridden by implementations that care when aggregators get removed
    }

    public AggregationState[] newAccesses(int agentInstanceId, boolean isJoin, AggregationStateFactory[] accessAggSpecs) {
        return newAccessInternal(agentInstanceId, accessAggSpecs, isJoin, null);
    }

    public AggregationState[] newAccesses(int agentInstanceId, boolean isJoin, AggregationStateFactory[] accessAggSpecs, Object groupKey) {
        return newAccessInternal(agentInstanceId, accessAggSpecs, isJoin, groupKey);
    }

    private AggregationState[] newAccessInternal(int agentInstanceId, AggregationStateFactory[] accessAggSpecs, boolean isJoin, Object groupKey) {
        AggregationState[] row = new AggregationState[accessAggSpecs.length];
        int i = 0;
        for (AggregationStateFactory spec : accessAggSpecs) {
            row[i] = spec.createAccess(this, agentInstanceId, 0, i, isJoin, groupKey);   // no group id assigned
            i++;
        }
        return row;
    }

    public AggregationState makeAccessAggLinearNonJoin(int agentInstanceId, int groupId, int aggregationId, int streamNum) {
        return new AggregationStateImpl(streamNum);
    }

    public AggregationState makeAccessAggLinearJoin(int agentInstanceId, int groupId, int aggregationId, int streamNum) {
        return new AggregationStateJoinImpl(streamNum);
    }

    public AggregationState makeAccessAggSortedNonJoin(int agentInstanceId, int groupId, int aggregationId, AggregationStateSortedSpec spec) {
        return new AggregationStateSortedImpl(spec);
    }

    public AggregationState makeAccessAggSortedJoin(int agentInstanceId, int groupId, int aggregationId, AggregationStateSortedSpec spec) {
        return new AggregationStateSortedJoin(spec);
    }

    public AggregationState makeAccessAggMinMaxEver(int agentInstanceId, int groupId, int aggregationId, AggregationStateMinMaxByEverSpec spec) {
        return new AggregationStateMinMaxByEver(spec);
    }

    public AggregationState makeAccessAggPlugin(int agentInstanceId, int groupId, int aggregationId, boolean join, PlugInAggregationMultiFunctionStateFactory providerFactory, Object groupKey) {
        PlugInAggregationMultiFunctionStateContext context = new PlugInAggregationMultiFunctionStateContext(agentInstanceId, groupKey);
        return providerFactory.makeAggregationState(context);
    }

    public void destroyedAgentInstance(int agentInstanceId) {
        // no action require
    }

    public EngineImportService getEngineImportService() {
        return engineImportService;
    }

    public Object getCriteriaKeyBinding(ExprEvaluator[] evaluators) {
        return null;    // no bindings
    }
}
