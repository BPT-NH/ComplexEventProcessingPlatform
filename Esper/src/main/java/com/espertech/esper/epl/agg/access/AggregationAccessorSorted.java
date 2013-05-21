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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents the aggregation accessor that provides the result for the "maxBy" aggregation function.
 */
public class AggregationAccessorSorted implements AggregationAccessor
{
    private final boolean max;
    private final Class componentType;

    public AggregationAccessorSorted(boolean max, Class componentType) {
        this.max = max;
        this.componentType = componentType;
    }

    public Object getValue(AggregationState state) {
        AggregationStateSorted sorted = (AggregationStateSorted) state;
        if (sorted.size() == 0) {
            return null;
        }
        Object array = Array.newInstance(componentType, sorted.size());

        Iterator<EventBean> it;
        if (max) {
            it = sorted.getReverseIterator();
        }
        else {
            it = sorted.iterator();
        }

        int count = 0;
        for (;it.hasNext();) {
            EventBean bean = it.next();
            Array.set(array, count++, bean.getUnderlying());
        }
        return array;
    }

    public Collection<EventBean> getEnumerableEvents(AggregationState state) {
        return ((AggregationStateSorted) state).collectionReadOnly();
    }

    public EventBean getEnumerableEvent(AggregationState state) {
        return null;
    }
}