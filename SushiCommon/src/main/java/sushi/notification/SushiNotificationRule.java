package sushi.notification;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * This class is the super class for notification rules.
 * A notification rule saves a situation for that a user wants to be notified.
 */
@Entity
@Table(name = "SushiNotificationRule")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Disc")
public abstract class SushiNotificationRule extends Persistable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;
	
	@ManyToOne
	protected SushiUser user;
	
	@Column(name = "priority")
	@Enumerated(EnumType.STRING)
	protected SushiNotificationPriorityEnum priority;
	
	//Getter and Setter
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public SushiUser getUser() {
		return user;
	}

	public void setUser(SushiUser user) {
		this.user = user;
	}

	public abstract Persistable getTriggeringEntity();
	
	//JPA-Methods
	
	/**
	 * Deletes all notification rules from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiNotificationRule");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Finds all notification rules from the database.
	 * @return all notification rules
	 */
	public static List<SushiNotificationRule> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiNotificationRule t");
		return q.getResultList();
	}
	
	/**
	 * Find all notification rules for a user from the database.
	 * @param user
	 * @return all notification rules for a user
	 */
	public static List<SushiNotificationRule> findByUser(SushiUser user){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE USER_ID = '" + user.getID() + "'", SushiNotificationRule.class);
		return q.getResultList();
	}
	
	/**
	 * Deletes this notification rule from the database.
	 * All connected notifications are deleted as well.
	 * @return 
	 */
	@Override
	public Persistable remove() {
		List<SushiNotification> notifications = SushiNotification.findForNotificationRule(this);
		for (SushiNotification notification : notifications) notification.remove();
		return (SushiNotificationRule) super.remove();
	}

}