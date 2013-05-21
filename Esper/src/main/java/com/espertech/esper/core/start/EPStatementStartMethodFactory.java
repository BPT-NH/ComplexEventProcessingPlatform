/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Starts and provides the stop method for EPL statements.
 */
public class EPStatementStartMethodFactory
{
    private static final Log log = LogFactory.getLog(EPStatementStartMethodFactory.class);

    /**
     * Ctor.
     * @param statementSpec is a container for the definition of all statement constructs that
     * may have been used in the statement, i.e. if defines the select clauses, insert into, outer joins etc.
     */
    public static EPStatementStartMethod makeStartMethod(StatementSpecCompiled statementSpec)
    {
        if (statementSpec.getUpdateSpec() != null)
        {
            return new EPStatementStartMethodUpdate(statementSpec);
        }
        if (statementSpec.getOnTriggerDesc() != null)
        {
            return new EPStatementStartMethodOnTrigger(statementSpec);
        }
        else if (statementSpec.getCreateWindowDesc() != null)
        {
            return new EPStatementStartMethodCreateWindow(statementSpec);
        }
        else if (statementSpec.getCreateIndexDesc() != null)
        {
            return new EPStatementStartMethodCreateIndex(statementSpec);
        }
        else if (statementSpec.getCreateGraphDesc() != null)
        {
            return new EPStatementStartMethodCreateGraph(statementSpec);
        }
        else if (statementSpec.getCreateSchemaDesc() != null)
        {
            return new EPStatementStartMethodCreateSchema(statementSpec);
        }
        else if (statementSpec.getCreateVariableDesc() != null)
        {
            return new EPStatementStartMethodCreateVariable(statementSpec);
        }
        else if (statementSpec.getContextDesc() != null)
        {
            return new EPStatementStartMethodCreateContext(statementSpec);
        }
        else if (statementSpec.getCreateExpressionDesc() != null)
        {
            return new EPStatementStartMethodCreateExpression(statementSpec);
        }
        else
        {
            return new EPStatementStartMethodSelect(statementSpec);
        }
    }
}
