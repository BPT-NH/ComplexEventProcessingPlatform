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
@Table(name="BPMNStartEvent")
@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorValue("BPMNStartEvent")
public class BPMNStartEvent extends AbstractBPMNElement {
	
	private static final long serialVersionUID = 1L;
	private BPMNEventType startEventType;

	public BPMNStartEvent() {
		super();
	}

	public BPMNStartEvent(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
		this.startEventType = BPMNEventType.Blank;
	}
	
	public BPMNStartEvent(String ID, String name, List<MonitoringPoint> monitoringPoints, BPMNEventType startEventType) {
		super(ID, name, monitoringPoints);
		this.startEventType = startEventType;
	}
	
	public String print() {
		return "StartEvent";
	}

	public BPMNEventType getStartEventType() {
		return startEventType;
	}

	public void setStartEventType(BPMNEventType startEventType) {
		this.startEventType = startEventType;
	}

}
