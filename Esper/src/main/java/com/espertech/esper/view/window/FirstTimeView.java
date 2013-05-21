/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.window;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.OneEventCollection;
import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.core.service.EPStatementHandleCallback;
import com.espertech.esper.core.service.ExtensionServicesContext;
import com.espertech.esper.schedule.ScheduleHandleCallback;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esper.util.StopCallback;
import com.espertech.esper.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 *
 */
public class FirstTimeView extends ViewSupport implements CloneableView, StoppableView, DataWindowView, StopCallback {
    private final FirstTimeViewFactory timeFirstViewFactory;
    protected final AgentInstanceViewFactoryChainContext agentInstanceContext;
    protected final long msecIntervalSize;
    protected final ScheduleSlot scheduleSlot;
    protected EPStatementHandleCallback handle;

    // Current running parameters
    protected LinkedHashSet<EventBean> events = new LinkedHashSet<EventBean>();
    protected boolean isClosed;

    /**
     * Constructor.
     * @param msecIntervalSize is the number of milliseconds to batch events for
     * @param timeFirstViewFactory fr copying this view in a group-by
     */
    public FirstTimeView(FirstTimeViewFactory timeFirstViewFactory,
                         AgentInstanceViewFactoryChainContext agentInstanceContext,
                         long msecIntervalSize)
    {
        this.agentInstanceContext = agentInstanceContext;
        this.timeFirstViewFactory = timeFirstViewFactory;
        this.msecIntervalSize = msecIntervalSize;

        this.scheduleSlot = agentInstanceContext.getStatementContext().getScheduleBucket().allocateSlot();

        scheduleCallback();

        agentInstanceContext.addTerminationCallback(this);
    }

    public View cloneView()
    {
        return timeFirstViewFactory.makeView(agentInstanceContext);
    }

    /**
     * Returns the interval size in milliseconds.
     * @return batch size
     */
    public final long getMsecIntervalSize()
    {
        return msecIntervalSize;
    }

    public final EventType getEventType()
    {
        return parent.getEventType();
    }

    public final void update(EventBean[] newData, EventBean[] oldData)
    {
        OneEventCollection oldDataToPost = null;
        if (oldData != null)
        {
            for (EventBean anOldData : oldData)
            {
                boolean removed = events.remove(anOldData);
                if (removed)
                {
                    if (oldDataToPost == null)
                    {
                        oldDataToPost = new OneEventCollection();
                    }
                    oldDataToPost.add(anOldData);
                    internalHandleRemoved(anOldData);
                }
            }
        }

        // add data points to the timeWindow
        OneEventCollection newDataToPost = null;
        if ((!isClosed) && (newData != null))
        {
            for (EventBean aNewData : newData)
            {
                events.add(aNewData);
                if (newDataToPost == null)
                {
                    newDataToPost = new OneEventCollection();
                }
                newDataToPost.add(aNewData);
                internalHandleAdded(aNewData);
            }
        }

        // If there are child views, call update method
        if ((this.hasViews()) && ((newDataToPost != null) || (oldDataToPost != null)))
        {
            updateChildren((newDataToPost != null) ? newDataToPost.toArray() : null,
                           (oldDataToPost != null) ? oldDataToPost.toArray() : null);
        }
    }

    public void internalHandleAdded(EventBean newEvent) {
        // no action
    }

    public void internalHandleRemoved(EventBean oldEvent) {
        // no action
    }

    public void internalHandleClosed() {
        // no action
    }


    /**
     * Returns true if the window is empty, or false if not empty.
     * @return true if empty
     */
    public boolean isEmpty()
    {
        return events.isEmpty();
    }

    public final Iterator<EventBean> iterator()
    {
        return events.iterator();
    }

    public final String toString()
    {
        return this.getClass().getName() +
                " msecIntervalSize=" + msecIntervalSize;
    }

    private void scheduleCallback()
    {
        long afterMSec = this.msecIntervalSize;

        ScheduleHandleCallback callback = new ScheduleHandleCallback() {
            public void scheduledTrigger(ExtensionServicesContext extensionServicesContext)
            {
                FirstTimeView.this.isClosed = true;
                internalHandleClosed();
            }
        };
        handle = new EPStatementHandleCallback(agentInstanceContext.getEpStatementAgentInstanceHandle(), callback);
        agentInstanceContext.getStatementContext().getSchedulingService().add(afterMSec, handle, scheduleSlot);
    }

    public void stopView() {
        stopSchedule();
        agentInstanceContext.removeTerminationCallback(this);
    }

    public void stop() {
        stopSchedule();
    }

    public void stopSchedule() {
        if (handle != null) {
            agentInstanceContext.getStatementContext().getSchedulingService().remove(handle, scheduleSlot);
        }
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public LinkedHashSet<EventBean> getEvents() {
        return events;
    }

    private static final Log log = LogFactory.getLog(TimeBatchViewRStream.class);
}
