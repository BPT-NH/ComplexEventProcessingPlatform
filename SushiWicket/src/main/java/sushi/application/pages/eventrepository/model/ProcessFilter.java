package sushi.application.pages.eventrepository.model;

import java.io.Serializable;

import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

public class ProcessFilter implements Serializable {
	
	String filterValue;
	String processFilterCriteria;
	String processFilterCondition;
	
	public ProcessFilter(){

	}
	
	public ProcessFilter(String processFilterCriteria, String processFilterCondition, String filterValue){
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

	public String getProcessFilterCriteria() {
		return processFilterCriteria;
	}


	public void setProcessFilterCriteria(String processFilterCriteria) {
		this.processFilterCriteria = processFilterCriteria;
	}


	public String getProcessFilterCondition() {
		return processFilterCondition;
	}


	public void setProcessFilterCondition(String processFilterCondition) {
		this.processFilterCondition = processFilterCondition;
	}


	public boolean match(SushiProcess process) {
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
		} else if(processFilterCriteria.equals("Process Instance")){
			try{
				int processInstanceID = Integer.parseInt(filterValue);
				boolean match = true;
				if(processFilterCondition.equals("<")){
					for(SushiProcessInstance instance : process.getProcessInstances()){
						match = instance.getID() < processInstanceID ? true : false;
					}
					return match;
				} else if(processFilterCondition.equals(">")){
					for(SushiProcessInstance instance : process.getProcessInstances()){
						match = instance.getID() > processInstanceID ? true : false;
					}
					return match;
				} else {
					for(SushiProcessInstance instance : process.getProcessInstances()){
						match = instance.getID() == processInstanceID ? true : false;
					}
					return match;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(processFilterCriteria.equals("Correlation Attribute")){
//			return SushiProcess.getCorrelationAttributesForProcess(process).contains(filterValue);
			return process.getCorrelationAttributes().contains(filterValue);
		}
		return false;
	}
}
