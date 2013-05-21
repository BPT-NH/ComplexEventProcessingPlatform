/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.view.std;

import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.view.View;

public interface MergeViewMarker extends View
{
    public ExprNode[] getGroupFieldNames();

    public void addParentView(AddPropertyValueView mergeDataView);
}
