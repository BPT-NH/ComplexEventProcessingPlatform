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

package com.espertech.esper.client.util;

import com.espertech.esper.client.EventType;
import com.espertech.esper.util.JavaClassHelper;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Carries return type information related to the return values returned by expressions.
 * <p>
 *     Use factory methods to initialize return type information according to the return values
 *     that your expression is going to provide.
 * </p>
 * <p>
 *  <ol>
 *      <li>
 *          Use {@link ExpressionReturnType#collectionOfEvents(com.espertech.esper.client.EventType)}
 *          to indicate that the expression returns a collection of events.
 *      </li>
 *      <li>
 *          Use {@link ExpressionReturnType#singleEvent(com.espertech.esper.client.EventType)}
 *          to indicate that the expression returns a single event.
 *      </li>
 *      <li>
 *          Use {@link ExpressionReturnType#collectionOfSingleValue(Class)}
 *          to indicate that the expression returns a collection of single values.
 *          A single value can be any object including null.
 *      </li>
 *      <li>
 *          Use {@link ExpressionReturnType#array(Class)}
 *          to indicate that the expression returns an array of single values.
 *          A single value can be any object including null.
 *      </li>
 *      <li>
 *          Use {@link ExpressionReturnType#singleValue(Class)}
 *          to indicate that the expression returns a single value.
 *          A single value can be any object including null.
 *          Such expression results cannot be used as input to enumeration methods, for example.
 *      </li>
 *  </ol>
 * </p>.
 */
public class ExpressionReturnType {

    private final Class singleValueType;
    private final Class componentType;
    private final EventType singleEventEventType;
    private final EventType collOfEventEventType;

    private ExpressionReturnType(Class singleValueType, Class componentType, EventType singleEventEventType, EventType collOfEventEventType) {
        this.singleValueType = singleValueType;
        this.componentType = componentType;
        this.singleEventEventType = singleEventEventType;
        this.collOfEventEventType = collOfEventEventType;
    }

    /**
     * Indicate that the expression return type is an array of a given component type.
     * @param arrayComponentType array component type
     * @return array of single value expression result type
     */
    public static ExpressionReturnType array(Class arrayComponentType) {
        if (arrayComponentType == null) {
            throw new IllegalArgumentException("Invalid null array component type");
        }
        return new ExpressionReturnType(JavaClassHelper.getArrayType(arrayComponentType), arrayComponentType, null, null);
    }

    /**
     * Indicate that the expression return type is a single (non-enumerable) value of the given type.
     * The expression can still return an array or collection or events however
     * since the engine would not know the type of such objects and may not use runtime reflection
     * it may not allow certain operations on expression results.
     * @param singleValueType type of single value returned, or null to indicate that the expression always returns null
     * @return single-value expression result type
     */
    public static ExpressionReturnType singleValue(Class singleValueType) {
        // null value allowed
        return new ExpressionReturnType(singleValueType, null, null, null);
    }

    /**
     * Indicate that the expression return type is a collection of a given component type.
     * @param collectionComponentType collection component type
     * @return collection of single value expression result type
     */
    public static ExpressionReturnType collectionOfSingleValue(Class collectionComponentType) {
        if (collectionComponentType == null) {
            throw new IllegalArgumentException("Invalid null collection component type");
        }
        return new ExpressionReturnType(Collection.class, collectionComponentType, null, null);
    }

    /**
     * Indicate that the expression return type is a collection of a given type of events.
     * @param eventTypeOfCollectionEvents the event type of the events that are part of the collection
     * @return collection of events expression result type
     */
    public static ExpressionReturnType collectionOfEvents(EventType eventTypeOfCollectionEvents) {
        if (eventTypeOfCollectionEvents == null) {
            throw new IllegalArgumentException("Invalid null event type");
        }
        return new ExpressionReturnType(JavaClassHelper.getArrayType(eventTypeOfCollectionEvents.getUnderlyingType()), null, null, eventTypeOfCollectionEvents);
    }

    /**
     * Indicate that the expression return type is single event of a given event type.
     * @param eventTypeOfSingleEvent the event type of the event returned
     * @return single-event expression result type
     */
    public static ExpressionReturnType singleEvent(EventType eventTypeOfSingleEvent) {
        if (eventTypeOfSingleEvent == null) {
            throw new IllegalArgumentException("Invalid null event type");
        }
        return new ExpressionReturnType(eventTypeOfSingleEvent.getUnderlyingType(), null, eventTypeOfSingleEvent, null);
    }

    /**
     * Returns the type of the single-value returned by an expression.
     * <p>
     *     For expressions that return a collection of single values, this method returns <code>Collection.class</code>.
     * </p>
     * <p>
     *     For expressions that return an array of single values, this method returns the array type of the component type.
     * </p>
     * <p>
     *     For expressions that return a single event, returns the underlying type of the event type.
     * </p>
     * <p>
     *     For expressions that return a collection of events, return array type of the underlying type of the event type.
     * </p>
     * <p>
     *     Can return null for expressions that always return null.
     * </p>
     * @return expression's single-value type or null if not applicable
     */
    public Class getSingleValueType() {
        return singleValueType;
    }

    /**
     * Returns the type of the component-type returned by an expression, if there is a component type.
     * <p>
     *     Returns null for single-event or collection-of-events return type.
     * </p>
     * <p>
     *     Returns null for single-value types that are not declared explicitly as an array or collection.
     * </p>
     * <p>
     *     For array and collection returns the component type of the collection.
     * </p>
     * @return expression's single-value type or null if not applicable
     */
    public Class getComponentType() {
        return componentType;
    }

    /**
     * Returns the event type of the event returned by the expression, or null if the expression does not
     * return a single event.
     * @return expression's single-event event type or null if not applicable
     */
    public EventType getSingleEventEventType() {
        return singleEventEventType;
    }

    /**
     * Returns the event type of the collection of events returned by the expression,
     * or null if the expression does not return a collection of events.
     * @return expression's single-event event type or null if not applicable
     */
    public EventType getCollOfEventEventType() {
        return collOfEventEventType;
    }

    /**
     * Returns an indicator whether the expression returns a non-null single-value.
     * If the expression returns a single event or a collection of events the method returns false.
     * If the expression returns a single value or collection of single values or array of single values the method returns true.
     * @return flag indicating expression returns a single value including array or collection
     */
    public boolean isSingleValueNonNull() {
        return collOfEventEventType == null && singleEventEventType == null && singleValueType != null;
    }

    /**
     * Interrogate the provided method and determine whether it returns
     * single-value, array of single-value or collection of single-value and
     * their component type.
     * @param method the class methods
     * @return expression return type
     */
    public static ExpressionReturnType fromMethod(Method method) {
        Class returnType = method.getReturnType();
        if (JavaClassHelper.isImplementsInterface(returnType, Collection.class)) {
            Class componentType = JavaClassHelper.getGenericReturnType(method, true);
            return ExpressionReturnType.collectionOfSingleValue(componentType);
        }
        return ExpressionReturnType.singleValue(method.getReturnType());
    }

    /**
     * Returns a nice text detailing the expression result type.
     * @return descriptive text
     */
    public String toTypeDescriptive() {
        if (singleEventEventType != null) {
            return "event type '" + singleEventEventType.getName() + "'";
        }
        else if (collOfEventEventType != null) {
            return "collection of events of type '" + collOfEventEventType.getName() + "'";
        }
        else if (componentType != null && singleValueType == Collection.class) {
            return "collection of " + componentType.getSimpleName();
        }
        else if (componentType != null) {
            return "array of " + JavaClassHelper.getClassNameFullyQualPretty(singleValueType);
        }
        else if (singleValueType != null) {
            return "class " + JavaClassHelper.getClassNameFullyQualPretty(singleValueType);
        }
        else {
            return "an incompatible type";
        }
    }
}
