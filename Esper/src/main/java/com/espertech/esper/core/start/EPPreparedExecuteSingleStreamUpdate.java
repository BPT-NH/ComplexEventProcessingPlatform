/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EventType;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.ExprEvaluatorContextStatement;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.core.StreamTypeServiceImpl;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprNodeUtility;
import com.espertech.esper.epl.expression.ExprValidationContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.named.NamedWindowUpdateHelper;
import com.espertech.esper.epl.spec.FireAndForgetSpecUpdate;
import com.espertech.esper.epl.spec.OnTriggerSetAssignment;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import com.espertech.esper.event.EventTypeSPI;
import com.espertech.esper.filter.FilterSpecCompiled;

/**
 * Starts and provides the stop method for EPL statements.
 */
public class EPPreparedExecuteSingleStreamUpdate extends EPPreparedExecuteSingleStream
{
    public EPPreparedExecuteSingleStreamUpdate(StatementSpecCompiled statementSpec, EPServicesContext services, StatementContext statementContext) throws ExprValidationException {
        super(statementSpec, services, statementContext);
    }

    public EPPreparedExecuteSingleStreamExec getExecutor(FilterSpecCompiled filter, String aliasName) {
        FireAndForgetSpecUpdate updateSpec = (FireAndForgetSpecUpdate) statementSpec.getFireAndForgetSpec();

        StreamTypeServiceImpl assignmentTypeService = new StreamTypeServiceImpl(
                new EventType[] {processor.getNamedWindowType(), null, processor.getNamedWindowType()},
                new String[] {aliasName, "", EPStatementStartMethodOnTrigger.INITIAL_VALUE_STREAM_NAME},
                new boolean[] {true, true, true}, services.getEngineURI(), true);
        assignmentTypeService.setStreamZeroUnambigous(true);
        ExprEvaluatorContextStatement evaluatorContextStmt = new ExprEvaluatorContextStatement(statementContext);
        ExprValidationContext validationContext = new ExprValidationContext(assignmentTypeService, statementContext.getMethodResolutionService(), null, statementContext.getSchedulingService(), statementContext.getVariableService(), evaluatorContextStmt, statementContext.getEventAdapterService(), statementContext.getStatementName(), statementContext.getStatementId(), statementContext.getAnnotations(), statementContext.getContextDescriptor());

        // validate update expressions
        try {
            for (OnTriggerSetAssignment assignment : updateSpec.getAssignments())
            {
                ExprNode validated = ExprNodeUtility.getValidatedSubtree(assignment.getExpression(), validationContext);
                assignment.setExpression(validated);
                EPStatementStartMethodHelperValidate.validateNoAggregations(validated, "Aggregation functions may not be used within an update-clause");
            }
        }
        catch (ExprValidationException e) {
            throw new EPException(e.getMessage(), e);
        }

        // make updater
        NamedWindowUpdateHelper updateHelper;
        try {
            updateHelper = NamedWindowUpdateHelper.make(processor.getNamedWindowName(),
                    (EventTypeSPI) processor.getNamedWindowType(), updateSpec.getAssignments(), aliasName);
        }
        catch (ExprValidationException e) {
            throw new EPException(e.getMessage(), e);
        }

        return new EPPreparedExecuteSingleStreamExecUpdate(filter, statementSpec.getFilterRootNode(), statementSpec.getAnnotations(), updateHelper);
    }
}
