/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.util;

import com.espertech.esper.collection.MultiKeyUntyped;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

/**
 * A comparator on objects that takes a boolean array for ascending/descending.
 */
public final class ObjectComparator implements Comparator<Object>, MetaDefItem, Serializable
{
    private static final long serialVersionUID = -2139033245746311007L;
    private final boolean isDescendingValue;

    /**
     * Ctor.
     * @param isDescendingValue ascending or descending
     */
    public ObjectComparator(boolean isDescendingValue)
    {
        this.isDescendingValue = isDescendingValue;
    }

    public final int compare(Object firstValue, Object secondValue)
    {
        return MultiKeyComparator.compareValues(firstValue, secondValue, isDescendingValue);
    }
}
