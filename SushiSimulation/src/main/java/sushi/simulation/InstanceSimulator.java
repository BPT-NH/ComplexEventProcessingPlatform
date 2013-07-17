package sushi.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.Broker;
/**
 * Represents the simulation of one process instance
 */
public class InstanceSimulator {

	public List<PathSimulator> pathSimulators;
	private Simulator simulator;
	private Map<BPMNAndGateway, List<AbstractBPMNElement>> andJoinsVisitedPredecessors;
	private Map<SushiAttribute, List<Serializable>> attributesAndValues;
	private List<SushiAttribute> differingAttributes;

	public InstanceSimulator(AbstractBPMNElement startElement, Simulator simulator, Map<SushiAttribute, List<Serializable>> attributesAndValues, Date currentSimulationDate, List<SushiAttribute> differingAttributes){
		this.setSimulator(simulator);
		this.attributesAndValues = attributesAndValues;
		this.differingAttributes = differingAttributes;
		PathSimulator initialPathSimulator = new PathSimulator(startElement, this, currentSimulationDate);
		pathSimulators = new ArrayList<PathSimulator>();
		pathSimulators.add(initialPathSimulator);
		andJoinsVisitedPredecessors = new HashMap<BPMNAndGateway, List<AbstractBPMNElement>>();
	}
	
	 /**
	  * gets the earliest PathSimulator and starts it
	  */
	public void simulateStep(){

		List<SushiEvent> newEvents = getEarliestSubSimulator().continueSimulation();
		Random random = new Random();
		int index;
		for(SushiEvent event : newEvents){
			SushiEventType eventType = event.getEventType();
			for(SushiAttribute attribute : eventType.getValueTypes()){
				for(SushiAttribute key : attributesAndValues.keySet()){
					if(attribute.equals(key)){
						List<Serializable> values = attributesAndValues.get(key);
						index = random.nextInt(values.size());
						event.getValues().put(attribute.getAttributeExpression(), values.get(index));
						for(SushiAttribute differingAttribute : differingAttributes){
							if(differingAttribute.equals(attribute)){
								if(values.size() > 1){
									attributesAndValues.get(key).remove(index);
								}
								break;
							}
						}
						break;
					}
				}
			}
			Broker.send(event);
		}
		if(pathSimulators.isEmpty()){
			getSimulator().unsubscribe(this);
		}
	}

	public void unsubscribe(PathSimulator simulator) {
		pathSimulators.remove(simulator);
	}

	public Simulator getSimulator() {
		return simulator;
	}

	private void setSimulator(Simulator parentSimulator) {
		this.simulator = parentSimulator;
	}

	public PathSimulator getEarliestSubSimulator(){
		PathSimulator earliestSubSimulator = pathSimulators.get(0);
		for(PathSimulator subSimulator : pathSimulators){
			if(subSimulator.getCurrentSimulationDate().before(earliestSubSimulator.getCurrentSimulationDate())){
				earliestSubSimulator = subSimulator;
			}
		}
		return earliestSubSimulator;
	}

	public Date getEarliestDate(){
		return getEarliestSubSimulator().getCurrentSimulationDate();
	}
	
	public void addJoinPredecessorToGateway(AbstractBPMNElement predecessor, BPMNAndGateway gateway){
		if(andJoinsVisitedPredecessors.containsKey(gateway)){
			andJoinsVisitedPredecessors.get(gateway).add(predecessor);
		}
		else{
			List<AbstractBPMNElement> predecessorList = new ArrayList<AbstractBPMNElement>();
			predecessorList.add(predecessor);
			andJoinsVisitedPredecessors.put(gateway, predecessorList);
		}
	}
	
	public Boolean allPredecessorsOfGatewayVisited(BPMNAndGateway gateway){
		return andJoinsVisitedPredecessors.containsKey(gateway) &&
				andJoinsVisitedPredecessors.get(gateway).containsAll(gateway.getPredecessors());
	}
	
	public void resetGateway(BPMNAndGateway gateway){
		for(AbstractBPMNElement predecessor : gateway.getPredecessors()){
			andJoinsVisitedPredecessors.get(gateway).remove(predecessor);
		}
	}
}
