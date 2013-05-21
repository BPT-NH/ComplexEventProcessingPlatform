package sushi.simulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.util.SetUtil;

public class SubSimulator {

	private AbstractBPMNElement currentElement;
	private InstanceSimulator instanceSimulator;
	private Date currentSimulationDate;
	
	public SubSimulator(AbstractBPMNElement startElement, InstanceSimulator parentSimulator, Map<String, String> correlationAttributeValues, Map<SushiEventType, Map<String, String>> eventTypeAttributes, Date currentSimulationDate){
		this.currentElement = startElement;
		this.setInstanceSimulator(parentSimulator);
		this.currentSimulationDate = currentSimulationDate;
	}
	
	public SubSimulator(AbstractBPMNElement startElement, InstanceSimulator parentSimulator, Map<String, String> correlationAttributeValues){
		this.currentElement = startElement;
		this.setInstanceSimulator(parentSimulator);
		currentSimulationDate = new Date();
	}
	
	
	public SubSimulator(AbstractBPMNElement startElement, InstanceSimulator parentSimulator, Date currentSimulationDate){
		this.currentElement = startElement;
		this.setInstanceSimulator(parentSimulator);
		this.currentSimulationDate = currentSimulationDate;
	}
	
	public List<SushiEvent> traverseElement() {
		AbstractBPMNElement nextElement = null;
		List<SushiEvent> newEvents = simulateCurrentElement();
		
		if(!currentElement.hasSuccessors()){
			getInstanceSimulator().unsubscribe(this);
			return newEvents;
		}
		//Bei mehreren ausgehenden Kanten...
		if(currentElement.getSuccessors().size() > 1){
			//Beim XOR-Split eine Kante verfolgen
			//TODO: deferred choice, OR, complex?
			if(currentElement instanceof BPMNXORGateway){
				Random random = new Random();
				int index = random.nextInt(currentElement.getSuccessors().size());
				nextElement = SetUtil.getElement(currentElement.getSuccessors(), index);
			}
			//ansonsten alle Kanten verfolgen
			else{
				for(AbstractBPMNElement successor : currentElement.getSuccessors()){
					//ein Simulator für jede Kante erzeugen
					SubSimulator subSimulator = new SubSimulator(successor, getInstanceSimulator(), currentSimulationDate);
					getInstanceSimulator().subSimulators.add(subSimulator);
				}
				getInstanceSimulator().unsubscribe(this);
			}
		}
		//Nachfolger durchlaufen (eine ausgehende Kante)
		else{
			nextElement = currentElement.getSuccessors().iterator().next();
			if(nextElement instanceof BPMNAndGateway){
				getInstanceSimulator().addVisitedJoinPredecessors(currentElement);
				if(!getInstanceSimulator().allPredecessorsOfElementVisited(nextElement)){
					getInstanceSimulator().unsubscribe(this);
				}
//				currentElement = nextElement;
			}
//			else{
//				currentElement = nextElement;
//			}
		}
		currentElement = nextElement;
		return newEvents;
	}

	private List<SushiEvent> simulateCurrentElement() {
		MonitoringPoint monitoringPoint;
		SushiEvent event;
		List<SushiEvent> newEvents = new ArrayList<SushiEvent>();
		//enable
		monitoringPoint = currentElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.enable);
		if(monitoringPoint != null){
			event = new SushiEvent(monitoringPoint.getEventType(), currentSimulationDate);
			newEvents.add(event);
		}
		//begin
		monitoringPoint = currentElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.begin);
		if(monitoringPoint != null){
			event = new SushiEvent(monitoringPoint.getEventType(), currentSimulationDate);
			newEvents.add(event);
		}
		//ausführung
		currentSimulationDate = new Date(currentSimulationDate.getTime() + getInstanceSimulator().getSimulator().getDurationForBPMNElement(currentElement));
		//terminate
		monitoringPoint = currentElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.terminate);
		if(monitoringPoint != null){
			event = new SushiEvent(monitoringPoint.getEventType(), currentSimulationDate);
			newEvents.add(event);
		}
		return newEvents;
	}

//	private List<SushiEvent> createEventsFromMonitoringPoints(AbstractBPMNElement element) {
//		//TODO: monitoringPointsFor enable and begin; -> date anpassen -> monitoringPoint für terminate
//		List<SushiEvent> newEvents = new ArrayList<SushiEvent>();
//		for(MonitoringPoint monitoringPoint : element.getMonitoringPoints()){
//			SushiEvent event = new SushiEvent(monitoringPoint.getEventType(), currentSimulationDate);
//			newEvents.add(event);
//		}
//		return newEvents;
//	}

	private InstanceSimulator getInstanceSimulator() {
		return instanceSimulator;
	}
	
	private void setInstanceSimulator(InstanceSimulator instanceSimulator) {
		this.instanceSimulator = instanceSimulator;
	}
}
