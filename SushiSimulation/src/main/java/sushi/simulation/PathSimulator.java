package sushi.simulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
/**
 * Represents one active path during the execution of a process 
 */
public class PathSimulator {

	private AbstractBPMNElement currentElement;
	private InstanceSimulator instanceSimulator;
	private Date currentSimulationDate;
	private Boolean currentElementIsTraversed;
	private List<SushiEvent> newEvents;
	
	public PathSimulator(AbstractBPMNElement startElement, InstanceSimulator parentSimulator, Date currentSimulationDate){
		this.setCurrentElement(startElement);
		this.setInstanceSimulator(parentSimulator);
		this.setCurrentSimulationDate(currentSimulationDate);
		this.setCurrentElementIsTraversed(false);
		this.newEvents = new ArrayList<SushiEvent>();
	}
	
	 /**
	  * Traverses the current element or if it is allready traversed continues to the next - produces events
	  */
	public List<SushiEvent> continueSimulation() {
		newEvents.clear();
		if(currentElementIsTraversed){
			addEventFromMonitorinPointTerminate();
			
			AbstractBPMNElement nextElement = getNextElement();
			

			if(getCurrentElement() instanceof BPMNXORGateway){
				skipUnreachableElements(nextElement);
			}

			
			setCurrentElement(nextElement);
			setCurrentElementIsTraversed(false);
			if(getCurrentElement() == null){
				getInstanceSimulator().unsubscribe(this);
			}
			else{
				//Terminate eines Elements bewirkt u.U. Enable des nächsten
				addEventFromMonitorinPointEnable();
			}
		}
		else{
			addEventFromMonitorinPointBegin();
			setCurrentSimulationDate(new Date(getCurrentSimulationDate().getTime() + getInstanceSimulator().getSimulator().getDurationForBPMNElement(getCurrentElement())));
			setCurrentElementIsTraversed(true);
		}
		return newEvents;
	}
	 /**
	  * skips unreachable elements after an XOR-Split by marking all indirect predecessors of the XOR
	  * and demarking all indirect predecessors of any actualElement of a PathSimulator from the same instance
	  */
	private void skipUnreachableElements(AbstractBPMNElement nextElement) {
		Set<AbstractBPMNElement> unreachableElements = getCurrentElement().getIndirectSuccessors();
		if(nextElement != null){
			unreachableElements.remove(nextElement);
			setCurrentElement(nextElement);
			for(PathSimulator pathSimulator : getInstanceSimulator().pathSimulators){
				unreachableElements.removeAll(pathSimulator.getCurrentElement().getIndirectSuccessors());
			}
		}
		skipElements(unreachableElements);
	}

	 /**
	  * Returns true if there is exactly one path to go - creates new PathSimulators if 2 or more paths should be simulated at once
	  */
	private AbstractBPMNElement getNextElement() {
		if(getCurrentElement().hasSuccessors()){
			//Bei mehreren ausgehenden Kanten...
			if(getCurrentElement().getSuccessors().size() > 1){
				//Beim XOR-Split eine Kante verfolgen
				//TODO: deferred choice, OR, complex?
				if(getCurrentElement() instanceof BPMNXORGateway){
					return getInstanceSimulator().getSimulator().choosePath(getCurrentElement());
				}
				//ansonsten alle Kanten verfolgen
				else{
					PathSimulator pathSimulator;
					for(AbstractBPMNElement successor : getCurrentElement().getSuccessors()){
						//ein Simulator für jede Kante erzeugen
						if(successor instanceof BPMNAndGateway){
							getInstanceSimulator().addJoinPredecessorToGateway(getCurrentElement(), (BPMNAndGateway) successor);
							if((getInstanceSimulator().allPredecessorsOfGatewayVisited((BPMNAndGateway) successor))){
								getInstanceSimulator().resetGateway((BPMNAndGateway) successor);
								pathSimulator = new PathSimulator(successor, getInstanceSimulator(), getCurrentSimulationDate());
								getInstanceSimulator().pathSimulators.add(pathSimulator);
							}
						}
						pathSimulator = new PathSimulator(successor, getInstanceSimulator(), getCurrentSimulationDate());
						getInstanceSimulator().pathSimulators.add(pathSimulator);
					}
				}
			}
			//Nachfolger durchlaufen (eine ausgehende Kante)
			else{
				AbstractBPMNElement successor = getCurrentElement().getSuccessors().iterator().next();
				if(successor instanceof BPMNAndGateway){
					getInstanceSimulator().addJoinPredecessorToGateway(getCurrentElement(), (BPMNAndGateway) successor);
					if((getInstanceSimulator().allPredecessorsOfGatewayVisited((BPMNAndGateway) successor))){
						getInstanceSimulator().resetGateway((BPMNAndGateway) successor);
					
					}
					else{
						return null;
					}
				}
				return successor;
			}
		}
		return null;
	}

	private InstanceSimulator getInstanceSimulator() {
		return instanceSimulator;
	}
	
	private void setInstanceSimulator(InstanceSimulator instanceSimulator) {
		this.instanceSimulator = instanceSimulator;
	}

	public Date getCurrentSimulationDate() {
		return currentSimulationDate;
	}

	private void setCurrentSimulationDate(Date currentSimulationDate) {
		this.currentSimulationDate = currentSimulationDate;
	}
	
	private void addEventFromMonitorinPointEnable() {
		addEventFromMonitorinPoint(MonitoringPointStateTransition.enable);
	}
	
	private void addEventFromMonitorinPointBegin() {
		addEventFromMonitorinPoint(MonitoringPointStateTransition.begin);
	}
	
	private void addEventFromMonitorinPointTerminate() {
		addEventFromMonitorinPoint(MonitoringPointStateTransition.terminate);
	}
	
	private void addEventFromMonitorinPointSkip() {
		addEventFromMonitorinPoint(MonitoringPointStateTransition.skip);
	}
	
	private void addEventFromMonitorinPoint(MonitoringPointStateTransition stateTransition) {
		MonitoringPoint monitoringPoint = getCurrentElement().getMonitoringPointByStateTransitionType(stateTransition);
		if(monitoringPoint != null){
			newEvents.add(new SushiEvent(monitoringPoint.getEventType(), getCurrentSimulationDate()));
		}
	}

	private Boolean getCurrentElementIsTraversed() {
		return currentElementIsTraversed;
	}

	private void setCurrentElementIsTraversed(Boolean currentElementIsTraversed) {
		this.currentElementIsTraversed = currentElementIsTraversed;
	}

	private void skipElements(Set<AbstractBPMNElement> unreachableElements) {
		Iterator<AbstractBPMNElement> iterator = unreachableElements.iterator();
		while(iterator.hasNext()){
			setCurrentElement(iterator.next());
			addEventFromMonitorinPointSkip();
		}
	}

	public AbstractBPMNElement getCurrentElement() {
		return currentElement;
	}

	private void setCurrentElement(AbstractBPMNElement currentElement) {
		this.currentElement = currentElement;
	}
	
}
