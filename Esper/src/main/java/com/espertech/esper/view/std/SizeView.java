/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.std;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.SingleEventIterator;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.view.CloneableView;
import com.espertech.esper.view.View;
import com.espertech.esper.view.ViewFieldEnum;
import com.espertech.esper.view.ViewSupport;
import com.espertech.esper.view.stat.StatViewAdditionalProps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This view is a very simple view presenting the number of elements in a stream or view.
 * The view computes a single long-typed count of the number of events passed through it similar
 * to the base statistics COUNT column.
 */
public class SizeView extends ViewSupport implements CloneableView
{
    private final AgentInstanceContext agentInstanceContext;
    private final EventType eventType;
    private final StatViewAdditionalProps additionalProps;

    protected long size = 0;
    private EventBean lastSizeEvent;
    protected Object[] lastValuesEventNew;

    /**
     * Ctor.
     * @param agentInstanceContext is services
     */
    public SizeView(AgentInstanceContext agentInstanceContext, EventType eventType, StatViewAdditionalProps additionalProps)
    {
        this.agentInstanceContext = agentInstanceContext;
        this.eventType = eventType;
        this.additionalProps = additionalProps;
    }

    public View cloneView()
    {
        return new SizeView(agentInstanceContext, eventType, additionalProps);
    }

    public final EventType getEventType()
    {
        return eventType;
    }

    public void update(EventBean[] newData, EventBean[] oldData)
    {
        long priorSize = size;

        // If we have child views, keep a reference to the old values, so we can update them as old data event.
        EventBean oldDataMap = null;
        if (lastSizeEvent == null)
        {
            if (this.hasViews())
            {
                Map<String, Object> postOldData = new HashMap<String, Object>();
                postOldData.put(ViewFieldEnum.SIZE_VIEW__SIZE.getName(), priorSize);
                addProperties(postOldData);
                oldDataMap = agentInstanceContext.getStatementContext().getEventAdapterService().adapterForTypedMap(postOldData, eventType);
            }
        }

        // add data points to the window
        if (newData != null)
        {
            size += newData.length;

            if ((additionalProps != null) && (newData.length != 0)) {
                if (lastValuesEventNew == null) {
                    lastValuesEventNew = new Object[additionalProps.getAdditionalExpr().length];
                }
                for (int val = 0; val < additionalProps.getAdditionalExpr().length; val++) {
                    lastValuesEventNew[val] = additionalProps.getAdditionalExpr()[val].evaluate(new EventBean[] {newData[newData.length - 1]}, true, agentInstanceContext);
                }
            }
        }

        if (oldData != null)
        {
            size -= oldData.length;
        }

        // If there are child views, fireStatementStopped update method
        if ((this.hasViews()) && (priorSize != size))
        {
            Map<String, Object> postNewData = new HashMap<String, Object>();
            postNewData.put(ViewFieldEnum.SIZE_VIEW__SIZE.getName(), size);
            addProperties(postNewData);
            EventBean newEvent = agentInstanceContext.getStatementContext().getEventAdapterService().adapterForTypedMap(postNewData, eventType);

            if (lastSizeEvent != null)
            {
                updateChildren(new EventBean[] {newEvent}, new EventBean[] {lastSizeEvent});
            }
            else
            {
                updateChildren(new EventBean[] {newEvent}, new EventBean[] {oldDataMap});
            }

            lastSizeEvent = newEvent;
        }
    }

    public final Iterator<EventBean> iterator()
    {
        HashMap<String, Object> current = new HashMap<String, Object>();
        current.put(ViewFieldEnum.SIZE_VIEW__SIZE.getName(), size);
        addProperties(current);
        return new SingleEventIterator(agentInstanceContext.getStatementContext().getEventAdapterService().adapterForTypedMap(current, eventType));
    }

    public final String toString()
    {
        return this.getClass().getName();
    }

    /**
     * Creates the event type for this view
     * @param statementContext is the event adapter service
     * @return event type for view
     */
    public static EventType createEventType(StatementContext statementContext, StatViewAdditionalProps additionalProps, int streamNum)
    {
        Map<String, Object> schemaMap = new HashMap<String, Object>();
        schemaMap.put(ViewFieldEnum.SIZE_VIEW__SIZE.getName(), long.class);
        StatViewAdditionalProps.addCheckDupProperties(schemaMap, additionalProps, ViewFieldEnum.SIZE_VIEW__SIZE);
        String outputEventTypeName = statementContext.getStatementId() + "_sizeview_" + streamNum;
        return statementContext.getEventAdapterService().createAnonymousMapType(outputEventTypeName, schemaMap);
    }

    private void addProperties(Map<String, Object> newDataMap)
    {
        if (additionalProps == null) {
            return;
        }
        additionalProps.addProperties(newDataMap, lastValuesEventNew);
    }
}
