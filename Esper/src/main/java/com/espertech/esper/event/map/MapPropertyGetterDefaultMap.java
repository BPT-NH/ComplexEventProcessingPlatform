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
import com.espertech.esper.event.arr.ObjectArrayPropertyGetterDefaultBase;

/**
 * Getter for map entry.
 */
public class MapPropertyGetterDefaultMap extends MapPropertyGetterDefaultBase
{
    public MapPropertyGetterDefaultMap(String propertyName, EventType fragmentEventType, EventAdapterService eventAdapterService) {
        super(propertyName, fragmentEventType, eventAdapterService);
    }

    protected Object handleCreateFragment(Object value) {
        return BaseNestableEventUtil.handleCreateFragmentMap(value, fragmentEventType, eventAdapterService);
    }
}
