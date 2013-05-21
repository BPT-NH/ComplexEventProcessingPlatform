package sushi.esper.examples;

import java.util.Arrays;
import java.util.List;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationRule;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;

public class TimeDiffOfContainers {

	SushiEsper esper = SushiEsper.getInstance();
	ExcelNormalizer importer = new ExcelNormalizer();
	SushiAggregation aggregation;
	private SushiEventType eventTypeEstimatedTime;
	private SushiEventType eventTypeActualTime;
	private SushiEventType eventTypeTooLate;
	private List<SushiAttribute> attributes1;
	private List<SushiAttribute> attributes2;
	private String filePath = "src/main/resources/Import containers 2011_excerpt.xls";
	private int OneMinuteMilliSecs= 60 * 1000;
	
	
	public static void main(String[] args) {

		TimeDiffOfContainers showCase = new TimeDiffOfContainers();
		showCase.prepareEventtypes();
		showCase.createAggregationRule();
		showCase.loadEvents();
		showCase.showAggregatedEvents();
	}
	
	public void doCase() {
		TimeDiffOfContainers showCase = new TimeDiffOfContainers();
		showCase.prepareEventtypes();
		showCase.createAggregationRule();
		showCase.loadEvents();
		showCase.showAggregatedEvents();
	}
	
	private void prepareEventtypes() {
		
		System.out.println();
		System.out.println("=============================");
		System.out.println("=============================");
		System.out.println();
		
		SushiAttribute rootAttribute1 = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		attributes1 = Arrays.asList(rootAttribute1);
		
		SushiAttribute rootAttribute2  = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		attributes2 = Arrays.asList(rootAttribute2);
				
		//estimated time of arrival eventType
		eventTypeEstimatedTime = new SushiEventType("ScheduledArrivalEvent", attributes1, "ETA_seavessel");
		Broker.send(eventTypeEstimatedTime);
		
		//actual time of arrival eventType
		eventTypeActualTime = new SushiEventType("ActualArrivalEvent", attributes2, "ATA_seavessel"); 
		Broker.send(eventTypeActualTime);
	}
	
	private void createAggregationRule() {
		aggregation = new SushiAggregation();
		//create EventType for MuchTooLate		
		SushiAttribute rootAttribute1 = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute2 = new SushiAttribute("TimeDifference", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute3 = new SushiAttribute("ATA", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute4 = new SushiAttribute("ETA", SushiAttributeTypeEnum.DATE);
		
		List<SushiAttribute> attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3, rootAttribute4);
		eventTypeTooLate = new SushiEventType("TooLateEvent", attributes);
		eventTypeTooLate = Broker.send(eventTypeTooLate);
		SushiAggregationRule aggregationRule = new SushiAggregationRule(eventTypeTooLate, "TooLateRule", 
				"Select A.Containernummer as Containernummer, ((B.Timestamp.getTime() - A.Timestamp.getTime()) / "+ OneMinuteMilliSecs +") as TimeDifference, B.Timestamp as ATA, A.Timestamp as ETA, B.Timestamp as Timestamp " +
				"From Pattern [ every A=ScheduledArrivalEvent -> (every  " +
						 "B=ActualArrivalEvent(A.Containernummer = Containernummer ))]");
		aggregation.addToEsper(aggregationRule);
	}
	
	public void loadEvents() {	
		//load estimated Time events
		List<SushiEvent> eventsEstimated = importer.importEventsFromFile(filePath, attributes1, "ETA_seavessel");
		SushiEvent.setEventType(eventsEstimated, eventTypeEstimatedTime);
		Broker.send(eventsEstimated);
		
		//load actual time of arrival events
		List<SushiEvent> eventsActual = importer.importEventsFromFile(filePath, attributes2, "ATA_seavessel");
		SushiEvent.setEventType(eventsActual, eventTypeActualTime);
		Broker.send(eventsActual);
	}
	
	private void showAggregatedEvents() {
		//show created events
		System.out.println();
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate).size() + " events added to EventType " + eventTypeTooLate.getTypeName());
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate));
	}


}
