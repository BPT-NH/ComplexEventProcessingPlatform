package sushi.notification;

import static org.junit.Assert.assertTrue;

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
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.persistence.Persistor;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
import sushi.user.SushiUser;

public class NotificationQueryTest {

	private SushiQuery query1;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void notificationTest() {
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		SushiUser user = new SushiUser("name", "1234", "email");
		user.save();

		query1 = new SushiQuery("NotifyTestType", "Select * from TestType", SushiQueryTypeEnum.LIVE);
		query1.save();
		query1.addToEsper();
		
		SushiNotificationRuleForQuery.removeAll();
		SushiNotificationRuleForQuery rule = new SushiNotificationRuleForQuery(query1, user, SushiNotificationPriorityEnum.LOW);
		rule.save();
		assertTrue(SushiNotificationRuleForEvent.findAll().size() == 1);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		Broker.send(event);
		
		List<SushiNotificationForQuery> listOfNotifications = SushiNotificationForQuery.findUnseenQueryNotificationForUser(user);
		assertTrue(listOfNotifications.size() == 1);
		SushiNotificationForQuery notification = listOfNotifications.get(0);
		notification.setSeen();
		assertTrue(SushiNotificationForQuery.findUnseenForUser(user).size() == 0);

	}

}
