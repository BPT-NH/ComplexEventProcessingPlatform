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
import com.espertech.esper.event.EventBeanWriter;
import com.espertech.esper.event.EventPropertyWriter;
import com.espertech.esper.event.MappedEventBean;

import java.util.Map;

/**
 * Writer method for writing to Map-type events.
 */
public class MapEventBeanWriterPerProp implements EventBeanWriter
{
    private final MapEventBeanPropertyWriter[] writers;

    /**
     * Ctor.
     * @param writers names of properties to write
     */
    public MapEventBeanWriterPerProp(MapEventBeanPropertyWriter[] writers)
    {
        this.writers = writers;
    }

    /**
     * Write values to an event.
     * @param values to write
     * @param theEvent to write to
     */
    public void write(Object[] values, EventBean theEvent)
    {
        MappedEventBean mappedEvent = (MappedEventBean) theEvent;
        Map<String, Object> map = mappedEvent.getProperties();

        for (int i = 0; i < writers.length; i++)
        {
            writers[i].write(values[i], map);
        }
    }
}
