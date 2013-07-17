package sushi.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AbstractBPMNGateway;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEventType;
import sushi.util.Tuple;

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

	public static Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, SushiEventType>>> getXORSplitsWithFollowingEventTypes(BPMNProcess model) {
		Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, SushiEventType>>> xorSplitsWithFolowingElementAndEventType = new HashMap<BPMNXORGateway, List<Tuple<AbstractBPMNElement, SushiEventType>>>();
		for(AbstractBPMNGateway gateway : model.getAllSplitGateways()){
			if(gateway instanceof BPMNXORGateway){
				List<Tuple<AbstractBPMNElement, SushiEventType>> successorAndFollowingEventTypes = new ArrayList<Tuple<AbstractBPMNElement, SushiEventType>>();
				for(AbstractBPMNElement successor : gateway.getSuccessors()){
					AbstractBPMNElement element = successor;
					//falls es einen Leeren Pfad zum join gibt
					if(model.getAllJoinGateways().contains(successor)){
						successorAndFollowingEventTypes.add(new Tuple<AbstractBPMNElement, SushiEventType>(null, null));
					}
					else{
						while(!(element instanceof BPMNTask)){
							element = element.getSuccessors().iterator().next();
						}
						Tuple<AbstractBPMNElement, SushiEventType> tuple = new Tuple(successor, element.getMonitoringPoints().get(0).getEventType());
						successorAndFollowingEventTypes.add(tuple);
						 xorSplitsWithFolowingElementAndEventType.put((BPMNXORGateway) gateway, successorAndFollowingEventTypes);
					}
				}
			}
		}
		return xorSplitsWithFolowingElementAndEventType;
	}

	public static Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>> convertProbabilityStrings(Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, String>>> xorSplitsWithSuccessorProbabilityStrings) {
		Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>> xorSplitsWithSuccessorProbabilities = new HashMap<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>>();
		Integer percent;
		for(BPMNXORGateway xorGateway : xorSplitsWithSuccessorProbabilityStrings.keySet()){
			percent = 0;
			List<Tuple<AbstractBPMNElement,Integer>> successorProbabilityList = new ArrayList<Tuple<AbstractBPMNElement,Integer>>();
			for(Tuple<AbstractBPMNElement, String> successorProbabilityString : xorSplitsWithSuccessorProbabilityStrings.get(xorGateway)){
				percent = percent + Integer.parseInt(successorProbabilityString.y);
				successorProbabilityList.add(new Tuple<AbstractBPMNElement, Integer>(successorProbabilityString.x, percent));
			}
			xorSplitsWithSuccessorProbabilities.put(xorGateway, successorProbabilityList);
		}
		return xorSplitsWithSuccessorProbabilities;
	}
}
