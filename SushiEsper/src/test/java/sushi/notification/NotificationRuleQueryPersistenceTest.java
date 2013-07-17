package sushi.notification;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.persistence.Persistor;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
import sushi.user.SushiUser;

public class NotificationRuleQueryPersistenceTest{

	private SushiEventType type1;
	private SushiUser user1;
	private SushiUser user2;
	private SushiEventType type2;
	private String michaMail = "micha@mail.de";
	private SushiQuery query1;
	private SushiQuery query2;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void testStoreAndRetrieve() {
		storeExampleNotificationRules();
		assertTrue("Value should be 2, but was " + SushiNotificationRuleForQuery.findAll().size(), SushiNotificationRuleForQuery.findAll().size()==2);
		SushiNotificationRuleForEvent.removeAll();
		assertTrue("Value should be 0, but was " + SushiNotificationRuleForQuery.findAll().size(), SushiNotificationRuleForQuery.findAll().size()==0);
	}

	@Test
	public void testFind() {
		storeExampleNotificationRules();
		assertTrue(SushiNotificationRuleForQuery.findAll().size() == 2);
		
		assertTrue(SushiNotificationRuleForQuery.findByQuery(query1).size() == 1);
		assertTrue(SushiNotificationRuleForQuery.findByQuery(query1).get(0).getUser().getMail().equals(michaMail));
		
		assertTrue(SushiNotificationRuleForQuery.findByUser(user1).size() == 1);
		assertTrue(SushiNotificationRuleForQuery.findByUser(user1).get(0).getTriggeringEntity() instanceof SushiQuery);
		assertTrue(SushiNotificationRuleForQuery.findByUser(user1).get(0).getTriggeringEntity().getID() == query1.getID());

	}

	@Test
	public void testRemove() {
		storeExampleNotificationRules();
		List<SushiNotificationRuleForQuery> notificitations;
		notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRuleForQuery deletedNotification = notificitations.get(0);
		deletedNotification.remove();

		notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 1);
		
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}
	
	@Test
	public void testRemoveUserWithNotificationRule() {
		storeExampleNotificationRules();
		List<SushiNotificationRuleForQuery> notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRuleForQuery deletedNotification = notificitations.get(0);
		SushiUser user = deletedNotification.getUser();
		user.remove();

		List<SushiUser> users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getID() != user.getID());
		
		//notification was deleted as well
		notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 1);
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}

	@Test
	public void testRemoveQueryWithNotificationRule() {
		storeExampleNotificationRules();
		List<SushiNotificationRuleForQuery> notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 2);

		SushiNotificationRuleForQuery deletedNotification = notificitations.get(0);
		SushiQuery query = deletedNotification.getQuery();
		query.remove();

		List<SushiQuery> queries = SushiQuery.getAllLiveQueries();
		assertTrue(queries.size() == 1);
		assertTrue(queries.get(0).getID() != query.getID());
		
		//notification was deleted as well
		notificitations = SushiNotificationRuleForQuery.findAllQueryNotificationRules();
		assertTrue(notificitations.size() == 1);
		assertTrue(notificitations.get(0).getID() != deletedNotification.getID());
	}

	
	
	private void storeExampleNotificationRules() {
		user1 = new SushiUser("Micha", "Micha1234", michaMail );
		user1.save();
		type1 = new SushiEventType("ToNotify");
		type1.save();
		query1 = new SushiQuery("allToNotify1", "Select * from ToNotify", SushiQueryTypeEnum.LIVE);
		query1.save();
		SushiNotificationRuleForQuery notification1 = new SushiNotificationRuleForQuery(query1, user1, SushiNotificationPriorityEnum.LOW);
		notification1.save();
		
		user2 = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		user2.save();
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		query2 = new SushiQuery("allToNotify2", "Select * from ToNotify2", SushiQueryTypeEnum.LIVE);
		query2.save();
		SushiNotificationRuleForQuery notification2 = new SushiNotificationRuleForQuery(query2, user2, SushiNotificationPriorityEnum.LOW);
		notification2.save();

		
	}

}
