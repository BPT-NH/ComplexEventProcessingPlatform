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

package com.espertech.esper.core.context.mgr;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class ContextControllerHashedGetterHashMultiple implements EventPropertyGetter {
    private static final Log log = LogFactory.getLog(ContextControllerHashedGetterHashMultiple.class);

    private final ExprEvaluator[] evaluators;
    private final int granularity;

    public ContextControllerHashedGetterHashMultiple(List<ExprNode> nodes, int granularity) {
        evaluators = new ExprEvaluator[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            evaluators[i] = nodes.get(i).getExprEvaluator();
        }
        this.granularity = granularity;
    }

    public Object get(EventBean eventBean) throws PropertyAccessException {
        EventBean[] events = new EventBean[] {eventBean};

        int hashCode = 0;
        for (int i = 0; i < evaluators.length; i++) {
            Object result = evaluators[i].evaluate(events, true, null);
            if (result == null) {
                continue;
            }
            if (hashCode == 0) {
                hashCode = result.hashCode();
            }
            else {
                hashCode = 31 * hashCode + result.hashCode();
            }
        }

        if (hashCode >= 0) {
            return hashCode % granularity;
        }
        return -hashCode % granularity;
    }

    public boolean isExistsProperty(EventBean eventBean) {
        return false;
    }

    public Object getFragment(EventBean eventBean) throws PropertyAccessException {
        return null;
    }
}
