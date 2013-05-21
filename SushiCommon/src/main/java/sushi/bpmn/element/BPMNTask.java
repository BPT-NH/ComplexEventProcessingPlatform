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
@Table(name="BPMNTask")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNTask extends AbstractBPMNElement implements AttachableElement {
	
	private static final long serialVersionUID = 1L;
	private BPMNBoundaryEvent attachedIntermediateEvent;

	public BPMNTask() {
		super();
	}

	public BPMNTask(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}
	
	public String print() {
			return "Task: " + this.getName();
	}
	
	@Override
	public boolean hasAttachedIntermediateEvent() {
		return attachedIntermediateEvent != null;
	}

	@Override
	public BPMNBoundaryEvent getAttachedIntermediateEvent() {
		return attachedIntermediateEvent;
	}

	@Override
	public void setAttachedIntermediateEvent(BPMNBoundaryEvent attachedEvent) {
		this.attachedIntermediateEvent = attachedEvent;
	}

}
