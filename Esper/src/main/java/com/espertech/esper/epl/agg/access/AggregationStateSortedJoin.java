/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.access;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.collection.RefCountedSetAtomicInteger;

/**
 * Implementation of access function for single-stream (not joins).
 */
public class AggregationStateSortedJoin extends AggregationStateSortedImpl
{
    protected final RefCountedSetAtomicInteger refs;

    public AggregationStateSortedJoin(AggregationStateSortedSpec spec) {
        super(spec);
        refs = new RefCountedSetAtomicInteger();
    }

    protected boolean referenceEvent(EventBean theEvent) {
        return refs.add(theEvent);
    }

    protected boolean dereferenceEvent(EventBean theEvent) {
        return refs.remove(theEvent);
    }
}
