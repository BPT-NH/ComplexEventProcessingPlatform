package sushi.esper.examples;

import java.util.Arrays;
import java.util.List;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.notification.SushiCondition;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
import sushi.transformation.TransformationManager;
import sushi.transformation.TransformationRule;
import sushi.user.SushiUser;

/**
 * This class will show the transformation of events.
 * It uploads events with estimated arrival times of ships and actual arrival times of ships.
 * It then creates events by transformation that encapsulate the time difference.
 * There can also be notifications created.
 */
public class TimeDiffOfContainers {

	ExcelImporter importer = new ExcelImporter();
	TransformationManager transformation;
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
		showCase.createTransformationRule();
//		showCase.createNotifications();
		showCase.loadEvents();
		showCase.showTransformedEvents();
	}
	
	public void doCase() {
		TimeDiffOfContainers showCase = new TimeDiffOfContainers();
		showCase.prepareEventtypes();
		showCase.createTransformationRule();
		showCase.loadEvents();
		showCase.showTransformedEvents();
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
	
	private void createNotifications() {
		SushiUser user = new SushiUser("susanne", "1234", "susanne.90@gmx.de");
		user.save();
		
		SushiQuery query = new SushiQuery("CascadingDelay", "Select A.Containernummer, A.DelayInMinutes From Pattern [ every (A=DelayEvent(DelayInMinutes > 200))]", SushiQueryTypeEnum.LIVE);
		query.save();
		query.addToEsper();
		
		SushiNotificationRule notific2 = new SushiNotificationRuleForEvent(eventTypeTooLate, new SushiCondition("Containernummer", "CLHU4801571") ,user, SushiNotificationPriorityEnum.LOW);
		Broker.send(notific2);
		
		SushiNotificationRule notific1 = new SushiNotificationRuleForQuery(query, user, SushiNotificationPriorityEnum.LOW);
		Broker.send(notific1);
	}
	
	private void createTransformationRule() {
		transformation = new TransformationManager();
		//create EventType for MuchTooLate		
		SushiAttribute rootAttribute1 = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute2 = new SushiAttribute("TimeDifference", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute3 = new SushiAttribute("ATA", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute4 = new SushiAttribute("ETA", SushiAttributeTypeEnum.DATE);
		
		List<SushiAttribute> attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3, rootAttribute4);
		eventTypeTooLate = new SushiEventType("TooLateEvent", attributes);
		eventTypeTooLate = Broker.send(eventTypeTooLate);
		TransformationRule transformationRule = new TransformationRule(eventTypeTooLate, "TooLateRule", 
				"Select A.Containernummer as Containernummer, ((B.Timestamp.getTime() - A.Timestamp.getTime()) / "+ OneMinuteMilliSecs +") as TimeDifference, B.Timestamp as ATA, A.Timestamp as ETA, B.Timestamp as Timestamp " +
				"From Pattern [ every A=ScheduledArrivalEvent -> (every  " +
						 "B=ActualArrivalEvent(A.Containernummer = Containernummer ))]");
		transformation.register(transformationRule);
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
	
	private void showTransformedEvents() {
		//show created events
		System.out.println();
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate).size() + " events added to EventType " + eventTypeTooLate.getTypeName());
		System.out.println(SushiEvent.findByEventType(eventTypeTooLate));
	}


}
