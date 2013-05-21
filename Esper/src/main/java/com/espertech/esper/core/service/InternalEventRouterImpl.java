/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.core.service;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventPropertyDescriptor;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.annotation.Drop;
import com.espertech.esper.client.annotation.Priority;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.OnTriggerSetAssignment;
import com.espertech.esper.epl.spec.UpdateDesc;
import com.espertech.esper.event.EventBeanCopyMethod;
import com.espertech.esper.event.EventBeanWriter;
import com.espertech.esper.event.EventTypeSPI;
import com.espertech.esper.util.NullableObject;
import com.espertech.esper.util.TypeWidener;
import com.espertech.esper.util.TypeWidenerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Routing implementation that allows to pre-process events.
 */
public class InternalEventRouterImpl implements InternalEventRouter
{
    private static final Log log = LogFactory.getLog(InternalEventRouterImpl.class);

    private final ConcurrentHashMap<EventType, NullableObject<InternalEventRouterPreprocessor>> preprocessors;
    private final Map<UpdateDesc, IRDescEntry> descriptors;
    private boolean hasPreprocessing = false;
    private InsertIntoListener insertIntoListener;

    /**
     * Ctor.
     */
    public InternalEventRouterImpl()
    {
        this.preprocessors = new ConcurrentHashMap<EventType, NullableObject<InternalEventRouterPreprocessor>>();
        this.descriptors = new LinkedHashMap<UpdateDesc, IRDescEntry>();
    }

    /**
     * Return true to indicate that there is pre-processing to take place.
     * @return preprocessing indicator
     */
    public boolean isHasPreprocessing()
    {
        return hasPreprocessing;
    }

    /**
     * Pre-process the event.
     * @param theEvent to preprocess
     * @param exprEvaluatorContext expression evaluation context
     * @return preprocessed event
     */
    public EventBean preprocess(EventBean theEvent, ExprEvaluatorContext exprEvaluatorContext)
    {
        return getPreprocessedEvent(theEvent, exprEvaluatorContext);
    }

    public void setInsertIntoListener(InsertIntoListener insertIntoListener) {
        this.insertIntoListener = insertIntoListener;
    }

    public void route(EventBean theEvent, EPStatementHandle statementHandle, InternalEventRouteDest routeDest, ExprEvaluatorContext exprEvaluatorContext, boolean addToFront)
    {
        if (!hasPreprocessing)
        {
            if (insertIntoListener != null) {
                boolean route = insertIntoListener.inserted(theEvent, statementHandle);
                if (route) {
                    routeDest.route(theEvent, statementHandle, addToFront);
                }
            }
            else {
                routeDest.route(theEvent, statementHandle, addToFront);
            }
            return;
        }

        EventBean preprocessed = getPreprocessedEvent(theEvent, exprEvaluatorContext);
        if (preprocessed != null)
        {
            if (insertIntoListener != null) {
                boolean route = insertIntoListener.inserted(theEvent, statementHandle);
                if (route) {
                    routeDest.route(preprocessed, statementHandle, addToFront);
                }
            }
            else {
                routeDest.route(preprocessed, statementHandle, addToFront);
            }
        }
    }

    public InternalEventRouterDesc getValidatePreprocessing(EventType eventType, UpdateDesc desc, Annotation[] annotations)
            throws ExprValidationException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Validating route preprocessing for type '" + eventType.getName() + "'");
        }

        if (!(eventType instanceof EventTypeSPI))
        {
            throw new ExprValidationException("Update statements require the event type to implement the " + EventTypeSPI.class + " interface");
        }
        EventTypeSPI eventTypeSPI = (EventTypeSPI) eventType;

        TypeWidener[] wideners = new TypeWidener[desc.getAssignments().size()];
        List<String> properties = new ArrayList<String>();
        for (int i = 0; i < desc.getAssignments().size(); i++)
        {
            OnTriggerSetAssignment assignment = desc.getAssignments().get(i);
            EventPropertyDescriptor writableProperty = eventTypeSPI.getWritableProperty(assignment.getVariableName());

            if (writableProperty == null)
            {
                throw new ExprValidationException("Property '" + assignment.getVariableName() + "' is not available for write access");
            }

            wideners[i] = TypeWidenerFactory.getCheckPropertyAssignType(assignment.getExpression().toExpressionString(), assignment.getExpression().getExprEvaluator().getType(),
                    writableProperty.getPropertyType(), assignment.getVariableName());
            properties.add(assignment.getVariableName());
        }

        // check copy-able
        EventBeanCopyMethod copyMethod = eventTypeSPI.getCopyMethod(properties.toArray(new String[properties.size()]));
        if (copyMethod == null)
        {
            throw new ExprValidationException("The update-clause requires the underlying event representation to support copy (via Serializable by default)");
        }

        return new InternalEventRouterDesc(desc, copyMethod, wideners, eventType, annotations);
    }

    public void addPreprocessing(InternalEventRouterDesc internalEventRouterDesc, InternalRoutePreprocessView outputView, StatementAgentInstanceLock agentInstanceLock, boolean hasSubselect)
    {
        descriptors.put(internalEventRouterDesc.getUpdateDesc(), new IRDescEntry(internalEventRouterDesc, outputView, agentInstanceLock, hasSubselect));

        // remove all preprocessors for this type as well as any known child types, forcing re-init on next use
        removePreprocessors(internalEventRouterDesc.getEventType());

        hasPreprocessing = true;
    }

    public void removePreprocessing(EventType eventType, UpdateDesc desc)
    {
        if (log.isInfoEnabled())
        {
            log.info("Removing route preprocessing for type '" + eventType.getName());
        }

        // remove all preprocessors for this type as well as any known child types
        removePreprocessors(eventType);

        descriptors.remove(desc);
        if (descriptors.isEmpty())
        {
            hasPreprocessing = false;
            preprocessors.clear();
        }
    }

    private EventBean getPreprocessedEvent(EventBean theEvent, ExprEvaluatorContext exprEvaluatorContext)
    {
        NullableObject<InternalEventRouterPreprocessor> processor = preprocessors.get(theEvent.getEventType());
        if (processor == null)
        {
            synchronized (this)
            {
                processor = initialize(theEvent.getEventType());
                preprocessors.put(theEvent.getEventType(), processor);
            }
        }

        if (processor.getObject() == null)
        {
            return theEvent;
        }
        else
        {
            return processor.getObject().process(theEvent, exprEvaluatorContext);
        }
    }

    private void removePreprocessors(EventType eventType)
    {
        preprocessors.remove(eventType);

        // find each child type entry
        for (EventType type : preprocessors.keySet())
        {
            if (type.getDeepSuperTypes() != null)
            {
                for (Iterator<EventType> it = type.getDeepSuperTypes(); it.hasNext();)
                {
                    if (it.next() == eventType)
                    {
                        preprocessors.remove(type);
                    }
                }
            }
        }
    }

    private NullableObject<InternalEventRouterPreprocessor> initialize(EventType eventType)
    {
        EventTypeSPI eventTypeSPI = (EventTypeSPI) eventType;
        List<InternalEventRouterEntry> desc = new ArrayList<InternalEventRouterEntry>();

        // determine which ones to process for this types, and what priority and drop
        Set<String> eventPropertiesWritten = new HashSet<String>();
        for (Map.Entry<UpdateDesc, IRDescEntry> entry : descriptors.entrySet())
        {
            boolean applicable = entry.getValue().getEventType() == eventType;
            if (!applicable)
            {
                if (eventType.getDeepSuperTypes() != null)
                {
                    for (Iterator<EventType> it = eventType.getDeepSuperTypes(); it.hasNext();)
                    {
                        if (it.next() == entry.getValue().getEventType())
                        {
                            applicable = true;
                            break;
                        }
                    }
                }
            }

            if (!applicable)
            {
                continue;
            }

            int priority = 0;
            boolean isDrop = false;
            Annotation[] annotations = entry.getValue().getAnnotations();
            for (int i = 0; i < annotations.length; i++)
            {
                if (annotations[i] instanceof Priority)
                {
                    priority = ((Priority) annotations[i]).value();
                }
                if (annotations[i] instanceof Drop)
                {
                    isDrop = true;
                }
            }

            List<String> properties = new ArrayList<String>();
            ExprNode[] expressions = new ExprNode[entry.getKey().getAssignments().size()];
            for (int i = 0; i < entry.getKey().getAssignments().size(); i++)
            {
                OnTriggerSetAssignment assignment = entry.getKey().getAssignments().get(i);
                expressions[i] = assignment.getExpression();
                properties.add(assignment.getVariableName());
                eventPropertiesWritten.add(assignment.getVariableName());
            }
            EventBeanWriter writer = eventTypeSPI.getWriter(properties.toArray(new String[properties.size()]));
            desc.add(new InternalEventRouterEntry(priority, isDrop, entry.getKey().getOptionalWhereClause(), expressions, writer, entry.getValue().getWideners(), entry.getValue().getOutputView(), entry.getValue().getAgentInstanceLock(), entry.getValue().hasSubselect));
        }

        EventBeanCopyMethod copyMethod = eventTypeSPI.getCopyMethod(eventPropertiesWritten.toArray(new String[eventPropertiesWritten.size()]));
        if (copyMethod == null)
        {
            return new NullableObject<InternalEventRouterPreprocessor>(null);
        }
        return new NullableObject<InternalEventRouterPreprocessor>(new InternalEventRouterPreprocessor(copyMethod, desc));
    }

    private static class IRDescEntry
    {
        private final InternalEventRouterDesc internalEventRouterDesc;
        private final InternalRoutePreprocessView outputView;
        private final StatementAgentInstanceLock agentInstanceLock;
        private final boolean hasSubselect;

        private IRDescEntry(InternalEventRouterDesc internalEventRouterDesc, InternalRoutePreprocessView outputView, StatementAgentInstanceLock agentInstanceLock, boolean hasSubselect) {
            this.internalEventRouterDesc = internalEventRouterDesc;
            this.outputView = outputView;
            this.agentInstanceLock = agentInstanceLock;
            this.hasSubselect = hasSubselect;
        }

        public EventType getEventType()
        {
            return internalEventRouterDesc.getEventType();
        }

        public Annotation[] getAnnotations()
        {
            return internalEventRouterDesc.getAnnotations();
        }

        public TypeWidener[] getWideners()
        {
            return internalEventRouterDesc.getWideners();
        }

        public InternalRoutePreprocessView getOutputView() {
            return outputView;
        }

        public StatementAgentInstanceLock getAgentInstanceLock() {
            return agentInstanceLock;
        }

        public boolean isHasSubselect() {
            return hasSubselect;
        }
    }
}
