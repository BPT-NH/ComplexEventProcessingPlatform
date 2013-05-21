/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.pattern;


import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.annotation.AuditEnum;
import com.espertech.esper.event.EventBeanUtility;
import com.espertech.esper.util.AuditPath;
import com.espertech.esper.util.JavaClassHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;

/**
 * This class represents the state of a followed-by operator in the evaluation state tree.
 */
public final class EvalAuditStateNode extends EvalStateNode implements Evaluator
{
    private final EvalAuditNode evalAuditNode;
    private EvalStateNode childState;


    /**
     * Constructor.
     * @param parentNode is the parent evaluator to call to indicate truth value
     * @param evalAuditNode is the factory node associated to the state
     */
    public EvalAuditStateNode(Evaluator parentNode,
                              EvalAuditNode evalAuditNode,
                              EvalStateNodeNumber stateNodeNumber,
                              long stateNodeId)
    {
        super(parentNode);

        this.evalAuditNode = evalAuditNode;
        childState = evalAuditNode.getChildNode().newState(this, stateNodeNumber, stateNodeId);
    }

    public EvalNode getFactoryNode() {
        return evalAuditNode;
    }

    public final void start(MatchedEventMap beginState)
    {
        childState.start(beginState);
        evalAuditNode.getFactoryNode().increaseRefCount(this, evalAuditNode.getContext().getPatternContext());
    }

    public final void evaluateTrue(MatchedEventMap matchEvent, EvalStateNode fromNode, boolean isQuitted)
    {
        if (evalAuditNode.getFactoryNode().isAuditPattern() && AuditPath.isInfoEnabled()) {
            String message = toStringEvaluateTrue(this, evalAuditNode.getFactoryNode().getPatternExpr(), matchEvent, fromNode, isQuitted);
            AuditPath.auditLog(evalAuditNode.getContext().getStatementContext().getEngineURI(), evalAuditNode.getContext().getPatternContext().getStatementName(), AuditEnum.PATTERN, message);
        }

        this.getParentEvaluator().evaluateTrue(matchEvent, this, isQuitted);

        if (isQuitted)
        {
            evalAuditNode.getFactoryNode().decreaseRefCount(this, evalAuditNode.getContext().getPatternContext());
        }
    }

    public final void evaluateFalse(EvalStateNode fromNode)
    {
        if (evalAuditNode.getFactoryNode().isAuditPattern() && AuditPath.isInfoEnabled()) {
            String message = toStringEvaluateFalse(this, evalAuditNode.getFactoryNode().getPatternExpr(), fromNode);
            AuditPath.auditLog(evalAuditNode.getContext().getStatementContext().getEngineURI(), evalAuditNode.getContext().getPatternContext().getStatementName(), AuditEnum.PATTERN, message);
        }

        this.getParentEvaluator().evaluateFalse(this);
    }

    public final void quit()
    {
        if (childState != null) {
            childState.quit();
        }
        evalAuditNode.getFactoryNode().decreaseRefCount(this, evalAuditNode.getContext().getPatternContext());
    }

    public final Object accept(EvalStateNodeVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public final Object childrenAccept(EvalStateNodeVisitor visitor, Object data)
    {
        if (childState != null) {
            childState.accept(visitor, data);
        }
        return data;
    }

    public EvalStateNode getChildState() {
        return childState;
    }

    public final String toString()
    {
        return "EvalAuditStateNode";
    }

    public boolean isNotOperator() {
        EvalNode evalNode = evalAuditNode.getChildNode();
        return evalNode instanceof EvalNotNode;
    }

    public boolean isFilterChildNonQuitting() {
        return evalAuditNode.getFactoryNode().isFilterChildNonQuitting();
    }

    public boolean isFilterStateNode() {
        return evalAuditNode.getChildNode() instanceof EvalFilterNode;
    }

    private static String toStringEvaluateTrue(EvalAuditStateNode current, String patternExpression, MatchedEventMap matchEvent, EvalStateNode fromNode, boolean isQuitted) {

        StringWriter writer = new StringWriter();

        writePatternExpr(current, patternExpression, writer);
        writer.write(" evaluate-true {");

        writer.write(" from: ");
        JavaClassHelper.writeInstance(writer, fromNode, false);

        writer.write(" map: {");
        String delimiter = "";
        Object[] data = matchEvent.getMatchingEvents();
        for (int i = 0; i < data.length; i++) {
            String name = matchEvent.getMeta().getTagsPerIndex()[i];
            Object value = matchEvent.getMatchingEventAsObject(i);
            writer.write(delimiter);
            writer.write(name);
            writer.write("=");
            if (value instanceof EventBean) {
                writer.write(((EventBean) value).getUnderlying().toString());
            }
            else if (value instanceof EventBean[]) {
                writer.write(EventBeanUtility.summarize((EventBean[]) value));
            }
            delimiter = ", ";
        }

        writer.write("} quitted: ");
        writer.write(Boolean.toString(isQuitted));

        writer.write("}");
        return writer.toString();
    }

    private String toStringEvaluateFalse(EvalAuditStateNode current, String patternExpression, EvalStateNode fromNode) {

        StringWriter writer = new StringWriter();
        writePatternExpr(current, patternExpression, writer);
        writer.write(" evaluate-false {");

        writer.write(" from ");
        JavaClassHelper.writeInstance(writer, fromNode, false);

        writer.write("}");
        return writer.toString();
    }

    protected static void writePatternExpr(EvalAuditStateNode current, String patternExpression, StringWriter writer) {
        if (patternExpression != null) {
            writer.write('(');
            writer.write(patternExpression);
            writer.write(')');
        }
        else {
            JavaClassHelper.writeInstance(writer, "subexr", current);
        }
    }

    private static final Log log = LogFactory.getLog(EvalAuditStateNode.class);
}
