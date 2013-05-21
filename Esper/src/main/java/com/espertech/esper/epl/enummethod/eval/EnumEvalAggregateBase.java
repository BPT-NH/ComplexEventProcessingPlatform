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

import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.event.arr.ObjectArrayEventType;

public class EnumEvalAggregateBase {

    protected ExprEvaluator initialization;
    protected ExprEvaluator innerExpression;
    protected int streamNumLambda;
    protected ObjectArrayEventType resultEventType;

    public EnumEvalAggregateBase(ExprEvaluator initialization,
                                 ExprEvaluator innerExpression, int streamNumLambda,
                                 ObjectArrayEventType resultEventType) {
        this.initialization = initialization;
        this.innerExpression = innerExpression;
        this.streamNumLambda = streamNumLambda;
        this.resultEventType = resultEventType;
    }

    public int getStreamNumSize() {
        return streamNumLambda + 2;
    }
}
