package sushi.simulation;

import java.util.HashMap;
import java.util.Map;

import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AbstractBPMNGateway;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEventType;

public class SimulationUtils {

	//TODO: methodenname nicht aussagekräftig
	public static Map<AbstractBPMNElement, String> getBPMNElementsFromEventTypes(Map<SushiEventType, String> eventTypesDurationStringMap, BPMNProcess bpmnProcess){
		Map<AbstractBPMNElement, String> tasksDurationString = new HashMap<AbstractBPMNElement, String>();
		for(AbstractBPMNElement bpmnElement : bpmnProcess.getSubElementsWithMonitoringpoints()){
			for(MonitoringPoint monitoringPoint : bpmnElement.getMonitoringPoints()){
				tasksDurationString.put(bpmnElement, eventTypesDurationStringMap.get(monitoringPoint.getEventType()));
			}
		}
		return tasksDurationString;
	}
	
	public static Map<AbstractBPMNElement, DerivationType> getBPMNElementsFromEventTypes2(Map<SushiEventType, DerivationType> eventTypesDurationStringMap, BPMNProcess bpmnProcess){
		Map<AbstractBPMNElement, DerivationType> tasksDurationString = new HashMap<AbstractBPMNElement, DerivationType>();
		for(AbstractBPMNElement bpmnElement : bpmnProcess.getSubElementsWithMonitoringpoints()){
			for(MonitoringPoint monitoringPoint : bpmnElement.getMonitoringPoints()){
				tasksDurationString.put(bpmnElement, eventTypesDurationStringMap.get(monitoringPoint.getEventType()));
			}
		}
		return tasksDurationString;
	}
	
	public static AbstractBPMNElement getBPMNElementFromEventType(SushiEventType eventType, BPMNProcess bpmnProcess){
		for(AbstractBPMNElement bpmnElement : bpmnProcess.getSubElementsWithMonitoringpoints()){
			for(MonitoringPoint monitoringPoint : bpmnElement.getMonitoringPoints()){
				if(eventType.equals(monitoringPoint.getEventType())){
					return bpmnElement;
				}
			}
		}
		return null;
		
	}
	
	public static Map<AbstractBPMNElement, Long> getDurationsFromMap(Map<AbstractBPMNElement, String> tasksDurationString) {
		Map<AbstractBPMNElement, Long> bpmnElementsDuration = new HashMap<AbstractBPMNElement, Long>();
		for(AbstractBPMNElement bpmnElement : tasksDurationString.keySet()){
			bpmnElementsDuration.put(bpmnElement, getDurationFromString(tasksDurationString.get(bpmnElement)));
		}
		return bpmnElementsDuration;
	}
	
	private static long getDurationFromString(String string) {
		Long duration = (long) 0;
		if(string != null){
			if(string.contains(":")){
				String[] timestrings = string.split(":");
				for(int i = 0; i < timestrings.length; i++){
					duration = duration + ( Long.parseLong(timestrings[i]) * (1000 * 60 * 60 * 60/ (60 *(i + 1))));
				}
			}
			else{
				duration = Long.parseLong(string);
				duration = duration * 1000 * 60 * 60;
			}
		}
		return duration;
	}

	public static Map<BPMNXORGateway, Map<AbstractBPMNElement, String>> getXORSplitsWithPathProbability(Map<Object, String> probabilityStrings, BPMNProcess model) {
		Map<BPMNXORGateway, Map<AbstractBPMNElement, String>> xorSplitsWithPathProbability = new HashMap<BPMNXORGateway, Map<AbstractBPMNElement, String>>();
		for(AbstractBPMNGateway gateway : model.getAllSplitGateways()){
			if(gateway instanceof BPMNXORGateway){
				Map<AbstractBPMNElement, String> pathProbability = new HashMap<AbstractBPMNElement, String>();
				for(AbstractBPMNElement successor : gateway.getSuccessors()){
					//TODO: mapping von baumstruktur auf bpmn modell
				}
				xorSplitsWithPathProbability.put((BPMNXORGateway) gateway, pathProbability);
			}
		}
		return xorSplitsWithPathProbability;
	}
}
