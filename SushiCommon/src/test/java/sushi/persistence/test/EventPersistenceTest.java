package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistor;

@FixMethodOrder(MethodSorters.JVM)
public class EventPersistenceTest implements PersistenceTest {
	
	private SushiMapTree<String, Serializable> michaAttributes;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve(){
		storeExampleEvents();
		assertTrue("Value should be 2, but was " + SushiEvent.findAll().size(), SushiEvent.findAll().size()==2);
		SushiEvent.removeAll();
		assertTrue("Value should be 0, but was " + SushiEvent.findAll().size(), SushiEvent.findAll().size()==0);
	}
	
	private void storeExampleEvents() {
		SushiMapTree<String, Serializable> hm = new SushiMapTree<String, Serializable>();
		hm.put("kuchen", "kaese");
		hm.put("kuchen2", "kirsch");
		hm.put("kuchen3", "apfel");
		
		SushiEventType firstEventType = new SushiEventType("Tsun");
		firstEventType.save();
		SushiEvent event1 = new SushiEvent(firstEventType, new Date(), hm);
		
		michaAttributes = new SushiMapTree<String, Serializable>();
		michaAttributes.put("getraenk1", "cola");
		michaAttributes.put("getraenk2", "apfelsaft");
		michaAttributes.put("getraenk3", "fanta");
		Date oldDate = null;
		try {
			oldDate = new SimpleDateFormat("dd/MM/yyyy").parse("18/05/2011");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SushiEventType secondEventType = new SushiEventType("Micha");
		secondEventType.save();
		SushiEvent event2 = new SushiEvent(secondEventType, oldDate, michaAttributes);
		ArrayList<SushiEvent> events = new ArrayList<SushiEvent>(Arrays.asList(event1, event2));
		SushiEvent.save(events);
	}
	
	@Override
	@Test
	public void testFind(){
		storeExampleEvents();
		assertTrue(SushiEvent.findAll().size() == 2);
		SushiEventType tsun = SushiEventType.findByTypeName("Tsun");
		SushiEvent event = SushiEvent.findByEventType(tsun).get(0);
		assertTrue(event.getValues().get("kuchen").equals("kaese"));
		assertTrue(event.getValues().get("kuchen2").equals("kirsch"));
		assertTrue(SushiEvent.findByValue("getraenk1", "cola" ).size() == 1);
		List<SushiEvent> events = SushiEvent.findByValues(michaAttributes);
		assertTrue(events.size() == 1);
		event = events.get(0);
		assertTrue(event.getEventType().getTypeName().equals("Micha"));
	}
	@Test
	public void testFindBetween(){
		storeExampleEvents();
		SushiEventType tsun = SushiEventType.findByTypeName("Tsun");
		SushiEventType micha = SushiEventType.findByTypeName("Micha");
		Date oldDate = null;
		try {
			oldDate = new SimpleDateFormat("dd/MM/yyyy").parse("17/05/2011");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(SushiEvent.findBetween(oldDate, new Date()).size()==2);
		
		assertTrue(SushiEvent.findBetween(oldDate, new Date(), tsun).size()==1);
		try {
			oldDate = new SimpleDateFormat("dd/MM/yyyy").parse("17/05/2012");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(SushiEvent.findBetween(oldDate, new Date()).size()==1);
		assertTrue(SushiEvent.findBetween(oldDate, new Date(), tsun).size()==1);
		assertTrue(SushiEvent.findBetween(oldDate, new Date(), micha).size()==0);
	}
	
	@Override
	@Test
	public void testRemove(){
		storeExampleEvents();
		List<SushiEvent> events;
		events = SushiEvent.findAll();
		assertTrue(events.size() == 2);

		SushiEvent deleteEvent = events.get(0);
		deleteEvent.remove();

		events = SushiEvent.findAll();
		assertTrue(events.size() == 1);
		
		assertTrue(events.get(0).getID() != deleteEvent.getID());
	}
	
	@Test
	public void testFindEventWithEventType(){
		SushiAttributeTree eventTypeTree = new SushiAttributeTree();
		SushiAttribute firstRootAttribute = new SushiAttribute("vehicle_information");
		new SushiAttribute(firstRootAttribute, "ETA", SushiAttributeTypeEnum.DATE);
		SushiAttribute secondRootAttribute = new SushiAttribute("sender", SushiAttributeTypeEnum.STRING);
		eventTypeTree.addRoot(firstRootAttribute);
		eventTypeTree.addRoot(secondRootAttribute);
		SushiEventType testEventType = new SushiEventType("Event", eventTypeTree);
		testEventType.setXMLName("EventTaxonomy");
		testEventType.setXMLEvent(true);
		testEventType.setTimestampName("Current timestamp");
		testEventType.save();
		
		SushiMapTree<String, Serializable> eventValueTree = new SushiMapTree<String, Serializable>();
		eventValueTree.addRootElement("sender", "DHL");
		eventValueTree.addChild("vehicle_information", "ETA", "24.12.2013 20:25");
		SushiEvent testEvent = new SushiEvent(testEventType, new Date(), eventValueTree);
		testEvent.save();
		
		for(SushiEvent eventFromDatabase : SushiEvent.findAll()){
			System.out.println(eventFromDatabase);
		}
		
	}
	
	@Test
	public void testGetNumberOfEvents() {
		storeExampleEvents();
		SushiEventType tsun = SushiEventType.findByTypeName("Tsun");
		assertTrue(SushiEvent.getNumberOfEventsByEventType(tsun) == 1);
	}

	@Test
	public void testGetDistinctValuesOfAttributes() {
		storeExampleEvents();
		SushiEventType tsun = SushiEventType.findByTypeName("Tsun");
		
		SushiMapTree<String, Serializable> hm2 = new SushiMapTree<String, Serializable>();
		hm2.put("kuchen", "kaese");
		hm2.put("kuchen2", "kirschkirsch");
		hm2.put("kuchen3", "apfel");
		SushiEvent event2 = new SushiEvent(tsun, new Date(), hm2);
		event2.save();

		List<String> values = SushiEvent.findDistinctValuesOfAttributeOfType("kuchen", tsun); 
		assertTrue(values.contains("kaese"));
		assertTrue(values.size() == 1);
		
		List<String> values2 = SushiEvent.findDistinctValuesOfAttributeOfType("kuchen2", tsun); 
		assertTrue(values2.contains("kirschkirsch"));
		assertTrue(values2.size() == 2);
		
		long appearancesOfKaese = SushiEvent.findNumberOfAppearancesByAttributeValue("kuchen", "kaese", tsun);
		long appearancesOfKirschkirsch = SushiEvent.findNumberOfAppearancesByAttributeValue("kuchen2", "kirschkirsch", tsun);
		assertTrue("should be 2, but was " + appearancesOfKaese, appearancesOfKaese == 2);
		assertTrue("should be 1, but was " + appearancesOfKirschkirsch, appearancesOfKirschkirsch == 1);
	}
}
