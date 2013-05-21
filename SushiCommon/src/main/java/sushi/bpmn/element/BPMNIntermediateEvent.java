package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * @author micha
 *
 */
@Entity
@Table(name="BPMNIntermediateCatchEvent")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNIntermediateEvent extends AbstractBPMNElement {

	private static final long serialVersionUID = 1L;
	private BPMNEventType intermediateEventType;
	private boolean isCatchEvent;
	
	public BPMNIntermediateEvent() {
		super();
	}

	public BPMNIntermediateEvent(String ID, String name, List<MonitoringPoint> monitoringPoints, BPMNEventType intermediateEventType) {
		super(ID, name, monitoringPoints);
		this.intermediateEventType = intermediateEventType;
	}

	public BPMNEventType getIntermediateEventType() {
		return intermediateEventType;
	}

	public void setIntermediateEventType(BPMNEventType intermediateEventType) {
		this.intermediateEventType = intermediateEventType;
	}

	public boolean isCatchEvent() {
		return isCatchEvent;
	}

	public void setCatchEvent(boolean isCatchEvent) {
		this.isCatchEvent = isCatchEvent;
	}

}
