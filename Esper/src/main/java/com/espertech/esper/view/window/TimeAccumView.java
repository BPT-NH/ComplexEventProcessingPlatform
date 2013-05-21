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
import com.espertech.esper.collection.ViewUpdatedCollection;
import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.core.service.EPStatementHandleCallback;
import com.espertech.esper.core.service.ExtensionServicesContext;
import com.espertech.esper.schedule.ScheduleHandleCallback;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esper.util.StopCallback;
import com.espertech.esper.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A data window view that holds events in a stream and only removes events from a stream (rstream) if
 * no more events arrive for a given time interval.
 * <p>
 * No batch version of the view exists as the batch version is simply the remove stream of this view, which removes
 * in batches.
 * <p>
 * The view is continuous, the insert stream consists of arriving events. The remove stream
 * only posts current window contents when no more events arrive for a given timer interval.
 */
public class TimeAccumView extends ViewSupport implements CloneableView, DataWindowView, StoppableView, StopCallback
{
    // View parameters
    private final TimeAccumViewFactory factory;
    protected final AgentInstanceViewFactoryChainContext agentInstanceContext;
    protected final long msecIntervalSize;
    protected final ViewUpdatedCollection viewUpdatedCollection;
    protected final ScheduleSlot scheduleSlot;

    // Current running parameters
    protected ArrayList<EventBean> currentBatch = new ArrayList<EventBean>();
    protected long callbackScheduledTime;
    protected EPStatementHandleCallback handle;

    /**
     * Constructor.
     * @param msecIntervalSize is the number of milliseconds to batch events for
     * @param viewUpdatedCollection is a collection that the view must update when receiving events
     * @param timeBatchViewFactory fr copying this view in a group-by
     * @param agentInstanceContext is required view services
     */
    public TimeAccumView(TimeAccumViewFactory timeBatchViewFactory,
                         AgentInstanceViewFactoryChainContext agentInstanceContext,
                         long msecIntervalSize,
                         ViewUpdatedCollection viewUpdatedCollection)
    {
        this.agentInstanceContext = agentInstanceContext;
        this.factory = timeBatchViewFactory;
        this.msecIntervalSize = msecIntervalSize;
        this.viewUpdatedCollection = viewUpdatedCollection;

        this.scheduleSlot = agentInstanceContext.getStatementContext().getScheduleBucket().allocateSlot();

        ScheduleHandleCallback callback = new ScheduleHandleCallback() {
            public void scheduledTrigger(ExtensionServicesContext extensionServicesContext)
            {
                TimeAccumView.this.sendRemoveStream();
            }
        };
        handle = new EPStatementHandleCallback(agentInstanceContext.getEpStatementAgentInstanceHandle(), callback);
        agentInstanceContext.addTerminationCallback(this);
    }

    public View cloneView()
    {
        return factory.makeView(agentInstanceContext);
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

    public void update(EventBean[] newData, EventBean[] oldData)
    {
        // we don't care about removed data from a prior view
        if ((newData == null) || (newData.length == 0))
        {
            return;
        }

        // If we have an empty window about to be filled for the first time, addSchedule a callback
        boolean removeSchedule = false;
        boolean addSchedule = false;
        long timestamp = agentInstanceContext.getStatementContext().getSchedulingService().getTime();

        if (!currentBatch.isEmpty())
        {
            // check if we need to reschedule
            long callbackTime = timestamp + msecIntervalSize;
            if (callbackTime != callbackScheduledTime)
            {
                removeSchedule = true;
                addSchedule = true;
            }
        }
        else
        {
            addSchedule = true;
        }

        if (removeSchedule)
        {
            agentInstanceContext.getStatementContext().getSchedulingService().remove(handle, scheduleSlot);
        }
        if (addSchedule)
        {
            agentInstanceContext.getStatementContext().getSchedulingService().add(msecIntervalSize, handle, scheduleSlot);
            callbackScheduledTime = msecIntervalSize + timestamp;
        }

        // add data points to the window
        for (EventBean newEvent : newData) {
            currentBatch.add(newEvent);
        }

        // forward insert stream to child views
        if (viewUpdatedCollection != null)
        {
            viewUpdatedCollection.update(newData, null);
        }

        // update child views
        if (this.hasViews())
        {
            updateChildren(newData, null);
        }
    }

    /**
     * This method sends the remove stream for all accumulated events.
     */
    protected void sendRemoveStream()
    {
        callbackScheduledTime = -1;

        // If there are child views and the batch was filled, fireStatementStopped update method
        if (this.hasViews())
        {
            // Convert to object arrays
            EventBean[] oldData = null;
            if (!currentBatch.isEmpty())
            {
                oldData = currentBatch.toArray(new EventBean[currentBatch.size()]);
            }

            // Post old data
            if (viewUpdatedCollection != null)
            {
                viewUpdatedCollection.update(null, oldData);
            }

            if (oldData != null)
            {
                updateChildren(null, oldData);
            }
        }

        currentBatch.clear();
    }

    /**
     * Returns true if the window is empty, or false if not empty.
     * @return true if empty
     */
    public boolean isEmpty()
    {
        return currentBatch.isEmpty();
    }

    public final Iterator<EventBean> iterator()
    {
        return currentBatch.iterator();
    }

    public final String toString()
    {
        return this.getClass().getName() + " msecIntervalSize=" + msecIntervalSize;
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

    private static final Log log = LogFactory.getLog(TimeAccumView.class);
}
