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
 * This class represents an 'and' operator in the evaluation tree representing an event expressions.
 */
public class EvalAndNode extends EvalNodeBase
{
    protected final EvalAndFactoryNode factoryNode;
    protected final EvalNode[] childNodes;

    public EvalAndNode(PatternAgentInstanceContext context, EvalAndFactoryNode factoryNode, EvalNode[] childNodes) {
        super(context);
        this.factoryNode = factoryNode;
        this.childNodes = childNodes;
    }

    public EvalAndFactoryNode getFactoryNode() {
        return factoryNode;
    }

    public EvalNode[] getChildNodes() {
        return childNodes;
    }

    public EvalStateNode newState(Evaluator parentNode,
                                  EvalStateNodeNumber stateNodeNumber, long stateNodeId)
    {
        return new EvalAndStateNode(parentNode, this);
    }

    private static final Log log = LogFactory.getLog(EvalAndNode.class);
}
