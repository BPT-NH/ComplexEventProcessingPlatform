package sushi.aggregation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;

public class SushiAggregationListener implements UpdateListener {
	private SushiEsper esper;
	private SushiEventType eventType;
	private int numberOfEventsFired;
	
	
	public SushiAggregationListener(SushiEsper sushiEsper, SushiEventType sushiEventType) {
		esper = sushiEsper;
		eventType = sushiEventType;
		numberOfEventsFired = 0;
	}

	public void update(EventBean[] newData, EventBean[] oldData) {
		
		Set<Map<String, Serializable>> uniqueEvents = new HashSet<Map<String, Serializable>>();
		
		// create event/attribute values for event type
		for (EventBean data : newData) {
			System.out.println("Event received: " + data.getUnderlying());
			Map<String, Serializable> attributes = (Map<String, Serializable>) data.getUnderlying();
			if (!uniqueEvents.contains(attributes)) {
				uniqueEvents.add(attributes);
				SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
				
				for (SushiAttribute attribute : eventType.getRootLevelValueTypes()) {
					try {
						String attributeName = attribute.getAttributeExpression();
						Serializable attributeValue = null;
						if (attribute.getType() == SushiAttributeTypeEnum.DATE) {
							attributeValue = (Date) attributes.get(attributeName);
						} else if (attribute.getType() == SushiAttributeTypeEnum.INTEGER) {
							if (attributes.get(attributeName) instanceof Long) {
								attributeValue = ((Long) attributes.get(attributeName)).intValue();
							} else if (attributes.get(attributeName) instanceof Double) {
								attributeValue = ((Double) attributes.get(attributeName)).intValue();
							} else {
								attributeValue = (Integer) attributes.get(attributeName);
							}
						} else {
							attributeValue = attributes.get(attributeName).toString();
						}
						values.put(attributeName, attributeValue); 
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				System.out.println(values.toString());
				SushiEvent event = new SushiEvent(eventType, new Date(), values);
				
				// timestamp
				Object timestamp = attributes.get("Timestamp");
				if (timestamp instanceof Date) {
					event.setTimestamp((Date) timestamp);
				}

				// add created event to Esper and database
				Broker.send(event);
				System.out.println("Event created: " + event);
				numberOfEventsFired++;
			}
		}
	}

	public int getNumberOfEventsFired() {
		return numberOfEventsFired;
	}

	public void setNumberOfEventsFired(int numberOfEventsFired) {
		this.numberOfEventsFired = numberOfEventsFired;
	}

	
}
