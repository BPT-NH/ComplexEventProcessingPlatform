/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.variable;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.SingleEventIterator;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.StatementResultService;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.OnTriggerSetDesc;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.view.ViewSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A factory for a view that handles the setting of variables upon receipt of a triggering event.
 */
public class OnSetVariableViewFactory
{
    private static final Log log = LogFactory.getLog(OnSetVariableViewFactory.class);
    private final EventAdapterService eventAdapterService;
    private final VariableService variableService;
    private final EventType eventType;
    private final VariableReadWritePackage variableReadWritePackage;
    private final StatementResultService statementResultService;

    /**
     * Ctor.
     * @param desc specification for the on-set statement
     * @param eventAdapterService for creating statements
     * @param variableService for setting variables
     * @param statementResultService for coordinating on whether insert and remove stream events should be posted
     * @param exprEvaluatorContext context for expression evalauation
     * @throws com.espertech.esper.epl.expression.ExprValidationException if the assignment expressions are invalid
     */
    public OnSetVariableViewFactory(String statementId, OnTriggerSetDesc desc, EventAdapterService eventAdapterService, VariableService variableService, StatementResultService statementResultService, ExprEvaluatorContext exprEvaluatorContext)
            throws ExprValidationException
    {
        this.eventAdapterService = eventAdapterService;
        this.variableService = variableService;
        this.statementResultService = statementResultService;

        variableReadWritePackage = new VariableReadWritePackage(desc.getAssignments(), variableService, eventAdapterService);
        String outputEventTypeName = statementId + "_outsetvar";
        eventType = eventAdapterService.createAnonymousMapType(outputEventTypeName, variableReadWritePackage.getVariableTypes());
    }

    public OnSetVariableView instantiate(ExprEvaluatorContext exprEvaluatorContext) {
        return new OnSetVariableView(this, exprEvaluatorContext);
    }

    public EventType getEventType()
    {
        return eventType;
    }

    public EventAdapterService getEventAdapterService() {
        return eventAdapterService;
    }

    public VariableService getVariableService() {
        return variableService;
    }

    public VariableReadWritePackage getVariableReadWritePackage() {
        return variableReadWritePackage;
    }

    public StatementResultService getStatementResultService() {
        return statementResultService;
    }
}
