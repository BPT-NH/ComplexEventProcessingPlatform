/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.ext;

import com.espertech.esper.client.EventType;
import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.core.service.ExprEvaluatorContextStatement;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.view.*;
import com.espertech.esper.view.window.RandomAccessByIndexGetter;

import java.util.List;

/**
 * Factory for rank window views.
 */
public class RankWindowViewFactory implements DataWindowViewFactory, DataWindowViewWithPrevious
{
    private final static String NAME = "Rank view";

    private List<ExprNode> viewParameters;

    /**
     * The unique-by expressions.
     */
    protected ExprNode[] uniqueCriteriaExpressions;

    /**
     * The sort-by expressions.
     */
    protected ExprNode[] sortCriteriaExpressions;


    /**
     * The flags defining the ascending or descending sort order.
     */
    protected boolean[] isDescendingValues;

    /**
     * The sort window size.
     */
    protected int sortWindowSize;

    private EventType eventType;

    public void setViewParameters(ViewFactoryContext viewFactoryContext, List<ExprNode> viewParams) throws ViewParameterException
    {
        this.viewParameters = viewParams;
    }

    public void attach(EventType parentEventType, StatementContext statementContext, ViewFactory optionalParentFactory, List<ViewFactory> parentViewFactories) throws ViewParameterException
    {
        eventType = parentEventType;
        String message = NAME + " requires a list of expressions providing unique keys, a numeric size parameter and a list of expressions providing sort keys";
        if (viewParameters.size() < 3)
        {
            throw new ViewParameterException(message);
        }

        // validate
        ExprNode[] validated = ViewFactorySupport.validate(NAME, parentEventType, statementContext, viewParameters, true);

        // find size-parameter index
        int indexNumericSize = -1;
        for (int i = 0; i < validated.length; i++) {
            if (validated[i] instanceof ExprConstantNode) {
                indexNumericSize = i;
                break;
            }
        }
        if (indexNumericSize == -1) {
            throw new ViewParameterException("Failed to find constant value for the numeric size parameter");
        }
        if (indexNumericSize == 0) {
            throw new ViewParameterException("Failed to find unique value expressions that are expected to occur before the numeric size parameter");
        }
        if (indexNumericSize == validated.length - 1) {
            throw new ViewParameterException("Failed to find sort key expressions after the numeric size parameter");
        }

        // validate non-constant for unique-keys and sort-keys
        for (int i = 0; i < indexNumericSize; i++)
        {
            ViewFactorySupport.assertReturnsNonConstant(NAME, validated[i], i);
        }
        for (int i = indexNumericSize+1; i < validated.length; i++)
        {
            ViewFactorySupport.assertReturnsNonConstant(NAME, validated[i], i);
        }

        // get sort size
        ExprEvaluatorContextStatement exprEvaluatorContext = new ExprEvaluatorContextStatement(statementContext);
        Object sortSize = ViewFactorySupport.evaluateAssertNoProperties(NAME, validated[indexNumericSize], indexNumericSize, exprEvaluatorContext);
        if ((sortSize == null) || (!(sortSize instanceof Number)))
        {
            throw new ViewParameterException(message);
        }
        sortWindowSize = ((Number) sortSize).intValue();

        // compile unique expressions
        uniqueCriteriaExpressions = new ExprNode[indexNumericSize];
        System.arraycopy(validated, 0, uniqueCriteriaExpressions, 0, indexNumericSize);

        // compile sort expressions
        sortCriteriaExpressions = new ExprNode[validated.length - indexNumericSize - 1];
        isDescendingValues = new boolean[sortCriteriaExpressions.length];

        int count = 0;
        for (int i = indexNumericSize + 1; i < validated.length; i++)
        {
            if (validated[i] instanceof ExprOrderedExpr)
            {
                isDescendingValues[count] = ((ExprOrderedExpr) validated[i]).isDescending();
                sortCriteriaExpressions[count] = validated[i].getChildNodes()[0];
            }
            else
            {
                sortCriteriaExpressions[count] = validated[i];
            }
            count++;
        }
    }

    public View makeView(AgentInstanceViewFactoryChainContext agentInstanceViewFactoryContext)
    {
        IStreamSortRankRandomAccess rankedRandomAccess = ViewServiceHelper.getOptPreviousExprSortedRankedAccess(agentInstanceViewFactoryContext);

        boolean useCollatorSort = false;
        if (agentInstanceViewFactoryContext.getAgentInstanceContext().getStatementContext().getConfigSnapshot() != null)
        {
            useCollatorSort = agentInstanceViewFactoryContext.getAgentInstanceContext().getStatementContext().getConfigSnapshot().getEngineDefaults().getLanguage().isSortUsingCollator();
        }

        ExprEvaluator[] uniqueEvals = ExprNodeUtility.getEvaluators(uniqueCriteriaExpressions);
        ExprEvaluator[] sortEvals = ExprNodeUtility.getEvaluators(sortCriteriaExpressions);
        return new RankWindowView(this, uniqueCriteriaExpressions, uniqueEvals, sortCriteriaExpressions, sortEvals, isDescendingValues, sortWindowSize, rankedRandomAccess, useCollatorSort, agentInstanceViewFactoryContext);
    }

    public Object makePreviousGetter() {
        return new RandomAccessByIndexGetter();
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public boolean canReuse(View view)
    {
        if (!(view instanceof SortWindowView))
        {
            return false;
        }

        SortWindowView other = (SortWindowView) view;
        if ((other.getSortWindowSize() != sortWindowSize) ||
            (!compare(other.getIsDescendingValues(), isDescendingValues)) ||
            (!ExprNodeUtility.deepEquals(other.getSortCriteriaExpressions(), sortCriteriaExpressions)) )
        {
            return false;
        }

        return other.isEmpty();
    }

    private boolean compare(boolean[] one, boolean[] two)
    {
        if (one.length != two.length)
        {
            return false;
        }

        for (int i = 0; i < one.length; i++)
        {
            if (one[i] != two[i])
            {
                return false;
            }
        }

        return true;
    }
}
