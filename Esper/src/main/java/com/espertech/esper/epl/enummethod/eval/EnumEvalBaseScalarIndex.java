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

public abstract class EnumEvalBaseScalarIndex implements EnumEval {

    protected final ExprEvaluator innerExpression;
    protected final int streamNumLambda;
    protected final ObjectArrayEventType evalEventType;
    protected final ObjectArrayEventType indexEventType;

    public EnumEvalBaseScalarIndex(ExprEvaluator innerExpression, int streamNumLambda, ObjectArrayEventType evalEventType, ObjectArrayEventType indexEventType) {
        this.innerExpression = innerExpression;
        this.streamNumLambda = streamNumLambda;
        this.evalEventType = evalEventType;
        this.indexEventType = indexEventType;
    }

    public int getStreamNumSize() {
        return streamNumLambda + 2;
    }
}
