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
 * For use in data flow instantiation, may provide operator instances.
 */
public interface EPDataFlowOperatorProvider {

    /**
     * Called to see if the provider would like to provide the operator instance as described in the context.
     * @param context operator instance requested
     * @return operator instance, or null if the default empty construct instantiation for the operator class should be used
     */
    public Object provide(EPDataFlowOperatorProviderContext context);
}
