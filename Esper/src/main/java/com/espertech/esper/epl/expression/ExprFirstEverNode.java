/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.epl.agg.service.AggregationMethodFactory;

/**
 * Represents the "firstever" aggregate function is an expression tree.
 */
public class ExprFirstEverNode extends ExprAggregateNodeBase
{
    private static final long serialVersionUID = 1436994080693454617L;

    /**
     * Ctor.
     * @param distinct - flag indicating unique or non-unique value aggregation
     */
    public ExprFirstEverNode(boolean distinct)
    {
        super(distinct);
    }

    public AggregationMethodFactory validateAggregationChild(ExprValidationContext validationContext) throws ExprValidationException
    {
        if (this.getChildNodes().length > 2)
        {
            throw new ExprValidationException("First aggregation node must have less then 2 child nodes");
        }
        if (this.getChildNodes().length == 2) {
            super.validateFilter(this.getChildNodes()[1].getExprEvaluator());
        }
        return new ExprFirstEverNodeFactory(this.getChildNodes()[0].getExprEvaluator().getType(), this.getChildNodes().length == 2);
    }

    protected String getAggregationFunctionName()
    {
        return "firstever";
    }

    public final boolean equalsNodeAggregate(ExprAggregateNode node)
    {
        return node instanceof ExprFirstEverNode;

    }
}