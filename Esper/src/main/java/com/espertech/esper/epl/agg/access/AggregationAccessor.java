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

/**
 * Accessor for access aggregation functions.
 */
public interface AggregationAccessor
{
    /**
     * Return the aggregation state value either as a scalar value or any other object.
     * <p>
     *     For enumeration over scalar values or objects return an array or collection of scalar or object values.
     * </p>
     * <p>
     *     Use the #getEnumerableEvents method to return a collection of events.
     * </p>
     * <p>
     *     Use the #getEnumerableEvent to return a single events.
     * </p>
     * @param state aggregation state, downcast as needed
     * @return return value
     */
    public Object getValue(AggregationState state);

    /**
     * Return the aggregation state value consisting of a collection of events.
     * @param state aggregation state, downcast as needed
     * @return return collection of events or null or empty collection
     */
    public Collection<EventBean> getEnumerableEvents(AggregationState state);

    /**
     * Return the aggregation state value consisting of a single event.
     * @param state aggregation state, downcast as needed
     * @return return event or null
     */
    public EventBean getEnumerableEvent(AggregationState state);
}
