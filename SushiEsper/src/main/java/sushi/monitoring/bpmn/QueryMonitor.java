package sushi.monitoring.bpmn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.IPattern;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.SushiEvent;
import sushi.event.collection.SushiTree;
import sushi.process.SushiProcessInstance;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;

/**
 * @author micha
 */
public class QueryMonitor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private SushiPatternQuery query;
	//TODO: Map für QueryStatus: ExecutionCount --> QueryStatus
//	private List<QueryStatus> queryStatus;
//	private Set<ViolationStatus> violationStatus;
	private SushiProcessInstance processInstance;
	private Date startTime;
	private Date endTime;
	private boolean isInLoop;
	private List<DetailedQueryStatus> detailedQueryStatus;
	
	public QueryMonitor(SushiPatternQuery query, QueryStatus queryStatus, SushiProcessInstance processInstance) {
		this.query = query;
//		this.queryStatus = new ArrayList<QueryStatus>();
//		this.queryStatus.add(queryStatus);
//		this.violationStatus = new HashSet<ViolationStatus>();
		this.processInstance = processInstance;
		this.startTime = this.calculateStartTime();
		this.isInLoop = determineIsInLoop();
		this.detailedQueryStatus = new ArrayList<DetailedQueryStatus>();
		this.detailedQueryStatus.add(new DetailedQueryStatus(this.query, queryStatus, new HashSet<ViolationStatus>()));
	}

	public SushiPatternQuery getQuery() {
		return query;
	}

	public QueryStatus getQueryStatus() {
		return getLastDetailedQueryStatus().getQueryStatus();
	}

	public void setQueryStatus(QueryStatus queryStatus) {
		//TODO: Queries sollten mehrmals beendbar sein, falls sie sich in einer Schleife befinden
		if(queryStatus.equals(QueryStatus.Finished) || queryStatus.equals(QueryStatus.Skipped)){
			this.endTime = new Date();
			if(queryStatus.equals(QueryStatus.Finished) && this.detailedQueryStatus.size() > 1 && !isInLoop){
				getLastDetailedQueryStatus().getViolationStatus().add(ViolationStatus.Duplication);
			}
		}
		
		if(getLastDetailedQueryStatus().getQueryStatus().equals(QueryStatus.Started)){
			//Bei erstem Finishen der Query, danach ist Start nicht mehr so einfach ermittelbar
			getLastDetailedQueryStatus().setQueryStatus(queryStatus);
		} else {
			//Falls Query nochmal fertiggestellt wird (Schleife)
			this.detailedQueryStatus.add(new DetailedQueryStatus(this.query, queryStatus, new HashSet<ViolationStatus>()));
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

	public Set<ViolationStatus> getViolationStatus() {
		Set<ViolationStatus> violations = new HashSet<ViolationStatus>();
		for(DetailedQueryStatus detailedQueryStatus : this.detailedQueryStatus){
			violations.addAll(detailedQueryStatus.getViolationStatus());
		}
		return violations;
	}

	public void setViolationStatus(Set<ViolationStatus> violationStatus) {
		getLastDetailedQueryStatus().setViolationStatus(violationStatus);
	}
	
	public void addViolationStatus(ViolationStatus violationStatus) {
		getLastDetailedQueryStatus().getViolationStatus().add(violationStatus);
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Returns, how often the query was triggered as finished or skipped.
	 * @return
	 */
	public int getExecutionCount() {
		return detailedQueryStatus.size();
	}
	
	/**
	 * Returns true, if the monitored elements of this queries are contained in a loop.
	 * @return
	 */
	public boolean isInLoop(){
		return this.isInLoop;
	}
	
	private boolean determineIsInLoop() {
		if(query.getPatternQueryType().equals(PatternQueryType.STATETRANSITION)){
			//Query sollte hier nur ein monitored Element enthalten
			if(!query.getMonitoredElements().isEmpty()){
				AbstractBPMNElement monitoredElement = query.getMonitoredElements().get(0);
				return monitoredElement.getIndirectSuccessors().contains(monitoredElement);
			}
		} else {
			SushiTree<AbstractBPMNElement> processDecompositionTree = this.processInstance.getProcess().getProcessDecompositionTree();
			if(processDecompositionTree != null){
				Set<AbstractBPMNElement> indirectParents = processDecompositionTree.getIndirectParents(this.query.getMonitoredElements());
				for(AbstractBPMNElement parent : indirectParents){
					if(parent instanceof Component && ((Component)parent).getType().equals(IPattern.LOOP)){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns true, if the {@link QueryStatus} is Finished or Skipped.
	 * @return
	 */
	public boolean isTerminated() {
		QueryStatus currentStatus = getLastDetailedQueryStatus().getQueryStatus();
		return currentStatus.equals(QueryStatus.Finished) || currentStatus.equals(QueryStatus.Skipped) || currentStatus.equals(QueryStatus.Aborted);
	}
	
	/**
	 * Returns true, if the {@link QueryStatus} is Finished.
	 * @return
	 */
	public boolean isFinished() {
		return getLastDetailedQueryStatus().getQueryStatus().equals(QueryStatus.Finished);
	}

	public DetailedQueryStatus getDetailedQueryStatus() {
		return getLastDetailedQueryStatus();
	}
	
	private DetailedQueryStatus getLastDetailedQueryStatus(){
		return this.detailedQueryStatus.get(this.detailedQueryStatus.size() - 1);
	}

	public boolean isRunning() {
		return getLastDetailedQueryStatus().getQueryStatus().equals(QueryStatus.Started);
	}

}
