/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.named;

import com.espertech.esper.client.EventType;
import com.espertech.esper.core.service.InternalEventRouter;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.metric.StatementMetricHandle;
import com.espertech.esper.epl.spec.*;
import com.espertech.esper.event.EventBeanReader;
import com.espertech.esper.event.EventBeanReaderDefaultImpl;
import com.espertech.esper.event.EventTypeSPI;

/**
 * View for the on-delete statement that handles removing events from a named window.
 */
public class NamedWindowOnExprFactoryFactory
{
    public static NamedWindowOnExprFactory make(EventType namedWindowEventType,
                                                String namedWindowName,
                                                String namedWindowAlias,
                                                OnTriggerDesc onTriggerDesc,
                                                EventType filterEventType,
                                                String filterStreamName,
                                                boolean addToFront,
                                                InternalEventRouter internalEventRouter,
                                                EventType outputEventType,
                                                StatementContext statementContext,
                                                StatementMetricHandle createNamedWindowMetricsHandle,
                                                boolean isDistinct
                                                )
        throws ExprValidationException
    {
        if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_DELETE)
        {
            return new NamedWindowOnDeleteViewFactory(namedWindowEventType, statementContext.getStatementResultService());
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_SELECT)
        {
            EventBeanReader eventBeanReader = null;
            if (isDistinct)
            {
                if (outputEventType instanceof EventTypeSPI)
                {
                    eventBeanReader = ((EventTypeSPI) outputEventType).getReader();
                }
                if (eventBeanReader == null)
                {
                    eventBeanReader = new EventBeanReaderDefaultImpl(outputEventType);
                }
            }
            OnTriggerWindowDesc windowDesc = (OnTriggerWindowDesc) onTriggerDesc;
            return new NamedWindowOnSelectViewFactory(namedWindowEventType, internalEventRouter, addToFront,
                    statementContext.getEpStatementHandle(), eventBeanReader, isDistinct, outputEventType, statementContext.getStatementResultService(), statementContext.getInternalEventEngineRouteDest(), windowDesc.isDeleteAndSelect());
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_UPDATE)
        {
            OnTriggerWindowUpdateDesc updateDesc = (OnTriggerWindowUpdateDesc) onTriggerDesc;
            NamedWindowUpdateHelper updateHelper = NamedWindowUpdateHelper.make(namedWindowName, (EventTypeSPI) namedWindowEventType, updateDesc.getAssignments(), namedWindowAlias);
            return new NamedWindowOnUpdateViewFactory(namedWindowEventType, statementContext.getStatementResultService(), updateHelper);
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_MERGE)
        {
            OnTriggerMergeDesc onMergeTriggerDesc = (OnTriggerMergeDesc) onTriggerDesc;
            NamedWindowOnMergeHelper onMergeHelper = new NamedWindowOnMergeHelper(statementContext, onMergeTriggerDesc, filterEventType, filterStreamName, internalEventRouter, namedWindowName, (EventTypeSPI) namedWindowEventType);
            return new NamedWindowOnMergeViewFactory(namedWindowEventType, onMergeHelper, statementContext.getStatementResultService(), createNamedWindowMetricsHandle, statementContext.getMetricReportingService());
        }
        else
        {
            throw new IllegalStateException("Unknown trigger type " + onTriggerDesc.getOnTriggerType());
        }
    }
}
