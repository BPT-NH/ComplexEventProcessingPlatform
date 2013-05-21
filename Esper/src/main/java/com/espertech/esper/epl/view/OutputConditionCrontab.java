/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.view;

import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.core.service.EPStatementHandleCallback;
import com.espertech.esper.core.service.ExtensionServicesContext;
import com.espertech.esper.schedule.ScheduleComputeHelper;
import com.espertech.esper.schedule.ScheduleHandleCallback;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esper.schedule.SchedulingService;
import com.espertech.esper.util.ExecutionPathDebugLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Output condition handling crontab-at schedule output.
 */
public final class OutputConditionCrontab extends OutputConditionBase implements OutputCondition
{
    private static final boolean DO_OUTPUT = true;
	private static final boolean FORCE_UPDATE = true;

    private final AgentInstanceContext context;
    private final OutputConditionCrontabFactory factory;

    private final ScheduleSlot scheduleSlot;
    private Long currentReferencePoint;
    private boolean isCallbackScheduled;

    public OutputConditionCrontab(OutputCallback outputCallback, AgentInstanceContext context, OutputConditionCrontabFactory factory) {
        super(outputCallback);
        this.context = context;
        this.factory = factory;
        scheduleSlot = context.getStatementContext().getScheduleBucket().allocateSlot();
    }

    public final void updateOutputCondition(int newEventsCount, int oldEventsCount)
    {
        if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
        {
        	log.debug(".updateOutputCondition, " +
        			"  newEventsCount==" + newEventsCount +
        			"  oldEventsCount==" + oldEventsCount);
        }

        if (currentReferencePoint == null)
        {
        	currentReferencePoint = context.getStatementContext().getSchedulingService().getTime();
        }

        // Schedule the next callback if there is none currently scheduled
        if (!isCallbackScheduled)
        {
        	scheduleCallback();
        }
    }

    public final String toString()
    {
        return this.getClass().getName() +
                " spec=" + factory.getScheduleSpec();
    }

    private void scheduleCallback()
    {
    	isCallbackScheduled = true;
        long current = context.getStatementContext().getSchedulingService().getTime();

        if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
        {
            log.debug(".scheduleCallback Scheduled new callback for " +
                    " now=" + current +
                    " currentReferencePoint=" + currentReferencePoint +
                    " spec=" + factory.getScheduleSpec());
        }

        ScheduleHandleCallback callback = new ScheduleHandleCallback() {
            public void scheduledTrigger(ExtensionServicesContext extensionServicesContext)
            {
                OutputConditionCrontab.this.isCallbackScheduled = false;
                OutputConditionCrontab.this.outputCallback.continueOutputProcessing(DO_OUTPUT, FORCE_UPDATE);
                scheduleCallback();
            }
        };
        EPStatementHandleCallback handle = new EPStatementHandleCallback(context.getEpStatementAgentInstanceHandle(), callback);
        SchedulingService schedulingService = context.getStatementContext().getSchedulingService();
        long nextScheduledTime = ScheduleComputeHelper.computeDeltaNextOccurance(factory.getScheduleSpec(), schedulingService.getTime());
        schedulingService.add(nextScheduledTime, handle, scheduleSlot);
    }

    public void terminated() {
        outputCallback.continueOutputProcessing(true, true);
    }

    private static final Log log = LogFactory.getLog(OutputConditionCrontab.class);
}
