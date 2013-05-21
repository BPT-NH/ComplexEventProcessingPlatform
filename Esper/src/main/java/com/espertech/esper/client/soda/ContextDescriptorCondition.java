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

package com.espertech.esper.client.soda;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * For use with overlapping or non-overlapping contexts, implementations represents a condition for starting/initiating
 * or ending/terminating a context.
 */
public interface ContextDescriptorCondition extends Serializable {

    /**
     * Populate the EPL.
     * @param writer output
     * @param formatter formatter
     */
    public void toEPL(StringWriter writer, EPStatementFormatter formatter);
}
