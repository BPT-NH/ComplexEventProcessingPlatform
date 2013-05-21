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

package com.espertech.esper.epl.agg.access;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.expression.ExprEvaluator;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents the aggregation accessor that provides the result for the "first" and "last" aggregation function with index.
 */
public class AggregationAccessorFirstLastIndex implements AggregationAccessor
{
    private final int streamNum;
    private final ExprEvaluator childNode;
    private final EventBean[] eventsPerStream;
    private final ExprEvaluator indexNode;
    private final int constant;
    private final boolean isFirst;

    /**
     * Ctor.
     * @param streamNum stream id
     * @param childNode expression
     * @param indexNode index expression
     * @param constant constant index
     * @param isFirst true if returning first, false for returning last
     */
    public AggregationAccessorFirstLastIndex(int streamNum, ExprEvaluator childNode, ExprEvaluator indexNode, int constant, boolean isFirst)
    {
        this.streamNum = streamNum;
        this.childNode = childNode;
        this.indexNode = indexNode;
        this.eventsPerStream = new EventBean[streamNum + 1];
        this.constant = constant;
        this.isFirst = isFirst;
    }

    public Object getValue(AggregationState state) {

        EventBean bean = getBean(state);
        if (bean == null) {
            return null;
        }
        eventsPerStream[streamNum] = bean;
        return childNode.evaluate(eventsPerStream, true, null);
    }

    public Collection<EventBean> getEnumerableEvents(AggregationState state) {
        EventBean bean = getBean(state);
        if (bean == null) {
            return null;
        }
        return Collections.singletonList(bean);
    }

    public EventBean getEnumerableEvent(AggregationState state) {
        return getBean(state);
    }

    private EventBean getBean(AggregationState state) {
        EventBean bean;
        int index = constant;
        if (index == -1) {
            Object result = indexNode.evaluate(null, true, null);
            if ((result == null) || (!(result instanceof Integer))) {
                return null;
            }
            index = (Integer) result;
        }
        if (isFirst) {
            bean = ((AggregationStateLinear) state).getFirstNthValue(index);
        }
        else {
            bean = ((AggregationStateLinear) state).getLastNthValue(index);
        }
        return bean;
    }
}