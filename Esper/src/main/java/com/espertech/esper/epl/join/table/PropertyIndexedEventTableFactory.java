/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.join.table;

import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.client.EventType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;

/**
 * Index factory that organizes events by the event property values into hash buckets. Based on a HashMap
 * with {@link com.espertech.esper.collection.MultiKeyUntyped} keys that store the property values.
 *
 * Takes a list of property names as parameter. Doesn't care which event type the events have as long as the properties
 * exist. If the same event is added twice, the class throws an exception on add.
 */
public class PropertyIndexedEventTableFactory implements EventTableFactory
{
    protected final int streamNum;
    protected final String[] propertyNames;
    protected final boolean unique;
    protected final String optionalIndexName;

    /**
     * Getters for properties.
     */
    protected final EventPropertyGetter[] propertyGetters;

    /**
     * Ctor.
     * @param streamNum - the stream number that is indexed
     * @param eventType - types of events indexed
     * @param propertyNames - property names to use for indexing
     * @param unique
     * @param optionalIndexName
     */
    public PropertyIndexedEventTableFactory(int streamNum, EventType eventType, String[] propertyNames, boolean unique, String optionalIndexName)
    {
        this.streamNum = streamNum;
        this.propertyNames = propertyNames;
        this.unique = unique;
        this.optionalIndexName = optionalIndexName;

        // Init getters
        propertyGetters = new EventPropertyGetter[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++)
        {
            propertyGetters[i] = eventType.getGetter(propertyNames[i]);
        }
    }

    public EventTable makeEventTable() {
        if (unique) {
            return new PropertyIndexedEventTableUnique(streamNum, propertyGetters, optionalIndexName);
        }
        else {
            return new PropertyIndexedEventTable(streamNum, propertyGetters);
        }
    }

    public Class getEventTableClass() {
        if (unique) {
            return PropertyIndexedEventTableUnique.class;
        }
        else {
            return PropertyIndexedEventTable.class;
        }
    }

    public String toQueryPlan()
    {
        return this.getClass().getSimpleName() +
                (unique ? " unique" : " non-unique") +
                " streamNum=" + streamNum +
                " propertyNames=" + Arrays.toString(propertyNames);
    }

    private static Log log = LogFactory.getLog(PropertyIndexedEventTableFactory.class);
}
