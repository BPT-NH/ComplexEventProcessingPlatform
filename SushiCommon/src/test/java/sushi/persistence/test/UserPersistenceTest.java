package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * @author micha
 *
 */
public class UserPersistenceTest implements PersistenceTest {

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve() {
		storeExampleUsers();
		assertTrue("Value should be 2, but was " + SushiUser.findAll().size(), SushiUser.findAll().size()==2);
		SushiUser.removeAll();
		assertTrue("Value should be 0, but was " + SushiUser.findAll().size(), SushiUser.findAll().size()==0);
	}

	@Test
	@Override
	public void testFind() {
		storeExampleUsers();
		assertTrue(SushiUser.findAll().size() == 2);
		assertTrue(SushiUser.findByName("Tsun").size() == 1);
		SushiUser tsun = SushiUser.findByName("Tsun").get(0);
		tsun.getMail().equals("tsun@mail.de");
		
		assertTrue(SushiUser.findByMail("micha@mail.de").size() == 1);
		SushiUser micha = SushiUser.findByMail("micha@mail.de").get(0);
		micha.getName().equals("Micha");
	}

	@Test
	@Override
	public void testRemove() {
		storeExampleUsers();
		List<SushiUser> users;
		users = SushiUser.findAll();
		assertTrue(users.size() == 2);

		SushiUser deletedUser = users.get(0);
		deletedUser.remove();

		users = SushiUser.findAll();
		assertTrue(users.size() == 1);
		
		assertTrue(users.get(0).getID() != deletedUser.getID());
	}
	
	private void storeExampleUsers() {
		SushiUser micha = new SushiUser("Micha", "Micha1234", "micha@mail.de");
		micha.save();
		SushiUser tsun = new SushiUser("Tsun", "Tsun1234", "tsun@mail.de");
		tsun.save();
	}

}
