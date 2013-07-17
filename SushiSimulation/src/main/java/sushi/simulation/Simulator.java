package sushi.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.Broker;
import sushi.process.SushiProcess;
import sushi.util.Tuple;
/**
 * The central class for simulation 
 */
public class Simulator {

	private int numberOfInstances;
	private AbstractBPMNElement startEvent;
	private List<InstanceSimulator> instanceSimulators;
	private Map<SushiAttribute, List<Serializable>> attributesAndValues;
	private Map<SushiAttribute, List<Serializable>> correlationAttributesMap;
	private Date currentSimulationDate;
	private Map<AbstractBPMNElement, Long> elementExecutionDurations;
	private Map<AbstractBPMNElement, DerivationType> elementTimeDerivationTypes;
	private Random random = new Random();
	private Map<AbstractBPMNElement, Long> elementDerivations;
	private Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>> xorSplitsWithSuccessorProbabilities;
	private List<SushiAttribute> identicalAttributes;
	private List<SushiAttribute> differingAttributes;
	private Map<AbstractBPMNElement, Tuple<Integer, Integer>> unexpectedEvents;
	
	
	public Simulator(SushiProcess process, BPMNProcess bpmnProcess, Map<SushiAttribute, List<Serializable>> attributesAndValues, Map<AbstractBPMNElement, String> tasksDurationString, Map<AbstractBPMNElement, String> tasksDerivationString, Map<AbstractBPMNElement, DerivationType> tasksDerivationTypes, Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>> xorSplitsWithSuccessorProbabilities){
		this.startEvent = bpmnProcess.getStartEvent();
		this.instanceSimulators = new ArrayList<InstanceSimulator>();
		this.attributesAndValues = attributesAndValues;
		this.correlationAttributesMap = getCorrelationAttributesMap(process);
		this.numberOfInstances = 0;
		this.currentSimulationDate = new Date();
		this.elementExecutionDurations = SimulationUtils.getDurationsFromMap(tasksDurationString);
		this.elementDerivations = SimulationUtils.getDurationsFromMap(tasksDerivationString);
		this.elementTimeDerivationTypes = tasksDerivationTypes;
		this.xorSplitsWithSuccessorProbabilities = xorSplitsWithSuccessorProbabilities;
		this.identicalAttributes = new ArrayList<SushiAttribute>();
		this.differingAttributes = new ArrayList<SushiAttribute>();
	}
	
	private Map<SushiAttribute, List<Serializable>> getCorrelationAttributesMap(SushiProcess process) {
		correlationAttributesMap = new HashMap<SushiAttribute, List<Serializable>>();
		for(SushiAttribute correlationAttribute : process.getCorrelationAttributes()){
			for(SushiAttribute attribute : attributesAndValues.keySet()){
				if(correlationAttribute.equals(attribute)){
					correlationAttributesMap.put(correlationAttribute, attributesAndValues.get(attribute));
					break;
				}
			}
			
		}
		return correlationAttributesMap;
	}
	
	 /**
	  * Simulates the process numberOfInstances over 1 Day
	  * @param numberOfInstances
	  */
	 public void simulate(int numberOfInstances) {
	 	simulate(numberOfInstances, 1);
	 }
	 
	 /**
	  * Simulates the process numberOfInstances over a numberOfDays
	  * @param numberOfInstances
	  * @param numberOfDays
	  */
	public void simulate(int numberOfInstances, int numberOfDays){
			int timeDifferenceInMs = timeDifferenceInMs(numberOfInstances, numberOfDays);
			for(int i = 0; i < numberOfInstances; i++){
				Map<SushiAttribute, List<Serializable>> instanceAttributes = createNewInstanceAttributesMap();
				InstanceSimulator instanceSimulator = new InstanceSimulator(startEvent, this, instanceAttributes, currentSimulationDate, differingAttributes);
				currentSimulationDate = new Date(currentSimulationDate.getTime() + timeDifferenceInMs);
				instanceSimulators.add(instanceSimulator);
			}
			startSimulation();
	}
	
	 /**
	  * starts the simulation bei repating to 
	  */
	private void startSimulation(){
		while(!instanceSimulators.isEmpty()){
			getEarliestInstanceSimulator().simulateStep();
		}
	}
	
	private Map<SushiAttribute, List<Serializable>> createNewInstanceAttributesMap() {
		//für jede Instanz wird eine neue Map erstellt mit bestimmten werten
		List<Serializable> valueList;
		Map<SushiAttribute, List<Serializable>> instanceCorrelationAttributes = getNextCorrelationAttributes();
		Map<SushiAttribute, List<Serializable>> instanceAttributes = new HashMap<SushiAttribute, List<Serializable>>();
		for(SushiAttribute attribute : attributesAndValues.keySet()){
			instanceAttributes.put(attribute, new ArrayList<Serializable>(attributesAndValues.get(attribute)));
		}
		addIdenticalAttributes(instanceAttributes);
		addCorrelationAttributes(instanceCorrelationAttributes,	instanceAttributes);
		return instanceAttributes;
	}

	private void addIdenticalAttributes(Map<SushiAttribute, List<Serializable>> instanceAttributes) {
		Random random = new Random();
		int index;
		List<Serializable> valueList;
		for(SushiAttribute identicalAttribute : identicalAttributes){
			for(SushiAttribute existingAttribute : instanceAttributes.keySet()){
				if(identicalAttribute.equals(existingAttribute)){
					valueList = new ArrayList<Serializable>();
					index = random.nextInt(instanceAttributes.get(existingAttribute).size());
					Serializable chosenObject = attributesAndValues.get(existingAttribute).get(index);
					valueList.add(chosenObject);
					instanceAttributes.put(existingAttribute, valueList);
					break;
				}
				
			}
		}
	}

	 /**
	  * overwrites the correlation attributes to make same identical per instance and unique between instances
	  */
	private void addCorrelationAttributes(Map<SushiAttribute, List<Serializable>> instanceCorrelationAttributes, Map<SushiAttribute, List<Serializable>> instanceAttributes) {
		//Korrelationsattribute überschreiben andere ausgewählte
		for(SushiAttribute correlationAttribute : instanceCorrelationAttributes.keySet()){
			for(SushiAttribute existingAttribute : instanceAttributes.keySet()){
				if(correlationAttribute.equals(existingAttribute)){
					instanceAttributes.put(existingAttribute, instanceCorrelationAttributes.get(correlationAttribute));
					break;
				}
			}
		}
	}
	
	 /**
	  * creates new correlation attributes for each instance by counting and stepping throught the possible values
	  */
	private Map<SushiAttribute, List<Serializable>> getNextCorrelationAttributes() {
		HashMap<SushiAttribute, List<Serializable>> instanceCorrelationAttributes = new HashMap<SushiAttribute, List<Serializable>>();
		//die Zahl wird jedes mal inkrementiert, um neue Werte zu erzeugen
		int index = numberOfInstances;
		int nextIndex;
		for(SushiAttribute correlationAttributeKey : correlationAttributesMap.keySet()){
			//TODO: werte aus event... holen
			List<Serializable> correlationAttribute = correlationAttributesMap.get(correlationAttributeKey);
			nextIndex = 0;
			int mapsize = correlationAttribute.size();
			while(index >= mapsize){
				nextIndex ++;
				index = index - mapsize;
			}
			Serializable value = correlationAttribute.get(index);
			List<Serializable> valueList = new ArrayList<Serializable>();
			valueList.add(value);
			instanceCorrelationAttributes.put(correlationAttributeKey, valueList);
			index = nextIndex;
		}
		numberOfInstances++;
		return instanceCorrelationAttributes;
	}

	public void unsubscribe(InstanceSimulator simulator) {
		instanceSimulators.remove(simulator);
	}
	
	public int timeDifferenceInMs(int numberOfInstances, int numberOfDays){
		
		int totalSimulationPeriodInMs = numberOfDays * 24 * 60 * 60 * 1000;
		return totalSimulationPeriodInMs / numberOfInstances;
	}
	
	public long getMeanDurationForBPMNElement(AbstractBPMNElement element){
		long meanDuration;
		if(this.elementExecutionDurations != null && this.elementExecutionDurations.containsKey(element)){
			meanDuration = elementExecutionDurations.get(element);
		}
		else{
			meanDuration = 0;
		}
		return meanDuration;
	}
	
	public long getDerivationForBPMNElement(AbstractBPMNElement element){
		long derivation;
		if(this.elementDerivations != null && this.elementDerivations.containsKey(element)){
			derivation = elementDerivations.get(element);
		}
		else{
			derivation = 0;
		}
		return derivation;
	}
	
	 /**
	  * returns the duration for executing a bpmn-element, calculation depends on the derivationType
	  */
	public long getDurationForBPMNElement(AbstractBPMNElement element){
		long mean = getMeanDurationForBPMNElement(element);
		long derivation = getDerivationForBPMNElement(element);
		if(elementTimeDerivationTypes == null ||(elementTimeDerivationTypes.get(element) == null)){
			return mean;
		}
		if(elementTimeDerivationTypes.get(element).equals(DerivationType.NORMAL)){
			double dur = mean + (random.nextGaussian() * derivation);
			return (long) dur;
		}
		else{
			return mean;
		}
		
	}

	 /**
	  * randomly chooses a path with the given probabilities
	  */
	public AbstractBPMNElement choosePath(AbstractBPMNElement currentElement) {
		List<Tuple<AbstractBPMNElement, Integer>> successorsWithProbability = xorSplitsWithSuccessorProbabilities.get(currentElement);
		Random random = new Random();
		int index = random.nextInt(100);
		for(Tuple<AbstractBPMNElement, Integer> tuple : successorsWithProbability){
			if(index < tuple.y){
				return tuple.x;
			}
		}
		return null;
	}
	
	private InstanceSimulator getEarliestInstanceSimulator() {
		InstanceSimulator earliestInstanceSimulator = instanceSimulators.get(0);
		for(InstanceSimulator instanceSimulator : instanceSimulators){
			if(instanceSimulator.getEarliestDate().before(earliestInstanceSimulator.getEarliestDate())){
				earliestInstanceSimulator = instanceSimulator;
			}
		}
		return earliestInstanceSimulator;
	}
	
	public void addAdvancedValueRules(List<ValueRule> valueRules){
		for(ValueRule valueRule : valueRules){
			if(valueRule.getRuleType().equals(ValueRuleType.EQUAL)){
				identicalAttributes.add(valueRule.getAttribute());
			}
			else{
				differingAttributes.add(valueRule.getAttribute());
			}
		}
	}
}
