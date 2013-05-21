package sushi.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author micha
 *
 */
public class Persistor {
	private static String PERSISTENCE_UNIT_NAME = DatabaseEnvironments.DEVELOPMENT.getDatabaseName();
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	private static EntityManager entityManager = entityManagerFactory.createEntityManager();

	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public static void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		Persistor.entityManagerFactory = entityManagerFactory;
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static void setEntityManager(EntityManager entityManager) {
		Persistor.entityManager = entityManager;
	}

	public static String getPERSISTENCE_UNIT_NAME() {
		return PERSISTENCE_UNIT_NAME;
	}
	
	public static void useDevelopmentEnviroment(){
		setPERSISTENCE_UNIT_NAME(DatabaseEnvironments.DEVELOPMENT.getDatabaseName());
	}
	
	public static void useTestEnviroment(){
		setPERSISTENCE_UNIT_NAME(DatabaseEnvironments.TEST.getDatabaseName());
	}

	public static void setPERSISTENCE_UNIT_NAME(String persistenceUnitName) {
		entityManager.close();
		entityManagerFactory.close();
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
		
	}

}