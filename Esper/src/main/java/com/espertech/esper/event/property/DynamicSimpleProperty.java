/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.event.property;

import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.arr.ObjectArrayDynamicPropertyGetter;
import com.espertech.esper.event.arr.ObjectArrayEventPropertyGetter;
import com.espertech.esper.event.bean.BeanEventType;
import com.espertech.esper.event.bean.DynamicSimplePropertyGetter;
import com.espertech.esper.event.map.MapDynamicPropertyGetter;
import com.espertech.esper.event.map.MapEventPropertyGetter;
import com.espertech.esper.event.xml.BaseXMLEventType;
import com.espertech.esper.event.xml.DOMAttributeAndElementGetter;
import com.espertech.esper.event.xml.SchemaElementComplex;
import com.espertech.esper.event.xml.SchemaItem;

import java.io.StringWriter;
import java.util.Map;

/**
 * Represents a dynamic simple property of a given name.
 * <p>
 * Dynamic properties always exist, have an Object type and are resolved to a method during runtime.
 */
public class DynamicSimpleProperty extends PropertyBase implements DynamicProperty
{
    /**
     * Ctor.
     * @param propertyName is the property name
     */
    public DynamicSimpleProperty(String propertyName)
    {
        super(propertyName);
    }

    public EventPropertyGetter getGetter(BeanEventType eventType, EventAdapterService eventAdapterService)
    {
        return new DynamicSimplePropertyGetter(propertyNameAtomic, eventAdapterService);
    }

    public boolean isDynamic()
    {
        return true;
    }

    public String[] toPropertyArray()
    {
        return new String[] {this.getPropertyNameAtomic()};
    }

    public Class getPropertyType(BeanEventType eventType, EventAdapterService eventAdapterService)
    {
        return Object.class;
    }

    public GenericPropertyDesc getPropertyTypeGeneric(BeanEventType beanEventType, EventAdapterService eventAdapterService)
    {
        return GenericPropertyDesc.getObjectGeneric();
    }

    public Class getPropertyTypeMap(Map optionalMapPropTypes, EventAdapterService eventAdapterService)
    {
        return Object.class;
    }

    public MapEventPropertyGetter getGetterMap(Map optionalMapPropTypes, EventAdapterService eventAdapterService)
    {
        return new MapDynamicPropertyGetter(propertyNameAtomic);
    }

    public void toPropertyEPL(StringWriter writer)
    {
        writer.append(propertyNameAtomic);
    }

    public EventPropertyGetter getGetterDOM(SchemaElementComplex complexProperty, EventAdapterService eventAdapterService, BaseXMLEventType eventType, String propertyExpression)
    {
        return new DOMAttributeAndElementGetter(propertyNameAtomic);
    }

    public EventPropertyGetter getGetterDOM()
    {
        return new DOMAttributeAndElementGetter(propertyNameAtomic);
    }

    public SchemaItem getPropertyTypeSchema(SchemaElementComplex complexProperty, EventAdapterService eventAdapterService)
    {
        return null;    // always returns Node
    }

    public ObjectArrayEventPropertyGetter getGetterObjectArray(Map<String, Integer> indexPerProperty, Map<String, Object> nestableTypes, EventAdapterService eventAdapterService) {
        return new ObjectArrayDynamicPropertyGetter(propertyNameAtomic);
    }
}
