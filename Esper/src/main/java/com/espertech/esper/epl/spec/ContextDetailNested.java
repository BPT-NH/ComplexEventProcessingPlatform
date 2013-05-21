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

import java.util.List;

public class ContextDetailNested implements ContextDetail {
    private static final long serialVersionUID = -6809635026804573109L;
    private final List<CreateContextDesc> contexts;

    public ContextDetailNested(List<CreateContextDesc> contexts) {
        this.contexts = contexts;
    }

    public List<CreateContextDesc> getContexts() {
        return contexts;
    }

    public List<FilterSpecCompiled> getFilterSpecsIfAny() {
        return null;
    }
}
