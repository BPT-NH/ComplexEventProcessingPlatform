package sushi.notification;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.email.EmailUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;
import sushi.visualisation.SushiChartTypeEnum;

@Entity
@Table(name = "SushiNotificationRule")
public class SushiNotificationRule extends Persistable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	@ManyToOne
	private SushiEventType eventType;
	
	@ManyToOne
	private SushiUser user;
	
	@Column(name = "priority")
	@Enumerated(EnumType.STRING)
	private SushiNotificationPriorityEnum priority;
	
	@OneToOne(optional=true, cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	private SushiCondition condition;
		
	/**
	 * Default-Constructor for JPA.
	 */
	public SushiNotificationRule() {
		this.ID = 0;
		this.eventType = null;
		this.user = null;
	}
	
	public SushiNotificationRule(SushiEventType eventType, SushiCondition condition, SushiUser user, SushiNotificationPriorityEnum priority) {
		this.eventType = eventType;
		this.condition = condition;
		this.user = user;
		this.priority = priority;
	}

	public SushiNotificationRule(SushiEventType eventType, SushiUser user, SushiNotificationPriorityEnum priority) {
		this.eventType = eventType;
		this.condition = null;
		this.user = user;
		this.priority = priority;
	}

	
	public void trigger(SushiEvent event) {
		SushiNotification notification = new SushiNotification(event, user, this);
		notification.save();
		if (priority == SushiNotificationPriorityEnum.HIGH) {
			//send mail
			EmailUtils.sendBP2013Mail(user.getMail(), "Notification GET-Events", notification.toString());
		}
	}
		
	public boolean matches(SushiEvent event) {
		if (!hasCondition()) return true;
		return condition.matches(event);
	}
	
	public String toString() {
		String representation = "Notification for " + this.eventType;
		if (hasCondition()) {
			representation += " with " + condition.getConditionString();
		}
		representation += " for user " + user.getName();
		
		return representation;
	}
	
	public boolean hasCondition() {
		return (this.condition != null && condition.getConditionString() != "");
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SushiEventType getEventType() {
		return eventType;
	}

	public void setEventType(SushiEventType eventType) {
		this.eventType = eventType;
	}

	public SushiUser getUser() {
		return user;
	}

	public void setUser(SushiUser user) {
		this.user = user;
	}

	public SushiCondition getCondition() {
		return condition;
	}

	public void setCondition(SushiCondition condition) {
		this.condition = condition;
	}

	
	/**
	 * Deletes all users from the database.
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

	public static List<SushiNotificationRule> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiNotificationRule t");
		return q.getResultList();
	}
		
	public static SushiNotificationRule findByID(int ID){
		return Persistor.getEntityManager().find(SushiNotificationRule.class, ID);
	}
		
	public static List<SushiNotificationRule> findByEventType(SushiEventType eventType){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE EVENTTYPE_ID = '" + eventType.getID() + "'", SushiNotificationRule.class);
		return q.getResultList();
	}

	public static List<SushiNotificationRule> findByUser(SushiUser user){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE USER_ID = '" + user.getID() + "'", SushiNotificationRule.class);
		return q.getResultList();
	}
	
	/**
	 * Deletes this notification from the database.
	 * @return 
	 */
	@Override
	public Persistable remove() {
		List<SushiNotification> notifications = SushiNotification.findForNotificationRule(this);
		for (SushiNotification notification : notifications) notification.remove();
		return (SushiNotificationRule) super.remove();
	}
	
}
