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

package com.espertech.esper.event.arr;

import com.espertech.esper.client.*;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.EventTypeNestableGetterFactory;
import com.espertech.esper.event.bean.BeanEventPropertyGetter;
import com.espertech.esper.event.bean.BeanEventType;
import com.espertech.esper.event.map.MapEventPropertyGetter;
import com.espertech.esper.event.property.IndexedProperty;
import com.espertech.esper.event.property.MappedProperty;
import com.espertech.esper.event.property.Property;

import java.util.Map;

public class EventTypeNestableGetterFactoryObjectArray implements EventTypeNestableGetterFactory {

    private final String eventTypeName;
    private final Map<String, Integer> propertiesIndex;

    public EventTypeNestableGetterFactoryObjectArray(String eventTypeName, Map<String, Integer> propertiesIndex) {
        this.eventTypeName = eventTypeName;
        this.propertiesIndex = propertiesIndex;
    }

    public Map<String, Integer> getPropertiesIndex() {
        return propertiesIndex;
    }

    public EventPropertyGetter getPropertyProvidedGetter(Map<String, Object> nestableTypes, String propertyName, Property prop, EventAdapterService eventAdapterService) {
        return prop.getGetterObjectArray(propertiesIndex, nestableTypes, eventAdapterService);
    }

    public EventPropertyGetter getGetterProperty(String name, BeanEventType nativeFragmentType, EventAdapterService eventAdapterService) {
        int index = getAssertIndex(name);
        return new ObjectArrayEntryPropertyGetter(index, nativeFragmentType, eventAdapterService);
    }

    public EventPropertyGetter getGetterEventBean(String name) {
        int index = getAssertIndex(name);
        return new ObjectArrayEventBeanPropertyGetter(index);
    }

    public EventPropertyGetter getGetterEventBeanArray(String name, EventType eventType) {
        int index = getAssertIndex(name);
        return new ObjectArrayEventBeanArrayPropertyGetter(index, eventType.getUnderlyingType());
    }

    public EventPropertyGetter getGetterBeanNestedArray(String name, EventType eventType, EventAdapterService eventAdapterService) {
        int index = getAssertIndex(name);
        return new ObjectArrayFragmentArrayPropertyGetter(index, eventType, eventAdapterService);
    }

    public EventPropertyGetter getGetterIndexedEventBean(String propertyNameAtomic, int index) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayEventBeanArrayIndexedPropertyGetter(propertyIndex, index);
    }

    public EventPropertyGetter getGetterIndexedUnderlyingArray(String propertyNameAtomic, int index, EventAdapterService eventAdapterService, EventType innerType) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayArrayPropertyGetter(propertyIndex, index, eventAdapterService, innerType);
    }

    public EventPropertyGetter getGetterIndexedPOJO(String propertyNameAtomic, int index, EventAdapterService eventAdapterService, Class componentType) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayArrayPOJOEntryIndexedPropertyGetter(propertyIndex, index, eventAdapterService, componentType);
    }

    public EventPropertyGetter getGetterMappedProperty(String propertyNameAtomic, String key) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayMappedPropertyGetter(propertyIndex, key);
    }

    public EventPropertyGetter getGetterIndexedEntryEventBeanArrayElement(String propertyNameAtomic, int index, EventPropertyGetter nestedGetter) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayEventBeanArrayIndexedElementPropertyGetter(propertyIndex, index, nestedGetter);
    }

    public EventPropertyGetter getGetterIndexedEntryPOJO(String propertyNameAtomic, int index, BeanEventPropertyGetter nestedGetter, EventAdapterService eventAdapterService, Class propertyTypeGetter) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        return new ObjectArrayArrayPOJOBeanEntryIndexedPropertyGetter(propertyIndex, index, nestedGetter, eventAdapterService, propertyTypeGetter);
    }

    public EventPropertyGetter getGetterNestedMapProp(String propertyName, MapEventPropertyGetter getterNested) {
        int index = getAssertIndex(propertyName);
        return new ObjectArrayMapPropertyGetter(index, getterNested);
    }

    public EventPropertyGetter getGetterNestedPOJOProp(String propertyName, BeanEventPropertyGetter nestedGetter, EventAdapterService eventAdapterService, Class nestedReturnType) {
        int index = getAssertIndex(propertyName);
        return new ObjectArrayPOJOEntryPropertyGetter(index, nestedGetter, eventAdapterService, nestedReturnType);
    }

    public EventPropertyGetter getGetterNestedEventBean(String propertyName, EventPropertyGetter nestedGetter) {
        int index = getAssertIndex(propertyName);
        return new ObjectArrayEventBeanEntryPropertyGetter(index, nestedGetter);
    }

    public EventPropertyGetter getGetterNestedEntryBean(String propertyName, EventPropertyGetter getter, EventType innerType, EventAdapterService eventAdapterService) {
        int propertyIndex = getAssertIndex(propertyName);
        if (getter instanceof ObjectArrayEventPropertyGetter) {
            return new ObjectArrayNestedEntryPropertyGetterObjectArray(propertyIndex, innerType, eventAdapterService, (ObjectArrayEventPropertyGetter) getter);
        }
        return new ObjectArrayNestedEntryPropertyGetterMap(propertyIndex, innerType, eventAdapterService, (MapEventPropertyGetter) getter);
    }

    public EventPropertyGetter getGetterNestedEntryBeanArray(String propertyNameAtomic, int index, EventPropertyGetter getter, EventType innerType, EventAdapterService eventAdapterService) {
        int propertyIndex = getAssertIndex(propertyNameAtomic);
        if (getter instanceof ObjectArrayEventPropertyGetter) {
            return new ObjectArrayNestedEntryPropertyGetterArrayObjectArray(propertyIndex, innerType, eventAdapterService, index, (ObjectArrayEventPropertyGetter) getter);
        }
        return new ObjectArrayNestedEntryPropertyGetterArrayMap(propertyIndex, innerType, eventAdapterService, index, (MapEventPropertyGetter) getter);
    }

    public EventPropertyGetter getGetterBeanNested(String name, EventType eventType, EventAdapterService eventAdapterService) {
        int index = getAssertIndex(name);
        if (eventType instanceof ObjectArrayEventType) {
            return new ObjectArrayPropertyGetterDefaultObjectArray(index, eventType, eventAdapterService);
        }
        return new ObjectArrayPropertyGetterDefaultMap(index, eventType, eventAdapterService);
    }

    private int getAssertIndex(String propertyName) {
        Integer index = propertiesIndex.get(propertyName);
        if (index == null) {
            throw new PropertyAccessException("Property '" + propertyName + "' could not be found as a property of type '" + eventTypeName + "'");
        }
        return index;
    }

    public EventPropertyGetterMapped getPropertyProvidedGetterMap(Map<String, Object> nestableTypes, String mappedPropertyName, MappedProperty mappedProperty, EventAdapterService eventAdapterService) {
        return mappedProperty.getGetterObjectArray(propertiesIndex, nestableTypes, eventAdapterService);
    }

    public EventPropertyGetterIndexed getPropertyProvidedGetterIndexed(Map<String, Object> nestableTypes, String indexedPropertyName, IndexedProperty indexedProperty, EventAdapterService eventAdapterService) {
        return indexedProperty.getGetterObjectArray(propertiesIndex, nestableTypes, eventAdapterService);
    }
}
