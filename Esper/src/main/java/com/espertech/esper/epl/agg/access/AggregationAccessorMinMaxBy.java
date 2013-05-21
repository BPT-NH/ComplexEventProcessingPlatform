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

import java.util.Collection;
import java.util.Collections;

/**
 * Represents the aggregation accessor that provides the result for the "maxBy" aggregation function.
 */
public class AggregationAccessorMinMaxBy implements AggregationAccessor
{
    private final boolean max;

    public AggregationAccessorMinMaxBy(boolean max) {
        this.max = max;
    }

    public Object getValue(AggregationState state) {
        EventBean event = getEnumerableEvent(state);
        if (event == null) {
            return null;
        }
        return event.getUnderlying();
    }

    public Collection<EventBean> getEnumerableEvents(AggregationState state) {
        EventBean bean = getEnumerableEvent(state);
        if (bean == null) {
            return null;
        }
        return Collections.singletonList(bean);
    }

    public EventBean getEnumerableEvent(AggregationState state) {
        if (max) {
            return ((AggregationStateSorted) state).getLastValue();
        }
        else {
            return ((AggregationStateSorted) state).getFirstValue();
        }
    }
}