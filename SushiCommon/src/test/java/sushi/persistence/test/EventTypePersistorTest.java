package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.util.SushiTestHelper;

@FixMethodOrder(MethodSorters.JVM)
public class EventTypePersistorTest implements PersistenceTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve() {
		storeExampleEventType();
		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);
		SushiEventType.removeAll();
		assertTrue("Value should be 0, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==0);
	}
	
	private void storeExampleEventType() {
		List<SushiEventType> eventTypes = SushiTestHelper.createEventTypes();
		SushiEventType firstEventType = eventTypes.get(0);
		SushiEventType secondEventType = eventTypes.get(1);

		ArrayList<SushiEventType> eventTypes1 = new ArrayList<SushiEventType>();
		eventTypes1.add(firstEventType);
		
		ArrayList<SushiEventType> eventTypes2 = new ArrayList<SushiEventType>();
		eventTypes1.add(secondEventType);
		
		assertTrue(SushiEventType.save(eventTypes));
		
		SushiProcess process1 = new SushiProcess("Process1", eventTypes1);
		process1.save();
		
		SushiProcess process2 = new SushiProcess("Process2", eventTypes2);
		process2.save();
		
	}

	private void storeExampleEvents() {
		SushiEventType eventType = SushiEventType.findByTypeName("Kino");

		SushiMapTree<String, Serializable> hm = new SushiMapTree<String, Serializable>();
		hm.put("Location", 1);
		hm.put("Movie", "Event");
		
		SushiEvent event = new SushiEvent(eventType, new Date(), hm);		
		event.save();
	}

	@Override
	@Test
	public void testFind() {
		storeExampleEventType();
		assertTrue(SushiEventType.findAll().size() == 2);
		SushiEventType eventType = SushiEventType.findByAttribute("TypeName", "Kino").get(0);
		assertTrue(eventType.getTypeName().equals("Kino"));
//		assertTrue(eventType.getValueTypes().get("Location") == false);
//		assertTrue(eventType.getValueTypes().get("SecondaryEvent") == true);
	}
	
	@Override
	@Test
	public void testRemove() {
		storeExampleEventType();
		List<SushiEventType> eventTypes;
		eventTypes = SushiEventType.findAll();
		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);

		SushiEventType deleteEventType = eventTypes.get(0);
		deleteEventType.remove();

		eventTypes = SushiEventType.findAll();
		assertTrue(eventTypes.size() == 1);
		
		assertTrue(eventTypes.get(0).getID() != deleteEventType.getID());
	}

	@Test
	public void testRemoveEventTypeWithEvents() {
		storeExampleEventType();
		storeExampleEvents();
		List<SushiEventType> eventTypes;
		List<SushiEvent> events;
		eventTypes = SushiEventType.findAll();
		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);
		
		SushiEventType deleteEventType = SushiEventType.findByTypeName("Kino");

		events = SushiEvent.findByEventType(deleteEventType);
		assertTrue("Value should be 1, but was " +events.size(), events.size() == 1);

//		assertTrue("should contain 1 event, but contains " + eventTypes.get(0).getChilds().size(), eventTypes.get(0).getChilds().size() == 1);
		
		deleteEventType.remove();

		events = SushiEvent.findByEventType(deleteEventType);
		assertTrue("Value should be 0, but was " + events.size(), events.size() == 0);
		
		eventTypes = SushiEventType.findAll();
		assertTrue(eventTypes.size() == 1);

		assertTrue(eventTypes.get(0).getID() != deleteEventType.getID());
	}
}
