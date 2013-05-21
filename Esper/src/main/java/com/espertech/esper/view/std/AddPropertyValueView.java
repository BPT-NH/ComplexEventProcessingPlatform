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
import com.espertech.esper.collection.MultiKeyUntyped;
import com.espertech.esper.collection.OneEventCollection;
import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This view simply adds a property to the events posted to it. This is useful for the group-merge views.
 */
public final class AddPropertyValueView extends ViewSupport implements CloneableView, StoppableView
{
    private final AgentInstanceViewFactoryChainContext agentInstanceContext;
    private final String[] propertyNames;
    private final Object propertyValues;
    private final EventType eventType;
    private boolean mustAddProperty;

    // Keep a history of posted old events to avoid reconstructing the event
    // and adhere to the contract of posting the same reference to child views
    private Map<EventBean, EventBean> newToOldEventMap;

    /**
     * Constructor.
     * @param propertyNames is the name of the field that is added to any events received by this view.
     * @param mergeValues is the values of the field that is added to any events received by this view.
     * @param mergedResultEventType is the event type that the merge view reports to it's child views
     * @param agentInstanceContext contains required view services
     */
    public AddPropertyValueView(AgentInstanceViewFactoryChainContext agentInstanceContext, String[] propertyNames, Object mergeValues, EventType mergedResultEventType)
    {
        this.propertyNames = propertyNames;
        this.propertyValues = mergeValues;
        this.eventType = mergedResultEventType;
        this.agentInstanceContext = agentInstanceContext;
        newToOldEventMap = new HashMap<EventBean, EventBean>();
    }

    public View cloneView()
    {
        return new AddPropertyValueView(agentInstanceContext, propertyNames, propertyValues, eventType);
    }

    public void setParent(Viewable parent)
    {
        if (log.isDebugEnabled())
        {
            log.debug(".setParent parent=" + parent);
        }
        super.setParent(parent);

        if (parent.getEventType() != eventType)
        {
            mustAddProperty = true;
        }
        else
        {
            mustAddProperty = false;
        }
    }

    /**
     * Returns field name for which to set the merge value for.
     * @return field name to use to set value
     */
    public final String[] getPropertyNames()
    {
        return propertyNames;
    }

    /**
     * Returns the value to set for the field.
     * @return value to set
     */
    public final Object getPropertyValues()
    {
        return propertyValues;
    }

    public final void update(EventBean[] newData, EventBean[] oldData)
    {
        if (!mustAddProperty)
        {
            updateChildren(newData, oldData);
            return;
        }

        EventBean[] newEvents = null;
        EventBean[] oldEvents = null;

        if (newData != null)
        {
            newEvents = new EventBean[newData.length];

            int index = 0;
            for (EventBean newEvent : newData)
            {
                EventBean theEvent = addProperty(newEvent, propertyNames, propertyValues, eventType, agentInstanceContext.getStatementContext().getEventAdapterService());
                newEvents[index++] = theEvent;

                newToOldEventMap.put(newEvent, theEvent);
            }
        }

        if (oldData != null)
        {
            oldEvents = new EventBean[oldData.length];

            int index = 0;
            for (EventBean oldEvent : oldData)
            {
                EventBean outgoing = newToOldEventMap.remove(oldEvent);
                if (outgoing != null)
                {
                    oldEvents[index++] = outgoing;
                }
                else
                {
                    EventBean theEvent = addProperty(oldEvent, propertyNames, propertyValues, eventType, agentInstanceContext.getStatementContext().getEventAdapterService());
                    oldEvents[index++] = theEvent;
                }
            }
        }

        updateChildren(newEvents, oldEvents);
    }

    public final EventType getEventType()
    {
        return eventType;
    }

    public final Iterator<EventBean> iterator()
    {
        final Iterator<EventBean> parentIterator = parent.iterator();

        return new Iterator<EventBean>()
        {
            public boolean hasNext()
            {
                return parentIterator.hasNext();
            }

            public EventBean next()
            {
                EventBean nextEvent = parentIterator.next();
                if (mustAddProperty)
                {
                    return addProperty(nextEvent, propertyNames, propertyValues, eventType,
                            agentInstanceContext.getStatementContext().getEventAdapterService());
                }
                else
                {
                    return nextEvent;
                }
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void stopView() {
        OneEventCollection oldEvents = new OneEventCollection();
        for (Map.Entry<EventBean, EventBean> oldEvent : newToOldEventMap.entrySet()) {
            oldEvents.add(oldEvent.getValue());
        }
        if (!oldEvents.isEmpty()) {
            updateChildren(null, oldEvents.toArray());
        }
        newToOldEventMap.clear();
    }

    /**
     * Add a property to the event passed in.
     * @param originalEvent - event to add property to
     * @param propertyNames - names of properties to add
     * @param propertyValues - value of properties to add
     * @param targetEventType - new event type
     * @param eventAdapterService - service for generating events and handling event types
     * @return event with added property
     */
    protected static EventBean addProperty(EventBean originalEvent,
                                       String[] propertyNames,
                                       Object propertyValues,
                                       EventType targetEventType,
                                       EventAdapterService eventAdapterService)
    {
        Map<String, Object> values = new HashMap<String, Object>();
        if (propertyValues instanceof MultiKeyUntyped) {
            MultiKeyUntyped props = (MultiKeyUntyped) propertyValues;
            Object[] propertyValuesArr = props.getKeys();

            for (int i = 0; i < propertyNames.length; i++)
            {
                values.put(propertyNames[i], propertyValuesArr[i]);
            }
        }
        else {
            values.put(propertyNames[0], propertyValues);
        }

        return eventAdapterService.adapterForTypedWrapper(originalEvent, values, targetEventType);
    }

    public final String toString()
    {
        return this.getClass().getName() + " propertyNames=" + Arrays.toString(propertyNames) +
                " propertyValue=" + propertyValues;
    }

    private static final Log log = LogFactory.getLog(AddPropertyValueView.class);
}
