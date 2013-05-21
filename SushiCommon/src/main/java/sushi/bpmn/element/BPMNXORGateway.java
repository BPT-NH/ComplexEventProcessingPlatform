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
@Table(name="BPMNXORGateway")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNXORGateway extends AbstractBPMNGateway {
	
	private static final long serialVersionUID = 1L;

	public BPMNXORGateway() {
		super();
	}

	public BPMNXORGateway(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}

}
