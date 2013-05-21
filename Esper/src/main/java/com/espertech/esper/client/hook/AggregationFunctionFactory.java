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

package com.espertech.esper.client.hook;

import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.AggregationValidationContext;

/**
 * Interface to implement for factories of aggregation functions.
 */
public interface AggregationFunctionFactory {

    /**
     * Sets the EPL function name assigned to the factory.
     * @param functionName assigned
     */
    public void setFunctionName(String functionName);

    /**
     * Implemented by plug-in aggregation functions to allow such functions to validate the
     * type of values passed to the function at statement compile time and to generally
     * interrogate parameter expressions.
     * @param validationContext expression information
     */
    public void validate(AggregationValidationContext validationContext);

    /**
     * Make a new, initalized aggregation state.
     * @return initialized aggregator
     */
    public AggregationMethod newAggregator();

    /**
     * Returns the type of the current value.
     * @return type of value returned by the aggregation methods
     */
    public Class getValueType();
}
