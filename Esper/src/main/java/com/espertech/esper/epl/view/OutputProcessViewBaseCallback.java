/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.view;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.collection.MultiKey;
import com.espertech.esper.collection.UniformPair;
import com.espertech.esper.epl.core.ResultSetProcessor;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;

import java.util.Iterator;
import java.util.Set;

/**
 * Factory for output processing views.
 */
public class OutputProcessViewBaseCallback extends OutputProcessViewBase
{
    private final OutputProcessViewCallback callback;

    public OutputProcessViewBaseCallback(ResultSetProcessor resultSetProcessor, OutputProcessViewCallback callback) {
        super(resultSetProcessor);
        this.callback = callback;
    }

    public Iterator<EventBean> iterator() {
        return OutputStrategyUtil.getIterator(joinExecutionStrategy, resultSetProcessor, parentView, false);
    }

    public void process(Set<MultiKey<EventBean>> newEvents, Set<MultiKey<EventBean>> oldEvents, ExprEvaluatorContext exprEvaluatorContext) {
        UniformPair<EventBean[]> pair = resultSetProcessor.processJoinResult(newEvents, oldEvents, false);
        callback.outputViaCallback(pair.getFirst());
    }

    public void terminated() {
    }

    public void update(EventBean[] newData, EventBean[] oldData) {
        UniformPair<EventBean[]> pair = resultSetProcessor.processViewResult(newData, oldData, false);
        callback.outputViaCallback(pair.getFirst());
    }
}
