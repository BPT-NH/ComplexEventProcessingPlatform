package sushi.application.pages.eventrepository.model;

import java.io.Serializable;

import sushi.bpmn.element.BPMNProcess;

public class BPMNProcessFilter implements Serializable {
	
	String filterValue;
	String processFilterCriteria;
	String processFilterCondition;
	
	public BPMNProcessFilter(){

	}
	
	public BPMNProcessFilter(String processFilterCriteria, String processFilterCondition, String filterValue){
		this.processFilterCondition = processFilterCondition;
		this.processFilterCriteria = processFilterCriteria;
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getBPMNProcessFilterCriteria() {
		return processFilterCriteria;
	}


	public void setBPMNProcessFilterCriteria(String processFilterCriteria) {
		this.processFilterCriteria = processFilterCriteria;
	}


	public String getBPMNProcessFilterCondition() {
		return processFilterCondition;
	}


	public void setBPMNProcessFilterCondition(String processFilterCondition) {
		this.processFilterCondition = processFilterCondition;
	}


	public boolean match(BPMNProcess process) {
		if(processFilterCriteria == null || processFilterCondition == null || filterValue == null){
			return true;
		}
		if(processFilterCriteria.equals("ID")){
			try{
				if(processFilterCondition.equals("<")){
					if(process.getID() < Integer.parseInt(filterValue)) return true;
				} else if(processFilterCondition.equals(">")){
					if(process.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(process.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(processFilterCriteria.equals("Name")){
			return (process.getName().equals(filterValue));
		} 
		return false;
	}
}
