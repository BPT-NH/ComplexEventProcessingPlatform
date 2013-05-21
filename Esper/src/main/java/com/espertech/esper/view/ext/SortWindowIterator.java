/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.ext;

import com.espertech.esper.collection.MixedEventBeanAndCollectionIteratorBase;

import java.util.SortedMap;

/**
 * Iterator for use by {@link SortWindowView}.
 */
public final class SortWindowIterator extends MixedEventBeanAndCollectionIteratorBase
{
    private final SortedMap<Object, Object> window;

    /**
     * Ctor.
     * @param window - sorted map with events
     */
    public SortWindowIterator(SortedMap<Object, Object> window)
    {
        super(window.keySet().iterator());
        this.window = window;
        init();
    }

    protected Object getValue(Object iteratorKeyValue) {
        return window.get(iteratorKeyValue);
    }
}
