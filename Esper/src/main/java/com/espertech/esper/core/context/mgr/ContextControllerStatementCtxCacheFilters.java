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

import com.espertech.esper.filter.FilterSpecCompiled;

import java.util.List;

public class ContextControllerStatementCtxCacheFilters implements ContextControllerStatementCtxCache {
    private final List<FilterSpecCompiled> filterSpecs;

    public ContextControllerStatementCtxCacheFilters(List<FilterSpecCompiled> filterSpecs) {
        this.filterSpecs = filterSpecs;
    }

    public List<FilterSpecCompiled> getFilterSpecs() {
        return filterSpecs;
    }
}
