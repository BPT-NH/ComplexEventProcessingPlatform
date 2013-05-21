package sushi.aggregation.test;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationListener;
import sushi.aggregation.SushiAggregationRule;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;
import sushi.persistence.Persistor;
import sushi.query.SushiQueryTypeEnum;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiQuery;

public class KinoRatingTest {

	private SushiEsper sushi;
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
		sushi = SushiEsper.getInstance();
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
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
	public void simpleAggregationTest() {
		String query =	"Select 'doublered' as Rating, B.Location as Location, A.Timestamp as Timestamp " +
			"From Pattern [ every (A=KinoEvent(Rating = 'red') ->" +
			 "B=KinoEvent(Rating = A.Rating " +
			"AND Location = A.Location)) ]";
		SushiAggregation aggregation = new SushiAggregation();
		SushiAggregationRule aggregationRule = new SushiAggregationRule(type, "test_rule1", query);
		SushiAggregationListener listener = aggregation.addToEsper(aggregationRule);	
		Broker.send(events);
				
		assertTrue("Not the same sushis", sushi == aggregation.getEsper());
		assertTrue("Not tha same administration", sushi.getEsperAdministrator() == aggregation.getEsperAdministrator());
	
		assertTrue("Listener should have found 2 matches, but found " + listener.getNumberOfEventsFired(), listener.getNumberOfEventsFired() == 2);
	}

	@Test
	public void aggregationTest() {
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
		SushiAggregation aggregation = new SushiAggregation();
		SushiAggregationRule aggregationRule = new SushiAggregationRule(newType, "test_rule2", query);
		SushiAggregationListener aggregationListener = aggregation.addToEsper(aggregationRule);	
		
		//Listener to check whether aggregated Events are added to Esper
		SushiQuery queryForAggregatedEvents = new SushiQuery("aggregatedEvents", "select * from " + newType.getTypeName(), SushiQueryTypeEnum.LIVE);
		SushiLiveQueryListener listener = queryForAggregatedEvents.addToEsper(sushi);
		
		Broker.send(events);
					
		assertTrue("Listener should have found 2 matches, but found " + aggregationListener.getNumberOfEventsFired(), aggregationListener.getNumberOfEventsFired() == 2);

		//check whether aggregated Events are added to Database
		int numberOfAggregatedEventsAddedToDB = SushiEvent.findByEventType(newType).size();
		assertTrue("should have added 2 events to database, but added " + numberOfAggregatedEventsAddedToDB, numberOfAggregatedEventsAddedToDB == 2);
		
		int numberOfFiredEvents = listener.getNumberOfLogEntries();
		assertTrue("should have added 2 events to esper, but added " + numberOfFiredEvents, numberOfFiredEvents== 2);
		
	}
}
