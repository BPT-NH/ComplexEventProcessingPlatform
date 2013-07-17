package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * This class represents the parallel gateways in a BPMN process.
 * @author micha
 */
@Entity
@Table(name="BPMNAndGateway")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNAndGateway extends AbstractBPMNGateway {

	private static final long serialVersionUID = 1L;

	public BPMNAndGateway() {
		super();
	}
	
	public BPMNAndGateway(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}

}
