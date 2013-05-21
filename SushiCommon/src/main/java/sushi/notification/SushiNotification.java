package sushi.notification;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sushi.event.SushiEvent;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

@Entity
@Table(name = "SushiNotification")
public class SushiNotification extends Persistable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	@Column(name="seen",columnDefinition="INT(1)")
	private boolean seen = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date timestamp = null;
	
	@ManyToOne
	private SushiUser user;

	@ManyToOne
	private SushiNotificationRule notificationRule;
	
	@ManyToOne
	private SushiEvent event;
		
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

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
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

	public SushiEvent getEvent() {
		return event;
	}

	public void setEvent(SushiEvent event) {
		this.event = event;
	}

	/*
	 *Default Constructor for JPA 
	 */
	@SuppressWarnings("unused")
	private SushiNotification() {
	}
	
	public SushiNotification(SushiEvent event, SushiUser user, SushiNotificationRule rule) {
		this.timestamp = new Date();
		this.event = event;
		this.user = user;
		this.notificationRule = rule;
	}
	
	public String toString() {
		if (! notificationRule.hasCondition()) { 
			return event.shortenedString() + " was received on " + timestamp;
		} else {
			return event.shortenedString() + " with " + notificationRule.getCondition().getConditionString() + " was received on " + timestamp;
		}
				
	}
	
	public void setSeen() {
		seen = true;
		this.merge();
	}

	
	//JPA-Methods
	
	/**
	 * Finds all users in the database.
	 * @return
	 */
	public static List<SushiNotification> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiNotification t");
		return q.getResultList();
	}
	
	public static List<SushiNotification> findForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "'", SushiNotification.class);
		return query.getResultList();
	}

	public static List<SushiNotification> findUnseenForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "' AND seen = 0", SushiNotification.class);
		return query.getResultList();
	}
	
	public static List<SushiNotification> findForNotificationRule(SushiNotificationRule rule) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE NOTIFICATIONRULE_ID = '" + rule.getID() + "'", SushiNotification.class);
		return query.getResultList();
	}
	
	public static List<SushiNotification> findForEvent(SushiEvent event) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE EVENT_ID = '" + event.getID() + "'", SushiNotification.class);
		return query.getResultList();
	}

	
	/**
	 * Deletes all users from the database.
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
