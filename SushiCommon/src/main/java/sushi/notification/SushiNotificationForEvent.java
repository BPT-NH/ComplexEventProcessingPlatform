package sushi.notification;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
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

/**
 * This class is a certain @see SushiNotification.
 * This kind of notification is created from a @see SushiNotificationRuleForEvent informing a user about the occurence of an event.
 */
@Entity
@DiscriminatorValue("E")
public class SushiNotificationForEvent extends SushiNotification {

	@ManyToOne
	protected SushiEvent event;		
	
	/**
	 *Default Constructor for JPA 
	 */
	@SuppressWarnings("unused")
	private SushiNotificationForEvent() {
	}
	
	/**
	 * Creates an event notification.
	 * @param event
	 * @param user
	 * @param rule
	 */
	public SushiNotificationForEvent(SushiEvent event, SushiUser user, SushiNotificationRuleForEvent rule) {
		this.timestamp = new Date();
		this.event = event;
		this.user = user;
		this.notificationRule = rule;
	}
	
	//Getter and Setter
	
	public SushiEvent getEvent() {
		return event;
	}
	
	public void setEvent(SushiEvent event) {
		this.event = event;
	}

	
	public String getTriggeringText() {
		return event.shortenedString();
	}
	
	/**
	 * creates a string representation of the event notification
	 */
	public String toString() {
		//cast notification rule to SushiNotificationRuleForEventType
		SushiNotificationRuleForEvent notificationEventType = (SushiNotificationRuleForEvent) notificationRule;
		if (! notificationEventType.hasCondition()) { 
			return event.shortenedString() + " was received on " + timestamp;
		} else {
			return event.shortenedString() + " with " + notificationEventType.getCondition().getConditionString() + " was received on " + timestamp;
		}			
	}
	
	//JPA-Methods
	
	/**
	 * Finds all notifications for an event.
	 * @param event
	 * @return all notifications for an event
	 */
	public static List<SushiNotificationForEvent> findForEvent(SushiEvent event) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE EVENT_ID = '" + event.getID() + "'", SushiNotificationForEvent.class);
		return query.getResultList();
	}

	/**
	 * Finds all event notifications.
	 * @return all event notifications
	 */
	public static List<SushiNotificationForEvent> findAllEventNotifications() {
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE Disc = 'E'", SushiNotificationForEvent.class);
		return q.getResultList();
	}
	
	/**
	 * Finds unseen event notifications for a user
	 * @param user
	 * @return unseen event notifications for user
	 */
	public static List<SushiNotificationForEvent> findUnseenEventNotificationForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "' AND seen = 0 AND Disc = 'E'", SushiNotificationForEvent.class);
		return query.getResultList();
	}
	
}
