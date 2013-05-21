/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.named;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.annotation.AuditEnum;
import com.espertech.esper.core.context.util.EPStatementAgentInstanceHandle;
import com.espertech.esper.core.service.StatementResultService;
import com.espertech.esper.epl.expression.ExprNodeUtility;
import com.espertech.esper.event.vaevent.ValueAddEventProcessor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This view is hooked into a named window's view chain as the last view and handles dispatching of named window
 * insert and remove stream results via {@link NamedWindowService} to consuming statements.
 */
public class NamedWindowTailView
{
    private final EventType eventType;
    private final NamedWindowService namedWindowService;
    private final StatementResultService statementResultService;
    private final ValueAddEventProcessor revisionProcessor;
    private final boolean isPrioritized;
    private final boolean isParentBatchWindow;
    private volatile Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> consumersNonContext;  // handles as copy-on-write

    public NamedWindowTailView(EventType eventType, NamedWindowService namedWindowService, StatementResultService statementResultService, ValueAddEventProcessor revisionProcessor, boolean prioritized, boolean parentBatchWindow) {
        this.eventType = eventType;
        this.namedWindowService = namedWindowService;
        this.statementResultService = statementResultService;
        this.revisionProcessor = revisionProcessor;
        isPrioritized = prioritized;
        isParentBatchWindow = parentBatchWindow;
        this.consumersNonContext = NamedWindowUtil.createConsumerMap(isPrioritized);
    }

    /**
     * Returns true to indicate that the data window view is a batch view.
     * @return true if batch view
     */
    public boolean isParentBatchWindow() {
        return isParentBatchWindow;
    }

    public EventType getEventType() {
        return eventType;
    }

    public StatementResultService getStatementResultService() {
        return statementResultService;
    }

    public NamedWindowService getNamedWindowService() {
        return namedWindowService;
    }

    public boolean isPrioritized() {
        return isPrioritized;
    }

    public ValueAddEventProcessor getRevisionProcessor() {
        return revisionProcessor;
    }

    public Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> getConsumersNonContext() {
        return consumersNonContext;
    }

    public NamedWindowConsumerView addConsumer(NamedWindowConsumerDesc consumerDesc)
    {
        NamedWindowConsumerCallback consumerCallback = new NamedWindowConsumerCallback() {
            public Iterator<EventBean> getIterator() {
                throw new UnsupportedOperationException("Iterator not supported on named windows that have a context attached and when that context is not the same as the consuming statement's context");
            }

            public void stopped(NamedWindowConsumerView namedWindowConsumerView) {
                removeConsumer(namedWindowConsumerView);
            }
        };

        // Construct consumer view, allow a callback to this view to remove the consumer
        boolean audit = AuditEnum.STREAM.getAudit(consumerDesc.getAgentInstanceContext().getStatementContext().getAnnotations()) != null;
        NamedWindowConsumerView consumerView = new NamedWindowConsumerView(ExprNodeUtility.getEvaluators(consumerDesc.getFilterList()), consumerDesc.getOptPropertyEvaluator(), eventType, consumerCallback, consumerDesc.getAgentInstanceContext(), audit);

        // Keep a list of consumer views per statement to accomodate joins and subqueries
        List<NamedWindowConsumerView> viewsPerStatements = consumersNonContext.get(consumerDesc.getAgentInstanceContext().getEpStatementAgentInstanceHandle());
        if (viewsPerStatements == null)
        {
            viewsPerStatements = new CopyOnWriteArrayList<NamedWindowConsumerView>();

            // avoid concurrent modification as a thread may currently iterate over consumers as its dispatching
            // without the engine lock
            Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> newConsumers = NamedWindowUtil.createConsumerMap(isPrioritized);
            newConsumers.putAll(consumersNonContext);
            newConsumers.put(consumerDesc.getAgentInstanceContext().getEpStatementAgentInstanceHandle(), viewsPerStatements);
            consumersNonContext = newConsumers;
        }
        viewsPerStatements.add(consumerView);

        return consumerView;
    }

    /**
     * Called by the consumer view to indicate it was stopped or destroyed, such that the
     * consumer can be deregistered and further dispatches disregard this consumer.
     * @param namedWindowConsumerView is the consumer representative view
     */
    public void removeConsumer(NamedWindowConsumerView namedWindowConsumerView)
    {
        EPStatementAgentInstanceHandle handleRemoved = null;
        // Find the consumer view
        for (Map.Entry<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> entry : consumersNonContext.entrySet())
        {
            boolean foundAndRemoved = entry.getValue().remove(namedWindowConsumerView);
            // Remove the consumer view
            if ((foundAndRemoved) && (entry.getValue().size() == 0))
            {
                // Remove the handle if this list is now empty
                handleRemoved = entry.getKey();
                break;
            }
        }
        if (handleRemoved != null)
        {
            Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> newConsumers = NamedWindowUtil.createConsumerMap(isPrioritized);
            newConsumers.putAll(consumersNonContext);
            newConsumers.remove(handleRemoved);
            consumersNonContext = newConsumers;
        }
    }
}
