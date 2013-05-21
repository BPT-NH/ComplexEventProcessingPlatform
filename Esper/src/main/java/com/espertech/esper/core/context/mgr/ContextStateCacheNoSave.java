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
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.spec.ContextDetailInitiatedTerminated;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.util.SerializerUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * For testing, only used within SPIs; Replaced by applicable EsperHA bindings.
 */
public class ContextStateCacheNoSave implements ContextStateCache {

    public final static ContextStatePathValueBinding DEFAULT_SPI_TEST_BINDING = new MyContextStatePathValueBindingSerializable();

    public ContextStatePathValueBinding getBinding(Object bindingInfo) {
        if (bindingInfo instanceof ContextDetailInitiatedTerminated) {
            return new ContextStateCacheNoSaveInitTermBinding();
        }
        return DEFAULT_SPI_TEST_BINDING;
    }

    public void updateContextPath(String contextName, ContextStatePathKey key, ContextStatePathValue value) {
        // no action required
    }

    public void addContextPath(String contextName, int level, int parentPath, int subPath, Integer optionalContextPartitionId, Object additionalInfo, ContextStatePathValueBinding binding) {
        // no action required
    }

    public void removeContextParentPath(String contextName, int level, int parentPath) {
        // no action required
    }

    public void removeContextPath(String contextName, int level, int parentPath, int subPath) {
        // no action required
    }

    public TreeMap<ContextStatePathKey, ContextStatePathValue> getContextPaths(String contextName) {
        return null; // no state required
    }

    public void removeContext(String contextName) {
        // no action required
    }

    /**
     * For testing, only used within SPIs; Replaced by applicable EsperHA bindings.
     */
    public static class MyContextStatePathValueBindingSerializable implements ContextStatePathValueBinding {
        public Object byteArrayToObject(byte[] bytes, EventAdapterService eventAdapterService) {
            return SerializerUtil.byteArrToObject(bytes);
        }

        public byte[] toByteArray(Object contextInfo) {
            return SerializerUtil.objectToByteArr(contextInfo);
        }
    }

    /**
     * For testing, only used within SPIs; Replaced by applicable EsperHA bindings.
     * Simple binding where any events get changed to type name and byte array.
     */
    public static class ContextStateCacheNoSaveInitTermBinding implements ContextStatePathValueBinding {

        public Object byteArrayToObject(byte[] bytes, EventAdapterService eventAdapterService) {
            ContextControllerInitTermState state = (ContextControllerInitTermState) SerializerUtil.byteArrToObject(bytes);
            for (Map.Entry<String, Object> entry : state.getPatternData().entrySet()) {
                if (entry.getValue() instanceof EventBeanNameValuePair) {
                    EventBeanNameValuePair event = (EventBeanNameValuePair) entry.getValue();
                    EventType type = eventAdapterService.getExistsTypeByName(event.getEventTypeName());
                    Object underlying = SerializerUtil.byteArrToObject(event.getBytes());
                    state.getPatternData().put(entry.getKey(), eventAdapterService.adapterForType(underlying, type));
                }
            }
            return state;
        }

        public byte[] toByteArray(Object contextInfo) {
            ContextControllerInitTermState state = (ContextControllerInitTermState) contextInfo;
            Map<String, Object> serializableProps = new HashMap<String, Object>(state.getPatternData());
            for (Map.Entry<String, Object> entry : state.getPatternData().entrySet()) {
                if (entry.getValue() instanceof EventBean) {
                    EventBean event = (EventBean) entry.getValue();
                    serializableProps.put(entry.getKey(), new EventBeanNameValuePair(event.getEventType().getName(), SerializerUtil.objectToByteArr(event.getUnderlying())));
                }
            }
            ContextControllerInitTermState serialized = new ContextControllerInitTermState(state.getStartTime(), serializableProps);
            return SerializerUtil.objectToByteArr(serialized);
        }
    }

    private static class EventBeanNameValuePair implements Serializable {
        private static final long serialVersionUID = 1385687612285835734L;
        private final String eventTypeName;
        private final byte[] bytes;

        private EventBeanNameValuePair(String eventTypeName, byte[] bytes) {
            this.eventTypeName = eventTypeName;
            this.bytes = bytes;
        }

        public String getEventTypeName() {
            return eventTypeName;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }
}
