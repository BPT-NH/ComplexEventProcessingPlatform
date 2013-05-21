/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

/**
 * Visitor that early-exists when it finds a context partition property.
 */
public class ExprNodeContextPropertiesVisitor implements ExprNodeVisitor
{
    private boolean found;

    public boolean isVisit(ExprNode exprNode)
    {
        return !found;
    }

    public void visit(ExprNode exprNode)
    {
        if (!(exprNode instanceof ExprContextPropertyNode)) {
            return;
        }
        found = true;
    }

    public boolean isFound() {
        return found;
    }
}
