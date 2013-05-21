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

package com.espertech.esper.epl.enummethod.eval;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.agg.aggregator.AggregatorAvgBigDecimal;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.math.MathContext;
import java.util.Collection;

public class EnumEvalAverageBigDecimalEvents extends EnumEvalBase implements EnumEval {

    private final MathContext optionalMathContext;

    public EnumEvalAverageBigDecimalEvents(ExprEvaluator innerExpression, int streamCountIncoming, MathContext optionalMathContext) {
        super(innerExpression, streamCountIncoming);
        this.optionalMathContext = optionalMathContext;
    }

    public Object evaluateEnumMethod(EventBean[] eventsLambda, Collection target, boolean isNewData, ExprEvaluatorContext context) {

        AggregatorAvgBigDecimal agg = new AggregatorAvgBigDecimal(optionalMathContext);

        Collection<EventBean> beans = (Collection<EventBean>) target;
        for (EventBean next : beans) {
            eventsLambda[streamNumLambda] = next;

            Number num = (Number) innerExpression.evaluate(eventsLambda, isNewData, context);
            if (num == null) {
                continue;
            }
            agg.enter(num);
        }

        return agg.getValue();
    }
}
