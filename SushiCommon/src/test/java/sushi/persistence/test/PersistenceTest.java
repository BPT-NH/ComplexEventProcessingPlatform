package sushi.persistence.test;

import sushi.persistence.Persistable;

/**
 * This class is an interface for the testing of persistable classes
 * It comprises the saving, finding and removing of {@link Persistable}s.
 * @author micha
 */
public interface PersistenceTest {
	
	public void testStoreAndRetrieve();
	
	public void testFind();
	
	public void testRemove();

}
