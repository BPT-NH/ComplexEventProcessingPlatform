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
import com.espertech.esper.event.EventBeanUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Index factory that organizes events by the event property values into hash buckets. Based on a HashMap
 * with {@link com.espertech.esper.collection.MultiKeyUntyped} keys that store the property values.
 */
public class PropertyIndexedEventTableSingleFactory implements EventTableFactory
{
    protected final int streamNum;
    protected final String propertyName;
    protected final boolean unique;
    protected final String optionalIndexName;

    protected final EventPropertyGetter propertyGetter;

    /**
     * Ctor.
     * @param streamNum - the stream number that is indexed
     * @param eventType - types of events indexed
     */
    public PropertyIndexedEventTableSingleFactory(int streamNum, EventType eventType, String propertyName, boolean unique, String optionalIndexName)
    {
        this.streamNum = streamNum;
        this.propertyName = propertyName;
        this.unique = unique;
        this.optionalIndexName = optionalIndexName;

        // Init getters
        propertyGetter = EventBeanUtility.getAssertPropertyGetter(eventType, propertyName);
    }

    public EventTable makeEventTable() {
        if (unique) {
            return new PropertyIndexedEventTableSingleUnique(streamNum, propertyGetter, optionalIndexName);
        }
        else {
            return new PropertyIndexedEventTableSingle(streamNum, propertyGetter);
        }
    }

    public Class getEventTableClass() {
        if (unique) {
            return PropertyIndexedEventTableSingleUnique.class;
        }
        else {
            return PropertyIndexedEventTableSingle.class;
        }
    }

    public String toQueryPlan() {
        return this.getClass().getSimpleName() +
                (unique ? " unique" : " non-unique") +
                " streamNum=" + streamNum +
                " propertyName=" + propertyName;
    }

    private static Log log = LogFactory.getLog(PropertyIndexedEventTableSingleFactory.class);
}
