package sushi.notification;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.eventhandling.NotificationObservable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

public class NotificationEventTest {
	
	@Before
	public void setup() {
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void notificationTest(){
		NotificationObservable.getInstance().clearInstance();
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRuleForEvent.removeAll();
		SushiNotificationRuleForEvent rule = new SushiNotificationRuleForEvent(eventType, user, SushiNotificationPriorityEnum.LOW);
		Broker.send(rule);
		assertTrue(SushiNotificationRuleForEvent.findAll().size() == 1);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		List<SushiNotificationForEvent> listOfNotifications = SushiNotificationForEvent.findUnseenEventNotificationForUser(user);
		assertTrue(listOfNotifications.size() == 1);
		SushiNotificationForEvent notification = listOfNotifications.get(0);
		notification.setSeen();
		assertTrue(SushiNotificationForEvent.findUnseenForUser(user).size() == 0);
		
	}

	@Test
	public void notificationWithConditionTestInteger(){
		NotificationObservable.getInstance().clearInstance();
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.INTEGER);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType1", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRuleForEvent rule = new SushiNotificationRuleForEvent(eventType, new SushiCondition("TestAttribute", "1"), user, SushiNotificationPriorityEnum.LOW);
		Broker.send(rule);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), 1);
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		assertTrue("should be 1, but was " + SushiNotificationForEvent.findUnseenEventNotificationForUser(user).size(), SushiNotificationForEvent.findUnseenEventNotificationForUser(user).size() == 1);
		
	}

	@Test
	public void notificationWithConditionTest(){
		NotificationObservable.getInstance().clearInstance();
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType2", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRuleForEvent rule = new SushiNotificationRuleForEvent(eventType, new SushiCondition("TestAttribute", "Wert"), user, SushiNotificationPriorityEnum.LOW);
		Broker.send(rule);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		assertTrue(SushiNotificationForEvent.findUnseenForUser(user).size() == 1);
		
	}

	
}
