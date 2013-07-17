package sushi.transformation.test;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.persistence.Persistor;
import sushi.query.SushiQueryTypeEnum;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiQuery;
import sushi.transformation.TransformationManager;
import sushi.transformation.TransformationListener;
import sushi.transformation.TransformationRule;

public class KinoRatingTest {

	private SushiStreamProcessingAdapter sushi;
	private static String kinoFileName = "KinoRating 31.10..xls";
	private static String kinoFilePath = System.getProperty("user.dir") + "/src/main/resources/" + kinoFileName;
	private List<SushiEvent> events;
	private SushiEventType type;

	/**
	 * @param args
	 */
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		sushi = SushiStreamProcessingAdapter.getInstance();
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
		type = new SushiEventType("KinoEvent", attributes, "Timestamp");
		Broker.send(type);
		SushiEvent.setEventType(events, type);
		System.out.println("Events imported: " + events.size());
		System.out.println(events.get(0));
	}
	
	@Test
	public void simpleTransformationTest() {
		String query =	"Select 'doublered' as Rating, B.Location as Location, A.Timestamp as Timestamp " +
			"From Pattern [ every (A=KinoEvent(Rating = 'red') ->" +
			 "B=KinoEvent(Rating = A.Rating " +
			"AND Location = A.Location)) ]";
		TransformationManager transformation = new TransformationManager();
		TransformationRule transformationRule = new TransformationRule(type, "test_rule1", query);
		TransformationListener listener = transformation.register(transformationRule);	
		Broker.send(events);
				
		assertTrue("Not the same sushis", sushi == transformation.getEsper());
		assertTrue("Not tha same administration", sushi.getEsperAdministrator() == transformation.getEsperAdministrator());
	
		assertTrue("Listener should have found 2 matches, but found " + listener.getNumberOfEventsFired(), listener.getNumberOfEventsFired() == 2);
	}

	@Test
	public void transformationTest() {
		String query =	"Select B.Location as Location, A.Timestamp as FirstOccurence, B.Timestamp as SecondOccurence " +
			"From Pattern [ every (A=KinoEvent(Rating = 'red') ->" +
			 "B=KinoEvent(Rating = 'red' " +
			"AND Location = B.Location)) ]";
			
		SushiAttribute rootAttribute1 = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute2 = new SushiAttribute("FirstOccurence", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute3 = new SushiAttribute("SecondOccurence", SushiAttributeTypeEnum.DATE);
		
		List<SushiAttribute> attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3);
		SushiEventType newType = new SushiEventType("newType", attributes, "Timestamp");
		Broker.send(newType);
		TransformationManager transformation = new TransformationManager();
		TransformationRule transformationRule = new TransformationRule(newType, "test_rule2", query);
		TransformationListener transformationListener = transformation.register(transformationRule);	
		
		// listener to check whether transformed events are added to Esper
		SushiQuery queryForTransformedEvents = new SushiQuery("transformedEvents", "select * from " + newType.getTypeName(), SushiQueryTypeEnum.LIVE);
		SushiLiveQueryListener listener = queryForTransformedEvents.addToEsper();
		
		Broker.send(events);
					
		assertTrue("Listener should have found 2 matches, but found " + transformationListener.getNumberOfEventsFired(), transformationListener.getNumberOfEventsFired() == 2);

		// check whether transformed Events are added to Database
		int numberOfTransformedEventsAddedToDB = SushiEvent.findByEventType(newType).size();
		assertTrue("should have added 2 events to database, but added " + numberOfTransformedEventsAddedToDB, numberOfTransformedEventsAddedToDB == 2);
		
		int numberOfFiredEvents = listener.getNumberOfLogEntries();
		assertTrue("should have added 2 events to esper, but added " + numberOfFiredEvents, numberOfFiredEvents == 2);
		
	}
}
