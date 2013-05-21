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
@Table(name="BPMNBoundaryEvent")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNBoundaryEvent extends BPMNIntermediateEvent {

	private static final long serialVersionUID = 1L;
	private boolean isCancelActivity = false;
	
	private AbstractBPMNElement attachedToElement = null;
	private float timeDuration;
	
	public BPMNBoundaryEvent() {
		super();
	}
	
	public BPMNBoundaryEvent(String ID, String name, List<MonitoringPoint> monitoringPoints, BPMNEventType intermediateEventType) {
		super(ID, name, monitoringPoints, intermediateEventType);
	}

	public boolean isCancelActivity() {
		return isCancelActivity;
	}

	public void setCancelActivity(boolean isCancelActivity) {
		this.isCancelActivity = isCancelActivity;
	}

	public AbstractBPMNElement getAttachedToElement() {
		return attachedToElement;
	}

	public void setAttachedToElement(AbstractBPMNElement attachedToElement) {
		this.attachedToElement = attachedToElement;
	}
	
	public boolean isBoundaryEvent() {
		return true;
	}
	
	public String print() {
		if (isCancelActivity()) return "cancelling Boundary event";
		return "Boundary Event";
	}
	
	public float getTimeDuration() {
		return this.timeDuration;
	}

	public void setTimeDuration(float duration) {
		this.timeDuration = duration;
	}
	
}
