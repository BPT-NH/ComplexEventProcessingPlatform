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
import sushi.correlation.Correlator;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;

public class SimulationTest {
	
	private SushiEventType eventType1, eventType2, eventType3, eventType4, eventType5, eventType6,
	eventType7, eventType8, eventType9;
	private BPMNProcess simpleBPMNProcess, complexProcess, superComplexProcess, megaComplexProcess,
	loopProcess, complexLoopProcess;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		
		createEventTypes();
		
	}

	private void createEventTypes() {
		SushiAttributeTree values = new SushiAttributeTree();
		values.addRoot(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER));
		values.addRoot(new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING));
		
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
		eventType6 = new SushiEventType("EventType6", values);
		Broker.send(eventType6);
		eventType7 = new SushiEventType("EventType7", values);
		Broker.send(eventType7);
		eventType8 = new SushiEventType("EventType8", values);
		Broker.send(eventType8);
		eventType9 = new SushiEventType("EventType9", values);
		Broker.send(eventType9);
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
		SushiAttribute locationAttribute = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		Correlator.correlate(eventTypes, Arrays.asList(locationAttribute), process, null);
		
		simpleBPMNProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, endEvent));
		simpleBPMNProcess.save();
		process.setBpmnProcess(simpleBPMNProcess);
		process.merge();
		return process;
	}
	
	private void createComplexProcess() {
		complexProcess = new BPMNProcess("6", "ComplexProcess", null);
		BPMNStartEvent startEvent = new BPMNStartEvent("7", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("8", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor1 = new BPMNXORGateway("9", "XOR1", null);
		BPMNTask task2 = new BPMNTask("10", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task3 = new BPMNTask("11", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task4 = new BPMNTask("12", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task5 = new BPMNTask("13", "Task5", Arrays.asList(new MonitoringPoint(eventType5, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor2 = new BPMNXORGateway("14", "XOR2", null);
		BPMNEndEvent endEvent = new BPMNEndEvent("15", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, xor1);
		AbstractBPMNElement.connectElements(xor1, task2);
		AbstractBPMNElement.connectElements(task2, task4);
		AbstractBPMNElement.connectElements(xor1, task3);
		AbstractBPMNElement.connectElements(task3, task5);
		AbstractBPMNElement.connectElements(task4, xor2);
		AbstractBPMNElement.connectElements(task5, xor2);
		AbstractBPMNElement.connectElements(xor2, endEvent);
		
		complexProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, task5, xor1, xor2, endEvent));
	}
	
	private void createSuperComplexProcess() {

		superComplexProcess = new BPMNProcess("16", "SuperComplexProcess", null);
		
		BPMNStartEvent startEvent = new BPMNStartEvent("17", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("18", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and1 = new BPMNAndGateway("19", "AND1", null);
		BPMNTask task2 = new BPMNTask("20", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task3 = new BPMNTask("21", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task4 = new BPMNTask("22", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task5 = new BPMNTask("23", "Task5", Arrays.asList(new MonitoringPoint(eventType5, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and2 = new BPMNAndGateway("24", "AND2", null);
		BPMNTask task6 = new BPMNTask("25", "Task5", Arrays.asList(new MonitoringPoint(eventType6, MonitoringPointStateTransition.terminate, "")));
		BPMNEndEvent endEvent = new BPMNEndEvent("26", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, and1);
		AbstractBPMNElement.connectElements(and1, task2);
		AbstractBPMNElement.connectElements(task2, task4);
		AbstractBPMNElement.connectElements(and1, task3);
		AbstractBPMNElement.connectElements(task3, task5);
		AbstractBPMNElement.connectElements(task4, and2);
		AbstractBPMNElement.connectElements(task5, and2);
		AbstractBPMNElement.connectElements(and2, task6);
		AbstractBPMNElement.connectElements(task6, endEvent);
		
		superComplexProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, task5, task6, and1, and2, endEvent));
	}
	
	private void createMegaComplexProcess() {
		megaComplexProcess = new BPMNProcess("27", "MegaComplexProcess", null);
		
		BPMNStartEvent startEvent = new BPMNStartEvent("28", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("29", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and1 = new BPMNAndGateway("30", "AND1", null);
		
		BPMNTask task2 = new BPMNTask("31", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task3 = new BPMNTask("32", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor1 = new BPMNXORGateway("33", "XOR1", null);
		BPMNTask task4 = new BPMNTask("34", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task5 = new BPMNTask("35", "Task5", Arrays.asList(new MonitoringPoint(eventType5, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor2 = new BPMNXORGateway("36", "XOR2", null);
		
		BPMNAndGateway and3 = new BPMNAndGateway("37", "AND3", null);
		BPMNTask task6 = new BPMNTask("38", "Task6", Arrays.asList(new MonitoringPoint(eventType6, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task7 = new BPMNTask("39", "Task7", Arrays.asList(new MonitoringPoint(eventType7, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and4 = new BPMNAndGateway("40", "AND4", null);
		BPMNTask task8 = new BPMNTask("41", "Task7", Arrays.asList(new MonitoringPoint(eventType8, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and2 = new BPMNAndGateway("42", "AND2", null);
		BPMNTask task9 = new BPMNTask("43", "Task5", Arrays.asList(new MonitoringPoint(eventType9, MonitoringPointStateTransition.terminate, "")));
		BPMNEndEvent endEvent = new BPMNEndEvent("44", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, and1);
		AbstractBPMNElement.connectElements(and1, task2);
		AbstractBPMNElement.connectElements(and1, task3);
		
		AbstractBPMNElement.connectElements(task2, xor1);
		AbstractBPMNElement.connectElements(xor1, task4);
		AbstractBPMNElement.connectElements(xor1, task5);
		AbstractBPMNElement.connectElements(task4, xor2);
		AbstractBPMNElement.connectElements(task5, xor2);
		
		AbstractBPMNElement.connectElements(task3, and3);
		AbstractBPMNElement.connectElements(and3, task6);
		AbstractBPMNElement.connectElements(and3, task7);
		AbstractBPMNElement.connectElements(task6, and4);
		AbstractBPMNElement.connectElements(task7, and4);
		AbstractBPMNElement.connectElements(and4, task8);
		
		AbstractBPMNElement.connectElements(xor2, and2);
		AbstractBPMNElement.connectElements(task8, and2);
		
		AbstractBPMNElement.connectElements(and2, task9);
		AbstractBPMNElement.connectElements(task9, endEvent);
		
		megaComplexProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, task5, task6, task7, task8, task9, and1, and2, and3, and4, xor1, xor2, endEvent));
	}
	
	private void createLoopProcess() {
		loopProcess = new BPMNProcess("45", "LoopProcess", null);
		
		BPMNStartEvent startEvent = new BPMNStartEvent("46", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("47", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor1 = new BPMNXORGateway("48", "XOR1", null);
		BPMNTask task2 = new BPMNTask("49", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor2 = new BPMNXORGateway("50", "XOR2", null);
		BPMNTask task3 = new BPMNTask("51", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task4 = new BPMNTask("52", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNEndEvent endEvent = new BPMNEndEvent("53", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, xor1);
		
		AbstractBPMNElement.connectElements(xor1, task2);
		AbstractBPMNElement.connectElements(task2, xor2);
		AbstractBPMNElement.connectElements(xor2, task3);
		AbstractBPMNElement.connectElements(task3, xor1);
		
		AbstractBPMNElement.connectElements(xor2, task4);
		AbstractBPMNElement.connectElements(task4, endEvent);
		
		loopProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, xor1, xor2, endEvent));

	}
	
	private void createComplexLoopProcess() {
		complexLoopProcess = new BPMNProcess("54", "complexLoopProcess", null);
		
		BPMNStartEvent startEvent = new BPMNStartEvent("55", "StartEvent", null);
		BPMNTask task1 = new BPMNTask("56", "Task1", Arrays.asList(new MonitoringPoint(eventType1, MonitoringPointStateTransition.terminate, "")));
		BPMNXORGateway xor1 = new BPMNXORGateway("57", "XOR1", null);
		BPMNTask task2 = new BPMNTask("58", "Task2", Arrays.asList(new MonitoringPoint(eventType2, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and1 = new BPMNAndGateway("59", "AND1", null);
		BPMNTask task3 = new BPMNTask("60", "Task3", Arrays.asList(new MonitoringPoint(eventType3, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task4 = new BPMNTask("61", "Task4", Arrays.asList(new MonitoringPoint(eventType4, MonitoringPointStateTransition.terminate, "")));
		BPMNAndGateway and2 = new BPMNAndGateway("62", "AND2", null);
		BPMNXORGateway xor2 = new BPMNXORGateway("63", "XOR2", null);
		BPMNTask task5 = new BPMNTask("64", "Task3", Arrays.asList(new MonitoringPoint(eventType5, MonitoringPointStateTransition.terminate, "")));
		BPMNTask task6 = new BPMNTask("65", "Task4", Arrays.asList(new MonitoringPoint(eventType6, MonitoringPointStateTransition.terminate, "")));
		BPMNEndEvent endEvent = new BPMNEndEvent("66", "EndEvent", null);
		
		AbstractBPMNElement.connectElements(startEvent, task1);
		AbstractBPMNElement.connectElements(task1, xor1);
		//Schleife
		AbstractBPMNElement.connectElements(xor1, task2);
		AbstractBPMNElement.connectElements(task2, and1);
		//Parallele ausführung
		AbstractBPMNElement.connectElements(and1, task3);
		AbstractBPMNElement.connectElements(and1, task4);
		AbstractBPMNElement.connectElements(task3, and2);
		AbstractBPMNElement.connectElements(task4, and2);
		
		AbstractBPMNElement.connectElements(and2, xor2);
		AbstractBPMNElement.connectElements(xor2, task5);
		AbstractBPMNElement.connectElements(task5, xor1);
		//Schleifen-Ende
		AbstractBPMNElement.connectElements(xor2, task6);
		AbstractBPMNElement.connectElements(task6, endEvent);
		
		complexLoopProcess.addBPMNElements(Arrays.asList(startEvent, task1, task2, task3, task4, task5, task6, xor1, xor2, and1, and2, endEvent));
	}
	
	/**
	 * This test includes correlation of events.
	 */
	@Test
	public void simpleProcessTest(){
		SushiProcess process = createSimpleCorrelatedProcess();
		
		Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes = new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>();
		for(SushiEventType eventType : process.getEventTypes()){
			Map<SushiAttribute, List<Serializable>> attributes = new HashMap<SushiAttribute, List<Serializable>>();
			for(SushiAttribute attribute : eventType.getValueTypes()){
				List<Serializable> list = new ArrayList<Serializable>();
				if(attribute.getType().equals(SushiAttributeTypeEnum.INTEGER)){
					list.add(1);
				} else if(attribute.getType().equals(SushiAttributeTypeEnum.STRING)){
					list.add("Movie1");
				}
				attributes.put(attribute, list);
			}
			eventTypeAttributes.put(eventType, attributes);
		}
		
		Simulator simulator = new Simulator(process, eventTypeAttributes, null);
		simulator.simulate(1);
		
		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 2);
		assertEquals(eventType1, events.get(0).getEventType());
		assertEquals(eventType2, events.get(1).getEventType());
	}
	
	@Test
	public void complexProcessTest(){
		createComplexProcess();
		
		Simulator simulator = new Simulator(complexProcess);
		simulator.simulate();
		
		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 3);
		assertEquals(eventType1, events.get(0).getEventType());
		assertTrue(eventType2.equals(events.get(1).getEventType()) || eventType3.equals(events.get(1).getEventType()));
		assertTrue(eventType4.equals(events.get(2).getEventType()) || eventType5.equals(events.get(2).getEventType()));
	}
	
	@Test
	public void superComplexProcessTest(){
		createSuperComplexProcess();
		
		Simulator simulator = new Simulator(superComplexProcess);
		simulator.simulate();
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue("Number of Events should be 6 but was" + events.size(), events.size() == 6);
		assertEquals(eventType1, eventTypes.get(0));
		assertTrue(eventType2.equals(eventTypes.get(1)) || eventType3.equals(eventTypes.get(1)));
		assertTrue(eventType2.equals(eventTypes.get(2)) || eventType3.equals(eventTypes.get(2))
				|| eventType4.equals(eventTypes.get(2)) || eventType5.equals(eventTypes.get(2)));
		assertTrue(eventType2.equals(eventTypes.get(3)) || eventType3.equals(eventTypes.get(3))
				|| eventType4.equals(eventTypes.get(3)) || eventType5.equals(eventTypes.get(3)));
		assertTrue(eventType4.equals(eventTypes.get(4)) || eventType5.equals(eventTypes.get(4)));
		assertEquals(eventType6, eventTypes.get(5));
		//kein Eventtyp doppelt?
		Set<SushiEventType> eventTypesSet = new HashSet<SushiEventType>(eventTypes);
		assertEquals(eventTypes.size(), eventTypesSet.size());
	}
	
	@Test
	public void megaComplexProcessTest(){
		createMegaComplexProcess();
		
		Simulator simulator = new Simulator(megaComplexProcess);
		simulator.simulate();
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue("Number of Events should be 8 but was" + events.size(), events.size() == 8);
		assertEquals(eventType1, eventTypes.get(0));
		assertTrue(eventType2.equals(eventTypes.get(1)) || eventType3.equals(eventTypes.get(1)));
		assertTrue(eventType4.equals(eventTypes.get(6)) || eventType5.equals(eventTypes.get(6)) || eventType8.equals(eventTypes.get(6)));
		assertEquals(eventType9, eventTypes.get(7));
	}
	
	@Test
	public void megaComplexProcessTest2(){
		createMegaComplexProcess();
		
		Simulator simulator = new Simulator(megaComplexProcess);
		simulator.simulate(20);
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue("Number of Events should be 160 but was" + events.size(), events.size() == 160);
		for(int i = 0; i < 160; i = i + 8){
			assertEquals(eventType1, eventTypes.get(i));
		}
		
	}
	
	@Test
	public void megaComplexProcessTest3(){
		createMegaComplexProcess();
		
		Simulator simulator = new Simulator(megaComplexProcess);
		simulator.simulate(20, false, 1);
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue("Number of Events should be 160 but was" + events.size(), events.size() == 160);
		assertEquals(eventType1, eventTypes.get(0));
		int i = 0;
		while(eventTypes.remove(eventType1)){
			i++;
		}
		assertEquals(20, i);
		
	}
	
	@Test
	public void loopProcessTest(){
		createLoopProcess();
		
		Simulator simulator = new Simulator(loopProcess);
		simulator.simulate();
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue(events.size() > 2);
		assertEquals(eventType1 ,eventTypes.get(0));
		assertEquals(eventType2 ,eventTypes.get(1));
		assertTrue(eventTypes.get(2).equals(eventType3) || eventTypes.get(2).equals(eventType4));
		assertTrue(eventTypes.get(2).equals(eventType3) || events.size() == 3);
		
	}
	
	@Test
	public void complexLoopProcessTest(){
		createComplexLoopProcess();
		
		Simulator simulator = new Simulator(complexLoopProcess);
		simulator.simulate();
		List<SushiEvent> events = SushiEvent.findAll();
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		for(SushiEvent event : events){
			eventTypes.add(event.getEventType());
		}
		assertTrue(events.size() > 4);
		assertEquals(eventType1 ,eventTypes.get(0));
		assertEquals(eventType2 ,eventTypes.get(1));
		assertTrue(eventTypes.get(2).equals(eventType3) || eventTypes.get(2).equals(eventType4));
		assertTrue(eventTypes.get(3).equals(eventType3) || eventTypes.get(3).equals(eventType4));
		assertTrue(!eventTypes.get(2).equals(eventTypes.get(3)));
		assertTrue(eventTypes.get(4).equals(eventType5) || events.size() == 5);
	}
}
