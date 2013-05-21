package sushi.application.pages.eventrepository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

public class ProcessInstanceFilter implements Serializable {
	
	String filterValue;
	String processInstanceFilterCriteria;
	String processInstanceFilterCondition;
	
	public ProcessInstanceFilter(){

	}
	
	public ProcessInstanceFilter(String processInstanceFilterCriteria, String processInstanceFilterCondition, String filterValue){
		this.processInstanceFilterCondition = processInstanceFilterCondition;
		this.processInstanceFilterCriteria = processInstanceFilterCriteria;
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getProcessInstanceFilterCriteria() {
		return processInstanceFilterCriteria;
	}


	public void setProcessInstanceFilterCriteria(String processFilterCriteria) {
		this.processInstanceFilterCriteria = processFilterCriteria;
	}


	public String getProcessInstanceFilterCondition() {
		return processInstanceFilterCondition;
	}


	public void setProcessInstanceFilterCondition(String processFilterCondition) {
		this.processInstanceFilterCondition = processFilterCondition;
	}


	public boolean match(SushiProcessInstance processInstance) {
		if(processInstanceFilterCriteria == null || processInstanceFilterCondition == null || filterValue == null){
			return true;
		}
		if(processInstanceFilterCriteria.equals("ID")){
			try{
				if(processInstanceFilterCondition.equals("<")){
					if(processInstance.getID() < Integer.parseInt(filterValue)) return true;
				} else if(processInstanceFilterCondition.equals(">")){
					if(processInstance.getID() > Integer.parseInt(filterValue)) return true;
				} else {
					if(processInstance.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(processInstanceFilterCriteria.equals("Process")){
			List<SushiProcess> filterProcesses = SushiProcess.findByName(filterValue);
			if(!filterProcesses.isEmpty()){
				List<SushiProcessInstance> filteredProcessInstances = new ArrayList<SushiProcessInstance>();
				for(SushiProcess process : filterProcesses){
					filteredProcessInstances.addAll(process.getProcessInstances());
				}
				return filteredProcessInstances.contains(processInstance);
			}
		} else if(processInstanceFilterCriteria.equals("Process (ID)")){
			try {
				if(processInstanceFilterCondition.equals("<")){
					if(processInstance.getProcess().getID() < Integer.parseInt(filterValue)) return true;
				} else if(processInstanceFilterCondition.equals(">")){
					if(processInstance.getProcess().getID() > Integer.parseInt(filterValue)) return true;
				} else {
					if(processInstance.getProcess().getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		}
		return false;
	}
}
