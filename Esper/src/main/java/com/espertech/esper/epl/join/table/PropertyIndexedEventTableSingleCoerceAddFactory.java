/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.join.table;

import com.espertech.esper.client.EventType;
import com.espertech.esper.util.JavaClassHelper;
import com.espertech.esper.util.SimpleNumberCoercer;
import com.espertech.esper.util.SimpleNumberCoercerFactory;

public class PropertyIndexedEventTableSingleCoerceAddFactory extends PropertyIndexedEventTableSingleFactory
{
    protected final SimpleNumberCoercer coercer;
    protected final Class coercionType;

    /**
     * Ctor.
     * @param streamNum is the stream number of the indexed stream
     * @param eventType is the event type of the indexed stream
     * @param propertyName are the property names to get property values
     * @param coercionType are the classes to coerce indexed values to
     */
    public PropertyIndexedEventTableSingleCoerceAddFactory(int streamNum, EventType eventType, String propertyName, Class coercionType)
    {
        super(streamNum, eventType, propertyName, false, null);
        this.coercionType = coercionType;
        if (JavaClassHelper.isNumeric(coercionType)) {
            coercer = SimpleNumberCoercerFactory.getCoercer(null, coercionType);
        }
        else {
            coercer = null;
        }
    }

    @Override
    public EventTable makeEventTable() {
        return new PropertyIndexedEventTableSingleCoerceAdd(streamNum, propertyGetter, coercer, coercionType);
    }
}
