package sushi.querycreation.test;

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
import sushi.xml.importer.SignavioBPMNParser;

public class XORTest extends AbstractQueryCreationTest{
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/XORTest.bpmn20.xml";
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 9);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "XORProcess", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}

	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType first = new SushiEventType("FirstEvent", values, "TimeStamp");
		first.save();
		
		values = createAttributeTree();
		SushiEventType branch1First = new SushiEventType("Branch1_FirstEvent", values, "TimeStamp");
		branch1First.save();
		
		values = createAttributeTree();
		SushiEventType branch1Second = new SushiEventType("Branch1_SecondEvent", values, "TimeStamp");
		branch1Second.save();
		
		values = createAttributeTree();
		SushiEventType branch2First = new SushiEventType("Branch2_FirstEvent", values, "TimeStamp");
		branch2First.save();
		
		values = createAttributeTree();
		SushiEventType second = new SushiEventType("SecondEvent", values, "TimeStamp");
		second.save();
		
		eventTypes.add(first);
		eventTypes.add(branch1First);
		eventTypes.add(branch2First);
		eventTypes.add(branch1Second);
		eventTypes.add(second);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		//XOR-Pfade bauen
		Random random = new Random();
		int choose = random.nextInt(2);
		
		//Events für verschiedene Prozessinstanzen erzeugen und an Broker senden
		//Reihenfolge der Events ist wichtig (über Liste abgebildet)
		for(SushiEventType eventType : eventTypes){
			SushiMapTree<String, Serializable> values;
			SushiEvent event;
			
			if(choose == 0){ /*oberer Pfad*/
				if(eventType.getTypeName().equals("Branch1_FirstEvent") || eventType.getTypeName().equals("Branch1_SecondEvent")){
					continue;
				}
			} else { /*unterer Pfad*/
				if(eventType.getTypeName().equals("Branch2_FirstEvent")){
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
		}
	}
	
	@AfterClass
	public static void tearDown() {
		AbstractQueryCreationTest.resetDatabase();
	}

}
