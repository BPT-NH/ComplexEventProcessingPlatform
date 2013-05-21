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

package com.espertech.esper.epl.spec;

import com.espertech.esper.filter.FilterSpecCompiled;

import java.util.ArrayList;
import java.util.List;

public class ContextDetailHash implements ContextDetail {

    private static final long serialVersionUID = -7754347180148095977L;
    private final List<ContextDetailHashItem> items;
    private final int granularity;
    private final boolean preallocate;

    public ContextDetailHash(List<ContextDetailHashItem> items, int granularity, boolean preallocate) {
        this.items = items;
        this.preallocate = preallocate;
        this.granularity = granularity;
    }

    public List<ContextDetailHashItem> getItems() {
        return items;
    }

    public boolean isPreallocate() {
        return preallocate;
    }

    public int getGranularity() {
        return granularity;
    }

    public List<FilterSpecCompiled> getFilterSpecsIfAny() {
        List<FilterSpecCompiled> filters = new ArrayList<FilterSpecCompiled>(items.size());
        for (ContextDetailHashItem item : items) {
            filters.add(item.getFilterSpecCompiled());
        }
        return filters;
    }
}
