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
 * This test tests the creation of queries for a simple sequence A->B->C, 
 * but also under consideration of multiple monitoring points for one task, 
 * so that the life cycle of a task is monitorable.
 * @author micha
 */
public class SimpleSequenceStateTransitionTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/SimpleSequenceStateTransition.bpmn20.xml";
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = BPMNParser.generateProcessFromXML(filePath);
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
		SushiEventType firstBegin = new SushiEventType("FirstEventBegin", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType firstTerminate = new SushiEventType("FirstEventTerminate", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType secondEnable = new SushiEventType("SecondEventEnable", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType secondTerminate = new SushiEventType("SecondEventTerminate", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType third = new SushiEventType("ThirdEvent", values, "Timestamp");
		
		eventTypes.add(firstBegin);
		eventTypes.add(firstTerminate);
		eventTypes.add(secondEnable);
		eventTypes.add(secondTerminate);
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
		}
	}
	
	@AfterClass
	public static void tearDown() {
		AbstractQueryCreationTest.resetDatabase();
	}
	
}
