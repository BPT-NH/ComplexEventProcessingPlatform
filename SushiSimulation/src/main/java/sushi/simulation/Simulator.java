package sushi.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.Broker;
import sushi.process.SushiProcess;

public class Simulator {

	private int numberOfInstance;
	private AbstractBPMNElement startEvent;
	private List<InstanceSimulator> instanceSimulators;
	private Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes;
	private Map<SushiAttribute, List<Serializable>> correlationAttributesMap;
	private Date currentSimulationDate;
	private Map<AbstractBPMNElement, Long> elementDurations;
	private Map<AbstractBPMNElement, DerivationType> elementTimeDerivationTypes;
	private Random random = new Random();
	private Map<AbstractBPMNElement, Long> elementDerivations;
	
	@Deprecated
	public Simulator(BPMNProcess process) {
		this.startEvent = process.getStartEvent();
		this.instanceSimulators = new ArrayList<InstanceSimulator>();
		this.correlationAttributesMap = new HashMap<SushiAttribute, List<Serializable>>();
		eventTypeAttributes = new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>();
		this.currentSimulationDate = new Date();
	}
	
	
	public Simulator(SushiProcess process, Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes, Map<AbstractBPMNElement, String> tasksDurationString){
		this(process, process.getBpmnProcess(), eventTypeAttributes, tasksDurationString);
	}
	
//	public Simulator(SushiProcess process, BPMNProcess bpmnProcess, Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes, Map<SushiEventType, String> eventTypesDurationString){
//		this.startEvent = bpmnProcess.getStartEvent();
//		this.instanceSimulators = new ArrayList<InstanceSimulator>();
//		this.eventTypeAttributes = eventTypeAttributes;
//		this.correlationAttributesMap = getCorrelationAttributesMap(process);
//		this.numberOfInstance = 0;
//		this.currentSimulationDate = new Date();
//		System.out.println(eventTypesDurationString);
//		if(eventTypesDurationString != null){
//			Map<AbstractBPMNElement, String> tasksDurationString = getTasksFromEventTypes(eventTypesDurationString, bpmnProcess);
//			this.taskDuration = getDurationsFromMap(tasksDurationString);
//		}
//	}
	
	public Simulator(SushiProcess process, BPMNProcess bpmnProcess, Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes, Map<AbstractBPMNElement, String> tasksDurationString){
		this.startEvent = bpmnProcess.getStartEvent();
		this.instanceSimulators = new ArrayList<InstanceSimulator>();
		this.eventTypeAttributes = eventTypeAttributes;
		this.correlationAttributesMap = getCorrelationAttributesMap(process);
		this.numberOfInstance = 0;
		this.currentSimulationDate = new Date();
		this.elementDurations = SimulationUtils.getDurationsFromMap(tasksDurationString);
	}
	
	public Simulator(SushiProcess process, BPMNProcess bpmnProcess, Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes, Map<AbstractBPMNElement, String> tasksDurationString, Map<AbstractBPMNElement, String> tasksDerivationString, Map<AbstractBPMNElement, DerivationType> tasksDerivationTypes){
		this.startEvent = bpmnProcess.getStartEvent();
		this.instanceSimulators = new ArrayList<InstanceSimulator>();
		this.eventTypeAttributes = eventTypeAttributes;
		this.correlationAttributesMap = getCorrelationAttributesMap(process);
		this.numberOfInstance = 0;
		this.currentSimulationDate = new Date();
		this.elementDurations = SimulationUtils.getDurationsFromMap(tasksDurationString);
		this.elementDerivations = SimulationUtils.getDurationsFromMap(tasksDerivationString);
		this.elementTimeDerivationTypes = tasksDerivationTypes;
	}
	
	private Map<SushiAttribute, List<Serializable>> getCorrelationAttributesMap(SushiProcess process) {
		correlationAttributesMap = new HashMap<SushiAttribute, List<Serializable>>();
		Map<SushiAttribute, List<Serializable>> attributeValues = eventTypeAttributes.values().iterator().next();
		for(SushiAttribute correlationAttribute : process.getCorrelationAttributes()){
			correlationAttributesMap.put(correlationAttribute, attributeValues.get(correlationAttribute));
		}
		return correlationAttributesMap;
	}
	
	private void startSimulation(){
		while(!instanceSimulators.isEmpty()){
			Random random = new Random();
			int index = random.nextInt(instanceSimulators.size());
			instanceSimulators.get(index).simulateStep();
		}
	}
	@Deprecated
	public void simulate() {
		this.simulate(new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>());
	}
	
	public void simulate(Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> instanceEventTypeAttributes) {
		InstanceSimulator instanceSimulator = new InstanceSimulator(startEvent, this, instanceEventTypeAttributes, currentSimulationDate);
		instanceSimulators.add(instanceSimulator);
		startSimulation();
	}
	
	
	 /**
	  * Simulates the process numberOfInstances times sequential.
	  * @param numberOfInstances
	  */
	 public void simulate(int numberOfInstances) {
	 	simulate(numberOfInstances, 1);
	 }
	 
	 public void simulate(int numberOfInstances, int numberOfDays) {
		 	simulate(numberOfInstances, true, numberOfDays);
		 }
	 
	 /**
	  * Simulates the process numberOfInstances times sequential (true) or parallel(false).
	  * @param numberOfInstances
	  * @param isSequential
	  */
	public void simulate(int numberOfInstances, Boolean isSequential, int numberOfDays){
		if(isSequential){
			List<Integer> instancesPerDay = spreadInstancesOverDays(numberOfInstances, numberOfDays);
			for(int instances : instancesPerDay){
				simulateInstances(instances);
				long time = currentSimulationDate.getTime();
				time = time + (24 * 60 * 60 * 1000);
				currentSimulationDate = new Date(time);
			}
		}
		else{
			for(Integer i = 0; i < numberOfInstances; i++){
				Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> instanceEventTypeAttributes = createNewInstanceEventTypeAttributesMap();
				InstanceSimulator instanceSimulator = new InstanceSimulator(startEvent, this, instanceEventTypeAttributes, currentSimulationDate);
				instanceSimulators.add(instanceSimulator);
			}
			startSimulation();
		}
	}


	public void simulateInstances(int numberOfInstances) {
		for(Integer i = 0; i < numberOfInstances; i++){
			Map<SushiAttribute, String> correlationAttributeValues = new HashMap<SushiAttribute, String>();
			for(SushiAttribute correlationAttribute : correlationAttributesMap.keySet()){
				correlationAttributeValues.put(correlationAttribute, i.toString());
			}
			Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> instanceEventTypeAttributes = createNewInstanceEventTypeAttributesMap();
			simulate(instanceEventTypeAttributes);
		}
	}
	
	private Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> createNewInstanceEventTypeAttributesMap() {
		//für jede Instanz wird eine neue Map erstellt mit bestimmten werten
		// TODO Auto-generated method stub
		Map<SushiAttribute, List<Serializable>> instanceCorrelationAttributes = getNextCorrelationAttributes();
		Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> instanceEventTypeAttributes = new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>();
		for(SushiEventType eventType : eventTypeAttributes.keySet()){
			Map<SushiAttribute, List<Serializable>> attributes = eventTypeAttributes.get(eventType);
			Map<SushiAttribute, List<Serializable>> instanceAttributes = new HashMap<SushiAttribute, List<Serializable>>();
			for(SushiAttribute attribute : attributes.keySet()){
				List<Serializable> valueList = new ArrayList<Serializable>();
				//auswählen, was in der Instanz noch vorhanden sein soll
				Random random = new Random();
				int index = random.nextInt(attributes.get(attribute).size());
				Serializable chosenObject = attributes.get(attribute).get(index);
				valueList.add(chosenObject);
				instanceAttributes.put(attribute, valueList);
			}
			//Korrelationsattribute überschreiben ggf. andere ausgewählte
			for(SushiAttribute correlationAttribute : instanceCorrelationAttributes.keySet()){
				instanceAttributes.put(correlationAttribute, instanceCorrelationAttributes.get(correlationAttribute));
			}
			instanceEventTypeAttributes.put(eventType, instanceAttributes);
		}
		return instanceEventTypeAttributes;
	}

	private Map<SushiAttribute, List<Serializable>> getNextCorrelationAttributes() {
		HashMap<SushiAttribute, List<Serializable>> instanceCorrelationAttributes = new HashMap<SushiAttribute, List<Serializable>>();
		//die Zahl wird jedes mal inkrementiert, um neue Werte zu erzeugen
		int index = numberOfInstance;
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
		numberOfInstance++;
		return instanceCorrelationAttributes;
	}

	public void unsubscribe(InstanceSimulator simulator) {
		instanceSimulators.remove(simulator);
	}
	
	public List<Integer> spreadInstancesOverDays(int numberOfInstances, int numberOfDays){
		List<Integer> instancesPerDay = new ArrayList<Integer>();
		int basicNumber = numberOfInstances / numberOfDays;
		for(int i = 0; i < numberOfDays; i++){
			instancesPerDay.add(basicNumber);
		}
		int remaining = numberOfInstances % numberOfDays;
		int remainingDays = numberOfDays;
		for(int i = 0; i < remainingDays; i++){
			if(remaining / (remainingDays /(i+1)) >= 1){
				instancesPerDay.set(i, basicNumber + 1);
				remaining --;
			}
		}
		return instancesPerDay;
	}
	
	public long getMeanDurationForBPMNElement(AbstractBPMNElement element){
		long meanDuration;
		if(this.elementDurations != null && this.elementDurations.containsKey(element)){
			meanDuration = elementDurations.get(element);
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
		else if(elementTimeDerivationTypes.get(element).equals(DerivationType.UNIFORM)){
			return mean;
		}
		else if(elementTimeDerivationTypes.get(element).equals(DerivationType.EXPONENTIAL)){
			return mean;
		}
		else{
			return mean;
		}
		
	}
}
