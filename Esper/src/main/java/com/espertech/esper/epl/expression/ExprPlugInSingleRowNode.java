/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.expression;

import com.espertech.esper.client.ConfigurationPlugInSingleRowFunction;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.util.ExpressionReturnType;
import com.espertech.esper.epl.core.EngineImportSingleRowDesc;
import com.espertech.esper.epl.enummethod.dot.ExprDotStaticMethodWrap;
import com.espertech.esper.epl.enummethod.dot.ExprDotStaticMethodWrapFactory;
import com.espertech.esper.filter.FilterSpecLookupable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an invocation of a plug-in single-row function  in the expression tree.
 */
public class ExprPlugInSingleRowNode extends ExprNodeBase implements ExprNodeInnerNodeProvider, ExprFilterOptimizableNode
{
    private static final long serialVersionUID = 2485214890449563098L;

    private final String functionName;
    private final Class clazz;
    private final List<ExprChainedSpec> chainSpec;
    private final EngineImportSingleRowDesc config;

    private transient boolean isReturnsConstantResult;
    private transient ExprEvaluator evaluator;

    /**
	 * Ctor.
	 * @param chainSpec - the class and name of the method that this node will invoke plus parameters
	 */
	public ExprPlugInSingleRowNode(String functionName, Class clazz, List<ExprChainedSpec> chainSpec, EngineImportSingleRowDesc config)
	{
        this.functionName = functionName;
        this.clazz = clazz;
		this.chainSpec = chainSpec;
        this.config = config;
    }

    public ExprEvaluator getExprEvaluator() {
        return evaluator;
    }

    public List<ExprChainedSpec> getChainSpec()
    {
        return chainSpec;
    }

    @Override
    public boolean isConstantResult()
    {
        return isReturnsConstantResult;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public boolean getFilterLookupEligible() {
        // We disallow context properties in a filter-optimizable expression if they are passed in since
        // the evaluation is context-free and shared.
        ExprNodeContextPropertiesVisitor visitor = new ExprNodeContextPropertiesVisitor();
        ExprNodeUtility.acceptChain(visitor, chainSpec);
        return !visitor.isFound() &&
                config.getFilterOptimizable() == ConfigurationPlugInSingleRowFunction.FilterOptimizable.ENABLED &&
                chainSpec.size() == 1 &&
                !isReturnsConstantResult;
    }

    public FilterSpecLookupable getFilterLookupable() {

        ExprDotEvalStaticMethod eval = (ExprDotEvalStaticMethod) evaluator;
        return new FilterSpecLookupable(toExpressionString(), eval, evaluator.getType());
    }

    public String toExpressionString()
	{
        StringBuilder buffer = new StringBuilder();
        ExprNodeUtility.toExpressionString(chainSpec, buffer, false, functionName);
		return buffer.toString();
	}

	public boolean equalsNode(ExprNode node)
	{
		if(!(node instanceof ExprPlugInSingleRowNode))
		{
			return false;
		}

        ExprPlugInSingleRowNode other = (ExprPlugInSingleRowNode) node;
        if (other.chainSpec.size() != this.chainSpec.size()) {
            return false;
        }
        for (int i = 0; i < chainSpec.size(); i++) {
            if (!(this.chainSpec.get(i).equals(other.chainSpec.get(i)))) {
                return false;
            }
        }
        return other.clazz == this.clazz && other.functionName.endsWith(this.functionName);
	}

	public void validate(ExprValidationContext validationContext) throws ExprValidationException
	{
        ExprNodeUtility.validate(chainSpec, validationContext);

        // get first chain item
        List<ExprChainedSpec> chainList = new ArrayList<ExprChainedSpec>(chainSpec);
        ExprChainedSpec firstItem = chainList.remove(0);

		// Get the types of the parameters for the first invocation
        boolean allowWildcard = validationContext.getStreamTypeService().getEventTypes().length == 1;
        EventType streamZeroType = null;
        if (validationContext.getStreamTypeService().getEventTypes().length > 0) {
            streamZeroType = validationContext.getStreamTypeService().getEventTypes()[0];
        }
        final ExprNodeUtilMethodDesc staticMethodDesc = ExprNodeUtility.resolveMethodAllowWildcardAndStream(clazz.getName(), null, firstItem.getName(), firstItem.getParameters(), validationContext.getMethodResolutionService(), validationContext.getEventAdapterService(), validationContext.getStatementId(), allowWildcard, streamZeroType, new ExprNodeUtilResolveExceptionHandlerDefault(firstItem.getName(), true), functionName);

        boolean allowValueCache = true;
        if (config.getValueCache() == ConfigurationPlugInSingleRowFunction.ValueCache.DISABLED) {
            isReturnsConstantResult = false;
            allowValueCache = false;
        }
        else if (config.getValueCache() == ConfigurationPlugInSingleRowFunction.ValueCache.CONFIGURED) {
            isReturnsConstantResult = validationContext.getMethodResolutionService().isUdfCache() && staticMethodDesc.isAllConstants() && chainList.isEmpty();
            allowValueCache = validationContext.getMethodResolutionService().isUdfCache();
        }
        else if (config.getValueCache() == ConfigurationPlugInSingleRowFunction.ValueCache.ENABLED) {
            isReturnsConstantResult = staticMethodDesc.isAllConstants() && chainList.isEmpty();
        }
        else {
            throw new IllegalStateException("Invalid value cache code " + config.getValueCache());
        }

        // this may return a pair of null if there is no lambda or the result cannot be wrapped for lambda-function use
        ExprDotStaticMethodWrap optionalLambdaWrap = ExprDotStaticMethodWrapFactory.make(staticMethodDesc.getReflectionMethod(), validationContext.getEventAdapterService(), chainList);
        ExpressionReturnType typeInfo = optionalLambdaWrap != null ? optionalLambdaWrap.getTypeInfo() : ExpressionReturnType.singleValue(staticMethodDesc.getReflectionMethod().getReturnType());

        ExprDotEval[] eval = ExprDotNodeUtility.getChainEvaluators(typeInfo, chainList, validationContext, false, new ExprDotNodeFilterAnalyzerInputStatic()).getChainWithUnpack();
        evaluator = new ExprDotEvalStaticMethod(validationContext.getStatementName(), clazz.getName(), staticMethodDesc.getFastMethod(), staticMethodDesc.getChildEvals(), allowValueCache && staticMethodDesc.isAllConstants(), optionalLambdaWrap, eval, config.isRethrowExceptions(), null);

        // If caching the result, evaluate now and return the result.
        if (isReturnsConstantResult) {
            final Object result = evaluator.evaluate(null, true, null);
            evaluator = new ExprEvaluator() {
                public Object evaluate(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
                    return result;
                }

                public Class getType() {
                    return staticMethodDesc.getFastMethod().getReturnType();
                }

                public Map<String, Object> getEventType() throws ExprValidationException {
                    return null;
                }
            };
        }
	}

    @Override
    public void accept(ExprNodeVisitor visitor) {
        super.accept(visitor);
        ExprNodeUtility.acceptChain(visitor, chainSpec);
    }

    @Override
    public void accept(ExprNodeVisitorWithParent visitor) {
        super.accept(visitor);
        ExprNodeUtility.acceptChain(visitor, chainSpec);
    }

    @Override
    public void acceptChildnodes(ExprNodeVisitorWithParent visitor, ExprNode parent) {
        super.acceptChildnodes(visitor, parent);
        ExprNodeUtility.acceptChain(visitor, chainSpec, this);
    }

    @Override
    public void replaceUnlistedChildNode(ExprNode nodeToReplace, ExprNode newNode) {
        ExprNodeUtility.replaceChainChildNode(nodeToReplace, newNode, chainSpec);
    }

    public List<ExprNode> getAdditionalNodes() {
        return ExprNodeUtility.collectChainParameters(chainSpec);
    }
}
