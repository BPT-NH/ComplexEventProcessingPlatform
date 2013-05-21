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

import com.espertech.esper.client.dataflow.EPDataFlowOperatorParameterProvider;
import com.espertech.esper.client.dataflow.EPDataFlowOperatorParameterProviderContext;
import com.espertech.esper.dataflow.annotations.DataFlowOpParameter;
import com.espertech.esper.epl.core.EngineImportService;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.event.WriteablePropertyDescriptor;
import com.espertech.esper.event.bean.PropertyHelper;
import com.espertech.esper.event.property.MappedProperty;
import com.espertech.esper.event.property.Property;
import com.espertech.esper.event.property.PropertyParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PopulateUtil {
    private final static String CLASS_PROPERTY_NAME = "class";
    private final static String SYSTEM_PROPETIES_NAME = "systemProperties".toLowerCase();

    private static Log log = LogFactory.getLog(PopulateUtil.class);

    public static Object instantiatePopulateObject(Map<String, Object> objectProperties, Class topClass, EngineImportService engineImportService) throws ExprValidationException {

        Class applicableClass = topClass;
        if (topClass.isInterface()) {
            applicableClass = findInterfaceImplementation(objectProperties, topClass, engineImportService);
        }

        Object top;
        try {
            top = applicableClass.newInstance();
        }
        catch (RuntimeException e) {
            throw new ExprValidationException("Exception instantiating class " + applicableClass.getName() + ": " + e.getMessage(), e);
        }
        catch (InstantiationException e) {
            throw new ExprValidationException(getMessageExceptionInstantiating(applicableClass), e);
        }
        catch (IllegalAccessException e) {
            throw new ExprValidationException("Illegal access to construct class " + applicableClass.getName() + ": " + e.getMessage(), e);
        }

        populateObject(topClass.getSimpleName(), 0, topClass.getSimpleName(), objectProperties, top, engineImportService, null, null);

        return top;
    }

    public static void populateObject(String operatorName, int operatorNum, String dataFlowName, Map<String, Object> objectProperties, Object top, EngineImportService engineImportService, EPDataFlowOperatorParameterProvider optionalParameterProvider, Map<String, Object> optionalParameterURIs)
            throws ExprValidationException
    {
        Class applicableClass = top.getClass();
        Set<WriteablePropertyDescriptor> writables = PropertyHelper.getWritableProperties(applicableClass);
        Set<Field> annotatedFields = JavaClassHelper.findAnnotatedFields(top.getClass(), DataFlowOpParameter.class);
        Set<Method> annotatedMethods = JavaClassHelper.findAnnotatedMethods(top.getClass(), DataFlowOpParameter.class);

        // find catch-all methods
        Set<Method> catchAllMethods = new LinkedHashSet<Method>();
        if (annotatedMethods != null) {
            for (Method method : annotatedMethods) {
                DataFlowOpParameter anno = (DataFlowOpParameter) JavaClassHelper.getAnnotations(DataFlowOpParameter.class, method.getDeclaredAnnotations()).get(0);
                if (anno.all()) {
                    if (method.getParameterTypes().length == 2 && method.getParameterTypes()[0] == String.class && method.getParameterTypes()[1] == Object.class) {
                        catchAllMethods.add(method);
                        continue;
                    }
                    throw new ExprValidationException("Invalid annotation for catch-call");
                }
            }
        }

        // map provided values
        for (Map.Entry<String, Object> property : objectProperties.entrySet()) {
            boolean found = false;
            String propertyName = property.getKey();

            // invoke catch-all setters
            for (Method method : catchAllMethods) {
                try {
                    method.invoke(top, new Object[] {propertyName, property.getValue()});
                }
                catch (IllegalAccessException e) {
                    throw new ExprValidationException("Illegal access invoking method for property '" + propertyName + "' for class " + applicableClass.getName() + " method " + method.getName(), e);
                }
                catch (InvocationTargetException e) {
                    throw new ExprValidationException("Exception invoking method for property '" + propertyName + "' for class " + applicableClass.getName() + " method " + method.getName() + ": " + e.getTargetException().getMessage(), e);
                }
                found = true;
            }

            if (propertyName.toLowerCase().equals(CLASS_PROPERTY_NAME)) {
                continue;
            }

            // use the writeable property descriptor (appropriate setter method) from writing the property
            WriteablePropertyDescriptor descriptor = findDescriptor(applicableClass, propertyName, writables);
            if (descriptor != null) {
                Object coerceProperty = coerceProperty(propertyName, applicableClass, property.getValue(), descriptor.getType(), engineImportService, false);

                try {
                    descriptor.getWriteMethod().invoke(top, new Object[] {coerceProperty});
                }
                catch (IllegalArgumentException e) {
                    throw new ExprValidationException("Illegal argument invoking setter method for property '" + propertyName + "' for class " + applicableClass.getName() + " method " + descriptor.getWriteMethod().getName() + " provided value " + coerceProperty, e);
                }
                catch (IllegalAccessException e) {
                    throw new ExprValidationException("Illegal access invoking setter method for property '" + propertyName + "' for class " + applicableClass.getName() + " method " + descriptor.getWriteMethod().getName(), e);
                }
                catch (InvocationTargetException e) {
                    throw new ExprValidationException("Exception invoking setter method for property '" + propertyName + "' for class " + applicableClass.getName() + " method " + descriptor.getWriteMethod().getName() + ": " + e.getTargetException().getMessage(), e);
                }
                continue;
            }

            // find the field annotated with {@link @GraphOpProperty}
            for (Field annotatedField : annotatedFields) {
                DataFlowOpParameter anno = (DataFlowOpParameter) JavaClassHelper.getAnnotations(DataFlowOpParameter.class, annotatedField.getDeclaredAnnotations()).get(0);
                if (anno.name().equals(propertyName) || annotatedField.getName().equals(propertyName)) {
                    Object coerceProperty = coerceProperty(propertyName, applicableClass, property.getValue(), annotatedField.getType(), engineImportService, true);
                    try {
                        annotatedField.setAccessible(true);
                        annotatedField.set(top, coerceProperty);
                    }
                    catch (Exception e) {
                        throw new ExprValidationException("Failed to set field '" + annotatedField.getName() + "': " + e.getMessage(), e);
                    }
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }

            throw new ExprValidationException("Failed to find writable property '" + propertyName + "' for class " + applicableClass.getName());
        }

        // second pass: if a parameter URI - value pairs were provided, check that
        if (optionalParameterURIs != null) {
            for (Field annotatedField : annotatedFields) {
                try {
                    annotatedField.setAccessible(true);
                    String uri = operatorName + "/" + annotatedField.getName();
                    if (optionalParameterURIs.containsKey(uri)) {
                        Object value = optionalParameterURIs.get(uri);
                        annotatedField.set(top, value);
                        if (log.isDebugEnabled()) {
                            log.debug("Found parameter '" + uri + "' for data flow " + dataFlowName + " setting " + value);
                        }
                    }
                    else {
                        if (log.isDebugEnabled()) {
                            log.debug("Not found parameter '" + uri + "' for data flow " + dataFlowName);
                        }
                    }
                }
                catch (Exception e) {
                    throw new ExprValidationException("Failed to set field '" + annotatedField.getName() + "': " + e.getMessage(), e);
                }
            }
        }

        // third pass: if a parameter provider is provided, use that
        if (optionalParameterProvider != null) {
            for (Field annotatedField : annotatedFields) {
                try {
                    annotatedField.setAccessible(true);
                    Object provided = annotatedField.get(top);
                    Object value = optionalParameterProvider.provide(new EPDataFlowOperatorParameterProviderContext(operatorName, annotatedField.getName(), top, operatorNum, provided, dataFlowName));
                    if (value != null) {
                        annotatedField.set(top, value);
                    }
                }
                catch (Exception e) {
                    throw new ExprValidationException("Failed to set field '" + annotatedField.getName() + "': " + e.getMessage(), e);
                }
            }
        }
    }

    private static Class findInterfaceImplementation(Map<String, Object> properties, Class topClass, EngineImportService engineImportService) throws ExprValidationException {
        String message = "Failed to find implementation for interface " + topClass.getName();

        // Allow to populate the special "class" field
        if (!properties.containsKey(CLASS_PROPERTY_NAME)) {
            throw new ExprValidationException(message + ", for interfaces please specified the '" + CLASS_PROPERTY_NAME + "' field that provides the class name either as a simple class name or fully qualified");
        }

        Class clazz = null;
        String className = (String) properties.get(CLASS_PROPERTY_NAME);
        try {
            clazz = JavaClassHelper.getClassForName(className);
        }
        catch (ClassNotFoundException e) {

            if (!className.contains(".")) {
                className = topClass.getPackage().getName() + "." + className;
                try {
                    clazz = JavaClassHelper.getClassForName(className);
                }
                catch (ClassNotFoundException ex) {
                }
            }

            if (clazz == null) {
                throw new ExprValidationPropertyException(message + ", could not find class by name '" + className + "'");
            }
        }

        if (!JavaClassHelper.isSubclassOrImplementsInterface(clazz, topClass)) {
            throw new ExprValidationException(message + ", class " + JavaClassHelper.getClassNameFullyQualPretty(clazz) + " does not implement the interface");
        }
        return clazz;
    }

    private static Object coerceProperty(String propertyName, Class containingType, Object value, Class type, EngineImportService engineImportService, boolean forceNumeric) throws ExprValidationException {
        if (value instanceof ExprNode && type != ExprNode.class) {
            if (value instanceof ExprIdentNode) {
                ExprIdentNode identNode = (ExprIdentNode) value;
                Property prop;
                try {
                    prop = PropertyParser.parse(identNode.getFullUnresolvedName(), false);
                }
                catch (Exception ex) {
                    throw new ExprValidationException("Failed to parse property '" + identNode.getFullUnresolvedName() + "'");
                }
                if (!(prop instanceof MappedProperty)) {
                    throw new ExprValidationException("Unrecognized property '" + identNode.getFullUnresolvedName() + "'");
                }
                MappedProperty mappedProperty = (MappedProperty) prop;
                if (mappedProperty.getPropertyNameAtomic().toLowerCase().equals(SYSTEM_PROPETIES_NAME)) {
                    return System.getProperty(mappedProperty.getKey());
                }
            }
            else {
                ExprNode exprNode = (ExprNode) value;
                ExprEvaluator evaluator = exprNode.getExprEvaluator();
                if (evaluator == null) {
                    throw new ExprValidationException("Failed to evaluate expression '" + exprNode.toExpressionString() + "'");
                }
                value = evaluator.evaluate(null, true, null);
            }
        }

        if (value == null) {
            return null;
        }
        if (value.getClass() == type) {
            return value;
        }
        if (JavaClassHelper.isAssignmentCompatible(value.getClass(), type)) {
            if (forceNumeric && JavaClassHelper.getBoxedType(value.getClass()) != JavaClassHelper.getBoxedType(type) && JavaClassHelper.isNumeric(type) && JavaClassHelper.isNumeric(value.getClass())) {
                value = JavaClassHelper.coerceBoxed((Number) value, JavaClassHelper.getBoxedType(type));
            }
            return value;
        }
        if (JavaClassHelper.isSubclassOrImplementsInterface(value.getClass(), type)) {
            return value;
        }
        if (type.isArray()) {
            if (!(value instanceof Collection)) {
                throw new ExprValidationException("Property '" + propertyName + "' of class " + JavaClassHelper.getClassNameFullyQualPretty(containingType) + " expects an array but receives a value of type " + value.getClass().getName());
            }
            Object[] items = ((Collection) value).toArray();
            Object coercedArray = Array.newInstance(type.getComponentType(), items.length);
            for (int i = 0; i < items.length; i++) {
                Object coercedValue = coerceProperty(propertyName + " (array element)", type, items[i], type.getComponentType(), engineImportService, false);
                Array.set(coercedArray, i, coercedValue);
            }
            return coercedArray;
        }
        if (!(value instanceof Map)) {
            throw new ExprValidationException("Property '" + propertyName + "' of class " + JavaClassHelper.getClassNameFullyQualPretty(containingType) + " expects an " + JavaClassHelper.getClassNameFullyQualPretty(type) + " but receives a value of type " + value.getClass().getName());
        }
        Map<String, Object> props = (Map<String, Object>) value;
        return instantiatePopulateObject(props, type, engineImportService);
    }

    private static WriteablePropertyDescriptor findDescriptor(Class clazz, String propertyName, Set<WriteablePropertyDescriptor> writables)
        throws ExprValidationException {
        for (WriteablePropertyDescriptor desc : writables) {
            if (desc.getPropertyName().toLowerCase().equals(propertyName.toLowerCase())) {
                return desc;
            }
        }
        return null;
    }

    private static String getMessageExceptionInstantiating(Class clazz) {
        return "Exception instantiating class " + clazz.getName() + ", please make sure the class has a public no-arg constructor (and for inner classes is declared static)";
    }
}
