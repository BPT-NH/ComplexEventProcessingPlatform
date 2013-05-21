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
import java.lang.reflect.Method;

public class BeanInstantiatorByFactoryReflection implements BeanInstantiator
{
    private static Log log = LogFactory.getLog(BeanInstantiatorByFactoryReflection.class);

    private final Method method;

    public BeanInstantiatorByFactoryReflection(Method method) {
        this.method = method;
    }

    public Object instantiate() {
        try
        {
            return method.invoke(null, null);
        }
        catch (InvocationTargetException e)
        {
            String message = "Unexpected exception encountered invoking factory method '" + method.getName() + "' on class '" + method.getDeclaringClass().getName() + "': " + e.getTargetException().getMessage();
            log.error(message, e);
            return null;
        }
        catch (IllegalAccessException ex) {
            String message = "Unexpected exception encountered invoking factory method '" + method.getName() + "' on class '" + method.getDeclaringClass().getName() + "': " + ex.getMessage();
            log.error(message, ex);
            return null;
        }
    }
}
