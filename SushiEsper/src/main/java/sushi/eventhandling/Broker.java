package sushi.eventhandling;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sushi.correlation.Correlator;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;

/**
 * This class is a central point for events, and event types to enter the platform.
 * This enables the consistency between streaming engine and database.
 */
public class Broker{
	
	private static Lock lock = new ReentrantLock(true);
	
	/**
	 * This method should be used to insert events into the platform.
	 * It will be correlated, saved in the database, send to the streaming engine
	 * and possibly invoke notifications.
	 * @param event
	 * @return
	 */
	public static SushiEvent send(SushiEvent event) {
		lock.lock();
		System.out.println(event);
		event.save();
		Correlator.correlate(Arrays.asList(event));
		SushiStreamProcessingAdapter.getInstance().addEvent(event);
		NotificationObservable.getInstance().trigger(event);
		lock.unlock();
		return event;
	}
	
	
	/**
	 * This method should be used to save eventNotificationRules.
	 * They will be added to the observable and saved in the database.
	 * @param rule
	 * @return
	 */
	public static SushiNotificationRule send(SushiNotificationRule rule) {
		lock.lock();
		rule.save();
		try {
			SushiNotificationRuleForEvent eventRule = (SushiNotificationRuleForEvent) rule;
			NotificationObservable.getInstance().addNotificationObserver(eventRule);
			return rule;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * This method should be used to insert event types into the platform.
	 * It will be saved in the database and registered at the streaming engine.
	 * @param eventType
	 * @return
	 */
	public static SushiEventType send(SushiEventType eventType) {
		lock.lock();
		SushiEventType registered = SushiEventType.findByTypeName(eventType.getTypeName()); 
		if (registered != null) {
			System.err.println("An EventType with name :" + eventType.getTypeName() + " is already saved.");
			lock.unlock();
			return registered;
		}
		eventType.save();
		SushiStreamProcessingAdapter.getInstance().addEventType(eventType);
		lock.unlock();
		return eventType;
	}
		
	/**
	 * This method should be used to send several events to the platform.
	 * They will be correlated, saved in the database, send to the streaming engine
	 * and possibly invoke notifications.
	 * @param events
	 * @return
	 */
	public static List<SushiEvent> send(List<SushiEvent> events) {
		lock.lock();
		if(events != null && !events.isEmpty()){
			SushiEvent.save(events);
			Correlator.correlate(events);
			SushiStreamProcessingAdapter.getInstance().addEvents(events);
			NotificationObservable.getInstance().trigger(events);
		}
		lock.unlock();
		return events;
	}
	
	/**
	 * This method should be used to remove event types from the platform.
	 * It will be removed from the database and deleted from the streaming engine.
	 * @param eventType
	 * @return
	 */
	public static SushiEventType remove(SushiEventType eventType) {
		lock.lock();
		eventType.remove();
		SushiStreamProcessingAdapter.getInstance().removeEventType(eventType);
		lock.unlock();
		return eventType;
	}

	/**
	 * This method should be used to remove an event from the platform.
	 * It will be removed from the database and deleted from the streaming engine.
	 * @param eventType
	 * @return
	 */	
	public static SushiEvent remove(SushiEvent event) {
		lock.lock();
		SushiStreamProcessingAdapter.getInstance().removeEvent(event);
		event.remove();
		lock.unlock();
		return event;
	}

	/**
	 * This implements a semaphore. During an operation the Broker is locked, to make sure the data is consistent.
	 * @return
	 */
	public static Lock getLock() {
		return lock;
	}
	
}
