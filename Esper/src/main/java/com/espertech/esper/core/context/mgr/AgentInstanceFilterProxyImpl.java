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
import com.espertech.esper.filter.FilterValueSetParam;

import java.util.IdentityHashMap;
import java.util.Map;

public class AgentInstanceFilterProxyImpl implements AgentInstanceFilterProxy {

    private final FilterSpecCompiled[] compiledArr;
    private final FilterValueSetParam[][] addendumArr;
    private final IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> addendumMap;

    public AgentInstanceFilterProxyImpl(IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> addendums) {
        if (addendums.size() > 6) {
            this.addendumMap = addendums;
            compiledArr = null;
            addendumArr = null;
        }
        else {
            compiledArr = new FilterSpecCompiled[addendums.size()];
            addendumArr = new FilterValueSetParam[addendums.size()][];
            int count = 0;
            for (Map.Entry<FilterSpecCompiled, FilterValueSetParam[]> entry : addendums.entrySet()) {
                compiledArr[count] = entry.getKey();
                addendumArr[count] = entry.getValue();
                count++;
            }
            addendumMap = null;
        }
    }

    public FilterValueSetParam[] getAddendumFilters(FilterSpecCompiled filterSpec) {
        if (addendumMap == null) {
            for (int i = 0; i < compiledArr.length; i++) {
                if (filterSpec == compiledArr[i]) {
                    return addendumArr[i];
                }
            }
            return null;
        }
        else {
            return addendumMap.get(filterSpec);
        }
    }
}
