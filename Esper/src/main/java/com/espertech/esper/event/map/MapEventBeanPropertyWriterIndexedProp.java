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

import java.lang.reflect.Array;
import java.util.Map;

public class MapEventBeanPropertyWriterIndexedProp extends MapEventBeanPropertyWriter {

    private final int index;

    public MapEventBeanPropertyWriterIndexedProp(String propertyName, int index) {
        super(propertyName);
        this.index = index;
    }

    @Override
    public void write(Object value, Map<String, Object> map) {
        Object arrayEntry = map.get(propertyName);
        if (arrayEntry != null && arrayEntry.getClass().isArray() && Array.getLength(arrayEntry) > index) {
            Array.set(arrayEntry, index, value);
        }
    }
}
