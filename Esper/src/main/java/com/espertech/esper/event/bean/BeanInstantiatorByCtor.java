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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanInstantiatorByCtor implements BeanInstantiator
{
    private static Log log = LogFactory.getLog(BeanInstantiatorByCtor.class);

    private final Constructor ctor;

    public BeanInstantiatorByCtor(Constructor ctor) {
        this.ctor = ctor;
    }

    public Object instantiate() {
        try
        {
            return ctor.newInstance();
        }
        catch (InvocationTargetException e)
        {
            String message = "Unexpected exception encountered invoking constructor '" + ctor.getName() + "' on class '" + ctor.getDeclaringClass().getName() + "': " + e.getTargetException().getMessage();
            log.error(message, e);
            return null;
        }
        catch (IllegalAccessException ex) {
            return handle(ex);
        }
        catch (InstantiationException ex) {
            return handle(ex);
        }
    }

    private Object handle(Exception e) {
        String message = "Unexpected exception encountered invoking newInstance on class '" + ctor.getDeclaringClass().getName() + "': " + e.getMessage();
        log.error(message, e);
        return null;
    }
}
