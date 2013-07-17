package sushi.esper.queries;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.EPOnDemandQueryResult;


import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.persistence.Persistor;
import sushi.query.SushiQueryTypeEnum;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiQuery;

public class SushiEsperTests {
	SushiStreamProcessingAdapter esper;
	private static String kinoFileName = "Kino.xls";
	private static String kinoFilePath = System.getProperty("user.dir") + "/src/test/resources/" + kinoFileName;
	private static List<SushiEvent> events;
	private SushiEventType eventType;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		SushiStreamProcessingAdapter.clearInstance();
		assertTrue(SushiStreamProcessingAdapter.instanceIsCleared());
		esper = SushiStreamProcessingAdapter.getInstance();
		ExcelImporter excelNormalizer = new ExcelImporter();
		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(kinoFilePath);
		assertTrue("Not the right attributes", columnTitles.equals(new ArrayList<String>(Arrays.asList("Timestamp", "Location", "Rating"))));
		columnTitles.remove("Timestamp");
		List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
		for (String attributeName : columnTitles) {
			if (attributeName.equals("Location")) {
				attributes.add(new SushiAttribute(attributeName, SushiAttributeTypeEnum.INTEGER));
			} else if (attributeName.equals("Rating")) {
				attributes.add(new SushiAttribute(attributeName, SushiAttributeTypeEnum.STRING));
			}
		}
		events = excelNormalizer.importEventsFromFile(kinoFilePath, attributes);
		eventType = new SushiEventType("Kino", attributes, "Timestamp");
		assertTrue("KinoWindow already exists", !esper.hasWindow("KinoWindow"));
		System.out.println("Events imported: " + events.size());
	}
	
	@Test
	public void testEventTypes() {
		//eventTyp erzeugen
		SushiAttributeTree attributes = new SushiAttributeTree();
		attributes.addRoot(new SushiAttribute("Sorte", SushiAttributeTypeEnum.STRING));
		attributes.addRoot(new SushiAttribute("Leckerheitsgrad", SushiAttributeTypeEnum.STRING));
		SushiEventType eventType = new SushiEventType("Eiscreme", attributes); 
		esper.addEventType(eventType);
		assertTrue("Event type was not added.", esper.isEventType(eventType));
		
		//Events erzeugen
		SushiMapTree<String, Serializable> values1 = new SushiMapTree<String, Serializable>();
		values1.addRootElement("Sorte", "Vanille");
		values1.addRootElement("Leckerheitsgrad", "awesome");
		SushiEvent event1 = new SushiEvent(eventType, new Date(System.currentTimeMillis()), values1);
		
		SushiMapTree<String, Serializable> values2 = new SushiMapTree<String, Serializable>();
		values1.addRootElement("Sorte", "Schoko");
		values1.addRootElement("Leckerheitsgrad", "super awesome");
		SushiEvent event2 = new SushiEvent(eventType, new Date(System.currentTimeMillis()), values2);
		
		ArrayList<SushiEvent> events = new ArrayList<SushiEvent>();
		events.add(event1);
		events.add(event2);
		SushiEvent.setEventType(events, eventType);
		esper.addEvents(events);
		
	}
	
	@Test
	public void testAddingLiveQuery(){
		eventType = Broker.send(eventType);
		SushiEventType eventTypeFromDB = SushiEventType.findByTypeName("Kino");
		assertTrue("eventType not found", eventTypeFromDB != null);
		assertTrue("eventType not the same", eventTypeFromDB == eventType);
		String query = "Select * From " + eventType.getTypeName();
		SushiQuery liveQuery = new SushiQuery("All", query, SushiQueryTypeEnum.LIVE);
		liveQuery.save();
		SushiLiveQueryListener listener = liveQuery.addToEsper();
		SushiEvent.setEventType(events, eventType);
		Broker.send(events);
		SushiLiveQueryListener listener2 = esper.getListenerByQuery(query);
		assertTrue("should be same Listeners", listener == listener2);
		assertTrue(listener.getNumberOfLogEntries() == events.size());
		SushiQuery liveQueryFromDB = SushiQuery.findQueryByTitle("All");
		assertTrue(liveQueryFromDB.getLog().size() == events.size());
	}

	@Test
	public void testSushiQuery(){
		eventType = Broker.send(eventType);
		SushiQuery query = new SushiQuery("testquery", "Select * From KinoWindow", SushiQueryTypeEnum.ONDEMAND);
		SushiEventType eventType = SushiEventType.findByTypeName("Kino");
		assertTrue("Event type not found", eventType != null);
		SushiEvent.setEventType(events, eventType);
		Broker.send(events);
		String log = query.execute();
		assertTrue("expected 999, got: " + log.substring(log.length()-4, log.length()), log.endsWith("999")); //999 events, last line has \n too
	}
	
	@Test
	public void testWindowCreation(){
		assertTrue("Window has already been created", ! SushiStreamProcessingAdapter.getInstance().hasWindow(eventType.getTypeName() + "Window"));
		SushiStreamProcessingAdapter.getInstance().createWindow(eventType);
		assertTrue("Window has not been created", SushiStreamProcessingAdapter.getInstance().hasWindow(eventType.getTypeName() + "Window"));
		//Send Events
		for (SushiEvent event : events) {
			event.setEventType(eventType);
			SushiStreamProcessingAdapter.getInstance().addEvent(event);
		}
		
		EPOnDemandQueryResult result = SushiStreamProcessingAdapter.getInstance().getEsperRuntime().executeQuery("Select * From KinoWindow");
		assertTrue("Number of events should have been 999, instead of " + result.getArray().length, result.getArray().length == 999);
	}

}
