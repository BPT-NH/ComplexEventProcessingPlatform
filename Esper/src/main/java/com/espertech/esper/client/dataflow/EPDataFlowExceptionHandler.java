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

package com.espertech.esper.client.dataflow;

/**
 * Handler for exceptions thrown by data flow operators.
 */
public interface EPDataFlowExceptionHandler {

    /**
     * Handle exception.
     * @param context provides all exception information
     */
    public void handle(EPDataFlowExceptionContext context);
}
