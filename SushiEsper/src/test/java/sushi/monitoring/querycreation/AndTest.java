package sushi.monitoring.querycreation;

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
import sushi.xml.importer.BPMNParser;

/**
 * This class tests the import of a BPMN process with and gateways, 
 * the creation of queries for this BPMN process and 
 * simulates the execution of the process to monitor the execution.
 * @author micha
 */
public class AndTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/AndTest.bpmn20.xml";
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = BPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 9);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "ANDProcess", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}

	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType first = new SushiEventType("FirstEvent", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType branch1First = new SushiEventType("Branch1_FirstEvent", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType branch1Second = new SushiEventType("Branch1_SecondEvent", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType branch2First = new SushiEventType("Branch2_FirstEvent", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType second = new SushiEventType("SecondEvent", values, "TimeStamp");
		
		eventTypes.add(first);
		eventTypes.add(branch1First);
		eventTypes.add(branch2First);
		eventTypes.add(branch1Second);
		eventTypes.add(second);
		
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
		}
	}

	@AfterClass
	public static void tearDown() {
		AbstractQueryCreationTest.resetDatabase();
	}

}
