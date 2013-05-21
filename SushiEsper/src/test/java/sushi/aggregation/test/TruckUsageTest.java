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

public class TruckUsageTest {
	
	private ExcelNormalizer excelNormalizer;
	private String obuEventsFile = "src/main/resources/OBU_Events.xls";
	private String truckUsageFile = "src/main/resources/Truck_Usage_Plan.xls";
	
	/**
	 * @param args
	 */
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		SushiEsper.clearInstance();
		excelNormalizer = new ExcelNormalizer();

	}
	
	@Test
	public void testTruckUsageFiles() {
		SushiAttribute rootAttribute1 = new SushiAttribute("TimeStamp End", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute2 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute3 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		
		List<SushiAttribute> attributes1 = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3);
		SushiEventType eventType1 = new SushiEventType("truckUsage", attributes1, "Timestamp Begin");
		assertTrue("eventtype already registered", !SushiEsper.getInstance().isEventType(eventType1));
		Broker.send(eventType1);
		
		SushiAttribute rootAttribute4 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute5 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		
		List<SushiAttribute> attributes2 = Arrays.asList(rootAttribute4, rootAttribute5);
		SushiEventType eventType2 = new SushiEventType("obuEvent", attributes2, "Timestamp");
		Broker.send(eventType2);
		
		SushiAttribute rootAttribute6 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute7 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute8 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> attributes3 = Arrays.asList(rootAttribute6, rootAttribute7, rootAttribute8);
		SushiEventType eventType3 = new SushiEventType("aggregatedEvent", attributes3);
		Broker.send(eventType3);

		assertTrue(SushiEsper.getInstance().getEventTypeInfo(eventType1, "TimeStamp_End") == Date.class);
		assertTrue(SushiEsper.getInstance().getEventTypeInfo(eventType1, "Timestamp") == Date.class);
		
//		SushiQuery timestamps = new SushiQuery("aggregate", "SELECT B.Timestamp " +
//				"FROM Pattern[ every A=truckUsage -> every B=obuEvent( " +
//				"A.Driver = B.Driver AND " +
//				"A.Timestamp.getTime() < B.Timestamp.getTime() AND " +
//				"A.TimeStampEnd.getTime() > B.Timestamp.getTime())]", QueryTypeEnum.LIVE);

		SushiAggregationRule rule = new SushiAggregationRule(eventType3, "TruckRule", "SELECT B.Timestamp as Timestamp, B.Driver as Driver, "+
				"B.Location as Location, A.Truck as Truck " +
				"FROM Pattern[ every A=truckUsage -> every B=obuEvent( " +
				"A.Driver = B.Driver AND " +
				"B.Timestamp.getTime() IN [A.Timestamp.getTime():A.TimeStamp_End.getTime()])]"
			);
		SushiAggregationListener listener = SushiAggregation.getInstance().addToEsper(rule);

		loadData(eventType1, truckUsageFile);
		loadData(eventType2, obuEventsFile);		

		SushiQuery query = new SushiQuery("timestamp", "select Timestamp.format() from aggregatedEventWindow", SushiQueryTypeEnum.ONDEMAND);
		String result = query.execute(SushiEsper.getInstance());
		System.out.println(result);
		
//		System.out.println(listenerX.getLog());
//		assertTrue(listenerX.getNumberOfLogEntries() > 0);		
		assertTrue("Number of aggregated events in the event processing engine should be 375, but was " + listener.getNumberOfEventsFired(), listener.getNumberOfEventsFired() == 375);
		assertTrue("Number of aggregated events in the database should be 375, but was " + SushiEvent.findByEventType(eventType3).size(), SushiEvent.findByEventType(eventType3).size() == 375);		
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
	
	@Test
	public void getUsefulTimestampFormat() {
		SushiAttribute rootAttribute4 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute5 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		
		List<SushiAttribute> attributes2 = Arrays.asList(rootAttribute4, rootAttribute5);
		SushiEventType eventType2 = new SushiEventType("obuEvent", attributes2, "Timestamp");
		Broker.send(eventType2);
		loadData(eventType2, obuEventsFile);
		SushiQuery query = new SushiQuery("timestamp", "select formatDate(Timestamp, 'yyyy-MM-dd HH:mm:ss') from obuEventWindow", SushiQueryTypeEnum.ONDEMAND);
		String result = query.execute(SushiEsper.getInstance());
		System.out.println(result);
	}
	
	@Test
	public void getAggregated() {
		SushiAttribute rootAttribute4 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute5 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		
		List<SushiAttribute> attributes2 = Arrays.asList(rootAttribute4, rootAttribute5);
		SushiEventType eventType2 = new SushiEventType("obuEvent", attributes2, "Timestamp");
		Broker.send(eventType2);
		loadData(eventType2, obuEventsFile);
		SushiQuery queryRome = new SushiQuery("rome", "select count(*) from obuEventWindow where Driver = 'Paul' AND Location = 'Rome'", SushiQueryTypeEnum.ONDEMAND);
	    String result = queryRome.execute(SushiEsper.getInstance());
	    String number = (String) result.subSequence(result.indexOf("=")+1, result.indexOf("}"));
	    System.out.println(number);

	}
	
}