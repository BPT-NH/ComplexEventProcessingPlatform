package sushi.monitoring.bpmn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.SushiEvent;
import sushi.process.SushiProcessInstance;
import sushi.query.SushiPatternQuery;

/**
 * @author micha
 */
public class QueryMonitor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private SushiPatternQuery query;
	private QueryStatus queryStatus;
	private ViolationStatus violationStatus;
	private SushiProcessInstance processInstance;
	private Date startTime;
	private Date endTime;
	
	public QueryMonitor(SushiPatternQuery query, QueryStatus queryStatus, SushiProcessInstance processInstance) {
		this.query = query;
		this.queryStatus = queryStatus;
		this.violationStatus = ViolationStatus.None;
		this.processInstance = processInstance;
		startTime = this.calculateStartTime();
	}

	public SushiPatternQuery getQuery() {
		return query;
	}

	public void setQuery(SushiPatternQuery query) {
		this.query = query;
	}

	public QueryStatus getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(QueryStatus queryStatus) {
		this.queryStatus = queryStatus;
		if(queryStatus.equals(QueryStatus.Finished) || queryStatus.equals(QueryStatus.Skipped)){
			this.endTime = new Date();
		}
	}
	
	private Date calculateStartTime() {
		if(!query.getMonitoredElements().isEmpty()){
			AbstractBPMNElement firstElement = query.getMonitoredElements().get(0);
			MonitoringPoint firstMonitoringPoint = getFirstMonitoringPoint(firstElement);
			if(firstMonitoringPoint != null){
				List<SushiEvent> eventsWithMatchingEventType = SushiEvent.findByEventType(firstMonitoringPoint.getEventType());
				for(SushiEvent event : eventsWithMatchingEventType){
					if(event.getProcessInstances().contains(processInstance)){
						return event.getTimestamp();
					}
				}
			}
		} 
		return new Date();
	}

	private MonitoringPoint getFirstMonitoringPoint(AbstractBPMNElement firstElement) {
		if(firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.enable) != null){
			return firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.enable);
		} else if(firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.begin) != null){
			return firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.begin);
		} else if(firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.terminate) != null){
			return firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.terminate);
		} else if(firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.skip) != null){
			return firstElement.getMonitoringPointByStateTransitionType(MonitoringPointStateTransition.skip);
		}
		return null;
	}

	public Date getStartTime() {
		return (startTime != null) ? startTime : new Date();
	}

	public Date getEndTime() {
		return (endTime != null) ? endTime : new Date();
	}

	public ViolationStatus getViolationStatus() {
		return violationStatus;
	}

	public void setViolationStatus(ViolationStatus violationStatus) {
		this.violationStatus = violationStatus;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
