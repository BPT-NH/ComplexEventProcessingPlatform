package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

public class NotificationRulePersistenceTest implements PersistenceTest {

	private SushiEventType type1;
	private SushiUser user1;
	private SushiUser user2;
	private SushiEventType type2;
	private String michaMail = "micha@mail.de";

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve() {
		storeExampleNotificationRules();
		assertTrue("Value should be 2, but was " + SushiNotificationRule.findAll().size(), SushiNotificationRule.findAll().size()==2);
		SushiNotificationRule.removeAll();
		assertTrue("Value should be 0, but was " + SushiNotificationRule.findAll().size(), SushiNotificationRule.findAll().size()==0);
	}

	@Test
	@Override
	public void testFind() {
		storeExampleNotificationRules();
		assertTrue(SushiNotificationRule.findAll().size() == 2);
		
		assertTrue(SushiNotificationRule.findByEventType(type1).size() == 1);
		assertTrue(SushiNotificationRule.findByEventType(type1).get(0).getUser().getMail().equals(michaMail));
		
		assertTrue(SushiNotificationRule.findByUser(user1).size() == 1);
		assertTrue(SushiNotificationRule.findByUser(user1).get(0).getEventType().getID() == type1.getID());

	}

	@Test
	@Override
	public void testRemove() {
		storeExampleNotificationRules();
		List<SushiNotificationRule> notificitations;
		notificitations = SushiNotificationRule.findAll();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRule deletedNotification = notificitations.get(0);
		deletedNotification.remove();

		notificitations = SushiNotificationRule.findAll();
		assertTrue(notificitations.size() == 1);
		
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	@Test
	public void testRemoveEventTypeWithNotificationRule() {
		storeExampleNotificationRules();
		List<SushiNotificationRule> notificitations = SushiNotificationRule.findAll();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRule deletedNotification = notificitations.get(0);
		SushiUser user = deletedNotification.getUser();
		user.remove();

		List<SushiUser> users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getID() != user.getID());
		
		//notification was deleted as well
		notificitations = SushiNotificationRule.findAll();
		assertTrue(notificitations.size() == 1);
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	
	private void storeExampleNotificationRules() {
		user1 = new SushiUser("Micha", "Micha1234", michaMail );
		user1.save();
		type1 = new SushiEventType("ToNotify");
		type1.save();
		SushiNotificationRule notification1 = new SushiNotificationRule(type1, user1, SushiNotificationPriorityEnum.LOW);
		notification1.save();
		
		user2 = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		user2.save();
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		SushiNotificationRule notification2 = new SushiNotificationRule(type2, user2, SushiNotificationPriorityEnum.LOW);
		notification2.save();

		
	}

}
