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

import com.espertech.esper.client.EventPropertyGetter;
import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.MultiKeyUntyped;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.named.NamedWindowProcessor;
import com.espertech.esper.epl.named.NamedWindowService;
import com.espertech.esper.epl.spec.ContextDetailPartitionItem;
import com.espertech.esper.epl.spec.ContextDetailPartitioned;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import com.espertech.esper.epl.spec.util.StatementSpecCompiledAnalyzerResult;
import com.espertech.esper.event.EventTypeUtility;
import com.espertech.esper.filter.*;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.util.JavaClassHelper;

import java.util.*;

public class ContextControllerPartitionedUtil {

    protected static Class[] validateContextDesc(String contextName, ContextDetailPartitioned segmentedSpec) throws ExprValidationException {

        if (segmentedSpec.getItems().isEmpty()) {
            throw new ExprValidationException("Empty list of partition items");
        }

        // verify properties exist
        for (ContextDetailPartitionItem item : segmentedSpec.getItems()) {
            EventType type = item.getFilterSpecCompiled().getFilterForEventType();
            for (String property : item.getPropertyNames()) {
                EventPropertyGetter getter = type.getGetter(property);
                if (getter == null) {
                    throw new ExprValidationException("For context '" + contextName + "' property name '" + property + "' not found on type " + type.getName());
                }
            }
        }

        // verify property number and types compatible
        ContextDetailPartitionItem firstItem = segmentedSpec.getItems().get(0);
        if (segmentedSpec.getItems().size() > 1) {
        // verify the same filter event type is only listed once

            for (int i = 0; i < segmentedSpec.getItems().size(); i++) {
                EventType compareTo = segmentedSpec.getItems().get(i).getFilterSpecCompiled().getFilterForEventType();

                for (int j = 0; j < segmentedSpec.getItems().size(); j++) {
                    if (i == j) {
                        continue;
                    }

                    EventType compareFrom = segmentedSpec.getItems().get(j).getFilterSpecCompiled().getFilterForEventType();
                    if (compareFrom == compareTo) {
                        throw new ExprValidationException("For context '" + contextName + "' the event type '" + compareFrom.getName() + "' is listed twice");
                    }
                    if (EventTypeUtility.isTypeOrSubTypeOf(compareFrom, compareTo) || EventTypeUtility.isTypeOrSubTypeOf(compareTo, compareFrom)) {
                        throw new ExprValidationException("For context '" + contextName + "' the event type '" + compareFrom.getName() + "' is listed twice: Event type '" +
                                compareFrom.getName() + "' is a subtype or supertype of event type '" + compareTo.getName() + "'");
                    }

                }
            }

            // build property type information
            String[] names = new String[firstItem.getPropertyNames().size()];
            Class[] types = new Class[firstItem.getPropertyNames().size()];
            Class[] typesBoxed = new Class[firstItem.getPropertyNames().size()];
            for (int i = 0; i < firstItem.getPropertyNames().size(); i++) {
                String property = firstItem.getPropertyNames().get(i);
                names[i] = property;
                types[i] = firstItem.getFilterSpecCompiled().getFilterForEventType().getPropertyType(property);
                typesBoxed[i] = JavaClassHelper.getBoxedType(types[i]);
            }

            // compare property types and numbers
            for (int item = 1; item < segmentedSpec.getItems().size(); item++) {
                ContextDetailPartitionItem nextItem = segmentedSpec.getItems().get(item);

                // compare number of properties
                if (nextItem.getPropertyNames().size() != types.length) {
                    throw new ExprValidationException("For context '" + contextName + "' expected the same number of property names for each event type, found " +
                            types.length + " properties for event type '" + firstItem.getFilterSpecCompiled().getFilterForEventType().getName() +
                            "' and " + nextItem.getPropertyNames().size() + " properties for event type '" + nextItem.getFilterSpecCompiled().getFilterForEventType().getName() + "'");
                }

                // compare property types
                for (int i = 0; i < nextItem.getPropertyNames().size(); i++) {
                    String property = nextItem.getPropertyNames().get(i);
                    Class type = JavaClassHelper.getBoxedType(nextItem.getFilterSpecCompiled().getFilterForEventType().getPropertyType(property));
                    Class typeBoxed = JavaClassHelper.getBoxedType(type);
                    boolean left = JavaClassHelper.isSubclassOrImplementsInterface(typeBoxed, typesBoxed[i]);
                    boolean right = JavaClassHelper.isSubclassOrImplementsInterface(typesBoxed[i], typeBoxed);
                    if (typeBoxed != typesBoxed[i] && !left && !right) {
                        throw new ExprValidationException("For context '" + contextName + "' for context '" + contextName + "' found mismatch of property types, property '" + names[i] +
                                "' of type '" + JavaClassHelper.getClassNameFullyQualPretty(types[i]) +
                                "' compared to property '" + property +
                                "' of type '" + JavaClassHelper.getClassNameFullyQualPretty(typeBoxed) + "'");
                    }
                }
            }
        }

        Class[] propertyTypes = new Class[firstItem.getPropertyNames().size()];
        for (int i = 0; i < firstItem.getPropertyNames().size(); i++) {
            String property = firstItem.getPropertyNames().get(i);
            propertyTypes[i] = firstItem.getFilterSpecCompiled().getFilterForEventType().getPropertyType(property);
        }
        return propertyTypes;
    }

    protected static void validateStatementForContext(String contextName, ContextControllerStatementBase statement, StatementSpecCompiledAnalyzerResult streamAnalysis, Collection<EventType> itemEventTypes, NamedWindowService namedWindowService)
        throws ExprValidationException
    {
        List<FilterSpecCompiled> filters = streamAnalysis.getFilters();

        boolean isCreateWindow = statement.getStatementSpec().getCreateWindowDesc() != null;

        // if no create-window: at least one of the filters must match one of the filters specified by the context
        if (!isCreateWindow) {
            for (FilterSpecCompiled filter : filters) {
                for (EventType itemEventType : itemEventTypes) {
                    EventType stmtFilterType = filter.getFilterForEventType();
                    if (stmtFilterType == itemEventType) {
                        return;
                    }
                    if (EventTypeUtility.isTypeOrSubTypeOf(stmtFilterType, itemEventType)) {
                        return;
                    }

                    NamedWindowProcessor processor = namedWindowService.getProcessor(stmtFilterType.getName());
                    if (processor != null && processor.getContextName() != null && processor.getContextName().equals(contextName)) {
                        return;
                    }
                }
            }

            if (!filters.isEmpty()) {
                throw new ExprValidationException(getTypeValidationMessage(contextName, filters.get(0).getFilterForEventType().getName()));
            }
            return;
        }

        // validate create-window
        String declaredAsName = statement.getStatementSpec().getCreateWindowDesc().getAsEventTypeName();
        if (declaredAsName != null) {
            for (EventType itemEventType : itemEventTypes) {
                if (itemEventType.getName().equals(declaredAsName)) {
                    return;
                }
            }

            throw new ExprValidationException(getTypeValidationMessage(contextName, declaredAsName));
        }
    }

    // Compare filters in statement with filters in segmented context, addendum filter compilation
    public static void populateAddendumFilters(Object keyValue, List<FilterSpecCompiled> filtersSpecs, ContextDetailPartitioned segmentedSpec, StatementSpecCompiled optionalStatementSpecCompiled, IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> addendums) {

        // determine whether create-named-window
        boolean isCreateWindow = optionalStatementSpecCompiled != null && optionalStatementSpecCompiled.getCreateWindowDesc() != null;
        if (!isCreateWindow) {
            for (FilterSpecCompiled filtersSpec : filtersSpecs) {

                ContextDetailPartitionItem foundPartition = null;
                for (ContextDetailPartitionItem partitionItem : segmentedSpec.getItems()) {
                    boolean typeOrSubtype = EventTypeUtility.isTypeOrSubTypeOf(filtersSpec.getFilterForEventType(), partitionItem.getFilterSpecCompiled().getFilterForEventType());
                    if (typeOrSubtype) {
                        foundPartition = partitionItem;
                    }
                }

                if (foundPartition == null) {
                    continue;
                }

                List<FilterValueSetParam> addendumFilters = new ArrayList<FilterValueSetParam>(foundPartition.getPropertyNames().size());
                if (foundPartition.getPropertyNames().size() == 1) {
                    String propertyName = foundPartition.getPropertyNames().get(0);
                    EventPropertyGetter getter = foundPartition.getFilterSpecCompiled().getFilterForEventType().getGetter(propertyName);
                    Class resultType = foundPartition.getFilterSpecCompiled().getFilterForEventType().getPropertyType(propertyName);
                    FilterSpecLookupable lookupable = new FilterSpecLookupable(propertyName, getter, resultType);
                    FilterValueSetParam filter = new FilterValueSetParamImpl(lookupable, FilterOperator.EQUAL, keyValue);
                    addendumFilters.add(filter);
                }
                else {
                    Object[] keys = ((MultiKeyUntyped) keyValue).getKeys();
                    for (int i = 0; i < foundPartition.getPropertyNames().size(); i++) {
                        String partitionPropertyName = foundPartition.getPropertyNames().get(i);
                        EventPropertyGetter getter = foundPartition.getFilterSpecCompiled().getFilterForEventType().getGetter(partitionPropertyName);
                        Class resultType = foundPartition.getFilterSpecCompiled().getFilterForEventType().getPropertyType(partitionPropertyName);
                        FilterSpecLookupable lookupable = new FilterSpecLookupable(partitionPropertyName, getter, resultType);
                        FilterValueSetParam filter = new FilterValueSetParamImpl(lookupable, FilterOperator.EQUAL, keys[i]);
                        addendumFilters.add(filter);
                    }
                }

                // add those predefined filter parameters, if any
                addendumFilters.addAll(Arrays.asList(foundPartition.getParametersCompiled()));

                // add to existing if any are present
                addAddendums(addendums, addendumFilters, filtersSpec);
            }
        }
        // handle segmented context for create-window
        else {
            String declaredAsName = optionalStatementSpecCompiled.getCreateWindowDesc().getAsEventTypeName();
            if (declaredAsName != null) {
                for (FilterSpecCompiled filtersSpec : filtersSpecs) {

                    ContextDetailPartitionItem foundPartition = null;
                    for (ContextDetailPartitionItem partitionItem : segmentedSpec.getItems()) {
                        if (partitionItem.getFilterSpecCompiled().getFilterForEventType().getName().equals(declaredAsName)) {
                            foundPartition = partitionItem;
                            break;
                        }
                    }

                    if (foundPartition == null) {
                        continue;
                    }

                    List<FilterValueSetParam> addendumFilters = new ArrayList<FilterValueSetParam>(foundPartition.getPropertyNames().size());
                    int propertyNumber = 0;
                    for (String partitionPropertyName : foundPartition.getPropertyNames()) {
                        EventPropertyGetter getter = foundPartition.getFilterSpecCompiled().getFilterForEventType().getGetter(partitionPropertyName);
                        Class resultType = foundPartition.getFilterSpecCompiled().getFilterForEventType().getPropertyType(partitionPropertyName);
                        FilterSpecLookupable lookupable = new FilterSpecLookupable(partitionPropertyName, getter, resultType);

                        Object propertyValue;
                        if (keyValue instanceof MultiKeyUntyped) {
                            propertyValue = ((MultiKeyUntyped) keyValue).get(propertyNumber);
                        }
                        else {
                            propertyValue = keyValue;
                        }

                        FilterValueSetParam filter = new FilterValueSetParamImpl(lookupable, FilterOperator.EQUAL, propertyValue);
                        addendumFilters.add(filter);
                        propertyNumber++;
                    }

                    // add to existing if any are present
                    addAddendums(addendums, addendumFilters, filtersSpec);
                }
            }
        }
    }

    private static void addAddendums(IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> addendums, List<FilterValueSetParam> addendumFilters, FilterSpecCompiled filtersSpec) {
        FilterValueSetParam[] existing = addendums.get(filtersSpec);
        if (existing == null) {
            addendums.put(filtersSpec, addendumFilters.toArray(new FilterValueSetParam[addendumFilters.size()]));
            return;
        }
        existing = (FilterValueSetParam[]) CollectionUtil.arrayExpandAddElements(existing, addendumFilters);
        addendums.put(filtersSpec, existing);
    }

    private static String getTypeValidationMessage(String contextName, String typeNameEx) {
        return "Segmented context '" + contextName + "' requires that any of the event types that are listed in the segmented context also appear in any of the filter expressions of the statement, type '" + typeNameEx + "' is not one of the types listed";
    }
}
