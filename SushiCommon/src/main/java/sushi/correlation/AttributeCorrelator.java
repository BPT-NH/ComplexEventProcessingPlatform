package sushi.correlation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * Provides methods to correlate existing and incoming events to process instances using single event type attributes.
 */
public class AttributeCorrelator {
	
	/**
	 * Defines the correlation for a process using single event type attributes and correlates existing events for the process.
	 * One definition of a correlation per process only.
	 * 
	 * @param selectedEventTypes events of the given event types will be correlated - event types must be related to the process
	 * @param correlationAttributes single event type attributes defining the correlation of the given process - 
	 * all the event types must contain this attribute (i.e. if an attribute with attribute expression 'A' is given and the event types 
	 * are E1, E2, and E3, the correlation is E1.A=E2.A, E1.A=E3.A and E2.A=E3.A
	 * @param process the process from which the process instances are derived and created
	 * @param timeCondition (optional) rule for advanced time correlation related to the process
	 */
	public static void correlate(List<SushiEventType> selectedEventTypes, List<SushiAttribute> correlationAttributes, SushiProcess process, TimeCondition timeCondition) {
		process.addCorrelationAttributes(correlationAttributes);
		if (timeCondition != null) {
			timeCondition.save();
			process.setTimeCondition(timeCondition);
		}
		process.merge();
		Set<SushiEvent> eventsToCorrelate = new HashSet<SushiEvent>();
		for (SushiEventType actualEventType : selectedEventTypes) {
			eventsToCorrelate.addAll(SushiEvent.findByEventType(actualEventType));
		}
		Iterator<SushiEvent> eventIterator = eventsToCorrelate.iterator();
		while (eventIterator.hasNext()) {
			SushiEvent actualEvent = eventIterator.next();
			correlateEventToProcessInstance(actualEvent, correlationAttributes, process, timeCondition);
		}
	}
	
	/**
	 * Correlates an event to a process instance using single event type attributes.
	 * If no matching process instance is found, a new process instance is created and the event is be correlated to this instance.
	 * 
	 * @param actualEvent the event to be correlated to a process instance
	 * @param correlationAttributes single event type attributes defining the correlation of the given process - 
	 * all the event types must contain this attribute (i.e. if an attribute with attribute expression 'A' is given and the event types 
	 * are E1, E2, and E3, the correlation is E1.A=E2.A, E1.A=E3.A and E2.A=E3.A
	 * @param process the process from which the process instances are derived and created
	 * @param timeCondition (optional) rule for advanced time correlation related to the process
	 */
	static void correlateEventToProcessInstance(SushiEvent actualEvent, List<SushiAttribute> correlationAttributes, SushiProcess process, TimeCondition timeCondition) {
		
		boolean insertedInExistingProcessInstance = false;
		List<SushiProcessInstance> processInstances = SushiProcessInstance.findByProcess(process);
		
		/*
		 * Looking for a match from existing process instances. 
		 * The event is related to a process instance if their values of all correlation attributes are equal.
		 * If a rule for advanced time correlation is provided, the event must additionally belong to the time period defined in the rule for advanced time correlation. 
		 * The event is finally added to the process instance.
		 */
		for (SushiProcessInstance actualProcessInstance : processInstances) {
			boolean processInstanceAndEventMatch = true;
			for (SushiAttribute actualCorrelationAttribute : correlationAttributes) {
				String nameOfactualAttribute = actualCorrelationAttribute.getAttributeExpression();
				SushiMapTree<String, Serializable> valueTreeOfProcessInstance = actualProcessInstance.getCorrelationAttributesAndValues();
				SushiMapTree<String, Serializable> valueTreeOfEvent = actualEvent.getValues();
				if (!valueTreeOfProcessInstance.get(nameOfactualAttribute).toString().equals(valueTreeOfEvent.get(nameOfactualAttribute).toString())) {
					processInstanceAndEventMatch = false;
					break;
				}
			}
			if (processInstanceAndEventMatch && timeCondition != null) {
				processInstanceAndEventMatch = timeCondition.belongsEventToTimerEvent(actualEvent, actualProcessInstance.getTimerEvent());
			}
			if (processInstanceAndEventMatch) {
				actualProcessInstance.addEvent(actualEvent);
				actualProcessInstance.merge();
				
				actualEvent.addProcessInstance(actualProcessInstance);
				actualEvent.merge();
				
				insertedInExistingProcessInstance = true;
				break;
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
				SushiEvent timerEvent = timeCondition.getTimerEventForEvent(actualEvent, correlationAttributes);
				if (timerEvent == null) {
					return;
				} else {
					newProcessInstance.setTimerEvent(timerEvent);
				}
			}
			for (SushiAttribute actualCorrelationAttribute : correlationAttributes) {
				String correlationAttribute = actualCorrelationAttribute.getAttributeExpression();
				Serializable correlationValue = actualEvent.getValues().get(actualCorrelationAttribute.getAttributeExpression());
				newProcessInstance.getCorrelationAttributesAndValues().put(correlationAttribute, correlationValue);
			}
			newProcessInstance.addEvent(actualEvent);
			newProcessInstance.save();
			
			actualEvent.addProcessInstance(newProcessInstance);
			actualEvent.merge();
			
			process.addProcessInstance(newProcessInstance);
			process.save();
			
			System.out.println("New process instance added!");
		}
	}
}
