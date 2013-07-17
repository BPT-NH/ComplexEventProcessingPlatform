package sushi.notification;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import sushi.email.EmailUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;


/**
 * This class is a certain @see SushiNotificationRule.
 * An event notification rule saves a event type and a condition.
 * If an event from that event type occurs that matches the condition the user is informed.
 */
@Entity
@DiscriminatorValue("E")
public class SushiNotificationRuleForEvent extends SushiNotificationRule {
	
	@ManyToOne
	protected SushiEventType eventType;

	@OneToOne(optional=true, cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
	private SushiCondition condition;
		
	/**
	 * Default-Constructor for JPA.
	 */
	public SushiNotificationRuleForEvent() {
		this.ID = 0;
		this.eventType = null;
		this.user = null;
	}
	
	/**
	 * Creates an event notification rule with event type and condition.
	 * @param eventType
	 * @param condition
	 * @param user
	 * @param priority
	 */
	public SushiNotificationRuleForEvent(SushiEventType eventType, SushiCondition condition, SushiUser user, SushiNotificationPriorityEnum priority) {
		this.eventType = eventType;
		this.condition = condition;
		this.user = user;
		this.priority = priority;
	}
	
	/**
	 * Creates an event notification rule for an event type without condition.
	 * @param eventType
	 * @param user
	 * @param priority
	 */
	public SushiNotificationRuleForEvent(SushiEventType eventType, SushiUser user, SushiNotificationPriorityEnum priority) {
		this.eventType = eventType;
		this.condition = new SushiCondition();
		this.user = user;
		this.priority = priority;
	}

	/**
	 * This method is called, when an event occurs, that matches the event type and condition of the notification rule.
	 * This will create a new notification.
	 * @param event
	 */
	public void trigger(SushiEvent event) {
		SushiNotificationForEvent notification = new SushiNotificationForEvent(event, user, this);
		notification.save();
		//here can be added other actions connected to the creation of a notification
		if (priority == SushiNotificationPriorityEnum.HIGH) {
			//send mail
			EmailUtils.sendBP2013Mail(user.getMail(), "Notification GET-Events", notification.toString());
		}
	}
	
	/**
	 * Checks whether an event matches the condition of this notification rule.
	 * Forwards the question whether an event matches the condition to the condition itself. 
	 * @param event
	 * @return whether an event matches a condition
	 */
	public boolean matches(SushiEvent event) {
		if (!hasCondition()) return true;
		return condition.matches(event);
	}
	
	/**
	 * Creates a string representation of the notification rule.
	 */
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
	
	//Getter and Setter
	
	public SushiEventType getEventType() {
		return eventType;
	}

	public void setEventType(SushiEventType eventType) {
		this.eventType = eventType;
	}

	public SushiCondition getCondition() {
		return condition;
	}

	public void setCondition(SushiCondition condition) {
		this.condition = condition;
	}

	public Persistable getTriggeringEntity() {
		return getEventType();
	}
			
	//JPA-Methods
	
	/**
	 * Finds an event notification rule by ID from database.
	 * @param ID
	 * @return event notification rule
	 */
	public static SushiNotificationRuleForEvent findByID(int ID){
		return Persistor.getEntityManager().find(SushiNotificationRuleForEvent.class, ID);
	}
		
	/**
	 * Finds all event notification rules for an event type
	 * @param eventType
	 * @return all event notification rules for an event type
	 */
	public static List<SushiNotificationRuleForEvent> findByEventType(SushiEventType eventType){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE EVENTTYPE_ID = '" + eventType.getID() + "'", SushiNotificationRuleForEvent.class);
		return q.getResultList();
	}

	/**
	 * Finds all event notification rules from database.
	 * @return all event notification rules
	 */
	public static List<SushiNotificationRuleForEvent> findAllEventNotificationRules() {
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE Disc = 'E'", SushiNotificationRuleForEvent.class);
		return q.getResultList();
	}

}
