package sushi.monitoring.querycreation.subprocess;

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
import sushi.monitoring.querycreation.AbstractQueryCreationTest;
import sushi.persistence.Persistor;
import sushi.xml.importer.BPMNParser;

/**
 * This class tests the import of a BPMN process with two end events, 
 * the creation of queries for this BPMN process and 
 * simulates the execution of the process to monitor the execution.
 * Before query creation, the process must be pre-processed and the two end events must be merged.
 * @author micha
 */
public class ProcessWithTwoEndEventsTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/Automontage.bpmn20.xml";
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = BPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 7);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "ProcessTwoEnds", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}

	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType karosserie = new SushiEventType("Karosserie", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType sommerReifen = new SushiEventType("Sommerreifen", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType winterReifen = new SushiEventType("Winterreifen", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType ausliefern = new SushiEventType("Ausliefern", values, "TimeStamp");
		
		values = createAttributeTree();
		SushiEventType fehlerbehandlung = new SushiEventType("Fehlerbehandlung", values, "TimeStamp");
		
		eventTypes.add(karosserie);
		eventTypes.add(fehlerbehandlung);
		eventTypes.add(sommerReifen);
		eventTypes.add(winterReifen);
		eventTypes.add(ausliefern);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		for(SushiEventType eventType : eventTypes){
			//Durchlauf ohne Fehler
			if(eventType.getTypeName().equals("Fehlerbehandlung") || eventType.getTypeName().equals("Winterreifen")){
				continue;
			}
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
