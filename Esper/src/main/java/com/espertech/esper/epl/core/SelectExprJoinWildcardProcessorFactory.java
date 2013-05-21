/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.core;

import com.espertech.esper.client.ConfigurationInformation;
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.CreateSchemaDesc;
import com.espertech.esper.epl.spec.InsertIntoDesc;
import com.espertech.esper.event.EventAdapterException;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.arr.ObjectArrayEventType;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.util.EventRepresentationUtil;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SelectExprJoinWildcardProcessorFactory
{
    /**
     * Ctor.
     * @param streamNames - name of each stream
     * @param streamTypes - type of each stream
     * @param eventAdapterService - service for generating events and handling event types
     * @param insertIntoDesc - describes the insert-into clause
     * @param selectExprEventTypeRegistry - registry for event type to statements
     * @param methodResolutionService - for resolving writable properties
     * @throws com.espertech.esper.epl.expression.ExprValidationException if the expression validation failed
     */
    public static SelectExprProcessor create(Collection<Integer> assignedTypeNumberStack,
                                                  String statementId,
                                                  String[] streamNames,
                                                  EventType[] streamTypes,
                                                  EventAdapterService eventAdapterService,
                                                  InsertIntoDesc insertIntoDesc,
                                                  SelectExprEventTypeRegistry selectExprEventTypeRegistry,
                                                  MethodResolutionService methodResolutionService,
                                                  Annotation[] annotations,
                                                  ConfigurationInformation configuration) throws ExprValidationException
    {
        if ((streamNames.length < 2) || (streamTypes.length < 2) || (streamNames.length != streamTypes.length))
        {
            throw new IllegalArgumentException("Stream names and types parameter length is invalid, expected use of this class is for join statements");
        }

        // Create EventType of result join events
        Map<String, Object> eventTypeMap = new LinkedHashMap<String, Object>();
        for (int i = 0; i < streamTypes.length; i++)
        {
            eventTypeMap.put(streamNames[i], streamTypes[i]);
        }

        // If we have an name for this type, add it
        boolean useMap = EventRepresentationUtil.isMap(annotations, configuration, CreateSchemaDesc.AssignedType.NONE);
        EventType resultEventType;

        if (insertIntoDesc != null)
        {
            EventType existingType = eventAdapterService.getExistsTypeByName(insertIntoDesc.getEventTypeName());

            SelectExprProcessor processor = null;
            if (existingType != null) {
                processor = SelectExprInsertEventBeanFactory.getInsertUnderlyingJoinWildcard(eventAdapterService, existingType, streamNames, streamTypes, methodResolutionService.getEngineImportService());
            }

            if (processor != null) {
                return processor;
            }
            else
            {
                try
                {
                    if (useMap) {
                        resultEventType = eventAdapterService.addNestableMapType(insertIntoDesc.getEventTypeName(), eventTypeMap, null, false, false, false, false, true);
                    }
                    else {
                        resultEventType = eventAdapterService.addNestableObjectArrayType(insertIntoDesc.getEventTypeName(), eventTypeMap, null, false, false, false, false, true);
                    }
                    selectExprEventTypeRegistry.add(resultEventType);
                }
                catch (EventAdapterException ex)
                {
                    throw new ExprValidationException(ex.getMessage());
                }
            }
        }
        else
        {
            if (useMap) {
                resultEventType = eventAdapterService.createAnonymousMapType(statementId + "_join_" + CollectionUtil.toString(assignedTypeNumberStack, "_"), eventTypeMap);
            }
            else {
                resultEventType = eventAdapterService.createAnonymousObjectArrayType(statementId + "_join_" + CollectionUtil.toString(assignedTypeNumberStack, "_"), eventTypeMap);
            }
        }

        if (resultEventType instanceof ObjectArrayEventType) {
            return new SelectExprJoinWildcardProcessorObjectArray(streamNames, resultEventType, eventAdapterService);
        }
        return new SelectExprJoinWildcardProcessorMap(streamNames, resultEventType, eventAdapterService);
    }
}
