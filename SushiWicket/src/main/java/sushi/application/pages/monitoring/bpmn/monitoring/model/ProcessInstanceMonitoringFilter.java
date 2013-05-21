package sushi.application.pages.monitoring.bpmn.monitoring.model;

import java.io.Serializable;

import sushi.monitoring.bpmn.ProcessInstanceMonitor;

/**
 * @author micha
 */
public class ProcessInstanceMonitoringFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String filterValue;
	String processInstanceMonitorFilterCriteria;
	String processInstanceMonitorFilterCondition;
	
	public ProcessInstanceMonitoringFilter(){

	}
	
	public ProcessInstanceMonitoringFilter(String processInstanceMonitorFilterCriteria, String processInstanceMonitorFilterCondition, String filterValue){
		this.processInstanceMonitorFilterCondition = processInstanceMonitorFilterCondition;
		this.processInstanceMonitorFilterCriteria = processInstanceMonitorFilterCriteria;
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getProcessInstanceMonitorFilterCriteria() {
		return processInstanceMonitorFilterCriteria;
	}


	public void setProcessInstanceMonitorFilterCriteria(String processInstanceMonitorFilterCriteria) {
		this.processInstanceMonitorFilterCriteria = processInstanceMonitorFilterCriteria;
	}


	public String getProcessInstanceMonitorFilterCondition() {
		return processInstanceMonitorFilterCondition;
	}


	public void setProcessInstanceMonitorFilterCondition(String processInstanceMonitorFilterCondition) {
		this.processInstanceMonitorFilterCondition = processInstanceMonitorFilterCondition;
	}


	public boolean match(ProcessInstanceMonitor processInstanceMonitor) {
		if(processInstanceMonitorFilterCriteria == null || processInstanceMonitorFilterCondition == null || filterValue == null){
			return true;
		}
		if(processInstanceMonitorFilterCriteria.equals("ID")){
			try{
				if(processInstanceMonitorFilterCondition.equals("<")){
					if(processInstanceMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else if(processInstanceMonitorFilterCondition.equals(">")){
					if(processInstanceMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(processInstanceMonitor.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else {
			return false;
		}
		return false;
	}
}
