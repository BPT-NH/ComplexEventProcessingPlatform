/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.spec;

import com.espertech.esper.client.ConfigurationInformation;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.soda.*;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.core.context.mgr.ContextManagementService;
import com.espertech.esper.core.context.util.ContextPropertyRegistry;
import com.espertech.esper.core.service.EPAdministratorHelper;
import com.espertech.esper.epl.agg.access.AggregationStateType;
import com.espertech.esper.epl.core.EngineImportService;
import com.espertech.esper.epl.core.EngineImportSingleRowDesc;
import com.espertech.esper.epl.db.DatabasePollingViewableFactory;
import com.espertech.esper.epl.declexpr.ExprDeclaredHelper;
import com.espertech.esper.epl.declexpr.ExprDeclaredNode;
import com.espertech.esper.epl.declexpr.ExprDeclaredNodeImpl;
import com.espertech.esper.epl.declexpr.ExprDeclaredService;
import com.espertech.esper.epl.enummethod.dot.ExprLambdaGoesNode;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.epl.named.NamedWindowService;
import com.espertech.esper.epl.parse.ASTAggregationHelper;
import com.espertech.esper.epl.parse.ASTFilterSpecHelper;
import com.espertech.esper.epl.parse.ASTWalkException;
import com.espertech.esper.epl.script.ExprNodeScript;
import com.espertech.esper.epl.variable.VariableService;
import com.espertech.esper.pattern.*;
import com.espertech.esper.rowregex.*;
import com.espertech.esper.schedule.SchedulingService;
import com.espertech.esper.type.CronOperatorEnum;
import com.espertech.esper.type.MathArithTypeEnum;
import com.espertech.esper.type.MinMaxTypeEnum;
import com.espertech.esper.type.RelationalOpEnum;
import com.espertech.esper.util.PlaceholderParseException;
import com.espertech.esper.util.PlaceholderParser;

import java.util.*;

/**
 * Helper for mapping internal representations of a statement to the SODA object model for statements.
 */
public class StatementSpecMapper
{
    /**
     * Unmap expresission.
     * @param expression to unmap
     * @return expression
     */
    public static Expression unmap(ExprNode expression) {
        return unmapExpressionDeep(expression, new StatementSpecUnMapContext());
    }

    /**
     * Unmap pattern.
     * @param node to unmap
     * @return pattern
     */
    public static PatternExpr unmap(EvalFactoryNode node) {
        return unmapPatternEvalDeep(node, new StatementSpecUnMapContext());
    }

    /**
     * Unmap annotation.
     * @param node to unmap
     * @return annotation
     */
    public static AnnotationPart unmap(AnnotationDesc node) {
        List<AnnotationPart> list = unmapAnnotations(new ArrayList<AnnotationDesc>(Collections.singletonList(node)));
        return list.get(0);
    }

    /**
     * Unmap match recognize pattern.
     * @param pattern recognize pattern to unmap
     * @return match recognize pattern
     */
    public static MatchRecognizeRegEx unmap(RowRegexExprNode pattern) {
        return unmapExpressionDeepRowRegex(pattern, new StatementSpecUnMapContext());
    }

    /**
     * Maps the SODA object model to a statement specification.
     * @param sodaStatement is the object model to map
     * @param engineImportService for resolving imports such as plug-in aggregations
     * @param variableService provides variable values
     * @param configuration supplies config values
     * @return statement specification, and internal representation of a statement
     */
    public static StatementSpecRaw map(EPStatementObjectModel sodaStatement, EngineImportService engineImportService, VariableService variableService, ConfigurationInformation configuration, SchedulingService schedulingService, String engineURI, PatternNodeFactory patternNodeFactory, NamedWindowService namedWindowService, ContextManagementService contextManagementService, ExprDeclaredService exprDeclaredService)
    {
        StatementSpecMapContext mapContext = new StatementSpecMapContext(engineImportService, variableService, configuration, schedulingService, engineURI, patternNodeFactory, namedWindowService, contextManagementService, exprDeclaredService);

        StatementSpecRaw raw = map(sodaStatement, mapContext);
        if (mapContext.isHasVariables())
        {
            raw.setHasVariables(true);
        }
        raw.setReferencedVariables(mapContext.getVariableNames());
        return raw;
    }

    private static StatementSpecRaw map(EPStatementObjectModel sodaStatement, StatementSpecMapContext mapContext)
    {
        StatementSpecRaw raw = new StatementSpecRaw(SelectClauseStreamSelectorEnum.ISTREAM_ONLY);

        List<AnnotationDesc> annotations = mapAnnotations(sodaStatement.getAnnotations());
        raw.setAnnotations(annotations);
        mapFireAndForget(sodaStatement.getFireAndForgetClause(), raw, mapContext);
        mapExpressionDeclaration(sodaStatement.getExpressionDeclarations(), raw, mapContext);
        mapScriptExpressions(sodaStatement.getScriptExpressions(), raw, mapContext);
        mapContextName(sodaStatement.getContextName(), raw, mapContext);
        mapUpdateClause(sodaStatement.getUpdateClause(), raw, mapContext);
        mapCreateContext(sodaStatement.getCreateContext(), raw, mapContext);
        mapCreateWindow(sodaStatement.getCreateWindow(), sodaStatement.getFromClause(), raw, mapContext);
        mapCreateIndex(sodaStatement.getCreateIndex(), raw, mapContext);
        mapCreateVariable(sodaStatement.getCreateVariable(), raw, mapContext);
        mapCreateSchema(sodaStatement.getCreateSchema(), raw, mapContext);
        mapCreateExpression(sodaStatement.getCreateExpression(), raw, mapContext);
        mapCreateGraph(sodaStatement.getCreateDataFlow(), raw, mapContext);
        mapOnTrigger(sodaStatement.getOnExpr(), raw, mapContext);
        InsertIntoDesc desc = mapInsertInto(sodaStatement.getInsertInto());
        raw.setInsertIntoDesc(desc);
        mapSelect(sodaStatement.getSelectClause(), raw, mapContext);
        mapFrom(sodaStatement.getFromClause(), raw, mapContext);
        mapWhere(sodaStatement.getWhereClause(), raw, mapContext);
        mapGroupBy(sodaStatement.getGroupByClause(), raw, mapContext);
        mapHaving(sodaStatement.getHavingClause(), raw, mapContext);
        mapOutputLimit(sodaStatement.getOutputLimitClause(), raw, mapContext);
        mapOrderBy(sodaStatement.getOrderByClause(), raw, mapContext);
        mapRowLimit(sodaStatement.getRowLimitClause(), raw, mapContext);
        mapMatchRecognize(sodaStatement.getMatchRecognizeClause(), raw, mapContext);
        mapForClause(sodaStatement.getForClause(), raw, mapContext);
        mapSQLParameters(sodaStatement.getFromClause(), raw, mapContext);

        // from clause is required for create-window
        if (sodaStatement.getCreateWindow() != null && raw.getStreamSpecs().size() == 0) {
            FilterSpecRaw spec = new FilterSpecRaw("java.lang.Object", Collections.<ExprNode>emptyList(), null);
            raw.getStreamSpecs().add(new FilterStreamSpecRaw(spec, ViewSpec.EMPTY_VIEWSPEC_ARRAY, null, new StreamSpecOptions()));
        }
        return raw;
    }

    private static void mapFireAndForget(FireAndForgetClause fireAndForgetClause, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        if (fireAndForgetClause == null) {
            return;
        }
        else if (fireAndForgetClause instanceof FireAndForgetDelete) {
            raw.setFireAndForgetSpec(new FireAndForgetSpecDelete());
        }
        else if (fireAndForgetClause instanceof FireAndForgetUpdate) {
            FireAndForgetUpdate upd = (FireAndForgetUpdate) fireAndForgetClause;
            List<OnTriggerSetAssignment> assignments = new ArrayList<OnTriggerSetAssignment>();
            for (AssignmentPair pair : upd.getAssignments())
            {
                ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
            }
            FireAndForgetSpecUpdate updspec = new FireAndForgetSpecUpdate(assignments);
            raw.setFireAndForgetSpec(updspec);
        }
        else {
            throw new IllegalStateException("Unrecognized fire-and-forget clause " + fireAndForgetClause);
        }
    }

    /**
     * Maps the internal representation of a statement to the SODA object model.
     * @param statementSpec is the internal representation
     * @return object model of statement
     */
    public static StatementSpecUnMapResult unmap(StatementSpecRaw statementSpec)
    {
        StatementSpecUnMapContext unmapContext = new StatementSpecUnMapContext();
        EPStatementObjectModel model = unmapInternal(statementSpec, unmapContext);
        return new StatementSpecUnMapResult(model, unmapContext.getIndexedParams());
    }

    private static EPStatementObjectModel unmapInternal(StatementSpecRaw statementSpec, StatementSpecUnMapContext unmapContext)
    {
        EPStatementObjectModel model = new EPStatementObjectModel();
        List<AnnotationPart> annotations = unmapAnnotations(statementSpec.getAnnotations());
        model.setAnnotations(annotations);
        unmapFireAndForget(statementSpec.getFireAndForgetSpec(), model, unmapContext);
        List<ExpressionDeclaration> expressionDeclarations = unmapExpressionDeclarations(statementSpec.getExpressionDeclDesc(), unmapContext);
        model.setExpressionDeclarations(expressionDeclarations);
        List<ScriptExpression> scripts = unmapScriptExpressions(statementSpec.getScriptExpressions(), unmapContext);
        model.setScriptExpressions(scripts);
        unmapContextName(statementSpec.getOptionalContextName(), model);
        unmapCreateContext(statementSpec.getCreateContextDesc(), model, unmapContext);
        unmapCreateWindow(statementSpec.getCreateWindowDesc(), model, unmapContext);
        unmapCreateIndex(statementSpec.getCreateIndexDesc(), model, unmapContext);
        unmapCreateVariable(statementSpec.getCreateVariableDesc(), model, unmapContext);
        unmapCreateSchema(statementSpec.getCreateSchemaDesc(), model, unmapContext);
        unmapCreateExpression(statementSpec.getCreateExpressionDesc(), model, unmapContext);
        unmapCreateGraph(statementSpec.getCreateDataFlowDesc(), model, unmapContext);
        unmapUpdateClause(statementSpec.getStreamSpecs(), statementSpec.getUpdateDesc(), model, unmapContext);
        unmapOnClause(statementSpec.getOnTriggerDesc(), model, unmapContext);
        InsertIntoClause insertIntoClause = unmapInsertInto(statementSpec.getInsertIntoDesc());
        model.setInsertInto(insertIntoClause);
        SelectClause selectClause = unmapSelect(statementSpec.getSelectClauseSpec(), statementSpec.getSelectStreamSelectorEnum(), unmapContext);
        model.setSelectClause(selectClause);
        unmapFrom(statementSpec.getStreamSpecs(), statementSpec.getOuterJoinDescList(), model, unmapContext);
        unmapWhere(statementSpec.getFilterRootNode(), model, unmapContext);
        unmapGroupBy(statementSpec.getGroupByExpressions(), model, unmapContext);
        unmapHaving(statementSpec.getHavingExprRootNode(), model, unmapContext);
        unmapOutputLimit(statementSpec.getOutputLimitSpec(), model, unmapContext);
        unmapOrderBy(statementSpec.getOrderByList(), model, unmapContext);
        unmapRowLimit(statementSpec.getRowLimitSpec(), model, unmapContext);
        unmapMatchRecognize(statementSpec.getMatchRecognizeSpec(), model, unmapContext);
        unmapForClause(statementSpec.getForClauseSpec(), model, unmapContext);
        unmapSQLParameters(statementSpec.getSqlParameters(), unmapContext);
        return model;
    }

    private static void unmapFireAndForget(FireAndForgetSpec fireAndForgetSpec, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext) {
        if (fireAndForgetSpec == null) {
            return;
        }
        else if (fireAndForgetSpec instanceof FireAndForgetSpecDelete) {
            model.setFireAndForgetClause(new FireAndForgetDelete());
        }
        else if (fireAndForgetSpec instanceof FireAndForgetSpecUpdate) {
            FireAndForgetSpecUpdate upd = (FireAndForgetSpecUpdate) fireAndForgetSpec;
            FireAndForgetUpdate faf = new FireAndForgetUpdate();
            for (OnTriggerSetAssignment assignment : upd.getAssignments())
            {
                Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
                faf.addAssignment(new AssignmentPair(assignment.getVariableName(), expr));
            }
            model.setFireAndForgetClause(faf);
        }
        else {
            throw new IllegalStateException("Unrecognized type of fire-and-forget: " + fireAndForgetSpec);
        }
    }

    // Collect substitution parameters
    private static void unmapSQLParameters(Map<Integer, List<ExprNode>> sqlParameters, StatementSpecUnMapContext unmapContext)
    {
        if (sqlParameters == null) {
            return;
        }
        for (Map.Entry<Integer, List<ExprNode>> pair : sqlParameters.entrySet()) {
            for (ExprNode node : pair.getValue()) {
                unmapExpressionDeep(node, unmapContext);
            }
        }
    }

    private static void unmapOnClause(OnTriggerDesc onTriggerDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (onTriggerDesc == null)
        {
            return;
        }
        if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_DELETE)
        {
            OnTriggerWindowDesc window = (OnTriggerWindowDesc) onTriggerDesc;
            model.setOnExpr(new OnDeleteClause(window.getWindowName(), window.getOptionalAsName()));
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_UPDATE)
        {
            OnTriggerWindowUpdateDesc window = (OnTriggerWindowUpdateDesc) onTriggerDesc;
            OnUpdateClause clause = new OnUpdateClause(window.getWindowName(), window.getOptionalAsName());
            for (OnTriggerSetAssignment assignment : window.getAssignments())
            {
                Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
                clause.addAssignment(assignment.getVariableName(), expr);
            }
            model.setOnExpr(clause);
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_SELECT)
        {
            OnTriggerWindowDesc window = (OnTriggerWindowDesc) onTriggerDesc;
            OnSelectClause onSelect = new OnSelectClause(window.getWindowName(), window.getOptionalAsName());
            onSelect.setDeleteAndSelect(window.isDeleteAndSelect());
            model.setOnExpr(onSelect);
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_SET)
        {
            OnTriggerSetDesc trigger = (OnTriggerSetDesc) onTriggerDesc;
            OnSetClause clause = new OnSetClause();
            for (OnTriggerSetAssignment assignment : trigger.getAssignments())
            {
                Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
                clause.addAssignment(assignment.getVariableName(), expr);
            }
            model.setOnExpr(clause);
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_SPLITSTREAM)
        {
            OnTriggerSplitStreamDesc trigger = (OnTriggerSplitStreamDesc) onTriggerDesc;
            OnInsertSplitStreamClause clause = OnInsertSplitStreamClause.create();
            for (OnTriggerSplitStream stream : trigger.getSplitStreams())
            {
                Expression whereClause = null;
                if (stream.getWhereClause() != null)
                {
                    whereClause = unmapExpressionDeep(stream.getWhereClause(), unmapContext);
                }
                InsertIntoClause insertIntoClause = unmapInsertInto(stream.getInsertInto());
                SelectClause selectClause = unmapSelect(stream.getSelectClause(), SelectClauseStreamSelectorEnum.ISTREAM_ONLY, unmapContext);
                clause.addItem(OnInsertSplitStreamItem.create(insertIntoClause, selectClause, whereClause));
            }
            model.setOnExpr(clause);
        }
        else if (onTriggerDesc.getOnTriggerType() == OnTriggerType.ON_MERGE)
        {
            OnTriggerMergeDesc trigger = (OnTriggerMergeDesc) onTriggerDesc;
            List<OnMergeMatchItem> matchItems = new ArrayList<OnMergeMatchItem>();
            for (OnTriggerMergeMatched matched : trigger.getItems()) {

                List<OnMergeMatchedAction> actions = new ArrayList<OnMergeMatchedAction>();
                Expression matchCond = matched.getOptionalMatchCond() != null ? unmapExpressionDeep(matched.getOptionalMatchCond(), unmapContext) : null;
                OnMergeMatchItem matchItem = new OnMergeMatchItem(matched.isMatchedUnmatched(), matchCond, actions);
                for (OnTriggerMergeAction actionitem : matched.getActions())
                {
                    OnMergeMatchedAction action;
                    if (actionitem instanceof OnTriggerMergeActionDelete) {
                        OnTriggerMergeActionDelete delete = (OnTriggerMergeActionDelete) actionitem;
                        Expression optionalCondition = delete.getOptionalWhereClause() == null ? null : unmapExpressionDeep(delete.getOptionalWhereClause(), unmapContext);
                        action = new OnMergeMatchedDeleteAction(optionalCondition);
                    }
                    else if (actionitem instanceof OnTriggerMergeActionUpdate) {
                        OnTriggerMergeActionUpdate merge = (OnTriggerMergeActionUpdate) actionitem;
                        List<AssignmentPair> assignments = new ArrayList<AssignmentPair>();
                        for (OnTriggerSetAssignment pair : merge.getAssignments())
                        {
                            Expression expr = unmapExpressionDeep(pair.getExpression(), unmapContext);
                            assignments.add(new AssignmentPair(pair.getVariableName(), expr));
                        }
                        Expression optionalCondition = merge.getOptionalWhereClause() == null ? null : unmapExpressionDeep(merge.getOptionalWhereClause(), unmapContext);
                        action = new OnMergeMatchedUpdateAction(assignments, optionalCondition);
                    }
                    else if (actionitem instanceof OnTriggerMergeActionInsert) {
                        OnTriggerMergeActionInsert insert = (OnTriggerMergeActionInsert) actionitem;
                        List<String> columnNames = new ArrayList<String>(insert.getColumns());
                        List<SelectClauseElement> select = unmapSelectClauseElements(insert.getSelectClause(), unmapContext);
                        Expression optionalCondition = insert.getOptionalWhereClause() == null ? null : unmapExpressionDeep(insert.getOptionalWhereClause(), unmapContext);
                        action = new OnMergeMatchedInsertAction(columnNames, select, optionalCondition, insert.getOptionalStreamName());
                    }
                    else {
                        throw new IllegalArgumentException("Unrecognized merged action type '" + actionitem.getClass() + "'");
                    }
                    actions.add(action);
                }
                matchItems.add(matchItem);
            }
            model.setOnExpr(OnMergeClause.create(trigger.getWindowName(), trigger.getOptionalAsName(), matchItems));
        }
        else {
            throw new IllegalArgumentException("Type of on-clause not handled: " + onTriggerDesc.getOnTriggerType());
        }
    }

    private static void unmapUpdateClause(List<StreamSpecRaw> desc, UpdateDesc updateDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (updateDesc == null)
        {
            return;
        }
        String type = ((FilterStreamSpecRaw) desc.get(0)).getRawFilterSpec().getEventTypeName();
        UpdateClause clause = new UpdateClause(type, updateDesc.getOptionalStreamName());
        for (OnTriggerSetAssignment assignment : updateDesc.getAssignments())
        {
            Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
            clause.addAssignment(assignment.getVariableName(), expr);
        }
        model.setUpdateClause(clause);

        if (updateDesc.getOptionalWhereClause() != null)
        {
            Expression expr = unmapExpressionDeep(updateDesc.getOptionalWhereClause(), unmapContext);
            model.getUpdateClause().setOptionalWhereClause(expr);
        }
    }

    private static void unmapContextName(String contextName, EPStatementObjectModel model) {
        model.setContextName(contextName);
    }

    private static void unmapCreateContext(CreateContextDesc createContextDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (createContextDesc == null)
        {
            return;
        }

        ContextDescriptor desc = unmapCreateContextDetail(createContextDesc.getContextDetail(), unmapContext);
        CreateContextClause clause = new CreateContextClause(createContextDesc.getContextName(), desc);
        model.setCreateContext(clause);
    }

    private static ContextDescriptor unmapCreateContextDetail(ContextDetail contextDetail, StatementSpecUnMapContext unmapContext) {
        ContextDescriptor desc;
        if (contextDetail instanceof ContextDetailInitiatedTerminated) {
            ContextDetailInitiatedTerminated spec = (ContextDetailInitiatedTerminated) contextDetail;
            ContextDescriptorCondition startCondition = unmapCreateContextRangeCondition(spec.getStart(), unmapContext);
            ContextDescriptorCondition endCondition = unmapCreateContextRangeCondition(spec.getEnd(), unmapContext);
            desc = new ContextDescriptorInitiatedTerminated(startCondition, endCondition, spec.isOverlapping());
        }
        else if (contextDetail instanceof ContextDetailPartitioned) {
            ContextDetailPartitioned seg = (ContextDetailPartitioned) contextDetail;
            List<ContextDescriptorKeyedSegmentedItem> segmentedItems = new ArrayList<ContextDescriptorKeyedSegmentedItem>();
            for (ContextDetailPartitionItem item : seg.getItems()) {
                Filter filter = unmapFilter(item.getFilterSpecRaw(), unmapContext);
                segmentedItems.add(new ContextDescriptorKeyedSegmentedItem(item.getPropertyNames(), filter));
            }
            desc = new ContextDescriptorKeyedSegmented(segmentedItems);
        }
        else if (contextDetail instanceof ContextDetailCategory) {
            ContextDetailCategory category = (ContextDetailCategory) contextDetail;
            List<ContextDescriptorCategoryItem> categoryItems = new ArrayList<ContextDescriptorCategoryItem>();
            Filter filter = unmapFilter(category.getFilterSpecRaw(), unmapContext);
            for (ContextDetailCategoryItem item : category.getItems()) {
                Expression expr = unmapExpressionDeep(item.getExpression(), unmapContext);
                categoryItems.add(new ContextDescriptorCategoryItem(expr, item.getName()));
            }
            desc = new ContextDescriptorCategory(categoryItems, filter);
        }
        else if (contextDetail instanceof ContextDetailHash) {
            ContextDetailHash init = (ContextDetailHash) contextDetail;
            List<ContextDescriptorHashSegmentedItem> hashes = new ArrayList<ContextDescriptorHashSegmentedItem>();
            for (ContextDetailHashItem item : init.getItems()) {
                DotExpressionItem dot = unmapChains(new ArrayList<ExprChainedSpec>(Collections.singletonList(item.getFunction())), unmapContext, false).get(0);
                SingleRowMethodExpression dotExpression = new SingleRowMethodExpression(new ArrayList<DotExpressionItem>(Collections.singletonList(dot)));
                Filter filter = unmapFilter(item.getFilterSpecRaw(), unmapContext);
                hashes.add(new ContextDescriptorHashSegmentedItem(dotExpression, filter));
            }
            desc = new ContextDescriptorHashSegmented(hashes, init.getGranularity(), init.isPreallocate());
        }
        else {
            ContextDetailNested nested = (ContextDetailNested) contextDetail;
            List<CreateContextClause> contexts = new ArrayList<CreateContextClause>();
            for (CreateContextDesc item : nested.getContexts()) {
                ContextDescriptor detail = unmapCreateContextDetail(item.getContextDetail(), unmapContext);
                contexts.add(new CreateContextClause(item.getContextName(), detail));
            }
            desc = new ContextDescriptorNested(contexts);
        }
        return desc;
    }

    private static ContextDescriptorCondition unmapCreateContextRangeCondition(ContextDetailCondition endpoint, StatementSpecUnMapContext unmapContext) {
        if (endpoint instanceof ContextDetailConditionCrontab) {
            ContextDetailConditionCrontab crontab = (ContextDetailConditionCrontab) endpoint;
            List<Expression> crontabExpr = unmapExpressionDeep(crontab.getCrontab(), unmapContext);
            return new ContextDescriptorConditionCrontab(crontabExpr);
        }
        else if (endpoint instanceof ContextDetailConditionPattern) {
            ContextDetailConditionPattern pattern = (ContextDetailConditionPattern) endpoint;
            PatternExpr patternExpr = unmapPatternEvalDeep(pattern.getPatternRaw(), unmapContext);
            return new ContextDescriptorConditionPattern(patternExpr, pattern.isInclusive());
        }
        else if (endpoint instanceof ContextDetailConditionFilter) {
            ContextDetailConditionFilter filter = (ContextDetailConditionFilter) endpoint;
            Filter filterExpr = unmapFilter(filter.getFilterSpecRaw(), unmapContext);
            return new ContextDescriptorConditionFilter(filterExpr, filter.getOptionalFilterAsName());
        }
        else if (endpoint instanceof ContextDetailConditionTimePeriod) {
            ContextDetailConditionTimePeriod period = (ContextDetailConditionTimePeriod) endpoint;
            TimePeriodExpression expression = (TimePeriodExpression) unmapExpressionDeep(period.getTimePeriod(), unmapContext);
            return new ContextDescriptorConditionTimePeriod(expression);
        }
        throw new IllegalStateException("Unrecognized endpoint " + endpoint);
    }

    private static void unmapCreateWindow(CreateWindowDesc createWindowDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (createWindowDesc == null)
        {
            return;
        }
        Expression filter = null;
        if (createWindowDesc.getInsertFilter() != null)
        {
            filter = unmapExpressionDeep(createWindowDesc.getInsertFilter(), unmapContext);
        }

        CreateWindowClause clause = new CreateWindowClause(createWindowDesc.getWindowName(), unmapViews(createWindowDesc.getViewSpecs(), unmapContext));
        clause.setInsert(createWindowDesc.isInsert());
        clause.setInsertWhereClause(filter);
        clause.setColumns(unmapColumns(createWindowDesc.getColumns()));
        model.setCreateWindow(clause);
    }

    private static void unmapCreateIndex(CreateIndexDesc createIndexDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (createIndexDesc == null)
        {
            return;
        }
        List<CreateIndexColumn> cols = new ArrayList<CreateIndexColumn>();
        for (CreateIndexItem item : createIndexDesc.getColumns()) {
            cols.add(new CreateIndexColumn(item.getName(), CreateIndexColumnType.valueOf(item.getType().name().toUpperCase())));
        }
        model.setCreateIndex(new CreateIndexClause(createIndexDesc.getIndexName(), createIndexDesc.getWindowName(), cols, createIndexDesc.isUnique()));
    }

    private static void unmapCreateVariable(CreateVariableDesc createVariableDesc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (createVariableDesc == null)
        {
            return;
        }
        Expression assignment = null;
        if (createVariableDesc.getAssignment() != null)
        {
            assignment = unmapExpressionDeep(createVariableDesc.getAssignment(), unmapContext);
        }
        CreateVariableClause clause = new CreateVariableClause(createVariableDesc.getVariableType(), createVariableDesc.getVariableName(), assignment, createVariableDesc.isConstant());
        clause.setArray(createVariableDesc.isArray());
        model.setCreateVariable(clause);
    }

    private static void unmapCreateSchema(CreateSchemaDesc desc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (desc == null)
        {
            return;
        }
        model.setCreateSchema(unmapCreateSchemaInternal(desc, unmapContext));
    }

    private static void unmapCreateExpression(CreateExpressionDesc desc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (desc == null)
        {
            return;
        }
        CreateExpressionClause clause;
        if (desc.getExpression() != null) {
            clause = new CreateExpressionClause(unmapExpressionDeclItem(desc.getExpression(), unmapContext));
        }
        else {
            clause = new CreateExpressionClause(unmapScriptExpression(desc.getScript(), unmapContext));
        }
        model.setCreateExpression(clause);
    }

    private static CreateSchemaClause unmapCreateSchemaInternal(CreateSchemaDesc desc, StatementSpecUnMapContext unmapContext)
    {
        List<SchemaColumnDesc> columns = unmapColumns(desc.getColumns());
        CreateSchemaClause clause = new CreateSchemaClause(desc.getSchemaName(), desc.getTypes(), columns, desc.getInherits(), desc.getAssignedType().mapToSoda());
        clause.setStartTimestampPropertyName(desc.getStartTimestampProperty());
        clause.setEndTimestampPropertyName(desc.getEndTimestampProperty());
        clause.setCopyFrom(desc.getCopyFrom());
        return clause;
    }

    private static void unmapCreateGraph(CreateDataFlowDesc desc, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (desc == null)
        {
            return;
        }

        List<CreateSchemaClause> schemas = new ArrayList<CreateSchemaClause>();
        for (CreateSchemaDesc schema : desc.getSchemas()) {
            schemas.add(unmapCreateSchemaInternal(schema, unmapContext));
        }

        List<DataFlowOperator> operators = new ArrayList<DataFlowOperator>();
        for (GraphOperatorSpec spec : desc.getOperators()) {
            operators.add(unmapGraphOperator(spec, unmapContext));
        }

        CreateDataFlowClause clause = new CreateDataFlowClause(desc.getGraphName(), schemas, operators);
        model.setCreateDataFlow(clause);
    }

    private static DataFlowOperator unmapGraphOperator(GraphOperatorSpec spec, StatementSpecUnMapContext unmapContext) {
        DataFlowOperator op = new DataFlowOperator();
        op.setOperatorName(spec.getOperatorName());
        op.setAnnotations(unmapAnnotations(spec.getAnnotations()));

        List<DataFlowOperatorInput> inputs = new ArrayList<DataFlowOperatorInput>();
        for (GraphOperatorInputNamesAlias in : spec.getInput().getStreamNamesAndAliases()) {
            inputs.add(new DataFlowOperatorInput(Arrays.asList(in.getInputStreamNames()), in.getOptionalAsName()));
        }
        op.setInput(inputs);

        List<DataFlowOperatorOutput> outputs = new ArrayList<DataFlowOperatorOutput>();
        for (GraphOperatorOutputItem out : spec.getOutput().getItems()) {
            List<DataFlowOperatorOutputType> types = out.getTypeInfo().isEmpty() ? null : new ArrayList<DataFlowOperatorOutputType>(Collections.singletonList(unmapTypeInfo(out.getTypeInfo().get(0))));
            outputs.add(new DataFlowOperatorOutput(out.getStreamName(), types));
        }
        op.setOutput(outputs);

        if (spec.getDetail() != null) {
            List<DataFlowOperatorParameter> parameters = new ArrayList<DataFlowOperatorParameter>();
            for (Map.Entry<String, Object> param : spec.getDetail().getConfigs().entrySet()) {
                Object value = param.getValue();
                if (value instanceof StatementSpecRaw) {
                    value = unmapInternal((StatementSpecRaw) value, unmapContext);
                }
                if (value instanceof ExprNode) {
                    value = unmapExpressionDeep((ExprNode) value, unmapContext);
                }
                parameters.add(new DataFlowOperatorParameter(param.getKey(), value));
            }
            op.setParameters(parameters);
        }
        else {
            op.setParameters(Collections.<DataFlowOperatorParameter>emptyList());
        }

        return op;
    }

    private static DataFlowOperatorOutputType unmapTypeInfo(GraphOperatorOutputItemType typeInfo) {
        List<DataFlowOperatorOutputType> types = Collections.emptyList();
        if (typeInfo.getTypeParameters() != null && !typeInfo.getTypeParameters().isEmpty()) {
            types = new ArrayList<DataFlowOperatorOutputType>();
            for (GraphOperatorOutputItemType type : typeInfo.getTypeParameters()) {
                types.add(unmapTypeInfo(type));
            }
        }
        return new DataFlowOperatorOutputType(typeInfo.isWildcard(), typeInfo.getTypeOrClassname(), types);
    }

    private static void unmapOrderBy(List<OrderByItem> orderByList, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if ((orderByList == null) || (orderByList.size() == 0))
        {
            return;
        }

        OrderByClause clause = new OrderByClause();
        for (OrderByItem item : orderByList)
        {
            Expression expr = unmapExpressionDeep(item.getExprNode(), unmapContext);
            clause.add(expr, item.isDescending());
        }
        model.setOrderByClause(clause);
    }

    private static void unmapOutputLimit(OutputLimitSpec outputLimitSpec, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (outputLimitSpec == null)
        {
            return;
        }

        OutputLimitSelector selector = OutputLimitSelector.DEFAULT;
        if (outputLimitSpec.getDisplayLimit() == OutputLimitLimitType.FIRST)
        {
            selector = OutputLimitSelector.FIRST;
        }
        if (outputLimitSpec.getDisplayLimit() == OutputLimitLimitType.LAST)
        {
            selector = OutputLimitSelector.LAST;
        }
        if (outputLimitSpec.getDisplayLimit() == OutputLimitLimitType.SNAPSHOT)
        {
            selector = OutputLimitSelector.SNAPSHOT;
        }
        if (outputLimitSpec.getDisplayLimit() == OutputLimitLimitType.ALL)
        {
            selector = OutputLimitSelector.ALL;
        }

        OutputLimitClause clause;
        OutputLimitUnit unit = OutputLimitUnit.EVENTS;
        if (outputLimitSpec.getRateType() == OutputLimitRateType.TIME_PERIOD)
        {
            unit = OutputLimitUnit.TIME_PERIOD;
            TimePeriodExpression timePeriod = (TimePeriodExpression) unmapExpressionDeep(outputLimitSpec.getTimePeriodExpr(), unmapContext);
            clause = new OutputLimitClause(selector, timePeriod);
        }
        else if (outputLimitSpec.getRateType() == OutputLimitRateType.AFTER)
        {
            unit = OutputLimitUnit.AFTER;
            if (outputLimitSpec.getAfterTimePeriodExpr() != null)
            {
                TimePeriodExpression after = (TimePeriodExpression) unmapExpressionDeep(outputLimitSpec.getAfterTimePeriodExpr(), unmapContext);
                clause = new OutputLimitClause(OutputLimitSelector.DEFAULT, OutputLimitUnit.AFTER, after, null);
            }
            else
            {
                clause = new OutputLimitClause(OutputLimitSelector.DEFAULT, OutputLimitUnit.AFTER, null, outputLimitSpec.getAfterNumberOfEvents());
            }
        }
        else if (outputLimitSpec.getRateType() == OutputLimitRateType.WHEN_EXPRESSION)
        {
            unit = OutputLimitUnit.WHEN_EXPRESSION;
            Expression whenExpression = unmapExpressionDeep(outputLimitSpec.getWhenExpressionNode(), unmapContext);
            List<AssignmentPair> thenAssignments = new ArrayList<AssignmentPair>();
            clause = new OutputLimitClause(selector, whenExpression, thenAssignments);
            if (outputLimitSpec.getThenExpressions() != null)
            {
                for (OnTriggerSetAssignment assignment : outputLimitSpec.getThenExpressions())
                {
                    Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
                    clause.addThenAssignment(assignment.getVariableName(), expr);
                }
            }
        }
        else if (outputLimitSpec.getRateType() == OutputLimitRateType.CRONTAB)
        {
            unit = OutputLimitUnit.CRONTAB_EXPRESSION;
            List<ExprNode> timerAtExpressions = outputLimitSpec.getCrontabAtSchedule();
            List<Expression> mappedExpr = unmapExpressionDeep(timerAtExpressions, unmapContext);
            clause = new OutputLimitClause(selector, mappedExpr.toArray(new Expression[mappedExpr.size()]));
        }
        else if (outputLimitSpec.getRateType() == OutputLimitRateType.TERM)
        {
            clause = new OutputLimitClause(selector, OutputLimitUnit.CONTEXT_PARTITION_TERM);
        }
        else
        {
            clause = new OutputLimitClause(selector, outputLimitSpec.getRate(), outputLimitSpec.getVariableName(), unit);
        }

        clause.setAfterNumberOfEvents(outputLimitSpec.getAfterNumberOfEvents());
        if (outputLimitSpec.getAfterTimePeriodExpr() != null) {
            clause.setAfterTimePeriodExpression(unmapExpressionDeep(outputLimitSpec.getAfterTimePeriodExpr(), unmapContext));
        }
        clause.setAndAfterTerminate(outputLimitSpec.isAndAfterTerminate());
        if (outputLimitSpec.getAndAfterTerminateExpr() != null) {
            clause.setAndAfterTerminateAndExpr(unmapExpressionDeep(outputLimitSpec.getAndAfterTerminateExpr(), unmapContext));
        }
        if (outputLimitSpec.getAndAfterTerminateThenExpressions() != null) {
            List<AssignmentPair> thenAssignments = new ArrayList<AssignmentPair>();
            for (OnTriggerSetAssignment assignment : outputLimitSpec.getAndAfterTerminateThenExpressions()) {
                Expression expr = unmapExpressionDeep(assignment.getExpression(), unmapContext);
                thenAssignments.add(new AssignmentPair(assignment.getVariableName(), expr));
            }
            clause.setAndAfterTerminateThenAssignments(thenAssignments);
        }
        model.setOutputLimitClause(clause);
    }

    private static void unmapRowLimit(RowLimitSpec rowLimitSpec, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (rowLimitSpec == null)
        {
            return;
        }
        RowLimitClause spec = new RowLimitClause(rowLimitSpec.getNumRows(), rowLimitSpec.getOptionalOffset(),
                rowLimitSpec.getNumRowsVariable(), rowLimitSpec.getOptionalOffsetVariable());
        model.setRowLimitClause(spec);
    }

    private static void unmapForClause(ForClauseSpec spec, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if ((spec == null) || (spec.getClauses() == null) || (spec.getClauses().size() == 0))
        {
            return;
        }
        ForClause clause = new ForClause();
        for (ForClauseItemSpec itemSpec : spec.getClauses()) {
            ForClauseItem item = new ForClauseItem(ForClauseKeyword.valueOf(itemSpec.getKeyword().toUpperCase()));
            item.setExpressions(unmapExpressionDeep(itemSpec.getExpressions(), unmapContext));
            clause.getItems().add(item);
        }
        model.setForClause(clause);
    }

    private static void unmapMatchRecognize(MatchRecognizeSpec spec, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (spec == null) {
            return;
        }
        MatchRecognizeClause clause = new MatchRecognizeClause();
        clause.setPartitionExpressions(unmapExpressionDeep(spec.getPartitionByExpressions(), unmapContext));

        List<SelectClauseExpression> measures = new ArrayList<SelectClauseExpression>();
        for (MatchRecognizeMeasureItem item : spec.getMeasures()) {
            measures.add(new SelectClauseExpression(unmapExpressionDeep(item.getExpr(), unmapContext), item.getName()));
        }
        clause.setMeasures(measures);
        clause.setAll(spec.isAllMatches());
        clause.setSkipClause(MatchRecognizeSkipClause.values()[spec.getSkip().getSkip().ordinal()]);

        List<MatchRecognizeDefine> defines = new ArrayList<MatchRecognizeDefine>();
        for (MatchRecognizeDefineItem define : spec.getDefines()) {

            defines.add(new MatchRecognizeDefine(define.getIdentifier(), unmapExpressionDeep(define.getExpression(), unmapContext)));
        }
        clause.setDefines(defines);

        if (spec.getInterval() != null) {
            clause.setIntervalClause(new MatchRecognizeIntervalClause((TimePeriodExpression) unmapExpressionDeep(spec.getInterval().getTimePeriodExpr(), unmapContext)));
        }
        clause.setPattern(unmapExpressionDeepRowRegex(spec.getPattern(), unmapContext));
        model.setMatchRecognizeClause(clause);
    }

    private static void mapOrderBy(OrderByClause orderByClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (orderByClause == null)
        {
            return;
        }
        for (OrderByElement element : orderByClause.getOrderByExpressions())
        {
            ExprNode orderExpr = mapExpressionDeep(element.getExpression(), mapContext);
            OrderByItem item = new OrderByItem(orderExpr, element.isDescending());
            raw.getOrderByList().add(item);
        }
    }

    private static void mapOutputLimit(OutputLimitClause outputLimitClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (outputLimitClause == null)
        {
            return;
        }

        OutputLimitLimitType displayLimit = OutputLimitLimitType.valueOf(outputLimitClause.getSelector().toString().toUpperCase());

        OutputLimitRateType rateType;
        if (outputLimitClause.getUnit() == OutputLimitUnit.EVENTS)
        {
            rateType = OutputLimitRateType.EVENTS;
        }
        else if (outputLimitClause.getUnit() == OutputLimitUnit.TIME_PERIOD)
        {
            rateType = OutputLimitRateType.TIME_PERIOD;
        }
        else if (outputLimitClause.getUnit() == OutputLimitUnit.CRONTAB_EXPRESSION)
        {
            rateType = OutputLimitRateType.CRONTAB;
        }
        else if (outputLimitClause.getUnit() == OutputLimitUnit.WHEN_EXPRESSION)
        {
            rateType = OutputLimitRateType.WHEN_EXPRESSION;
        }
        else if (outputLimitClause.getUnit() == OutputLimitUnit.AFTER)
        {
            rateType = OutputLimitRateType.AFTER;
        }
        else if (outputLimitClause.getUnit() == OutputLimitUnit.CONTEXT_PARTITION_TERM)
        {
            rateType = OutputLimitRateType.TERM;
        }
        else
        {
            throw new IllegalArgumentException("Unknown output limit unit " + outputLimitClause.getUnit());
        }

        Double frequency = outputLimitClause.getFrequency();
        String frequencyVariable = outputLimitClause.getFrequencyVariable();

        if (frequencyVariable != null)
        {
            mapContext.setHasVariables(true);
        }

        ExprNode whenExpression = null;
        List<OnTriggerSetAssignment> assignments = null;
        if (outputLimitClause.getWhenExpression() != null)
        {
            whenExpression = mapExpressionDeep(outputLimitClause.getWhenExpression(), mapContext);

            assignments = new ArrayList<OnTriggerSetAssignment>();
            for (AssignmentPair pair : outputLimitClause.getThenAssignments())
            {
                ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
            }
        }

        List<ExprNode> timerAtExprList = null;
        if (outputLimitClause.getCrontabAtParameters() != null)
        {
            timerAtExprList = mapExpressionDeep(Arrays.asList(outputLimitClause.getCrontabAtParameters()), mapContext);
        }

        ExprTimePeriod timePeriod = null;
        if (outputLimitClause.getTimePeriodExpression() != null)
        {
            timePeriod = (ExprTimePeriod) mapExpressionDeep(outputLimitClause.getTimePeriodExpression(), mapContext);
        }

        ExprTimePeriod afterTimePeriod = null;
        if (outputLimitClause.getAfterTimePeriodExpression() != null)
        {
            afterTimePeriod = (ExprTimePeriod) mapExpressionDeep(outputLimitClause.getAfterTimePeriodExpression(), mapContext);
        }

        ExprNode andAfterTerminateAndExpr = null;
        if (outputLimitClause.getAndAfterTerminateAndExpr() != null) {
            andAfterTerminateAndExpr = mapExpressionDeep(outputLimitClause.getAndAfterTerminateAndExpr(), mapContext);
        }

        List<OnTriggerSetAssignment> afterTerminateAssignments = null;
        if (outputLimitClause.getAndAfterTerminateThenAssignments() != null) {
            afterTerminateAssignments = new ArrayList<OnTriggerSetAssignment>();
            for (AssignmentPair pair : outputLimitClause.getAndAfterTerminateThenAssignments())
            {
                ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                afterTerminateAssignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
            }
        }

        OutputLimitSpec spec = new OutputLimitSpec(frequency, frequencyVariable, rateType, displayLimit, whenExpression, assignments, timerAtExprList, timePeriod, afterTimePeriod, outputLimitClause.getAfterNumberOfEvents(), outputLimitClause.isAndAfterTerminate(), andAfterTerminateAndExpr, afterTerminateAssignments);
        raw.setOutputLimitSpec(spec);
    }

    private static void mapOnTrigger(OnClause onExpr, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (onExpr == null)
        {
            return;
        }

        if (onExpr instanceof OnDeleteClause)
        {
            OnDeleteClause onDeleteClause = (OnDeleteClause) onExpr;
            raw.setOnTriggerDesc(new OnTriggerWindowDesc(onDeleteClause.getWindowName(), onDeleteClause.getOptionalAsName(), OnTriggerType.ON_DELETE, false));
        }
        else if (onExpr instanceof OnSelectClause)
        {
            OnSelectClause onSelectClause = (OnSelectClause) onExpr;
            raw.setOnTriggerDesc(new OnTriggerWindowDesc(onSelectClause.getWindowName(), onSelectClause.getOptionalAsName(), OnTriggerType.ON_SELECT, onSelectClause.isDeleteAndSelect()));
        }
        else if (onExpr instanceof OnSetClause)
        {
            OnSetClause setClause = (OnSetClause) onExpr;
            mapContext.setHasVariables(true);
            List<OnTriggerSetAssignment> assignments = new ArrayList<OnTriggerSetAssignment>();
            for (AssignmentPair pair : setClause.getAssignments())
            {
                ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
            }
            OnTriggerSetDesc desc = new OnTriggerSetDesc(assignments);
            raw.setOnTriggerDesc(desc);
        }
        else if (onExpr instanceof OnUpdateClause)
        {
            OnUpdateClause updateClause = (OnUpdateClause) onExpr;
            List<OnTriggerSetAssignment> assignments = new ArrayList<OnTriggerSetAssignment>();
            for (AssignmentPair pair : updateClause.getAssignments())
            {
                ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
            }
            OnTriggerWindowUpdateDesc desc = new OnTriggerWindowUpdateDesc(updateClause.getWindowName(), updateClause.getOptionalAsName(), assignments);
            raw.setOnTriggerDesc(desc);
        }
        else if (onExpr instanceof OnInsertSplitStreamClause)
        {
            OnInsertSplitStreamClause splitClause = (OnInsertSplitStreamClause) onExpr;
            mapContext.setHasVariables(true);
            List<OnTriggerSplitStream> streams = new ArrayList<OnTriggerSplitStream>();
            for (OnInsertSplitStreamItem item : splitClause.getItems())
            {
                ExprNode whereClause = null;
                if (item.getWhereClause() != null)
                {
                    whereClause = mapExpressionDeep(item.getWhereClause(), mapContext);
                }

                InsertIntoDesc insertDesc = mapInsertInto(item.getInsertInto());
                SelectClauseSpecRaw selectDesc = mapSelectRaw(item.getSelectClause(), mapContext);

                streams.add(new OnTriggerSplitStream(insertDesc, selectDesc, whereClause));
            }
            OnTriggerSplitStreamDesc desc = new OnTriggerSplitStreamDesc(OnTriggerType.ON_SPLITSTREAM, splitClause.isFirst(), streams);
            raw.setOnTriggerDesc(desc);
        }
        else if (onExpr instanceof OnMergeClause)
        {
            OnMergeClause merge = (OnMergeClause) onExpr;
            List<OnTriggerMergeMatched> matcheds = new ArrayList<OnTriggerMergeMatched>();
            for (OnMergeMatchItem matchItem : merge.getMatchItems()) {
                List<OnTriggerMergeAction> actions = new ArrayList<OnTriggerMergeAction>();
                for (OnMergeMatchedAction action : matchItem.getActions())
                {
                    OnTriggerMergeAction actionItem;
                    if (action instanceof OnMergeMatchedDeleteAction) {
                        OnMergeMatchedDeleteAction delete = (OnMergeMatchedDeleteAction) action;
                        ExprNode optionalCondition = delete.getWhereClause() == null ? null : mapExpressionDeep(delete.getWhereClause(), mapContext);
                        actionItem = new OnTriggerMergeActionDelete(optionalCondition);
                    }
                    else if (action instanceof OnMergeMatchedUpdateAction) {
                        OnMergeMatchedUpdateAction update = (OnMergeMatchedUpdateAction) action;
                        List<OnTriggerSetAssignment> assignments = new ArrayList<OnTriggerSetAssignment>();
                        for (AssignmentPair pair : update.getAssignments())
                        {
                            ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
                            assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
                        }
                        ExprNode optionalCondition = update.getWhereClause() == null ? null : mapExpressionDeep(update.getWhereClause(), mapContext);
                        actionItem = new OnTriggerMergeActionUpdate(optionalCondition, assignments);
                    }
                    else if (action instanceof OnMergeMatchedInsertAction) {
                        OnMergeMatchedInsertAction insert = (OnMergeMatchedInsertAction) action;
                        List<String> columnNames = new ArrayList<String>(insert.getColumnNames());
                        List<SelectClauseElementRaw> select = mapSelectClauseElements(insert.getSelectList(), mapContext);
                        ExprNode optionalCondition = insert.getWhereClause() == null ? null : mapExpressionDeep(insert.getWhereClause(), mapContext);
                        actionItem = new OnTriggerMergeActionInsert(optionalCondition, insert.getOptionalStreamName(), columnNames, select);
                    }
                    else {
                        throw new IllegalArgumentException("Unrecognized merged action type '" + action.getClass() + "'");
                    }
                    actions.add(actionItem);
                }
                ExprNode optionalCondition = matchItem.getOptionalCondition() == null ? null : mapExpressionDeep(matchItem.getOptionalCondition(), mapContext);
                matcheds.add(new OnTriggerMergeMatched(matchItem.isMatched(), optionalCondition, actions));
            }
            OnTriggerMergeDesc mergeDesc = new OnTriggerMergeDesc(merge.getWindowName(), merge.getOptionalAsName(), matcheds);
            raw.setOnTriggerDesc(mergeDesc);
        }
        else
        {
            throw new IllegalArgumentException("Cannot map on-clause expression type : " + onExpr);
        }
    }

    private static void mapRowLimit(RowLimitClause rowLimitClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (rowLimitClause == null)
        {
            return;
        }
        if ((rowLimitClause.getNumRowsVariable() != null) ||
            (rowLimitClause.getOptionalOffsetRowsVariable() != null))
        {
            raw.setHasVariables(true);
            mapContext.getVariableNames().add(rowLimitClause.getOptionalOffsetRowsVariable());
        }
        raw.setRowLimitSpec(new RowLimitSpec(rowLimitClause.getNumRows(), rowLimitClause.getOptionalOffsetRows(),
                rowLimitClause.getNumRowsVariable(), rowLimitClause.getOptionalOffsetRowsVariable()));
    }

    private static void mapForClause(ForClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if ((clause == null) || (clause.getItems().size() == 0))
        {
            return;
        }
        raw.setForClauseSpec(new ForClauseSpec());
        for (ForClauseItem item : clause.getItems()) {
            ForClauseItemSpec specItem = new ForClauseItemSpec(item.getKeyword().getName(), mapExpressionDeep(item.getExpressions(), mapContext));
            raw.getForClauseSpec().getClauses().add(specItem);
        }
    }

    private static void mapMatchRecognize(MatchRecognizeClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        if (clause == null) {
            return;
        }
        MatchRecognizeSpec spec = new MatchRecognizeSpec();
        spec.setPartitionByExpressions(mapExpressionDeep(clause.getPartitionExpressions(), mapContext));

        List<MatchRecognizeMeasureItem> measures = new ArrayList<MatchRecognizeMeasureItem>();
        for (SelectClauseExpression item : clause.getMeasures()) {
            measures.add(new MatchRecognizeMeasureItem(mapExpressionDeep(item.getExpression(), mapContext), item.getAsName()));
        }
        spec.setMeasures(measures);
        spec.setAllMatches(clause.isAll());
        spec.setSkip(new MatchRecognizeSkip(MatchRecognizeSkipEnum.values()[clause.getSkipClause().ordinal()]));

        List<MatchRecognizeDefineItem> defines = new ArrayList<MatchRecognizeDefineItem>();
        for (MatchRecognizeDefine define : clause.getDefines()) {

            defines.add(new MatchRecognizeDefineItem(define.getName(), mapExpressionDeep(define.getExpression(), mapContext)));
        }
        spec.setDefines(defines);

        if (clause.getIntervalClause() != null) {
            ExprTimePeriod timePeriod = (ExprTimePeriod) mapExpressionDeep(clause.getIntervalClause().getExpression(), mapContext);
            try
            {
                timePeriod.validate(new ExprValidationContext(null, null, null, null, null, null, null, null, null, null, null));
            }
            catch (ExprValidationException e)
            {
                throw new RuntimeException("Error validating time-period expression: " + e.getMessage(), e);
            }
            spec.setInterval(new MatchRecognizeInterval(timePeriod));
        }
        spec.setPattern(mapExpressionDeepRowRegex(clause.getPattern(), mapContext));
        raw.setMatchRecognizeSpec(spec);
    }

    private static void mapHaving(Expression havingClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (havingClause == null)
        {
            return;
        }
        ExprNode node = mapExpressionDeep(havingClause, mapContext);
        raw.setHavingExprRootNode(node);
    }

    private static void unmapHaving(ExprNode havingExprRootNode, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (havingExprRootNode == null)
        {
            return;
        }
        Expression expr = unmapExpressionDeep(havingExprRootNode, unmapContext);
        model.setHavingClause(expr);
    }

    private static void mapGroupBy(GroupByClause groupByClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (groupByClause == null)
        {
            return;
        }
        for (Expression expr : groupByClause.getGroupByExpressions())
        {
            ExprNode node = mapExpressionDeep(expr, mapContext);
            raw.getGroupByExpressions().add(node);
        }
    }

    private static void unmapGroupBy(List<ExprNode> groupByExpressions, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (groupByExpressions.size() == 0)
        {
            return;
        }
        GroupByClause clause = new GroupByClause();
        for (ExprNode node : groupByExpressions)
        {
            Expression expr = unmapExpressionDeep(node, unmapContext);
            clause.getGroupByExpressions().add(expr);
        }
        model.setGroupByClause(clause);
    }

    private static void mapWhere(Expression whereClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (whereClause == null)
        {
            return;
        }
        ExprNode node = mapExpressionDeep(whereClause, mapContext);
        raw.setFilterExprRootNode(node);
    }

    private static void unmapWhere(ExprNode filterRootNode, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        if (filterRootNode == null)
        {
            return;
        }
        Expression expr = unmapExpressionDeep(filterRootNode, unmapContext);
        model.setWhereClause(expr);
    }

    private static void unmapFrom(List<StreamSpecRaw> streamSpecs, List<OuterJoinDesc> outerJoinDescList, EPStatementObjectModel model, StatementSpecUnMapContext unmapContext)
    {
        FromClause from = new FromClause();
        model.setFromClause(from);

        for (StreamSpecRaw stream : streamSpecs)
        {
            Stream targetStream;
            if (stream instanceof FilterStreamSpecRaw)
            {
                FilterStreamSpecRaw filterStreamSpec = (FilterStreamSpecRaw) stream;
                Filter filter = unmapFilter(filterStreamSpec.getRawFilterSpec(), unmapContext);
                FilterStream filterStream = new FilterStream(filter, filterStreamSpec.getOptionalStreamName());
                unmapStreamOpts(stream.getOptions(), filterStream);
                targetStream = filterStream;
            }
            else if (stream instanceof DBStatementStreamSpec)
            {
                DBStatementStreamSpec db = (DBStatementStreamSpec) stream;
                targetStream = new SQLStream(db.getDatabaseName(), db.getSqlWithSubsParams(), db.getOptionalStreamName(), db.getMetadataSQL());
            }
            else if (stream instanceof PatternStreamSpecRaw)
            {
                PatternStreamSpecRaw pattern = (PatternStreamSpecRaw) stream;
                PatternExpr patternExpr = unmapPatternEvalDeep(pattern.getEvalFactoryNode(), unmapContext);
                PatternStream patternStream = new PatternStream(patternExpr, pattern.getOptionalStreamName());
                unmapStreamOpts(stream.getOptions(), patternStream);
                targetStream = patternStream;
            }
            else if (stream instanceof MethodStreamSpec)
            {
                MethodStreamSpec method = (MethodStreamSpec) stream;
                MethodInvocationStream methodStream = new MethodInvocationStream(method.getClassName(), method.getMethodName(), method.getOptionalStreamName());
                for (ExprNode exprNode : method.getExpressions())
                {
                    Expression expr = unmapExpressionDeep(exprNode, unmapContext);
                    methodStream.addParameter(expr);
                }
                targetStream = methodStream;
            }
            else
            {
                throw new IllegalArgumentException("Stream modelled by " + stream.getClass() + " cannot be unmapped");
            }

            if (targetStream instanceof ProjectedStream)
            {
                ProjectedStream projStream = (ProjectedStream) targetStream;
                for (ViewSpec viewSpec : stream.getViewSpecs())
                {
                    List<Expression> viewExpressions = unmapExpressionDeep(viewSpec.getObjectParameters(), unmapContext);
                    projStream.addView(View.create(viewSpec.getObjectNamespace(), viewSpec.getObjectName(), viewExpressions));
                }
            }
            from.add(targetStream);
        }

        for (OuterJoinDesc desc : outerJoinDescList)
        {
            PropertyValueExpression left = null;
            PropertyValueExpression right = null;
            ArrayList<PropertyValueExpressionPair> additionalProperties = new ArrayList<PropertyValueExpressionPair>();

            if (desc.getOptLeftNode() != null) {
                left = (PropertyValueExpression) unmapExpressionFlat(desc.getOptLeftNode(), unmapContext);
                right = (PropertyValueExpression) unmapExpressionFlat(desc.getOptRightNode(), unmapContext);

                if (desc.getAdditionalLeftNodes() != null)
                {
                    for (int i = 0; i < desc.getAdditionalLeftNodes().length; i++)
                    {
                        ExprIdentNode leftNode = desc.getAdditionalLeftNodes()[i];
                        ExprIdentNode rightNode = desc.getAdditionalRightNodes()[i];
                        PropertyValueExpression propLeft = (PropertyValueExpression) unmapExpressionFlat(leftNode, unmapContext);
                        PropertyValueExpression propRight = (PropertyValueExpression) unmapExpressionFlat(rightNode, unmapContext);
                        additionalProperties.add(new PropertyValueExpressionPair(propLeft, propRight));
                    }
                }
            }
            from.add(new OuterJoinQualifier(desc.getOuterJoinType(), left, right, additionalProperties));
        }
    }

    private static void unmapStreamOpts(StreamSpecOptions options, ProjectedStream stream)
    {
        stream.setUnidirectional(options.isUnidirectional());
        stream.setRetainUnion(options.isRetainUnion());
        stream.setRetainIntersection(options.isRetainIntersection());
    }

    private static StreamSpecOptions mapStreamOpts(ProjectedStream stream)
    {
        return new StreamSpecOptions(stream.isUnidirectional(), stream.isRetainUnion(), stream.isRetainIntersection());
    }

    private static SelectClause unmapSelect(SelectClauseSpecRaw selectClauseSpec, SelectClauseStreamSelectorEnum selectStreamSelectorEnum, StatementSpecUnMapContext unmapContext)
    {
        SelectClause clause = SelectClause.create();
        clause.setStreamSelector(SelectClauseStreamSelectorEnum.mapFromSODA(selectStreamSelectorEnum));
        clause.addElements(unmapSelectClauseElements(selectClauseSpec.getSelectExprList(), unmapContext));
        clause.setDistinct(selectClauseSpec.isDistinct());
        return clause;
    }

    private static List<SelectClauseElement> unmapSelectClauseElements(List<SelectClauseElementRaw> selectExprList, StatementSpecUnMapContext unmapContext) {
        List<SelectClauseElement> elements = new ArrayList<SelectClauseElement>();
        for (SelectClauseElementRaw raw : selectExprList)
        {
            if (raw instanceof SelectClauseStreamRawSpec)
            {
                SelectClauseStreamRawSpec streamSpec = (SelectClauseStreamRawSpec) raw;
                elements.add(new SelectClauseStreamWildcard(streamSpec.getStreamName(), streamSpec.getOptionalAsName()));
            }
            else if (raw instanceof SelectClauseElementWildcard)
            {
                elements.add(new SelectClauseWildcard());
            }
            else if (raw instanceof SelectClauseExprRawSpec)
            {
                SelectClauseExprRawSpec rawSpec = (SelectClauseExprRawSpec) raw;
                Expression expression = unmapExpressionDeep(rawSpec.getSelectExpression(), unmapContext);
                elements.add(new SelectClauseExpression(expression, rawSpec.getOptionalAsName()));
            }
            else
            {
                throw new IllegalStateException("Unexpected select clause element typed " + raw.getClass().getName());
            }
        }
        return elements;
    }

    private static InsertIntoClause unmapInsertInto(InsertIntoDesc insertIntoDesc)
    {
        if (insertIntoDesc == null)
        {
            return null;
        }
        StreamSelector selector = SelectClauseStreamSelectorEnum.mapFromSODA(insertIntoDesc.getStreamSelector());
        return InsertIntoClause.create(insertIntoDesc.getEventTypeName(),
                        insertIntoDesc.getColumnNames().toArray(new String[insertIntoDesc.getColumnNames().size()]), selector);
    }

    private static void mapCreateContext(CreateContextClause createContext, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        if (createContext == null) {
            return;
        }

        ContextDetail detail = mapCreateContextDetail(createContext.getDescriptor(), mapContext);

        CreateContextDesc desc = new CreateContextDesc(createContext.getContextName(), detail);
        raw.setCreateContextDesc(desc);
    }

    private static ContextDetail mapCreateContextDetail(ContextDescriptor descriptor, StatementSpecMapContext mapContext) {
        ContextDetail detail;
        if (descriptor instanceof ContextDescriptorInitiatedTerminated) {
            ContextDescriptorInitiatedTerminated desc = (ContextDescriptorInitiatedTerminated) descriptor;
            ContextDetailCondition start = mapCreateContextRangeCondition(desc.getStartCondition(), mapContext);
            ContextDetailCondition end = mapCreateContextRangeCondition(desc.getEndCondition(), mapContext);
            detail = new ContextDetailInitiatedTerminated(start, end, desc.isOverlapping());
        }
        else if (descriptor instanceof ContextDescriptorKeyedSegmented) {
            ContextDescriptorKeyedSegmented seg = (ContextDescriptorKeyedSegmented) descriptor;
            List<ContextDetailPartitionItem> itemsdesc = new ArrayList<ContextDetailPartitionItem>();
            for (ContextDescriptorKeyedSegmentedItem item : seg.getItems()) {
                FilterSpecRaw rawSpec = mapFilter(item.getFilter(), mapContext);
                itemsdesc.add(new ContextDetailPartitionItem(rawSpec, item.getPropertyNames()));
            }
            detail = new ContextDetailPartitioned(itemsdesc);
        }
        else if (descriptor instanceof ContextDescriptorCategory) {
            ContextDescriptorCategory cat = (ContextDescriptorCategory) descriptor;
            FilterSpecRaw rawSpec = mapFilter(cat.getFilter(), mapContext);
            List<ContextDetailCategoryItem> itemsdesc = new ArrayList<ContextDetailCategoryItem>();
            for (ContextDescriptorCategoryItem item : cat.getItems()) {
                ExprNode expr = mapExpressionDeep(item.getExpression(), mapContext);
                itemsdesc.add(new ContextDetailCategoryItem(expr, item.getLabel()));
            }
            detail = new ContextDetailCategory(itemsdesc, rawSpec);
        }
        else if (descriptor instanceof ContextDescriptorHashSegmented) {
            ContextDescriptorHashSegmented hash = (ContextDescriptorHashSegmented) descriptor;
            List<ContextDetailHashItem> itemsdesc = new ArrayList<ContextDetailHashItem>();
            for (ContextDescriptorHashSegmentedItem item : hash.getItems()) {
                FilterSpecRaw rawSpec = mapFilter(item.getFilter(), mapContext);
                SingleRowMethodExpression singleRowMethodExpression = (SingleRowMethodExpression) item.getHashFunction();
                ExprChainedSpec func = mapChains(Collections.singletonList(singleRowMethodExpression.getChain().get(0)), mapContext).get(0);
                itemsdesc.add(new ContextDetailHashItem(func, rawSpec));
            }
            detail = new ContextDetailHash(itemsdesc, hash.getGranularity(), hash.isPreallocate());
        }
        else {
            ContextDescriptorNested nested = (ContextDescriptorNested) descriptor;
            List<CreateContextDesc> itemsdesc = new ArrayList<CreateContextDesc>();
            for (CreateContextClause item : nested.getContexts()) {
                itemsdesc.add(new CreateContextDesc(item.getContextName(), mapCreateContextDetail(item.getDescriptor(), mapContext)));
            }
            detail = new ContextDetailNested(itemsdesc);
        }
        return detail;
    }

    private static ContextDetailCondition mapCreateContextRangeCondition(ContextDescriptorCondition condition, StatementSpecMapContext mapContext) {
        if (condition instanceof ContextDescriptorConditionCrontab) {
            ContextDescriptorConditionCrontab crontab = (ContextDescriptorConditionCrontab) condition;
            List<ExprNode> expr = mapExpressionDeep(crontab.getCrontabExpressions(), mapContext);
            return new ContextDetailConditionCrontab(expr);
        }
        else if (condition instanceof ContextDescriptorConditionFilter) {
            ContextDescriptorConditionFilter filter = (ContextDescriptorConditionFilter) condition;
            FilterSpecRaw filterExpr = mapFilter(filter.getFilter(), mapContext);
            return new ContextDetailConditionFilter(filterExpr, filter.getOptionalAsName());
        }
        if (condition instanceof ContextDescriptorConditionPattern) {
            ContextDescriptorConditionPattern pattern = (ContextDescriptorConditionPattern) condition;
            EvalFactoryNode patternExpr = mapPatternEvalDeep(pattern.getPattern(), mapContext);
            return new ContextDetailConditionPattern(patternExpr, pattern.isInclusive());
        }
        if (condition instanceof ContextDescriptorConditionTimePeriod) {
            ContextDescriptorConditionTimePeriod timePeriod = (ContextDescriptorConditionTimePeriod) condition;
            ExprNode expr = mapExpressionDeep(timePeriod.getTimePeriod(), mapContext);
            return new ContextDetailConditionTimePeriod((ExprTimePeriod) expr);
        }
        throw new IllegalStateException("Unrecognized condition " + condition);
    }

    private static void mapCreateWindow(CreateWindowClause createWindow, FromClause fromClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (createWindow == null)
        {
            return;
        }

        ExprNode insertFromWhereExpr = null;
        if (createWindow.getInsertWhereClause() != null)
        {
            insertFromWhereExpr = mapExpressionDeep(createWindow.getInsertWhereClause(), mapContext);
        }
        List<ColumnDesc> columns = mapColumns(createWindow.getColumns());

        String asEventTypeName = null;
        if (fromClause != null && !fromClause.getStreams().isEmpty() && fromClause.getStreams().get(0) instanceof FilterStream) {
            asEventTypeName = ((FilterStream) fromClause.getStreams().get(0)).getFilter().getEventTypeName();
        }
        raw.setCreateWindowDesc(new CreateWindowDesc(createWindow.getWindowName(), mapViews(createWindow.getViews(), mapContext), new StreamSpecOptions(), createWindow.isInsert(), insertFromWhereExpr,  columns, asEventTypeName));
    }

    private static void mapCreateIndex(CreateIndexClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (clause == null)
        {
            return;
        }

        List<CreateIndexItem> cols = new ArrayList<CreateIndexItem>();
        for (CreateIndexColumn col : clause.getColumns()) {
            cols.add(new CreateIndexItem(col.getColumnName(), CreateIndexType.valueOf(col.getType().name().toUpperCase())));
        }

        CreateIndexDesc desc = new CreateIndexDesc(clause.isUnique(), clause.getIndexName(), clause.getWindowName(), cols);
        raw.setCreateIndexDesc(desc);
    }

    private static void mapUpdateClause(UpdateClause updateClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (updateClause == null)
        {
            return;
        }
        List<OnTriggerSetAssignment> assignments = new ArrayList<OnTriggerSetAssignment>();
        for (AssignmentPair pair : updateClause.getAssignments())
        {
            ExprNode expr = mapExpressionDeep(pair.getValue(), mapContext);
            assignments.add(new OnTriggerSetAssignment(pair.getName(), expr));
        }
        ExprNode whereClause = null;
        if (updateClause.getOptionalWhereClause() != null)
        {
            whereClause = mapExpressionDeep(updateClause.getOptionalWhereClause(), mapContext);
        }
        UpdateDesc desc = new UpdateDesc(updateClause.getOptionalAsClauseStreamName(), assignments, whereClause);
        raw.setUpdateDesc(desc);
        FilterSpecRaw filterSpecRaw = new FilterSpecRaw(updateClause.getEventType(), Collections.EMPTY_LIST, null);
        raw.getStreamSpecs().add(new FilterStreamSpecRaw(filterSpecRaw, ViewSpec.EMPTY_VIEWSPEC_ARRAY, null, new StreamSpecOptions()));
    }

    private static void mapCreateVariable(CreateVariableClause createVariable, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (createVariable == null)
        {
            return;
        }

        ExprNode assignment = null;
        if (createVariable.getOptionalAssignment() != null)
        {
            assignment = mapExpressionDeep(createVariable.getOptionalAssignment(), mapContext);
        }

        raw.setCreateVariableDesc(new CreateVariableDesc(createVariable.getVariableType(), createVariable.getVariableName(), assignment, createVariable.isConstant(), createVariable.isArray()));
    }

    private static void mapCreateSchema(CreateSchemaClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (clause == null)
        {
            return;
        }
        CreateSchemaDesc desc = mapCreateSchemaInternal(clause, raw, mapContext);
        raw.setCreateSchemaDesc(desc);
    }

    private static void mapCreateExpression(CreateExpressionClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (clause == null)
        {
            return;
        }

        CreateExpressionDesc desc;
        if (clause.getExpressionDeclaration() != null) {
            ExpressionDeclItem item = mapExpressionDeclItem(clause.getExpressionDeclaration(), mapContext);
            desc = new CreateExpressionDesc(item);
        }
        else {
            ExpressionScriptProvided item = mapScriptExpression(clause.getScriptExpression(), mapContext);
            desc = new CreateExpressionDesc(item);
        }
        raw.setCreateExpressionDesc(desc);
    }

    private static CreateSchemaDesc mapCreateSchemaInternal(CreateSchemaClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        List<ColumnDesc> columns = mapColumns(clause.getColumns());
        return new CreateSchemaDesc(clause.getSchemaName(), clause.getTypes(), columns, clause.getInherits(), CreateSchemaDesc.AssignedType.mapFrom(clause.getTypeDefinition()), clause.getStartTimestampPropertyName(), clause.getEndTimestampPropertyName(), clause.getCopyFrom());
    }

    private static void mapCreateGraph(CreateDataFlowClause clause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (clause == null)
        {
            return;
        }

        List<CreateSchemaDesc> schemas = new ArrayList<CreateSchemaDesc>();
        for (CreateSchemaClause schema : clause.getSchemas()) {
            schemas.add(mapCreateSchemaInternal(schema, raw, mapContext));
        }

        List<GraphOperatorSpec> ops = new ArrayList<GraphOperatorSpec>();
        for (DataFlowOperator op : clause.getOperators()) {
            ops.add(mapGraphOperator(op, mapContext));
        }

        CreateDataFlowDesc desc = new CreateDataFlowDesc(clause.getDataFlowName(), ops, schemas);
        raw.setCreateDataFlowDesc(desc);
    }

    private static GraphOperatorSpec mapGraphOperator(DataFlowOperator op, StatementSpecMapContext mapContext) {
        List<AnnotationDesc> annotations = mapAnnotations(op.getAnnotations());

        GraphOperatorInput input = new GraphOperatorInput();
        for (DataFlowOperatorInput in : op.getInput()) {
            input.getStreamNamesAndAliases().add(new GraphOperatorInputNamesAlias(in.getInputStreamNames().toArray(new String[in.getInputStreamNames().size()]), in.getOptionalAsName()));
        }

        GraphOperatorOutput output = new GraphOperatorOutput();
        for (DataFlowOperatorOutput out : op.getOutput()) {
            output.getItems().add(new GraphOperatorOutputItem(out.getStreamName(), mapGraphOpType(out.getTypeInfo())));
        }

        Map<String, Object> detail = new LinkedHashMap<String, Object>();
        for (DataFlowOperatorParameter entry : op.getParameters()) {
            Object value = entry.getParameterValue();
            if (value instanceof EPStatementObjectModel) {
                value = map((EPStatementObjectModel) value, mapContext);
            }
            else if (value instanceof Expression) {
                value = mapExpressionDeep((Expression) value, mapContext);
            }
            else {
                // no action
            }
            detail.put(entry.getParameterName(), value);
        }

        return new GraphOperatorSpec(op.getOperatorName(), input, output, new GraphOperatorDetail(detail), annotations);
    }

    private static List<GraphOperatorOutputItemType> mapGraphOpType(List<DataFlowOperatorOutputType> typeInfos) {
        if (typeInfos == null) {
            return Collections.emptyList();
        }
        List<GraphOperatorOutputItemType> types = new ArrayList<GraphOperatorOutputItemType>();
        for (DataFlowOperatorOutputType info : typeInfos) {
            GraphOperatorOutputItemType type = new GraphOperatorOutputItemType(info.isWildcard(), info.getTypeOrClassname(), mapGraphOpType(info.getTypeParameters()));
            types.add(type);
        }
        return types;
    }

    private static List<ColumnDesc> mapColumns(List<SchemaColumnDesc> columns) {
        if (columns == null) {
            return null;
        }
        List<ColumnDesc> result = new ArrayList<ColumnDesc>();
        for (SchemaColumnDesc col : columns) {
            result.add(new ColumnDesc(col.getName(), col.getType(), col.isArray()));
        }
        return result;
    }

    private static List<SchemaColumnDesc> unmapColumns(List<ColumnDesc> columns) {
        if (columns == null) {
            return null;
        }
        List<SchemaColumnDesc> result = new ArrayList<SchemaColumnDesc>();
        for (ColumnDesc col : columns) {
            result.add(new SchemaColumnDesc(col.getName(), col.getType(), col.isArray()));
        }
        return result;
    }

    private static InsertIntoDesc mapInsertInto(InsertIntoClause insertInto)
    {
        if (insertInto == null)
        {
            return null;
        }

        String eventTypeName = insertInto.getStreamName();
        InsertIntoDesc desc = new InsertIntoDesc(SelectClauseStreamSelectorEnum.mapFromSODA(insertInto.getStreamSelector()), eventTypeName);

        for (String name : insertInto.getColumnNames())
        {
            desc.add(name);
        }
        return desc;
    }

    private static void mapSelect(SelectClause selectClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (selectClause == null)
        {
            return;
        }
        SelectClauseSpecRaw spec = mapSelectRaw(selectClause, mapContext);
        raw.setSelectStreamDirEnum(SelectClauseStreamSelectorEnum.mapFromSODA(selectClause.getStreamSelector()));
        raw.setSelectClauseSpec(spec);
    }

    private static List<SelectClauseElementRaw> mapSelectClauseElements(List<SelectClauseElement> elements, StatementSpecMapContext mapContext) {
        List<SelectClauseElementRaw> result = new ArrayList<SelectClauseElementRaw>();
        for (SelectClauseElement element : elements)
        {
            if (element instanceof SelectClauseWildcard)
            {
                result.add(new SelectClauseElementWildcard());
            }
            else if (element instanceof SelectClauseExpression)
            {
                SelectClauseExpression selectExpr = (SelectClauseExpression) element;
                Expression expr = selectExpr.getExpression();
                ExprNode exprNode = mapExpressionDeep(expr, mapContext);
                SelectClauseExprRawSpec rawElement = new SelectClauseExprRawSpec(exprNode, selectExpr.getAsName());
                result.add(rawElement);
            }
            else if (element instanceof SelectClauseStreamWildcard)
            {
                SelectClauseStreamWildcard streamWild = (SelectClauseStreamWildcard) element;
                SelectClauseStreamRawSpec rawElement = new SelectClauseStreamRawSpec(streamWild.getStreamName(), streamWild.getOptionalColumnName());
                result.add(rawElement);
            }
        }
        return result;
    }

    private static SelectClauseSpecRaw mapSelectRaw(SelectClause selectClause, StatementSpecMapContext mapContext)
    {
        SelectClauseSpecRaw spec = new SelectClauseSpecRaw();
        spec.addAll(mapSelectClauseElements(selectClause.getSelectList(), mapContext));
        spec.setDistinct(selectClause.isDistinct());
        return spec;
    }

    private static Expression unmapExpressionDeep(ExprNode exprNode, StatementSpecUnMapContext unmapContext)
    {
        Expression parent = unmapExpressionFlat(exprNode, unmapContext);
        unmapExpressionRecursive(parent, exprNode, unmapContext);
        return parent;
    }

    private static List<ExprNode> mapExpressionDeep(List<Expression> expressions, StatementSpecMapContext mapContext)
    {
        List<ExprNode> result = new ArrayList<ExprNode>();
        if (expressions == null) {
            return result;
        }
        for (Expression expr : expressions)
        {
            if (expr == null) {
                result.add(null);
                continue;
            }
            result.add(mapExpressionDeep(expr, mapContext));
        }
        return result;
    }

    private static MatchRecognizeRegEx unmapExpressionDeepRowRegex(RowRegexExprNode exprNode, StatementSpecUnMapContext unmapContext)
    {
        MatchRecognizeRegEx parent = unmapExpressionFlatRowregex(exprNode, unmapContext);
        unmapExpressionRecursiveRowregex(parent, exprNode, unmapContext);
        return parent;
    }

    private static ExprNode mapExpressionDeep(Expression expr, StatementSpecMapContext mapContext)
    {
        ExprNode parent = mapExpressionFlat(expr, mapContext);
        mapExpressionRecursive(parent, expr, mapContext);
        return parent;
    }

    private static RowRegexExprNode mapExpressionDeepRowRegex(MatchRecognizeRegEx expr, StatementSpecMapContext mapContext)
    {
        RowRegexExprNode parent = mapExpressionFlatRowregex(expr, mapContext);
        mapExpressionRecursiveRowregex(parent, expr, mapContext);
        return parent;
    }

    private static ExprNode mapExpressionFlat(Expression expr, StatementSpecMapContext mapContext)
    {
        if (expr == null)
        {
            throw new IllegalArgumentException("Null expression parameter");
        }
        if (expr instanceof ArithmaticExpression)
        {
            ArithmaticExpression arith = (ArithmaticExpression) expr;
            return new ExprMathNode(MathArithTypeEnum.parseOperator(arith.getOperator()),
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isIntegerDivision(),
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isDivisionByZeroReturnsNull());
        }
        else if (expr instanceof PropertyValueExpression)
        {
            PropertyValueExpression prop = (PropertyValueExpression) expr;
            int indexDot = ASTFilterSpecHelper.unescapedIndexOfDot(prop.getPropertyName());
            if (indexDot != -1)
            {
                String stream = prop.getPropertyName().substring(0, indexDot);
                String property = prop.getPropertyName().substring(indexDot + 1, prop.getPropertyName().length());

                if (mapContext.getVariableService().getReader(stream) != null)
                {
                    mapContext.setHasVariables(true);
                    return new ExprVariableNodeImpl(prop.getPropertyName(), mapContext.getVariableService());
                }

                if (mapContext.getContextName() != null) {
                    com.espertech.esper.core.context.util.ContextDescriptor contextDescriptor = mapContext.getContextManagementService().getContextDescriptor(mapContext.getContextName());
                    if (contextDescriptor != null && contextDescriptor.getContextPropertyRegistry().isContextPropertyPrefix(stream)) {
                        return new ExprContextPropertyNode(property);
                    }
                }

                return new ExprIdentNodeImpl(property, stream);
            }

            if (mapContext.getVariableService().getReader(prop.getPropertyName()) != null)
            {
                mapContext.setHasVariables(true);
                return new ExprVariableNodeImpl(prop.getPropertyName(), mapContext.getVariableService());
            }

            return new ExprIdentNodeImpl(prop.getPropertyName());
        }
        else if (expr instanceof Conjunction)
        {
            return new ExprAndNodeImpl();
        }
        else if (expr instanceof Disjunction)
        {
            return new ExprOrNode();
        }
        else if (expr instanceof RelationalOpExpression)
        {
            RelationalOpExpression op = (RelationalOpExpression) expr;
            if (op.getOperator().equals("="))
            {
                return new ExprEqualsNodeImpl(false, false);
            }
            else if (op.getOperator().equals("!="))
            {
                return new ExprEqualsNodeImpl(true, false);
            }
            else if (op.getOperator().toUpperCase().trim().equals("IS"))
            {
                return new ExprEqualsNodeImpl(false, true);
            }
            else if (op.getOperator().toUpperCase().trim().equals("IS NOT"))
            {
                return new ExprEqualsNodeImpl(true, true);
            }
            else
            {
                return new ExprRelationalOpNodeImpl(RelationalOpEnum.parse(op.getOperator()));
            }
        }
        else if (expr instanceof ConstantExpression)
        {
            ConstantExpression op = (ConstantExpression) expr;
            Class constantType = null;
            if (op.getConstantType() != null) {
                try
                {
                    constantType = Class.forName(op.getConstantType());
                }
                catch (ClassNotFoundException e)
                {
                    throw new EPException("Error looking up class name '" + op.getConstantType() + "' to resolve as constant type");
                }
            }
            return new ExprConstantNodeImpl(op.getConstant(), constantType);
        }
        else if (expr instanceof ConcatExpression)
        {
            return new ExprConcatNode();
        }
        else if (expr instanceof SubqueryExpression)
        {
            SubqueryExpression sub = (SubqueryExpression) expr;
            StatementSpecRaw rawSubselect = map(sub.getModel(), mapContext);
            return new ExprSubselectRowNode(rawSubselect);
        }
        else if (expr instanceof SubqueryInExpression)
        {
            SubqueryInExpression sub = (SubqueryInExpression) expr;
            StatementSpecRaw rawSubselect = map(sub.getModel(), mapContext);
            ExprSubselectInNode inSub = new ExprSubselectInNode(rawSubselect);
            inSub.setNotIn(sub.isNotIn());
            return inSub;
        }
        else if (expr instanceof SubqueryExistsExpression)
        {
            SubqueryExistsExpression sub = (SubqueryExistsExpression) expr;
            StatementSpecRaw rawSubselect = map(sub.getModel(), mapContext);
            return new ExprSubselectExistsNode(rawSubselect);
        }
        else if (expr instanceof SubqueryQualifiedExpression)
        {
            SubqueryQualifiedExpression sub = (SubqueryQualifiedExpression) expr;
            StatementSpecRaw rawSubselect = map(sub.getModel(), mapContext);
            boolean isNot = false;
            RelationalOpEnum relop = null;
            if (sub.getOperator().equals("!="))
            {
                isNot = true;
            }
            if (sub.getOperator().equals("="))
            {
            }
            else
            {
                relop = RelationalOpEnum.parse(sub.getOperator());
            }
            return new ExprSubselectAllSomeAnyNode(rawSubselect, isNot, sub.isAll(), relop);
        }
        else if (expr instanceof CountStarProjectionExpression)
        {
            return new ExprCountNode(false, !expr.getChildren().isEmpty());
        }
        else if (expr instanceof CountProjectionExpression)
        {
            CountProjectionExpression count = (CountProjectionExpression) expr;
            return new ExprCountNode(count.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof AvgProjectionExpression)
        {
            AvgProjectionExpression avg = (AvgProjectionExpression) expr;
            return new ExprAvgNode(avg.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof SumProjectionExpression)
        {
            SumProjectionExpression avg = (SumProjectionExpression) expr;
            return new ExprSumNode(avg.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof BetweenExpression)
        {
            BetweenExpression between = (BetweenExpression) expr;
            return new ExprBetweenNodeImpl(between.isLowEndpointIncluded(), between.isHighEndpointIncluded(), between.isNotBetween());
        }
        else if (expr instanceof PriorExpression)
        {
            return new ExprPriorNode();
        }
        else if (expr instanceof PreviousExpression)
        {
            PreviousExpression prev = (PreviousExpression) expr;
            return new ExprPreviousNode(PreviousType.valueOf(prev.getType().toString()));
        }
        else if (expr instanceof StaticMethodExpression)
        {
            StaticMethodExpression method = (StaticMethodExpression) expr;
            List<ExprChainedSpec> chained = mapChains(method.getChain(), mapContext);
            chained.add(0, new ExprChainedSpec(method.getClassName(), Collections.<ExprNode>emptyList(), false));
            return new ExprDotNode(chained,
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isDuckTyping(),
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isUdfCache());
        }
        else if (expr instanceof MinProjectionExpression)
        {
            MinProjectionExpression method = (MinProjectionExpression) expr;
            return new ExprMinMaxAggrNode(method.isDistinct(), MinMaxTypeEnum.MIN, expr.getChildren().size() > 1);
        }
        else if (expr instanceof MaxProjectionExpression)
        {
            MaxProjectionExpression method = (MaxProjectionExpression) expr;
            return new ExprMinMaxAggrNode(method.isDistinct(), MinMaxTypeEnum.MAX, expr.getChildren().size() > 1);
        }
        else if (expr instanceof NotExpression)
        {
            return new ExprNotNode();
        }
        else if (expr instanceof InExpression)
        {
            InExpression inExpr = (InExpression) expr;
            return new ExprInNodeImpl(inExpr.isNotIn());
        }
        else if (expr instanceof CoalesceExpression)
        {
            return new ExprCoalesceNode();
        }
        else if (expr instanceof CaseWhenThenExpression)
        {
            return new ExprCaseNode(false);
        }
        else if (expr instanceof CaseSwitchExpression)
        {
            return new ExprCaseNode(true);
        }
        else if (expr instanceof MaxRowExpression)
        {
            return new ExprMinMaxRowNode(MinMaxTypeEnum.MAX);
        }
        else if (expr instanceof MinRowExpression)
        {
            return new ExprMinMaxRowNode(MinMaxTypeEnum.MIN);
        }
        else if (expr instanceof BitwiseOpExpression)
        {
            BitwiseOpExpression bit = (BitwiseOpExpression) expr;
            return new ExprBitWiseNode(bit.getBinaryOp());
        }
        else if (expr instanceof ArrayExpression)
        {
            return new ExprArrayNode();
        }
        else if (expr instanceof LikeExpression)
        {
            LikeExpression like = (LikeExpression) expr;
            return new ExprLikeNode(like.isNot());
        }
        else if (expr instanceof RegExpExpression)
        {
            RegExpExpression regexp = (RegExpExpression) expr;
            return new ExprRegexpNode(regexp.isNot());
        }
        else if (expr instanceof MedianProjectionExpression)
        {
            MedianProjectionExpression median = (MedianProjectionExpression) expr;
            return new ExprMedianNode(median.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof AvedevProjectionExpression)
        {
            AvedevProjectionExpression node = (AvedevProjectionExpression) expr;
            return new ExprAvedevNode(node.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof StddevProjectionExpression)
        {
            StddevProjectionExpression node = (StddevProjectionExpression) expr;
            return new ExprStddevNode(node.isDistinct(), expr.getChildren().size() > 1);
        }
        else if (expr instanceof LastEverProjectionExpression)
        {
            LastEverProjectionExpression node = (LastEverProjectionExpression) expr;
            return new ExprLastEverNode(node.isDistinct());
        }
        else if (expr instanceof FirstEverProjectionExpression)
        {
            FirstEverProjectionExpression node = (FirstEverProjectionExpression) expr;
            return new ExprFirstEverNode(node.isDistinct());
        }
        else if (expr instanceof InstanceOfExpression)
        {
            InstanceOfExpression node = (InstanceOfExpression) expr;
            return new ExprInstanceofNode(node.getTypeNames());
        }
        else if (expr instanceof TypeOfExpression)
        {
            return new ExprTypeofNode();
        }
        else if (expr instanceof CastExpression)
        {
            CastExpression node = (CastExpression) expr;
            return new ExprCastNode(node.getTypeName());
        }
        else if (expr instanceof PropertyExistsExpression)
        {
            return new ExprPropertyExistsNode();
        }
        else if (expr instanceof CurrentTimestampExpression)
        {
            return new ExprTimestampNode();
        }
        else if (expr instanceof IStreamBuiltinExpression)
        {
            return new ExprIStreamNode();
        }
        else if (expr instanceof TimePeriodExpression)
        {
            TimePeriodExpression tpe = (TimePeriodExpression) expr;
            return new ExprTimePeriodImpl(tpe.isHasYears(), tpe.isHasMonths(), tpe.isHasWeeks(), tpe.isHasDays(), tpe.isHasHours(), tpe.isHasMinutes(), tpe.isHasSeconds(), tpe.isHasMilliseconds());
        }
        else if (expr instanceof NewOperatorExpression) {
            NewOperatorExpression noe = (NewOperatorExpression) expr;
            return new ExprNewNode(noe.getColumnNames().toArray(new String[noe.getColumnNames().size()]));
        }
        else if (expr instanceof CompareListExpression)
        {
            CompareListExpression exp = (CompareListExpression) expr;
            if ((exp.getOperator().equals("=")) || (exp.getOperator().equals("!=")))
            {
                return new ExprEqualsAllAnyNode((exp.getOperator().equals("!=")), exp.isAll());
            }
            else
            {
                return new ExprRelationalOpAllAnyNode(RelationalOpEnum.parse(exp.getOperator()), exp.isAll());
            }
        }
        else if (expr instanceof SubstitutionParameterExpression)
        {
            SubstitutionParameterExpression node = (SubstitutionParameterExpression) expr;
            if (!(node.isSatisfied()))
            {
                throw new EPException("Substitution parameter value for index " + node.getIndex() + " not set, please provide a value for this parameter");
            }
            return new ExprConstantNodeImpl(node.getConstant());
        }
        else if (expr instanceof SingleRowMethodExpression) {
            SingleRowMethodExpression single = (SingleRowMethodExpression) expr;
            if ((single.getChain() == null) || (single.getChain().size() == 0)) {
                throw new IllegalArgumentException("Single row method expression requires one or more method calls");
            }
            List<ExprChainedSpec> chain = mapChains(single.getChain(), mapContext);
            String functionName = chain.get(0).getName();
            Pair<Class, EngineImportSingleRowDesc> pair;
            try
            {
                pair = mapContext.getEngineImportService().resolveSingleRow(functionName);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Function name '" + functionName + "' cannot be resolved to a single-row function: " + e.getMessage(), e);
            }
            chain.get(0).setName(pair.getSecond().getMethodName());
            return new ExprPlugInSingleRowNode(functionName, pair.getFirst(), chain, pair.getSecond());
        }
        else if (expr instanceof PlugInProjectionExpression)
        {
            PlugInProjectionExpression node = (PlugInProjectionExpression) expr;
            ExprNode exprNode = ASTAggregationHelper.tryResolveAsAggregation(mapContext.getEngineImportService(), node.isDistinct(), node.getFunctionName(), mapContext.getPlugInAggregations(), mapContext.getEngineURI());
            if (exprNode == null) {
                throw new EPException("Error resolving aggregation function named '" + node.getFunctionName() + "'");
            }
            return exprNode;
        }
        else if (expr instanceof OrderedObjectParamExpression)
        {
            OrderedObjectParamExpression order = (OrderedObjectParamExpression) expr;
            return new ExprOrderedExpr(order.isDescending());
        }
        else if (expr instanceof CrontabFrequencyExpression)
        {
            return new ExprNumberSetFrequency();
        }
        else if (expr instanceof CrontabRangeExpression)
        {
            return new ExprNumberSetRange();
        }
        else if (expr instanceof CrontabParameterSetExpression)
        {
            return new ExprNumberSetList();
        }
        else if (expr instanceof CrontabParameterExpression)
        {
            CrontabParameterExpression cronParam = (CrontabParameterExpression) expr;
            if (cronParam.getType() == ScheduleItemType.WILDCARD)
            {
                return new ExprNumberSetWildcard();
            }
            CronOperatorEnum operator;
            if (cronParam.getType() == ScheduleItemType.LASTDAY)
            {
                operator = CronOperatorEnum.LASTDAY;
            }
            else if (cronParam.getType() == ScheduleItemType.WEEKDAY)
            {
                operator = CronOperatorEnum.WEEKDAY;
            }
            else if (cronParam.getType() == ScheduleItemType.LASTWEEKDAY)
            {
                operator = CronOperatorEnum.LASTWEEKDAY;
            }
            else {
                throw new IllegalArgumentException("Cron parameter not recognized: " + cronParam.getType());
            }
            return new ExprNumberSetCronParam(operator);
        }
        else if (expr instanceof AccessProjectionExpressionBase) {
            AccessProjectionExpressionBase theBase = (AccessProjectionExpressionBase) expr;
            AggregationStateType type;
            if (expr instanceof FirstProjectionExpression) {
                type = AggregationStateType.FIRST;
            }
            else if (expr instanceof LastProjectionExpression) {
                type = AggregationStateType.LAST;
            }
            else {
                type = AggregationStateType.WINDOW;
            }

            return new ExprAggMultiFunctionLinearAccessNode(type, theBase.isWildcard(), theBase.getStreamWildcard());
        }
        else if (expr instanceof DotExpression) {
            DotExpression theBase = (DotExpression) expr;
            List<ExprChainedSpec> chain = mapChains(theBase.getChain(), mapContext);
            if (chain.size() == 1) {
                String name = chain.get(0).getName();
                ExprDeclaredNodeImpl declared = ExprDeclaredHelper.getExistsDeclaredExpr(name, chain.get(0).getParameters(), mapContext.getExpressionDeclarations().values(), mapContext.getExprDeclaredService());
                if (declared != null) {
                    return declared;
                }
                ExprNodeScript script = ExprDeclaredHelper.getExistsScript(mapContext.getConfiguration().getEngineDefaults().getScripts().getDefaultDialect(),
                        name, chain.get(0).getParameters(), mapContext.getScripts().values(), mapContext.getExprDeclaredService());
                if (script != null) {
                    return script;
                }
            }
            return new ExprDotNode(chain,
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isDuckTyping(),
                    mapContext.getConfiguration().getEngineDefaults().getExpression().isUdfCache());
        }
        else if (expr instanceof LambdaExpression) {
            LambdaExpression theBase = (LambdaExpression) expr;
            return new ExprLambdaGoesNode(new ArrayList<String>(theBase.getParameters()));
        }
        throw new IllegalArgumentException("Could not map expression node of type " + expr.getClass().getSimpleName());
    }

    private static List<Expression> unmapExpressionDeep(List<ExprNode> expressions, StatementSpecUnMapContext unmapContext)
    {
        List<Expression> result = new ArrayList<Expression>();
        if (expressions == null) {
            return result;
        }
        for (ExprNode expr : expressions)
        {
            if (expr == null) {
                result.add(null);
                continue;
            }
            result.add(unmapExpressionDeep(expr, unmapContext));
        }
        return result;
    }

    private static MatchRecognizeRegEx unmapExpressionFlatRowregex(RowRegexExprNode expr, StatementSpecUnMapContext unmapContext)
    {
        if (expr instanceof RowRegexExprNodeAlteration) {
            return new MatchRecognizeRegExAlteration();
        }
        else if (expr instanceof RowRegexExprNodeAtom) {
            RowRegexExprNodeAtom atom = (RowRegexExprNodeAtom) expr;
            return new MatchRecognizeRegExAtom(atom.getTag(), MatchRecogizePatternElementType.values()[atom.getType().ordinal()]);
        }
        else if (expr instanceof RowRegexExprNodeConcatenation) {
            return new MatchRecognizeRegExConcatenation();
        }
        else {
            RowRegexExprNodeNested nested = (RowRegexExprNodeNested) expr;
            return new MatchRecognizeRegExNested(MatchRecogizePatternElementType.values()[nested.getType().ordinal()]);
        }
    }

    private static RowRegexExprNode mapExpressionFlatRowregex(MatchRecognizeRegEx expr, StatementSpecMapContext mapContext)
    {
        if (expr instanceof MatchRecognizeRegExAlteration) {
            return new RowRegexExprNodeAlteration();
        }
        else if (expr instanceof MatchRecognizeRegExAtom) {
            MatchRecognizeRegExAtom atom = (MatchRecognizeRegExAtom) expr;
            return new RowRegexExprNodeAtom(atom.getName(), RegexNFATypeEnum.values()[atom.getType().ordinal()]);
        }
        else if (expr instanceof MatchRecognizeRegExConcatenation) {
            return new RowRegexExprNodeConcatenation();
        }
        else {
            MatchRecognizeRegExNested nested = (MatchRecognizeRegExNested) expr;
            return new RowRegexExprNodeNested(RegexNFATypeEnum.values()[nested.getType().ordinal()]);
        }
    }

    private static Expression unmapExpressionFlat(ExprNode expr, StatementSpecUnMapContext unmapContext)
    {
        if (expr instanceof ExprMathNode)
        {
            ExprMathNode math = (ExprMathNode) expr;
            return new ArithmaticExpression(math.getMathArithTypeEnum().getExpressionText());
        }
        else if (expr instanceof ExprIdentNode)
        {
            ExprIdentNode prop = (ExprIdentNode) expr;
            String propertyName = prop.getUnresolvedPropertyName();
            if (prop.getStreamOrPropertyName() != null)
            {
                propertyName = prop.getStreamOrPropertyName() + "." + prop.getUnresolvedPropertyName();
            }
            return new PropertyValueExpression(propertyName);
        }
        else if (expr instanceof ExprVariableNode)
        {
            ExprVariableNode prop = (ExprVariableNode) expr;
            String propertyName = prop.getVariableNameWithSubProp();
            return new PropertyValueExpression(propertyName);
        }
        else if (expr instanceof ExprContextPropertyNode)
        {
            ExprContextPropertyNode prop = (ExprContextPropertyNode) expr;
            return new PropertyValueExpression(ContextPropertyRegistry.CONTEXT_PREFIX + "." + prop.getPropertyName());
        }
        else if (expr instanceof ExprEqualsNode)
        {
            ExprEqualsNode equals = (ExprEqualsNode) expr;
            String operator;
            if (!equals.isIs()) {
                operator = "=";
                if (equals.isNotEquals())
                {
                    operator = "!=";
                }
            }
            else {
                operator = "is";
                if (equals.isNotEquals())
                {
                    operator = "is not";
                }
            }
            return new RelationalOpExpression(operator);
        }
        else if (expr instanceof ExprRelationalOpNode)
        {
            ExprRelationalOpNode rel = (ExprRelationalOpNode) expr;
            return new RelationalOpExpression(rel.getRelationalOpEnum().getExpressionText());
        }
        else if (expr instanceof ExprAndNode)
        {
            return new Conjunction();
        }
        else if (expr instanceof ExprOrNode)
        {
            return new Disjunction();
        }
        else if (expr instanceof ExprConstantNode)
        {
            ExprConstantNode constNode = (ExprConstantNode) expr;
            String constantType = null;
            if (constNode.getType() != null) {
                constantType = constNode.getType().getName();
            }
            return new ConstantExpression(constNode.getValue(), constantType);
        }
        else if (expr instanceof ExprConcatNode)
        {
            return new ConcatExpression();
        }
        else if (expr instanceof ExprSubselectRowNode)
        {
            ExprSubselectRowNode sub = (ExprSubselectRowNode) expr;
            StatementSpecUnMapResult unmapped = unmap(sub.getStatementSpecRaw());
            unmapContext.addAll(unmapped.getIndexedParams());
            return new SubqueryExpression(unmapped.getObjectModel());
        }
        else if (expr instanceof ExprSubselectInNode)
        {
            ExprSubselectInNode sub = (ExprSubselectInNode) expr;
            StatementSpecUnMapResult unmapped = unmap(sub.getStatementSpecRaw());
            unmapContext.addAll(unmapped.getIndexedParams());
            return new SubqueryInExpression(unmapped.getObjectModel(), sub.isNotIn());
        }
        else if (expr instanceof ExprSubselectExistsNode)
        {
            ExprSubselectExistsNode sub = (ExprSubselectExistsNode) expr;
            StatementSpecUnMapResult unmapped = unmap(sub.getStatementSpecRaw());
            unmapContext.addAll(unmapped.getIndexedParams());
            return new SubqueryExistsExpression(unmapped.getObjectModel());
        }
        else if (expr instanceof ExprSubselectAllSomeAnyNode)
        {
            ExprSubselectAllSomeAnyNode sub = (ExprSubselectAllSomeAnyNode) expr;
            StatementSpecUnMapResult unmapped = unmap(sub.getStatementSpecRaw());
            unmapContext.addAll(unmapped.getIndexedParams());
            String operator = "=";
            if (sub.isNot())
            {
                operator = "!=";
            }
            if (sub.getRelationalOp() != null)
            {
                operator = sub.getRelationalOp().getExpressionText();
            }
            return new SubqueryQualifiedExpression(unmapped.getObjectModel(), operator, sub.isAll());
        }
        else if (expr instanceof ExprCountNode)
        {
            ExprCountNode sub = (ExprCountNode) expr;
            if (sub.getChildNodes().length == 0)
            {
                return new CountStarProjectionExpression();
            }
            else
            {
                return new CountProjectionExpression(sub.isDistinct());
            }
        }
        else if (expr instanceof ExprAvgNode)
        {
            ExprAvgNode sub = (ExprAvgNode) expr;
            return new AvgProjectionExpression(sub.isDistinct());
        }
        else if (expr instanceof ExprSumNode)
        {
            ExprSumNode sub = (ExprSumNode) expr;
            return new SumProjectionExpression(sub.isDistinct());
        }
        else if (expr instanceof ExprBetweenNode)
        {
            ExprBetweenNode between = (ExprBetweenNode) expr;
            return new BetweenExpression(between.isLowEndpointIncluded(), between.isHighEndpointIncluded(), between.isNotBetween());
        }
        else if (expr instanceof ExprPriorNode)
        {
            return new PriorExpression();
        }
        else if (expr instanceof ExprRateAggNode)
        {
            return new PlugInProjectionExpression("rate", false);
        }
        else if (expr instanceof ExprNthAggNode)
        {
            return new PlugInProjectionExpression("nth", false);
        }
        else if (expr instanceof ExprLeavingAggNode)
        {
            return new PlugInProjectionExpression("leaving", false);
        }
        else if (expr instanceof ExprAggMultiFunctionSortedMinMaxByNode)
        {
            ExprAggMultiFunctionSortedMinMaxByNode node = (ExprAggMultiFunctionSortedMinMaxByNode) expr;
            return new PlugInProjectionExpression(node.getAggregationFunctionName(), false);
        }
        else if (expr instanceof ExprPreviousNode)
        {
            ExprPreviousNode prev = (ExprPreviousNode) expr;
            PreviousExpression result = new PreviousExpression();
            result.setType(PreviousExpressionType.valueOf(prev.getPreviousType().toString()));
            return result;
        }
        else if (expr instanceof ExprMinMaxAggrNode)
        {
            ExprMinMaxAggrNode node = (ExprMinMaxAggrNode) expr;
            if (node.getMinMaxTypeEnum() == MinMaxTypeEnum.MIN)
            {
                return new MinProjectionExpression(node.isDistinct());
            }
            else
            {
                return new MaxProjectionExpression(node.isDistinct());
            }
        }
        else if (expr instanceof ExprNotNode)
        {
            return new NotExpression();
        }
        else if (expr instanceof ExprInNode)
        {
            ExprInNode inExpr = (ExprInNode) expr;
            return new InExpression(inExpr.isNotIn());
        }
        else if (expr instanceof ExprCoalesceNode)
        {
            return new CoalesceExpression();
        }
        else if (expr instanceof ExprCaseNode)
        {
            ExprCaseNode mycase = (ExprCaseNode) expr;
            if (mycase.isCase2())
            {
                return new CaseSwitchExpression();
            }
            else
            {
                return new CaseWhenThenExpression();
            }
        }
        else if (expr instanceof ExprMinMaxRowNode)
        {
            ExprMinMaxRowNode node = (ExprMinMaxRowNode) expr;
            if (node.getMinMaxTypeEnum() == MinMaxTypeEnum.MAX)
            {
                return new MaxRowExpression();
            }
            return new MinRowExpression();
        }
        else if (expr instanceof ExprBitWiseNode)
        {
            ExprBitWiseNode node = (ExprBitWiseNode) expr;
            return new BitwiseOpExpression(node.getBitWiseOpEnum());
        }
        else if (expr instanceof ExprArrayNode)
        {
            return new ArrayExpression();
        }
        else if (expr instanceof ExprLikeNode)
        {
            ExprLikeNode exprLikeNode = (ExprLikeNode) expr;
            return new LikeExpression(exprLikeNode.isNot());
        }
        else if (expr instanceof ExprRegexpNode)
        {
            ExprRegexpNode exprRegexNode = (ExprRegexpNode) expr;
            return new RegExpExpression(exprRegexNode.isNot());
        }
        else if (expr instanceof ExprMedianNode)
        {
            ExprMedianNode median = (ExprMedianNode) expr;
            return new MedianProjectionExpression(median.isDistinct());
        }
        else if (expr instanceof ExprLastEverNode)
        {
            ExprLastEverNode last = (ExprLastEverNode) expr;
            return new LastEverProjectionExpression(last.isDistinct());
        }
        else if (expr instanceof ExprFirstEverNode)
        {
            ExprFirstEverNode first = (ExprFirstEverNode) expr;
            return new FirstEverProjectionExpression(first.isDistinct());
        }
        else if (expr instanceof ExprAvedevNode)
        {
            ExprAvedevNode node = (ExprAvedevNode) expr;
            return new AvedevProjectionExpression(node.isDistinct());
        }
        else if (expr instanceof ExprStddevNode)
        {
            ExprStddevNode node = (ExprStddevNode) expr;
            return new StddevProjectionExpression(node.isDistinct());
        }
        else if (expr instanceof ExprPlugInAggFunctionNode)
        {
            ExprPlugInAggFunctionNode node = (ExprPlugInAggFunctionNode) expr;
            return new PlugInProjectionExpression(node.getAggregationFunctionName(), node.isDistinct());
        }
        else if (expr instanceof ExprPlugInAggFunctionFactoryNode)
        {
            ExprPlugInAggFunctionFactoryNode node = (ExprPlugInAggFunctionFactoryNode) expr;
            return new PlugInProjectionExpression(node.getAggregationFunctionName(), node.isDistinct());
        }
        else if (expr instanceof ExprPlugInAggMultiFunctionNode)
        {
            ExprPlugInAggMultiFunctionNode node = (ExprPlugInAggMultiFunctionNode) expr;
            return new PlugInProjectionExpression(node.getAggregationFunctionName(), node.isDistinct());
        }
        else if (expr instanceof ExprPlugInSingleRowNode)
        {
            ExprPlugInSingleRowNode node = (ExprPlugInSingleRowNode) expr;
            List<DotExpressionItem> chain = unmapChains(node.getChainSpec(), unmapContext, false);
            chain.get(0).setName(node.getFunctionName());  // starts with actual function name not mapped on
            return new SingleRowMethodExpression(chain);
        }
        else if (expr instanceof ExprInstanceofNode)
        {
            ExprInstanceofNode node = (ExprInstanceofNode) expr;
            return new InstanceOfExpression(node.getClassIdentifiers());
        }
        else if (expr instanceof ExprTypeofNode)
        {
            return new TypeOfExpression();
        }
        else if (expr instanceof ExprCastNode)
        {
            ExprCastNode node = (ExprCastNode) expr;
            return new CastExpression(node.getClassIdentifier());
        }
        else if (expr instanceof ExprPropertyExistsNode)
        {
            return new PropertyExistsExpression();
        }
        else if (expr instanceof ExprTimestampNode)
        {
            return new CurrentTimestampExpression();
        }
        else if (expr instanceof ExprIStreamNode)
        {
            return new IStreamBuiltinExpression();
        }
        else if (expr instanceof ExprSubstitutionNode)
        {
            ExprSubstitutionNode node = (ExprSubstitutionNode) expr;
            SubstitutionParameterExpression subParam = new SubstitutionParameterExpression(node.getIndex());
            unmapContext.add(node.getIndex(), subParam);
            return subParam;
        }
        else if (expr instanceof ExprTimePeriod)
        {
            ExprTimePeriod node = (ExprTimePeriod) expr;
            return new TimePeriodExpression(node.isHasYear(), node.isHasMonth(), node.isHasWeek(), node.isHasDay(), node.isHasHour(), node.isHasMinute(), node.isHasSecond(), node.isHasMillisecond());
        }
        else if (expr instanceof ExprNumberSetWildcard)
        {
            return new CrontabParameterExpression(ScheduleItemType.WILDCARD);
        }
        else if (expr instanceof ExprNumberSetFrequency)
        {
            return new CrontabFrequencyExpression();
        }
        else if (expr instanceof ExprNumberSetRange)
        {
            return new CrontabRangeExpression();
        }
        else if (expr instanceof ExprNumberSetList)
        {
            return new CrontabParameterSetExpression();
        }
        else if (expr instanceof ExprNewNode)
        {
            ExprNewNode newNode = (ExprNewNode) expr;
            return new NewOperatorExpression(new ArrayList<String>(Arrays.asList(newNode.getColumnNames())));
        }
        else if (expr instanceof ExprOrderedExpr)
        {
            ExprOrderedExpr order = (ExprOrderedExpr) expr;
            return new OrderedObjectParamExpression(order.isDescending());
        }
        else if (expr instanceof ExprEqualsAllAnyNode)
        {
            ExprEqualsAllAnyNode node = (ExprEqualsAllAnyNode) expr;
            String operator = node.isNot() ? "!=" : "=";
            return new CompareListExpression(node.isAll(), operator);
        }
        else if (expr instanceof ExprRelationalOpAllAnyNode)
        {
            ExprRelationalOpAllAnyNode node = (ExprRelationalOpAllAnyNode) expr;
            return new CompareListExpression(node.isAll(), node.getRelationalOpEnum().getExpressionText());
        }
        else if (expr instanceof ExprNumberSetCronParam)
        {
            ExprNumberSetCronParam cronParam = (ExprNumberSetCronParam) expr;
            ScheduleItemType type;
            if (cronParam.getCronOperator() == CronOperatorEnum.LASTDAY)
            {
                type = ScheduleItemType.LASTDAY;
            }
            else if (cronParam.getCronOperator() == CronOperatorEnum.LASTWEEKDAY)
            {
                type = ScheduleItemType.LASTWEEKDAY;
            }
            else if (cronParam.getCronOperator() == CronOperatorEnum.WEEKDAY)
            {
                type = ScheduleItemType.WEEKDAY;
            }
            else {
                throw new IllegalArgumentException("Cron parameter not recognized: " + cronParam.getCronOperator());
            }
            return new CrontabParameterExpression(type);
        }
        else if (expr instanceof ExprAggMultiFunctionLinearAccessNode)
        {
            ExprAggMultiFunctionLinearAccessNode accessNode = (ExprAggMultiFunctionLinearAccessNode) expr;
            AccessProjectionExpressionBase ape;
            if (accessNode.getStateType() == AggregationStateType.FIRST) {
                ape = new FirstProjectionExpression();
            }
            else if (accessNode.getStateType() == AggregationStateType.WINDOW) {
                ape = new WindowProjectionExpression();
            }
            else {
                ape = new LastProjectionExpression();
            }
            ape.setWildcard(accessNode.isWildcard());
            ape.setStreamWildcard(accessNode.getStreamWildcard());
            return ape;
        }
        else if (expr instanceof ExprDotNode)
        {
            ExprDotNode dotNode = (ExprDotNode) expr;
            DotExpression dotExpr = new DotExpression();
            for (ExprChainedSpec chain : dotNode.getChainSpec()) {
                dotExpr.add(chain.getName(), unmapExpressionDeep(chain.getParameters(), unmapContext), chain.isProperty());
            }
            return dotExpr;
        }
        else if (expr instanceof ExprDeclaredNode)
        {
            ExprDeclaredNode declNode = (ExprDeclaredNode) expr;
            DotExpression dotExpr = new DotExpression();
            dotExpr.add(declNode.getPrototype().getName(),
                    unmapExpressionDeep(declNode.getChainParameters(), unmapContext));
            return dotExpr;
        }
        else if (expr instanceof ExprLambdaGoesNode)
        {
            ExprLambdaGoesNode lambdaNode = (ExprLambdaGoesNode) expr;
            LambdaExpression lambdaExpr = new LambdaExpression(new ArrayList<String>(lambdaNode.getGoesToNames()));
            return lambdaExpr;
        }
        else if (expr instanceof ExprNodeScript) {
            ExprNodeScript scriptNode = (ExprNodeScript) expr;
            DotExpression dotExpr = new DotExpression();
            dotExpr.add(scriptNode.getScript().getName(), unmapExpressionDeep(scriptNode.getParameters(), unmapContext));
            return dotExpr;
        }
        throw new IllegalArgumentException("Could not map expression node of type " + expr.getClass().getSimpleName());
    }

    private static void unmapExpressionRecursive(Expression parent, ExprNode expr, StatementSpecUnMapContext unmapContext)
    {
        for (ExprNode child : expr.getChildNodes())
        {
            Expression result = unmapExpressionFlat(child, unmapContext);
            parent.getChildren().add(result);
            unmapExpressionRecursive(result, child, unmapContext);
        }
    }

    private static void unmapExpressionRecursiveRowregex(MatchRecognizeRegEx parent, RowRegexExprNode expr, StatementSpecUnMapContext unmapContext)
    {
        for (RowRegexExprNode child : expr.getChildNodes())
        {
            MatchRecognizeRegEx result = unmapExpressionFlatRowregex(child, unmapContext);
            parent.getChildren().add(result);
            unmapExpressionRecursiveRowregex(result, child, unmapContext);
        }
    }

    private static void mapExpressionRecursive(ExprNode parent, Expression expr, StatementSpecMapContext mapContext)
    {
        for (Expression child : expr.getChildren())
        {
            ExprNode result = mapExpressionFlat(child, mapContext);
            parent.addChildNode(result);
            mapExpressionRecursive(result, child, mapContext);
        }
    }

    private static void mapExpressionRecursiveRowregex(RowRegexExprNode parent, MatchRecognizeRegEx expr, StatementSpecMapContext mapContext)
    {
        for (MatchRecognizeRegEx child : expr.getChildren())
        {
            RowRegexExprNode result = mapExpressionFlatRowregex(child, mapContext);
            parent.addChildNode(result);
            mapExpressionRecursiveRowregex(result, child, mapContext);
        }
    }

    private static void mapFrom(FromClause fromClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if (fromClause == null)
        {
            return;
        }

        for (Stream stream : fromClause.getStreams())
        {
            StreamSpecRaw spec;

            ViewSpec[] views = ViewSpec.EMPTY_VIEWSPEC_ARRAY;
            if (stream instanceof ProjectedStream)
            {
                ProjectedStream projectedStream = (ProjectedStream) stream;
                views = ViewSpec.toArray(mapViews(projectedStream.getViews(), mapContext));
            }

            if (stream instanceof FilterStream)
            {
                FilterStream filterStream = (FilterStream) stream;
                FilterSpecRaw filterSpecRaw = mapFilter(filterStream.getFilter(), mapContext);
                StreamSpecOptions options = mapStreamOpts(filterStream);
                spec = new FilterStreamSpecRaw(filterSpecRaw, views, filterStream.getStreamName(), options);
            }
            else if (stream instanceof SQLStream)
            {
                SQLStream sqlStream = (SQLStream) stream;
                spec = new DBStatementStreamSpec(sqlStream.getStreamName(), views,
                        sqlStream.getDatabaseName(), sqlStream.getSqlWithSubsParams(), sqlStream.getOptionalMetadataSQL());
            }
            else if (stream instanceof PatternStream)
            {
                PatternStream patternStream = (PatternStream) stream;
                EvalFactoryNode child = mapPatternEvalDeep(patternStream.getExpression(), mapContext);
                StreamSpecOptions options = mapStreamOpts(patternStream);
                spec = new PatternStreamSpecRaw(child, Collections.<EvalFactoryNode, String>emptyMap(), views, patternStream.getStreamName(), options);
            }
            else if (stream instanceof MethodInvocationStream)
            {
                MethodInvocationStream methodStream = (MethodInvocationStream) stream;
                List<ExprNode> expressions = new ArrayList<ExprNode>();
                for (Expression expr : methodStream.getParameterExpressions())
                {
                    ExprNode exprNode = mapExpressionDeep(expr, mapContext);
                    expressions.add(exprNode);
                }

                spec = new MethodStreamSpec(methodStream.getStreamName(), views, "method",
                        methodStream.getClassName(), methodStream.getMethodName(), expressions);
            }
            else
            {
                throw new IllegalArgumentException("Could not map from stream " + stream + " to an internal representation");
            }

            raw.getStreamSpecs().add(spec);
        }

        for (OuterJoinQualifier qualifier : fromClause.getOuterJoinQualifiers())
        {
            ExprIdentNode left = null;
            ExprIdentNode right = null;
            ExprIdentNode[] additionalLeft = null;
            ExprIdentNode[] additionalRight = null;

            if (qualifier.getLeft() != null) {

                left = (ExprIdentNode) mapExpressionFlat(qualifier.getLeft(), mapContext);
                right = (ExprIdentNode) mapExpressionFlat(qualifier.getRight(), mapContext);

                if (qualifier.getAdditionalProperties().size() != 0)
                {
                    additionalLeft = new ExprIdentNode[qualifier.getAdditionalProperties().size()];
                    additionalRight = new ExprIdentNode[qualifier.getAdditionalProperties().size()];
                    int count = 0;
                    for (PropertyValueExpressionPair pair : qualifier.getAdditionalProperties())
                    {
                        additionalLeft[count] = (ExprIdentNode) mapExpressionFlat(pair.getLeft(), mapContext);
                        additionalRight[count] = (ExprIdentNode) mapExpressionFlat(pair.getRight(), mapContext);
                        count++;
                    }
                }
            }

            raw.getOuterJoinDescList().add(new OuterJoinDesc(qualifier.getType(), left, right, additionalLeft, additionalRight));
        }
    }

    private static List<ViewSpec> mapViews(List<View> views, StatementSpecMapContext mapContext)
    {
        List<ViewSpec> viewSpecs = new ArrayList<ViewSpec>();
        for (View view : views)
        {
            List<ExprNode> viewExpressions = mapExpressionDeep(view.getParameters(), mapContext);
            viewSpecs.add(new ViewSpec(view.getNamespace(), view.getName(), viewExpressions));
        }
        return viewSpecs;
    }

    private static List<View> unmapViews(List<ViewSpec> viewSpecs, StatementSpecUnMapContext unmapContext)
    {
        List<View> views = new ArrayList<View>();
        for (ViewSpec viewSpec : viewSpecs)
        {
            List<Expression> viewExpressions = unmapExpressionDeep(viewSpec.getObjectParameters(), unmapContext);
            views.add(View.create(viewSpec.getObjectNamespace(), viewSpec.getObjectName(), viewExpressions));
        }
        return views;
    }

    private static EvalFactoryNode mapPatternEvalFlat(PatternExpr eval, StatementSpecMapContext mapContext)
    {
        if (eval == null)
        {
            throw new IllegalArgumentException("Null expression parameter");
        }
        if (eval instanceof PatternAndExpr)
        {
            return mapContext.getPatternNodeFactory().makeAndNode();
        }
        else if (eval instanceof PatternOrExpr)
        {
            return mapContext.getPatternNodeFactory().makeOrNode();
        }
        else if (eval instanceof PatternFollowedByExpr)
        {
            PatternFollowedByExpr fb = (PatternFollowedByExpr) eval;
            List<ExprNode> maxExpr = mapExpressionDeep(fb.getOptionalMaxPerSubexpression(), mapContext);
            return mapContext.getPatternNodeFactory().makeFollowedByNode(maxExpr, mapContext.getConfiguration().getEngineDefaults().getPatterns().getMaxSubexpressions() != null);
        }
        else if (eval instanceof PatternEveryExpr)
        {
            return mapContext.getPatternNodeFactory().makeEveryNode();
        }
        else if (eval instanceof PatternFilterExpr)
        {
            PatternFilterExpr filterExpr = (PatternFilterExpr) eval;
            FilterSpecRaw filterSpec = mapFilter(filterExpr.getFilter(), mapContext);
            return mapContext.getPatternNodeFactory().makeFilterNode(filterSpec, filterExpr.getTagName(), filterExpr.getOptionalConsumptionLevel());
        }
        else if (eval instanceof PatternObserverExpr)
        {
            PatternObserverExpr observer = (PatternObserverExpr) eval;
            List<ExprNode> expressions = mapExpressionDeep(observer.getParameters(), mapContext);
            return mapContext.getPatternNodeFactory().makeObserverNode(new PatternObserverSpec(observer.getNamespace(), observer.getName(), expressions));
        }
        else if (eval instanceof PatternGuardExpr)
        {
            PatternGuardExpr guard = (PatternGuardExpr) eval;
            List<ExprNode> expressions = mapExpressionDeep(guard.getParameters(), mapContext);
            return mapContext.getPatternNodeFactory().makeGuardNode(new PatternGuardSpec(guard.getNamespace(), guard.getName(), expressions));
        }
        else if (eval instanceof PatternNotExpr)
        {
            return mapContext.getPatternNodeFactory().makeNotNode();
        }
        else if (eval instanceof PatternMatchUntilExpr)
        {
            PatternMatchUntilExpr until = (PatternMatchUntilExpr) eval;
            ExprNode low = until.getLow() != null ? mapExpressionDeep(until.getLow(), mapContext) : null;
            ExprNode high = until.getHigh() != null ? mapExpressionDeep(until.getHigh(), mapContext) : null;
            return mapContext.getPatternNodeFactory().makeMatchUntilNode(low, high);
        }
        else if (eval instanceof PatternEveryDistinctExpr)
        {
            PatternEveryDistinctExpr everyDist = (PatternEveryDistinctExpr) eval;
            List<ExprNode> expressions = mapExpressionDeep(everyDist.getExpressions(), mapContext);
            return mapContext.getPatternNodeFactory().makeEveryDistinctNode(expressions);
        }
        throw new IllegalArgumentException("Could not map pattern expression node of type " + eval.getClass().getSimpleName());
    }

    private static PatternExpr unmapPatternEvalFlat(EvalFactoryNode eval, StatementSpecUnMapContext unmapContext)
    {
        if (eval instanceof EvalAndFactoryNode)
        {
            return new PatternAndExpr();
        }
        else if (eval instanceof EvalOrFactoryNode)
        {
            return new PatternOrExpr();
        }
        else if (eval instanceof EvalFollowedByFactoryNode)
        {
            EvalFollowedByFactoryNode fb = (EvalFollowedByFactoryNode) eval;
            List<Expression> expressions = unmapExpressionDeep(fb.getOptionalMaxExpressions(), unmapContext);
            return new PatternFollowedByExpr(expressions);
        }
        else if (eval instanceof EvalEveryFactoryNode)
        {
            return new PatternEveryExpr();
        }
        else if (eval instanceof EvalNotFactoryNode)
        {
            return new PatternNotExpr();
        }
        else if (eval instanceof EvalFilterFactoryNode)
        {
            EvalFilterFactoryNode filterNode = (EvalFilterFactoryNode) eval;
            Filter filter = unmapFilter(filterNode.getRawFilterSpec(), unmapContext);
            PatternFilterExpr expr = new PatternFilterExpr(filter, filterNode.getEventAsName());
            expr.setOptionalConsumptionLevel(filterNode.getConsumptionLevel());
            return expr;
        }
        else if (eval instanceof EvalObserverFactoryNode)
        {
            EvalObserverFactoryNode observerNode = (EvalObserverFactoryNode) eval;
            List<Expression> expressions = unmapExpressionDeep(observerNode.getPatternObserverSpec().getObjectParameters(), unmapContext);
            return new PatternObserverExpr(observerNode.getPatternObserverSpec().getObjectNamespace(),
                    observerNode.getPatternObserverSpec().getObjectName(), expressions);
        }
        else if (eval instanceof EvalGuardFactoryNode)
        {
            EvalGuardFactoryNode guardNode = (EvalGuardFactoryNode) eval;
            List<Expression> expressions = unmapExpressionDeep(guardNode.getPatternGuardSpec().getObjectParameters(), unmapContext);
            return new PatternGuardExpr(guardNode.getPatternGuardSpec().getObjectNamespace(),
                    guardNode.getPatternGuardSpec().getObjectName(), expressions);
        }
        else if (eval instanceof EvalMatchUntilFactoryNode)
        {
            EvalMatchUntilFactoryNode matchUntilNode = (EvalMatchUntilFactoryNode) eval;
            Expression low;
            Expression high;

            if (matchUntilNode.getLowerBounds() == matchUntilNode.getUpperBounds() && matchUntilNode.getLowerBounds() != null) {
                low = high = unmapExpressionDeep(matchUntilNode.getLowerBounds(), unmapContext);
            }
            else {
                low = matchUntilNode.getLowerBounds() != null ? unmapExpressionDeep(matchUntilNode.getLowerBounds(), unmapContext) : null;
                high = matchUntilNode.getUpperBounds() != null ? unmapExpressionDeep(matchUntilNode.getUpperBounds(), unmapContext) : null;
            }
            return new PatternMatchUntilExpr(low, high);
        }
        else if (eval instanceof EvalEveryDistinctFactoryNode)
        {
            EvalEveryDistinctFactoryNode everyDistinctNode = (EvalEveryDistinctFactoryNode) eval;
            List<Expression> expressions = unmapExpressionDeep(everyDistinctNode.getExpressions(), unmapContext);
            return new PatternEveryDistinctExpr(expressions);
        }
        else if (eval instanceof EvalAuditFactoryNode)
        {
            return null;
        }
        throw new IllegalArgumentException("Could not map pattern expression node of type " + eval.getClass().getSimpleName());
    }

    private static void unmapPatternEvalRecursive(PatternExpr parent, EvalFactoryNode eval, StatementSpecUnMapContext unmapContext)
    {
        for (EvalFactoryNode child : eval.getChildNodes())
        {
            PatternExpr result = unmapPatternEvalFlat(child, unmapContext);
            parent.getChildren().add(result);
            unmapPatternEvalRecursive(result, child, unmapContext);
        }
    }

    private static void mapPatternEvalRecursive(EvalFactoryNode parent, PatternExpr expr, StatementSpecMapContext mapContext)
    {
        for (PatternExpr child : expr.getChildren())
        {
            EvalFactoryNode result = mapPatternEvalFlat(child, mapContext);
            parent.addChildNode(result);
            mapPatternEvalRecursive(result, child, mapContext);
        }
    }

    private static PatternExpr unmapPatternEvalDeep(EvalFactoryNode exprNode, StatementSpecUnMapContext unmapContext)
    {
        PatternExpr parent = unmapPatternEvalFlat(exprNode, unmapContext);
        unmapPatternEvalRecursive(parent, exprNode, unmapContext);
        return parent;
    }

    private static EvalFactoryNode mapPatternEvalDeep(PatternExpr expr, StatementSpecMapContext mapContext)
    {
        EvalFactoryNode parent = mapPatternEvalFlat(expr, mapContext);
        mapPatternEvalRecursive(parent, expr, mapContext);
        return parent;
    }

    private static FilterSpecRaw mapFilter(Filter filter, StatementSpecMapContext mapContext)
    {
        List<ExprNode> expr = new ArrayList<ExprNode>();
        if (filter.getFilter() != null)
        {
            ExprNode exprNode = mapExpressionDeep(filter.getFilter(), mapContext);
            expr.add(exprNode);
        }

        PropertyEvalSpec evalSpec = null;
        if (filter.getOptionalPropertySelects() != null)
        {
            evalSpec = new PropertyEvalSpec();
            for (ContainedEventSelect propertySelect : filter.getOptionalPropertySelects())
            {
                SelectClauseSpecRaw selectSpec = null;
                if (propertySelect.getSelectClause() != null) {
                    selectSpec = mapSelectRaw(propertySelect.getSelectClause(), mapContext);
                }

                ExprNode exprNodeWhere = null;
                if (propertySelect.getWhereClause() != null) {
                    exprNodeWhere = mapExpressionDeep(propertySelect.getWhereClause(), mapContext);
                }

                ExprNode splitterExpr = null;
                if (propertySelect.getSplitExpression() != null) {
                    splitterExpr = mapExpressionDeep(propertySelect.getSplitExpression(), mapContext);
                }

                evalSpec.add(new PropertyEvalAtom(splitterExpr, propertySelect.getOptionalSplitExpressionTypeName(), propertySelect.getOptionalAsName(), selectSpec, exprNodeWhere));
            }
        }

        return new FilterSpecRaw(filter.getEventTypeName(), expr, evalSpec);
    }

    private static Filter unmapFilter(FilterSpecRaw filter, StatementSpecUnMapContext unmapContext)
    {
        Expression expr = null;
        if (filter.getFilterExpressions().size() > 1)
        {
            expr = new Conjunction();
            for (ExprNode exprNode : filter.getFilterExpressions())
            {
                Expression expression = unmapExpressionDeep(exprNode, unmapContext);
                expr.getChildren().add(expression);
            }
        }
        else if (filter.getFilterExpressions().size() == 1)
        {
            expr = unmapExpressionDeep(filter.getFilterExpressions().get(0), unmapContext);
        }

        Filter filterDef = new Filter(filter.getEventTypeName(), expr);

        if (filter.getOptionalPropertyEvalSpec() != null)
        {
            List<ContainedEventSelect> propertySelects = new ArrayList<ContainedEventSelect>();
            for (PropertyEvalAtom atom : filter.getOptionalPropertyEvalSpec().getAtoms())
            {
                SelectClause selectClause = null;
                if (atom.getOptionalSelectClause() != null && !atom.getOptionalSelectClause().getSelectExprList().isEmpty()) {
                    selectClause = unmapSelect(atom.getOptionalSelectClause(), SelectClauseStreamSelectorEnum.ISTREAM_ONLY, unmapContext);
                }

                Expression filterExpression = null;
                if (atom.getOptionalWhereClause() != null) {
                    filterExpression = unmapExpressionDeep(atom.getOptionalWhereClause(), unmapContext);
                }

                Expression splitExpression = unmapExpressionDeep(atom.getSplitterExpression(), unmapContext);

                ContainedEventSelect contained = new ContainedEventSelect(splitExpression);
                contained.setOptionalSplitExpressionTypeName(atom.getOptionalResultEventType());
                contained.setSelectClause(selectClause);
                contained.setWhereClause(filterExpression);
                contained.setOptionalAsName(atom.getOptionalAsName());

                if (atom.getSplitterExpression() != null) {
                    contained.setSplitExpression(unmapExpressionDeep(atom.getSplitterExpression(), unmapContext));
                }
                propertySelects.add(contained);
            }
            filterDef.setOptionalPropertySelects(propertySelects);
        }
        return filterDef;
    }

    private static List<AnnotationPart> unmapAnnotations(List<AnnotationDesc> annotations) {
        List<AnnotationPart> result = new ArrayList<AnnotationPart>();
        for (AnnotationDesc desc : annotations) {
            result.add(unmapAnnotation(desc));
        }
        return result;
    }

    private static List<ExpressionDeclaration> unmapExpressionDeclarations(ExpressionDeclDesc expr, StatementSpecUnMapContext unmapContext) {
        if (expr == null || expr.getExpressions().isEmpty()) {
            return Collections.emptyList();
        }
        List<ExpressionDeclaration> result = new ArrayList<ExpressionDeclaration>();
        for (ExpressionDeclItem desc : expr.getExpressions()) {
            result.add(unmapExpressionDeclItem(desc, unmapContext));
        }
        return result;
    }

    private static ExpressionDeclaration unmapExpressionDeclItem(ExpressionDeclItem desc, StatementSpecUnMapContext unmapContext) {
        return new ExpressionDeclaration(desc.getName(), desc.getParametersNames(), unmapExpressionDeep(desc.getInner(), unmapContext));
    }

    private static List<ScriptExpression> unmapScriptExpressions(List<ExpressionScriptProvided> scripts, StatementSpecUnMapContext unmapContext) {
        if (scripts == null || scripts.isEmpty()) {
            return Collections.emptyList();
        }
        List<ScriptExpression> result = new ArrayList<ScriptExpression>();
        for (ExpressionScriptProvided script : scripts) {
            ScriptExpression e = unmapScriptExpression(script, unmapContext);
            result.add(e);
        }
        return result;
    }

    private static ScriptExpression unmapScriptExpression(ExpressionScriptProvided script, StatementSpecUnMapContext unmapContext) {
        String returnType = script.getOptionalReturnTypeName();
        if (returnType != null && script.isOptionalReturnTypeIsArray()) {
            returnType = returnType + "[]";
        }
        return new ScriptExpression(script.getName(), script.getParameterNames(), script.getExpression(), returnType, script.getOptionalDialect());
    }

    private static AnnotationPart unmapAnnotation(AnnotationDesc desc) {
        if ((desc.getAttributes() == null) || (desc.getAttributes().isEmpty())) {
            return new AnnotationPart(desc.getName());
        }

        List<AnnotationAttribute> attributes = new ArrayList<AnnotationAttribute>();
        for (Pair<String, Object> pair : desc.getAttributes()) {
            if (pair.getSecond() instanceof AnnotationDesc) {
                attributes.add(new AnnotationAttribute(pair.getFirst(), unmapAnnotation((AnnotationDesc) pair.getSecond())));
            }
            else {
                attributes.add(new AnnotationAttribute(pair.getFirst(), pair.getSecond()));
            }
        }
        return new AnnotationPart(desc.getName(), attributes);
    }

    public static List<AnnotationDesc> mapAnnotations(List<AnnotationPart> annotations) {
        List<AnnotationDesc> result;
        if (annotations != null) {
            result = new ArrayList<AnnotationDesc>();
            for (AnnotationPart part : annotations) {
                result.add(mapAnnotation(part));
            }
        }
        else {
            result = Collections.emptyList();
        }
        return result;
    }

    private static void mapContextName(String contextName, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        raw.setOptionalContextName(contextName);
        mapContext.setContextName(contextName);
    }

    private static void mapExpressionDeclaration(List<ExpressionDeclaration> expressionDeclarations, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        if (expressionDeclarations == null || expressionDeclarations.isEmpty()) {
            return;
        }

        ExpressionDeclDesc desc = new ExpressionDeclDesc();
        raw.setExpressionDeclDesc(desc);

        for (ExpressionDeclaration decl : expressionDeclarations) {
            ExpressionDeclItem item = mapExpressionDeclItem(decl, mapContext);
            desc.getExpressions().add(item);
            mapContext.addExpressionDeclarations(item);
        }
    }

    private static ExpressionDeclItem mapExpressionDeclItem(ExpressionDeclaration decl, StatementSpecMapContext mapContext) {
        return new ExpressionDeclItem(decl.getName(), decl.getParameterNames(), mapExpressionDeep(decl.getExpression(), mapContext));
    }

    private static void mapScriptExpressions(List<ScriptExpression> scriptExpressions, StatementSpecRaw raw, StatementSpecMapContext mapContext) {
        if (scriptExpressions == null || scriptExpressions.isEmpty()) {
            return;
        }

        List<ExpressionScriptProvided> scripts = new ArrayList<ExpressionScriptProvided>();
        raw.setScriptExpressions(scripts);

        for (ScriptExpression decl : scriptExpressions) {
            ExpressionScriptProvided scriptProvided = mapScriptExpression(decl, mapContext);
            scripts.add(scriptProvided);
            mapContext.addScript(scriptProvided);
        }
    }

    private static ExpressionScriptProvided mapScriptExpression(ScriptExpression decl, StatementSpecMapContext mapContext) {
        String returnType = decl.getOptionalReturnType() != null ? decl.getOptionalReturnType().replace("[]", "") : null;
        boolean isArray = decl.getOptionalReturnType() != null ? decl.getOptionalReturnType().contains("[]") : false;
        return new ExpressionScriptProvided(decl.getName(), decl.getExpressionText(), decl.getParameterNames(), returnType, isArray, decl.getOptionalDialect());
    }

    private static AnnotationDesc mapAnnotation(AnnotationPart part) {
        if ((part.getAttributes() == null) || (part.getAttributes().isEmpty())) {
            return new AnnotationDesc(part.getName(), Collections.EMPTY_LIST);
        }

        List<Pair<String, Object>> attributes = new ArrayList<Pair<String, Object>>();
        for (AnnotationAttribute pair : part.getAttributes()) {
            if (pair.getValue() instanceof AnnotationPart) {
                attributes.add(new Pair<String, Object>(pair.getName(), mapAnnotation((AnnotationPart) pair.getValue())));
            }
            else {
                attributes.add(new Pair<String, Object>(pair.getName(), pair.getValue()));
            }
        }
        return new AnnotationDesc(part.getName(), attributes);
    }

    private static void mapSQLParameters(FromClause fromClause, StatementSpecRaw raw, StatementSpecMapContext mapContext)
    {
        if ((fromClause == null) || (fromClause.getStreams() == null)) {
            return;
        }
        int streamNum = -1;
        for (Stream stream : fromClause.getStreams()) {
            streamNum++;
            if (!(stream instanceof SQLStream)) {
                continue;
            }
            SQLStream sqlStream = (SQLStream) stream;

            List<PlaceholderParser.Fragment> sqlFragments = null;
            try
            {
                sqlFragments = PlaceholderParser.parsePlaceholder(sqlStream.getSqlWithSubsParams());
            }
            catch (PlaceholderParseException e)
            {
                throw new RuntimeException("Error parsing SQL placeholder expression '" + sqlStream.getSqlWithSubsParams() + "': ");
            }

            for (PlaceholderParser.Fragment fragment : sqlFragments)
            {
                if (!(fragment instanceof PlaceholderParser.ParameterFragment)) {
                    continue;
                }

                // Parse expression, store for substitution parameters
                String expression = fragment.getValue();
                if (expression.toUpperCase().equals(DatabasePollingViewableFactory.SAMPLE_WHERECLAUSE_PLACEHOLDER)) {
                    continue;
                }

                if (expression.trim().length() == 0) {
                    throw new ASTWalkException("Missing expression within ${...} in SQL statement");
                }
                String toCompile = "select * from java.lang.Object where " + expression;
                StatementSpecRaw rawSqlExpr = EPAdministratorHelper.compileEPL(toCompile, expression, false, null, SelectClauseStreamSelectorEnum.ISTREAM_ONLY,
                        mapContext.getEngineImportService(), mapContext.getVariableService(), mapContext.getSchedulingService(), mapContext.getEngineURI(), mapContext.getConfiguration(), mapContext.getPatternNodeFactory(), mapContext.getContextManagementService(), mapContext.getExprDeclaredService());

                if ((rawSqlExpr.getSubstitutionParameters() != null) && (rawSqlExpr.getSubstitutionParameters().size() > 0)) {
                    throw new ASTWalkException("EPL substitution parameters are not allowed in SQL ${...} expressions, consider using a variable instead");
                }

                if (rawSqlExpr.isHasVariables()) {
                    mapContext.setHasVariables(true);
                }

                // add expression
                if (raw.getSqlParameters() == null) {
                    raw.setSqlParameters(new HashMap<Integer, List<ExprNode>>());
                }
                List<ExprNode> listExp = raw.getSqlParameters().get(streamNum);
                if (listExp == null) {
                    listExp = new ArrayList<ExprNode>();
                    raw.getSqlParameters().put(streamNum, listExp);
                }
                listExp.add(rawSqlExpr.getFilterRootNode());
            }
        }
    }

    private static List<ExprChainedSpec> mapChains(List<DotExpressionItem> pairs, StatementSpecMapContext mapContext) {
        List<ExprChainedSpec> chains = new ArrayList<ExprChainedSpec>();
        for (DotExpressionItem item : pairs) {
            chains.add(new ExprChainedSpec(item.getName(), mapExpressionDeep(item.getParameters(), mapContext), item.isProperty()));
        }
        return chains;
    }

    private static List<DotExpressionItem> unmapChains(List<ExprChainedSpec> pairs, StatementSpecUnMapContext unmapContext, boolean isProperty) {
        List<DotExpressionItem> result = new ArrayList<DotExpressionItem>();
        for (ExprChainedSpec chain : pairs) {
            result.add(new DotExpressionItem(chain.getName(), unmapExpressionDeep(chain.getParameters(), unmapContext), chain.isProperty()));
        }
        return result;
    }
}
