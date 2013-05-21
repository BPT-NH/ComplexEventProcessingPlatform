/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Encapsulates the information required by {@link IndexTreeBuilder} to maintain the filter parameter tree structure
 * when filters are added and removed from the tree.
 */
public class IndexTreePath
{
    private final ArrayDeque<EventTypeIndexBuilderIndexLookupablePair> indizes;

    /**
     * Constructor.
     */
    public IndexTreePath()
    {
        indizes = new ArrayDeque<EventTypeIndexBuilderIndexLookupablePair>(2);
    }

    /**
     * Add an index to end of the list representing a path through indexes.
     * @param index to add
     * @param filteredForValue is the value the index filters
     */
    public final void add(FilterParamIndexBase index, Object filteredForValue)
    {
        indizes.add(new EventTypeIndexBuilderIndexLookupablePair(index, filteredForValue));
    }

    public final String toString()
    {
        return Arrays.toString(indizes.toArray());
    }

    public EventTypeIndexBuilderIndexLookupablePair[] toArray() {
        return indizes.toArray(new EventTypeIndexBuilderIndexLookupablePair[indizes.size()]);
    }
}

