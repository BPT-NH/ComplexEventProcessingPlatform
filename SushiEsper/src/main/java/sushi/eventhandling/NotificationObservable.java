package sushi.eventhandling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationRuleForEvent;

/**
 * This method implements the Observable Pattern.
 * It saves the notification rules. It is informed by the broker about incoming events
 * and triggers the notification rules.
 */
public class NotificationObservable{
	
	private static NotificationObservable instance = null;
	private Map<SushiEventType, HashSet<SushiNotificationRuleForEvent>> notifications = new HashMap<SushiEventType, HashSet<SushiNotificationRuleForEvent>>();
	
	/**
	 * Singleton, therefore the constructor is private.
	 */
	private NotificationObservable() {
		initiateWithNotificationsFromDB();
	}
	
	/**
	 * Singleton class. Use this method to get the instance.
	 * @return
	 */
	public static NotificationObservable getInstance() {
		if (instance == null) {
			instance = new NotificationObservable();
		}
		return instance;
	}
	
	/**
	 * Clears the singleton-object.
	 */
	public void clearInstance() {
		instance = null;
	}
	
	/**
	 * Triggers the notification rule with an event.
	 * A new notification will be created.
	 * @param event
	 */
	public void trigger(SushiEvent event) {
		Set<SushiNotificationRuleForEvent> notificationsToTrigger= notifications.get(event.getEventType());
		if (notificationsToTrigger == null) return;
		for (SushiNotificationRuleForEvent notification : notificationsToTrigger) {
			if (notification.matches(event)) {
				notification.trigger(event);
			}
		}
	}
	
	/**
	 * Registers the notifications from the database.
	 */
	private void initiateWithNotificationsFromDB() {
		List<SushiNotificationRuleForEvent> notificationsFromDB = SushiNotificationRuleForEvent.findAllEventNotificationRules();
		for (SushiNotificationRuleForEvent notification: notificationsFromDB) {
			addNotificationObserver(notification);
		}
	}
	
	/**
	 * Adds a new notification rule.
	 * @param notification
	 */
	public void addNotificationObserver(SushiNotificationRuleForEvent notification) {
		//get notifications already registered for this eventtype
		HashSet<SushiNotificationRuleForEvent> listOfNotifcationsForEventType = notifications.get(notification.getEventType());
		if (listOfNotifcationsForEventType == null) listOfNotifcationsForEventType = new HashSet<SushiNotificationRuleForEvent>();
		listOfNotifcationsForEventType.add(notification);
		notifications.put(notification.getEventType(), listOfNotifcationsForEventType);
	}

	/**
	 * Removes all notification rules that subscribed for the event type
	 * @param eventType
	 */
	public void removeNotificationObserversForEventType(SushiEventType eventType) {
		notifications.remove(eventType);
	}
	
	public void clearNotifications() {
		notifications.clear();
	}
	
	/**
	 * Removes a certain notification rule
	 * @param notification
	 */
	public void removeNotificationObserver(SushiNotificationRuleForEvent notification) {
		//get notifications already registered for this eventtype
		HashSet<SushiNotificationRuleForEvent> listOfNotifcationsForEventType = notifications.get(notification.getEventType());
		if (listOfNotifcationsForEventType == null) return;
		listOfNotifcationsForEventType.remove(notification);
		notifications.put(notification.getEventType(), listOfNotifcationsForEventType);
	}
	
	/**
	 * Removes several notification rules.
	 * @param notifications
	 */
	public void removeNotificationObservers(List<SushiNotificationRuleForEvent> notifications) {
		for (SushiNotificationRuleForEvent notification : notifications) {
			removeNotificationObserver(notification);
		}
	}

	/**
	 * Takes several events and forwards the triggering to the coressponding notification rules.
	 * @param events
	 */
	public void trigger(List<SushiEvent> events) {
		for (SushiEvent event : events) trigger(event);
	}

}
