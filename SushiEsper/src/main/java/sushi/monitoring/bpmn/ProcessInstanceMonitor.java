package sushi.monitoring.bpmn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public ProcessInstanceMonitor(SushiProcessInstance processInstance){
		this.processInstance = processInstance;
		this.ID = processInstance.getID();
		this.queryMonitors = new ArrayList<QueryMonitor>();
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

	public void setProcessInstance(SushiProcessInstance processInstance) {
		this.processInstance = processInstance;
		refreshStatus();
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
		return refreshStatus();
	}

	private boolean allQueriesTerminated() {
		for(QueryMonitor queryMonitor : queryMonitors){
			if(queryMonitor.getQueryStatus() == QueryStatus.Started){
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
			searchForStoppableQueries();
			if(allQueriesTerminated()){
				this.status = ProcessInstanceStatus.Finished;
			} else {
				this.status = ProcessInstanceStatus.Running;
			}
		}
		return this.status;
	}

	/**
	 * Searches for queries for the given process instance, that could be set skipped or finished.
	 */
	private void searchForStoppableQueries() {
		for(SushiPatternQuery query : getQueriesWithStatus(QueryStatus.Finished)){
			//XORQuery finished, SubQueries mit Running auf Skipped setzten  
			if(query.getPatternQueryType().equals(PatternQueryType.XOR)){
				skipStartedSubQueries(query);
			}
		}
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
	
	public QueryStatus getStatusForQuery(SushiPatternQuery query){
		if(findQueryMonitorByQuery(query) != null){
			return findQueryMonitorByQuery(query).getQueryStatus();
		} else {
			return QueryStatus.NotExisting;
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

}
