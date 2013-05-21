/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.spec;

import com.espertech.esper.epl.declexpr.ExprDeclaredNode;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprNodeUtility;
import com.espertech.esper.epl.expression.ExprSubselectNode;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Specification object representing a complete EPL statement including all EPL constructs.
 */
public class StatementSpecCompiled
{
    public static final StatementSpecCompiled DEFAULT_SELECT_ALL_EMPTY;

    static {
        DEFAULT_SELECT_ALL_EMPTY = new StatementSpecCompiled();
        DEFAULT_SELECT_ALL_EMPTY.getSelectClauseSpec().setSelectExprList(new SelectClauseElementWildcard());
    }

    private final OnTriggerDesc onTriggerDesc;
    private final CreateWindowDesc createWindowDesc;
    private final CreateIndexDesc createIndexDesc;
    private final CreateVariableDesc createVariableDesc;
    private final CreateSchemaDesc createSchemaDesc;
    private InsertIntoDesc insertIntoDesc;
    private SelectClauseStreamSelectorEnum selectStreamDirEnum;
    private SelectClauseSpecCompiled selectClauseSpec;
    private final StreamSpecCompiled[] streamSpecs;
    private final OuterJoinDesc[] outerJoinDescList;
    private ExprNode filterExprRootNode;
    private final ExprNode[] groupByExpressions;
    private final ExprNode havingExprRootNode;
    private final OutputLimitSpec outputLimitSpec;
    private final OrderByItem[] orderByList;
    private final ExprSubselectNode[] subSelectExpressions;
    private final ExprDeclaredNode[] declaredExpressions;
    private final Set<String> variableReferences;
    private final RowLimitSpec rowLimitSpec;
    private final String[] eventTypeReferences;
    private final Annotation[] annotations;
    private final UpdateDesc updateSpec;
    private final MatchRecognizeSpec matchRecognizeSpec;
    private final ForClauseSpec forClauseSpec;
    private final Map<Integer, List<ExprNode>> sqlParameters;
    private final CreateContextDesc contextDesc;
    private final String optionalContextName;
    private final CreateDataFlowDesc createGraphDesc;
    private final CreateExpressionDesc createExpressionDesc;
    private final FireAndForgetSpec fireAndForgetSpec;

    /**
     * Ctor.
     * @param insertIntoDesc insert into def
     * @param selectClauseStreamSelectorEnum stream selection
     * @param selectClauseSpec select clause
     * @param streamSpecs specs for streams
     * @param outerJoinDescList outer join def
     * @param filterExprRootNode where filter expr nodes
     * @param groupByExpressions group by expression
     * @param havingExprRootNode having expression
     * @param outputLimitSpec output limit
     * @param orderByList order by
     * @param subSelectExpressions list of subqueries
     * @param onTriggerDesc describes on-delete statements
     * @param createWindowDesc describes create-window statements
     * @param createVariableDesc describes create-variable statements
     * @param rowLimitSpec row limit specification, or null if none supplied
     * @param eventTypeReferences event type names statically determined
     * @param annotations statement annotations
     * @param updateSpec update specification if used
     * @param matchRecognizeSpec if provided
     * @param variableReferences variables referenced
     * @param createIndexDesc when an index get
     */
    public StatementSpecCompiled(OnTriggerDesc onTriggerDesc,
                                 CreateWindowDesc createWindowDesc,
                                 CreateIndexDesc createIndexDesc,
                                 CreateVariableDesc createVariableDesc,
                                 CreateSchemaDesc createSchemaDesc,
                                 InsertIntoDesc insertIntoDesc,
                                 SelectClauseStreamSelectorEnum selectClauseStreamSelectorEnum,
                                 SelectClauseSpecCompiled selectClauseSpec,
                                 StreamSpecCompiled[] streamSpecs,
                                 OuterJoinDesc[] outerJoinDescList,
                                 ExprNode filterExprRootNode,
                                 ExprNode[] groupByExpressions,
                                 ExprNode havingExprRootNode,
                                 OutputLimitSpec outputLimitSpec,
                                 OrderByItem[] orderByList,
                                 ExprSubselectNode[] subSelectExpressions,
                                 ExprDeclaredNode[] declaredExpressions,
                                 Set<String> variableReferences,
                                 RowLimitSpec rowLimitSpec,
                                 String[] eventTypeReferences,
                                 Annotation[] annotations,
                                 UpdateDesc updateSpec,
                                 MatchRecognizeSpec matchRecognizeSpec,
                                 ForClauseSpec forClauseSpec,
                                 Map<Integer, List<ExprNode>> sqlParameters,
                                 CreateContextDesc contextDesc,
                                 String optionalContextName,
                                 CreateDataFlowDesc createGraphDesc,
                                 CreateExpressionDesc createExpressionDesc,
                                 FireAndForgetSpec fireAndForgetSpec)
    {
        this.onTriggerDesc = onTriggerDesc;
        this.createWindowDesc = createWindowDesc;
        this.createIndexDesc = createIndexDesc;
        this.createVariableDesc = createVariableDesc;
        this.createSchemaDesc = createSchemaDesc;
        this.insertIntoDesc = insertIntoDesc;
        this.selectStreamDirEnum = selectClauseStreamSelectorEnum;
        this.selectClauseSpec = selectClauseSpec;
        this.streamSpecs = streamSpecs;
        this.outerJoinDescList = outerJoinDescList;
        this.filterExprRootNode = filterExprRootNode;
        this.groupByExpressions = groupByExpressions;
        this.havingExprRootNode = havingExprRootNode;
        this.outputLimitSpec = outputLimitSpec;
        this.orderByList = orderByList;
        this.subSelectExpressions = subSelectExpressions;
        this.declaredExpressions = declaredExpressions;
        this.variableReferences = variableReferences;
        this.rowLimitSpec = rowLimitSpec;
        this.eventTypeReferences = eventTypeReferences;
        this.annotations = annotations;
        this.updateSpec = updateSpec;
        this.matchRecognizeSpec = matchRecognizeSpec;
        this.forClauseSpec = forClauseSpec;
        this.sqlParameters = sqlParameters;
        this.contextDesc = contextDesc;
        this.optionalContextName = optionalContextName;
        this.createGraphDesc = createGraphDesc;
        this.createExpressionDesc = createExpressionDesc;
        this.fireAndForgetSpec = fireAndForgetSpec;
    }

    /**
     * Ctor.
     */
    public StatementSpecCompiled()
    {
        onTriggerDesc = null;
        createWindowDesc = null;
        createIndexDesc = null;
        createVariableDesc = null;
        createSchemaDesc = null;
        insertIntoDesc = null;
        selectStreamDirEnum = SelectClauseStreamSelectorEnum.RSTREAM_ISTREAM_BOTH;
        selectClauseSpec = new SelectClauseSpecCompiled(false);
        streamSpecs = StreamSpecCompiled.EMPTY_STREAM_ARRAY;
        outerJoinDescList = OuterJoinDesc.EMPTY_OUTERJOIN_ARRAY;
        filterExprRootNode = null;
        groupByExpressions = ExprNodeUtility.EMPTY_EXPR_ARRAY;
        havingExprRootNode = null;
        outputLimitSpec = null;
        orderByList = OrderByItem.EMPTY_ORDERBY_ARRAY;
        subSelectExpressions = ExprSubselectNode.EMPTY_SUBSELECT_ARRAY;
        declaredExpressions = ExprNodeUtility.EMPTY_DECLARED_ARR;
        variableReferences = new HashSet<String>();
        rowLimitSpec = null;
        eventTypeReferences = new String[0];
        annotations = new Annotation[0];
        updateSpec = null;
        matchRecognizeSpec = null;
        forClauseSpec = null;
        sqlParameters = null;
        contextDesc = null;
        optionalContextName = null;
        createGraphDesc = null;
        createExpressionDesc = null;
        fireAndForgetSpec = null;
    }

    /**
     * Returns the specification for an create-window statement.
     * @return create-window spec, or null if not such a statement
     */
    public CreateWindowDesc getCreateWindowDesc()
    {
        return createWindowDesc;
    }

    /**
     * Returns the create-variable statement descriptor.
     * @return create-variable spec
     */
    public CreateVariableDesc getCreateVariableDesc()
    {
        return createVariableDesc;
    }

    /**
     * Returns the FROM-clause stream definitions.
     * @return list of stream specifications
     */
    public StreamSpecCompiled[] getStreamSpecs()
    {
        return streamSpecs;
    }

    /**
     * Returns SELECT-clause list of expressions.
     * @return list of expressions and optional name
     */
    public SelectClauseSpecCompiled getSelectClauseSpec()
    {
        return selectClauseSpec;
    }

    /**
     * Returns the WHERE-clause root node of filter expression.
     * @return filter expression root node
     */
    public ExprNode getFilterRootNode()
    {
        return filterExprRootNode;
    }

    /**
     * Returns the LEFT/RIGHT/FULL OUTER JOIN-type and property name descriptor, if applicable. Returns null if regular join.
     * @return outer join type, stream names and property names
     */
    public OuterJoinDesc[] getOuterJoinDescList()
    {
        return outerJoinDescList;
    }

    /**
     * Returns list of group-by expressions.
     * @return group-by expression nodes as specified in group-by clause
     */
    public ExprNode[] getGroupByExpressions()
    {
        return groupByExpressions;
    }

    /**
     * Returns expression root node representing the having-clause, if present, or null if no having clause was supplied.
     * @return having-clause expression top node
     */
    public ExprNode getHavingExprRootNode()
    {
        return havingExprRootNode;
    }

    /**
     * Returns the output limit definition, if any.
     * @return output limit spec
     */
    public OutputLimitSpec getOutputLimitSpec()
    {
        return outputLimitSpec;
    }

    /**
     * Return a descriptor with the insert-into event name and optional list of columns.
     * @return insert into specification
     */
    public InsertIntoDesc getInsertIntoDesc()
    {
        return insertIntoDesc;
    }

    /**
     * Returns the list of order-by expression as specified in the ORDER BY clause.
     * @return Returns the orderByList.
     */
    public OrderByItem[] getOrderByList() {
        return orderByList;
    }

    /**
     * Returns the stream selector (rstream/istream).
     * @return stream selector
     */
    public SelectClauseStreamSelectorEnum getSelectStreamSelectorEnum()
    {
        return selectStreamDirEnum;
    }

    /**
     * Set the where clause filter node.
     * @param optionalFilterNode is the where-clause filter node
     */
    public void setFilterExprRootNode(ExprNode optionalFilterNode)
    {
        filterExprRootNode = optionalFilterNode;
    }

    /**
     * Returns the list of lookup expression nodes.
     * @return lookup nodes
     */
    public ExprSubselectNode[] getSubSelectExpressions()
    {
        return subSelectExpressions;
    }

    /**
     * Returns the specification for an on-delete or on-select statement.
     * @return on-trigger spec, or null if not such a statement
     */
    public OnTriggerDesc getOnTriggerDesc()
    {
        return onTriggerDesc;
    }

    /**
     * Returns true to indicate the statement has variables.
     * @return true for statements that use variables
     */
    public boolean isHasVariables()
    {
        return variableReferences != null && !variableReferences.isEmpty();
    }

    /**
     * Sets the stream selection.
     * @param selectStreamDirEnum stream selection
     */
    public void setSelectStreamDirEnum(SelectClauseStreamSelectorEnum selectStreamDirEnum) {
        this.selectStreamDirEnum = selectStreamDirEnum;
    }

    /**
     * Returns the row limit specification, or null if none supplied.
     * @return row limit spec if any
     */
    public RowLimitSpec getRowLimitSpec()
    {
        return rowLimitSpec;
    }

    /**
     * Returns the event type name in used by the statement.
     * @return set of event type name
     */
    public String[] getEventTypeReferences()
    {
        return eventTypeReferences;
    }

    /**
     * Returns annotations or empty array if none.
     * @return annotations
     */
    public Annotation[] getAnnotations()
    {
        return annotations;
    }

    /**
     * Sets the insert-into clause.
     * @param insertIntoDesc insert-into clause.
     */
    public void setInsertIntoDesc(InsertIntoDesc insertIntoDesc)
    {
        this.insertIntoDesc = insertIntoDesc;
    }

    /**
     * Sets the select clause.
     * @param selectClauseSpec select clause
     */
    public void setSelectClauseSpec(SelectClauseSpecCompiled selectClauseSpec)
    {
        this.selectClauseSpec = selectClauseSpec;
    }

    /**
     * Returns the update spec if update clause is used.
     * @return update desc
     */
    public UpdateDesc getUpdateSpec()
    {
        return updateSpec;
    }

    /**
     * Returns the match recognize spec, if used
     * @return match recognize spec
     */
    public MatchRecognizeSpec getMatchRecognizeSpec() {
        return matchRecognizeSpec;
    }

    /**
     * Return variables referenced.
     * @return variables
     */
    public Set<String> getVariableReferences() {
        return variableReferences;
    }

    /**
     * Returns create index
     * @return create index
     */
    public CreateIndexDesc getCreateIndexDesc()
    {
        return createIndexDesc;
    }

    public CreateSchemaDesc getCreateSchemaDesc()
    {
        return createSchemaDesc;
    }

    public ForClauseSpec getForClauseSpec()
    {
        return forClauseSpec;
    }

    public Map<Integer, List<ExprNode>> getSqlParameters()
    {
        return sqlParameters;
    }

    public ExprDeclaredNode[] getDeclaredExpressions() {
        return declaredExpressions;
    }

    public CreateContextDesc getContextDesc() {
        return contextDesc;
    }

    public String getOptionalContextName() {
        return optionalContextName;
    }

    public CreateDataFlowDesc getCreateGraphDesc() {
        return createGraphDesc;
    }

    public CreateExpressionDesc getCreateExpressionDesc() {
        return createExpressionDesc;
    }

    public FireAndForgetSpec getFireAndForgetSpec() {
        return fireAndForgetSpec;
    }
}
