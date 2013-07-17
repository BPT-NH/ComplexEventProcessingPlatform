package sushi.monitoring.bpmn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import sushi.query.SushiPatternQuery;

public class DetailedQueryStatus implements Serializable {
	
	private SushiPatternQuery query;
	private QueryStatus queryStatus;
	private Set<ViolationStatus> violationStatus;
	private Date endTime;
	
	public DetailedQueryStatus(SushiPatternQuery query, QueryStatus queryStatus, Set<ViolationStatus> violationStatus) {
		this.query = query;
		this.setQueryStatus(queryStatus);
		this.violationStatus = violationStatus;
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
	
	public Set<ViolationStatus> getViolationStatus() {
		return violationStatus;
	}
	
	public void setViolationStatus(Set<ViolationStatus> violationStatus) {
		this.violationStatus = violationStatus;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	@Override
	public String toString() {
		return query.getTitle() + " is " + queryStatus + ". Violations: " + violationStatus;
	}
	

}
