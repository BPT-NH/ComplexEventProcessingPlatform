package sushi.bpmn.element;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * This enum contains the event types of an BPMN event.
 * @author micha
 */
public enum BPMNEventType {
	
	@Enumerated(EnumType.STRING)
	Blank, Timer, Compensation, Cancel, Error, Message, Link, Signal;

}
