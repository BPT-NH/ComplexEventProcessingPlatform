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

package com.espertech.esper.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ObjectInputStreamWithTCCL extends ObjectInputStream {

    private static Log log = LogFactory.getLog(ObjectInputStreamWithTCCL.class);

    public ObjectInputStreamWithTCCL(InputStream input) throws IOException {
        super(input);
    }

    public ObjectInputStreamWithTCCL() throws IOException, SecurityException {
    }

    @Override
    public Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

        if (log.isDebugEnabled()) {
            log.debug("Resolving class " + desc.getName() + " id " + desc.getSerialVersionUID() + " classloader " + Thread.currentThread().getContextClassLoader().getClass());
        }

        ClassLoader currentTccl = null;
        try {
            currentTccl = Thread.currentThread().getContextClassLoader();
            return currentTccl.loadClass(desc.getName());
        }
        catch (Exception e) {
        }
        return super.resolveClass(desc);
    }
}
