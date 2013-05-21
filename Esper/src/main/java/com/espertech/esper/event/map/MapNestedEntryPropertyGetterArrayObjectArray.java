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

package com.espertech.esper.event.map;

import com.espertech.esper.client.EventType;
import com.espertech.esper.event.BaseNestableEventUtil;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.arr.ObjectArrayEventPropertyGetter;

public class MapNestedEntryPropertyGetterArrayObjectArray extends MapNestedEntryPropertyGetterBase {

    private final int index;
    private final ObjectArrayEventPropertyGetter getter;

    public MapNestedEntryPropertyGetterArrayObjectArray(String propertyMap, EventType fragmentType, EventAdapterService eventAdapterService, int index, ObjectArrayEventPropertyGetter getter) {
        super(propertyMap, fragmentType, eventAdapterService);
        this.index = index;
        this.getter = getter;
    }

    public Object handleNestedValue(Object value) {
        return BaseNestableEventUtil.handleNestedValueArrayWithObjectArray(value, index, getter);
    }

    public Object handleNestedValueFragment(Object value) {
        return BaseNestableEventUtil.handleNestedValueArrayWithObjectArrayFragment(value, index, getter, fragmentType, eventAdapterService);
    }
}
