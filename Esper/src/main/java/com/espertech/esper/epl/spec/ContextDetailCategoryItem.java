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

import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterValueSetParam;

import java.io.Serializable;

public class ContextDetailCategoryItem implements Serializable {

    private static final long serialVersionUID = -2022340210686656104L;
    private final ExprNode expression;
    private final String name;
    private FilterValueSetParam[] compiledFilterParam;

    public ContextDetailCategoryItem(ExprNode expression, String name) {
        this.expression = expression;
        this.name = name;
    }

    public ExprNode getExpression() {
        return expression;
    }

    public String getName() {
        return name;
    }

    public FilterValueSetParam[] getCompiledFilterParam() {
        return compiledFilterParam;
    }

    public void setCompiledFilter(FilterSpecCompiled filterSpec) {
        this.compiledFilterParam = filterSpec.getValueSet(null, null, null).getParameters();
    }
}
