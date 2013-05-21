package sushi.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.EventTypeRule;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.notification.SushiCondition;
import sushi.persistence.Persistor;

public class EventTypeRuleTest implements PersistenceTest {

	private SushiEventType eventType;
	private SushiEventType createdEventType;
	private EventTypeRule eventTypeRule;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve() {
		eventTypeRule = createEventTypeRule();
		eventTypeRule.save();
		assertNotNull(EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType));
		EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType).equals(eventTypeRule);
	}

	@Test
	@Override
	public void testFind() {
		eventTypeRule = createEventTypeRule();
		eventTypeRule.save();
		assertNotNull(EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType));
		EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType).equals(eventTypeRule);
	}

	@Test
	@Override
	public void testRemove() {
		eventTypeRule = createEventTypeRule();
		eventTypeRule.save();
		assertNotNull(EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType));
		eventTypeRule.remove();
		assertNull(EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType));
	}
	
	@Test
	public void testRemoveOfContainedEventType() {
		eventTypeRule = createEventTypeRule();
		eventTypeRule.save();
		assertNotNull(EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType));
		eventType.remove();
		assertNull(SushiEventType.findByID(eventType.getID()));
	}
	
	private EventTypeRule createEventTypeRule() {
		SushiAttribute rootAttribute1 = new SushiAttribute("Timestamp", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute2 = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		List<SushiAttribute> attributes1 = Arrays.asList(rootAttribute1, rootAttribute2);
		eventType = new SushiEventType("testEventType1", attributes1);
		eventType.save();
		SushiAttribute rootAttribute3 = new SushiAttribute("Timestamp", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute4 = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		List<SushiAttribute> attributes2 = Arrays.asList(rootAttribute3, rootAttribute4);
		createdEventType = new SushiEventType("testEventType2", attributes2);
		createdEventType.save();
		eventTypeRule = new EventTypeRule(new ArrayList<SushiEventType>(Arrays.asList(eventType)), new SushiCondition(), createdEventType);
		return eventTypeRule;
	}
	
	private void storeExampleEventType() {
		SushiAttributeTree values = new SushiAttributeTree();
		values.addRoot(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER));
		values.addRoot(new SushiAttribute("SecondaryEvent", SushiAttributeTypeEnum.STRING));
		SushiEventType firstEventType = new SushiEventType("Kino", values);

		SushiEventType secondEventType = new SushiEventType("GET-Transport");
		
		ArrayList<SushiEventType> eventTypes = new ArrayList<SushiEventType>(Arrays.asList(firstEventType, secondEventType));
		assertTrue(SushiEventType.save(eventTypes));
	}
	
	@Test
	public void testRemoveEventTypeWithEventTypeRuleForCreation(){
		storeExampleEventType();
		ArrayList<SushiEventType> usedEventTypes = new ArrayList<SushiEventType>();
		usedEventTypes.addAll(SushiEventType.findAll());
		SushiEventType createdEventType = SushiEventType.findByTypeName("Kino");
		EventTypeRule rule = new EventTypeRule(usedEventTypes, new SushiCondition(), createdEventType);
		rule.save();

		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);
		assertTrue("rule not saved", EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType) != null);
		SushiEventType deleteEventType = SushiEventType.findByTypeName("Kino");
		deleteEventType.remove();
		assertTrue("should not find eventtyperule, but found " + EventTypeRule.findEventTypeRuleForCreatedEventType(deleteEventType), EventTypeRule.findEventTypeRuleForCreatedEventType(deleteEventType) == null);

		List<SushiEventType> eventTypes;
		eventTypes = SushiEventType.findAll();
		assertTrue(eventTypes.size() == 1);

		assertTrue(eventTypes.get(0).getID() != deleteEventType.getID());
	}

	@Test
	public void testRemoveEventTypeWithEventTypeRuleAsSource(){
		storeExampleEventType();
		ArrayList<SushiEventType> usedEventTypes = new ArrayList<SushiEventType>();
		usedEventTypes.addAll(SushiEventType.findAll());
		SushiEventType createdEventType = SushiEventType.findByTypeName("Kino");
		EventTypeRule rule = new EventTypeRule(usedEventTypes, new SushiCondition(), createdEventType);
		rule.save();
		List<SushiEventType> eventTypes;
		eventTypes = SushiEventType.findAll();
		System.out.println(eventTypes);
		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);
		SushiEventType deleteEventType = SushiEventType.findByTypeName("GET-Transport");
		deleteEventType.remove();
		
		assertTrue("eventtyperule was deleted, but should not have been ", EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType) != null);

		eventTypes = SushiEventType.findAll();
		assertTrue(eventTypes.size() == 1);

		assertTrue(eventTypes.get(0).getID() != deleteEventType.getID());		
	}
	
	@Test
	public void testRemoveEventTypeWithEventTypeRuleAsOnlySource(){
		assertTrue("Value should be 0, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==0);
		storeExampleEventType();
		ArrayList<SushiEventType> usedEventTypes = new ArrayList<SushiEventType>();
		usedEventTypes.add(SushiEventType.findByTypeName("GET-Transport"));
		SushiEventType createdEventType = SushiEventType.findByTypeName("Kino");
		EventTypeRule rule = new EventTypeRule(usedEventTypes, new SushiCondition(), createdEventType);
		rule.save();
		List<SushiEventType> eventTypes;
		eventTypes = SushiEventType.findAll();
		assertTrue("Value should be 2, but was " + SushiEventType.findAll().size(),SushiEventType.findAll().size()==2);
		SushiEventType deleteEventType = SushiEventType.findByTypeName("GET-Transport");
		deleteEventType.remove();
		
		assertTrue("eventtyperule was not deleted ", EventTypeRule.findEventTypeRuleForCreatedEventType(createdEventType) == null);

		eventTypes = SushiEventType.findAll();
		assertTrue(eventTypes.size() == 1);

		assertTrue(eventTypes.get(0).getID() != deleteEventType.getID());				
	}


}
