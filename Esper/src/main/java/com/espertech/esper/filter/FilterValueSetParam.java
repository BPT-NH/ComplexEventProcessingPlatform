/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import java.io.Serializable;
import java.io.StringWriter;

/**
 * This interface represents one filter parameter in an {@link FilterValueSet} filter specification.
 * <p> Each filtering parameter has a lookup-able and operator type, and a value to filter for.
 */
public interface FilterValueSetParam extends Serializable
{
    /**
     * Returns the lookup-able for the filter parameter.
     * @return lookup-able
     */
    public FilterSpecLookupable getLookupable();

    /**
     * Returns the filter operator type.
     * @return filter operator type
     */
    public FilterOperator getFilterOperator();

    /**
     * Return the filter parameter constant to filter for.
     * @return filter parameter constant's value
     */
    public Object getFilterForValue();

    public void appendTo(StringWriter writer);
}
