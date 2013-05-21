/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator for an array of items.
 */
public class EventTypeIndexBuilderIndexLookupableIterator implements Iterator<EventTypeIndexBuilderIndexLookupablePair>
{
    private final EventTypeIndexBuilderIndexLookupablePair[] items;
    private int position;

    public EventTypeIndexBuilderIndexLookupableIterator(EventTypeIndexBuilderIndexLookupablePair[] items)
    {
        this.items = items;
    }

    public boolean hasNext()
    {
        if (position >= items.length)
        {
            return false;
        }
        return true;
    }

    public EventTypeIndexBuilderIndexLookupablePair next()
    {
        if (position >= items.length)
        {
            throw new NoSuchElementException();
        }
        return items[position++];
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
