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

import com.espertech.esper.client.context.ContextPartitionSelector;
import com.espertech.esper.client.context.EPContextPartitionAdmin;
import com.espertech.esper.core.context.mgr.AgentInstanceSelector;

public interface EPContextPartitionAdminSPI extends EPContextPartitionAdmin
{
    public EPContextPartitionExtract extractDestroyPaths(String contextName, ContextPartitionSelector selector);
    public EPContextPartitionExtract extractStopPaths(String contextName, ContextPartitionSelector selector);

    public EPContextPartitionExtract extractPaths(String contextName, ContextPartitionSelector selector);
    public EPContextPartitionImportResult importStartPaths(String contextName, EPContextPartitionImportable importable, AgentInstanceSelector agentInstanceSelector);
}
