package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * This class represents a event-based gateway in a BPMN process.
 * @author micha
 */
@Entity
@Table(name="BPMNEventBasedGateway")
@Inheritance(strategy = InheritanceType.JOINED)
public class BPMNEventBasedGateway extends AbstractBPMNGateway {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "eventBasedGatewayType")
	private BPMNEventBasedGatewayType type;

	public BPMNEventBasedGateway() {
		super();
	}
	
	public BPMNEventBasedGateway(String ID, String name, List<MonitoringPoint> monitoringPoints, BPMNEventBasedGatewayType type) {
		super(ID, name, monitoringPoints);
		this.type = type;
	}

	public BPMNEventBasedGatewayType getType() {
		return type;
	}

	public void setType(BPMNEventBasedGatewayType type) {
		this.type = type;
	}

}
