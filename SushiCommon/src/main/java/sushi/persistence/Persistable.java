package sushi.persistence;

import java.io.Serializable;


/**
 * @author micha
 *
 */
public abstract class Persistable implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
	 * Waits, until other transactions are finished.
	 */
	public void waitForTransaction() {
		while(Persistor.getEntityManager().getTransaction().isActive());
	}

}
