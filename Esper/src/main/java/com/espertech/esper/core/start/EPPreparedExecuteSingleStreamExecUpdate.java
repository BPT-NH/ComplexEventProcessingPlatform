/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.named.NamedWindowProcessorInstance;
import com.espertech.esper.epl.named.NamedWindowUpdateHelper;
import com.espertech.esper.filter.FilterSpecCompiled;

import java.lang.annotation.Annotation;

public class EPPreparedExecuteSingleStreamExecUpdate implements EPPreparedExecuteSingleStreamExec
{
    private final FilterSpecCompiled filter;
    private final ExprNode optionalWhereClause;
    private final Annotation[] annotations;
    private final NamedWindowUpdateHelper updateHelper;

    public EPPreparedExecuteSingleStreamExecUpdate(FilterSpecCompiled filter, ExprNode optionalWhereClause, Annotation[] annotations, NamedWindowUpdateHelper updateHelper) {
        this.filter = filter;
        this.optionalWhereClause = optionalWhereClause;
        this.annotations = annotations;
        this.updateHelper = updateHelper;
    }

    public EventBean[] execute(NamedWindowProcessorInstance instance) {
        return instance.getTailViewInstance().snapshotUpdate(filter, optionalWhereClause, updateHelper, annotations);
    }
}
