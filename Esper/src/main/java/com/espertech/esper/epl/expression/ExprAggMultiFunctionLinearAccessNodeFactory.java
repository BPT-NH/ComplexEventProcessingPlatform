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
import com.espertech.esper.util.JavaClassHelper;

public class ExprAggMultiFunctionLinearAccessNodeFactory implements AggregationMethodFactory
{
    private final static ExprNode[] NO_CRITERIA_EXPR = new ExprNode[0];

    private final AggregationStateType stateType;
    private final Class resultType;
    private final int streamNum;
    private final ExprEvaluator childNode;
    private final ExprNode indexEvalNode;
    private final boolean istreamOnly;
    private final boolean ondemandQuery;

    public ExprAggMultiFunctionLinearAccessNodeFactory(AggregationStateType stateType, Class resultType, int streamNum, ExprEvaluator childNode, ExprNode indexEvalNode, boolean istreamOnly, boolean ondemandQuery)
    {
        this.stateType = stateType;
        this.resultType = resultType;
        this.streamNum = streamNum;
        this.childNode = childNode;
        this.indexEvalNode = indexEvalNode;
        this.istreamOnly = istreamOnly;
        this.ondemandQuery = ondemandQuery;
    }

    public Class getResultType()
    {
        if (stateType == AggregationStateType.WINDOW) {
            return JavaClassHelper.getArrayType(resultType);
        }
        else {
            return resultType;
        }
    }

    public AggregationStateKey getAggregationStateKey(boolean isMatchRecognize) {
        return new AggregationStateKeyWStream(streamNum, AggregationStateTypeWStream.DATAWINDOWACCESS_LINEAR, NO_CRITERIA_EXPR);
    }

    public boolean isAccessAggregation() {
        if (ondemandQuery && stateType == AggregationStateType.WINDOW) {
            return true;
        }
        if (istreamOnly || ondemandQuery) {
            return false;
        }
        return true;
    }

    public AggregationStateFactory getAggregationStateFactory(boolean isMatchRecognize) {
        // For match-recognize we don't use the access functions
        if (isMatchRecognize) {
            return null;
        }

        return new AggregationStateFactory() {
            public AggregationState createAccess(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId, boolean join, Object groupKey) {
                if (join) {
                    return methodResolutionService.makeAccessAggLinearJoin(agentInstanceId, groupId, aggregationId, streamNum);
                }
                return methodResolutionService.makeAccessAggLinearNonJoin(agentInstanceId, groupId, aggregationId, streamNum);
            }
        };
    }

    public AggregationAccessor getAccessor()
    {
        if (indexEvalNode != null) {
            boolean isFirst = stateType == AggregationStateType.FIRST;
            int constant = -1;
            if (indexEvalNode.isConstantResult()) {
                constant = (Integer) indexEvalNode.getExprEvaluator().evaluate(null, true, null);
            }
            return new AggregationAccessorFirstLastIndex(streamNum, childNode, indexEvalNode.getExprEvaluator(), constant, isFirst);
        }
        else {
            if (stateType == AggregationStateType.FIRST) {
                return new AggregationAccessorFirst(streamNum, childNode);
            }
            else if (stateType == AggregationStateType.LAST) {
                return new AggregationAccessorLast(streamNum, childNode);
            }
            else if (stateType == AggregationStateType.WINDOW) {
                return new AggregationAccessorAll(streamNum, childNode, resultType);
            }
        }
        throw new IllegalStateException("Access type is undefined or not known as code '" + stateType + "'");
    }

    public AggregationMethod make(MethodResolutionService methodResolutionService, int agentInstanceId, int groupId, int aggregationId) {
        if (stateType == AggregationStateType.FIRST) {
            return methodResolutionService.makeFirstEverValueAggregator(agentInstanceId, groupId, aggregationId, resultType, false);
        }
        else if (stateType == AggregationStateType.LAST) {
            return methodResolutionService.makeLastEverValueAggregator(agentInstanceId, groupId, aggregationId, resultType, false);
        }
        throw new RuntimeException("Window aggregation function is not available");
    }

    public AggregationMethodFactory getPrototypeAggregator() {
        return this;
    }
}