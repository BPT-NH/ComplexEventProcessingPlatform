/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.event;

import com.espertech.esper.client.*;
import com.espertech.esper.epl.parse.ASTFilterSpecHelper;
import com.espertech.esper.event.bean.BeanEventType;
import com.espertech.esper.event.property.IndexedProperty;
import com.espertech.esper.event.property.MappedProperty;
import com.espertech.esper.event.property.Property;
import com.espertech.esper.event.property.PropertyParser;
import com.espertech.esper.util.GraphUtil;
import com.espertech.esper.util.JavaClassHelper;

import java.util.*;

/**
 * Implementation of the {@link com.espertech.esper.client.EventType} interface for handling name value pairs.
 */
public abstract class BaseNestableEventType implements EventTypeSPI
{
    protected final EventTypeMetadata metadata;
    protected final String typeName;
    protected final EventAdapterService eventAdapterService;
    protected final EventType[] optionalSuperTypes;
    protected final Set<EventType> optionalDeepSupertypes;
    protected final int eventTypeId;
    protected final EventTypeNestableGetterFactory getterFactory;

    // Simple (not-nested) properties are stored here
    protected String[] propertyNames;       // Cache an array of property names so not to construct one frequently
    protected EventPropertyDescriptor[] propertyDescriptors;

    protected final Map<String, PropertySetDescriptorItem> propertyItems;
    protected Map<String, EventPropertyGetter> propertyGetterCache; // Mapping of all property names and getters

    // Nestable definition of Map contents is here
    protected Map<String, Object> nestableTypes;  // Deep definition of the map-type, containing nested maps and objects

    protected String startTimestampPropertyName;
    protected String endTimestampPropertyName;

    protected abstract void postUpdateNestableTypes();

    /**
     * Constructor takes a type name, map of property names and types, for
     * use with nestable Map events.
     * @param typeName is the event type name used to distinquish map types that have the same property types,
     * empty string for anonymous maps, or for insert-into statements generating map events
     * the stream name
     * @param propertyTypes is pairs of property name and type
     * @param eventAdapterService is required for access to objects properties within map values
     * @param optionalSuperTypes the supertypes to this type if any, or null if there are no supertypes
     * @param optionalDeepSupertypes the deep supertypes to this type if any, or null if there are no deep supertypes
     * @param metadata event type metadata
     */
    public BaseNestableEventType(EventTypeMetadata metadata,
                                 String typeName,
                                 int eventTypeId,
                                 EventAdapterService eventAdapterService,
                                 Map<String, Object> propertyTypes,
                                 EventType[] optionalSuperTypes,
                                 Set<EventType> optionalDeepSupertypes,
                                 ConfigurationEventTypeWithSupertype typeConfig,
                                 EventTypeNestableGetterFactory getterFactory
    )
    {
        this.metadata = metadata;
        this.eventTypeId = eventTypeId;
        this.typeName = typeName;
        this.eventAdapterService = eventAdapterService;
        this.getterFactory = getterFactory;

        this.optionalSuperTypes = optionalSuperTypes;
        if (optionalDeepSupertypes == null)
        {
            this.optionalDeepSupertypes = Collections.emptySet();
        }
        else
        {
            this.optionalDeepSupertypes = optionalDeepSupertypes;
        }

        // determine property set and prepare getters
        PropertySetDescriptor propertySet = EventTypeUtility.getNestableProperties(propertyTypes, eventAdapterService, getterFactory, optionalSuperTypes);
        
        nestableTypes = propertySet.getNestableTypes();
        propertyNames = propertySet.getPropertyNameArray();
        propertyItems = propertySet.getPropertyItems();
        propertyDescriptors = propertySet.getPropertyDescriptors().toArray(new EventPropertyDescriptor[propertySet.getPropertyDescriptors().size()]);

        EventTypeUtility.TimestampPropertyDesc desc = EventTypeUtility.validatedDetermineTimestampProps(this, typeConfig == null ? null : typeConfig.getStartTimestampPropertyName(), typeConfig == null ? null : typeConfig.getEndTimestampPropertyName(), optionalSuperTypes);
        startTimestampPropertyName = desc.getStart();
        endTimestampPropertyName = desc.getEnd();
    }

    public String getName()
    {
        return typeName;
    }

    public int getEventTypeId() {
        return eventTypeId;
    }

    public String getStartTimestampPropertyName() {
        return startTimestampPropertyName;
    }

    public String getEndTimestampPropertyName() {
        return endTimestampPropertyName;
    }

    public final Class getPropertyType(String propertyName)
    {
        return EventTypeUtility.getNestablePropertyType(propertyName, propertyItems, nestableTypes, eventAdapterService);
    }

    public EventPropertyGetter getGetter(final String propertyName)
    {
        if (propertyGetterCache == null) {
            propertyGetterCache = new HashMap<String, EventPropertyGetter>();
        }
        return EventTypeUtility.getNestableGetter(propertyName, propertyItems, propertyGetterCache, nestableTypes, eventAdapterService, getterFactory);
    }

    public EventPropertyGetterMapped getGetterMapped(String mappedPropertyName) {
        PropertySetDescriptorItem item = propertyItems.get(mappedPropertyName);
        if (item == null || !item.getPropertyDescriptor().isMapped()) {
            return null;
        }
        MappedProperty mappedProperty = new MappedProperty(mappedPropertyName);
        return getterFactory.getPropertyProvidedGetterMap(nestableTypes, mappedPropertyName, mappedProperty, this.eventAdapterService);
    }

    public EventPropertyGetterIndexed getGetterIndexed(String indexedPropertyName) {
        PropertySetDescriptorItem item = propertyItems.get(indexedPropertyName);
        if (item == null || !item.getPropertyDescriptor().isIndexed()) {
            return null;
        }
        IndexedProperty indexedProperty = new IndexedProperty(indexedPropertyName);
        return getterFactory.getPropertyProvidedGetterIndexed(nestableTypes, indexedPropertyName, indexedProperty, this.eventAdapterService);
    }

    public String[] getPropertyNames()
    {
        return propertyNames;
    }

    public boolean isProperty(String propertyName)
    {
        Class propertyType = getPropertyType(propertyName);
        if (propertyType == null)
        {
            // Could be a native null type, such as "insert into A select null as field..."
            if (propertyItems.containsKey(ASTFilterSpecHelper.unescapeDot(propertyName)))
            {
                return true;
            }
        }
        return propertyType != null;
    }

    public EventType[] getSuperTypes()
    {
        return optionalSuperTypes;
    }

    public Iterator<EventType> getDeepSuperTypes()
    {
        return optionalDeepSupertypes.iterator();
    }

    /**
     * Returns the name-type map of map properties, each value in the map
     * can be a Class or a Map<String, Object> (for nested maps).
     * @return is the property name and types
     */
    public Map<String, Object> getTypes()
    {
        return this.nestableTypes;
    }

    /**
     * Adds additional properties that do not yet exist on the given type.
     * <p.
     * Ignores properties already present. Allows nesting.
     * @param typeMap properties to add
     * @param eventAdapterService for resolving further map event types that are property types
     */
    public void addAdditionalProperties(Map<String, Object> typeMap, EventAdapterService eventAdapterService)
    {
        // merge type graphs
        nestableTypes = GraphUtil.mergeNestableMap(nestableTypes, typeMap);

        postUpdateNestableTypes();

        // construct getters and types for each property (new or old)
        PropertySetDescriptor propertySet = EventTypeUtility.getNestableProperties(typeMap, eventAdapterService, getterFactory, optionalSuperTypes);

        // add each new descriptor
        List<EventPropertyDescriptor> newPropertyDescriptors = new ArrayList<EventPropertyDescriptor>();
        for (EventPropertyDescriptor propertyDesc : propertySet.getPropertyDescriptors())
        {
            if (propertyItems.containsKey(propertyDesc.getPropertyName()))  // not a new property
            {
                continue;
            }
            newPropertyDescriptors.add(propertyDesc);
        }

        // add each that is not already present
        List<String> newPropertyNames = new ArrayList<String>();
        for (String propertyName : propertySet.getPropertyNameList())
        {
            if (propertyItems.containsKey(propertyName))  // not a new property
            {
                continue;
            }
            newPropertyNames.add(propertyName);
            propertyItems.put(propertyName, propertySet.getPropertyItems().get(propertyName));
        }

        // expand property name array
        String[] allPropertyNames = new String[propertyNames.length + newPropertyNames.size()];
        System.arraycopy(propertyNames, 0, allPropertyNames, 0, propertyNames.length);
        int count = propertyNames.length;
        for (String newProperty : newPropertyNames)
        {
            allPropertyNames[count++] = newProperty;
        }
        propertyNames = allPropertyNames;

        // expand descriptor array
        EventPropertyDescriptor[] allPropertyDescriptors = new EventPropertyDescriptor[propertyDescriptors.length + newPropertyNames.size()];
        System.arraycopy(propertyDescriptors, 0, allPropertyDescriptors, 0, propertyDescriptors.length);
        count = propertyDescriptors.length;
        for (EventPropertyDescriptor desc : newPropertyDescriptors)
        {
            allPropertyDescriptors[count++] = desc;
        }
        propertyDescriptors = allPropertyDescriptors;
    }

    public EventPropertyDescriptor[] getPropertyDescriptors()
    {
        return propertyDescriptors;
    }

    /**
     * Compares two sets of properties and determines if they are the same, allowing for
     * boxed/unboxed types, and nested map types.
     * @param setOne is the first set of properties
     * @param setTwo is the second set of properties
     * @param otherName name of the type compared to
     * @return null if the property set is equivalent or message if not
     */
    public static String isDeepEqualsProperties(String otherName, Map<String, Object> setOne, Map<String, Object> setTwo)
    {
        // Should have the same number of properties
        if (setOne.size() != setTwo.size())
        {
            return "Type by name '" + otherName + "' expects " + setOne.size() + " properties but receives " + setTwo.size() + " properties";
        }

        // Compare property by property
        for (Map.Entry<String, Object> entry : setOne.entrySet())
        {
            String propName = entry.getKey();
            Object setTwoType = setTwo.get(entry.getKey());
            boolean setTwoTypeFound = setTwo.containsKey(entry.getKey());
            Object setOneType = entry.getValue();

            // allow null for nested event types
            if ((setOneType instanceof String || setOneType instanceof EventType) && setTwoType == null) {
                continue;
            }
            if ((setTwoType instanceof String || setTwoType instanceof EventType) && setOneType == null) {
                continue;
            }
            if (!setTwoTypeFound) {
                return "The property '" + propName + "' is not provided but required";
            }
            if (setTwoType == null)
            {
                continue;
            }
            if (setOneType == null)
            {
                return "Type by name '" + otherName + "' in property '" + propName + "' incompatible with null-type or property name not found in target";
            }

            if ((setTwoType instanceof Class) && (setOneType instanceof Class))
            {
                Class boxedOther = JavaClassHelper.getBoxedType((Class)setTwoType);
                Class boxedThis = JavaClassHelper.getBoxedType((Class)setOneType);
                if (!boxedOther.equals(boxedThis))
                {
                    if (!JavaClassHelper.isSubclassOrImplementsInterface(boxedOther, boxedThis)) {
                        return "Type by name '" + otherName + "' in property '" + propName + "' expected " + boxedThis + " but receives " + boxedOther;
                    }
                }
            }
            else if ((setTwoType instanceof BeanEventType) && (setOneType instanceof Class))
            {
                Class boxedOther = JavaClassHelper.getBoxedType(((BeanEventType)setTwoType).getUnderlyingType());
                Class boxedThis = JavaClassHelper.getBoxedType((Class)setOneType);
                if (!boxedOther.equals(boxedThis))
                {
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected " + boxedThis + " but receives " + boxedOther;
                }
            }
            else if (setTwoType instanceof EventType[] && ((EventType[])setTwoType)[0] instanceof BeanEventType && setOneType instanceof Class && ((Class) setOneType).isArray())
            {
                Class boxedOther = JavaClassHelper.getBoxedType((((EventType[])setTwoType)[0]).getUnderlyingType());
                Class boxedThis = JavaClassHelper.getBoxedType(((Class)setOneType).getComponentType());
                if (!boxedOther.equals(boxedThis))
                {
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected " + boxedThis + " but receives " + boxedOther;
                }
            }
            else if ((setTwoType instanceof Map) && (setOneType instanceof Map))
            {
                String messageIsDeepEquals = isDeepEqualsProperties(propName, (Map<String, Object>)setOneType, (Map<String, Object>)setTwoType);
                if (messageIsDeepEquals != null)
                {
                    return messageIsDeepEquals;
                }
            }
            else if ((setTwoType instanceof EventType) && (setOneType instanceof EventType))
            {
                boolean mismatch;
                if (setTwoType instanceof EventTypeSPI && setOneType instanceof EventTypeSPI) {
                    mismatch = !((EventTypeSPI) setOneType).equalsCompareType((EventTypeSPI) setTwoType);
                }
                else {
                    mismatch = !setOneType.equals(setTwoType);
                }
                if (mismatch) {
                    EventType setOneEventType = (EventType) setOneType;
                    EventType setTwoEventType = (EventType) setTwoType;
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneEventType.getName() + "' but receives event type '" + setTwoEventType.getName() + "'";
                }
            }
            else if ((setTwoType instanceof String) && (setOneType instanceof EventType))
            {
                if (!((EventType) setOneType).getName().equals(setTwoType))
                {
                    EventType setOneEventType = (EventType) setOneType;
                    String setTwoEventType = (String) setTwoType;
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneEventType.getName() + "' but receives event type '" + setTwoEventType + "'";
                }
            }
            else if ((setTwoType instanceof EventType) && (setOneType instanceof String))
            {
                if (!((EventType) setTwoType).getName().equals(setOneType))
                {
                    String setOneEventType = (String) setOneType;
                    EventType setTwoEventType = (EventType) setTwoType;
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneEventType + "' but receives event type '" + setTwoEventType.getName() + "'";
                }
            }
            else if ((setTwoType instanceof String) && (setOneType instanceof String))
            {
                if (!setTwoType.equals(setOneType))
                {
                    String setOneEventType = (String) setOneType;
                    String setTwoEventType = (String) setTwoType;
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneEventType + "' but receives event type '" + setTwoEventType + "'";
                }
            }
            else if ((setTwoType instanceof EventType[]) && (setOneType instanceof String))
            {
                EventType[] setTwoTypeArr = (EventType[]) setTwoType;
                EventType setTwoFragmentType = setTwoTypeArr[0];
                String setOneTypeString = (String)setOneType;
                if (!(setOneTypeString.endsWith("[]"))) {
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneType + "' but receives event type '" + setTwoFragmentType.getName() + "[]'";
                }
                String setOneTypeNoArray = (setOneTypeString).replaceAll("\\[\\]", "");
                if (!(setTwoFragmentType.getName().equals(setOneTypeNoArray)))
                {
                    return "Type by name '" + otherName + "' in property '" + propName + "' expected event type '" + setOneTypeNoArray + "[]' but receives event type '" + setTwoFragmentType.getName() + "'";
                }
            }
            else
            {
                String typeOne = getTypeName(setOneType);
                String typeTwo = getTypeName(setTwoType);
                if (typeOne.equals(typeTwo)) {
                    continue;
                }
                return "Type by name '" + otherName + "' in property '" + propName + "' expected " + typeOne + " but receives " + typeTwo;
            }
        }

        return null;
    }

    private static String getTypeName(Object type)
    {
        if (type == null)
        {
            return "null";
        }
        if (type instanceof Class)
        {
            return ((Class) type).getName();
        }
        if (type instanceof EventType)
        {
            return "event type '" + ((EventType)type).getName() + "'";
        }
        if (type instanceof String) {
            Class boxedType = JavaClassHelper.getBoxedType(JavaClassHelper.getPrimitiveClassForName((String)type));
            if (boxedType != null) {
                return boxedType.getName();
            }
        }
        return type.getClass().getName();
    }

    public EventPropertyDescriptor getPropertyDescriptor(String propertyName)
    {
        PropertySetDescriptorItem item = propertyItems.get(propertyName);
        if (item == null) {
            return null;
        }
        return item.getPropertyDescriptor();
    }

    public EventTypeMetadata getMetadata()
    {
        return metadata;
    }

    public FragmentEventType getFragmentType(String propertyName)
    {
        PropertySetDescriptorItem item = propertyItems.get(propertyName);
        if (item != null)  // may contain null values
        {
            return item.getFragmentEventType();
        }

        // see if this is a nested property
        int index = ASTFilterSpecHelper.unescapedIndexOfDot(propertyName);
        if (index == -1)
        {
            // dynamic simple property
            if (propertyName.endsWith("?"))
            {
                return null;
            }

            // parse, can be an indexed property
            Property property = PropertyParser.parse(propertyName, false);
            if (property instanceof IndexedProperty)
            {
                IndexedProperty indexedProp = (IndexedProperty) property;
                Object type = nestableTypes.get(indexedProp.getPropertyNameAtomic());
                if (type == null)
                {
                    return null;
                }
                else if (type instanceof EventType[])
                {
                    EventType eventType = ((EventType[]) type)[0];
                    return new FragmentEventType(eventType, false, false);
                }
                else if (type instanceof String)
                {
                    String propTypeName = type.toString();
                    boolean isArray = EventTypeUtility.isPropertyArray(propTypeName);
                    if (!isArray) {
                        return null;
                    }
                    propTypeName = EventTypeUtility.getPropertyRemoveArray(propTypeName);
                    EventType innerType = eventAdapterService.getExistsTypeByName(propTypeName);
                    if (!(innerType instanceof BaseNestableEventType))
                    {
                        return null;
                    }
                    return new FragmentEventType(innerType, false, false);  // false since an index is present
                }
                if (!(type instanceof Class))
                {
                    return null;
                }
                if (!((Class) type).isArray())
                {
                    return null;
                }
                // its an array
                return EventBeanUtility.createNativeFragmentType(((Class)type).getComponentType(), null, eventAdapterService);
            }
            else if (property instanceof MappedProperty)
            {
                // No type information available for the inner event
                return null;
            }
            else
            {
                return null;
            }
        }

        // Map event types allow 2 types of properties inside:
        //   - a property that is a Java object is interrogated via bean property getters and BeanEventType
        //   - a property that is a Map itself is interrogated via map property getters
        // The property getters therefore act on

        // Take apart the nested property into a map key and a nested value class property name
        String propertyMap = ASTFilterSpecHelper.unescapeDot(propertyName.substring(0, index));
        String propertyNested = propertyName.substring(index + 1, propertyName.length());

        // If the property is dynamic, it cannot be a fragment
        if (propertyMap.endsWith("?"))
        {
            return null;
        }

        Object nestedType = nestableTypes.get(propertyMap);
        if (nestedType == null)
        {
            // parse, can be an indexed property
            Property property = PropertyParser.parse(propertyMap, false);
            if (property instanceof IndexedProperty)
            {
                IndexedProperty indexedProp = (IndexedProperty) property;
                Object type = nestableTypes.get(indexedProp.getPropertyNameAtomic());
                if (type == null)
                {
                    return null;
                }
                // handle map-in-map case
                if (type instanceof String) {
                    String propTypeName = type.toString();
                    boolean isArray = EventTypeUtility.isPropertyArray(propTypeName);
                    if (isArray) {
                        propTypeName = EventTypeUtility.getPropertyRemoveArray(propTypeName);
                    }
                    EventType innerType = eventAdapterService.getExistsTypeByName(propTypeName);
                    if (!(innerType instanceof BaseNestableEventType))
                    {
                        return null;
                    }
                    return innerType.getFragmentType(propertyNested);
                }
                // handle eventtype[] in map
                else if (type instanceof EventType[])
                {
                    EventType innerType = ((EventType[]) type)[0];
                    return innerType.getFragmentType(propertyNested);
                }
                // handle array class in map case
                else
                {
                    if (!(type instanceof Class))
                    {
                        return null;
                    }
                    if (!((Class) type).isArray())
                    {
                        return null;
                    }
                    FragmentEventType fragmentParent = EventBeanUtility.createNativeFragmentType((Class) type, null, eventAdapterService);
                    if (fragmentParent == null)
                    {
                        return null;
                    }
                    return fragmentParent.getFragmentType().getFragmentType(propertyNested);
                }
            }
            else if (property instanceof MappedProperty)
            {
                // No type information available for the property's map value
                return null;
            }
            else
            {
                return null;
            }
        }

        // If there is a map value in the map, return the Object value if this is a dynamic property
        if (nestedType == Map.class)
        {
            return null;
        }
        else if (nestedType instanceof Map)
        {
            return null;
        }
        else if (nestedType instanceof Class)
        {
            Class simpleClass = (Class) nestedType;
            if (!JavaClassHelper.isFragmentableType(simpleClass))
            {
                return null;
            }
            EventType nestedEventType = eventAdapterService.getBeanEventTypeFactory().createBeanTypeDefaultName(simpleClass);
            return nestedEventType.getFragmentType(propertyNested);
        }
        else if (nestedType instanceof EventType)
        {
            EventType innerType = (EventType) nestedType;
            return innerType.getFragmentType(propertyNested);
        }
        else if (nestedType instanceof EventType[])
        {
            EventType[] innerType = (EventType[]) nestedType;
            return innerType[0].getFragmentType(propertyNested);
        }
        else if (nestedType instanceof String)
        {
            String nestedName = nestedType.toString();
            boolean isArray = EventTypeUtility.isPropertyArray(nestedName);
            if (isArray) {
                nestedName = EventTypeUtility.getPropertyRemoveArray(nestedName);
            }
            EventType innerType = eventAdapterService.getExistsTypeByName(nestedName);
            if (!(innerType instanceof BaseNestableEventType))
            {
                return null;
            }
            return innerType.getFragmentType(propertyNested);
        }
        else
        {
            String message = "Nestable map type configuration encountered an unexpected value type of '"
                + nestedType.getClass() + " for property '" + propertyName + "', expected Class, Map.class or Map<String, Object> as value type";
            throw new PropertyAccessException(message);
        }
    }

    /**
     * Returns a message if the type, compared to this type, is not compatible in regards to the property numbers
     * and types.
     * @param otherType to compare to
     * @return message
     */
    public String getEqualsMessage(EventType otherType)
    {
        if (!(otherType instanceof BaseNestableEventType))
        {
            return "Type by name '" + otherType.getName() + "' is not a compatible type (target type underlying is '" + otherType.getUnderlyingType().getSimpleName() + "')";
        }

        BaseNestableEventType other = (BaseNestableEventType) otherType;

        if ((metadata.getTypeClass() != EventTypeMetadata.TypeClass.ANONYMOUS) && (!other.typeName.equals(this.typeName)))
        {
            return "Type by name '" + otherType.getName() + "' is not the same name";
        }

        return isDeepEqualsProperties(otherType.getName(), other.nestableTypes, this.nestableTypes);
    }

    public boolean equalsCompareType(EventType otherEventType) {
        if (this == otherEventType)
        {
            return true;
        }

        String message = getEqualsMessage(otherEventType);
        return message == null;
    }
}
