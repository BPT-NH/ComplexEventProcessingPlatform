package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

public class NotificationPersistenceTest implements PersistenceTest{

	private SushiEventType type1;
	private SushiEventType type2;
	private SushiNotificationRule rule2;
	private String michaMail = "micha@mail.de";
	private SushiUser user1;
	private SushiUser user2;
	private SushiNotificationRule rule1;
	private SushiEvent event1;
	private SushiEvent event2;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}

	@Test
	@Override
	public void testStoreAndRetrieve() {
		storeExampleNotificationRules();
		storeExampleNotifications();
		assertTrue("Value should be 2, but was " + SushiNotification.findAll().size(), SushiNotification.findAll().size()==2);
		SushiNotification.removeAll();
		assertTrue("Value should be 0, but was " + SushiNotification.findAll().size(), SushiNotification.findAll().size()==0);
		
	}

	@Test
	@Override
	public void testFind() {
		storeExampleNotificationRules();
		storeExampleNotifications();
		assertTrue(SushiNotification.findAll().size() == 2);
		
		assertTrue(SushiNotification.findUnseenForUser(user1).size() == 1);
		SushiNotification notification1 = SushiNotification.findUnseenForUser(user1).get(0); 
		assertTrue(notification1.getUser().getMail().equals(michaMail));
		notification1.setSeen();
		assertTrue(SushiNotification.findUnseenForUser(user1).size() == 0);
		
		assertTrue(SushiNotification.findForNotificationRule(rule2).size() == 1);
		assertTrue(SushiNotification.findForNotificationRule(rule2).get(0).getEvent().getID() == event2.getID());
	}

	@Test
	@Override
	public void testRemove() {
		storeExampleNotificationRules();
		storeExampleNotifications();
		List<SushiNotification> notificitations;
		notificitations = SushiNotification.findAll();
		assertTrue(notificitations.size() == 2);

		SushiNotification deletedNotification = notificitations.get(0);
		deletedNotification.remove();

		notificitations = SushiNotification.findAll();
		assertTrue(notificitations.size() == 1);
		
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	@Test
	public void testRemoveRuleWithExistingNotifications() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotification> notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 2);
				
		SushiNotification deletedNotification = notifications.get(0);
		SushiNotificationRule deletedNotificationRule = deletedNotification.getNotificationRule();
		deletedNotificationRule.remove();

		List<SushiNotificationRule> notificationRules = SushiNotificationRule.findAll();
		assertTrue(notificationRules.size() == 1);
		assertTrue(notificationRules.get(0).getID() != deletedNotificationRule.getID());
		
		//notification should be deleted as well
		notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());	
	}
	
	@Test
	public void testRemoveUserWithExistingNotification() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotification> notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 2);
				
		SushiNotification deletedNotification = notifications.get(0);
		SushiUser deletedUser = deletedNotification.getUser();
		deletedUser.remove();

		List<SushiUser> users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getID() != deletedUser.getID());
		
		//notification should be deleted as well
		notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());
	}

	@Test
	public void testRemoveEventWithExistingNotification() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotification> notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 2);
				
		SushiNotification deletedNotification = notifications.get(0);
		SushiEvent deletedEvent = deletedNotification.getEvent();
		deletedEvent.remove();

		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 1);
		assertTrue(events.get(0).getID() != deletedEvent.getID());
		
		//notification should be deleted as well
		notifications = SushiNotification.findAll();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());
		
	}
	
	private void storeExampleNotificationRules() {
		user1 = new SushiUser("Micha", "Micha1234", michaMail );
		user1.save();
		type1 = new SushiEventType("ToNotify");
		type1.save();
		rule1 = new SushiNotificationRule(type1, user1, SushiNotificationPriorityEnum.LOW);
		rule1.save();
		
		user2 = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		user2.save();
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		rule2 = new SushiNotificationRule(type2, user2, SushiNotificationPriorityEnum.LOW);
		rule2.save();
	}
	
	private void storeExampleNotifications() {
		event1 = new SushiEvent(type1, new Date());
		event1.save();
		SushiNotification notification1 = new SushiNotification(event1, user1, rule1);
		notification1.save();
		
		event2 = new SushiEvent(type2, new Date()); 
		event2.save();
		SushiNotification notification2 = new SushiNotification(event2, user2, rule2);
		notification2.save();
	}


}
