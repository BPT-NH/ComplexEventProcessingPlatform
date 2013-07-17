package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * This class tests the saving, finding and removing of {@link SushiNotificationRule}.
 */
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
		assertTrue("Value should be 2, but was " + SushiNotificationRuleForEvent.findAll().size(), SushiNotificationRuleForEvent.findAll().size()==2);
		SushiNotificationRuleForEvent.removeAll();
		assertTrue("Value should be 0, but was " + SushiNotificationRuleForEvent.findAll().size(), SushiNotificationRuleForEvent.findAll().size()==0);
	}

	@Test
	@Override
	public void testFind() {
		storeExampleNotificationRules();
		assertTrue(SushiNotificationRuleForEvent.findAll().size() == 2);
		
		assertTrue(SushiNotificationRuleForEvent.findByEventType(type1).size() == 1);
		assertTrue(SushiNotificationRuleForEvent.findByEventType(type1).get(0).getUser().getMail().equals(michaMail));
		
		assertTrue(SushiNotificationRuleForEvent.findByUser(user1).size() == 1);
		assertTrue(SushiNotificationRuleForEvent.findByUser(user1).get(0).getTriggeringEntity() instanceof SushiEventType);
		assertTrue(SushiNotificationRuleForEvent.findByUser(user1).get(0).getTriggeringEntity().getID() == type1.getID());

	}

	@Test
	@Override
	public void testRemove() {
		storeExampleNotificationRules();
		List<SushiNotificationRuleForEvent> notificitations;
		notificitations = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRuleForEvent deletedNotification = notificitations.get(0);
		deletedNotification.remove();

		notificitations = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		assertTrue(notificitations.size() == 1);
		
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	@Test
	public void testRemoveEventTypeWithNotificationRule() {
		storeExampleNotificationRules();
		List<SushiNotificationRuleForEvent> notificitations = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRuleForEvent deletedNotification = notificitations.get(0);
		SushiUser user = deletedNotification.getUser();
		user.remove();

		List<SushiUser> users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getID() != user.getID());
		
		//notification was deleted as well
		notificitations = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		assertTrue(notificitations.size() == 1);
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	
	private void storeExampleNotificationRules() {
		user1 = new SushiUser("Micha", "Micha1234", michaMail );
		user1.save();
		type1 = new SushiEventType("ToNotify");
		type1.save();
		SushiNotificationRuleForEvent notification1 = new SushiNotificationRuleForEvent(type1, user1, SushiNotificationPriorityEnum.LOW);
		notification1.save();
		
		user2 = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		user2.save();
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		SushiNotificationRuleForEvent notification2 = new SushiNotificationRuleForEvent(type2, user2, SushiNotificationPriorityEnum.LOW);
		notification2.save();

		
	}

}
