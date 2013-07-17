package sushi.persistence;

import java.io.Serializable;

/**
 * This is the parent class for all class, which are saved via JPA.
 */
public abstract class Persistable implements Serializable {
		
	private static final long serialVersionUID = 1L;

	/**
	 * Saves the current object to the database.
	 * @return
	 */
	public Persistable save(){
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistor.getEntityManager().persist(this);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Merges the state of the current object to the database.
	 * @return
	 */
	public Persistable merge(){
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistor.getEntityManager().merge(this);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Removes the current object from the database.
	 * @return
	 */
	public Persistable remove(){
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistable toBeRemoved = Persistor.getEntityManager().merge(this);
			Persistor.getEntityManager().remove(toBeRemoved);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Refreshes the state of the current object from the database.
	 * Overwrites changes made to the object, if any. 
	 * @return
	 */
	public Persistable refresh(){
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistor.getEntityManager().refresh(this);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the database ID for the object.
	 * @return
	 */
	public abstract int getID();
	
	/**
	 * Waits, until other transactions are finished.
	 */
	public void waitForTransaction() {
		while(Persistor.getEntityManager().getTransaction().isActive());
	}

}
