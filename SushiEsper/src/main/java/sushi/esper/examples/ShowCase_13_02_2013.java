package sushi.esper.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationListener;
import sushi.aggregation.SushiAggregationRule;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.excel.importer.ExcelNormalizer;
import sushi.excel.importer.FileNormalizer;

public class ShowCase_13_02_2013 {

	SushiEsper esper = SushiEsper.getInstance();
	ExcelNormalizer excelImporter = new ExcelNormalizer();
	SushiAggregation aggregation;
	private SushiEventType eventTypeEstimatedTime;
	private SushiEventType eventTypeActualTime;
	private SushiEventType eventTypeTooLate;
	private String filepath = "src/main/resources/Import containers 2011_excerpt.xls";
	private List<SushiAttribute> attributes1;
	private List<SushiAttribute> attributes2;
	
	
	public static void main(String[] args) {
		ShowCase_13_02_2013 showCase = new ShowCase_13_02_2013();
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
		eventTypeEstimatedTime = new SushiEventType("ScheduledArrivalEvent", attributes1, "ETA seavessel");
		esper.addEventType(eventTypeEstimatedTime);
		
		//actual time of arrival eventType
		eventTypeActualTime = new SushiEventType("ActualArrivalEvent", attributes2, "ATA seavessel"); 
		esper.addEventType(eventTypeActualTime);
	}
	
	private void createAggregationRule() {
		int TIMEDIFF = 24*60*60*1000;
		System.out.println("Searching for Timedifference greater than: " + TIMEDIFF);
		aggregation = new SushiAggregation();
		
		//create EventType for MuchTooLate		
		SushiAttribute rootAttribute1 = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute2 = new SushiAttribute("TimeDifference", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute3 = new SushiAttribute("ATA", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute4 = new SushiAttribute("ETA", SushiAttributeTypeEnum.DATE);
		
		List<SushiAttribute> attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3, rootAttribute4);
		eventTypeTooLate = new SushiEventType("TooLateEvent", attributes);
		esper.addEventType(eventTypeTooLate);
		SushiAggregationRule aggregationRule = new SushiAggregationRule(eventTypeTooLate, "TooLateRule", 
				"Select A.Containernummer as Containernummer, (B.Timestamp.getTime() - A.Timestamp.getTime()) as TimeDifference, B.Timestamp as ATA, A.Timestamp as ETA " +
				"From Pattern [ every A=ScheduledArrivalEvent -> (every  " +
						 "B=ActualArrivalEvent(A.Containernummer = Containernummer " +
				"and (Timestamp.getTime() - A.Timestamp.getTime()) > " + TIMEDIFF + "))]");
		aggregation.addToEsper(aggregationRule);
	}
	
	public void loadEvents() {	
		//load estimated Time events
		List<SushiEvent> eventsEstimated = excelImporter.importEventsFromFile(filepath, attributes1, "ETA seavessel");
		SushiEvent.setEventType(eventsEstimated, eventTypeEstimatedTime);
		esper.addEvents(eventsEstimated);
		
		//load actual time of arrival events
		List<SushiEvent> eventsActual = excelImporter.importEventsFromFile(filepath, attributes2, "ATA seavessel");
		SushiEvent.setEventType(eventsEstimated, eventTypeEstimatedTime);
		esper.addEvents(eventsActual);
	}
	
	private void showAggregatedEvents() {
		//show created events
		System.out.println();
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate).size() + " events added to EventType " + eventTypeTooLate.getTypeName());
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate));
	}


}
