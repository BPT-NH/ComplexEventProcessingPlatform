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

package com.espertech.esper.event.bean;

import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

public class BeanInstantiatorByFactoryFastClass implements BeanInstantiator
{
    private static Log log = LogFactory.getLog(BeanInstantiatorByFactoryFastClass.class);

    private final FastMethod method;

    public BeanInstantiatorByFactoryFastClass(FastMethod method) {
        this.method = method;
    }

    public Object instantiate() {
        try
        {
            return method.invoke(null, null);
        }
        catch (InvocationTargetException e)
        {
            String message = "Unexpected exception encountered invoking factory method '" + method.getName() + "' on class '" + method.getJavaMethod().getDeclaringClass().getName() + "': " + e.getTargetException().getMessage();
            log.error(message, e);
            return null;
        }
    }
}
