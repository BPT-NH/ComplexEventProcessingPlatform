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

/**
 * Getter for Map-entries with well-defined fragment type.
 */
public class ObjectArrayArrayPropertyGetter implements ObjectArrayEventPropertyGetterAndIndexed
{
    private final int propertyIndex;
    private final int index;
    private final EventAdapterService eventAdapterService;
    private final EventType fragmentType;

    /**
     * Ctor.
     * @param propertyIndex property index
     * @param index array index
     * @param eventAdapterService factory for event beans and event types
     * @param fragmentType type of the entry returned
     */
    public ObjectArrayArrayPropertyGetter(int propertyIndex, int index, EventAdapterService eventAdapterService, EventType fragmentType)
    {
        this.propertyIndex = propertyIndex;
        this.index = index;
        this.fragmentType = fragmentType;
        this.eventAdapterService = eventAdapterService;
    }

    public boolean isObjectArrayExistsProperty(Object[] array)
    {
        return true;
    }

    public Object getObjectArray(Object[] array) throws PropertyAccessException
    {
        return getObjectArrayInternal(array, index);
    }

    public Object get(EventBean eventBean, int index) throws PropertyAccessException {
        Object[] array = BaseNestableEventUtil.checkedCastUnderlyingObjectArray(eventBean);
        return getObjectArrayInternal(array, index);
    }

    public Object get(EventBean obj) throws PropertyAccessException
    {
        Object[] array = BaseNestableEventUtil.checkedCastUnderlyingObjectArray(obj);
        return getObjectArray(array);
    }

    private Object getObjectArrayInternal(Object[] array, int index) throws PropertyAccessException
    {
        Object value = array[propertyIndex];
        return BaseNestableEventUtil.getIndexedValue(value, index);
    }

    public boolean isExistsProperty(EventBean eventBean)
    {
        return true;
    }

    public Object getFragment(EventBean obj) throws PropertyAccessException
    {
        Object fragmentUnderlying = get(obj);
        return BaseNestableEventUtil.getFragmentNonPojo(eventAdapterService, fragmentUnderlying, fragmentType);
    }
}