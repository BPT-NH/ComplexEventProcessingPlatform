package sushi.aggregation.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
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
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

public class TruckUsageWithExternalKnowledgeTest {
	
	private ExcelNormalizer excelNormalizer;
	private String folder = System.getProperty("user.dir") + "/src/main/resources/truckUsageWithExternalKnowledgeTest/";
	private String truckRouteFile = folder + "Distances.xls";
	private String firstObuEventsFile = folder + "OBU_Events_01.xls";
	private String secondObuEventsFile = folder + "OBU_Events_02.xls";
	private String firstTruckUsageFile = folder + "Truck_Usage_Plan_Advanced_01.xls";
	private String secondTruckUsageFile = folder + "Truck_Usage_Plan_Advanced_02.xls";
	
	@Before
	public void setup() {
		Persistor.useTestEnviroment();
		SushiEsper.clearInstance();
		excelNormalizer = new ExcelNormalizer();
	}
	
	@Test
	public void testTruckUsageFiles() {
		
		List<SushiAttribute> attributes;
		
		SushiAttribute rootAttribute1 = new SushiAttribute("Origin", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute2 = new SushiAttribute("Destination", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute3 = new SushiAttribute("Distance in km", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute4 = new SushiAttribute("Time approx", SushiAttributeTypeEnum.INTEGER);
		attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3, rootAttribute4);
		SushiEventType eventType1 = new SushiEventType("TruckRoute", attributes, "Import time");
		Broker.send(eventType1);
		
		SushiAttribute rootAttribute5 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute6 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute7 = new SushiAttribute("Action", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute5, rootAttribute6, rootAttribute7);
		SushiEventType eventType2 = new SushiEventType("truckUsage", attributes, "Timestamp");
		assertTrue("Event type already registered", !SushiEsper.getInstance().isEventType(eventType2));
		Broker.send(eventType2);
		
		SushiAttribute rootAttribute8 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute9 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute8, rootAttribute9);
		SushiEventType eventType3 = new SushiEventType("obuEvent", attributes, "Timestamp");
		Broker.send(eventType3);
		
		SushiAttribute rootAttribute10 = new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute11 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute12 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute13 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute10, rootAttribute11, rootAttribute12, rootAttribute13);
		SushiEventType eventType4 = new SushiEventType("enrichedObuEvent", attributes, "Timestamp");
		Broker.send(eventType4);
		
		SushiAttribute rootAttribute14 = new SushiAttribute("Timestamp Second Location", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute15 = new SushiAttribute("Origin", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute16 = new SushiAttribute("Destination", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute17 = new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute18 = new SushiAttribute("Truck Usage End", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute19 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute20 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute21 = new SushiAttribute("Distance in km", SushiAttributeTypeEnum.INTEGER);
		attributes = Arrays.asList(rootAttribute14, rootAttribute15, rootAttribute16, rootAttribute17, rootAttribute18, rootAttribute19, rootAttribute20, rootAttribute21);
		SushiEventType eventType5 = new SushiEventType("DrivenRoute", attributes, "Timestamp First Location");
		Broker.send(eventType5);
		
		SushiAttribute rootAttribute22 = new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute23 = new SushiAttribute("Truck Usage End", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute24 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute25 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute26 = new SushiAttribute("Total distance in km", SushiAttributeTypeEnum.INTEGER);
		attributes = Arrays.asList(rootAttribute22, rootAttribute23, rootAttribute24, rootAttribute25, rootAttribute26);
		SushiEventType eventType6 = new SushiEventType("TraveledDistance", attributes, "Time of Detection");
		Broker.send(eventType6);

		assertTrue(SushiEsper.getInstance().getEventTypeInfo(eventType4, "Driver") == String.class);
		assertTrue(SushiEsper.getInstance().getEventTypeInfo(eventType6, "Timestamp") == Date.class);
		
		loadData(eventType1, truckRouteFile);

		SushiAggregationRule firstRule = new SushiAggregationRule(eventType4, "someRuleName", 
				"SELECT B.Timestamp as Timestamp, B.Driver as Driver, " +
					"B.Location as Location, A.Truck as Truck, " +
					"A.Timestamp as Truck_Usage_Start " +
				"FROM Pattern[every (" +
					"every A=truckUsage(A.Action='Start') " +
						"-> " +
					"every (B=obuEvent(A.Driver = B.Driver AND A.Timestamp.getTime() < B.Timestamp.getTime()) " +
						"AND NOT C=truckUsage(C.Action='End' AND A.Timestamp.getTime() < C.Timestamp.getTime() " +
							"AND A.Driver = C.Driver AND A.Truck = C.Truck))" +
				")]"
			);
		SushiAggregationListener firstListener = SushiAggregation.getInstance().addToEsper(firstRule);
		
		SushiAggregationRule secondRule = new SushiAggregationRule(eventType5, "someRuleName", 
				"SELECT A.Timestamp AS Timestamp, B.Timestamp AS Timestamp_Second_Location, " + 
				"A.Location AS Origin, B.Location AS Destination, " + 
				"B.Truck_Usage_Start AS Truck_Usage_Start, B.Driver AS Driver, B.Truck AS Truck, " + 
				"coalesce(" +
					"integerValueFromEvent('TruckRoute', 'Distance_in_km', {'Origin', A.Location, 'Destination', B.Location}), " +
					"integerValueFromEvent('TruckRoute', 'Distance_in_km', {'Origin', B.Location, 'Destination', A.Location}), " +
					"0) AS Distance_in_km " + 
				"FROM Pattern[" +
					"every A=enrichedObuEvent " + 
					"-> " +
					"(B=enrichedObuEvent(A.Driver = B.Driver AND A.Truck = B.Truck AND A.Truck_Usage_Start.getTime() = B.Truck_Usage_Start.getTime()) " +
						"AND NOT enrichedObuEvent(Truck_Usage_Start.getTime() = B.Truck_Usage_Start.getTime())" +
				")]"
			);
		SushiAggregationListener secondListener = SushiAggregation.getInstance().addToEsper(secondRule);
		
		SushiAggregationRule thirdRule = new SushiAggregationRule(eventType6, "someRuleName", 
				"SELECT currentDate() AS Timestamp, A.Timestamp AS Truck_Usage_Start, C.Timestamp AS Truck_Usage_End, " +
				"A.Truck AS Truck, A.Driver AS Driver, sumFromEventList(B, 'Distance_in_km') AS Total_distance_in_km " +
				"FROM Pattern[" +
					"every A=truckUsage(A.Action='Start') " +
					"-> " +
					"(B=DrivenRoute(A.Timestamp.getTime() = B.Truck_Usage_Start.getTime() AND A.Truck = B.Truck) " +
						"UNTIL (C=truckUsage(C.Action='End' AND A.Timestamp.getTime() < C.Timestamp.getTime() " +
							"AND A.Driver = C.Driver AND A.Truck = C.Truck))" +
				")]"
			);
		SushiAggregationListener thirdListener = SushiAggregation.getInstance().addToEsper(thirdRule);
		
		loadData(eventType2, firstTruckUsageFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 0, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 0);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 0, but was " + SushiEvent.findByEventType(eventType4).size(), SushiEvent.findByEventType(eventType4).size() == 0);
		assertTrue("Number of generated DrivenRoute events in the event processing engine should be 0, but was " + secondListener.getNumberOfEventsFired(), secondListener.getNumberOfEventsFired() == 0);
		assertTrue("Number of generated DrivenRoute events in the database should be 0, but was " + SushiEvent.findByEventType(eventType5).size(), SushiEvent.findByEventType(eventType5).size() == 0);	
		assertTrue("Number of generated TraveledDistance events in the event processing engine should be 0, but was " + thirdListener.getNumberOfEventsFired(), thirdListener.getNumberOfEventsFired() == 0);
		assertTrue("Number of generated TraveledDistance events in the database should be 0, but was " + SushiEvent.findByEventType(eventType6).size(), SushiEvent.findByEventType(eventType6).size() == 0);
		
		loadData(eventType3, firstObuEventsFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 16, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 16);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 16, but was " + SushiEvent.findByEventType(eventType4).size(), SushiEvent.findByEventType(eventType4).size() == 16);
		assertTrue("Number of generated DrivenRoute events in the event processing engine should be 14, but was " + secondListener.getNumberOfEventsFired(), secondListener.getNumberOfEventsFired() == 14);
		assertTrue("Number of generated DrivenRoute events in the database should be 14, but was " + SushiEvent.findByEventType(eventType5).size(), SushiEvent.findByEventType(eventType5).size() == 14);	
		assertTrue("Number of generated TraveledDistance events in the event processing engine should be 0, but was " + thirdListener.getNumberOfEventsFired(), thirdListener.getNumberOfEventsFired() == 0);
		assertTrue("Number of generated TraveledDistance events in the database should be 0, but was " + SushiEvent.findByEventType(eventType6).size(), SushiEvent.findByEventType(eventType6).size() == 0);
		
		loadData(eventType2, secondTruckUsageFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 16, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 16);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 16, but was " + SushiEvent.findByEventType(eventType4).size(), SushiEvent.findByEventType(eventType4).size() == 16);
		assertTrue("Number of generated DrivenRoute events in the event processing engine should be 14, but was " + secondListener.getNumberOfEventsFired(), secondListener.getNumberOfEventsFired() == 14);
		assertTrue("Number of generated DrivenRoute events in the database should be 14, but was " + SushiEvent.findByEventType(eventType5).size(), SushiEvent.findByEventType(eventType5).size() == 14);	
		assertTrue("Number of generated TraveledDistance events in the event processing engine should be 2, but was " + thirdListener.getNumberOfEventsFired(), thirdListener.getNumberOfEventsFired() == 2);
		assertTrue("Number of generated TraveledDistance events in the database should be 2, but was " + SushiEvent.findByEventType(eventType6).size(), SushiEvent.findByEventType(eventType6).size() == 2);
		List<SushiEvent> traveledDistanceEvents = SushiEvent.findByEventType(eventType6);
		for (SushiEvent event : traveledDistanceEvents) {
			if (event.getValues().get("Driver").equals("Helmut")) {
				assertTrue("Distanced traveled by Helmut should be 4886, but was " + event.getValues().get("Total_distance_in_km"), ((Integer) event.getValues().get("Total_distance_in_km")).equals(new Integer(4886)));
			} else if (event.getValues().get("Driver").equals("Paul")) {
				assertTrue("Distanced traveled by Paul should be 9196, but was " + event.getValues().get("Total_distance_in_km"), ((Integer) event.getValues().get("Total_distance_in_km")).equals(new Integer(9196)));
			}
		}
		
		loadData(eventType3, secondObuEventsFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 27, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 27);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 27, but was " + SushiEvent.findByEventType(eventType4).size(), SushiEvent.findByEventType(eventType4).size() == 27);
		assertTrue("Number of generated DrivenRoute events in the event processing engine should be 24, but was " + secondListener.getNumberOfEventsFired(), secondListener.getNumberOfEventsFired() == 24);
		assertTrue("Number of generated DrivenRoute events in the database should be 24, but was " + SushiEvent.findByEventType(eventType5).size(), SushiEvent.findByEventType(eventType5).size() == 24);	
		assertTrue("Number of generated TraveledDistance events in the event processing engine should be 2, but was " + thirdListener.getNumberOfEventsFired(), thirdListener.getNumberOfEventsFired() == 2);
		assertTrue("Number of generated TraveledDistance events in the database should be 2, but was " + SushiEvent.findByEventType(eventType6).size(), SushiEvent.findByEventType(eventType6).size() == 2);
	}
	
	public void loadData(SushiEventType eventType, String filePath) {
		List<SushiAttribute> attributes = eventType.getValueTypes();
		String timestamp = eventType.getTimestampName();
		// insert events into event type via SushiEsper
		List<SushiEvent> events = excelNormalizer.importEventsFromFile(filePath, attributes, timestamp);
		for (SushiEvent event : events) {
			event.setEventType(eventType);
		}
		
		Broker.send(events);
	}
	
}