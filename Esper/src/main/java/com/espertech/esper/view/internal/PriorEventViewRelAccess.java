/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.internal;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.view.window.RelativeAccessByEventNIndex;

import java.util.Collection;
import java.util.Iterator;

public class PriorEventViewRelAccess implements RelativeAccessByEventNIndex
{
    private final RelativeAccessByEventNIndex buffer;
    private final int relativeIndex;

    /**
     * Ctor.
     * @param buffer is the buffer to acces
     * @param relativeIndex is the index to pull out
     */
    public PriorEventViewRelAccess(RelativeAccessByEventNIndex buffer, int relativeIndex)
    {
        this.buffer = buffer;
        this.relativeIndex = relativeIndex;
    }

    public EventBean getRelativeToEvent(EventBean theEvent, int prevIndex)
    {
        return buffer.getRelativeToEvent(theEvent, relativeIndex);
    }

    public EventBean getRelativeToEnd(EventBean theEvent, int index)
    {
        // No requirement to index from end of current buffer
        return null;
    }

    public Iterator<EventBean> getWindowToEvent(Object evalEvent)
    {
        return null;
    }

    public Collection<EventBean> getWindowToEventCollReadOnly(Object evalEvent) {
        return null;
    }

    public int getWindowToEventCount(EventBean evalEvent)
    {
        return 0;
    }
}
