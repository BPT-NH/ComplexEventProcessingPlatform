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
@Table(name="BPMNSubProcess")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNSubProcess extends BPMNProcess implements AttachableElement{

	private static final long serialVersionUID = 1L;
	private BPMNBoundaryEvent attachedIntermediateEvent;

	public BPMNSubProcess() {
		super();
	}
	
	public BPMNSubProcess(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}

	public boolean isProcess() {
		return true;
	}
	
	public String print() {
		return "SubProcess";
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
