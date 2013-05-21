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

package com.espertech.esper.epl.spec.util;

import com.espertech.esper.epl.expression.ExprSubselectNode;
import com.espertech.esper.epl.spec.*;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.pattern.EvalFilterFactoryNode;
import com.espertech.esper.pattern.EvalNodeAnalysisResult;
import com.espertech.esper.pattern.EvalNodeUtil;

import java.util.ArrayList;
import java.util.List;

public class StatementSpecCompiledAnalyzerResult {

    private final List<FilterSpecCompiled> filters;
    private final List<NamedWindowConsumerStreamSpec> namedWindowConsumers;

    public StatementSpecCompiledAnalyzerResult(List<FilterSpecCompiled> filters, List<NamedWindowConsumerStreamSpec> namedWindowConsumers) {
        this.filters = filters;
        this.namedWindowConsumers = namedWindowConsumers;
    }

    public List<FilterSpecCompiled> getFilters() {
        return filters;
    }

    public List<NamedWindowConsumerStreamSpec> getNamedWindowConsumers() {
        return namedWindowConsumers;
    }
}
