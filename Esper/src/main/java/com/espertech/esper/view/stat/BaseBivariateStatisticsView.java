/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.stat;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.SingleEventIterator;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.view.ViewSupport;

import java.util.Iterator;

/**
 * View for computing statistics that require 2 input variable arrays containing X and Y datapoints.
 * Subclasses compute correlation or regression values, for instance.
 */
public abstract class BaseBivariateStatisticsView extends ViewSupport
{
    /**
     * This bean can be overridden by subclasses providing extra values such as correlation, regression.
     */
    protected BaseStatisticsBean statisticsBean = new BaseStatisticsBean();

    private final ExprNode expressionX;
    private final ExprNode expressionY;
    private final ExprEvaluator expressionXEval;
    private final ExprEvaluator expressionYEval;
    private final EventBean[] eventsPerStream = new EventBean[1];

    /**
     * Services required by implementing classes.
     */
    protected final AgentInstanceContext agentInstanceContext;

    /**
     * Additional properties.
     */
    protected final StatViewAdditionalProps additionalProps;

    /**
     * Event type.
     */
    protected final EventType eventType;

    protected Object[] lastValuesEventNew;
    private EventBean lastNewEvent;

    /**
     * Populate bean.
     * @param baseStatisticsBean results
     * @param eventAdapterService event adapters
     * @param eventType type
     * @param additionalProps additional props
     * @param decoration decoration values
     * @return bean
     */
    protected abstract EventBean populateMap(BaseStatisticsBean baseStatisticsBean, EventAdapterService eventAdapterService,
                                             EventType eventType, StatViewAdditionalProps additionalProps, Object[] decoration);

    /**
     * Constructor requires the name of the two fields to use in the parent view to compute the statistics.
     * @param expressionX is the expression to get the X values from
     * @param expressionY is the expression to get the Y values from
     * @param agentInstanceContext contains required view services
     * @param eventType type of event
     * @param additionalProps additional props
     */
    public BaseBivariateStatisticsView(AgentInstanceContext agentInstanceContext,
                                       ExprNode expressionX,
                                       ExprNode expressionY,
                                       EventType eventType,
                                       StatViewAdditionalProps additionalProps
                                       )
    {
        this.agentInstanceContext = agentInstanceContext;
        this.expressionX = expressionX;
        this.expressionXEval = expressionX.getExprEvaluator();
        this.expressionY = expressionY;
        this.expressionYEval = expressionY.getExprEvaluator();
        this.eventType = eventType;
        this.additionalProps = additionalProps;
    }

    public void update(EventBean[] newData, EventBean[] oldData)
    {
        // If we have child views, keep a reference to the old values, so we can fireStatementStopped them as old data event.
        EventBean oldValues = null;
        if (lastNewEvent == null)
        {
            if (this.hasViews())
            {
                oldValues = populateMap(statisticsBean, agentInstanceContext.getStatementContext().getEventAdapterService(), eventType, additionalProps, lastValuesEventNew);
            }
        }

        // add data points to the bean
        if (newData != null)
        {
            for (int i = 0; i < newData.length; i++)
            {
                eventsPerStream[0] = newData[i];
                Number xnum = (Number) expressionXEval.evaluate(eventsPerStream, true, agentInstanceContext);
                Number ynum = (Number) expressionYEval.evaluate(eventsPerStream, true, agentInstanceContext);
                if (xnum != null && ynum != null) {
                    double X = xnum.doubleValue();
                    double Y = ynum.doubleValue();
                    statisticsBean.addPoint(X, Y);
                }
            }

            if ((additionalProps != null) && (newData.length != 0)) {
                if (lastValuesEventNew == null) {
                    lastValuesEventNew = new Object[additionalProps.getAdditionalExpr().length];
                }
                for (int val = 0; val < additionalProps.getAdditionalExpr().length; val++) {
                    lastValuesEventNew[val] = additionalProps.getAdditionalExpr()[val].evaluate(eventsPerStream, true, agentInstanceContext);
                }
            }
        }

        // remove data points from the bean
        if (oldData != null)
        {
            for (int i = 0; i < oldData.length; i++)
            {
                eventsPerStream[0] = oldData[i];
                Number xnum = (Number) expressionXEval.evaluate(eventsPerStream, true, agentInstanceContext);
                Number ynum = (Number) expressionYEval.evaluate(eventsPerStream, true, agentInstanceContext);
                if (xnum != null && ynum != null) {
                    double X = xnum.doubleValue();
                    double Y = ynum.doubleValue();
                    statisticsBean.removePoint(X, Y);
                }
            }
        }

        // If there are child view, fireStatementStopped update method
        if (this.hasViews())
        {
            EventBean newDataMap = populateMap(statisticsBean, agentInstanceContext.getStatementContext().getEventAdapterService(), eventType, additionalProps, lastValuesEventNew);

            if (lastNewEvent == null)
            {
                updateChildren(new EventBean[] {newDataMap}, new EventBean[] {oldValues});
            }
            else
            {
                updateChildren(new EventBean[] {newDataMap}, new EventBean[] {lastNewEvent});
            }

            lastNewEvent = newDataMap;
        }
    }

    public final Iterator<EventBean> iterator()
    {
        return new SingleEventIterator(populateMap(statisticsBean,
                agentInstanceContext.getStatementContext().getEventAdapterService(),
                eventType, additionalProps, lastValuesEventNew));
    }

    /**
     * Returns the expression supplying X data points.
     * @return X expression
     */
    public final ExprNode getExpressionX()
    {
        return expressionX;
    }

    /**
     * Returns the expression supplying Y data points.
     * @return Y expression
     */
    public final ExprNode getExpressionY()
    {
        return expressionY;
    }

    public BaseStatisticsBean getStatisticsBean() {
        return statisticsBean;
    }

    public Object[] getLastValuesEventNew() {
        return lastValuesEventNew;
    }

    public void setLastValuesEventNew(Object[] lastValuesEventNew) {
        this.lastValuesEventNew = lastValuesEventNew;
    }

    public StatViewAdditionalProps getAdditionalProps() {
        return additionalProps;
    }
}
