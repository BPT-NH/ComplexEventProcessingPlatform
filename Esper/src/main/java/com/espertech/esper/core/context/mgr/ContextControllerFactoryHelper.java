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

package com.espertech.esper.core.context.mgr;

import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.*;
import com.espertech.esper.filter.FilterSpecCompiled;

import java.util.*;

public class ContextControllerFactoryHelper {

    public static ContextControllerFactory[] getFactory(ContextControllerFactoryServiceContext serviceContext, ContextStateCache contextStateCache) throws ExprValidationException {
        if (!(serviceContext.getDetail() instanceof ContextDetailNested)) {
            ContextControllerFactory factory = buildContextFactory(serviceContext, serviceContext.getContextName(), serviceContext.getDetail(), 1, null, contextStateCache);
            factory.validateFactory();
            return new ContextControllerFactory[] {factory};
        }
        return buildNestedContextFactories(serviceContext, contextStateCache);
    }

    private static ContextControllerFactory[] buildNestedContextFactories(ContextControllerFactoryServiceContext serviceContext, ContextStateCache contextStateCache) throws ExprValidationException {
        ContextDetailNested nestedSpec = (ContextDetailNested) serviceContext.getDetail();
        // determine nested filter use
        Map<CreateContextDesc, List<FilterSpecCompiled>> filtersPerNestedContext = null;
        for (int i = 0; i < nestedSpec.getContexts().size(); i++) {
            CreateContextDesc contextParent = nestedSpec.getContexts().get(i);
            for (int j = i + 1; j < nestedSpec.getContexts().size(); j++) {
                CreateContextDesc contextControlled = nestedSpec.getContexts().get(j);
                List<FilterSpecCompiled> specs = contextControlled.getFilterSpecs();
                if (specs == null) {
                    continue;
                }
                if (filtersPerNestedContext == null) {
                    filtersPerNestedContext = new HashMap<CreateContextDesc, List<FilterSpecCompiled>>();
                }
                List<FilterSpecCompiled> existing = filtersPerNestedContext.get(contextParent);
                if (existing != null) {
                    existing.addAll(specs);
                }
                else {
                    filtersPerNestedContext.put(contextParent, specs);
                }
            }
        }

        // create contexts
        Set<String> namesUsed = new HashSet<String>();
        ContextControllerFactory[] hierarchy = new ContextControllerFactory[nestedSpec.getContexts().size()];
        for (int i = 0; i < nestedSpec.getContexts().size(); i++) {
            CreateContextDesc context = nestedSpec.getContexts().get(i);

            if (namesUsed.contains(context.getContextName())) {
                throw new ExprValidationException("Context by name '" + context.getContextName() + "' has already been declared within nested context '" + serviceContext.getContextName() + "'");
            }
            namesUsed.add(context.getContextName());

            int nestingLevel = i + 1;

            List<FilterSpecCompiled> optFiltersNested = null;
            if (filtersPerNestedContext != null) {
                optFiltersNested = filtersPerNestedContext.get(context);
            }

            hierarchy[i] = buildContextFactory(serviceContext, context.getContextName(), context.getContextDetail(), nestingLevel, optFiltersNested, contextStateCache);
            hierarchy[i].validateFactory();
        }
        return hierarchy;
    }

    private static ContextControllerFactory buildContextFactory(ContextControllerFactoryServiceContext serviceContext, String contextName, ContextDetail detail, int nestingLevel, List<FilterSpecCompiled> optFiltersNested, ContextStateCache contextStateCache) throws ExprValidationException {
        ContextControllerFactoryContext factoryContext = new ContextControllerFactoryContext(serviceContext.getContextName(), contextName, serviceContext.getServicesContext(), serviceContext.getAgentInstanceContextCreate(), nestingLevel, serviceContext.isRecoveringResilient());
        return buildContextFactory(factoryContext, detail, optFiltersNested, contextStateCache);
    }

    private static ContextControllerFactory buildContextFactory(ContextControllerFactoryContext factoryContext, ContextDetail detail, List<FilterSpecCompiled> optFiltersNested, ContextStateCache contextStateCache) throws ExprValidationException {

        ContextControllerFactory factory;
        if (detail instanceof ContextDetailInitiatedTerminated) {
            factory = new ContextControllerInitTermFactory(factoryContext, (ContextDetailInitiatedTerminated) detail, contextStateCache);
        }
        else if (detail instanceof ContextDetailPartitioned) {
            factory = new ContextControllerPartitionedFactory(factoryContext, (ContextDetailPartitioned) detail, optFiltersNested, contextStateCache);
        }
        else if (detail instanceof ContextDetailCategory) {
            factory = new ContextControllerCategoryFactory(factoryContext, (ContextDetailCategory) detail, optFiltersNested, contextStateCache);
        }
        else if (detail instanceof ContextDetailHash) {
            factory = new ContextControllerHashFactory(factoryContext, (ContextDetailHash) detail, optFiltersNested, contextStateCache);
        }
        else {
            throw new UnsupportedOperationException("Context detail " + detail + " is not yet supported in a nested context");
        }

        return factory;
    }
}
