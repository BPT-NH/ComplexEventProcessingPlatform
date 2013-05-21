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

package com.espertech.esper.epl.enummethod.eval;

import com.espertech.esper.client.EventType;
import com.espertech.esper.client.util.ExpressionReturnType;
import com.espertech.esper.epl.core.MethodResolutionService;
import com.espertech.esper.epl.core.StreamTypeService;
import com.espertech.esper.epl.enummethod.dot.*;
import com.espertech.esper.epl.expression.ExprDotNodeUtility;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.arr.ObjectArrayEventType;

import java.util.List;

public class ExprDotEvalFirstLastOf extends ExprDotEvalEnumMethodBase {

    public EventType[] getAddStreamTypes(String enumMethodUsedName, List<String> goesToNames, EventType inputEventType, Class collectionComponentType, List<ExprDotEvalParam> bodiesAndParameters) {
        return ExprDotNodeUtility.getSingleLambdaParamEventType(enumMethodUsedName, goesToNames, inputEventType, collectionComponentType);
    }

    public EnumEval getEnumEval(MethodResolutionService methodResolutionService, EventAdapterService eventAdapterService, StreamTypeService streamTypeService, String statementId, String enumMethodUsedName, List<ExprDotEvalParam> bodiesAndParameters, EventType inputEventType, Class collectionComponentType, int numStreamsIncoming) {

        if (bodiesAndParameters.isEmpty()) {
            if (inputEventType != null) {
                super.setTypeInfo(ExpressionReturnType.singleEvent(inputEventType));
            }
            else {
                super.setTypeInfo(ExpressionReturnType.singleValue(collectionComponentType));
            }
            if (this.getEnumMethodEnum() == EnumMethodEnum.FIRST) {
                return new EnumEvalFirstOfNoPredicate(numStreamsIncoming);
            }
            else {
                return new EnumEvalLastOfNoPredicate(numStreamsIncoming);
            }
        }

        ExprDotEvalParamLambda first = (ExprDotEvalParamLambda) bodiesAndParameters.get(0);
        if (inputEventType != null) {
            super.setTypeInfo(ExpressionReturnType.singleEvent(inputEventType));
            if (this.getEnumMethodEnum() == EnumMethodEnum.FIRST) {
                return new EnumEvalFirstOfPredicateEvents(first.getBodyEvaluator(), first.getStreamCountIncoming());
            }
            else {
                return new EnumEvalLastOfPredicateEvents(first.getBodyEvaluator(), first.getStreamCountIncoming());
            }
        }
        super.setTypeInfo(ExpressionReturnType.singleValue(collectionComponentType));
        if (this.getEnumMethodEnum() == EnumMethodEnum.FIRST) {
            return new EnumEvalFirstOfPredicateScalar(first.getBodyEvaluator(), first.getStreamCountIncoming(), (ObjectArrayEventType) first.getGoesToTypes()[0]);
        }
        else {
            return new EnumEvalLastOfPredicateScalar(first.getBodyEvaluator(), first.getStreamCountIncoming(), (ObjectArrayEventType) first.getGoesToTypes()[0]);
        }

    }
}
