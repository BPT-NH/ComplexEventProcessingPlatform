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

package com.espertech.esper.epl.agg.service;

import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprNodeUtility;

public class AggregationStateKeyWStream implements AggregationStateKey {
    private final int streamNum;
    private final AggregationStateTypeWStream stateType;
    private final ExprNode[] exprNodes;

    public AggregationStateKeyWStream(int streamNum, AggregationStateTypeWStream stateType, ExprNode[] exprNodes) {
        this.streamNum = streamNum;
        this.stateType = stateType;
        this.exprNodes = exprNodes;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregationStateKeyWStream that = (AggregationStateKeyWStream) o;

        if (streamNum != that.streamNum) return false;
        if (stateType != that.stateType) return false;
        if (!ExprNodeUtility.deepEquals(exprNodes, that.exprNodes)) return false;

        return true;
    }

    public int hashCode() {
        int result = streamNum;
        result = 31 * result + stateType.hashCode();
        return result;
    }
}
