/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Superclass of all nodes in an evaluation tree representing an event pattern expression.
 * Follows the Composite pattern. Child nodes do not carry references to parent nodes, the tree
 * is unidirectional.
 */
public abstract class EvalNodeBase implements EvalNode
{
    private static final Log log = LogFactory.getLog(EvalNodeBase.class);

    private final PatternAgentInstanceContext context;

    protected EvalNodeBase(PatternAgentInstanceContext context) {
        this.context = context;
    }

    /**
     * Create the evaluation state node containing the truth value state for each operator in an
     * event expression.
     *
     *
     *
     *
     * @param parentNode is the parent evaluator node that this node indicates a change in truth value to
     * @param stateNodeNumber
     * @param stateNodeId
     * @return state node containing the truth value state for the operator
     */
    public abstract EvalStateNode newState(Evaluator parentNode,
                                           EvalStateNodeNumber stateNodeNumber,
                                           long stateNodeId);

    public final PatternAgentInstanceContext getContext() {
        return context;
    }
}
