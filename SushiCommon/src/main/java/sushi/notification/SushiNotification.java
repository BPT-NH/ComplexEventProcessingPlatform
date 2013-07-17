package sushi.notification;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sushi.event.SushiEvent;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * This class is the super class for notifications.
 * A notification is created from a @see SushiNotificationRule informing a user about a situation.
 */
@Entity
@Table(name = "SushiNotification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Disc")
public abstract class SushiNotification extends Persistable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;

	@Column(name="seen",columnDefinition="INT(1)")
	protected boolean seen = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date timestamp = null;
	
	@ManyToOne
	protected SushiUser user;

	@ManyToOne
	protected SushiNotificationRule notificationRule;

	public boolean isSeen() {
		return seen;
	}
	
	//Getter and Setter
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SushiNotificationRule getNotificationRule() {
		return notificationRule;
	}

	public void setNotificationRule(SushiNotificationRule notificationRule) {
		this.notificationRule = notificationRule;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
		this.merge();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public SushiUser getUser() {
		return user;
	}

	public void setUser(SushiUser user) {
		this.user = user;
	}

	public void setSeen() {
		seen = true;
		this.merge();
	}

	public abstract String getTriggeringText();
	
	//JPA-Methods
	
	/**
	 * Finds all notifications in the database.
	 * @return all notifications
	 */
	public static List<SushiNotification> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiNotification t");
		return q.getResultList();
	}
	
	/**
	 * Finds all notifications for a user
	 * @param user
	 * @return all notifications for a user
	 */
	public static List<SushiNotification> findForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "'", SushiNotification.class);
		return query.getResultList();
	}

	/**
	 * Finds unseen notifications for a user
	 * @param user
	 * @return unseen notifications for a user
	 */
	public static List<SushiNotification> findUnseenForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "' AND seen = 0", SushiNotification.class);
		return query.getResultList();
	}
	
	/**
	 * Finds all notifications belonging to a notification rule
	 * @param notification rule
	 * @return all notifications for a notification rule
	 */
	public static List<SushiNotification> findForNotificationRule(SushiNotificationRule rule) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE NOTIFICATIONRULE_ID = '" + rule.getID() + "'", SushiNotification.class);
		return query.getResultList();
	}

	/**
	 * Deletes all notifications from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiNotification");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
