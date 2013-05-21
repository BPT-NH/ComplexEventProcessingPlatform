/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.service;

import com.espertech.esper.dispatch.DispatchService;
import com.espertech.esper.dispatch.Dispatchable;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.view.ViewSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;

/**
 * Convenience view for dispatching view updates received from a parent view to update listeners
 * via the dispatch service.
 */
public abstract class UpdateDispatchViewBase extends ViewSupport implements Dispatchable, UpdateDispatchView
{
    /**
     * Handles result delivery
     */
    protected final StatementResultService statementResultService;

    /**
     * Dispatches events to listeners.
     */
    protected final DispatchService dispatchService;

    /**
     * For iteration with patterns.
     */
    protected EventBean lastIterableEvent;

    /**
     * Flag to indicate we have registered a dispatch.
     */
    protected ThreadLocal<Boolean> isDispatchWaiting = new ThreadLocal<Boolean>() {
        protected synchronized Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    /**
     * Ctor.
     * @param dispatchService - for performing the dispatch
     * @param statementResultServiceImpl - handles result delivery
     */
    public UpdateDispatchViewBase(StatementResultService statementResultServiceImpl, DispatchService dispatchService)
    {
        this.dispatchService = dispatchService;
        this.statementResultService = statementResultServiceImpl;
    }

    public EventType getEventType()
    {
        return null;
    }

    public Iterator<EventBean> iterator()
    {
        throw new UnsupportedOperationException();
    }

    public void execute()
    {
        isDispatchWaiting.set(false);
        statementResultService.execute();
    }

    /**
     * Remove event reference to last event.
     */
    public void clear()
    {
        lastIterableEvent = null;
    }

    public StatementResultService getStatementResultService() {
        return statementResultService;
    }

    private static Log log = LogFactory.getLog(UpdateDispatchViewBase.class);
}
