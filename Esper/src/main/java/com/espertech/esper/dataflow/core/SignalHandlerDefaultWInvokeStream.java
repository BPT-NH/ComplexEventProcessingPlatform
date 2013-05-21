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

package com.espertech.esper.dataflow.core;

import com.espertech.esper.client.dataflow.EPDataFlowSignal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SignalHandlerDefaultWInvokeStream extends SignalHandlerDefaultWInvoke {

    private final int streamNum;

    public SignalHandlerDefaultWInvokeStream(Object target, Method method, int streamNum) {
        super(target, method);
        this.streamNum = streamNum;
    }

    @Override
    public void handleSignalInternal(EPDataFlowSignal signal) throws InvocationTargetException {
        fastMethod.invoke(target, new Object[] {streamNum, signal});
    }
}
