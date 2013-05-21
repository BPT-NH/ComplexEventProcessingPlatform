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

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.context.*;
import com.espertech.esper.core.context.util.ContextControllerSelectorUtil;
import com.espertech.esper.client.context.ContextPartitionState;
import com.espertech.esper.epl.spec.ContextDetailCondition;
import com.espertech.esper.epl.spec.ContextDetailConditionCrontab;
import com.espertech.esper.event.EventAdapterService;
import com.espertech.esper.pattern.MatchedEventMap;
import com.espertech.esper.pattern.MatchedEventMapImpl;
import com.espertech.esper.schedule.ScheduleComputeHelper;
import com.espertech.esper.schedule.ScheduleSpec;
import com.espertech.esper.type.NumberSetParameter;

import java.util.*;

public class ContextControllerInitTerm implements ContextController, ContextControllerConditionCallback {

    protected final int pathId;
    protected final ContextControllerLifecycleCallback activationCallback;
    protected final ContextControllerInitTermFactory factory;

    protected ContextControllerCondition startCondition;

    protected Map<ContextControllerCondition, ContextControllerInitTermInstance> endConditions = new LinkedHashMap<ContextControllerCondition, ContextControllerInitTermInstance>();

    protected int currentSubpathId;

    public ContextControllerInitTerm(int pathId, ContextControllerLifecycleCallback lifecycleCallback, ContextControllerInitTermFactory factory) {
        this.pathId = pathId;
        this.activationCallback = lifecycleCallback;
        this.factory = factory;
    }

    public void importContextPartitions(ContextControllerState state, int pathIdToUse, ContextInternalFilterAddendum filterAddendum, AgentInstanceSelector agentInstanceSelector) {
        initializeFromState(null, null, filterAddendum, state, pathIdToUse, agentInstanceSelector);
    }

    public void deletePath(ContextPartitionIdentifier identifier) {
        ContextPartitionIdentifierInitiatedTerminated initterm = (ContextPartitionIdentifierInitiatedTerminated) identifier;
        for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entry : endConditions.entrySet()) {
            if (ContextControllerInitTerm.compare(initterm.getStartTime(), initterm.getProperties(), initterm.getEndTime(),
                    entry.getValue().getStartTime(), entry.getValue().getStartProperties(), entry.getValue().getEndTime())) {
                entry.getKey().deactivate();
                endConditions.remove(entry.getKey());
                break;
            }
        }
    }

    public void activate(EventBean optionalTriggeringEvent, Map<String, Object> optionalTriggeringPattern, ContextControllerState controllerState, ContextInternalFilterAddendum filterAddendum, Integer importPathId) {

        if (factory.getFactoryContext().getNestingLevel() == 1) {
            controllerState = ContextControllerStateUtil.getRecoveryStates(factory.getStateCache(), factory.getFactoryContext().getOutermostContextName());
        }
        if (controllerState == null) {
            startCondition = makeEndpoint(factory.getContextDetail().getStart(), filterAddendum, true, 0);

            // if this is single-instance mode, check if we are currently running according to schedule
            boolean currentlyRunning = false;
            if (!factory.getContextDetail().isOverlapping()) {
                currentlyRunning = determineCurrentlyRunning(startCondition);
            }

            if (currentlyRunning) {
                currentSubpathId++;
                ContextControllerCondition endEndpoint = makeEndpoint(factory.getContextDetail().getEnd(), filterAddendum, false, currentSubpathId);
                endEndpoint.activate(optionalTriggeringEvent, null, 0, factory.getFactoryContext().isRecoveringResilient());
                long startTime = factory.getSchedulingService().getTime();
                Long endTime = endEndpoint.getExpectedEndTime();
                Map<String, Object> builtinProps = getBuiltinProperties(factory, startTime, endTime, Collections.<String, Object>emptyMap());
                ContextControllerInstanceHandle instanceHandle = activationCallback.contextPartitionInstantiate(null, currentSubpathId, null, this, optionalTriggeringEvent, optionalTriggeringPattern, null, builtinProps, controllerState, filterAddendum, factory.getFactoryContext().isRecoveringResilient(), ContextPartitionState.STARTED);
                endConditions.put(endEndpoint, new ContextControllerInitTermInstance(instanceHandle, null, startTime, endTime, currentSubpathId));

                ContextControllerInitTermState state = new ContextControllerInitTermState(factory.getFactoryContext().getServicesContext().getSchedulingService().getTime(), builtinProps);
                factory.getStateCache().addContextPath(factory.getFactoryContext().getOutermostContextName(), factory.getFactoryContext().getNestingLevel(), pathId, currentSubpathId, instanceHandle.getContextPartitionOrPathId(), state, factory.getBinding());
            }
            else {
                startCondition.activate(optionalTriggeringEvent, null, 0, factory.getFactoryContext().isRecoveringResilient());
            }
            return;
        }

        startCondition = makeEndpoint(factory.getContextDetail().getStart(), filterAddendum, true, 0);

        // if this is single-instance mode, check if we are currently running according to schedule
        boolean currentlyRunning = false;
        if (!factory.getContextDetail().isOverlapping()) {
            currentlyRunning = determineCurrentlyRunning(startCondition);
        }
        if (!currentlyRunning) {
            startCondition.activate(optionalTriggeringEvent, null, 0, factory.getFactoryContext().isRecoveringResilient());
        }

        int pathIdToUse = importPathId != null ? importPathId : pathId;
        initializeFromState(optionalTriggeringEvent, optionalTriggeringPattern, filterAddendum, controllerState, pathIdToUse, null);
    }

    protected ContextControllerCondition makeEndpoint(ContextDetailCondition endpoint, ContextInternalFilterAddendum filterAddendum, boolean isStartEndpoint, int subPathId) {
        return ContextControllerConditionFactory.getEndpoint(factory.getFactoryContext().getContextName(), factory.getFactoryContext().getServicesContext(), factory.getFactoryContext().getAgentInstanceContextCreate(),
                endpoint, this, filterAddendum, isStartEndpoint,
                factory.getFactoryContext().getNestingLevel(), pathId, subPathId);
    }

    public void visitSelectedPartitions(ContextPartitionSelector contextPartitionSelector, ContextPartitionVisitor visitor) {
        int nestingLevel = factory.getFactoryContext().getNestingLevel();
        if (contextPartitionSelector instanceof ContextPartitionSelectorFiltered) {
            ContextPartitionSelectorFiltered filter = (ContextPartitionSelectorFiltered) contextPartitionSelector;
            ContextPartitionIdentifierInitiatedTerminated identifier = new ContextPartitionIdentifierInitiatedTerminated();
            for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entry : endConditions.entrySet()) {
                identifier.setEndTime(entry.getValue().getEndTime());
                identifier.setStartTime(entry.getValue().getStartTime());
                identifier.setProperties(entry.getValue().getStartProperties());
                identifier.setContextPartitionId(entry.getValue().getInstanceHandle().getContextPartitionOrPathId());
                if (filter.filter(identifier)) {
                    ContextControllerInitTermState state = new ContextControllerInitTermState(factory.getFactoryContext().getServicesContext().getSchedulingService().getTime(), entry.getValue().getStartProperties());
                    visitor.visit(nestingLevel, pathId, factory.getBinding(), state, this, entry.getValue().getInstanceHandle());
                }
            }
            return;
        }
        if (contextPartitionSelector instanceof ContextPartitionSelectorById) {
            ContextPartitionSelectorById filter = (ContextPartitionSelectorById) contextPartitionSelector;
            for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entry : endConditions.entrySet()) {
                if (filter.getContextPartitionIds().contains(entry.getValue().getInstanceHandle().getContextPartitionOrPathId())) {
                    ContextControllerInitTermState state = new ContextControllerInitTermState(factory.getFactoryContext().getServicesContext().getSchedulingService().getTime(), entry.getValue().getStartProperties());
                    visitor.visit(nestingLevel, pathId, factory.getBinding(), state, this, entry.getValue().getInstanceHandle());
                }
            }
            return;
        }
        if (contextPartitionSelector instanceof ContextPartitionSelectorAll) {
            for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entry : endConditions.entrySet()) {
                ContextControllerInitTermState state = new ContextControllerInitTermState(factory.getFactoryContext().getServicesContext().getSchedulingService().getTime(), entry.getValue().getStartProperties());
                visitor.visit(nestingLevel, pathId, factory.getBinding(), state, this, entry.getValue().getInstanceHandle());
            }
            return;
        }
        throw ContextControllerSelectorUtil.getInvalidSelector(new Class[0], contextPartitionSelector);
    }

    public void rangeNotification(Map<String, Object> builtinProperties, ContextControllerCondition originCondition, EventBean optionalTriggeringEvent, Map<String, Object> optionalTriggeringPattern, ContextInternalFilterAddendum filterAddendum) {
        // handle start-condition notification
        if (originCondition == startCondition) {

            // For single-instance mode, deactivate
            if (!factory.getContextDetail().isOverlapping()) {
                if (startCondition.isRunning()) {
                    startCondition.deactivate();
                }
            }
            // For overlapping mode, make sure we activate again or stay activated
            else {
                if (!startCondition.isRunning()) {
                    startCondition.activate(null, null, 0, factory.getFactoryContext().isRecoveringResilient());
                }
            }

            currentSubpathId++;
            ContextControllerCondition endEndpoint = makeEndpoint(factory.getContextDetail().getEnd(), filterAddendum, false, currentSubpathId);
            MatchedEventMap matchedEventMap = getMatchedEventMap(builtinProperties);
            endEndpoint.activate(null, matchedEventMap, 0, false);
            long startTime = factory.getSchedulingService().getTime();
            Long endTime = endEndpoint.getExpectedEndTime();
            Map<String, Object> builtinProps = getBuiltinProperties(factory, startTime, endTime, builtinProperties);
            ContextControllerInstanceHandle instanceHandle = activationCallback.contextPartitionInstantiate(null, currentSubpathId, null, this, optionalTriggeringEvent, optionalTriggeringPattern, matchedEventMap, builtinProps, null, filterAddendum, factory.getFactoryContext().isRecoveringResilient(), ContextPartitionState.STARTED);
            endConditions.put(endEndpoint, new ContextControllerInitTermInstance(instanceHandle, builtinProperties, startTime, endTime, currentSubpathId));

            ContextControllerInitTermState state = new ContextControllerInitTermState(factory.getFactoryContext().getServicesContext().getSchedulingService().getTime(), builtinProperties);
            factory.getStateCache().addContextPath(factory.getFactoryContext().getOutermostContextName(), factory.getFactoryContext().getNestingLevel(), pathId, currentSubpathId, instanceHandle.getContextPartitionOrPathId(), state, factory.getBinding());
        }
        else {
            if (originCondition.isRunning()) {
                originCondition.deactivate();
            }

            // indicate terminate
            ContextControllerInitTermInstance instance = endConditions.remove(originCondition);
            if (instance == null) {
                return;
            }
            activationCallback.contextPartitionTerminate(instance.getInstanceHandle(), builtinProperties);

            // re-activate start condition if not overlapping
            if (!factory.getContextDetail().isOverlapping()) {
                startCondition.activate(optionalTriggeringEvent, null, 0, false);
            }

            factory.getStateCache().removeContextPath(factory.getFactoryContext().getOutermostContextName(), factory.getFactoryContext().getNestingLevel(), pathId, instance.getSubPathId());
        }
    }

    protected MatchedEventMap getMatchedEventMap(Map<String, Object> builtinProperties) {
        Object[] props = new Object[factory.getMatchedEventMapMeta().getTagsPerIndex().length];
        int count = 0;
        for (String name : factory.getMatchedEventMapMeta().getTagsPerIndex()) {
            props[count++] = builtinProperties.get(name);
        }
        return new MatchedEventMapImpl(factory.getMatchedEventMapMeta(), props);
    }

    public void setContextPartitionRange(List<NumberSetParameter> partitionRanges) {
        throw new UnsupportedOperationException();
    }

    protected boolean determineCurrentlyRunning(ContextControllerCondition startCondition) {

        // we are not currently running if either of the endpoints is not crontab-triggered
        if ((factory.getContextDetail().getStart() instanceof ContextDetailConditionCrontab) &&
           ((factory.getContextDetail().getEnd() instanceof ContextDetailConditionCrontab)))     {
            ScheduleSpec scheduleStart = ((ContextDetailConditionCrontab) factory.getContextDetail().getStart()).getSchedule();
            ScheduleSpec scheduleEnd = ((ContextDetailConditionCrontab) factory.getContextDetail().getEnd()).getSchedule();
            long nextScheduledStartTime = ScheduleComputeHelper.computeNextOccurance(scheduleStart, factory.getTimeProvider().getTime());
            long nextScheduledEndTime = ScheduleComputeHelper.computeNextOccurance(scheduleEnd, factory.getTimeProvider().getTime());
            return nextScheduledStartTime >= nextScheduledEndTime;
        }

        if (startCondition instanceof ContextControllerConditionTimePeriod) {
            ContextControllerConditionTimePeriod condition = (ContextControllerConditionTimePeriod) startCondition;
            Long endTime = condition.getExpectedEndTime();
            if (endTime != null && endTime <= 0) {
                return true;
            }
        }

        return false;
    }

    public ContextControllerFactory getFactory() {
        return factory;
    }

    public int getPathId() {
        return pathId;
    }

    public void deactivate() {
        if (startCondition != null) {
            if (startCondition.isRunning()) {
                startCondition.deactivate();
            }
        }

        for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entry : endConditions.entrySet()) {
            if (entry.getKey().isRunning()) {
                entry.getKey().deactivate();
            }
        }
        endConditions.clear();
        factory.getStateCache().removeContextParentPath(factory.getFactoryContext().getOutermostContextName(), factory.getFactoryContext().getNestingLevel(), pathId);
    }

    protected static Map<String, Object> getBuiltinProperties(ContextControllerInitTermFactory factory, long startTime, Long endTime, Map<String, Object> startEndpointData) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ContextPropertyEventType.PROP_CTX_NAME, factory.getFactoryContext().getContextName());
        props.put(ContextPropertyEventType.PROP_CTX_STARTTIME, startTime);
        props.put(ContextPropertyEventType.PROP_CTX_ENDTIME, endTime);
        props.putAll(startEndpointData);
        return props;
    }

    private void initializeFromState(EventBean optionalTriggeringEvent,
                                     Map<String, Object> optionalTriggeringPattern,
                                     ContextInternalFilterAddendum filterAddendum,
                                     ContextControllerState controllerState,
                                     int pathIdToUse,
                                     AgentInstanceSelector agentInstanceSelector) {

        TreeMap<ContextStatePathKey, ContextStatePathValue> states = controllerState.getStates();
        NavigableMap<ContextStatePathKey, ContextStatePathValue> childContexts = ContextControllerStateUtil.getChildContexts(factory.getFactoryContext(), pathIdToUse, states);
        EventAdapterService eventAdapterService = factory.getFactoryContext().getServicesContext().getEventAdapterService();

        int maxSubpathId = Integer.MIN_VALUE;
        for (Map.Entry<ContextStatePathKey, ContextStatePathValue> entry : childContexts.entrySet()) {
            ContextControllerInitTermState state = (ContextControllerInitTermState) factory.getBinding().byteArrayToObject(entry.getValue().getBlob(), eventAdapterService);

            if (controllerState.isImported()) {
                Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> existing = null;
                for (Map.Entry<ContextControllerCondition, ContextControllerInitTermInstance> entryExisting : endConditions.entrySet()) {
                    if (compare(state.getStartTime(), state.getPatternData(), null,
                                entryExisting.getValue().getStartTime(), entryExisting.getValue().getStartProperties(), null)) {
                        existing = entryExisting;
                        break;
                    }
                }
                if (existing != null) {
                    ContextControllerInstanceHandle existingHandle = existing.getValue().getInstanceHandle();
                    if (existingHandle != null) {
                        activationCallback.contextPartitionNavigate(existingHandle, this, controllerState, entry.getValue().getOptionalContextPartitionId(), filterAddendum, agentInstanceSelector, entry.getValue().getBlob());
                        continue;
                    }
                }
            }

            ContextControllerCondition endEndpoint = makeEndpoint(factory.getContextDetail().getEnd(), filterAddendum, false, entry.getKey().getSubPath());
            long timeOffset = factory.getFactoryContext().getServicesContext().getSchedulingService().getTime() - state.getStartTime();

            endEndpoint.activate(optionalTriggeringEvent, null, timeOffset, factory.getFactoryContext().isRecoveringResilient());
            long startTime = state.getStartTime();
            Long endTime = endEndpoint.getExpectedEndTime();
            Map<String, Object> builtinProps = getBuiltinProperties(factory, startTime, endTime, state.getPatternData());
            int contextPartitionId = entry.getValue().getOptionalContextPartitionId();

            int assignedSubPathId = !controllerState.isImported() ? entry.getKey().getSubPath() : ++currentSubpathId;
            ContextControllerInstanceHandle instanceHandle = activationCallback.contextPartitionInstantiate(contextPartitionId, assignedSubPathId, entry.getKey().getSubPath(), this, optionalTriggeringEvent, optionalTriggeringPattern, null, builtinProps, controllerState, filterAddendum, factory.getFactoryContext().isRecoveringResilient(), entry.getValue().getState());
            endConditions.put(endEndpoint, new ContextControllerInitTermInstance(instanceHandle, state.getPatternData(), startTime, endTime, assignedSubPathId));

            if (entry.getKey().getSubPath() > maxSubpathId) {
                maxSubpathId = assignedSubPathId;
            }
        }

        if (!controllerState.isImported()) {
            currentSubpathId = maxSubpathId != Integer.MIN_VALUE ? maxSubpathId : 0;
        }
    }

    public static boolean compare(long savedStartTime,
                                  Map<String, Object> savedProperties,
                                  Long savedEndTime,
                                  long existingStartTime,
                                  Map<String, Object> existingProperties,
                                  Long existingEndTime) {

        if (savedStartTime != existingStartTime) {
            return false;
        }
        if (savedEndTime != null && existingEndTime != null && !savedEndTime.equals(existingEndTime)) {
            return false;
        }

        for (Map.Entry<String, Object> savedEntry : savedProperties.entrySet()) {
            Object existingValue = existingProperties.get(savedEntry.getKey());
            Object savedValue = savedEntry.getValue();
            if (savedValue == null && existingValue == null) {
                continue;
            }
            if (savedValue == null || existingValue == null) {
                return false;
            }
            if (existingValue.equals(savedValue)) {
                continue;
            }
            if (existingValue instanceof EventBean && savedValue instanceof EventBean) {
                if (((EventBean)existingValue).getUnderlying().equals(((EventBean) savedValue).getUnderlying())) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }
}
