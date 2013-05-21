package sushi.eventhandling;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sushi.correlation.Correlator;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

public class Broker{
	
	private static Lock lock = new ReentrantLock(true);
	
	public static SushiEvent send(SushiEvent event) {
		lock.lock();
		System.out.println(event);
		event.save();
		Correlator.correlate(Arrays.asList(event));
		SushiEsper.getInstance().addEvent(event);
		NotificationObservable.getInstance().trigger(event);
		lock.unlock();
		return event;
	}
	
	public static SushiEventType send(SushiEventType eventType) {
		lock.lock();
		SushiEventType registered = SushiEventType.findByTypeName(eventType.getTypeName()); 
		if (registered != null) {
			System.err.println("An EventType with name :" + eventType.getTypeName() + " is already saved.");
			return registered;
		}
		eventType.save();
		SushiEsper.getInstance().addEventType(eventType);
		lock.unlock();
		return eventType;
	}
	
	public static SushiEventType remove(SushiEventType eventType) {
		lock.lock();
		eventType.remove();
		SushiEsper.getInstance().removeEventType(eventType);
		lock.unlock();
		return eventType;
	}
	
	public static List<SushiEvent> send(List<SushiEvent> events) {
		lock.lock();
		if(events != null && !events.isEmpty()){
			Correlator.correlate(events);
			SushiEvent.save(events);
			SushiEsper.getInstance().addEvents(events);
			NotificationObservable.getInstance().trigger(events);
		}
		lock.unlock();
		return events;
	}
	
	public static SushiEvent remove(SushiEvent event) {
		lock.lock();
		SushiEsper.getInstance().removeEvent(event);
		event.remove();
		lock.unlock();
		return event;
	}

	public static Lock getLock() {
		return lock;
	}
	
	 
}
