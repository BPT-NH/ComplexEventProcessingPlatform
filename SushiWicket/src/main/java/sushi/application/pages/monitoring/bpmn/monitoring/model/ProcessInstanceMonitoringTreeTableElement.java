package sushi.application.pages.monitoring.bpmn.monitoring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;
import sushi.query.SushiPatternQuery;

/**
 * Representation of a tree node of the process instance treetable.
 * Each element contains a {@link SushiPatternQuery} and associated informations for these query.
 *
 * @param <T> type of content to be stored
 */
public class ProcessInstanceMonitoringTreeTableElement implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private SushiPatternQuery query;
	private ProcessInstanceMonitoringTreeTableElement parent;
	private Set<ProcessInstanceMonitoringTreeTableElement> children = new HashSet<ProcessInstanceMonitoringTreeTableElement>();
	private List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
	private Set<AbstractBPMNElement> monitoredElements;
	private ProcessInstanceMonitor processInstanceMonitor;
	private String startTime;
	private String endTime;
	
	/**
	 * creates a root node
	 * @param processInstanceMonitor 
	 * 
	 * @param content the content to be stored in the new node
	 */
	public ProcessInstanceMonitoringTreeTableElement(int ID, SushiPatternQuery query, ProcessInstanceMonitor processInstanceMonitor) {
		this.ID = ID;
		this.query = query;
		this.processInstanceMonitor = processInstanceMonitor;
		this.monitoredElements = new HashSet<AbstractBPMNElement>(query.getMonitoredElements());
		this.startTime = processInstanceMonitor.getStartTimeForQuery(query).toString();
		this.endTime = processInstanceMonitor.getEndTimeForQuery(query).toString();
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public ProcessInstanceMonitoringTreeTableElement(ProcessInstanceMonitoringTreeTableElement parent, int ID, SushiPatternQuery query, ProcessInstanceMonitor processInstanceMonitor) {
		this(ID, query, processInstanceMonitor);
		this.parent = parent;
		this.parent.getChildren().add(this);
	}

	public Integer getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}

	public SushiPatternQuery getContent() {
		return query;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public ProcessInstanceMonitoringTreeTableElement getParent() {
		return parent;
	}

	public Set<ProcessInstanceMonitoringTreeTableElement> getChildren() {
		return children;
	}
	
	@Override
    public String toString() {
		if (query == null) {
			return new String();
		}
		return query.toString();
    }


	public void remove() {
		if(this.parent != null){
			this.parent.getChildren().remove(this);
		}
		//Müssen Kinder noch explizit entfernt werden?
	}

	public void setParent(ProcessInstanceMonitoringTreeTableElement parent) {
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

	public Set<AbstractBPMNElement> getMonitoredElements() {
		return monitoredElements;
	}

	public void setMonitoredElements(Set<AbstractBPMNElement> monitoredElements) {
		this.monitoredElements = monitoredElements;
	}

	public SushiPatternQuery getQuery() {
		return query;
	}

	public void setQuery(SushiPatternQuery query) {
		this.query = query;
	}

	public ProcessInstanceMonitor getProcessInstanceMonitor() {
		return processInstanceMonitor;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
}
