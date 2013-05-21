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

package com.espertech.esper.core.context.mgr;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.epl.expression.ExprEvaluator;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.util.Serializer;
import com.espertech.esper.util.SerializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;

public class ContextControllerHashedGetterCRC32Serialized implements EventPropertyGetter {
    private static final Log log = LogFactory.getLog(ContextControllerHashedGetterCRC32Serialized.class);

    private final String statementName;
    private final ExprEvaluator[] evaluators;
    private final Serializer[] serializers;
    private final int granularity;

    public ContextControllerHashedGetterCRC32Serialized(String statementName, List<ExprNode> nodes, int granularity) {
        this.statementName = statementName;
        evaluators = new ExprEvaluator[nodes.size()];
        Class[] returnTypes = new Class[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            evaluators[i] = nodes.get(i).getExprEvaluator();
            returnTypes[i] = evaluators[i].getType();
        }
        serializers = SerializerFactory.getSerializers(returnTypes);
        this.granularity = granularity;
    }

    public Object get(EventBean eventBean) throws PropertyAccessException {
        EventBean[] events = new EventBean[] {eventBean};

        Object[] parameters = new Object[evaluators.length];
        for (int i = 0; i < serializers.length; i++) {
            parameters[i] = evaluators[i].evaluate(events, true, null);
        }

        byte[] bytes;
        try {
            bytes = SerializerFactory.serialize(serializers, parameters);
        } catch (IOException e) {
            log.error("Exception serializing parameters for computing consistent hash for statement '" + statementName + "': " + e.getMessage(), e);
            bytes = new byte[0];
        }

        CRC32 crc = new CRC32();
        crc.update(bytes);
        long value = crc.getValue() % granularity;

        int result = (int) value;
        if (result >= 0) {
            return result;
        }
        return -result;
    }

    public boolean isExistsProperty(EventBean eventBean) {
        return false;
    }

    public Object getFragment(EventBean eventBean) throws PropertyAccessException {
        return null;
    }
}
