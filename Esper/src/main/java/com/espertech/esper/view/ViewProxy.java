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

package com.espertech.esper.view;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.annotation.AuditEnum;
import com.espertech.esper.event.EventBeanUtility;
import com.espertech.esper.util.AuditPath;
import com.espertech.esper.util.JavaClassHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

public class ViewProxy implements java.lang.reflect.InvocationHandler {

    private static Method target = JavaClassHelper.getMethodByName(View.class, "update");

    private final String engineURI;
    private final String statementName;
    private final String viewName;
    private final View view;

    public static Object newInstance(String engineURI, String statementName, String viewName, View view) {
        return java.lang.reflect.Proxy.newProxyInstance(
                 view.getClass().getClassLoader(),
                JavaClassHelper.getSuperInterfaces(view.getClass()),
                new ViewProxy(engineURI, statementName, viewName, view));
    }

    public ViewProxy(String engineURI, String statementName, String viewName, View view) {
        this.engineURI = engineURI;
        this.statementName = statementName;
        this.viewName = viewName;
        this.view = view;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {

        if (!m.equals(target)) {
            return m.invoke(view, args);
        }

        Object result = m.invoke(view, args);
        if (AuditPath.isInfoEnabled()) {
            EventBean[] newData = (EventBean[]) args[0];
            EventBean[] oldData = (EventBean[]) args[1];
            AuditPath.auditLog(engineURI, statementName, AuditEnum.VIEW, viewName + " insert {" + EventBeanUtility.summarize(newData) + "} remove {" + EventBeanUtility.summarize(oldData) + "}");
        }
        return result;
    }
}

