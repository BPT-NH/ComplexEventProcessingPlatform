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

package com.espertech.esper.core.context.util;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.context.ContextPartitionSelector;

import java.util.Iterator;

public interface ContextIteratorHandler {
    public Iterator<EventBean> iterator(String statementId);
    public SafeIterator<EventBean> safeIterator(String statementId);
    public Iterator<EventBean> iterator(String statementId, ContextPartitionSelector selector);
    public SafeIterator<EventBean> safeIterator(String statementId, ContextPartitionSelector selector);
}
