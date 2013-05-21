/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.access;

/**
 * Enum for aggregation multi-function state type.
 */
public enum AggregationStateType
{
    /**
     * For "first" function.
     */
    FIRST,
    /**
     * For "last" function.
     */
    LAST,
    /**
     * For "window" function.
     */
    WINDOW
}