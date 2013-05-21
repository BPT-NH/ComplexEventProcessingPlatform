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

public class NotificationTest {
	
	@Before
	public void setup() {
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void notificationTest(){
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRule.removeAll();
		SushiNotificationRule rule = new SushiNotificationRule(eventType, user, SushiNotificationPriorityEnum.LOW);
		rule.save();
		assertTrue(SushiNotificationRule.findAll().size() == 1);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		List<SushiNotification> listOfNotifications = SushiNotification.findUnseenForUser(user);
		assertTrue(listOfNotifications.size() == 1);
		SushiNotification notification = listOfNotifications.get(0);
		notification.setSeen();
		assertTrue(SushiNotification.findUnseenForUser(user).size() == 0);
		
	}

	@Test
	public void notificationWithConditionTestInteger(){
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.INTEGER);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRule rule = new SushiNotificationRule(eventType, new SushiCondition("TestAttribute", "1"), user, SushiNotificationPriorityEnum.LOW);
		rule.save();
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), 1);
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		assertTrue("should be 1, but was " + SushiNotification.findUnseenForUser(user).size(), SushiNotification.findUnseenForUser(user).size() == 1);
		
	}

	@Test
	public void notificationWithConditionTest(){
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();
		
		SushiNotificationRule rule = new SushiNotificationRule(eventType, new SushiCondition("TestAttribute", "Wert"), user, SushiNotificationPriorityEnum.LOW);
		rule.save();
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		assertTrue(SushiNotification.findUnseenForUser(user).size() == 1);
		
	}

	
}
