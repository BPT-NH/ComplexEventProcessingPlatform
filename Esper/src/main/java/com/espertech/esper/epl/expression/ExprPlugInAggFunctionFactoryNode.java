/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.hook.AggregationFunctionFactory;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.epl.agg.service.AggregationValidationContext;

/**
 * Represents a custom aggregation function in an expresson tree.
 */
public class ExprPlugInAggFunctionFactoryNode extends ExprAggregateNodeBase
{
    private static final long serialVersionUID = 65459875362787079L;
    private transient AggregationFunctionFactory aggregationFunctionFactory;
    private final String functionName;

    /**
     * Ctor.
     * @param distinct - flag indicating unique or non-unique value aggregation
     * @param aggregationFunctionFactory - is the base class for plug-in aggregation functions
     * @param functionName is the aggregation function name
     */
    public ExprPlugInAggFunctionFactoryNode(boolean distinct, AggregationFunctionFactory aggregationFunctionFactory, String functionName)
    {
        super(distinct);
        this.aggregationFunctionFactory = aggregationFunctionFactory;
        this.functionName = functionName;
        aggregationFunctionFactory.setFunctionName(functionName);
    }

    public AggregationMethodFactory validateAggregationChild(ExprValidationContext validationContext) throws ExprValidationException
    {
        Class[] parameterTypes = new Class[this.getChildNodes().length];
        Object[] constant = new Object[this.getChildNodes().length];
        boolean[] isConstant = new boolean[this.getChildNodes().length];
        ExprNode[] expressions = new ExprNode[this.getChildNodes().length];

        int count = 0;
        boolean hasDataWindows = true;
        for (ExprNode child : this.getChildNodes())
        {
            if (child.isConstantResult())
            {
                isConstant[count] = true;
                constant[count] = child.getExprEvaluator().evaluate(null, true, validationContext.getExprEvaluatorContext());
            }
            parameterTypes[count] = child.getExprEvaluator().getType();
            expressions[count] = child;

            count++;

            if (!ExprNodeUtility.hasRemoveStream(child, validationContext.getStreamTypeService())) {
                hasDataWindows = false;
            }
        }

        AggregationValidationContext context = new AggregationValidationContext(parameterTypes, isConstant, constant, super.isDistinct(), hasDataWindows, expressions);
        try
        {
            aggregationFunctionFactory.validate(context);
        }
        catch (RuntimeException ex)
        {
            throw new ExprValidationException("Plug-in aggregation function factory '" + functionName + "' failed validation: " + ex.getMessage());
        }

        Class childType = null;
        if (this.getChildNodes().length > 0)
        {
            childType = this.getChildNodes()[0].getExprEvaluator().getType();
        }

        return new ExprPlugInAggFunctionFactory(aggregationFunctionFactory, super.isDistinct(), childType);
    }

    public String getAggregationFunctionName()
    {
        return functionName;
    }

    public final boolean equalsNodeAggregate(ExprAggregateNode node)
    {
        if (!(node instanceof ExprPlugInAggFunctionFactoryNode))
        {
            return false;
        }

        ExprPlugInAggFunctionFactoryNode other = (ExprPlugInAggFunctionFactoryNode) node;
        return other.getAggregationFunctionName().equals(this.getAggregationFunctionName());
    }
}
