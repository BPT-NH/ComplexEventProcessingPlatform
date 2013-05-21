package sushi.eventhandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationRule;

public class NotificationObservable{
	
	private static NotificationObservable instance = null;
	private Map<SushiEventType, HashSet<SushiNotificationRule>> notifications = new HashMap<SushiEventType, HashSet<SushiNotificationRule>>();
	private NotificationObservable() {
		initiateWithNotificationsFromDB();
	}
	
	public static NotificationObservable getInstance() {
		if (instance == null) {
			instance = new NotificationObservable();
		}
		return instance;
	}
	
	public void clearInstance() {
		instance = null;
	}
	
	public void trigger(SushiEvent event) {
		Set<SushiNotificationRule> notificationsToTrigger= notifications.get(event.getEventType());
		if (notificationsToTrigger == null) return;
		for (SushiNotificationRule notification : notificationsToTrigger) {
			if (notification.matches(event)) {
				notification.trigger(event);
			}
		}
	}
	
	private void initiateWithNotificationsFromDB() {
		List<SushiNotificationRule> notificationsFromDB = SushiNotificationRule.findAll();
		for (SushiNotificationRule notification: notificationsFromDB) {
			addNotificationObserver(notification);
		}
	}
	
	public void addNotificationObserver(SushiNotificationRule notification) {
		//get notifications already registered for this eventtype
		HashSet<SushiNotificationRule> listOfNotifcationsForEventType = notifications.get(notification.getEventType());
		if (listOfNotifcationsForEventType == null) listOfNotifcationsForEventType = new HashSet<SushiNotificationRule>();
		listOfNotifcationsForEventType.add(notification);
		notifications.put(notification.getEventType(), listOfNotifcationsForEventType);
	}

	public void removeNotificationObserversForEventType(SushiEventType eventType) {
		notifications.remove(eventType);
	}
	
	public void clearNotifications() {
		notifications.clear();
	}
	
	public void removeNotificationObserver(SushiNotificationRule notification) {
		//get notifications already registered for this eventtype
		HashSet<SushiNotificationRule> listOfNotifcationsForEventType = notifications.get(notification.getEventType());
		if (listOfNotifcationsForEventType == null) return;
		listOfNotifcationsForEventType.remove(notification);
		notifications.put(notification.getEventType(), listOfNotifcationsForEventType);
	}
	
	public void removeNotificationObservers(List<SushiNotificationRule> notifications) {
		for (SushiNotificationRule notification : notifications) {
			removeNotificationObserver(notification);
		}
	}

	public void trigger(List<SushiEvent> events) {
		for (SushiEvent event : events) trigger(event);
	}

	
}
