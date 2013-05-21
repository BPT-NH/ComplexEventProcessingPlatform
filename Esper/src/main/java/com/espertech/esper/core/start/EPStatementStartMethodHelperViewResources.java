/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.epl.core.ViewResourceDelegateUnverified;
import com.espertech.esper.epl.core.ViewResourceDelegateVerified;
import com.espertech.esper.epl.core.ViewResourceDelegateVerifiedStream;
import com.espertech.esper.epl.expression.ExprPreviousNode;
import com.espertech.esper.epl.expression.ExprPriorNode;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.util.CollectionUtil;
import com.espertech.esper.view.DataWindowViewWithPrevious;
import com.espertech.esper.view.ViewFactory;
import com.espertech.esper.view.ViewFactoryChain;
import com.espertech.esper.view.internal.PriorEventViewFactory;
import com.espertech.esper.view.std.GroupByViewFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class EPStatementStartMethodHelperViewResources
{
    private static final Log log = LogFactory.getLog(EPStatementStartMethodHelperViewResources.class);

    public static ViewResourceDelegateVerified verifyPreviousAndPriorRequirements(ViewFactoryChain[] unmaterializedViewChain, ViewResourceDelegateUnverified delegate)
            throws ExprValidationException
    {
        boolean hasPriorNodes = !delegate.getPriorRequests().isEmpty();
        boolean hasPreviousNodes = !delegate.getPreviousRequests().isEmpty();

        int numStreams = unmaterializedViewChain.length;
        ViewResourceDelegateVerifiedStream[] perStream = new ViewResourceDelegateVerifiedStream[numStreams];

        // verify "previous"
        List<ExprPreviousNode>[] previousPerStream = new List[numStreams];
        for (ExprPreviousNode previousNode : delegate.getPreviousRequests()) {
            int stream = previousNode.getStreamNumber();
            List<ViewFactory> factories = unmaterializedViewChain[stream].getViewFactoryChain();

            boolean pass = inspectViewFactoriesForPrevious(factories);
            if (!pass) {
                throw new ExprValidationException("Previous function requires a single data window view onto the stream");
            }

            boolean found = false;
            for (ViewFactory factory : factories) {
                if (factory instanceof DataWindowViewWithPrevious) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ExprValidationException("Required data window not found for the 'prev' function, specify a data window for which previous events are retained");
            }

            if (previousPerStream[stream] == null) {
                previousPerStream[stream] = new ArrayList<ExprPreviousNode>();
            }
            previousPerStream[stream].add(previousNode);
        }

        // verify "prior"
        SortedMap<Integer, List<ExprPriorNode>>[] priorPerStream = new SortedMap[numStreams];
        for (ExprPriorNode priorNode : delegate.getPriorRequests()) {
            int stream = priorNode.getStreamNumber();

            if (priorPerStream[stream] == null) {
                priorPerStream[stream] = new TreeMap<Integer, List<ExprPriorNode>>();
            }

            TreeMap<Integer, List<ExprPriorNode>> treemap = (TreeMap<Integer, List<ExprPriorNode>>) priorPerStream[stream];
            List<ExprPriorNode> callbackList = treemap.get(priorNode.getConstantIndexNumber());
            if (callbackList == null)
            {
                callbackList = new LinkedList<ExprPriorNode>();
                treemap.put(priorNode.getConstantIndexNumber(), callbackList);
            }
            callbackList.add(priorNode);
        }

        // build per-stream info
        for (int i = 0; i < numStreams; i++) {
            if (previousPerStream[i] == null) {
                previousPerStream[i] = Collections.emptyList();
            }
            if (priorPerStream[i] == null) {
                priorPerStream[i] = CollectionUtil.EMPTY_SORTED_MAP;
            }
            perStream[i] = new ViewResourceDelegateVerifiedStream(previousPerStream[i], priorPerStream[i]);
        }

        return new ViewResourceDelegateVerified(hasPriorNodes, hasPreviousNodes, perStream);
    }

    private static boolean inspectViewFactoriesForPrevious(List<ViewFactory> viewFactories)
    {
        // We allow the capability only if
        //  - 1 view
        //  - 2 views and the first view is a group-by (for window-per-group access)
        if (viewFactories.size() == 1)
        {
            return true;
        }
        if (viewFactories.size() == 2)
        {
            if (viewFactories.get(0) instanceof GroupByViewFactory)
            {
                return true;
            }
            if (viewFactories.get(1) instanceof PriorEventViewFactory) {
                return true;
            }
            return false;
        }
        return true;
    }

}
