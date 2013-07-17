package sushi.esper.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sushi.esper.SushiCEPListener;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.persistence.Persistor;
import sushi.util.SushiTestHelper;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.TimerControlEvent;

public class StatementTest {
	
	private static String filePathRating = System.getProperty("user.dir")+"/src/test/resources/Kino.xls";
	private static String filePathKino = System.getProperty("user.dir")+"/src/test/resources/Kino_Filme.xls";
	private ExcelImporter excelNormalizer = new ExcelImporter();
	private SushiStreamProcessingAdapter sushiEsper = SushiStreamProcessingAdapter.getInstance();
	
	private List<SushiEvent> createRatingEvents() {
        List<String> ratingColumnTitles = excelNormalizer.getColumnTitlesFromFile(filePathRating);
        List<SushiAttribute> ratingAttributes = SushiTestHelper.createAttributes(ratingColumnTitles);
        return excelNormalizer.importEventsFromFile(filePathRating, ratingAttributes);
	}
	
	private List<SushiEvent> createKinoEvents() {
		List<String> kinoColumnTitles = excelNormalizer.getColumnTitlesFromFile(filePathKino);
		List<SushiAttribute> kinoAttributes = SushiTestHelper.createAttributes(kinoColumnTitles);
		return excelNormalizer.importEventsFromFile(filePathKino, kinoAttributes);
	}
	private SushiEventType createCompositeEventType() {
		Set<String> columnTitles = new HashSet<String>();
		columnTitles.addAll(excelNormalizer.getColumnTitlesFromFile(filePathRating));
		columnTitles.addAll(excelNormalizer.getColumnTitlesFromFile(filePathKino));
		columnTitles.remove("Timestamp");
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(new ArrayList<String>(columnTitles));
		return new SushiEventType("Event", attributes, "Timestamp");
	}
	
	@Before
	public void setup() {
		Persistor.useTestEnviroment();
	}
	
	// TODO: Query und Listener anlegen und dann Events reinladen und Abfragen
	// Bewertung pro Film
	@Test
	public void testNormalQuery() {
//		Configuration cepConfig = new Configuration();
//		cepConfig.addEventType("Event", SushiEvent.class.getName());
		SushiEventType eventType = createCompositeEventType();
		Broker.send(eventType);
//		cepConfig.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
//		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
//		EPRuntime cepRT = cep.getEPRuntime();
		EPRuntime cepRT = sushiEsper.getEsperRuntime();
		
//		cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));

		EPAdministrator cepAdm = sushiEsper.getEsperAdministrator();
		
//		cepAdm.createEPL("CREATE WINDOW EventWindow.win:keepall() AS SELECT * FROM Event");
//		cepAdm.createEPL("INSERT INTO EventWindow SELECT * FROM Event");
		
		EPStatement transformationStatement = cepAdm.createEPL("" +
				"SELECT A.Timestamp.getTime(), B.Timestamp.getTime(), A.values('Location'), A.values('Movie') " +
				"FROM EventWindow AS A, EventWindow AS B " +
				"WHERE " +
					"B.values('Action')='Ende' AND " +
					"A.values('Action')='Start' AND " +
					"A.values('Location')=B.values('Location') AND " +
					"A.values('Movie')=B.values('Movie') AND " +
					"(A.Timestamp.getTime()).before(B.Timestamp.getTime(), 0 hours, 3 hours)");
		
		transformationStatement.addListener(new SushiCEPListener());
		
		// create events
		List<SushiEvent> ratingEvents = createRatingEvents();
		
        // send events
		for (SushiEvent event : ratingEvents) {
			cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
//			cepRT.sendEvent(new CurrentTimeEvent(event.getTimestamp().getTime()));
//			cepRT.sendEvent(event);
			event.setEventType(eventType);
			Broker.send(event);
		}
		
		// create events
		List<SushiEvent> kinoEvents = createKinoEvents();
		
        // send events
		for (SushiEvent event : kinoEvents) {
			cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
//			cepRT.sendEvent(new CurrentTimeEvent(event.getTimestamp().getTime()));
//			cepRT.sendEvent(event);
			event.setEventType(eventType);
			Broker.send(event);
		}
		
		cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_INTERNAL));
	}
	
	@Test
	public void testTimestamp() {
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("Event", SushiEvent.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
		EPRuntime cepRT = cep.getEPRuntime();
		EPAdministrator cepAdm = cep.getEPAdministrator();

		// create statement
		EPStatement timeStatement = cepAdm.createEPL("select count(*) from Event.win:time(1 hour)");
		timeStatement.addListener(new SushiCEPListener());
		
		// create events
		List<SushiEvent> ratingEvents = createRatingEvents();
		sortEventListByDate(ratingEvents);
		
		// pass events to Esper engine
				for (SushiEvent event : ratingEvents) {
					cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
					System.out.println(new CurrentTimeEvent(event.getTimestamp().getTime()).toString());
					cepRT.sendEvent(new CurrentTimeEvent(event.getTimestamp().getTime()));
					cepRT.sendEvent(event);
				}
	}
	
	@Test
	public void testContextQuery() {
		Configuration cepConfig = new Configuration();
		cepConfig.addEventType("Event", SushiEvent.class.getName());
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);
		EPRuntime cepRT = cep.getEPRuntime();
		
		EPAdministrator cepAdm = cep.getEPAdministrator();
		
		cepAdm.createEPL("" +
				"CREATE CONTEXT NestedContext " +
					"CONTEXT SegmentedByLocation PARTITION BY values('Location') FROM Event, " +
					"CONTEXT SegmentedByTime INITIATED BY Event(values('Action')='Ende') TERMINATED AFTER 1 hour, " +
					"CONTEXT SegmentedByRating PARTITION BY values('Rating') FROM Event");
		
		EPStatement transformationStatement = cepAdm.createEPL("" +
				"CONTEXT NestedContext " +
				"SELECT ID, values('Location'), values('Rating'), count(*) " +
				"FROM Event " +
				"GROUP BY values('Location'), values('Rating') " +
				"OUTPUT LAST EVERY 30 minute");
		
		transformationStatement.addListener(new SushiCEPListener());
		
		List<SushiEvent> events = new ArrayList<SushiEvent>();
		events.addAll(createRatingEvents());
		events.addAll(createKinoEvents());
		this.sortEventListByDate(events);
		
		for (SushiEvent event : events) {
			cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
			cepRT.sendEvent(new CurrentTimeEvent(event.getTimestamp().getTime()));
			cepRT.sendEvent(event);
		}
		
		cepRT.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_INTERNAL));
	}
	
	
	private void sortEventListByDate(List<SushiEvent> events){
		Collections.sort(events, new Comparator<SushiEvent>(){
			 
			@Override
			public int compare(SushiEvent event1, SushiEvent event2) {
				return event1.getTimestamp().compareTo(event2.getTimestamp());
			}
 
        });
	}

}
