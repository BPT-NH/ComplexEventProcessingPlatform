package sushi.querycreation.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.xml.importer.SignavioBPMNParser;

/**
 * @author micha
 */
public class SimpleSequenceTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/SimpleSequence.bpmn20.xml";
		Persistor.useTestEnviroment();
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 5);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "SimpleProcess", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}

	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType first = new SushiEventType("FirstEvent", values, "Timestamp");
		
		values = createAttributeTree();
		SushiEventType second = new SushiEventType("SecondEvent", values, "Timestamp");
		
		values = createAttributeTree();
		SushiEventType third = new SushiEventType("ThirdEvent", values, "Timestamp");
		
		eventTypes.add(first);
		eventTypes.add(second);
		eventTypes.add(third);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		for(SushiEventType eventType : eventTypes){
			SushiMapTree<String, Serializable> values;
			SushiEvent event;
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 1);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 2);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 3);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 4);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
		}
	}
	
	@AfterClass
	public static void tearDown() {
		AbstractQueryCreationTest.resetDatabase();
	}
	
}
