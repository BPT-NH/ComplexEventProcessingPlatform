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

package com.espertech.esper.event.arr;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.event.BaseNestableEventUtil;
import com.espertech.esper.event.EventAdapterService;

import java.util.Map;

/**
 * Getter for map array.
 */
public class ObjectArrayFragmentArrayPropertyGetter implements ObjectArrayEventPropertyGetter
{
    private final int propertyIndex;
    private final EventType fragmentEventType;
    private final EventAdapterService eventAdapterService;

    /**
     * Ctor.
     * @param propertyIndex property index
     * @param fragmentEventType event type of fragment
     * @param eventAdapterService for creating event instances
     */
    public ObjectArrayFragmentArrayPropertyGetter(int propertyIndex, EventType fragmentEventType, EventAdapterService eventAdapterService)
    {
        this.propertyIndex = propertyIndex;
        this.fragmentEventType = fragmentEventType;
        this.eventAdapterService = eventAdapterService;
    }

    public Object getObjectArray(Object[] array) throws PropertyAccessException
    {
        return array[propertyIndex];
    }

    public boolean isObjectArrayExistsProperty(Object[] array)
    {
        return true;
    }

    public Object get(EventBean obj) throws PropertyAccessException
    {
        Object[] array = BaseNestableEventUtil.checkedCastUnderlyingObjectArray(obj);
        return getObjectArray(array);
    }

    public boolean isExistsProperty(EventBean eventBean)
    {
        return true;
    }

    public Object getFragment(EventBean eventBean) throws PropertyAccessException
    {
        Object value = get(eventBean);
        return BaseNestableEventUtil.getFragmentArray(eventAdapterService, value, fragmentEventType);
    }
}
