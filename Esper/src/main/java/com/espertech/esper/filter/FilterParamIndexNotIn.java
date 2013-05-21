/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.filter;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.collection.MultiKeyUntyped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Index for filter parameter constants to match using the 'not in' operator to match against a
 * all other values then the supplied set of values.
 */
public final class FilterParamIndexNotIn extends FilterParamIndexLookupableBase
{
    private final Map<Object, Set<EventEvaluator>> constantsMap;
    private final Map<MultiKeyUntyped, EventEvaluator> filterValueEvaluators;
    private final Set<EventEvaluator> evaluatorsSet;
    private final ReadWriteLock constantsMapRWLock;

    public FilterParamIndexNotIn(FilterSpecLookupable lookupable)
    {
        super(FilterOperator.NOT_IN_LIST_OF_VALUES, lookupable);

        constantsMap = new HashMap<Object, Set<EventEvaluator>>();
        filterValueEvaluators = new HashMap<MultiKeyUntyped, EventEvaluator>();
        evaluatorsSet = new HashSet<EventEvaluator>();
        constantsMapRWLock = new ReentrantReadWriteLock();
    }

    public final EventEvaluator get(Object filterConstant)
    {
        MultiKeyUntyped keyValues = (MultiKeyUntyped) filterConstant;
        return filterValueEvaluators.get(keyValues);
    }

    public final void put(Object filterConstant, EventEvaluator evaluator)
    {
        // Store evaluator keyed to set of values
        MultiKeyUntyped keys = (MultiKeyUntyped) filterConstant;
        filterValueEvaluators.put(keys, evaluator);
        evaluatorsSet.add(evaluator);

        // Store each value to match against in Map with it's evaluator as a list
        Object[] keyValues = keys.getKeys();
        for (Object keyValue : keyValues)
        {
            Set<EventEvaluator> evaluators = constantsMap.get(keyValue);
            if (evaluators == null)
            {
                evaluators = new HashSet<EventEvaluator>();
                constantsMap.put(keyValue, evaluators);
            }
            evaluators.add(evaluator);
        }
    }

    public final boolean remove(Object filterConstant)
    {
        MultiKeyUntyped keys = (MultiKeyUntyped) filterConstant;

        // remove the mapping of value set to evaluator
        EventEvaluator eval = filterValueEvaluators.remove(keys);
        evaluatorsSet.remove(eval);
        boolean isRemoved = false;
        if (eval != null)
        {
            isRemoved = true;
        }

        Object[] keyValues = keys.getKeys();
        for (Object keyValue : keyValues)
        {
            Set<EventEvaluator> evaluators = constantsMap.get(keyValue);
            if (evaluators != null) // could already be removed as constants may be the same
            {
                evaluators.remove(eval);
                if (evaluators.isEmpty())
                {
                    constantsMap.remove(keyValue);
                }
            }
        }
        return isRemoved;
    }

    public final int size()
    {
        return constantsMap.size();
    }

    public final ReadWriteLock getReadWriteLock()
    {
        return constantsMapRWLock;
    }

    public final void matchEvent(EventBean theEvent, Collection<FilterHandle> matches)
    {
        Object attributeValue = lookupable.getGetter().get(theEvent);

        if (attributeValue == null)
        {
            return;
        }

        // Look up in hashtable the set of not-in evaluators
        constantsMapRWLock.readLock().lock();
        Set<EventEvaluator> evalNotMatching = constantsMap.get(attributeValue);

        // if all known evaluators are matching, invoke all
        if (evalNotMatching == null)
        {
            try {
                for (EventEvaluator eval : evaluatorsSet)
                {
                    eval.matchEvent(theEvent, matches);
                }
            }
            finally {
                constantsMapRWLock.readLock().unlock();
            }
            return;
        }

        // if none are matching, we are done
        if (evalNotMatching.size() == evaluatorsSet.size())
        {
            constantsMapRWLock.readLock().unlock();
            return;
        }

        // handle partial matches: loop through all evaluators and see which one should not be matching, match all else
        try {
            for (EventEvaluator eval : evaluatorsSet)
            {
                if (!(evalNotMatching.contains(eval)))
                {
                    eval.matchEvent(theEvent, matches);
                }
            }
        }
        finally {
            constantsMapRWLock.readLock().unlock();
        }
    }

    private static final Log log = LogFactory.getLog(FilterParamIndexNotIn.class);
}
