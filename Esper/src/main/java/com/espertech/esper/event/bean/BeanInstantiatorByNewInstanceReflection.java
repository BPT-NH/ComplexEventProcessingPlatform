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

import net.sf.cglib.reflect.FastClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

public class BeanInstantiatorByNewInstanceReflection implements BeanInstantiator
{
    private static Log log = LogFactory.getLog(BeanInstantiatorByNewInstanceReflection.class);

    private final Class clazz;

    public BeanInstantiatorByNewInstanceReflection(Class clazz) {
        this.clazz = clazz;
    }

    public Object instantiate() {
        try
        {
            return clazz.newInstance();
        }
        catch (IllegalAccessException e)
        {
            return handle(e);
        }
        catch (InstantiationException e)
        {
            return handle(e);
        }
    }

    private Object handle(Exception e) {
        String message = "Unexpected exception encountered invoking newInstance on class '" + clazz.getName() + "': " + e.getMessage();
        log.error(message, e);
        return null;
    }
}
