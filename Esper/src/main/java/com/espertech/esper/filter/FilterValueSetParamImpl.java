/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import java.io.StringWriter;

/**
 * Filter parameter value defining the event property to filter, the filter operator, and the filter value.
 */
public class FilterValueSetParamImpl implements FilterValueSetParam
{
    private static final long serialVersionUID = 6010018248791276406L;
    
    private final FilterSpecLookupable lookupable;
    private final FilterOperator filterOperator;
    private final Object filterValue;

    /**
     * Ctor.
     * @param lookupable - stuff to use to interrogate
     * @param filterOperator - operator to apply
     * @param filterValue - value to look for
     */
    public FilterValueSetParamImpl(FilterSpecLookupable lookupable, FilterOperator filterOperator, Object filterValue)
    {
        this.lookupable = lookupable;
        this.filterOperator = filterOperator;
        this.filterValue = filterValue;
    }

    public FilterSpecLookupable getLookupable() {
        return lookupable;
    }

    public FilterOperator getFilterOperator()
    {
        return filterOperator;
    }

    public Object getFilterForValue()
    {
        return filterValue;
    }

    public String toString() {
        return "FilterValueSetParamImpl{" +
                "lookupable='" + lookupable + '\'' +
                ", filterOperator=" + filterOperator +
                ", filterValue=" + filterValue +
                '}';
    }

    public void appendTo(StringWriter writer) {
        lookupable.appendTo(writer);
        writer.append(filterOperator.getTextualOp());
        writer.append(filterValue == null ? "null" : filterValue.toString());
    }
}
