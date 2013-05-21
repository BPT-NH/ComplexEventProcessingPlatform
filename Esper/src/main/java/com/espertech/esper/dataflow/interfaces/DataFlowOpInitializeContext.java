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

package com.espertech.esper.dataflow.interfaces;

import com.espertech.esper.core.service.StatementContext;

public class DataFlowOpInitializeContext {

    private final StatementContext statementContext;

    public DataFlowOpInitializeContext(StatementContext statementContext) {
        this.statementContext = statementContext;
    }

    public StatementContext getStatementContext() {
        return statementContext;
    }
}
