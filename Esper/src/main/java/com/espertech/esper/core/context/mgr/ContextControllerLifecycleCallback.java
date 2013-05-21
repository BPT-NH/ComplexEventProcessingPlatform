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

package com.espertech.esper.core.context.mgr;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.context.ContextPartitionState;

import java.util.Map;

public interface ContextControllerLifecycleCallback {
    public ContextControllerInstanceHandle contextPartitionInstantiate(Integer optionalContextPartitionId,
                                                                       int subpath,
                                                                       Integer importSubpathId, ContextController originator, EventBean optionalTriggeringEvent,
                                                                       Map<String, Object> optionalTriggeringPattern,
                                                                       Object partitionKey,
                                                                       Map<String, Object> contextProperties,
                                                                       ContextControllerState states,
                                                                       ContextInternalFilterAddendum filterAddendum,
                                                                       boolean isRecoveringResilient,
                                                                       ContextPartitionState state);

    public void contextPartitionNavigate(ContextControllerInstanceHandle existingHandle,
                                         ContextController originator,
                                         ContextControllerState controllerState,
                                         int exportedCPOrPathId,
                                         ContextInternalFilterAddendum filterAddendum,
                                         AgentInstanceSelector agentInstanceSelector, byte[] payload);

    public void contextPartitionTerminate(ContextControllerInstanceHandle contextNestedHandle,
                                          Map<String, Object> terminationProperties);
}
