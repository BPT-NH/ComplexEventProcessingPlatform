/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.service;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Collection;

/**
 * Implements an aggregation service for match recognize.
 */
public class AggregationServiceMatchRecognizeImpl implements AggregationServiceMatchRecognize
{
    private ExprEvaluator evaluatorsEachStream[][];
    private AggregationMethod aggregatorsEachStream[][];
    private AggregationMethod aggregatorsAll[];

    public AggregationServiceMatchRecognizeImpl(ExprEvaluator[][] evaluatorsEachStream, AggregationMethod[][] aggregatorsEachStream, AggregationMethod[] aggregatorsAll) {
        this.evaluatorsEachStream = evaluatorsEachStream;
        this.aggregatorsEachStream = aggregatorsEachStream;
        this.aggregatorsAll = aggregatorsAll;
    }

    public void applyEnter(EventBean[] eventsPerStream, int streamId, ExprEvaluatorContext exprEvaluatorContext) {

        ExprEvaluator[] evaluatorsStream = evaluatorsEachStream[streamId];
        if (evaluatorsStream == null)
        {
            return;
        }

        AggregationMethod[] aggregatorsStream = aggregatorsEachStream[streamId];
        for (int j = 0; j < evaluatorsStream.length; j++)
        {
            Object columnResult = evaluatorsStream[j].evaluate(eventsPerStream, true, exprEvaluatorContext);
            aggregatorsStream[j].enter(columnResult);
        }
    }

    public Object getValue(int column, int agentInstanceId)
    {
        return aggregatorsAll[column].getValue();
    }

    public Collection<EventBean> getCollection(int column, ExprEvaluatorContext context) {
        return null;
    }

    public EventBean getEventBean(int column, ExprEvaluatorContext context) {
        return null;
    }

    public void clearResults()
    {
        for (AggregationMethod aggregator : aggregatorsAll)
        {
            aggregator.clear();
        }
    }
}