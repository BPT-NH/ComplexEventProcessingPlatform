package sushi.monitoring.bpmn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.collection.SushiTree;
import sushi.process.SushiProcessInstance;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;

/**
 * @author micha
 */
public class ProcessInstanceMonitor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int ID;
	private SushiProcessInstance processInstance;
	//TODO: Tree der QueryMonitors
	private List<QueryMonitor> queryMonitors;
	private ProcessInstanceStatus status;
	private Date startTime;
	private Date endTime;
	private ViolationMonitor violationMonitor;
	
	public ProcessInstanceMonitor(SushiProcessInstance processInstance){
		this.processInstance = processInstance;
		this.ID = processInstance.getID();
		this.queryMonitors = new ArrayList<QueryMonitor>();
		this.violationMonitor = new ViolationMonitor(this);
		refreshStatus();
	}
	
	public void addQuery(SushiPatternQuery query){
		this.queryMonitors.add(new QueryMonitor(query,QueryStatus.Started, processInstance));
		refreshStatus();
	}
	
	public void addQueries(Set<SushiPatternQuery> queries) {
		for(SushiPatternQuery query : queries){
			this.addQuery(query);
		}
	}

	public SushiProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setQueryFinished(SushiPatternQuery query) {
		if(findQueryMonitorByQuery(query) != null){
			findQueryMonitorByQuery(query).setQueryStatus(QueryStatus.Finished);
		}
		refreshStatus();
	}
	
	public void setQuerySkipped(SushiPatternQuery query) {
		if(findQueryMonitorByQuery(query) != null){
			findQueryMonitorByQuery(query).setQueryStatus(QueryStatus.Skipped);
		}
		refreshStatus();
	}

	public ProcessInstanceStatus getStatus() {
		return status;
	}
	
	public void setStatus(ProcessInstanceStatus processInstanceStatus) {
		this.status = processInstanceStatus;
	}

	private boolean allQueriesTerminated() {
		int executionCount = 0;
		if(!queryMonitors.isEmpty()){
			executionCount = queryMonitors.get(0).getExecutionCount();
		}
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.isRunning() && queryMonitor.getExecutionCount() == executionCount){
				return false;
			}
		}
		return true;
	}
	
	private ProcessInstanceStatus refreshStatus(){
		this.startTime = calculateStartTime();
		this.endTime = calculateEndTime();
		if(processInstance == null){
			this.status = ProcessInstanceStatus.NotExisting;
		} else {
			adaptQueryStatus();
			if(allQueriesTerminated()){
				this.status = ProcessInstanceStatus.Finished;
			} else {
				this.status = ProcessInstanceStatus.Running;
			}
		}
		return this.status;
	}

	/**
	 * Searches for queries for the given process instance, that could be set started, skipped or finished.
	 */
	private void adaptQueryStatus() {
		for(SushiPatternQuery query : getQueriesWithStatus(QueryStatus.Finished)){
			//XORQuery finished, SubQueries mit Running auf Skipped setzten  
			if(query.getPatternQueryType().equals(PatternQueryType.XOR)){
				skipStartedSubQueries(query);
			}
		}
		
		violationMonitor.searchForViolations();
	}
	
	private QueryMonitor getRootQueryMonitor(){
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQuery().getParentQuery() == null){
				return queryMonitor;
			}
		}
		return null;
	}

	List<QueryMonitor> getSubQueryMonitors(QueryMonitor queryMonitor) {
		Set<SushiPatternQuery> childQueries = queryMonitor.getQuery().getChildQueries();
		List<QueryMonitor> subQueryMonitors = new ArrayList<QueryMonitor>();
		for(SushiPatternQuery childQuery : childQueries){
			subQueryMonitors.add(getQueryMonitorForQuery(childQuery));
		}
		return subQueryMonitors;
	}

	QueryMonitor getQueryMonitorForQuery(SushiPatternQuery query) {
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQuery().equals(query)){
				return queryMonitor;
			}
		}
		return null;
	}
	
	public List<QueryMonitor> getQueryMonitorsWithStatus(QueryStatus status) {
		List<QueryMonitor> queryMonitorsWithStatus = new ArrayList<QueryMonitor>();
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQueryStatus().equals(status)){
				queryMonitorsWithStatus.add(queryMonitor);
			}
		}
		return queryMonitorsWithStatus;
	}

	/**
	 * Searches recursively for child queries with {@link QueryStatus} Started and sets their {@link QueryStatus} to Skipped.
	 * @param parentQuery
	 */
	private void skipStartedSubQueries(SushiPatternQuery parentQuery) {
		for(SushiPatternQuery query : parentQuery.getChildQueries()){
			if(getStatusForQuery(query).equals(QueryStatus.Started)){
				setStatusForQuery(query, QueryStatus.Skipped);
				if(query.hasChildQueries()){
					skipStartedSubQueries(query);
				}
			}
		}
	}

	public int getID() {
		return ID;
	}

	/**
	 * Returns all {@link SushiPatternQuery} from the contained {@link QueryMonitor}s.
	 * @param queryStatus
	 * @return
	 */
	public Set<SushiPatternQuery> getQueries() {
		Set<SushiPatternQuery> queries = new HashSet<SushiPatternQuery>();
		for(QueryMonitor queryMonitor : queryMonitors){
			queries.add(queryMonitor.getQuery());
		}
		return queries;
	}
	
	/**
	 * Returns all {@link SushiPatternQuery} from the contained {@link QueryMonitor}s, which have the specified {@link QueryStatus}.
	 * @param queryStatus
	 * @return
	 */
	public Set<SushiPatternQuery> getQueriesWithStatus(QueryStatus queryStatus) {
		Set<SushiPatternQuery> queries = new HashSet<SushiPatternQuery>();
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQueryStatus().equals(queryStatus)){
				queries.add(queryMonitor.getQuery());
			}
		}
		return queries;
	}
	
	/**
	 * Returns all {@link QueryMonitor}, which have the specified {@link PatternQueryType}.
	 * @param queryStatus
	 * @return
	 */
	public Set<QueryMonitor> getQueryMonitorsWithQueryType(PatternQueryType patternQueryType) {
		Set<QueryMonitor> queryMonitorsWithPatternQueryType = new HashSet<QueryMonitor>();
		for(QueryMonitor queryMonitor : this.queryMonitors){
			if(queryMonitor.getQuery().getPatternQueryType().equals(patternQueryType)){
				queryMonitorsWithPatternQueryType.add(queryMonitor);
			}
		}
		return queryMonitorsWithPatternQueryType;
	}
	
	/**
	 * Returns all {@link QueryMonitor}, which have the specified monitored {@link AbstractBPMNElement}.
	 * @param queryStatus
	 * @return
	 */
	public Set<QueryMonitor> getQueryMonitorsWithMonitoredElements(Collection<AbstractBPMNElement> monitoredElements) {
		Set<QueryMonitor> queryMonitorsWithMonitoredElements = new HashSet<QueryMonitor>();
		for(QueryMonitor queryMonitor : this.queryMonitors){
			List<AbstractBPMNElement> queryMonitoredElements = queryMonitor.getQuery().getMonitoredElements();
			if(queryMonitoredElements.containsAll(monitoredElements) && monitoredElements.containsAll(queryMonitoredElements)){
				queryMonitorsWithMonitoredElements.add(queryMonitor);
			}
		}
		return queryMonitorsWithMonitoredElements;
	}
	
	public QueryStatus getStatusForQuery(SushiPatternQuery query){
		if(findQueryMonitorByQuery(query) != null){
			return findQueryMonitorByQuery(query).getQueryStatus();
		} else {
			return QueryStatus.NotExisting;
		}
	}
	
	public Set<ViolationStatus> getViolationStatusForQuery(SushiPatternQuery query){
		if(findQueryMonitorByQuery(query) != null){
			return findQueryMonitorByQuery(query).getViolationStatus();
		} else {
			return null;
		}
	}
	
	public void setStatusForQuery(SushiPatternQuery query, QueryStatus queryStatus){
		QueryMonitor queryMonitor = findQueryMonitorByQuery(query);
		if(queryMonitor != null){
			queryMonitor.setQueryStatus(queryStatus);
		} 
	}
	
	private QueryMonitor findQueryMonitorByQuery(SushiPatternQuery query){
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQuery().equals(query)){
				return queryMonitor;
			}
		}
		return null;
	}

	public Date getStartTime() {
		return (startTime != null) ? startTime : new Date();
	}

	public Date getEndTime() {
		return (endTime != null) ? endTime : new Date();
	}
	
	private Date calculateStartTime(){
		Date startTime = new Date();
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getStartTime().before(startTime)){
				startTime = queryMonitor.getStartTime();
			}
		}
		return startTime;
	}
	
	private Date calculateEndTime(){
		Date endTime = calculateStartTime();
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getEndTime().after(endTime)){
				endTime = queryMonitor.getStartTime();
			}
		}
		return endTime;
	}
	
	public Date getStartTimeForQuery(SushiPatternQuery query){
		if(findQueryMonitorByQuery(query) != null){
			return findQueryMonitorByQuery(query).getStartTime();
		}
		return new Date();
	}
	
	public Date getEndTimeForQuery(SushiPatternQuery query){
		if(findQueryMonitorByQuery(query) != null){
			return findQueryMonitorByQuery(query).getEndTime();
		}
		return new Date();
	}
	
	public long getRuntimeForQuery(SushiPatternQuery query){
		return getEndTimeForQuery(query).getTime() - getStartTimeForQuery(query).getTime();
	}

	public SushiTree<DetailedQueryStatus> getDetailedStatus() {
		SushiTree<DetailedQueryStatus> detailedQueryStatusTree = new SushiTree<DetailedQueryStatus>();
		QueryMonitor rootQueryMonitor = getRootQueryMonitor();
		if(rootQueryMonitor != null){
			detailedQueryStatusTree.addRootElement(rootQueryMonitor.getDetailedQueryStatus());
			addSubQueryMonitorsToTree(rootQueryMonitor, detailedQueryStatusTree);
		}
		return detailedQueryStatusTree;
	}

	private void addSubQueryMonitorsToTree(QueryMonitor parentQueryMonitor, SushiTree<DetailedQueryStatus> detailedQueryStatusTree) {
		List<QueryMonitor> subQueryMonitors = getSubQueryMonitors(parentQueryMonitor);
		if(!subQueryMonitors.isEmpty()){
			for(QueryMonitor queryMonitor : subQueryMonitors){
				detailedQueryStatusTree.addChild(parentQueryMonitor.getDetailedQueryStatus(), queryMonitor.getDetailedQueryStatus());
				addSubQueryMonitorsToTree(queryMonitor, detailedQueryStatusTree);
			}
		}
	}

}
