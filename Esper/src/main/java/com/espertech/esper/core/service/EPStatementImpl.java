/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.service;

import com.espertech.esper.client.*;
import com.espertech.esper.client.context.ContextPartitionSelector;
import com.espertech.esper.collection.SafeIteratorImpl;
import com.espertech.esper.collection.SingleEventIterator;
import com.espertech.esper.dispatch.DispatchService;
import com.espertech.esper.timer.TimeSourceService;
import com.espertech.esper.view.Viewable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Statement implementation for EPL statements.
 */
public class EPStatementImpl implements EPStatementSPI
{
    private static Log log = LogFactory.getLog(EPStatementImpl.class);

    private final EPStatementListenerSet statementListenerSet;
    private final String expressionNoAnnotations;
    private final boolean nameProvided;
    private boolean isPattern;
    private UpdateDispatchViewBase dispatchChildView;
    private StatementLifecycleSvc statementLifecycleSvc;

    private long timeLastStateChange;
    private Viewable parentView;
    private EPStatementState currentState;
    private EventType eventType;
    private StatementMetadata statementMetadata;
    private Object userObject;
    private StatementContext statementContext;
    private String serviceIsolated;

    /**
     * Ctor.
     * @param isPattern is true to indicate this is a pure pattern expression
     * @param dispatchService for dispatching events to listeners to the statement
     * @param statementLifecycleSvc handles lifecycle transitions for the statement
     * @param isBlockingDispatch is true if the dispatch to listeners should block to preserve event generation order
     * @param isSpinBlockingDispatch true to use spin locks blocking to deliver results, as locks are usually uncontended
     * @param msecBlockingTimeout is the max number of milliseconds of block time
     * @param timeLastStateChange the timestamp the statement was created and started
     * @param timeSourceService time source provider
     * @param statementMetadata statement metadata
     * @param userObject the application define user object associated to each statement, if supplied
     * @param statementContext the statement service context
     * @param expressionNoAnnotations expression text witout annotations
     * @param isFailed indicator to start in failed state
     * @param nameProvided true to indicate a statement name has been provided and is not a system-generated name
     */
    public EPStatementImpl(String expressionNoAnnotations,
                              boolean isPattern,
                              DispatchService dispatchService,
                              StatementLifecycleSvc statementLifecycleSvc,
                              long timeLastStateChange,
                              boolean isBlockingDispatch,
                              boolean isSpinBlockingDispatch,
                              long msecBlockingTimeout,
                              TimeSourceService timeSourceService,
                              StatementMetadata statementMetadata,
                              Object userObject,
                              StatementContext statementContext,
                              boolean isFailed,
                              boolean nameProvided)
    {
        this.isPattern = isPattern;
        this.expressionNoAnnotations = expressionNoAnnotations;
        this.statementLifecycleSvc = statementLifecycleSvc;
        this.statementContext = statementContext;
        this.nameProvided = nameProvided; 
        statementListenerSet = new EPStatementListenerSet();
        if (isBlockingDispatch)
        {
            if (isSpinBlockingDispatch)
            {
                this.dispatchChildView = new UpdateDispatchViewBlockingSpin(statementContext.getStatementResultService(), dispatchService, msecBlockingTimeout, timeSourceService);
            }
            else
            {
                this.dispatchChildView = new UpdateDispatchViewBlockingWait(statementContext.getStatementResultService(), dispatchService, msecBlockingTimeout);
            }
        }
        else
        {
            this.dispatchChildView = new UpdateDispatchViewNonBlocking(statementContext.getStatementResultService(), dispatchService);
        }
        if (!isFailed) {
            this.currentState = EPStatementState.STOPPED;
        }
        else {
            this.currentState = EPStatementState.FAILED;
        }
        this.timeLastStateChange = timeLastStateChange;
        this.statementMetadata = statementMetadata;
        this.userObject = userObject;
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
    }

    public String getStatementId()
    {
        return statementContext.getStatementId();
    }

    public void start()
    {
        if (statementLifecycleSvc == null)
        {
            throw new IllegalStateException("Cannot start statement, statement is in destroyed state");
        }
        statementLifecycleSvc.start(statementContext.getStatementId());
    }

    public void stop()
    {
        if (statementLifecycleSvc == null)
        {
            throw new IllegalStateException("Cannot stop statement, statement is in destroyed state");
        }
        statementLifecycleSvc.stop(statementContext.getStatementId());

        // On stop, we give the dispatch view a chance to dispatch final results, if any
        statementContext.getStatementResultService().dispatchOnStop();

        dispatchChildView.clear();
    }

    public void destroy()
    {
        if (currentState == EPStatementState.DESTROYED)
        {
            throw new IllegalStateException("Statement already destroyed");
        }
        statementLifecycleSvc.destroy(statementContext.getStatementId());
        parentView = null;
        eventType = null;
        dispatchChildView = null;
        statementLifecycleSvc = null;
    }

    public EPStatementState getState()
    {
        return currentState;
    }

    public void setCurrentState(EPStatementState currentState, long timeLastStateChange)
    {
        this.currentState = currentState;
        this.timeLastStateChange = timeLastStateChange;
    }

    public void setParentView(Viewable viewable)
    {
        if (viewable == null)
        {
            if (parentView != null)
            {
                parentView.removeView(dispatchChildView);
            }
            parentView = null;
        }
        else
        {
            parentView = viewable;
            parentView.addView(dispatchChildView);
            eventType = parentView.getEventType();
        }
    }

    public String getText()
    {
        return statementContext.getExpression();
    }

    public String getName()
    {
        return statementContext.getStatementName();
    }

    public Iterator<EventBean> iterator(ContextPartitionSelector selector) {
        if (statementContext.getContextDescriptor() == null) {
            throw getUnsupportedNonContextIterator();
        }
        if (selector == null) {
            throw new IllegalArgumentException("No selector provided");
        }

        // Return null if not started
        statementContext.getVariableService().setLocalVersion();
        if (parentView == null)
        {
            return null;
        }
        return statementContext.getContextDescriptor().iterator(statementContext.getStatementId(), selector);
    }

    public SafeIterator<EventBean> safeIterator(ContextPartitionSelector selector) {
        if (statementContext.getContextDescriptor() == null) {
            throw getUnsupportedNonContextIterator();
        }
        if (selector == null) {
            throw new IllegalArgumentException("No selector provided");
        }

        // Return null if not started
        if (parentView == null) {
            return null;
        }

        statementContext.getVariableService().setLocalVersion();
        return statementContext.getContextDescriptor().safeIterator(statementContext.getStatementId(), selector);
    }

    public Iterator<EventBean> iterator()
    {
        // Return null if not started
        statementContext.getVariableService().setLocalVersion();
        if (parentView == null)
        {
            return null;
        }
        if (statementContext.getContextDescriptor() != null) {
            return statementContext.getContextDescriptor().iterator(statementContext.getStatementId());
        }
        if (isPattern)
        {
            return new SingleEventIterator(statementContext.getStatementResultService().getLastIterableEvent());
        }
        else
        {
            return parentView.iterator();
        }
    }

    public SafeIterator<EventBean> safeIterator()
    {
        // Return null if not started
        if (parentView == null)
        {
            return null;
        }

        if (statementContext.getContextDescriptor() != null) {
            statementContext.getVariableService().setLocalVersion();
            return statementContext.getContextDescriptor().safeIterator(statementContext.getStatementId());
        }

        // Set variable version and acquire the lock first
        statementContext.getDefaultAgentInstanceLock().acquireReadLock();
        try
        {
            statementContext.getVariableService().setLocalVersion();

            // Provide iterator - that iterator MUST be closed else the lock is not released
            if (isPattern)
            {
                return new SafeIteratorImpl<EventBean>(statementContext.getDefaultAgentInstanceLock(), dispatchChildView.iterator());
            }
            else
            {
                return new SafeIteratorImpl<EventBean>(statementContext.getDefaultAgentInstanceLock(), parentView.iterator());
            }
        }
        catch (RuntimeException ex)
        {
            statementContext.getDefaultAgentInstanceLock().releaseReadLock();
            throw ex;
        }
    }

    public EventType getEventType()
    {
        return eventType;
    }

    /**
     * Returns the set of listeners to the statement.
     * @return statement listeners
     */
    public EPStatementListenerSet getListenerSet()
    {
        return statementListenerSet;
    }

    public void setListeners(EPStatementListenerSet listenerSet)
    {
        statementListenerSet.setListeners(listenerSet);
        statementContext.getStatementResultService().setUpdateListeners(listenerSet);
    }

    /**
     * Add a listener to the statement.
     * @param listener to add
     */
    public void addListener(UpdateListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("Null listener reference supplied");
        }
        if (isDestroyed())
        {
            throw new IllegalStateException("Statement is in destroyed state");
        }

        statementListenerSet.addListener(listener);
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
        statementLifecycleSvc.dispatchStatementLifecycleEvent(
                new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_ADD, listener));
    }

    public void addListenerWithReplay(UpdateListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("Null listener reference supplied");
        }

        if (isDestroyed())
        {
            throw new IllegalStateException("Statement is in destroyed state");
        }

        statementContext.getDefaultAgentInstanceLock().acquireReadLock();
        try
        {
            // Add listener - listener not receiving events from this statement, as the statement is locked
            statementListenerSet.addListener(listener);
            statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
            statementLifecycleSvc.dispatchStatementLifecycleEvent(
                    new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_ADD, listener));

            Iterator<EventBean> it = iterator();
            if (it == null)
            {
                try
                {
                    listener.update(null, null);
                }
                catch (Throwable t)
                {
                    String message = "Unexpected exception invoking listener update method for replay on listener class '" + listener.getClass().getSimpleName() +
                            "' : " + t.getClass().getSimpleName() + " : " + t.getMessage();
                    log.error(message, t);
                }
                return;
            }

            ArrayList<EventBean> events = new ArrayList<EventBean>();
            for (; it.hasNext();)
            {
                events.add(it.next());
            }

            if (events.isEmpty())
            {
                try
                {
                    listener.update(null, null);
                }
                catch (Throwable t)
                {
                    String message = "Unexpected exception invoking listener update method for replay on listener class '" + listener.getClass().getSimpleName() +
                            "' : " + t.getClass().getSimpleName() + " : " + t.getMessage();
                    log.error(message, t);
                }
            }
            else
            {
                EventBean[] iteratorResult = events.toArray(new EventBean[events.size()]);
                try
                {
                    listener.update(iteratorResult, null);
                }
                catch (Throwable t)
                {
                    String message = "Unexpected exception invoking listener update method for replay on listener class '" + listener.getClass().getSimpleName() +
                            "' : " + t.getClass().getSimpleName() + " : " + t.getMessage();
                    log.error(message, t);
                }
            }
        }
        finally
        {
            statementContext.getDefaultAgentInstanceLock().releaseReadLock();
        }
    }

    /**
     * Remove a listeners to a statement.
     * @param listener to remove
     */
    public void removeListener(UpdateListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("Null listener reference supplied");
        }

        statementListenerSet.removeListener(listener);
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
        if (statementLifecycleSvc != null)
        {
            statementLifecycleSvc.dispatchStatementLifecycleEvent(
                new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_REMOVE, listener));
        }
    }

    /**
     * Remove all listeners to a statement.
     */
    public void removeAllListeners()
    {
        statementListenerSet.removeAllListeners();
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
        if (statementLifecycleSvc != null)
        {
            statementLifecycleSvc.dispatchStatementLifecycleEvent(
                new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_REMOVE_ALL));
        }
    }

    public void addListener(StatementAwareUpdateListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("Null listener reference supplied");
        }
        if (isDestroyed())
        {
            throw new IllegalStateException("Statement is in destroyed state");
        }

        statementListenerSet.addListener(listener);
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
        statementLifecycleSvc.dispatchStatementLifecycleEvent(
                new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_ADD, listener));
    }

    public void removeListener(StatementAwareUpdateListener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("Null listener reference supplied");
        }

        statementListenerSet.removeListener(listener);
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
        if (statementLifecycleSvc != null)
        {
            statementLifecycleSvc.dispatchStatementLifecycleEvent(
                new StatementLifecycleEvent(this, StatementLifecycleEvent.LifecycleEventType.LISTENER_REMOVE, listener));
        }
    }

    public Iterator<StatementAwareUpdateListener> getStatementAwareListeners()
    {
        return Arrays.asList(statementListenerSet.getStmtAwareListeners()).iterator();
    }

    public Iterator<UpdateListener> getUpdateListeners()
    {
        return Arrays.asList(statementListenerSet.getListeners()).iterator();
    }

    public long getTimeLastStateChange()
    {
        return timeLastStateChange;
    }

    public boolean isStarted()
    {
        return currentState == EPStatementState.STARTED;
    }

    public boolean isStopped()
    {
        return currentState == EPStatementState.STOPPED;
    }

    public boolean isDestroyed()
    {
        return currentState == EPStatementState.DESTROYED;
    }

    public void setSubscriber(Object subscriber)
    {
        statementListenerSet.setSubscriber(subscriber);
        statementContext.getStatementResultService().setUpdateListeners(statementListenerSet);
    }

    public Object getSubscriber()
    {
        return statementListenerSet.getSubscriber();
    }

    public boolean isPattern() {
        return isPattern;
    }

    public StatementMetadata getStatementMetadata()
    {
        return statementMetadata;
    }

    public Object getUserObject()
    {
        return userObject;
    }

    public Annotation[] getAnnotations()
    {
        return statementContext.getAnnotations();
    }

    public StatementContext getStatementContext()
    {
        return statementContext;
    }

    public String getExpressionNoAnnotations()
    {
        return expressionNoAnnotations;
    }

    public String getServiceIsolated()
    {
        return serviceIsolated;
    }

    public void setServiceIsolated(String serviceIsolated)
    {
        this.serviceIsolated = serviceIsolated;
    }

    public boolean isNameProvided()
    {
        return nameProvided;
    }

    private UnsupportedOperationException getUnsupportedNonContextIterator() {
        return new UnsupportedOperationException("Iterator with context selector is only supported for statements under context");
    }
}
