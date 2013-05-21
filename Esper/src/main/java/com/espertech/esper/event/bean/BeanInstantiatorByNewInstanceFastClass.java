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
import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

public class BeanInstantiatorByNewInstanceFastClass implements BeanInstantiator
{
    private static Log log = LogFactory.getLog(BeanInstantiatorByNewInstanceFastClass.class);

    private final FastClass fastClass;

    public BeanInstantiatorByNewInstanceFastClass(FastClass fastClass) {
        this.fastClass = fastClass;
    }

    public Object instantiate() {
        try
        {
            return fastClass.newInstance();
        }
        catch (InvocationTargetException e)
        {
            String message = "Unexpected exception encountered invoking newInstance on class '" + fastClass.getJavaClass().getName() + "': " + e.getTargetException().getMessage();
            log.error(message, e);
            return null;
        }
    }
}
