package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * This class represents the end events in a BPMN process.
 * @author micha
 *
 */
@Entity
@Table(name="BPMNEndEvent")
@Inheritance(strategy=InheritanceType.JOINED)
public class BPMNEndEvent extends AbstractBPMNElement {

	private static final long serialVersionUID = 1L;


	public BPMNEndEvent() {
		super();
	}
	
	public BPMNEndEvent(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}
	
	
	public String print() {
		return "EndEvent";
	}

}
