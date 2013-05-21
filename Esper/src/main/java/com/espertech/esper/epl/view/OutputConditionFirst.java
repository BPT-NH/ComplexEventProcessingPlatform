/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.view;

import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.OutputLimitLimitType;
import com.espertech.esper.epl.spec.OutputLimitSpec;

/**
 * An output condition that is satisfied at the first event
 * of either a time-based or count-based batch.
 */
public class OutputConditionFirst extends OutputConditionBase implements OutputCondition
{
	private final OutputCondition innerCondition;
	private boolean witnessedFirst;

	public OutputConditionFirst(OutputCallback outputCallback, AgentInstanceContext agentInstanceContext, OutputConditionFactory innerConditionFactory)
    {
		super(outputCallback);
		OutputCallback localCallback = createCallbackToLocal();
		this.innerCondition = innerConditionFactory.make(agentInstanceContext, localCallback);
		this.witnessedFirst = false;
	}

	public void updateOutputCondition(int newEventsCount, int oldEventsCount)
	{
		if(!witnessedFirst)
		{
			witnessedFirst = true;
			boolean doOutput = true;
			boolean forceUpdate = false;
			outputCallback.continueOutputProcessing(doOutput, forceUpdate);
		}
		innerCondition.updateOutputCondition(newEventsCount, oldEventsCount);
	}

	private OutputCallback createCallbackToLocal()
	{
		return new OutputCallback()
		{
			public void continueOutputProcessing(boolean doOutput, boolean forceUpdate)
			{
				OutputConditionFirst.this.continueOutputProcessing(forceUpdate);
			}
		};
	}

    public void terminated() {
        outputCallback.continueOutputProcessing(true, true);
    }

	private void continueOutputProcessing(boolean forceUpdate)
	{
		boolean doOutput = !witnessedFirst;
		outputCallback.continueOutputProcessing(doOutput, forceUpdate);
		witnessedFirst = false;
	}
}
