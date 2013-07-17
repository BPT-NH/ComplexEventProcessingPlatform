package sushi.correlation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiMapTree;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * Provides methods to correlate existing and incoming events to process instances using correlation rules.
 */
public class RuleCorrelator {
	
	/**
	 * Defines the correlation for a process using correlation rules and correlates existing events for the process.
	 * 
	 * @param correlationRules set of correlation rules (pairs of attributes belonging to the same or different event type)
	 * defining the correlation of the given process (e.g. E1.A=E2.B, E2.G=E3.H, E3.H=E4.H for event types E1, E2, E3, E4)
	 * @param process the process from which the process instances are derived and created
	 * @param timeCondition (optional) rule for advanced time correlation related to the process
	 */
	public static void correlate(Set<CorrelationRule> correlationRules, SushiProcess process, TimeCondition timeCondition) {
		Set<SushiEventType> eventTypes = new HashSet<SushiEventType>();
		for (CorrelationRule rule : correlationRules) {
			process.addCorrelationRule(rule);
			eventTypes.add(rule.getFirstAttribute().getEventType());
			eventTypes.add(rule.getSecondAttribute().getEventType());
		}
		if (timeCondition != null) {
			timeCondition.save();
			process.setTimeCondition(timeCondition);
		}
		process.setEventTypes(eventTypes);
		process.merge();
		Set<SushiEvent> eventsToCorrelate = new HashSet<SushiEvent>();
		for(SushiEventType actualEventType : eventTypes){
			eventsToCorrelate.addAll(SushiEvent.findByEventType(actualEventType));
		}
		Iterator<SushiEvent> eventIterator = eventsToCorrelate.iterator();
		while(eventIterator.hasNext()){
			SushiEvent actualEvent = eventIterator.next();
			correlateEventToProcessInstance(actualEvent, correlationRules, process, timeCondition);
		}
	}
	
	/**
	 * Correlates an event to a process instance using correlation rules.
	 * If no matching process instance is found, a new process instance is created and the event is be correlated to this instance.
	 * 
	 * @param actualEvent the event to be correlated to a process instance
	 * @param correlationRules set of correlation rules (pairs of attributes belonging to the same or different event type)
	 * defining the correlation of the given process (e.g. E1.A=E2.B, E2.G=E3.H, E3.H=E4.H for event types E1, E2, E3, E4)
	 * @param process the process from which the process instances are derived and created
	 * @param timeCondition (optional) rule for advanced time correlation related to the process
	 */
	static void correlateEventToProcessInstance(SushiEvent actualEvent, Set<CorrelationRule> correlationRules, SushiProcess process, TimeCondition timeCondition) {
		
		boolean insertedInExistingProcessInstance = false;
		List<SushiProcessInstance> processInstances = SushiProcessInstance.findByProcess(process);
		Set<SushiProcessInstance> matchedProcessInstances = new HashSet<SushiProcessInstance>();
		
		/*
		 * Looking for matching existing process instances. 
		 * If no rule for advanced time correlation is provided, the event is related to a process instance 
		 * if their values defined through the correlation rules are equal.
		 */
		for (SushiProcessInstance actualProcessInstance : processInstances) {
			boolean processInstanceAndEventMatch = false;
			SushiMapTree<String, Serializable> valueTreeOfProcessInstance = actualProcessInstance.getCorrelationAttributesAndValues();
			SushiMapTree<String, Serializable> valueTreeOfEvent = actualEvent.getValues();
			for (CorrelationRule actualCorrelationRule : correlationRules) {
				if (actualCorrelationRule.getFirstAttribute().getEventType().equals(actualEvent.getEventType())) {
					String qualifiedAttributeName = actualCorrelationRule.getSecondAttribute().getQualifiedAttributeName();
					String attributeExpression = actualCorrelationRule.getFirstAttribute().getAttributeExpression();
					if (valueTreeOfProcessInstance.get(qualifiedAttributeName) != null) {
						if (valueTreeOfProcessInstance.get(qualifiedAttributeName).toString().equals(valueTreeOfEvent.get(attributeExpression).toString())) {
							processInstanceAndEventMatch = true;
						} else {
							processInstanceAndEventMatch = false;
							break;
						}
						continue;
					}
				}
				if (actualCorrelationRule.getSecondAttribute().getEventType().equals(actualEvent.getEventType())) {
					String qualifiedAttributeName = actualCorrelationRule.getFirstAttribute().getQualifiedAttributeName();
					String attributeExpression = actualCorrelationRule.getSecondAttribute().getAttributeExpression();
					if (valueTreeOfProcessInstance.get(qualifiedAttributeName) != null) {
						if (valueTreeOfProcessInstance.get(qualifiedAttributeName).toString().equals(valueTreeOfEvent.get(attributeExpression).toString())) {
							processInstanceAndEventMatch = true;
						} else {
							processInstanceAndEventMatch = false;
							break;
						}
						continue;
					}
				}
			}
			if (processInstanceAndEventMatch) {
				matchedProcessInstances.add(actualProcessInstance);
			}
		}
		/* 
		 * If the event matches to exactly one process instance: 
		 * If no rule for advanced time correlation is provided, 
		 * 		the event is added to the process instance.
		 * If a rule for advanced time correlation is provided, 
		 * 		the event must additionally belong to the time period defined in the rule for advanced time correlation. 
		 * 
		 * If the event matches to more than one process instance: 
		 * If no rule for advanced time correlation is provided, 
		 * 		the process instances are merged to one process instance and the event is added to the process instance.
		 * If a rule for advanced time correlation is provided, 
		 * 		the event must additionally belong to the time period defined in the rule for advanced time correlation.
		 * 		The process instances will be merged only if the timer events are equal.
		 * 
		 * The event is finally added to the process instance(s).
		 */
		if (!matchedProcessInstances.isEmpty()) {
			for (SushiProcessInstance actualProcessInstance : matchedProcessInstances) {
				if (timeCondition == null || timeCondition.belongsEventToTimerEvent(actualEvent, actualProcessInstance.getTimerEvent())) {
					Iterator<SushiProcessInstance> processInstanceIterator = matchedProcessInstances.iterator();
					while (processInstanceIterator.hasNext()) {
						SushiProcessInstance processInstanceToMerge = processInstanceIterator.next();
						if (processInstanceToMerge != actualProcessInstance && (processInstanceToMerge.getTimerEvent() == null || processInstanceToMerge.getTimerEvent().equals(actualProcessInstance.getTimerEvent()))) {
							for (SushiEvent eventToMerge : processInstanceToMerge.getEvents()) {						
								if (!actualProcessInstance.getEvents().contains(eventToMerge)) {
									actualProcessInstance.addEvent(eventToMerge);
									eventToMerge.addProcessInstance(actualProcessInstance);
									eventToMerge.save();
								}
							}
							// The correlation values are merged here.
							actualProcessInstance.getCorrelationAttributesAndValues().putAll(processInstanceToMerge.getCorrelationAttributesAndValues());
							processInstanceToMerge.remove();
							System.out.println("Process instance merged and removed.");
						}
					}
					storeCorrelationValuesOfProcessInstance(actualProcessInstance, correlationRules, actualEvent);
					
					actualProcessInstance.addEvent(actualEvent);
					actualProcessInstance.merge();
					
					actualEvent.addProcessInstance(actualProcessInstance);
					actualEvent.merge();
					
					insertedInExistingProcessInstance = true;
					break;
				}
			}
		}
		
		/*
		 * If no process instance is matched and no rule for advanced time correlation is defined, 
		 * a new process instance is created here, the correlation values are taken from the event and 
		 * stored in the process instance. The event is finally added to the new process instance.
		 * If no process instance is matched and a rule for advanced time correlation is defined, 
		 * a new process instance is created here only if a timer event serving as the benchmark 
		 * to which the advanced time correlation can be related exists and is not already 
		 * related to a process instance.
		 */
		if (!insertedInExistingProcessInstance) {
			SushiProcessInstance newProcessInstance = new SushiProcessInstance();
			if (timeCondition != null) {
				SushiEvent timerEvent = timeCondition.getTimerEventForEvent(actualEvent, correlationRules);
				if (timerEvent == null) {
					return;
				} else {
					newProcessInstance.setTimerEvent(timerEvent);
				}
			}
			
			storeCorrelationValuesOfProcessInstance(newProcessInstance, correlationRules, actualEvent);
			
			newProcessInstance.addEvent(actualEvent);
			newProcessInstance.save();
			
			actualEvent.addProcessInstance(newProcessInstance);
			actualEvent.merge();
			
			process.addProcessInstance(newProcessInstance);
			process.save();
			
			System.out.println("New process instance added.");
		}
	}
	
	/**
	 * Helper method to store correlation values in a given process instance based on the new event.
	 * May cascade over the given correlation rules. Example: Event from type E2 has values c=3 and d=4. 
	 * Correlation rules are E1.a=E2.c and E2.d=E3.d - correlation values are. E1.a=3, E2.c=3, E2.d=4 and E3.d=4.
	 * 
	 * @param processInstance the process instance where the correlation values have to be stored
	 * @param correlationRules set of correlation rules defining the correlation of the given process
	 * @param event the event where the values for correlation are derived from
	 */
	private static void storeCorrelationValuesOfProcessInstance(SushiProcessInstance processInstance, Set<CorrelationRule> correlationRules, SushiEvent event) {
		List<SushiAttribute> correlationAttributes = extractCorrelationAttributes(correlationRules);
//		Set<CorrelationRule> correlationRules = new HashSet<CorrelationRule>(correlationRulesOfProcess);
		for (SushiAttribute actualCorrelationAttribute : correlationAttributes) {
			if (actualCorrelationAttribute.getEventType().equals(event.getEventType())) {
				String qualifiedAttributeName = actualCorrelationAttribute.getQualifiedAttributeName();
				String attributeExpression = actualCorrelationAttribute.getAttributeExpression();
				Serializable correlationValue = event.getValues().get(attributeExpression);
				if (!processInstance.getCorrelationAttributesAndValues().containsKey(qualifiedAttributeName)) {
					processInstance.getCorrelationAttributesAndValues().put(qualifiedAttributeName, correlationValue);
//					correlationAttributes.remove(actualCorrelationAttribute);
					// find related attribute
					for (CorrelationRule actualCorrelationRule : correlationRules) {
						if (actualCorrelationAttribute.equalsWithEventType(actualCorrelationRule.getFirstAttribute())) {
							SushiAttribute relatedAttribute = actualCorrelationRule.getSecondAttribute();
							if (!processInstance.getCorrelationAttributesAndValues().containsKey(relatedAttribute.getQualifiedAttributeName())) {
								processInstance.getCorrelationAttributesAndValues().put(relatedAttribute.getQualifiedAttributeName(), correlationValue);
//								correlationAttributes.remove(relatedAttribute);
//								correlationRules.remove(actualCorrelationRule);
								storeCorrelationValuesOfProcessInstance(processInstance, correlationRules, correlationAttributes, relatedAttribute, correlationValue);
							}
						}
						if (actualCorrelationAttribute.equalsWithEventType(actualCorrelationRule.getSecondAttribute())) {
							SushiAttribute relatedAttribute = actualCorrelationRule.getFirstAttribute();
							if (!processInstance.getCorrelationAttributesAndValues().containsKey(relatedAttribute.getQualifiedAttributeName())) {
								processInstance.getCorrelationAttributesAndValues().put(relatedAttribute.getQualifiedAttributeName(), correlationValue);
//								correlationAttributes.remove(relatedAttribute);
//								correlationRules.remove(actualCorrelationRule);
								storeCorrelationValuesOfProcessInstance(processInstance, correlationRules, correlationAttributes, relatedAttribute, correlationValue);
							}
						}
					}
				}
			}
		}
	}
	
	private static void storeCorrelationValuesOfProcessInstance(SushiProcessInstance processInstance, Set<CorrelationRule> correlationRules, List<SushiAttribute> correlationAttributes, SushiAttribute actualAttribute, Serializable correlationValue) {
		for (CorrelationRule actualCorrelationRule : correlationRules) {
			if (actualAttribute.equalsWithEventType(actualCorrelationRule.getFirstAttribute())) {
				SushiAttribute relatedAttribute = actualCorrelationRule.getSecondAttribute();
				if (!processInstance.getCorrelationAttributesAndValues().containsKey(relatedAttribute.getQualifiedAttributeName())) {
					processInstance.getCorrelationAttributesAndValues().put(relatedAttribute.getQualifiedAttributeName(), correlationValue);
					storeCorrelationValuesOfProcessInstance(processInstance, correlationRules, correlationAttributes, relatedAttribute, correlationValue);
				}
			}
			if (actualAttribute.equalsWithEventType(actualCorrelationRule.getSecondAttribute())) {
				SushiAttribute relatedAttribute = actualCorrelationRule.getFirstAttribute();
				if (!processInstance.getCorrelationAttributesAndValues().containsKey(relatedAttribute.getQualifiedAttributeName())) {
					processInstance.getCorrelationAttributesAndValues().put(relatedAttribute.getQualifiedAttributeName(), correlationValue);
					storeCorrelationValuesOfProcessInstance(processInstance, correlationRules, correlationAttributes, relatedAttribute, correlationValue);
				}
			}
		}
	}
	
	/**
	 * Helper method to extract all attributes from the correlation rules. Unique by attribute expression and event type.
	 * 
	 * @param correlationRules correlation rules where the attributes have to be extracted from
	 * @return list of event type attributes
	 */
	private static List<SushiAttribute> extractCorrelationAttributes(Set<CorrelationRule> correlationRules) {
		List<SushiAttribute> correlationAttributes = new ArrayList<SushiAttribute>();
		for (CorrelationRule actualCorrelationRule : correlationRules) {
			boolean firstAttributeInList = false, secondAttributeInList = false;
			for (SushiAttribute correlationAttribute : correlationAttributes) {
				if (correlationAttribute.equalsWithEventType(actualCorrelationRule.getFirstAttribute())) {
					firstAttributeInList = true;
				}
				if (correlationAttribute.equalsWithEventType(actualCorrelationRule.getSecondAttribute())) {
					secondAttributeInList = true;
				}
			}
			if (!firstAttributeInList) {
				correlationAttributes.add(actualCorrelationRule.getFirstAttribute());
			}
			if (!secondAttributeInList) {
				correlationAttributes.add(actualCorrelationRule.getSecondAttribute());
			}
		}
		return correlationAttributes;
	}
}
