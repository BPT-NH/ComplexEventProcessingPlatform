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

import com.espertech.esper.client.EventType;
import com.espertech.esper.client.context.ContextPartitionIdentifier;
import com.espertech.esper.core.context.stmt.StatementAIResourceRegistryFactory;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.ContextDetail;
import com.espertech.esper.epl.spec.ContextDetailPartitionItem;
import com.espertech.esper.filter.FilterSpecCompiled;
import com.espertech.esper.filter.FilterSpecLookupable;
import com.espertech.esper.filter.FilterValueSetParam;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public interface ContextControllerFactory {

    public ContextControllerFactoryContext getFactoryContext();

    public Map<String, Object> getContextBuiltinProps();
    public boolean isSingleInstanceContext();
    public ContextDetail getContextDetail();
    public List<ContextDetailPartitionItem> getContextDetailPartitionItems();
    public StatementAIResourceRegistryFactory getStatementAIResourceRegistryFactory();

    public void validateFactory() throws ExprValidationException;
    public ContextControllerStatementCtxCache validateStatement(ContextControllerStatementBase statement) throws ExprValidationException;
    public ContextController createNoCallback(int pathId, ContextControllerLifecycleCallback callback);
    public void populateFilterAddendums(IdentityHashMap<FilterSpecCompiled, FilterValueSetParam[]> filterAddendum, ContextControllerStatementDesc statement, Object key, int contextId);

    public FilterSpecLookupable getFilterLookupable(EventType eventType);

    public ContextStateCache getStateCache();

    public ContextPartitionIdentifier keyPayloadToIdentifier(Object payload);
}
