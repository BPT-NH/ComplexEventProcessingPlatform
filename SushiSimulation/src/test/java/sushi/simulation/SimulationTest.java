package sushi.simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNEndEvent;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.correlation.AttributeCorrelator;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;

public class SimulationTest {
	
	private SushiAttribute location, movie;
	private SushiEventType eventType1, eventType2, eventType3, eventType4, eventType5, eventType6,
	eventType7, eventType8, eventType9;
	private BPMNProcess simpleBPMNProcess, complexBPMNProcess, loopProcess;
	private Map<SushiAttribute, List<Serializable>> attributes;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		
		createEventTypes();
		
	}

	private void createEventTypes() {
		SushiAttributeTree values = new SushiAttributeTree();
		
		attributes = new HashMap<SushiAttribute, List<Serializable>>();
		
		eventType1 = new SushiEventType("EventType1", values);
		Broker.send(eventType1);
		eventType2 = new SushiEventType("EventType2", values);
		Broker.send(eventType2);
		eventType3 = new SushiEventType("EventType3", values);
		Broker.send(eventType3);
		eventType4 = new SushiEventType("EventType4", values);
		Broker.send(eventType4);
		eventType5 = new SushiEventType("EventType5", values);
		Broker.send(eventType5);
	}

	private SushiProcess createSimpleCorrelatedProcess() {

		simpleBPMNProcess = new BPMNProcess("1", "SimpleProcess", null);
		BPMNStartEvent startEvent = new BPMNStartEvent("2", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("3", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task2 = new BPMNTask("4", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNEndEvent endEvent = new BPMNEndEvent("5", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, task2);
		AbstractBPMNElement.connectElements(task2, endEvent);
		
		List<SushiEventType> eventTypes = Arrays.asList(eventType1, eventType2);
		SushiProcess process = new SushiProcess("SimpleProcess", eventTypes);
		process.save();
		
		simpleBPMNProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, endEvent));
		simpleBPMNProcess.save();
		process.setBpmnProcess(simpleBPMNProcess);
		process.merge();
		return process;
	}
	
	private SushiProcess createComplexProcess() {
		complexBPMNProcess = new BPMNProcess("6", "ComplexProcess", null);
		BPMNStartEvent startEvent = new BPMNStartEvent("7", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("8", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and1 = new BPMNAndGateway("9", "XOR1", null);
		BPMNTask task2 = new BPMNTask("10", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task3 = new BPMNTask("11", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task4 = new BPMNTask("12", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task5 = new BPMNTask("13", "Task5", Arrays.asList(new MonitoringPoint(eventType5, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and2 = new BPMNAndGateway("14", "XOR2", null);
		BPMNEndEvent endEvent = new BPMNEndEvent("15", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, and1);
		AbstractBPMNElement.connectElements(and1, task2);
		AbstractBPMNElement.connectElements(task2, task4);
		AbstractBPMNElement.connectElements(and1, task3);
		AbstractBPMNElement.connectElements(task3, task5);
		AbstractBPMNElement.connectElements(task4, and2);
		AbstractBPMNElement.connectElements(task5, and2);
		AbstractBPMNElement.connectElements(and2, endEvent);
		
		complexBPMNProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, task5, and1, and2, endEvent));
		complexBPMNProcess.save();
		List<SushiEventType> eventTypes = Arrays.asList(eventType1, eventType2, eventType3, eventType4, eventType5);
		SushiProcess process = new SushiProcess("ComplexProcess", eventTypes);
		process.save();
		process.setBpmnProcess(simpleBPMNProcess);
		process.merge();
		return process;
	}
	
	@Test
	public void simpleProcessTest(){
		SushiProcess process = createSimpleCorrelatedProcess();
		
		Map<AbstractBPMNElement, String> elementDurations = new HashMap<AbstractBPMNElement, String>();
		Map<AbstractBPMNElement, DerivationType> elementDerivation = new HashMap<AbstractBPMNElement, DerivationType>();
		for(AbstractBPMNElement bpmnElement : simpleBPMNProcess.getBPMNElementsWithOutSequenceFlows()){
			elementDerivation.put(bpmnElement, DerivationType.FIXED);
			elementDurations.put(bpmnElement, "1");
		}
		Simulator simulator = new Simulator(process, simpleBPMNProcess, attributes, elementDurations, new HashMap<AbstractBPMNElement, String>(), elementDerivation, null);
		simulator.simulate(1);
		
		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 2);
		assertEquals(eventType1, events.get(0).getEventType());
		assertEquals(eventType2, events.get(1).getEventType());
	}
	
	@Test
	public void complexProcessTest(){
		SushiProcess process = createComplexProcess();
		
		Map<AbstractBPMNElement, String> elementDurations = new HashMap<AbstractBPMNElement, String>();
		Map<AbstractBPMNElement, DerivationType> elementDerivation = new HashMap<AbstractBPMNElement, DerivationType>();
		for(AbstractBPMNElement bpmnElement : complexBPMNProcess.getBPMNElementsWithOutSequenceFlows()){
			elementDerivation.put(bpmnElement, DerivationType.FIXED);
			elementDurations.put(bpmnElement, "1");
		}
		
		Simulator simulator = new Simulator(process, complexBPMNProcess, attributes, elementDurations, new HashMap<AbstractBPMNElement, String>(), elementDerivation, null);
		simulator.simulate(1);
		
		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 5);
		assertEquals(eventType1, events.get(0).getEventType());
		assertTrue(eventType2.equals(events.get(1).getEventType()) || eventType3.equals(events.get(1).getEventType()));
		assertTrue(eventType4.equals(events.get(2).getEventType()) || eventType5.equals(events.get(2).getEventType()) || eventType2.equals(events.get(1).getEventType()) || eventType3.equals(events.get(1).getEventType()));
	}
}
