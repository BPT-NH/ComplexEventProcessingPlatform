/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import com.espertech.esper.filter.FilterSpecCompiled;

/**
 * Starts and provides the stop method for EPL statements.
 */
public class EPPreparedExecuteSingleStreamDelete extends EPPreparedExecuteSingleStream
{
    public EPPreparedExecuteSingleStreamDelete(StatementSpecCompiled statementSpec, EPServicesContext services, StatementContext statementContext) throws ExprValidationException {
        super(statementSpec, services, statementContext);
    }

    public EPPreparedExecuteSingleStreamExec getExecutor(FilterSpecCompiled filter, String aliasName) {
        return new EPPreparedExecuteSingleStreamExecDelete(filter, statementSpec.getFilterRootNode(), statementSpec.getAnnotations());
    }
}
