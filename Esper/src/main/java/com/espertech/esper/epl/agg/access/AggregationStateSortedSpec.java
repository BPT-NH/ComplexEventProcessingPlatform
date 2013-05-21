/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.access;

import com.espertech.esper.epl.expression.ExprEvaluator;

import java.util.Comparator;

public class AggregationStateSortedSpec
{
    private final int streamId;
    private final ExprEvaluator[] criteria;
    private final Comparator<Object> comparator;
    private final Object criteriaKeyBinding;

    public AggregationStateSortedSpec(int streamId, ExprEvaluator[] criteria, Comparator<Object> comparator, Object criteriaKeyBinding) {
        this.streamId = streamId;
        this.criteria = criteria;
        this.comparator = comparator;
        this.criteriaKeyBinding = criteriaKeyBinding;
    }

    public int getStreamId() {
        return streamId;
    }

    public ExprEvaluator[] getCriteria() {
        return criteria;
    }

    public Comparator<Object> getComparator() {
        return comparator;
    }

    public Object getCriteriaKeyBinding() {
        return criteriaKeyBinding;
    }
}
