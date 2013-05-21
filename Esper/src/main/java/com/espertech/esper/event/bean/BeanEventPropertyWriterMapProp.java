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

import com.espertech.esper.client.EventBean;
import net.sf.cglib.reflect.FastMethod;

public class BeanEventPropertyWriterMapProp extends BeanEventPropertyWriter {

    private final String key;

    public BeanEventPropertyWriterMapProp(Class clazz, FastMethod writerMethod, String key) {
        super(clazz, writerMethod);
        this.key = key;
    }

    public void write(Object value, EventBean target) {
        super.invoke(new Object[] {key, value}, target.getUnderlying());
    }
}
