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

package com.espertech.esper.epl.core.eval;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.epl.core.SelectExprProcessor;
import com.espertech.esper.event.vaevent.ValueAddEventProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;

public class EvalInsertNoWildcardSingleColCoercionBeanWrapVariant extends EvalBaseFirstProp implements SelectExprProcessor {

    private static final Log log = LogFactory.getLog(EvalInsertNoWildcardSingleColCoercionBeanWrapVariant.class);

    private final ValueAddEventProcessor vaeProcessor;

    public EvalInsertNoWildcardSingleColCoercionBeanWrapVariant(SelectExprContext selectExprContext, EventType resultEventType, ValueAddEventProcessor vaeProcessor) {
        super(selectExprContext, resultEventType);
        this.vaeProcessor = vaeProcessor;
    }

    public EventBean processFirstCol(Object result) {
        EventBean wrappedEvent = super.getEventAdapterService().adapterForBean(result);
        EventBean variant = vaeProcessor.getValueAddEventBean(wrappedEvent);
        return super.getEventAdapterService().adapterForTypedWrapper(variant, Collections.EMPTY_MAP, super.getResultEventType());
    }
}