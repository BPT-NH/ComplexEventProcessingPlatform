package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationForEvent;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

public class NotificationPersistenceTest implements PersistenceTest{

	private SushiEventType type1;
	private SushiEventType type2;
	private SushiNotificationRuleForEvent rule2;
	private String michaMail = "micha@mail.de";
	private SushiUser user1;
	private SushiUser user2;
	private SushiNotificationRuleForEvent rule1;
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
		assertTrue("Value should be 2, but was " + SushiNotificationForEvent.findAll().size(), SushiNotificationForEvent.findAll().size()==2);
		SushiNotificationForEvent.removeAll();
		assertTrue("Value should be 0, but was " + SushiNotificationForEvent.findAll().size(), SushiNotificationForEvent.findAll().size()==0);
		
	}

	@Test
	@Override
	public void testFind() {
		storeExampleNotificationRules();
		storeExampleNotifications();
		assertTrue(SushiNotificationForEvent.findAll().size() == 2);
		
		assertTrue(SushiNotificationForEvent.findUnseenForUser(user1).size() == 1);
		SushiNotificationForEvent notification1 = SushiNotificationForEvent.findUnseenEventNotificationForUser(user1).get(0); 
		assertTrue(notification1.getUser().getMail().equals(michaMail));
		notification1.setSeen();
		assertTrue(SushiNotificationForEvent.findUnseenForUser(user1).size() == 0);
		
		assertTrue(SushiNotificationForEvent.findForNotificationRule(rule2).size() == 1);
		assertTrue(((SushiNotificationForEvent) SushiNotificationForEvent.findForNotificationRule(rule2).get(0)).getEvent().getID() == event2.getID());
	}

	@Test
	@Override
	public void testRemove() {
		storeExampleNotificationRules();
		storeExampleNotifications();
		List<SushiNotificationForEvent> notificitations;
		notificitations = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notificitations.size() == 2);

		SushiNotificationForEvent deletedNotification = notificitations.get(0);
		deletedNotification.remove();

		notificitations = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notificitations.size() == 1);
		
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	@Test
	public void testRemoveRuleWithExistingNotifications() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotificationForEvent> notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 2);
				
		SushiNotificationForEvent deletedNotification = notifications.get(0);
		SushiNotificationRule deletedNotificationRule = deletedNotification.getNotificationRule();
		deletedNotificationRule.remove();

		List<SushiNotificationRuleForEvent> notificationRules = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		assertTrue(notificationRules.size() == 1);
		assertTrue(notificationRules.get(0).getID() != deletedNotificationRule.getID());
		
		//notification should be deleted as well
		notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());	
	}
	
	@Test
	public void testRemoveUserWithExistingNotification() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotificationForEvent> notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 2);
				
		SushiNotificationForEvent deletedNotification = notifications.get(0);
		SushiUser deletedUser = deletedNotification.getUser();
		deletedUser.remove();

		List<SushiUser> users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getID() != deletedUser.getID());
		
		//notification should be deleted as well
		notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());
	}

	@Test
	public void testRemoveEventWithExistingNotification() {
		storeExampleNotificationRules();
		storeExampleNotifications();

		List<SushiNotificationForEvent> notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 2);
				
		SushiNotificationForEvent deletedNotification = notifications.get(0);
		SushiEvent deletedEvent = deletedNotification.getEvent();
		deletedEvent.remove();

		List<SushiEvent> events = SushiEvent.findAll();
		assertTrue(events.size() == 1);
		assertTrue(events.get(0).getID() != deletedEvent.getID());
		
		//notification should be deleted as well
		notifications = SushiNotificationForEvent.findAllEventNotifications();
		assertTrue(notifications.size() == 1);
		assertTrue(notifications.get(0).getID() != deletedNotification.getID());
		
	}
	
	private void storeExampleNotificationRules() {
		user1 = new SushiUser("Micha", "Micha1234", michaMail );
		user1.save();
		type1 = new SushiEventType("ToNotify");
		type1.save();
		rule1 = new SushiNotificationRuleForEvent(type1, user1, SushiNotificationPriorityEnum.LOW);
		rule1.save();
		
		user2 = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		user2.save();
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		rule2 = new SushiNotificationRuleForEvent(type2, user2, SushiNotificationPriorityEnum.LOW);
		rule2.save();
	}
	
	private void storeExampleNotifications() {
		event1 = new SushiEvent(type1, new Date());
		event1.save();
		SushiNotificationForEvent notification1 = new SushiNotificationForEvent(event1, user1, rule1);
		notification1.save();
		
		event2 = new SushiEvent(type2, new Date()); 
		event2.save();
		SushiNotificationForEvent notification2 = new SushiNotificationForEvent(event2, user2, rule2);
		notification2.save();
	}


}
