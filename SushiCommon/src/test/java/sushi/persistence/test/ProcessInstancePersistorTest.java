package sushi.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

public class ProcessInstancePersistorTest implements PersistenceTest {
	
	private SushiEvent michaEvent;
	private SushiEvent tsunEvent;
	private SushiEventType michaEventType;
	private SushiEventType tsunEventType;
	private SushiProcess firstProcess;
	private SushiProcessInstance firstProcessInstance;
	private int processID;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();

		SushiMapTree<String, Serializable> hm = new SushiMapTree<String, Serializable>();
		hm.put("kuchen", "kaese");
		hm.put("kuchen2", "kirsch");
		hm.put("kuchen3", "apfel");
		tsunEventType = new SushiEventType("Tsun");
		tsunEventType.save();
		tsunEvent = new SushiEvent(tsunEventType, new Date(), hm);
		tsunEvent.save();
		
		SushiMapTree<String, Serializable> hm2 = new SushiMapTree<String, Serializable>();
		hm2.put("getraenk1", "cola");
		hm2.put("getraenk2", "apfelsaft");
		hm2.put("getraenk3", "fanta");
		Date oldDate = null;
		try {
			oldDate = new SimpleDateFormat("dd/MM/yyyy").parse("18/05/2011");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		michaEventType = new SushiEventType("Micha");
		michaEventType.save();
		michaEvent = new SushiEvent(michaEventType, oldDate, hm2);
		michaEvent.save();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve(){
		storeExampleProcessInstances();
		assertTrue("Value should be 2, but was " + SushiProcessInstance.findAll().size(),SushiProcessInstance.findAll().size()==2);
		SushiProcessInstance.removeAll();
		assertTrue("Value should be 0, but was " + SushiProcessInstance.findAll().size(),SushiProcessInstance.findAll().size()==0);
	}
	
	private void storeExampleProcessInstances() {
		SushiAttribute correlationAttribute = new SushiAttribute("location", SushiAttributeTypeEnum.INTEGER);
		
		SushiMapTree<String, Serializable> correlation1 = new SushiMapTree<String, Serializable>();
		correlation1.put(correlationAttribute.getAttributeExpression(), "1");
		
		SushiMapTree<String, Serializable> correlation2 = new SushiMapTree<String, Serializable>();
		correlation2.put(correlationAttribute.getAttributeExpression(), "2");
		
		firstProcessInstance = new SushiProcessInstance(correlation1);
		firstProcessInstance.addEvent(tsunEvent);
		firstProcessInstance.save();
		tsunEvent.addProcessInstance(firstProcessInstance);
		tsunEvent.save();

		SushiProcessInstance secondProcessInstance = new SushiProcessInstance(correlation2);
		secondProcessInstance.addEvent(michaEvent);
		secondProcessInstance.save();
		michaEvent.addProcessInstance(secondProcessInstance);
		michaEvent.save();
		
		ArrayList<SushiProcessInstance> processInstances = new ArrayList<SushiProcessInstance>(Arrays.asList(firstProcessInstance, secondProcessInstance));
		SushiProcessInstance.save(processInstances);
		assertTrue(SushiProcessInstance.findAll().size() == 2);
		
		SushiProcess process = new SushiProcess();
		process.addProcessInstance(firstProcessInstance);
		process.addProcessInstance(secondProcessInstance);
		process.save();
		processID = process.getID();
	}
	
	private void storeExampleProcess(){
		firstProcess = new SushiProcess("Kino");
		firstProcess.addEventType(new SushiEventType("KinoEvent"));
		firstProcess.addProcessInstance(firstProcessInstance);
		firstProcess.save();
	}
	
	@Override
	@Test
	public void testFind(){
		storeExampleProcessInstances();
		storeExampleProcess();
		assertTrue(SushiProcessInstance.findAll().size() == 2);
		assertTrue("Value should be 2, but was " + SushiProcessInstance.findByCorrelationAttribute("location").size(), 
				SushiProcessInstance.findByCorrelationAttribute("location").size() == 2);
		List<SushiProcessInstance> processInstances = SushiProcessInstance.findByCorrelationAttribute("location");
		testFirstProcessInstance(processInstances.get(0));
		testSecondProcessInstance(processInstances.get(1));
		processInstances = SushiProcessInstance.findByCorrelationAttributeAndValue("location", "1");
		testFirstProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByCorrelationAttributeAndValue("location", "2");
		testSecondProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByContainedEvent(tsunEvent);
		testFirstProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByContainedEvent(michaEvent);
		testSecondProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByContainedEventType(tsunEventType);
		testFirstProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByContainedEventType(michaEventType);
		testSecondProcessInstance(processInstances.get(0));
		processInstances = SushiProcessInstance.findByProcess(firstProcess);
		testFirstProcessInstance(processInstances.get(0));
	}
	
	private void testFirstProcessInstance(SushiProcessInstance processInstance) {
		assertTrue(processInstance.getEvents().size() == 1);
		assertTrue(processInstance.getEvents().get(0).getEventType().getTypeName().equals("Tsun"));
	}
	
	private void testSecondProcessInstance(SushiProcessInstance processInstance) {
		assertTrue(processInstance.getEvents().size() == 1);
		assertTrue(processInstance.getEvents().get(0).getEventType().getTypeName().equals("Micha"));
	}

	@Override
	@Test
	public void testRemove(){
		storeExampleProcessInstances();
		List<SushiProcessInstance> processInstances;
		processInstances = SushiProcessInstance.findAll();
		assertTrue("Value should be 2, but was " + SushiProcessInstance.findAll().size(),SushiProcessInstance.findAll().size()==2);
		
		SushiProcessInstance deleteProcessInstance = processInstances.get(0);
		
		//Add a timerEvent to process instance
		SushiEvent timerEvent = new SushiEvent(new Date(), new SushiMapTree<String, Serializable>());
		timerEvent.save();
		deleteProcessInstance.setTimerEvent(timerEvent);
		deleteProcessInstance.save();
		assertTrue("Value should be 2, but was " + SushiProcessInstance.findAll().size(),SushiProcessInstance.findAll().size()==2);
		
		List<SushiEvent> eventsOfDeletedProcessInstance = deleteProcessInstance.getEvents();
		assertTrue(eventsOfDeletedProcessInstance.size() == 1);
		deleteProcessInstance.remove();
		SushiEvent eventWithOutProcessInstance = eventsOfDeletedProcessInstance.get(0);
		assertTrue(eventWithOutProcessInstance.getProcessInstances().size() == 0);
		SushiProcess process = SushiProcess.findByID(processID);
		assertNotNull("There should be a process", process);
		assertTrue("Number of process instances was " + process.getProcessInstances().size() + " but should be 1.",
				process.getProcessInstances().size() == 1);

		processInstances = SushiProcessInstance.findAll();
		assertTrue(processInstances.size() == 1);
		
		assertTrue(processInstances.get(0).getID() != deleteProcessInstance.getID());
		
		SushiProcessInstance.removeAll();
		processInstances = SushiProcessInstance.findAll();
		assertTrue(processInstances.size() == 0);
		process = SushiProcess.findByID(processID);
		assertNotNull(process);
		assertTrue(process.getProcessInstances().size() == 0);
	}

}
