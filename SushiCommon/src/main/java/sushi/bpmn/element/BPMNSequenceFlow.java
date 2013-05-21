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
@Table(name="BPMNSequenceFlow")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNSequenceFlow extends AbstractBPMNElement {
	
	private static final long serialVersionUID = 1L;
	private String sourceRef;
	private String targetRef;

	public BPMNSequenceFlow() {
		super();
	}

	public BPMNSequenceFlow(String ID, String name, String extractSourceRef, String extractTargetRef) {
		super(ID, name);
		this.sourceRef = extractSourceRef;
		this.targetRef = extractTargetRef;
	}

	public boolean isSequenceFlow() {
		return true;
	}
	
	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	public BPMNSequenceFlow(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}
	
	public String toString() {
		return "SequenceFlow from " + this.sourceRef + " to " + this.targetRef;
	}
	
}
