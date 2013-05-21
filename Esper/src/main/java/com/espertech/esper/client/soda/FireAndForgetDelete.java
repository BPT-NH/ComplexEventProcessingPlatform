/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.client.soda;

import java.io.StringWriter;

/**
 * Fire-and-forget (on-demand) delete DML.
 */
public class FireAndForgetDelete implements FireAndForgetClause
{
    private static final long serialVersionUID = -3565886245820109541L;

    public void toEPLBeforeFrom(StringWriter writer) {
        writer.append("delete ");
    }

    public void toEPLAfterFrom(StringWriter writer) {
    }
}
