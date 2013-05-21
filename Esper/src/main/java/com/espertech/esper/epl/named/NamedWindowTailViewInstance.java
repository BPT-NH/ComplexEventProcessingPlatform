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
import com.espertech.esper.collection.ArrayEventIterator;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.context.util.EPStatementAgentInstanceHandle;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprNodeUtility;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.view.ViewSupport;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This view is hooked into a named window's view chain as the last view and handles dispatching of named window
 * insert and remove stream results via {@link com.espertech.esper.epl.named.NamedWindowService} to consuming statements.
 */
public class NamedWindowTailViewInstance extends ViewSupport implements Iterable<EventBean>
{
    private final NamedWindowRootViewInstance rootViewInstance;
    private final NamedWindowTailView tailView;
    private final AgentInstanceContext agentInstanceContext;

    private volatile Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> consumersInContext;  // handles as copy-on-write
    private volatile long numberOfEvents;

    public NamedWindowTailViewInstance(NamedWindowRootViewInstance rootViewInstance, NamedWindowTailView tailView, AgentInstanceContext agentInstanceContext) {
        this.rootViewInstance = rootViewInstance;
        this.tailView = tailView;
        this.agentInstanceContext = agentInstanceContext;
        this.consumersInContext = NamedWindowUtil.createConsumerMap(tailView.isPrioritized());
    }

    public void update(EventBean[] newData, EventBean[] oldData)
    {
        // Only old data (remove stream) needs to be removed from indexes (kept by root view), if any
        if (oldData != null)
        {
            rootViewInstance.removeOldData(oldData);
            numberOfEvents -= oldData.length;
        }

        if ((newData != null) && (!tailView.isParentBatchWindow())) {
            rootViewInstance.addNewData(newData);
        }

        if (newData != null)
        {
            numberOfEvents += newData.length;
        }

        // Post to child views, only if there are listeners or subscribers
        if (tailView.getStatementResultService().isMakeNatural() || tailView.getStatementResultService().isMakeSynthetic())
        {
            updateChildren(newData, oldData);
        }

        if (!consumersInContext.isEmpty() || !tailView.getConsumersNonContext().isEmpty()) {
            NamedWindowDeltaData delta = new NamedWindowDeltaData(newData, oldData);
            tailView.getNamedWindowService().addDispatch(delta, consumersInContext);
            tailView.getNamedWindowService().addDispatch(delta, tailView.getConsumersNonContext());
        }
    }

    /**
     * Adds a consuming (selecting) statement to the named window.
     * @return consumer view
     */
    public NamedWindowConsumerView addConsumer(NamedWindowConsumerDesc consumerDesc, boolean isSubselect)
    {
        NamedWindowConsumerCallback consumerCallback = new NamedWindowConsumerCallback() {
            public Iterator<EventBean> getIterator() {
                return NamedWindowTailViewInstance.this.iterator();
            }

            public void stopped(NamedWindowConsumerView namedWindowConsumerView) {
                removeConsumer(namedWindowConsumerView);
            }
        };

        // Construct consumer view, allow a callback to this view to remove the consumer
        boolean audit = AuditEnum.STREAM.getAudit(consumerDesc.getAgentInstanceContext().getStatementContext().getAnnotations()) != null;
        NamedWindowConsumerView consumerView = new NamedWindowConsumerView(ExprNodeUtility.getEvaluators(consumerDesc.getFilterList()), consumerDesc.getOptPropertyEvaluator(), tailView.getEventType(), consumerCallback, consumerDesc.getAgentInstanceContext(), audit);

        // Keep a list of consumer views per statement to accomodate joins and subqueries
        List<NamedWindowConsumerView> viewsPerStatements = consumersInContext.get(consumerDesc.getAgentInstanceContext().getEpStatementAgentInstanceHandle());
        if (viewsPerStatements == null)
        {
            viewsPerStatements = new CopyOnWriteArrayList<NamedWindowConsumerView>();

            // avoid concurrent modification as a thread may currently iterate over consumers as its dispatching
            // without the engine lock
            Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> newConsumers = NamedWindowUtil.createConsumerMap(tailView.isPrioritized());
            newConsumers.putAll(consumersInContext);
            newConsumers.put(consumerDesc.getAgentInstanceContext().getEpStatementAgentInstanceHandle(), viewsPerStatements);
            consumersInContext = newConsumers;
        }
        if (isSubselect) {
            viewsPerStatements.add(0, consumerView);
        }
        else {
            viewsPerStatements.add(consumerView);
        }

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
        for (Map.Entry<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> entry : consumersInContext.entrySet())
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
            Map<EPStatementAgentInstanceHandle, List<NamedWindowConsumerView>> newConsumers = NamedWindowUtil.createConsumerMap(tailView.isPrioritized());
            newConsumers.putAll(consumersInContext);
            newConsumers.remove(handleRemoved);
            consumersInContext = newConsumers;
        }
    }

    public EventType getEventType()
    {
        return tailView.getEventType();
    }

    public Iterator<EventBean> iterator()
    {
        if (tailView.getRevisionProcessor() != null)
        {
            Collection<EventBean> coll = tailView.getRevisionProcessor().getSnapshot(agentInstanceContext.getEpStatementAgentInstanceHandle(), parent);
            return coll.iterator();
        }

        agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().acquireReadLock();
        try
        {
            Iterator<EventBean> it = parent.iterator();
            if (!it.hasNext())
            {
                return CollectionUtil.NULL_EVENT_ITERATOR;
            }
            ArrayList<EventBean> list = new ArrayList<EventBean>();
            while (it.hasNext())
            {
                list.add(it.next());
            }
            return new ArrayEventIterator(list.toArray(new EventBean[list.size()]));
        }
        finally
        {
            agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().releaseReadLock();
        }
    }

    /**
     * Returns a snapshot of window contents, thread-safely
     * @param filter filters if any
     * @return window contents
     */
    public Collection<EventBean> snapshot(FilterSpecCompiled filter, Annotation[] annotations)
    {
        if (tailView.getRevisionProcessor() != null)
        {
            return tailView.getRevisionProcessor().getSnapshot(agentInstanceContext.getEpStatementAgentInstanceHandle(), parent);
        }

        agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().acquireReadLock();
        try
        {
            return snapshotNoLock(filter, annotations);
        }
        finally
        {
            agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().releaseReadLock();
        }
    }

    public EventBean[] snapshotUpdate(FilterSpecCompiled filter, ExprNode optionalWhereClause, NamedWindowUpdateHelper updateHelper, Annotation[] annotations) {
        agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().acquireReadLock();
        try {
            Collection<EventBean> events = snapshotNoLockWithFilter(filter, annotations, optionalWhereClause, agentInstanceContext);
            if (events.isEmpty()) {
                return CollectionUtil.EVENT_PER_STREAM_EMPTY;
            }

            EventBean[] eventsPerStream = new EventBean[3];
            EventBean[] updated = new EventBean[events.size()];
            int count = 0;
            for (EventBean event : events) {
                updated[count++] = updateHelper.update(event, eventsPerStream, agentInstanceContext);
            }

            EventBean[] deleted = events.toArray(new EventBean[events.size()]);
            rootViewInstance.update(updated, deleted);
            return updated;
        }
        finally {
            agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().releaseReadLock();
        }
    }

    public EventBean[] snapshotDelete(FilterSpecCompiled filter, ExprNode filterExpr, Annotation[] annotations) {
        agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().acquireReadLock();
        try {
            Collection<EventBean> events = snapshotNoLockWithFilter(filter, annotations, filterExpr, agentInstanceContext);
            if (events.isEmpty()) {
                return CollectionUtil.EVENT_PER_STREAM_EMPTY;
            }
            EventBean[] eventsDeleted = events.toArray(new EventBean[events.size()]);
            rootViewInstance.update(null, eventsDeleted);
            return eventsDeleted;
        }
        finally {
            agentInstanceContext.getEpStatementAgentInstanceHandle().getStatementAgentInstanceLock().releaseReadLock();
        }
    }

    public Collection<EventBean> snapshotNoLock(FilterSpecCompiled filter, Annotation[] annotations)
    {
        if (tailView.getRevisionProcessor() != null) {
            return tailView.getRevisionProcessor().getSnapshot(agentInstanceContext.getEpStatementAgentInstanceHandle(), parent);
        }

        Collection<EventBean> indexedResult = rootViewInstance.snapshot(filter, annotations);
        if (indexedResult != null) {
            return indexedResult;
        }
        Iterator<EventBean> it = parent.iterator();
        if (!it.hasNext()) {
            return Collections.EMPTY_LIST;
        }
        ArrayDeque<EventBean> list = new ArrayDeque<EventBean>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public Collection<EventBean> snapshotNoLockWithFilter(FilterSpecCompiled filter, Annotation[] annotations, ExprNode filterExpr, ExprEvaluatorContext exprEvaluatorContext)
    {
        if (tailView.getRevisionProcessor() != null) {
            return tailView.getRevisionProcessor().getSnapshot(agentInstanceContext.getEpStatementAgentInstanceHandle(), parent);
        }

        Collection<EventBean> indexedResult = rootViewInstance.snapshot(filter, annotations);
        if (indexedResult != null) {
            if (indexedResult.isEmpty()) {
                return indexedResult;
            }
            if (filterExpr == null) {
                return indexedResult;
            }
            ArrayDeque<EventBean> deque = new ArrayDeque<EventBean>(Math.min(indexedResult.size(), 16));
            ExprNodeUtility.applyFilterExpressionIterable(indexedResult, filterExpr.getExprEvaluator(), exprEvaluatorContext, deque);
            return deque;
        }

        // fall back to window operator if snapshot doesn't resolve successfully
        Iterator<EventBean> it = parent.iterator();
        if (!it.hasNext()) {
            return Collections.EMPTY_LIST;
        }
        if (filterExpr != null) {
            ExprEvaluator evaluator = filterExpr.getExprEvaluator();
            ArrayDeque<EventBean> list = new ArrayDeque<EventBean>();
            EventBean[] eventsPerStream = new EventBean[1];
            while (it.hasNext()) {
                EventBean event = it.next();
                eventsPerStream[0] = event;
                Boolean result = (Boolean) evaluator.evaluate(eventsPerStream, true, exprEvaluatorContext);
                if ((result != null) && result) {
                    list.add(event);
                }
            }
            return list;
        }
        else {
            ArrayDeque<EventBean> list = new ArrayDeque<EventBean>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list;
        }
    }

    public AgentInstanceContext getAgentInstanceContext() {
        return agentInstanceContext;
    }

    /**
     * Destroy the view.
     */
    public void destroy()
    {
        consumersInContext = NamedWindowUtil.createConsumerMap(tailView.isPrioritized());
    }

    /**
     * Returns the number of events held.
     * @return number of events
     */
    public long getNumberOfEvents()
    {
        return numberOfEvents;
    }

    public NamedWindowTailView getTailView() {
        return tailView;
    }
}
