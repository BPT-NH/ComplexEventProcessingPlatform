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

package com.espertech.esper.client.context;

import java.util.List;

/**
 * Selects context partitions of a nested context by providing selector according to the nested contexts.
 */
public interface ContextPartitionSelectorNested extends ContextPartitionSelector {
    /**
     * Selectors for each level of the nested context.
     * @return selectors
     */
    public List<ContextPartitionSelector[]> getSelectors();
}
