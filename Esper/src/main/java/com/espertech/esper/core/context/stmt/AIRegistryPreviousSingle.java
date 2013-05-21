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

package com.espertech.esper.core.context.stmt;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.expression.ExprPreviousEvalStrategy;

import java.util.Collection;

public class AIRegistryPreviousSingle implements AIRegistryPrevious, ExprPreviousEvalStrategy {

    private ExprPreviousEvalStrategy strategy;

    public void assignService(int num, ExprPreviousEvalStrategy value) {
        this.strategy = value;
    }

    public void deassignService(int num) {
        this.strategy = null;
    }

    public int getAgentInstanceCount() {
        return strategy == null ? 0 : 1;
    }

    public Object evaluate(EventBean[] eventsPerStream, ExprEvaluatorContext exprEvaluatorContext) {
        return strategy.evaluate(eventsPerStream, exprEvaluatorContext);
    }

    public Collection<EventBean> evaluateGetCollEvents(EventBean[] eventsPerStream, ExprEvaluatorContext context) {
        return strategy.evaluateGetCollEvents(eventsPerStream, context);
    }

    public Collection evaluateGetCollScalar(EventBean[] eventsPerStream, ExprEvaluatorContext context) {
        return strategy.evaluateGetCollScalar(eventsPerStream, context);
    }

    public EventBean evaluateGetEventBean(EventBean[] eventsPerStream, ExprEvaluatorContext context) {
        return strategy.evaluateGetEventBean(eventsPerStream, context);
    }
}
