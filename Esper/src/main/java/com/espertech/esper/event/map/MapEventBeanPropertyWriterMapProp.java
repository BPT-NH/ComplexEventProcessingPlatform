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

import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.MappedEventBean;

import java.util.Map;

public class MapEventBeanPropertyWriterMapProp extends MapEventBeanPropertyWriter {

    private final String key;

    public MapEventBeanPropertyWriterMapProp(String propertyName, String key) {
        super(propertyName);
        this.key = key;
    }

    @Override
    public void write(Object value, Map<String, Object> map) {
        Map mapEntry = (Map) map.get(propertyName);
        if (mapEntry != null) {
            mapEntry.put(key, value);
        }
    }
}
