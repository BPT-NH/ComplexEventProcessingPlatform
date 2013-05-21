package sushi.application.pages.monitoring.bpmn.analysis.modal.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.monitoring.bpmn.ProcessMonitor;
import sushi.query.SushiPatternQuery;

/**
 * representation of a tree node
 *
 * @param <T> type of content to be stored
 */
public class ProcessAnalysingTreeTableElement implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private SushiPatternQuery query;
	private ProcessAnalysingTreeTableElement parent;
	private Set<ProcessAnalysingTreeTableElement> children = new HashSet<ProcessAnalysingTreeTableElement>();
	private List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
	private Set<AbstractBPMNElement> monitoredElements;
	private ProcessMonitor processMonitor;
	private float averageRuntime;
	private String pathFrequency;
	
	/**
	 * creates a root node
	 * @param processMonitor 
	 * 
	 * @param content the content to be stored in the new node
	 */
	public ProcessAnalysingTreeTableElement(int ID, SushiPatternQuery query, ProcessMonitor processMonitor) {
		this.ID = ID;
		this.query = query;
		this.processMonitor = processMonitor;
		this.monitoredElements = new HashSet<AbstractBPMNElement>(query.getMonitoredElements());
		this.averageRuntime = processMonitor.getAverageRuntimeForQuery(query);
		this.pathFrequency = NumberFormat.getPercentInstance().format(processMonitor.getPathFrequencyForQuery(query));
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public ProcessAnalysingTreeTableElement(ProcessAnalysingTreeTableElement parent, int ID, SushiPatternQuery query, ProcessMonitor processMonitor) {
		this(ID, query, processMonitor);
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

	public ProcessAnalysingTreeTableElement getParent() {
		return parent;
	}

	public Set<ProcessAnalysingTreeTableElement> getChildren() {
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

	public void setParent(ProcessAnalysingTreeTableElement parent) {
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

	public ProcessMonitor getProcessMonitor() {
		return processMonitor;
	}

	public float getAverageRuntime() {
		return averageRuntime;
	}

	public String getPathFrequency() {
		return pathFrequency;
	}
	
}
