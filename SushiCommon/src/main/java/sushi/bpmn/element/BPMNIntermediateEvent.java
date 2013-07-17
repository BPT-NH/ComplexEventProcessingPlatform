package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * This class represents an intermediate event in a BPMN process.
 * @author micha
 */
@Entity
@Table(name="BPMNIntermediateCatchEvent")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNIntermediateEvent extends AbstractBPMNElement {

	private static final long serialVersionUID = 1L;
	protected float timeDuration;
	
	@Column(name = "intermediateEventType")
	private BPMNEventType intermediateEventType;
	
	@Column(name = "isCatchEvent")
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
	
	public float getTimeDuration() {
		return this.timeDuration;
	}

	public void setTimeDuration(float duration) {
		this.timeDuration = duration;
	}

}
