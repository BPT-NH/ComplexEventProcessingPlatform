package sushi.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.Broker;

public class InstanceSimulator {

	private List<AbstractBPMNElement> visitedJoinPredecessors;
	public List<SubSimulator> subSimulators;
	private Simulator simulator;
	private Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes;
	private Date currentSimulationDate;

	public InstanceSimulator(AbstractBPMNElement startElement, Simulator simulator, Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes, Date currentSimulationDate){
		this.setSimulator(simulator);
		this.eventTypeAttributes = eventTypeAttributes;
		SubSimulator initialSubSimulator = new SubSimulator(startElement, this, currentSimulationDate);
		subSimulators = new ArrayList<SubSimulator>();
		subSimulators.add(initialSubSimulator);
		visitedJoinPredecessors = new ArrayList<AbstractBPMNElement>();
		this.currentSimulationDate = currentSimulationDate;
	}
	
	public InstanceSimulator(AbstractBPMNElement startElement, Simulator simulator){
		this.setSimulator(simulator);
		SubSimulator initialSubSimulator = new SubSimulator(startElement, this, currentSimulationDate);
		subSimulators = new ArrayList<SubSimulator>();
		subSimulators.add(initialSubSimulator);
		visitedJoinPredecessors = new ArrayList<AbstractBPMNElement>();
		this.eventTypeAttributes = new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>();
	}
	
	public void simulateStep(){
		if(subSimulators.isEmpty()){
			getSimulator().unsubscribe(this);
			return;
		}
		Random random = new Random();
		int index = random.nextInt(subSimulators.size());
		List<SushiEvent> newEvents = subSimulators.get(index).traverseElement();
		for(SushiEvent event : newEvents){
			for(SushiEventType eventType : eventTypeAttributes.keySet()){
				//TODO: filtern, wenn ein eventTyp mehrfach vorkommt!
				if(event.getEventType().equals(eventType)){
					Map<SushiAttribute, List<Serializable>> attributes= eventTypeAttributes.get(eventType);
					for(SushiAttribute attribute : attributes.keySet()){
						List<Serializable> values = attributes.get(attribute);
						//TODO: random? wert auswählen
						event.getValues().put(attribute.getAttributeExpression(), values.get(0));
					}
				}
			}
			System.err.println("Simulate: " + event.getTimestamp().getTime());
			Broker.send(event);
		}
	}

	public void unsubscribe(SubSimulator simulator) {
		subSimulators.remove(simulator);
	}

	public Simulator getSimulator() {
		return simulator;
	}

	private void setSimulator(Simulator parentSimulator) {
		this.simulator = parentSimulator;
	}

	public List<AbstractBPMNElement> getVisitedJoinPredecessors() {
		return visitedJoinPredecessors;
	}

	public void addVisitedJoinPredecessors(AbstractBPMNElement visitedJoinPredecessor) {
		visitedJoinPredecessors.add(visitedJoinPredecessor);
	}

	public boolean allPredecessorsOfElementVisited(AbstractBPMNElement element) {
		for(AbstractBPMNElement joinPredecessor : element.getPredecessors()){
			if(!getVisitedJoinPredecessors().contains(joinPredecessor)){
				return false;
			}
		}
		//entfernen, um Schleifen mit Parallelen anteilen korrekt abzubilden
		getVisitedJoinPredecessors().removeAll(element.getPredecessors());
		return true;
	}

}
