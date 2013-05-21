package sushi.querycreation.test.complex;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import sushi.querycreation.test.AbstractQueryCreationTest;
import sushi.xml.importer.SignavioBPMNParser;

public class ComplexProcessTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/ComplexProcessTest.bpmn20.xml";
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue("Number of BPMN elements without sequence flows should be 17 but was " + BPMNProcess.getBPMNElementsWithOutSequenceFlows().size(), BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 17);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "ComplexProcess", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}
	
	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType task1 = new SushiEventType("Task1", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task2 = new SushiEventType("Task2", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task3 = new SushiEventType("Task3", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task4 = new SushiEventType("Task4", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task5 = new SushiEventType("Task5", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task6 = new SushiEventType("Task6", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType task7 = new SushiEventType("Task7", values, "Timestamp");
		
		eventTypes.add(task1);
		eventTypes.add(task2);
		eventTypes.add(task3);
		eventTypes.add(task2);
		eventTypes.add(task4);
		eventTypes.add(task5);
		eventTypes.add(task6);
		eventTypes.add(task7);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		//XOR-Pfade bauen
		Random random = new Random();
		int choose = random.nextInt(3);
		
		//Events für verschiedene Prozessinstanzen erzeugen und an Broker senden
		for(SushiEventType eventType : eventTypes){
			SushiMapTree<String, Serializable> values;
			SushiEvent event;
			
			if(choose == 0){ /*oberer Pfad*/
				if(eventType.getTypeName().equals("Task4") || eventType.getTypeName().equals("Task5") || eventType.getTypeName().equals("Task6")){
					continue;
				}
			} else if(choose == 1){ /*mittlerer Pfad*/
				if(eventType.getTypeName().equals("Task2") || eventType.getTypeName().equals("Task3") || eventType.getTypeName().equals("Task5") || eventType.getTypeName().equals("Task6")){
					continue;
				}
			} else { /*unterer Pfad*/
				if(eventType.getTypeName().equals("Task2") || eventType.getTypeName().equals("Task3") || eventType.getTypeName().equals("Task4")){
					continue;
				}
			}
			
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
