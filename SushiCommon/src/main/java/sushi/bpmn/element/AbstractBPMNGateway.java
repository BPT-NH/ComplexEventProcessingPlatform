package sushi.bpmn.element;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKey;
import javax.persistence.Table;

import sushi.bpmn.monitoringpoint.MonitoringPoint;

/**
 * @author micha
 *
 */
@Entity
@Table(name = "AbstractBPMNGateway")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AbstractBPMNGateway extends AbstractBPMNElement {
	
	private static final long serialVersionUID = 1L;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @MapKey(name = "ID")
	private List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();

	public AbstractBPMNGateway() {
		super();
	}
	
	public AbstractBPMNGateway(String ID, String name) {
		super(ID, name);
	}
	
	public AbstractBPMNGateway(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name);
		this.monitoringPoints = monitoringPoints;
	}
	
	public List<MonitoringPoint> getMonitoringPoints() {
		return monitoringPoints;
	}
	
	public void setMonitoringPoints(List<MonitoringPoint> monitoringPoints) {
		this.monitoringPoints = monitoringPoints;
	}
	
//	public String getMonitoringPoint(MonitoringPoint monitoringPointType) {
//	TODO Monitoring Points für Gateways evtl anders?
//	}
	
//	public void setMonitoringPoint(MonitoringPoint monitoringPointType, String monitoringPoint) {
//	TODO
//	}
	
	public boolean isJoinGateway(){
		return (this.getPredecessors().size() > 1 && this.getSuccessors().size() == 1);
	}
	
	public boolean isSplitGateway(){
		return (this.getPredecessors().size() == 1 && this.getSuccessors().size() > 1);
	}

	public boolean isLoopGateway(BPMNProcess process) {
		return this.getIndirectSuccessors().contains(this);
	}
	
}
