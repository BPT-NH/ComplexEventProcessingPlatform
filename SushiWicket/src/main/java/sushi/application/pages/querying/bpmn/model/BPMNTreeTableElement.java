package sushi.application.pages.querying.bpmn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;

/**
 * Representation of a tree node of the BPMN treetable.
 * Each element contains a {@link AbstractBPMNElement} and associated informations for these element.
 *
 * @param <T> type of content to be stored
 */
public class BPMNTreeTableElement implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private AbstractBPMNElement content;
	private BPMNTreeTableElement parent;
	private Set<BPMNTreeTableElement> children = new HashSet<BPMNTreeTableElement>();
	private List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
	
	/**
	 * creates a root node
	 * 
	 * @param content the content to be stored in the new node
	 */
	public BPMNTreeTableElement(int ID, AbstractBPMNElement content) {
		this.ID = ID;
		this.content = content;
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public BPMNTreeTableElement(BPMNTreeTableElement parent, int ID, AbstractBPMNElement content) {
		this(ID, content);
		this.parent = parent;
		this.parent.getChildren().add(this);
	}

	public Integer getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}

	public AbstractBPMNElement getContent() {
		return content;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public BPMNTreeTableElement getParent() {
		return parent;
	}

	public Set<BPMNTreeTableElement> getChildren() {
		return children;
	}
	
	@Override
    public String toString() {
		if (content == null) {
			return new String();
		}
		return content.toString();
    }


	public void remove() {
		if(this.parent != null){
			this.parent.getChildren().remove(this);
		}
		//Müssen Kinder noch explizit entfernt werden?
	}

	public void setParent(BPMNTreeTableElement parent) {
		this.parent = parent;
		if(parent != null){
			this.parent.getChildren().add(this);
		}
	}

	public boolean hasMonitoringPoints(){
		return !monitoringPoints.isEmpty();
	}
	
	public void addMonitoringPoint(MonitoringPoint monitoringPoint){
		if(getMonitoringPoint(monitoringPoint.getStateTransitionType()) != null){
			monitoringPoints.remove(getMonitoringPoint(monitoringPoint.getStateTransitionType()));
		} 
		monitoringPoints.add(monitoringPoint);
	}
	
	public void addMonitoringPoints(List<MonitoringPoint> monitoringPoints){
		for(MonitoringPoint monitoringPoint : monitoringPoints){
			addMonitoringPoint(monitoringPoint);
		}
	}
	
	public MonitoringPoint getMonitoringPoint(MonitoringPointStateTransition type){
		for(MonitoringPoint monitoringPoint : monitoringPoints){
			if(monitoringPoint.getStateTransitionType().equals(type)){
				return monitoringPoint;
			}
		}
		return null;
	}
	
	public boolean hasMonitoringPoint(MonitoringPointStateTransition type){
		return this.getMonitoringPoint(type) != null;
	}

}
