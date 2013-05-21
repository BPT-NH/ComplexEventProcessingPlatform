package sushi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.process.SushiProcess;

public class SushiTestHelper {
	
	/**
	 * Creates attributes from the given attribute names which are the column titles of the excel files.
	 * @return list of SushiAttributes
	 */
	public static List<SushiAttribute> createAttributes(List<String> attributeNames) {
		List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
		for (String attributeName : attributeNames) {
			if (attributeName.startsWith("Time")) {
				attributes.add(new SushiAttribute(attributeName, SushiAttributeTypeEnum.DATE));
			} else if (attributeName.equals("Location") || attributeName.equals("Duration")) {
				attributes.add(new SushiAttribute(attributeName, SushiAttributeTypeEnum.INTEGER));
			} else {
				attributes.add(new SushiAttribute(attributeName, SushiAttributeTypeEnum.STRING));
			}
		}
		return attributes;
	}
	
	/**
	 * Creates two event types for testing.
	 * The event types have to be saved in the test at first!
	 * @return list of two SushiEventTypes
	 */
	public static List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		SushiAttributeTree values = new SushiAttributeTree();
		values.addRoot(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER));
		values.addRoot(new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING));
		
		eventTypes.add(new SushiEventType("Kino", values));
		eventTypes.add(new SushiEventType("GET-Transport"));
		return eventTypes;
	}
	
	/**
	 * Creates three events for the given EventType.
	 * The events have to be saved in the test at first! 
	 * @return list of three SushiEvents
	 */
	public static List<SushiEvent> createEvents(SushiEventType eventType) {
		List<SushiEvent> events = new ArrayList<SushiEvent>();
		for (int i = 1; i < 4; i++) {
			SushiMapTree<String, Serializable> mapTree = new SushiMapTree<String, Serializable>();
			SushiEvent event = new SushiEvent(eventType, new Date(), mapTree);
			for (SushiAttribute valueType : eventType.getValueTypes()) {
				String attributeName = valueType.getAttributeExpression();
				mapTree.put(attributeName, attributeName + i);
			}
			events.add(event);
		}
		return events;
	}
	
	/**
	 * Creates a new process.
	 * The process has to be saved in the test at first! 
	 * @return a SushiProcess
	 */
	public static SushiProcess createProcess(List<SushiEventType> eventTypes) {
		SushiProcess process = new SushiProcess("Kino", eventTypes);
		return process;
	}

}
