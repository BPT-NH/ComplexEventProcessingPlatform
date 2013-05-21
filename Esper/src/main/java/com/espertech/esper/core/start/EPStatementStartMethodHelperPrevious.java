/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.core.context.util.AgentInstanceViewFactoryChainContext;
import com.espertech.esper.epl.core.ViewResourceDelegateVerified;
import com.espertech.esper.epl.core.ViewResourceDelegateVerifiedStream;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.view.DataWindowViewWithPrevious;
import com.espertech.esper.view.ViewFactory;
import com.espertech.esper.view.window.RandomAccessByIndexGetter;
import com.espertech.esper.view.window.RelativeAccessByEventNIndexMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Starts and provides the stop method for EPL statements.
 */
public class EPStatementStartMethodHelperPrevious
{
    private static final Log log = LogFactory.getLog(EPStatementStartMethodHelperPrevious.class);

    public static Map<ExprPreviousNode, ExprPreviousEvalStrategy> compilePreviousNodeStrategies(ViewResourceDelegateVerified viewResourceDelegate, AgentInstanceViewFactoryChainContext[] contexts) {

        if (!viewResourceDelegate.isHasPrevious()) {
            return Collections.emptyMap();
        }

        Map<ExprPreviousNode, ExprPreviousEvalStrategy> strategies = new HashMap<ExprPreviousNode, ExprPreviousEvalStrategy>();

        for (int streamNum = 0; streamNum < contexts.length; streamNum++) {

            // get stream-specific info
            ViewResourceDelegateVerifiedStream delegate = viewResourceDelegate.getPerStream()[streamNum];

            // obtain getter
            handlePrevious(delegate.getPreviousRequests(), contexts[streamNum].getPreviousNodeGetter(), strategies);
        }

        return strategies;
    }

    public static DataWindowViewWithPrevious findPreviousViewFactory(List<ViewFactory> factories) {
        ViewFactory factoryFound = null;
        for (ViewFactory factory : factories) {
            if (factory instanceof DataWindowViewWithPrevious) {
                factoryFound = factory;
                break;
            }
        }
        if (factoryFound == null) {
            throw new RuntimeException("Failed to find 'previous'-handling view factory");  // was verified earlier, should not occur
        }
        return (DataWindowViewWithPrevious) factoryFound;
    }

    private static void handlePrevious(List<ExprPreviousNode> previousRequests, Object previousNodeGetter, Map<ExprPreviousNode, ExprPreviousEvalStrategy> strategies) {

        if (previousRequests.isEmpty()) {
            return;
        }
        
        RandomAccessByIndexGetter randomAccessGetter = null;
        RelativeAccessByEventNIndexMap relativeAccessGetter = null;
        if (previousNodeGetter instanceof RandomAccessByIndexGetter) {
            randomAccessGetter = (RandomAccessByIndexGetter) previousNodeGetter;
        }
        else if (previousNodeGetter instanceof RelativeAccessByEventNIndexMap) {
            relativeAccessGetter = (RelativeAccessByEventNIndexMap) previousNodeGetter;
        }
        else {
            throw new RuntimeException("Unexpected 'previous' handler: " + previousNodeGetter);
        }
        
        for (ExprPreviousNode previousNode : previousRequests) {
            int streamNumber = previousNode.getStreamNumber();
            PreviousType previousType = previousNode.getPreviousType();
            ExprPreviousEvalStrategy evaluator;

            if (previousType == PreviousType.PREVWINDOW) {
                evaluator = new ExprPreviousEvalStrategyWindow(streamNumber, previousNode.getChildNodes()[1].getExprEvaluator(), previousNode.getResultType().getComponentType(),
                        randomAccessGetter, relativeAccessGetter);
            }
            else if (previousType == PreviousType.PREVCOUNT) {
                evaluator = new ExprPreviousEvalStrategyCount(streamNumber, randomAccessGetter, relativeAccessGetter);
            }
            else {
                evaluator = new ExprPreviousEvalStrategyPrev(streamNumber, previousNode.getChildNodes()[0].getExprEvaluator(), previousNode.getChildNodes()[1].getExprEvaluator(),
                        randomAccessGetter, relativeAccessGetter, previousNode.isConstantIndex(), previousNode.getConstantIndexNumber(), previousType == PreviousType.PREVTAIL);
            }

            strategies.put(previousNode, evaluator);
        }
    }
}
