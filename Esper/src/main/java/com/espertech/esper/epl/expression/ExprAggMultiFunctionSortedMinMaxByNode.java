/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.agg.service.AggregationMethodFactory;
import com.espertech.esper.event.EventAdapterService;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

public class ExprAggMultiFunctionSortedMinMaxByNode extends ExprAggregateNodeBase implements ExprEvaluatorEnumeration
{
    private static final long serialVersionUID = -8407756454712340265L;
    private final boolean max;
    private final boolean ever;
    private final boolean sortedwin;

    private transient EventType containedType;

    /**
     * Ctor.
     */
    public ExprAggMultiFunctionSortedMinMaxByNode(boolean max, boolean ever, boolean sortedwin) {
        super(false);
        this.max = max;
        this.ever = ever;
        this.sortedwin = sortedwin;
    }

    public AggregationMethodFactory validateAggregationChild(ExprValidationContext validationContext) throws ExprValidationException
    {
        // validate that the streams referenced in the criteria are a single stream's
        Set<Integer> streams = ExprNodeUtility.getIdentStreamNumbers(getChildNodes()[0]);
        if (streams.size() > 1) {
            throw new ExprValidationException(getErrorPrefix() + " requires that any parameter expressions evaluate properties of the same stream");
        }
        int streamNum = streams.iterator().next();

        // validate that there is a remove stream, use "ever" if not
        boolean forceEver = false;
        if (!ever && ExprAggMultiFunctionLinearAccessNode.getIstreamOnly(validationContext.getStreamTypeService(), streamNum)) {
            if (sortedwin) {
                throw new ExprValidationException(getErrorPrefix() + " requires that a data window is declared for the stream");
            }
            forceEver = true;
        }

        // determine typing and evaluation
        containedType = validationContext.getStreamTypeService().getEventTypes()[streamNum];
        Class resultType = validationContext.getStreamTypeService().getEventTypes()[streamNum].getUnderlyingType();
        ExprEvaluator[] evaluators = ExprNodeUtility.getEvaluators(this.getChildNodes());

        // determine ordering ascending/descending and build criteria expression without "asc" marker
        ExprNode[] criteriaExpressions = new ExprNode[this.getChildNodes().length];
        boolean[] sortDescending = new boolean[getChildNodes().length];
        for (int i = 0; i < getChildNodes().length; i++) {
            ExprNode parameter = getChildNodes()[i];
            criteriaExpressions[i] = parameter;
            if (parameter instanceof ExprOrderedExpr) {
                ExprOrderedExpr ordered = (ExprOrderedExpr) parameter;
                sortDescending[i] = ordered.isDescending();
                if (!ordered.isDescending()) {
                    criteriaExpressions[i] = ordered.getChildNodes()[0];
                }
            }
        }

        return new ExprAggMultiFunctionSortedMinMaxByNodeFactory(streamNum, resultType, criteriaExpressions, validationContext.getMethodResolutionService(), sortDescending, evaluators, max, ever || forceEver, sortedwin);
    }

    public String getAggregationFunctionName() {
        if (sortedwin) {
            return "sorted";
        }
        if (ever) {
            return max ? "maxbyever" : "minbyever";
        }
        return max ? "maxby" : "minby";
    }

    public String toExpressionString()
    {
        StringWriter writer = new StringWriter();
        writer.append(getAggregationFunctionName());
        ExprNodeUtility.toExpressionStringParams(writer, this.getChildNodes(), false, null, false);
        return writer.toString();
    }

    public Collection<EventBean> evaluateGetROCollectionEvents(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        return super.aggregationResultFuture.getCollection(column, context);
    }

    public Collection evaluateGetROCollectionScalar(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        return null;
    }

    public EventType getEventTypeCollection(EventAdapterService eventAdapterService) {
        if (!sortedwin) {
            return null;
        }
        return containedType;
    }

    public Class getComponentTypeCollection() throws ExprValidationException {
        return null;
    }

    public EventType getEventTypeSingle(EventAdapterService eventAdapterService, String statementId) throws ExprValidationException {
        if (sortedwin) {
            return null;
        }
        return containedType;
    }

    public EventBean evaluateGetEventBean(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        return super.aggregationResultFuture.getEventBean(column, context);
    }

    @Override
    protected boolean equalsNodeAggregate(ExprAggregateNode node) {
        if (this == node) return true;
        if (node == null || getClass() != node.getClass()) return false;

        ExprAggMultiFunctionSortedMinMaxByNode that = (ExprAggMultiFunctionSortedMinMaxByNode) node;
        return max == that.max && ever == that.ever && sortedwin == that.sortedwin;
    }

    private String getErrorPrefix() {
        return "The '" + getAggregationFunctionName() + "' aggregation function";
    }
}