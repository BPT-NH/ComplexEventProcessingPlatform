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
 * Represents the count(...) and count(*) and count(distinct ...) aggregate function is an expression tree.
 */
public class ExprCountNode extends ExprAggregateNodeBase
{
    private static final long serialVersionUID = 1859320277242087598L;

    private final boolean hasFilter;

    /**
     * Ctor.
     * @param distinct - flag indicating unique or non-unique value aggregation
     */
    public ExprCountNode(boolean distinct, boolean hasFilter)
    {
        super(distinct);
        this.hasFilter = hasFilter;
    }

    public AggregationMethodFactory validateAggregationChild(ExprValidationContext validationContext) throws ExprValidationException
    {
        if (this.getChildNodes().length > 2)
        {
            throw new ExprValidationException("Count node must have less then 2 child nodes");
        }

        Class childType = null;
        boolean ignoreNulls = false;

        if (this.getChildNodes().length == 0) {
            // defaults
        }
        else if (this.getChildNodes().length == 1) {
            if (!hasFilter) {
                childType = this.getChildNodes()[0].getExprEvaluator().getType();
                ignoreNulls = true;
            }
            else {
                super.validateFilter(this.getChildNodes()[0].getExprEvaluator());
            }
        }
        else if (this.getChildNodes().length == 2) {
            childType = this.getChildNodes()[0].getExprEvaluator().getType();
            ignoreNulls = true;
        }
        return new ExprCountNodeFactory(ignoreNulls, super.isDistinct, childType, hasFilter);
    }

    protected String getAggregationFunctionName()
    {
        return "count";
    }

    public final boolean equalsNodeAggregate(ExprAggregateNode node)
    {
        if (!(node instanceof ExprCountNode))
        {
            return false;
        }

        return true;
    }
}
