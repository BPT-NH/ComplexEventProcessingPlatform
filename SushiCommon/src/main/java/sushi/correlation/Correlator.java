package sushi.correlation;

import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * Provides methods to correlate existing and incoming events to process instances.
 */
public class Correlator {
	
	/**
	 * Correlation for incoming events.
	 * Retrieves the processes that are related to each event via its event type.
	 * Tries to correlate the events to process instances.
	 * Definition of correlations by both single event type attributes 
	 * (attributes with the same attribute expression belonging to all the given event types) 
	 * and correlation rules (pairs of attributes belonging to the same or different event type) 
	 * are supported.
	 * 
	 * @param events events to be correlated
	 */
	public static void correlate(List<SushiEvent> events) {
		for (SushiEvent event : events) {
			SushiEventType eventType = event.getEventType();
			List<SushiProcess> processes = SushiProcess.findByEventType(eventType);
			for (SushiProcess process : processes) {
				if (process.isCorrelationWithCorrelationRules()) {
					RuleCorrelator.correlateEventToProcessInstance(event, process.getCorrelationRules(), process, process.getTimeCondition());
				} else {
					AttributeCorrelator.correlateEventToProcessInstance(event, process.getCorrelationAttributes(), process, process.getTimeCondition());
				}
			}
		}
	}
	
	/**
	 * Destroys all instances related to the given process.
	 * @param selectedProcess
	 */
	public static void removeExistingCorrelation(SushiProcess selectedProcess) {
		List<SushiProcessInstance> existingProcessInstances = SushiProcessInstance.findByProcess(selectedProcess);
		for (SushiProcessInstance processInstance : existingProcessInstances) {
			processInstance.remove();
		}
	}
}
