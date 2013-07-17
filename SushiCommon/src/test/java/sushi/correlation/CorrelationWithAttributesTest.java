package sushi.correlation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.util.SushiTestHelper;

/**
 * @author micha
 *
 */
public class CorrelationWithAttributesTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void testCorrelator(){
		assertNumberOfDataSets(0, 0, 0, 0);
		
		//EventTyp anlegen
		List<SushiEventType> eventTypes = SushiTestHelper.createEventTypes();
		SushiEventType kinoEventType = null;
		for(SushiEventType eventType : eventTypes){
			eventType.save();
			if(eventType.getTypeName().equals("Kino")){
				kinoEventType = eventType;
			}
		}
		assertNotNull(kinoEventType);
		assertNumberOfDataSets(0, 2, 0, 0);
		
		//Events reinladen
		List<SushiEvent> events = SushiTestHelper.createEvents(kinoEventType);
		for(SushiEvent event : events){
			event.save();
		}
		
		List<SushiEventType> correlationEventTypes = new ArrayList<SushiEventType>();
		correlationEventTypes.add(kinoEventType);
		
		assertNumberOfDataSets(3, 2, 0, 0);
		
		//Process anlegen
		SushiProcess process = SushiTestHelper.createProcess(Arrays.asList(kinoEventType));
		process.save();
		process.save();
		assertNumberOfDataSets(3, 2, 1, 0);
		
		//Korrelieren
		SushiAttribute correlationAttribute = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		AttributeCorrelator.correlate(correlationEventTypes, Arrays.asList(correlationAttribute), process, null);
		
		//Prüfen, gleiche Anzahl Events, EventTypen, Prozesse und richtige Anzahl Prozessinstanzen
		assertNumberOfDataSets(3, 2, 1, 3);
	}
	
	@Test
	public void testCorrelatorWithOutEvents(){
		assertNumberOfDataSets(0, 0, 0, 0);
		
		//EventTyp anlegen
		List<SushiEventType> eventTypes = SushiTestHelper.createEventTypes();
		SushiEventType kinoEventType = null;
		for(SushiEventType eventType : eventTypes){
			eventType.save();
			if(eventType.getTypeName().equals("Kino")){
				kinoEventType = eventType;
			}
		}
		assertNotNull(kinoEventType);
		assertNumberOfDataSets(0, 2, 0, 0);
		
		List<SushiEventType> correlationEventTypes = new ArrayList<SushiEventType>();
		correlationEventTypes.add(kinoEventType);
		
		//Process anlegen
		SushiProcess process = SushiTestHelper.createProcess(Arrays.asList(kinoEventType));
		process.save();
		assertNumberOfDataSets(0, 2, 1, 0);
		
		assertTrue(SushiProcess.findByName(process.getName()).size() == 1);
		SushiProcess processFromDataBase = SushiProcess.findByName(process.getName()).get(0);
		
		processFromDataBase.save();
		processFromDataBase.save();
		assertNumberOfDataSets(0, 2, 1, 0);
		
		//Korrelieren
		SushiAttribute correlationAttribute = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		AttributeCorrelator.correlate(correlationEventTypes, Arrays.asList(correlationAttribute), processFromDataBase, null);
		
		//Prüfen, gleiche Anzahl Events, EventTypen, Prozesse und richtige Anzahl Prozessinstanzen
		assertNumberOfDataSets(0, 2, 1, 0);
	}
	
	private void assertNumberOfDataSets(int events, int eventTypes, int processes, int processInstances) {
		assertTrue("Number of events must be " + events + " but was " + SushiEvent.findAll().size(), SushiEvent.findAll().size() == events);
		assertTrue("Number of event types must be " + eventTypes + " but was " + SushiEventType.findAll().size(), SushiEventType.findAll().size() == eventTypes);
		assertTrue("Number of processes must be " + processes + " but was " + SushiProcess.findAll().size(), SushiProcess.findAll().size() == processes);
		assertTrue("Number of process instances must be " + processInstances + " but was " + SushiProcessInstance.findAll().size(), SushiProcessInstance.findAll().size() == processInstances);
	}

}
