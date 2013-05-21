/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.PropertyAccessException;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.epl.core.PropertyResolutionDescriptor;
import com.espertech.esper.epl.core.StreamTypeService;
import com.espertech.esper.epl.core.StreamTypesException;
import com.espertech.esper.util.LevenshteinDistance;

public class ExprIdentNodeUtil
{
    public static Pair<PropertyResolutionDescriptor, String> getTypeFromStream(StreamTypeService streamTypeService, String propertyNameNestable, boolean explicitPropertiesOnly, boolean obtainFragment)
                    throws ExprValidationPropertyException {
        String streamOrProp = null;
        String prop = propertyNameNestable;
        if (propertyNameNestable.indexOf('.') != -1) {
            prop = propertyNameNestable.substring(propertyNameNestable.indexOf('.') + 1);
            streamOrProp = propertyNameNestable.substring(0, propertyNameNestable.indexOf('.'));
        }
        if (explicitPropertiesOnly) {
            return getTypeFromStreamExplicitProperties(streamTypeService, prop, streamOrProp, obtainFragment);
        }
        return getTypeFromStream(streamTypeService, prop, streamOrProp, obtainFragment);
    }

    /**
     * Determine stream id and property type given an unresolved property name and
     * a stream name that may also be part of the property name.
     * <p>
     * For example: select s0.p1 from...    p1 is the property name, s0 the stream name, however this could also be a nested property
     * @param streamTypeService - service for type infos
     * @param unresolvedPropertyName - property name
     * @param streamOrPropertyName - stream name, this can also be the first part of the property name
     * @return pair of stream number and property type
     * @throws ExprValidationPropertyException if no such property exists
     */
    protected static Pair<PropertyResolutionDescriptor, String> getTypeFromStream(StreamTypeService streamTypeService, String unresolvedPropertyName, String streamOrPropertyName, boolean obtainFragment)
        throws ExprValidationPropertyException
    {
        PropertyResolutionDescriptor propertyInfo;

        // no stream/property name supplied
        if (streamOrPropertyName == null)
        {
            try
            {
                propertyInfo = streamTypeService.resolveByPropertyName(unresolvedPropertyName, obtainFragment);
            }
            catch (StreamTypesException ex)
            {
                throw getSuggestionException(ex);
            }
            catch (PropertyAccessException ex)
            {
                throw new ExprValidationPropertyException("Failed to find property '" + unresolvedPropertyName + "', the property name does not parse (are you sure?): " + ex.getMessage(), ex);
            }

            // resolves without a stream name, return descriptor and null stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, propertyInfo.getStreamName());
        }

        // try to resolve the property name and stream name as it is (ie. stream name as a stream name)
        StreamTypesException typeExceptionOne;
        try
        {
            propertyInfo = streamTypeService.resolveByStreamAndPropName(streamOrPropertyName, unresolvedPropertyName, obtainFragment);
            // resolves with a stream name, return descriptor and stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, streamOrPropertyName);
        }
        catch (StreamTypesException ex)
        {
            typeExceptionOne = ex;
        }

        // try to resolve the property name to a nested property 's0.p0'
        StreamTypesException typeExceptionTwo;
        String propertyNameCandidate = streamOrPropertyName + '.' + unresolvedPropertyName;
        try
        {
            propertyInfo = streamTypeService.resolveByPropertyName(propertyNameCandidate, obtainFragment);
            // resolves without a stream name, return null for stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, null);
        }
        catch (StreamTypesException ex)
        {
            typeExceptionTwo = ex;
        }

        throw getSuggestionExceptionSecondStep(propertyNameCandidate, typeExceptionOne, typeExceptionTwo);
    }

    /**
     * This method only resolves against explicitly-listed properties (for use with XML or other types that allow any name as a property name).
     * @param streamTypeService stream types
     * @param unresolvedPropertyName property name
     * @param streamOrPropertyName optional stream name
     * @return property info
     * @throws ExprValidationPropertyException if the property could not be resolved
     */
    protected static Pair<PropertyResolutionDescriptor, String> getTypeFromStreamExplicitProperties(StreamTypeService streamTypeService, String unresolvedPropertyName, String streamOrPropertyName, boolean obtainFragment)
        throws ExprValidationPropertyException
    {
        PropertyResolutionDescriptor propertyInfo;

        // no stream/property name supplied
        if (streamOrPropertyName == null)
        {
            try
            {
                propertyInfo = streamTypeService.resolveByPropertyNameExplicitProps(unresolvedPropertyName, obtainFragment);
            }
            catch (StreamTypesException ex)
            {
                throw getSuggestionException(ex);
            }
            catch (PropertyAccessException ex)
            {
                throw new ExprValidationPropertyException(ex.getMessage());
            }

            // resolves without a stream name, return descriptor and null stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, propertyInfo.getStreamName());
        }

        // try to resolve the property name and stream name as it is (ie. stream name as a stream name)
        StreamTypesException typeExceptionOne;
        try
        {
            propertyInfo = streamTypeService.resolveByStreamAndPropNameExplicitProps(streamOrPropertyName, unresolvedPropertyName, obtainFragment);
            // resolves with a stream name, return descriptor and stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, streamOrPropertyName);
        }
        catch (StreamTypesException ex)
        {
            typeExceptionOne = ex;
        }

        // try to resolve the property name to a nested property 's0.p0'
        StreamTypesException typeExceptionTwo;
        String propertyNameCandidate = streamOrPropertyName + '.' + unresolvedPropertyName;
        try
        {
            propertyInfo = streamTypeService.resolveByPropertyNameExplicitProps(propertyNameCandidate, obtainFragment);
            // resolves without a stream name, return null for stream name
            return new Pair<PropertyResolutionDescriptor, String>(propertyInfo, null);
        }
        catch (StreamTypesException ex)
        {
            typeExceptionTwo = ex;
        }

        throw getSuggestionExceptionSecondStep(propertyNameCandidate, typeExceptionOne, typeExceptionTwo);
    }

    private static ExprValidationPropertyException getSuggestionExceptionSecondStep(String propertyNameCandidate, StreamTypesException typeExceptionOne, StreamTypesException typeExceptionTwo) {
        String suggestionOne = getSuggestion(typeExceptionOne);
        String suggestionTwo = getSuggestion(typeExceptionTwo);
        if (suggestionOne != null)
        {
            return new ExprValidationPropertyException(typeExceptionOne.getMessage() + suggestionOne);
        }
        if (suggestionTwo != null)
        {
            return new ExprValidationPropertyException(typeExceptionTwo.getMessage() + suggestionTwo);
        }

        // fail to resolve
        return new ExprValidationPropertyException("Failed to resolve property '" + propertyNameCandidate + "' to a stream or nested property in a stream");
    }

    private static ExprValidationPropertyException getSuggestionException(StreamTypesException ex) {
        String suggestion = getSuggestion(ex);
        if (suggestion != null)
        {
            return new ExprValidationPropertyException(ex.getMessage() + suggestion);
        }
        else
        {
            return new ExprValidationPropertyException(ex.getMessage());
        }
    }

    private static String getSuggestion(StreamTypesException ex)
    {
        if (ex == null)
        {
            return null;
        }
        if (ex.getOptionalSuggestion() == null)
        {
            return null;
        }
        if (ex.getOptionalSuggestion().getFirst() > LevenshteinDistance.ACCEPTABLE_DISTANCE)
        {
            return null;
        }
        return " (did you mean '" + ex.getOptionalSuggestion().getSecond() + "'?)";
    }
}
