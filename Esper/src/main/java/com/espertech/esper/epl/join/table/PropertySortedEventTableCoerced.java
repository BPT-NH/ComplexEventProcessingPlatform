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
import com.espertech.esper.util.JavaClassHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertySortedEventTableCoerced extends PropertySortedEventTable
{
    private Class coercionType;

    public PropertySortedEventTableCoerced(int streamNum, EventPropertyGetter propertyGetter, Class coercionType) {
        super(streamNum, propertyGetter);
        this.coercionType = coercionType;
    }

    @Override
    protected Object coerce(Object value) {
        if (!value.getClass().equals(coercionType))
        {
            if (value instanceof Number)
            {
                return JavaClassHelper.coerceBoxed((Number) value, coercionType);
            }
        }
        return value;        
    }

    public String toString()
    {
        return "PropertySortedEventTableCoerced" +
                " streamNum=" + streamNum +
                " propertyGetter=" + propertyGetter +
                " coercionType=" + coercionType;
    }

    private static Log log = LogFactory.getLog(PropertySortedEventTableCoerced.class);
}
