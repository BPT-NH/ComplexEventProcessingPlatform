package sushi.application.pages.monitoring.bpmn.analysis.model;

import java.io.Serializable;

import sushi.monitoring.bpmn.ProcessMonitor;

public class ProcessMonitoringFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String filterValue;
	String processMonitorFilterCriteria;
	String processMonitorFilterCondition;
	
	public ProcessMonitoringFilter(){

	}
	
	public ProcessMonitoringFilter(String processMonitorFilterCriteria, String processMonitorFilterCondition, String filterValue){
		this.processMonitorFilterCondition = processMonitorFilterCondition;
		this.processMonitorFilterCriteria = processMonitorFilterCriteria;
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getProcessMonitorFilterCriteria() {
		return processMonitorFilterCriteria;
	}


	public void setProcessMonitorFilterCriteria(String processMonitorFilterCriteria) {
		this.processMonitorFilterCriteria = processMonitorFilterCriteria;
	}


	public String getProcessMonitorFilterCondition() {
		return processMonitorFilterCondition;
	}


	public void setProcessMonitorFilterCondition(String processMonitorFilterCondition) {
		this.processMonitorFilterCondition = processMonitorFilterCondition;
	}


	public boolean match(ProcessMonitor processMonitor) {
		if(processMonitorFilterCriteria == null || processMonitorFilterCondition == null || filterValue == null){
			return true;
		} 
		if(processMonitorFilterCriteria.equals("ID")){
			try{
				if(processMonitorFilterCondition.equals("<")){
					if(processMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else if(processMonitorFilterCondition.equals(">")){
					if(processMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(processMonitor.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		}
		return false;
	}
}
