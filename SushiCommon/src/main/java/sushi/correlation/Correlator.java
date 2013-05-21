package sushi.correlation;

import java.io.Serializable;
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
 * @author micha
 *
 */
public class Correlator {
	
	public static void correlate(List<SushiEvent> events){
		for(SushiEvent event : events){
			SushiEventType eventType = event.getEventType();
			List<SushiProcess> processes = SushiProcess.findByEventType(eventType);
			for(SushiProcess process : processes){
				correlateEventToProcessInstance(event, process.getCorrelationAttributes(), process, process.getTimeCondition());
			}
		}
	}
	
	public static void correlate(List<SushiEventType> selectedEventTypes, List<SushiAttribute> correlationAttributes, SushiProcess selectedProcess, TimeCondition timeCondition) {
		selectedProcess.addCorrelationAttributes(correlationAttributes);
		if(timeCondition != null){
			timeCondition.save();
			selectedProcess.setTimeCondition(timeCondition);
		}
		selectedProcess.merge();
		Set<SushiEvent> eventsToCorrelate = new HashSet<SushiEvent>();
		for(SushiEventType actualEventType : selectedEventTypes){
			eventsToCorrelate.addAll(SushiEvent.findByEventType(actualEventType));
		}
		Iterator<SushiEvent> eventIterator = eventsToCorrelate.iterator();
		while(eventIterator.hasNext()){
			SushiEvent actualEvent = eventIterator.next();
			correlateEventToProcessInstance(actualEvent, correlationAttributes, selectedProcess, timeCondition);
		}
	}
	
	/**
	 * Korrelation von Event zu Prozessinstanz.
	 * Mit dem Prozess können alle bereits existierenden Prozessinstanzen ermittelt werden.
	 * Dann wird über alle Prozessinstanzen iteriert und anhand der correlationAttributes verglichen, ob Event und Prozessinstanz übereinstimmen.
	 * @param actualEvent
	 * @param correlationAttributes
	 * @param process
	 * @param timeCondition 
	 */
	private static void correlateEventToProcessInstance(SushiEvent actualEvent, List<SushiAttribute> correlationAttributes, SushiProcess process, TimeCondition timeCondition) {
		boolean insertedInExistingProcessInstance = false;
		List<SushiProcessInstance> processInstances = SushiProcessInstance.findByProcess(process);
		//Vergleichen,
		for(SushiProcessInstance actualProcessInstance : processInstances){
			boolean processInstanceAndEventMatch = true;
			for(SushiAttribute actualCorrelationAttribute : correlationAttributes){
				String nameOfactualAttribute = actualCorrelationAttribute.getAttributeExpression();
				SushiMapTree<String, Serializable> valueTreeOfProcessInstance = actualProcessInstance.getCorrelationAttributesAndValues();
				SushiMapTree<String, Serializable> valueTreeOfEvent = actualEvent.getValues();
				if(!valueTreeOfProcessInstance.get(nameOfactualAttribute).equals(valueTreeOfEvent.get(nameOfactualAttribute))){
					processInstanceAndEventMatch = false;
					break;
				}
			}
			//Auf die Zeit testen
			if(processInstanceAndEventMatch && timeCondition != null){
				processInstanceAndEventMatch = timeCondition.belongsEventToTimerEvent(actualEvent, actualProcessInstance.getTimerEvent());
			}
			if(processInstanceAndEventMatch){
				actualProcessInstance.addEvent(actualEvent);
				actualProcessInstance.merge();
				
				actualEvent.addProcessInstance(actualProcessInstance);
				actualEvent.merge();
				
				insertedInExistingProcessInstance = true;
				break;
			}
		}
		if(!insertedInExistingProcessInstance){
			SushiProcessInstance newProcessInstance = new SushiProcessInstance();
			if(timeCondition != null){
				SushiEvent timerEvent = timeCondition.getTimerEventForEvent(actualEvent, correlationAttributes);
				
				//Abbruch, falls Event zu keiner Prozessinstanz korreliert werden kann
				if(timerEvent == null){
					return;
				}
				else{
//					timerEvent.save();
					newProcessInstance.setTimerEvent(timerEvent);
				}
			}
			//Ablegen der relevanten correlationWerte in der neuen ProzessInstanz
			for(SushiAttribute actualCorrelationAttribute : correlationAttributes) {
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
		actualEvent.save();
	}
	
	public static void removeExistingCorrelation(SushiProcess selectedProcess){
		List<SushiProcessInstance> existingProcessInstances = SushiProcessInstance.findByProcess(selectedProcess);
		for(SushiProcessInstance processInstance : existingProcessInstances){
			processInstance.remove();
		}
	}

}
