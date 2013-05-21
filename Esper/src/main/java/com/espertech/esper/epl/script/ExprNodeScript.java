/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.epl.script;

import com.espertech.esper.client.hook.EPLScriptContext;
import com.espertech.esper.epl.core.EngineImportException;
import com.espertech.esper.epl.expression.*;
import com.espertech.esper.epl.script.jsr223.ExpressionScriptCompiledJSR223;
import com.espertech.esper.epl.script.jsr223.JSR223Helper;
import com.espertech.esper.epl.script.mvel.ExpressionScriptCompiledMVEL;
import com.espertech.esper.epl.script.mvel.MVELHelper;
import com.espertech.esper.epl.spec.ExpressionScriptCompiled;
import com.espertech.esper.epl.spec.ExpressionScriptProvided;
import com.espertech.esper.util.JavaClassHelper;

import javax.script.CompiledScript;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExprNodeScript extends ExprNodeBase {

    public static String CONTEXT_BINDING_NAME = "epl";
    private static final long serialVersionUID = 2661218104424440161L;

    private final String defaultDialect;
    private final ExpressionScriptProvided script;
    private final List<ExprNode> parameters;

    private transient ExprEvaluator evaluator;

    public ExprNodeScript(String defaultDialect, ExpressionScriptProvided script, List<ExprNode> parameters) {
        this.defaultDialect = defaultDialect;
        this.script = script;
        this.parameters = parameters;
    }

    public ExprEvaluator getExprEvaluator() {
        return evaluator;
    }

    public List<ExprNode> getParameters() {
        return parameters;
    }

    public String toExpressionString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(script.getName());
        ExprNodeUtility.toExpressionStringIncludeParen(parameters, buffer);
        return buffer.toString();
    }

    public ExpressionScriptProvided getScript() {
        return script;
    }

    public boolean isConstantResult() {
        return false;
    }

    public boolean equalsNode(ExprNode node) {
        if (this == node) return true;
        if (node == null || getClass() != node.getClass()) return false;

        ExprNodeScript that = (ExprNodeScript) node;

        if (script != null ? !script.equals(that.script) : that.script != null) return false;
        return ExprNodeUtility.deepEquals(parameters, that.parameters);
    }

    public void validate(ExprValidationContext validationContext) throws ExprValidationException {

        if (evaluator != null) {
            return;
        }

        if (script.getParameterNames().size() != parameters.size()) {
            throw new ExprValidationException("Invalid number of parameters for script '" + script.getName() + "', expected " + script.getParameterNames().size() + " parameters but received " + parameters.size() + " parameters");
        }

        // validate all expression parameters
        List<ExprNode> validatedParameters = new ArrayList<ExprNode>();
        for (ExprNode expr : parameters) {
            validatedParameters.add(ExprNodeUtility.getValidatedSubtree(expr, validationContext));
        }

        // set up map of input parameter names and evaluators
        String[] inputParamNames = new String[script.getParameterNames().size()];
        ExprEvaluator[] evaluators = new ExprEvaluator[script.getParameterNames().size()];

        for (int i = 0; i < script.getParameterNames().size(); i++) {
            inputParamNames[i] = script.getParameterNames().get(i);
            evaluators[i] = validatedParameters.get(i).getExprEvaluator();
        }

        // Compile script
        if (script.getCompiled() == null) {
            compileScript(evaluators);
        }

        // Determine declared return type
        Class declaredReturnType = getDeclaredReturnType(script.getOptionalReturnTypeName(), validationContext);
        if (script.isOptionalReturnTypeIsArray() && declaredReturnType != null) {
            declaredReturnType = JavaClassHelper.getArrayType(declaredReturnType);
        }
        Class returnType;
        if (script.getCompiled().getKnownReturnType() == null && script.getOptionalReturnTypeName() == null) {
            returnType = Object.class;
        }
        else if (script.getCompiled().getKnownReturnType() != null) {
            if (declaredReturnType == null) {
                returnType = script.getCompiled().getKnownReturnType();
            }
            else {
                Class knownReturnType = script.getCompiled().getKnownReturnType();
                if (declaredReturnType.isArray() && knownReturnType.isArray()) {
                    // we are fine
                }
                else if (!JavaClassHelper.isAssignmentCompatible(knownReturnType, declaredReturnType)) {
                    throw new ExprValidationException("Return type and declared type not compatible for script '" + script.getName() + "', known return type is " + knownReturnType.getName() + " versus declared return type " + declaredReturnType.getName());
                }
                returnType = declaredReturnType;
            }
        }
        else {
            returnType = declaredReturnType;
        }
        if (returnType == null) {
            returnType = Object.class;
        }

        // Prepare evaluator - this sets the evaluator
        prepareEvaluator(validationContext.getStatementName(), inputParamNames, evaluators, returnType);
    }

    private void compileScript(ExprEvaluator[] evaluators)
            throws ExprValidationException
    {
        String dialect = script.getOptionalDialect() == null ? defaultDialect : script.getOptionalDialect();

        ExpressionScriptCompiled compiled;
        if (dialect.toLowerCase().trim().equals("mvel")) {
            Map<String, Class> mvelInputParamTypes = new HashMap<String, Class>();
            for (int i = 0; i < script.getParameterNames().size(); i++) {
                String mvelParamName = script.getParameterNames().get(i);
                mvelInputParamTypes.put(mvelParamName, evaluators[i].getType());
            }
            mvelInputParamTypes.put(CONTEXT_BINDING_NAME, EPLScriptContext.class);
            compiled = MVELHelper.compile(script.getName(), script.getExpression(), mvelInputParamTypes);
        }
        else {
            CompiledScript compiledScript = JSR223Helper.verifyCompileScript(script, dialect);
            compiled = new ExpressionScriptCompiledJSR223(compiledScript);
        }
        script.setCompiled(compiled);
    }

    private void prepareEvaluator(String statementName, String[] inputParamNames, ExprEvaluator[] evaluators, Class returnType) {
        if (script.getCompiled() instanceof ExpressionScriptCompiledMVEL) {
            ExpressionScriptCompiledMVEL mvel = (ExpressionScriptCompiledMVEL) script.getCompiled();
            evaluator = new ExprNodeScriptEvalMVEL(script.getName(), statementName, inputParamNames, evaluators, returnType, mvel.getCompiled());
        }
        else {
            ExpressionScriptCompiledJSR223 jsr223 = (ExpressionScriptCompiledJSR223) script.getCompiled();
            evaluator = new ExprNodeScriptEvalJSR223(script.getName(), statementName, inputParamNames, evaluators, returnType, jsr223.getCompiled());
        }
    }

    private Class getDeclaredReturnType(String returnTypeName, ExprValidationContext validationContext)
            throws ExprValidationException
    {
        if (returnTypeName == null) {
            return null;
        }

        if (returnTypeName.equals("void")) {
            return null;
        }

        Class returnType = JavaClassHelper.getClassForSimpleName(returnTypeName);
        if (returnType != null) {
            return returnType;
        }

        try {
            return validationContext.getMethodResolutionService().resolveClass(returnTypeName);
        }
        catch (EngineImportException e1) {
            throw new ExprValidationException("Failed to resolve return type '" + returnTypeName + "' specified for script '" + script.getName() + "'");
        }
    }
}
