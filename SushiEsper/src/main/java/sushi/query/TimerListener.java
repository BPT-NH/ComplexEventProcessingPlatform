package sushi.query;

import java.util.Date;

import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;

/**
 * This thread is indicated to wait for the specified time duration and then to send a timer event to Esper.
 * It is used for monitoring of attached intermediate timer events.
 * @author micha
 */
public class TimerListener extends Thread {
	
	private SushiEvent timerEvent;
	private SushiEventType boundaryTimerEventType;
	private float timeDuration;

	public TimerListener(SushiEvent timerEvent, SushiEventType boundaryTimerEventType, float timeDuration) {
		this.timerEvent = timerEvent;
		this.boundaryTimerEventType = boundaryTimerEventType;
		this.timeDuration = timeDuration;
	}

	@Override
	public void run() {
		try {
			//Millisekunden = Minuten * 1000 * 60
			float time = timeDuration * 1000 * 60;
			int sleepTime = Math.round(time);
			
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("Send Boundary Timer Event to Esper" + createBoundaryTimerEvent());
		
		Broker.getLock().lock();
		SushiEsper.getInstance().addEvent(createBoundaryTimerEvent());
		Broker.getLock().unlock();
	}

	private SushiEvent createBoundaryTimerEvent() {
		SushiEvent boundaryTimerEvent = new SushiEvent(boundaryTimerEventType, new Date());
		boundaryTimerEvent.setProcessInstances(timerEvent.getProcessInstances());
		return boundaryTimerEvent;
	}
	
}

