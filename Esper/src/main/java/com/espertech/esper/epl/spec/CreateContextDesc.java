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

import java.io.Serializable;
import java.util.List;

public class CreateContextDesc implements Serializable {

    private static final long serialVersionUID = -5318225626899036861L;
    private final String contextName;
    private final ContextDetail contextDetail;

    public CreateContextDesc(String contextName, ContextDetail contextDetail) {
        this.contextName = contextName;
        this.contextDetail = contextDetail;
    }

    public String getContextName() {
        return contextName;
    }

    public ContextDetail getContextDetail() {
        return contextDetail;
    }

    public List<FilterSpecCompiled> getFilterSpecs() {
        return contextDetail.getFilterSpecsIfAny();
    }
}
