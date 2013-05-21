/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.view;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.collection.UniformPair;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.epl.core.ResultSetProcessor;

/**
 * Handles output rate limiting for FIRST, only applicable with a having-clause and no group-by clause.
 * <p>
 * Without having-clause the order of processing won't matter therefore its handled by the
 * {@link OutputProcessViewConditionDefault}. With group-by the {@link com.espertech.esper.epl.core.ResultSetProcessor} handles the per-group first criteria.
 */
public class OutputProcessViewConditionFirstPostProcess extends OutputProcessViewConditionFirst
{
    private final OutputStrategyPostProcess postProcessor;

    public OutputProcessViewConditionFirstPostProcess(ResultSetProcessor resultSetProcessor, Long afterConditionTime, Integer afterConditionNumberOfEvents, boolean afterConditionSatisfied, OutputProcessViewConditionFactory parent, AgentInstanceContext agentInstanceContext, OutputStrategyPostProcess postProcessor) {
        super(resultSetProcessor, afterConditionTime, afterConditionNumberOfEvents, afterConditionSatisfied, parent, agentInstanceContext);
        this.postProcessor = postProcessor;
    }

    public void output(boolean forceUpdate, UniformPair<EventBean[]> results)
    {
        // Child view can be null in replay from named window
        if (childView != null)
        {
            postProcessor.output(forceUpdate, results, childView);
        }
    }
}
