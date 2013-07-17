package sushi.monitoring.querycreation.timer;

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
 * Tests the boundary intermediate timer event with a waiting time of zero and 
 * not sending the message intermediate event that could follow the TimerTask.
 * @author micha
 */
public class MessageAndTimerWithZeroTimeTest extends AbstractQueryCreationTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/MessageAndTimerWithZeroWaiting.bpmn20.xml";
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
		queryCreationTemplateMethod(filePath, "MessageAndTimer", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
		MessageAndTimerTest.afterQueriesTests(process);
	}
	
	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		values = createAttributeTree();
		SushiEventType messageStart = new SushiEventType("MessageStart", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType timerTask = new SushiEventType("TimerTask", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType messageIntermediate = new SushiEventType("MessageIntermediate", values, "Timestamp");
		values = createAttributeTree();
		SushiEventType secondTask = new SushiEventType("SecondTask", values, "Timestamp");
		
		
		eventTypes.add(messageStart);
		eventTypes.add(timerTask);
		eventTypes.add(messageIntermediate);
		eventTypes.add(secondTask);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		//Events für verschiedene Prozessinstanzen erzeugen und an Broker senden
		for(SushiEventType eventType : eventTypes){
			//MessageIntermediate skippen um attached intermediate timer zu testen
			if(eventType.getTypeName().equals("MessageIntermediate")){
				try {
					Thread.sleep(15 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
