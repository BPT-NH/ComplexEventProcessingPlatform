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

package com.espertech.esper.plugin;

import com.espertech.esper.client.ConfigurationPlugInAggregationMultiFunction;
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprValidationContext;

/**
 * Context for use with {@link PlugInAggregationMultiFunctionFactory} provides
 * information about an aggregation function at the time of validation.
 * <p>
 *     At validation time the event type information, parameter expressions
 *     and other statement-specific services are available.
 * </p>
 */
public class PlugInAggregationMultiFunctionValidationContext {
    private final String functionName;
    private final EventType[] eventTypes;
    private final ExprNode[] parameterExpressions;
    private final String engineURI;
    private final String statementName;
    private final ExprValidationContext validationContext;
    private final ConfigurationPlugInAggregationMultiFunction config;

    /**
     * Ctor.
     * @param functionName the aggregation function name
     * @param eventTypes the event types of all events in the select clause
     * @param parameterExpressions the parameter expressions
     * @param engineURI the engine URI
     * @param statementName the statement name
     * @param validationContext additional validation contextual services
     * @param config the original configuration object for the aggregation multi-function
     */
    public PlugInAggregationMultiFunctionValidationContext(String functionName, EventType[] eventTypes, ExprNode[] parameterExpressions, String engineURI, String statementName, ExprValidationContext validationContext, ConfigurationPlugInAggregationMultiFunction config) {
        this.functionName = functionName;
        this.eventTypes = eventTypes;
        this.parameterExpressions = parameterExpressions;
        this.engineURI = engineURI;
        this.statementName = statementName;
        this.validationContext = validationContext;
        this.config = config;
    }

    /**
     * Returns the aggregation function name
     * @return aggregation function name
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Returns the event types of all events in the select clause
     * @return types
     */
    public EventType[] getEventTypes() {
        return eventTypes;
    }

    /**
     * Returns parameters expressions to this aggregation function.
     * @return parameter expressions
     */
    public ExprNode[] getParameterExpressions() {
        return parameterExpressions;
    }

    /**
     * Returns the engine URI.
     * @return engine URI.
     */
    public String getEngineURI() {
        return engineURI;
    }

    /**
     * Returns the statement name.
     * @return statement name
     */
    public String getStatementName() {
        return statementName;
    }

    /**
     * Returns additional validation contextual services.
     * @return validation context
     */
    public ExprValidationContext getValidationContext() {
        return validationContext;
    }

    /**
     * Returns the original configuration object for the aggregation multi-function
     * @return config
     */
    public ConfigurationPlugInAggregationMultiFunction getConfig() {
        return config;
    }
}
