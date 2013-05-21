/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.spec;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.annotation.Audit;
import com.espertech.esper.client.annotation.AuditEnum;
import com.espertech.esper.collection.Pair;
import com.espertech.esper.core.service.ExprEvaluatorContextStatement;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.core.StreamTypeService;
import com.espertech.esper.epl.core.StreamTypeServiceImpl;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.epl.property.PropertyEvaluator;
import com.espertech.esper.epl.property.PropertyEvaluatorFactory;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.event.EventTypeSPI;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecCompiler;
import com.espertech.esper.pattern.*;
import com.espertech.esper.pattern.guard.GuardFactory;
import com.espertech.esper.pattern.guard.GuardParameterException;
import com.espertech.esper.pattern.observer.ObserverFactory;
import com.espertech.esper.pattern.observer.ObserverParameterException;
import com.espertech.esper.util.JavaClassHelper;
import com.espertech.esper.util.UuidGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.util.*;

/**
 * Pattern specification in unvalidated, unoptimized form.
 */
public class PatternStreamSpecRaw extends StreamSpecBase implements StreamSpecRaw
{
    private final EvalFactoryNode evalFactoryNode;
    private final Map<EvalFactoryNode, String> evalNodeExpressions;

    private static final Log log = LogFactory.getLog(PatternStreamSpecRaw.class);
    private static final long serialVersionUID = 6393401926404401433L;

    /**
     * Ctor.
     * @param evalFactoryNode - pattern evaluation node representing pattern statement
     * @param viewSpecs - specifies what view to use to derive data
     * @param optionalStreamName - stream name, or null if none supplied
     * @param streamSpecOptions - additional options, such as unidirectional stream in a join
     */
    public PatternStreamSpecRaw(EvalFactoryNode evalFactoryNode, Map<EvalFactoryNode, String> evalNodeExpressions, ViewSpec[] viewSpecs, String optionalStreamName, StreamSpecOptions streamSpecOptions)
    {
        super(optionalStreamName, viewSpecs, streamSpecOptions);
        this.evalFactoryNode = evalFactoryNode;
        this.evalNodeExpressions = evalNodeExpressions;
    }

    /**
     * Returns the pattern expression evaluation node for the top pattern operator.
     * @return parent pattern expression node
     */
    public EvalFactoryNode getEvalFactoryNode()
    {
        return evalFactoryNode;
    }

    public PatternStreamSpecCompiled compile(StatementContext context,
                                      Set<String> eventTypeReferences,
                                      boolean isInsertInto,
                                      Collection<Integer> assignedTypeNumberStack)
            throws ExprValidationException
    {
        return compileInternal(context, eventTypeReferences, isInsertInto, assignedTypeNumberStack, null, null);
    }

    public PatternStreamSpecCompiled compile(StatementContext context,
                                      Set<String> eventTypeReferences,
                                      boolean isInsertInto,
                                      Collection<Integer> assignedTypeNumberStack,
                                      MatchEventSpec priorTags,
                                      Set<String> priorAllTags)
            throws ExprValidationException
    {
        return compileInternal(context, eventTypeReferences, isInsertInto, assignedTypeNumberStack, priorTags, priorAllTags);
    }

    private PatternStreamSpecCompiled compileInternal(StatementContext context,
                                      Set<String> eventTypeReferences,
                                      boolean isInsertInto,
                                      Collection<Integer> assignedTypeNumberStack,
                                      MatchEventSpec tags,
                                      Set<String> priorAllTags)
            throws ExprValidationException
    {
        if (tags == null) {
            tags = new MatchEventSpec();
        }
        Deque<Integer> subexpressionIdStack = new ArrayDeque<Integer>(assignedTypeNumberStack);
        ExprEvaluatorContext evaluatorContextStmt = new ExprEvaluatorContextStatement(context);
        Stack<EvalFactoryNode> nodeStack = new Stack<EvalFactoryNode>();

        // detemine ordered tags
        Set<EvalFactoryNode> filterFactoryNodes = EvalNodeUtil.recursiveGetChildNodes(evalFactoryNode, FilterForFilterFactoryNodes.INSTANCE);
        LinkedHashSet<String> allTagNamesOrdered = new LinkedHashSet<String>();
        if (priorAllTags != null) {
            allTagNamesOrdered.addAll(priorAllTags);
        }
        for (EvalFactoryNode filterNode : filterFactoryNodes) {
            EvalFilterFactoryNode factory = (EvalFilterFactoryNode) filterNode;
            int tagNumber;
            if (factory.getEventAsName() != null) {
                if (!allTagNamesOrdered.contains(factory.getEventAsName())) {
                    allTagNamesOrdered.add(factory.getEventAsName());
                    tagNumber = allTagNamesOrdered.size() - 1;
                }
                else {
                    tagNumber = findTagNumber(factory.getEventAsName(), allTagNamesOrdered);
                }
                factory.setEventAsTagNumber(tagNumber);
            }
        }

        recursiveCompile(evalFactoryNode, context, evaluatorContextStmt, eventTypeReferences, isInsertInto, tags, subexpressionIdStack, nodeStack, allTagNamesOrdered);

        Audit auditPattern = AuditEnum.PATTERN.getAudit(context.getAnnotations());
        Audit auditPatternInstance = AuditEnum.PATTERNINSTANCES.getAudit(context.getAnnotations());
        EvalFactoryNode compiledEvalFactoryNode = evalFactoryNode;
        if (auditPattern != null || auditPatternInstance != null) {
            EvalAuditInstanceCount instanceCount = new EvalAuditInstanceCount();
            compiledEvalFactoryNode = recursiveAddAuditNode(null, auditPattern != null, auditPatternInstance != null, evalFactoryNode, evalNodeExpressions, instanceCount);
        }

        return new PatternStreamSpecCompiled(compiledEvalFactoryNode, tags.getTaggedEventTypes(), tags.getArrayEventTypes(), allTagNamesOrdered, this.getViewSpecs(), this.getOptionalStreamName(), this.getOptions());
    }

    private static void recursiveCompile(EvalFactoryNode evalNode, StatementContext context, ExprEvaluatorContext evaluatorContext, Set<String> eventTypeReferences, boolean isInsertInto, MatchEventSpec tags, Deque<Integer> subexpressionIdStack, Stack<EvalFactoryNode> parentNodeStack, LinkedHashSet<String> allTagNamesOrdered) throws ExprValidationException
    {
        int counter = 0;
        parentNodeStack.push(evalNode);
        for (EvalFactoryNode child : evalNode.getChildNodes())
        {
            subexpressionIdStack.addLast(counter++);
            recursiveCompile(child, context, evaluatorContext, eventTypeReferences, isInsertInto, tags, subexpressionIdStack, parentNodeStack, allTagNamesOrdered);
            subexpressionIdStack.removeLast();
        }
        parentNodeStack.pop();

        LinkedHashMap<String, Pair<EventType, String>> newTaggedEventTypes = null;
        LinkedHashMap<String, Pair<EventType, String>> newArrayEventTypes = null;

        if (evalNode instanceof EvalFilterFactoryNode)
        {
            EvalFilterFactoryNode filterNode = (EvalFilterFactoryNode) evalNode;
            String eventName = filterNode.getRawFilterSpec().getEventTypeName();
            EventType resolvedEventType = FilterStreamSpecRaw.resolveType(context.getEngineURI(), eventName, context.getEventAdapterService(), context.getPlugInTypeResolutionURIs());
            EventType finalEventType = resolvedEventType;
            String optionalTag = filterNode.getEventAsName();
            boolean isPropertyEvaluation = false;
            boolean isParentMatchUntil = isParentMatchUntil(evalNode, parentNodeStack);

            // obtain property event type, if final event type is properties
            if (filterNode.getRawFilterSpec().getOptionalPropertyEvalSpec() != null)
            {
                PropertyEvaluator optionalPropertyEvaluator = PropertyEvaluatorFactory.makeEvaluator(filterNode.getRawFilterSpec().getOptionalPropertyEvalSpec(), resolvedEventType, filterNode.getEventAsName(), context.getEventAdapterService(), context.getMethodResolutionService(), context.getSchedulingService(), context.getVariableService(), context.getEngineURI(), context.getStatementId(), context.getStatementName(), context.getAnnotations(), subexpressionIdStack, context.getConfigSnapshot());
                finalEventType = optionalPropertyEvaluator.getFragmentEventType();
                isPropertyEvaluation = true;
            }

            if (finalEventType instanceof EventTypeSPI)
            {
                eventTypeReferences.add(((EventTypeSPI) finalEventType).getMetadata().getPrimaryName());
            }

            // If a tag was supplied for the type, the tags must stay with this type, i.e. a=BeanA -> b=BeanA -> a=BeanB is a no
            if (optionalTag != null)
            {
                Pair<EventType, String> pair = tags.getTaggedEventTypes().get(optionalTag);
                EventType existingType = null;
                if (pair != null)
                {
                    existingType = pair.getFirst();
                }
                if (existingType == null)
                {
                    pair = tags.getArrayEventTypes().get(optionalTag);
                    if (pair != null)
                    {
                        throw new ExprValidationException("Tag '" + optionalTag + "' for event '" + eventName +
                                "' used in the repeat-until operator cannot also appear in other filter expressions");
                    }
                }
                if ((existingType != null) && (existingType != finalEventType))
                {
                    throw new ExprValidationException("Tag '" + optionalTag + "' for event '" + eventName +
                            "' has already been declared for events of type " + existingType.getUnderlyingType().getName());
                }
                pair = new Pair<EventType, String>(finalEventType, eventName);

                // add tagged type
                if (isPropertyEvaluation || isParentMatchUntil)
                {
                    newArrayEventTypes = new LinkedHashMap<String, Pair<EventType, String>>();
                    newArrayEventTypes.put(optionalTag, pair);
                }
                else
                {
                    newTaggedEventTypes = new LinkedHashMap<String, Pair<EventType, String>>();
                    newTaggedEventTypes.put(optionalTag, pair);
                }
            }

            // For this filter, filter types are all known tags at this time,
            // and additionally stream 0 (self) is our event type.
            // Stream type service allows resolution by property name event if that name appears in other tags.
            // by defaulting to stream zero.
            // Stream zero is always the current event type, all others follow the order of the map (stream 1 to N).
            String selfStreamName = optionalTag;
            if (selfStreamName == null)
            {
                selfStreamName = "s_" + UuidGenerator.generate();
            }
            LinkedHashMap<String, Pair<EventType, String>> filterTypes = new LinkedHashMap<String, Pair<EventType, String>>();
            Pair<EventType, String> typePair = new Pair<EventType, String>(finalEventType, eventName);
            filterTypes.put(selfStreamName, typePair);
            filterTypes.putAll(tags.getTaggedEventTypes());

            // for the filter, specify all tags used
            LinkedHashMap<String, Pair<EventType, String>> filterTaggedEventTypes = new LinkedHashMap<String, Pair<EventType, String>>(tags.getTaggedEventTypes());
            filterTaggedEventTypes.remove(optionalTag);

            // handle array tags (match-until clause)
            LinkedHashMap<String, Pair<EventType, String>> arrayCompositeEventTypes = null;
            if (tags.getArrayEventTypes() != null && !tags.getArrayEventTypes().isEmpty())
            {
                arrayCompositeEventTypes = new LinkedHashMap<String, Pair<EventType, String>>();
                String patternSubexEventType = getPatternSubexEventType(context.getStatementId(), "pattern", subexpressionIdStack);

                for (Map.Entry<String, Pair<EventType, String>> entry : tags.getArrayEventTypes().entrySet())
                {
                    LinkedHashMap<String, Pair<EventType, String>> specificArrayType = new LinkedHashMap<String, Pair<EventType, String>>();
                    specificArrayType.put(entry.getKey(), entry.getValue());
                    EventType arrayTagCompositeEventType = context.getEventAdapterService().createSemiAnonymousMapType(patternSubexEventType, Collections.<String, Pair<EventType,String>>emptyMap(), specificArrayType, isInsertInto);

                    String tag = entry.getKey();
                    if (!filterTypes.containsKey(tag))
                    {
                        Pair<EventType, String> pair = new Pair<EventType, String>(arrayTagCompositeEventType, tag);
                        filterTypes.put(tag, pair);
                        arrayCompositeEventTypes.put(tag, pair);
                    }
                }
            }

            StreamTypeService streamTypeService = new StreamTypeServiceImpl(filterTypes, context.getEngineURI(), true, false);
            List<ExprNode> exprNodes = filterNode.getRawFilterSpec().getFilterExpressions();

            FilterSpecCompiled spec = FilterSpecCompiler.makeFilterSpec(resolvedEventType, eventName, exprNodes,
                    filterNode.getRawFilterSpec().getOptionalPropertyEvalSpec(),  filterTaggedEventTypes, arrayCompositeEventTypes, streamTypeService,
                   null, context, subexpressionIdStack);
            filterNode.setFilterSpec(spec);
        }
        else if (evalNode instanceof EvalObserverFactoryNode)
        {
            EvalObserverFactoryNode observerNode = (EvalObserverFactoryNode) evalNode;
            try
            {
                ObserverFactory observerFactory = context.getPatternResolutionService().create(observerNode.getPatternObserverSpec());

                StreamTypeService streamTypeService = getStreamTypeService(context.getEngineURI(), context.getStatementId(), context.getEventAdapterService(), tags.getTaggedEventTypes(), tags.getArrayEventTypes(), subexpressionIdStack, "observer");
                ExprValidationContext validationContext = new ExprValidationContext(streamTypeService, context.getMethodResolutionService(), null, context.getSchedulingService(), context.getVariableService(), evaluatorContext, context.getEventAdapterService(), context.getStatementName(), context.getStatementId(), context.getAnnotations(), context.getContextDescriptor());
                List<ExprNode> validated = validateExpressions(observerNode.getPatternObserverSpec().getObjectParameters(), validationContext);

                MatchedEventConvertor convertor = new MatchedEventConvertorImpl(tags.getTaggedEventTypes(), tags.getArrayEventTypes(), allTagNamesOrdered, context.getEventAdapterService());

                observerNode.setObserverFactory(observerFactory);
                observerFactory.setObserverParameters(validated, convertor);
            }
            catch (ObserverParameterException e)
            {
                throw new ExprValidationException("Invalid parameter for pattern observer: " + e.getMessage(), e);
            }
            catch (PatternObjectException e)
            {
                throw new ExprValidationException("Failed to resolve pattern observer: " + e.getMessage(), e);
            }
        }
        else if (evalNode instanceof EvalGuardFactoryNode)
        {
            EvalGuardFactoryNode guardNode = (EvalGuardFactoryNode) evalNode;
            try
            {
                GuardFactory guardFactory = context.getPatternResolutionService().create(guardNode.getPatternGuardSpec());

                StreamTypeService streamTypeService = getStreamTypeService(context.getEngineURI(), context.getStatementId(), context.getEventAdapterService(), tags.getTaggedEventTypes(), tags.getArrayEventTypes(), subexpressionIdStack, "guard");
                ExprValidationContext validationContext = new ExprValidationContext(streamTypeService, context.getMethodResolutionService(), null, context.getSchedulingService(), context.getVariableService(), evaluatorContext, context.getEventAdapterService(), context.getStatementName(), context.getStatementId(), context.getAnnotations(), context.getContextDescriptor());
                List<ExprNode> validated = validateExpressions(guardNode.getPatternGuardSpec().getObjectParameters(), validationContext);

                MatchedEventConvertor convertor = new MatchedEventConvertorImpl(tags.getTaggedEventTypes(), tags.getArrayEventTypes(), allTagNamesOrdered, context.getEventAdapterService());

                guardNode.setGuardFactory(guardFactory);
                guardFactory.setGuardParameters(validated, convertor);
            }
            catch (GuardParameterException e)
            {
                throw new ExprValidationException("Invalid parameter for pattern guard: " + e.getMessage(), e);
            }
            catch (PatternObjectException e)
            {
                throw new ExprValidationException("Failed to resolve pattern guard: " + e.getMessage(), e);
            }
        }
        else if (evalNode instanceof EvalEveryDistinctFactoryNode)
        {
            EvalEveryDistinctFactoryNode distinctNode = (EvalEveryDistinctFactoryNode) evalNode;
            MatchEventSpec matchEventFromChildNodes = analyzeMatchEvent(distinctNode);
            StreamTypeService streamTypeService = getStreamTypeService(context.getEngineURI(), context.getStatementId(), context.getEventAdapterService(), matchEventFromChildNodes.getTaggedEventTypes(), matchEventFromChildNodes.getArrayEventTypes(), subexpressionIdStack, "every-distinct");
            ExprValidationContext validationContext = new ExprValidationContext(streamTypeService, context.getMethodResolutionService(), null, context.getSchedulingService(), context.getVariableService(), evaluatorContext, context.getEventAdapterService(), context.getStatementName(), context.getStatementId(), context.getAnnotations(), context.getContextDescriptor());
            List<ExprNode> validated;
            try
            {
                validated = validateExpressions(distinctNode.getExpressions(), validationContext);
            }
            catch (ExprValidationPropertyException ex)
            {
                throw new ExprValidationPropertyException(ex.getMessage() + ", every-distinct requires that all properties resolve from sub-expressions to the every-distinct", ex.getCause());
            }

            MatchedEventConvertor convertor = new MatchedEventConvertorImpl(matchEventFromChildNodes.getTaggedEventTypes(), matchEventFromChildNodes.getArrayEventTypes(), allTagNamesOrdered, context.getEventAdapterService());

            distinctNode.setConvertor(convertor);

            // Determine whether some expressions are constants or time period
            List<ExprNode> distinctExpressions = new ArrayList<ExprNode>();
            Long msecToExpire = null;
            for (ExprNode expr : validated) {
                if (expr instanceof ExprTimePeriod) {
                    Double secondsExpire = (Double) ((ExprTimePeriod) expr).evaluate(null, true, evaluatorContext);
                    if ((secondsExpire != null) && (secondsExpire > 0)) {
                        msecToExpire = Math.round(1000d * secondsExpire);
                    }
                    log.debug("Setting every-distinct msec-to-expire to " + msecToExpire);
                }
                else if (expr.isConstantResult()) {
                    log.warn("Every-distinct node utilizes an expression returning a constant value, please check expression '" + expr.toExpressionString() + "', not adding expression to distinct-value expression list");
                }
                else {
                    distinctExpressions.add(expr);
                }
            }
            if (distinctExpressions.isEmpty()) {
                throw new ExprValidationException("Every-distinct node requires one or more distinct-value expressions that each return non-constant result values");
            }
            distinctNode.setDistinctExpressions(distinctExpressions, msecToExpire);
        }
        else if (evalNode instanceof EvalMatchUntilFactoryNode)
        {
            EvalMatchUntilFactoryNode matchUntilNode = (EvalMatchUntilFactoryNode) evalNode;

            // compile bounds expressions, if any
            MatchEventSpec untilMatchEventSpec = new MatchEventSpec(tags.getTaggedEventTypes(), tags.getArrayEventTypes());
            StreamTypeService streamTypeService = getStreamTypeService(context.getEngineURI(), context.getStatementId(), context.getEventAdapterService(), untilMatchEventSpec.getTaggedEventTypes(), untilMatchEventSpec.getArrayEventTypes(), subexpressionIdStack, "until");
            ExprValidationContext validationContext = new ExprValidationContext(streamTypeService, context.getMethodResolutionService(), null, context.getSchedulingService(), context.getVariableService(), evaluatorContext, context.getEventAdapterService(), context.getStatementName(), context.getStatementId(), context.getAnnotations(), context.getContextDescriptor());

            String message = "Match-until bounds value expressions must return a numeric value";
            if (matchUntilNode.getLowerBounds() != null) {
                ExprNode validated = ExprNodeUtility.getValidatedSubtree(matchUntilNode.getLowerBounds(), validationContext);
                matchUntilNode.setLowerBounds(validated);
                if ((validated.getExprEvaluator().getType() == null) || (!JavaClassHelper.isNumeric(validated.getExprEvaluator().getType()))) {
                    throw new ExprValidationException(message);
                }
            }

            if (matchUntilNode.getUpperBounds() != null) {
                ExprNode validated = ExprNodeUtility.getValidatedSubtree(matchUntilNode.getUpperBounds(), validationContext);
                matchUntilNode.setUpperBounds(validated);
                if ((validated.getExprEvaluator().getType() == null) || (!JavaClassHelper.isNumeric(validated.getExprEvaluator().getType()))) {
                    throw new ExprValidationException(message);
                }
            }

            MatchedEventConvertor convertor = new MatchedEventConvertorImpl(untilMatchEventSpec.getTaggedEventTypes(), untilMatchEventSpec.getArrayEventTypes(), allTagNamesOrdered, context.getEventAdapterService());
            matchUntilNode.setConvertor(convertor);

            // compile new tag lists
            Set<String> arrayTags = null;
            EvalNodeAnalysisResult matchUntilAnalysisResult = EvalNodeUtil.recursiveAnalyzeChildNodes(matchUntilNode.getChildNodes().get(0));
            for (EvalFilterFactoryNode filterNode : matchUntilAnalysisResult.getFilterNodes())
            {
                String optionalTag = filterNode.getEventAsName();
                if (optionalTag != null)
                {
                    if (arrayTags == null)
                    {
                        arrayTags = new HashSet<String>();
                    }
                    arrayTags.add(optionalTag);
                }
            }

            if (arrayTags != null)
            {
                for (String arrayTag : arrayTags)
                {
                    if (!tags.getArrayEventTypes().containsKey(arrayTag))
                    {
                        tags.getArrayEventTypes().put(arrayTag, tags.getTaggedEventTypes().get(arrayTag));
                        tags.getTaggedEventTypes().remove(arrayTag);
                    }
                }
            }
            matchUntilNode.setTagsArrayedSet(getIndexesForTags(allTagNamesOrdered, arrayTags));
        }
        else if (evalNode instanceof EvalFollowedByFactoryNode)
        {
            EvalFollowedByFactoryNode followedByNode = (EvalFollowedByFactoryNode) evalNode;
            StreamTypeService streamTypeService = new StreamTypeServiceImpl(context.getEngineURI(), false);
            ExprValidationContext validationContext = new ExprValidationContext(streamTypeService, context.getMethodResolutionService(), null, context.getSchedulingService(), context.getVariableService(), evaluatorContext, context.getEventAdapterService(), context.getStatementName(), context.getStatementId(), context.getAnnotations(), context.getContextDescriptor());

            if (followedByNode.getOptionalMaxExpressions() != null) {
                List<ExprNode> validated = new ArrayList<ExprNode>();
                for (ExprNode maxExpr : followedByNode.getOptionalMaxExpressions()) {
                    if (maxExpr == null) {
                        validated.add(null);
                    }
                    else {
                        ExprNodeSummaryVisitor visitor = new ExprNodeSummaryVisitor();
                        maxExpr.accept(visitor);
                        if (!visitor.isPlain())
                        {
                            String errorMessage = "Invalid maximum expression in followed-by, " + visitor.getMessage() + " are not allowed within the expression";
                            log.error(errorMessage);
                            throw new ExprValidationException(errorMessage);
                        }

                        ExprNode validatedExpr = ExprNodeUtility.getValidatedSubtree(maxExpr, validationContext);
                        validated.add(validatedExpr);
                        if ((validatedExpr.getExprEvaluator().getType() == null) || (!JavaClassHelper.isNumeric(validatedExpr.getExprEvaluator().getType()))) {
                            String message = "Invalid maximum expression in followed-by, the expression must return an integer value";
                            throw new ExprValidationException(message);
                        }
                    }
                }
                followedByNode.setOptionalMaxExpressions(validated);
            }
        }

        if (newTaggedEventTypes != null)
        {
            tags.getTaggedEventTypes().putAll(newTaggedEventTypes);
        }
        if (newArrayEventTypes != null)
        {
            tags.getArrayEventTypes().putAll(newArrayEventTypes);
        }
    }

    private static int[] getIndexesForTags(LinkedHashSet<String> allTagNamesOrdered, Set<String> arrayTags) {
        if (arrayTags == null || arrayTags.isEmpty()) {
            return new int[0];
        }
        int[] indexes = new int[arrayTags.size()];
        int count = 0;
        for (String arrayTag : arrayTags) {
            int index = 0;
            int found = findTagNumber(arrayTag, allTagNamesOrdered);
            indexes[count] = found;
            count++;
        }
        return indexes;
    }

    private static int findTagNumber(String findTag, LinkedHashSet<String> allTagNamesOrdered) {
        int index = 0;
        for (String tag : allTagNamesOrdered) {
            if (findTag.equals(tag)) {
                return index;
            }
            index++;
        }
        throw new EPException("Failed to find tag '" + findTag + "' among known tags");
    }


    private static boolean isParentMatchUntil(EvalFactoryNode currentNode, Stack<EvalFactoryNode> parentNodeStack) {
        if (parentNodeStack.isEmpty()) {
            return false;
        }

        for (EvalFactoryNode deepParent : parentNodeStack) {
            if (deepParent instanceof EvalMatchUntilFactoryNode) {
                EvalMatchUntilFactoryNode matchUntilFactoryNode = (EvalMatchUntilFactoryNode) deepParent;
                if (matchUntilFactoryNode.getChildNodes().get(0) == currentNode) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<ExprNode> validateExpressions(List<ExprNode> objectParameters, ExprValidationContext validationContext)
            throws ExprValidationException
    {
        if (objectParameters == null)
        {
            return objectParameters;
        }
        List<ExprNode> validated = new ArrayList<ExprNode>();
        for (ExprNode node : objectParameters)
        {
            validated.add(ExprNodeUtility.getValidatedSubtree(node, validationContext));
        }
        return validated;
    }

    private static StreamTypeService getStreamTypeService(String engineURI, String statementId, EventAdapterService eventAdapterService, Map<String, Pair<EventType, String>> taggedEventTypes, Map<String, Pair<EventType, String>> arrayEventTypes, Deque<Integer> subexpressionIdStack, String objectType)
    {
        LinkedHashMap<String, Pair<EventType, String>> filterTypes = new LinkedHashMap<String, Pair<EventType, String>>();
        filterTypes.putAll(taggedEventTypes);

        // handle array tags (match-until clause)
        if (arrayEventTypes != null)
        {
            String patternSubexEventType = getPatternSubexEventType(statementId, objectType, subexpressionIdStack);
            EventType arrayTagCompositeEventType = eventAdapterService.createSemiAnonymousMapType(patternSubexEventType, new HashMap(), arrayEventTypes, false);
            for (Map.Entry<String, Pair<EventType, String>> entry : arrayEventTypes.entrySet())
            {
                String tag = entry.getKey();
                if (!filterTypes.containsKey(tag))
                {
                    Pair<EventType, String> pair = new Pair<EventType, String>(arrayTagCompositeEventType, tag);
                    filterTypes.put(tag, pair);
                }
            }
        }

        return new StreamTypeServiceImpl(filterTypes, engineURI, true, false);
    }

    private static String getPatternSubexEventType(String statementId, String objectType, Deque<Integer> subexpressionIdStack) {
        StringWriter writer = new StringWriter();
        writer.append(statementId);
        writer.append("_");
        writer.append(objectType);
        for (Integer num : subexpressionIdStack) {
            writer.append("_");
            writer.append(Integer.toString(num));
        }
        return writer.toString();
    }

    private static EvalFactoryNode recursiveAddAuditNode(EvalFactoryNode parentNode, boolean auditPattern, boolean auditPatternInstance, EvalFactoryNode evalNode, Map<EvalFactoryNode, String> evalNodeExpressions, EvalAuditInstanceCount instanceCount) {
        String expressionText = evalNodeExpressions.get(evalNode);
        boolean filterChildNonQuitting = parentNode != null && parentNode.isFilterChildNonQuitting();
        EvalAuditFactoryNode audit = new EvalAuditFactoryNode(auditPattern, auditPatternInstance, expressionText, instanceCount, filterChildNonQuitting);
        audit.addChildNode(evalNode);

        List<EvalFactoryNode> newChildNodes = new ArrayList<EvalFactoryNode>();
        for (EvalFactoryNode child : evalNode.getChildNodes()) {
            newChildNodes.add(recursiveAddAuditNode(evalNode, auditPattern, auditPatternInstance, child, evalNodeExpressions, instanceCount));
        }

        evalNode.getChildNodes().clear();
        evalNode.addChildNodes(newChildNodes);

        return audit;
    }

    private static MatchEventSpec analyzeMatchEvent(EvalFactoryNode relativeNode)
    {
        LinkedHashMap<String, Pair<EventType, String>> taggedEventTypes = new LinkedHashMap<String, Pair<EventType, String>>();
        LinkedHashMap<String, Pair<EventType, String>> arrayEventTypes = new LinkedHashMap<String, Pair<EventType, String>>();

        // Determine all the filter nodes used in the pattern
        EvalNodeAnalysisResult evalNodeAnalysisResult = EvalNodeUtil.recursiveAnalyzeChildNodes(relativeNode);

        // collect all filters underneath
        for (EvalFilterFactoryNode filterNode : evalNodeAnalysisResult.getFilterNodes())
        {
            String optionalTag = filterNode.getEventAsName();
            if (optionalTag != null)
            {
                taggedEventTypes.put(optionalTag, new Pair<EventType, String>(filterNode.getFilterSpec().getFilterForEventType(), filterNode.getFilterSpec().getFilterForEventTypeName()));
            }
        }

        // collect those filters under a repeat since they are arrays
        Set<String> arrayTags = new HashSet<String>();
        for (EvalMatchUntilFactoryNode matchUntilNode : evalNodeAnalysisResult.getRepeatNodes())
        {
            EvalNodeAnalysisResult matchUntilAnalysisResult = EvalNodeUtil.recursiveAnalyzeChildNodes(matchUntilNode.getChildNodes().get(0));
            for (EvalFilterFactoryNode filterNode : matchUntilAnalysisResult.getFilterNodes())
            {
                String optionalTag = filterNode.getEventAsName();
                if (optionalTag != null)
                {
                    arrayTags.add(optionalTag);
                }
            }
        }

        // for each array tag change collection
        for (String arrayTag : arrayTags)
        {
            if (taggedEventTypes.get(arrayTag) != null)
            {
                arrayEventTypes.put(arrayTag, taggedEventTypes.get(arrayTag));
                taggedEventTypes.remove(arrayTag);
            }
        }

        return new MatchEventSpec(taggedEventTypes, arrayEventTypes);
    }

    public static class FilterForFilterFactoryNodes implements EvalNodeUtilFactoryFilter {
        public static FilterForFilterFactoryNodes INSTANCE = new FilterForFilterFactoryNodes();
        public boolean consider(EvalFactoryNode node) {
            return node instanceof EvalFilterFactoryNode;
        }
    }
}
