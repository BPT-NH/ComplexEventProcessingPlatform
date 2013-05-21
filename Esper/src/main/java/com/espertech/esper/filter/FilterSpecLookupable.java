/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.util.MetaDefItem;

import java.io.Serializable;
import java.io.StringWriter;

public class FilterSpecLookupable implements MetaDefItem, Serializable
{
    private static final long serialVersionUID = 3576828533611557509L;
    private final String expression;
    private transient final EventPropertyGetter getter;
    private final Class returnType;

    public FilterSpecLookupable(String expression, EventPropertyGetter getter, Class returnType) {
        this.expression = expression;
        this.getter = getter;
        this.returnType = returnType;
    }

    public String getExpression() {
        return expression;
    }

    public EventPropertyGetter getGetter() {
        return getter;
    }

    public Class getReturnType() {
        return returnType;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterSpecLookupable that = (FilterSpecLookupable) o;

        if (!expression.equals(that.expression)) return false;

        return true;
    }

    public int hashCode() {
        return expression.hashCode();
    }

    public void appendTo(StringWriter writer) {
        writer.append(expression);
    }
}

