/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.join.base;

import com.espertech.esper.client.EventType;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.core.service.StreamJoinAnalysisResult;
import com.espertech.esper.epl.expression.ExprAndNodeImpl;
import com.espertech.esper.epl.expression.ExprEvaluatorContext;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.join.plan.*;
import com.espertech.esper.epl.join.pollindex.*;
import com.espertech.esper.epl.join.table.HistoricalStreamIndexList;
import com.espertech.esper.epl.join.util.QueryPlanIndexHook;
import com.espertech.esper.epl.join.util.QueryPlanIndexHookUtil;
import com.espertech.esper.epl.spec.OuterJoinDesc;
import com.espertech.esper.type.OuterJoinType;
import com.espertech.esper.util.AuditPath;
import com.espertech.esper.util.DependencyGraph;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Factory for building a {@link JoinSetComposer} from analyzing filter nodes, for
 * fast join tuple result set composition.
 */
public class JoinSetComposerPrototypeFactory
{
    private static final Log queryPlanLog = LogFactory.getLog(AuditPath.QUERYPLAN_LOG);

    /**
     * Builds join tuple composer.
     * @param outerJoinDescList - list of descriptors for outer join criteria
     * @param optionalFilterNode - filter tree for analysis to build indexes for fast access
     * @param streamTypes - types of streams
     * @param streamNames - names of streams
     * @return composer implementation
     * @throws ExprValidationException is thrown to indicate that
     * validation of view use in joins failed.
     */
    public static JoinSetComposerPrototype makeComposerPrototype(String statementName,
                                                          String statementId,
                                                          OuterJoinDesc[] outerJoinDescList,
                                                          ExprNode optionalFilterNode,
                                                          EventType[] streamTypes,
                                                          String[] streamNames,
                                                          StreamJoinAnalysisResult streamJoinAnalysisResult,
                                                          boolean queryPlanLogging,
                                                          Annotation[] annotations,
                                                          HistoricalViewableDesc historicalViewableDesc,
                                                          ExprEvaluatorContext exprEvaluatorContext,
                                                          boolean selectsRemoveStream,
                                                          boolean hasAggregations)
            throws ExprValidationException
    {
        // Determine if there is a historical stream, and what dependencies exist
        DependencyGraph historicalDependencyGraph = new DependencyGraph(streamTypes.length, false);
        for (int i = 0; i < streamTypes.length; i++)
        {
            if (historicalViewableDesc.getHistorical()[i]) {
                SortedSet<Integer> streamsThisStreamDependsOn = historicalViewableDesc.getDependenciesPerHistorical()[i];
                historicalDependencyGraph.addDependency(i, streamsThisStreamDependsOn);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Dependency graph: " + historicalDependencyGraph);
        }

        // Handle a join with a database or other historical data source for 2 streams
        if ((historicalViewableDesc.isHasHistorical()) && (streamTypes.length == 2))
        {
            return makeComposerHistorical2Stream(outerJoinDescList, optionalFilterNode, streamTypes, historicalViewableDesc, queryPlanLogging, exprEvaluatorContext);
        }

        boolean isOuterJoins = !OuterJoinDesc.consistsOfAllInnerJoins(outerJoinDescList);

        // Query graph for graph relationships between streams/historicals
        // For outer joins the query graph will just contain outer join relationships
        QueryGraph queryGraph = new QueryGraph(streamTypes.length);
        if (outerJoinDescList.length > 0)
        {
            OuterJoinAnalyzer.analyze(outerJoinDescList, queryGraph);
            if (log.isDebugEnabled())
            {
                log.debug(".makeComposer After outer join queryGraph=\n" + queryGraph);
            }
        }

        // Let the query graph reflect the where-clause
        if (optionalFilterNode != null)
        {
            // Analyze relationships between streams using the optional filter expression.
            // Relationships are properties in AND and EQUALS nodes of joins.
            FilterExprAnalyzer.analyze(optionalFilterNode, queryGraph, isOuterJoins);
            if (log.isDebugEnabled())
            {
                log.debug(".makeComposer After filter expression queryGraph=\n" + queryGraph);
            }

            // Add navigation entries based on key and index property equivalency (a=b, b=c follows a=c)
            QueryGraph.fillEquivalentNav(streamTypes, queryGraph);
            if (log.isDebugEnabled())
            {
                log.debug(".makeComposer After fill equiv. nav. queryGraph=\n" + queryGraph);
            }
        }

        // Historical index lists
        HistoricalStreamIndexList[] historicalStreamIndexLists = new HistoricalStreamIndexList[streamTypes.length];

        QueryPlan queryPlan = QueryPlanBuilder.getPlan(streamTypes, outerJoinDescList, queryGraph, streamNames,
                historicalViewableDesc, historicalDependencyGraph, historicalStreamIndexLists,
                streamJoinAnalysisResult, queryPlanLogging, annotations, exprEvaluatorContext);

        // remove unused indexes - consider all streams or all unidirectional
        HashSet<String> usedIndexes = new HashSet<String>();
        QueryPlanIndex[] indexSpecs = queryPlan.getIndexSpecs();
        for (int streamNum = 0; streamNum < queryPlan.getExecNodeSpecs().length; streamNum++) {
            QueryPlanNode planNode = queryPlan.getExecNodeSpecs()[streamNum];
            if (planNode != null) {
                planNode.addIndexes(usedIndexes);
            }
        }
        for (QueryPlanIndex indexSpec : indexSpecs) {
            if (indexSpec == null) {
                continue;
            }
            Map<String, QueryPlanIndexItem> items = indexSpec.getItems();
            String[] indexNames = items.keySet().toArray(new String[items.size()]);
            for (String indexName : indexNames) {
                if (!usedIndexes.contains(indexName)) {
                    items.remove(indexName);
                }
            }
        }

        if (queryPlanLogging && queryPlanLog.isInfoEnabled()) {
            queryPlanLog.info("Query plan: " + queryPlan.toQueryPlan());

            QueryPlanIndexHook hook = QueryPlanIndexHookUtil.getHook(annotations);
            if (hook != null) {
                hook.join(queryPlan);
            }
        }

        boolean joinRemoveStream = selectsRemoveStream || hasAggregations;
        return new JoinSetComposerPrototypeImpl(statementName,
                                                statementId,
                                                outerJoinDescList,
                                                optionalFilterNode,
                                                streamTypes,
                                                streamNames,
                                                streamJoinAnalysisResult,
                                                annotations,
                                                historicalViewableDesc,
                                                exprEvaluatorContext,
                                                indexSpecs,
                                                queryPlan,
                                                historicalStreamIndexLists,
                                                joinRemoveStream,
                                                isOuterJoins);
    }

    private static JoinSetComposerPrototype makeComposerHistorical2Stream(OuterJoinDesc[] outerJoinDescList,
                                                   ExprNode optionalFilterNode,
                                                   EventType[] streamTypes,
                                                   HistoricalViewableDesc historicalViewableDesc,
                                                   boolean queryPlanLogging,
                                                   ExprEvaluatorContext exprEvaluatorContext)
            throws ExprValidationException
    {
        int polledViewNum = 0;
        int streamViewNum = 1;
        if (historicalViewableDesc.getHistorical()[1])
        {
            streamViewNum = 0;
            polledViewNum = 1;
        }

        // if all-historical join, check dependency
        boolean isAllHistoricalNoSubordinate = false;
        if ((historicalViewableDesc.getHistorical()[0]) && historicalViewableDesc.getHistorical()[1])
        {
            DependencyGraph graph = new DependencyGraph(2, false);
            graph.addDependency(0, historicalViewableDesc.getDependenciesPerHistorical()[0]);
            graph.addDependency(1, historicalViewableDesc.getDependenciesPerHistorical()[1]);
            if (graph.getFirstCircularDependency() != null)
            {
                throw new ExprValidationException("Circular dependency detected between historical streams");
            }

            // if both streams are independent
            if (graph.getRootNodes().size() == 2)
            {
                isAllHistoricalNoSubordinate = true; // No parameters used by either historical
            }
            else
            {
                if ((graph.getDependenciesForStream(0).size() == 0))
                {
                    streamViewNum = 0;
                    polledViewNum = 1;
                }
                else
                {
                    streamViewNum = 1;
                    polledViewNum = 0;
                }
            }
        }

        // Build an outer join expression node
        boolean isOuterJoin = false;
        ExprNode outerJoinEqualsNode = null;
        if (outerJoinDescList.length > 0)
        {
            OuterJoinDesc outerJoinDesc = outerJoinDescList[0];
            if (outerJoinDesc.getOuterJoinType().equals(OuterJoinType.FULL))
            {
                isOuterJoin = true;
            }
            else if ((outerJoinDesc.getOuterJoinType().equals(OuterJoinType.LEFT)) &&
                    (streamViewNum == 0))
            {
                    isOuterJoin = true;
            }
            else if ((outerJoinDesc.getOuterJoinType().equals(OuterJoinType.RIGHT)) &&
                    (streamViewNum == 1))
            {
                    isOuterJoin = true;
            }

            outerJoinEqualsNode  = outerJoinDesc.makeExprNode(exprEvaluatorContext);
        }

        // Determine filter for indexing purposes
        ExprNode filterForIndexing = null;
        if ((outerJoinEqualsNode != null) && (optionalFilterNode != null))  // both filter and outer join, add
        {
            filterForIndexing = new ExprAndNodeImpl();
            filterForIndexing.addChildNode(optionalFilterNode);
            filterForIndexing.addChildNode(outerJoinEqualsNode);
        }
        else if ((outerJoinEqualsNode == null) && (optionalFilterNode != null))
        {
            filterForIndexing = optionalFilterNode;
        }
        else if (outerJoinEqualsNode != null)
        {
            filterForIndexing = outerJoinEqualsNode;
        }

        Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy> indexStrategies =
                determineIndexing(filterForIndexing, streamTypes[polledViewNum], streamTypes[streamViewNum], polledViewNum, streamViewNum);

        if (queryPlanLogging && queryPlanLog.isInfoEnabled()) {
            queryPlanLog.info("historical lookup strategy: " + indexStrategies.getFirst().toQueryPlan());
            queryPlanLog.info("historical index strategy: " + indexStrategies.getSecond().toQueryPlan());
        }

        return new JoinSetComposerPrototypeHistorical2StreamImpl(
                                                    optionalFilterNode,
                                                    streamTypes,
                                                    exprEvaluatorContext,
                                                    polledViewNum,
                                                    streamViewNum,
                                                    isOuterJoin,
                                                    outerJoinEqualsNode,
                                                    indexStrategies,
                                                    isAllHistoricalNoSubordinate,
                                                    outerJoinDescList);
    }

    private static Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy> determineIndexing(ExprNode filterForIndexing,
                                                                                              EventType polledViewType,
                                                                                              EventType streamViewType,
                                                                                              int polledViewStreamNum,
                                                                                              int streamViewStreamNum)
    {
        // No filter means unindexed event tables
        if (filterForIndexing == null)
        {
            return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(
                            new HistoricalIndexLookupStrategyNoIndex(), new PollResultIndexingStrategyNoIndex());
        }

        // analyze query graph; Whereas stream0=named window, stream1=delete-expr filter
        QueryGraph queryGraph = new QueryGraph(2);
        FilterExprAnalyzer.analyze(filterForIndexing, queryGraph, false);

        return determineIndexing(queryGraph, polledViewType, streamViewType, polledViewStreamNum, streamViewStreamNum);
    }

    /**
     * Constructs indexing and lookup strategy for a given relationship that a historical stream may have with another
     * stream (historical or not) that looks up into results of a poll of a historical stream.
     * <p>
     * The term "polled" refers to the assumed-historical stream.
     * @param queryGraph relationship representation of where-clause filter and outer join on-expressions
     * @param polledViewType the event type of the historical that is indexed
     * @param streamViewType the event type of the stream looking up in indexes
     * @param polledViewStreamNum the stream number of the historical that is indexed
     * @param streamViewStreamNum the stream number of the historical that is looking up
     * @return indexing and lookup strategy pair
     */
    public static Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy> determineIndexing(QueryGraph queryGraph,
                                                                                                  EventType polledViewType,
                                                                                                  EventType streamViewType,
                                                                                                  int polledViewStreamNum,
                                                                                                  int streamViewStreamNum)
    {
        QueryGraphValue queryGraphValue = queryGraph.getGraphValue(streamViewStreamNum, polledViewStreamNum);
        QueryGraphValuePairHashKeyIndex hashKeysAndIndes = queryGraphValue.getHashKeyProps();
        QueryGraphValuePairRangeIndex rangeKeysAndIndex = queryGraphValue.getRangeProps();

        // index and key property names
        List<QueryGraphValueEntryHashKeyed> hashKeys = hashKeysAndIndes.getKeys();
        String[] hashIndexes = hashKeysAndIndes.getIndexed();
        List<QueryGraphValueEntryRange> rangeKeys = rangeKeysAndIndex.getKeys();
        String[] rangeIndexes = rangeKeysAndIndex.getIndexed();

        // If the analysis revealed no join columns, must use the brute-force full table scan
        if (hashKeys.isEmpty() && rangeKeys.isEmpty())
        {
            return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(
                            new HistoricalIndexLookupStrategyNoIndex(), new PollResultIndexingStrategyNoIndex());
        }

        CoercionDesc keyCoercionTypes = CoercionUtil.getCoercionTypesHash(new EventType[]{streamViewType, polledViewType}, 0, 1, hashKeys, hashIndexes);

        if (rangeKeys.isEmpty()) {
            // No coercion
            if (!keyCoercionTypes.isCoerce())
            {
                if (hashIndexes.length == 1) {
                    PollResultIndexingStrategyIndexSingle indexing = new PollResultIndexingStrategyIndexSingle(polledViewStreamNum, polledViewType, hashIndexes[0]);
                    HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategyIndexSingle(streamViewStreamNum, hashKeys.get(0));
                    return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
                }
                else {
                    PollResultIndexingStrategyIndex indexing = new PollResultIndexingStrategyIndex(polledViewStreamNum, polledViewType, hashIndexes);
                    HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategyIndex(streamViewType, streamViewStreamNum, hashKeys);
                    return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
                }
            }

            // With coercion, same lookup strategy as the index coerces
            if (hashIndexes.length == 1) {
                PollResultIndexingStrategy indexing = new PollResultIndexingStrategyIndexCoerceSingle(polledViewStreamNum, polledViewType, hashIndexes[0], keyCoercionTypes.getCoercionTypes()[0]);
                HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategyIndexSingle(streamViewStreamNum, hashKeys.get(0));
                return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
            }
            else {
                PollResultIndexingStrategy indexing = new PollResultIndexingStrategyIndexCoerce(polledViewStreamNum, polledViewType, hashIndexes, keyCoercionTypes.getCoercionTypes());
                HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategyIndex(streamViewType, streamViewStreamNum, hashKeys);
                return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
            }
        }
        else {
            CoercionDesc rangeCoercionTypes = CoercionUtil.getCoercionTypesRange(new EventType[]{streamViewType, polledViewType}, 1, rangeIndexes, rangeKeys);
            if (rangeKeys.size() == 1 && hashKeys.size() == 0) {
                Class rangeCoercionType = rangeCoercionTypes.isCoerce() ? rangeCoercionTypes.getCoercionTypes()[0] : null;
                PollResultIndexingStrategySorted indexing = new PollResultIndexingStrategySorted(polledViewStreamNum, polledViewType, rangeIndexes[0], rangeCoercionType);
                HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategySorted(streamViewStreamNum, rangeKeys.get(0));
                return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
            }
            else {
                PollResultIndexingStrategyComposite indexing = new PollResultIndexingStrategyComposite(polledViewStreamNum, polledViewType, hashIndexes, keyCoercionTypes.getCoercionTypes(), rangeIndexes, rangeCoercionTypes.getCoercionTypes());
                HistoricalIndexLookupStrategy strategy = new HistoricalIndexLookupStrategyComposite(streamViewStreamNum, hashKeys, keyCoercionTypes.getCoercionTypes(), rangeKeys, rangeCoercionTypes.getCoercionTypes());
                return new Pair<HistoricalIndexLookupStrategy, PollResultIndexingStrategy>(strategy, indexing);
            }
        }
    }

    private static final Log log = LogFactory.getLog(JoinSetComposerPrototypeFactory.class);
}
