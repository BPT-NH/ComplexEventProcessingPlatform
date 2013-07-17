package sushi.bpmn.element;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * This class is a logical representation for a BPMN gateway element.
 * @author micha
 */
@Entity
@Table(name = "AbstractBPMNGateway")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AbstractBPMNGateway extends AbstractBPMNElement {
	
	private static final long serialVersionUID = 1L;
	
	public AbstractBPMNGateway() {
		super();
	}
	
	public AbstractBPMNGateway(String ID, String name) {
		super(ID, name);
	}
	
	public AbstractBPMNGateway(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name);
	}
	
	/**
	 * Proofs, if a gateway is a joining gateway.
	 * @return
	 */
	public boolean isJoinGateway(){
		return (this.getPredecessors().size() > 1 && this.getSuccessors().size() == 1);
	}
	
	/**
	 * Proofs, if a gateway is a split gateway.
	 * @return
	 */
	public boolean isSplitGateway(){
		return (this.getPredecessors().size() == 1 && this.getSuccessors().size() > 1);
	}

}
