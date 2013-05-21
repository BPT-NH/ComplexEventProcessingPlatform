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

package com.espertech.esper.core.thread;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.core.context.util.EPStatementAgentInstanceHandle;
import com.espertech.esper.core.service.EPRuntimeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Route execution work unit.
 */
public class RouteUnitMultiple implements RouteUnitRunnable
{
    private static final Log log = LogFactory.getLog(RouteUnitMultiple.class);

    private final EPRuntimeImpl epRuntime;
    private final EventBean theEvent;
    private Object callbackList;
    private EPStatementAgentInstanceHandle handle;
    private final long filterVersion;

    /**
     * Ctor.
     * @param epRuntime runtime to process
     * @param callbackList callback list
     * @param theEvent event to pass
     * @param handle statement handle
     * @param filterVersion version of filter
     */
    public RouteUnitMultiple(EPRuntimeImpl epRuntime, Object callbackList, EventBean theEvent, EPStatementAgentInstanceHandle handle, long filterVersion)
    {
        this.epRuntime = epRuntime;
        this.callbackList = callbackList;
        this.theEvent = theEvent;
        this.handle = handle;
        this.filterVersion = filterVersion;
    }

    public void run()
    {
        try
        {
            epRuntime.processStatementFilterMultiple(handle, callbackList, theEvent, filterVersion);

            epRuntime.dispatch();

            epRuntime.processThreadWorkQueue();
        }
        catch (RuntimeException e)
        {
            log.error("Unexpected error processing multiple route execution: " + e.getMessage(), e);
        }
    }

}
