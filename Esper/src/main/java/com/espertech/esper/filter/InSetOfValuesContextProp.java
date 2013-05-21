/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.pattern.MatchedEventMap;
import com.espertech.esper.util.SimpleNumberCoercer;

/**
 * Event property value in a list of values following an in-keyword.
 */
public class InSetOfValuesContextProp implements FilterSpecParamInValue
{
    private static final long serialVersionUID = 1193129743441752016L;
    private final String propertyName;
    private transient final EventPropertyGetter getter;
    private transient final SimpleNumberCoercer numberCoercer;

    public InSetOfValuesContextProp(String propertyName, EventPropertyGetter getter, SimpleNumberCoercer coercer) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.numberCoercer = coercer;
    }

    public Object getFilterValue(MatchedEventMap matchedEvents, ExprEvaluatorContext evaluatorContext) {
        if (evaluatorContext.getContextProperties() == null) {
            return null;
        }
        Object result = getter.get(evaluatorContext.getContextProperties());

        if (numberCoercer == null) {
            return result;
        }
        return numberCoercer.coerceBoxed((Number) result);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InSetOfValuesContextProp that = (InSetOfValuesContextProp) o;

        if (!propertyName.equals(that.propertyName)) return false;

        return true;
    }

    public int hashCode() {
        return propertyName.hashCode();
    }
}
