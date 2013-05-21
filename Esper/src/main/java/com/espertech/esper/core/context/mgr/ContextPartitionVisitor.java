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

import com.espertech.esper.client.context.ContextPartitionIdentifier;

public interface ContextPartitionVisitor {
    public void visit(int nestingLevel,
                      int pathId,
                      ContextStatePathValueBinding binding,
                      Object payload,
                      ContextController contextController,
                      ContextControllerInstanceHandle instanceHandle);
}
