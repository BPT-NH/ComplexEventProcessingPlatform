package sushi.application.pages.monitoring.bpmn.monitoring.model;

import sushi.application.pages.eventrepository.model.AbstractFilter;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;

/**
 * This class filters {@link ProcessInstanceMonitor}s.
 * @author micha
 */
public class ProcessInstanceMonitoringFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the class, which filters {@link ProcessInstanceMonitor}s.
	 */
	public ProcessInstanceMonitoringFilter(){
		super();
	}
	
	public ProcessInstanceMonitoringFilter(String processInstanceMonitorFilterCriteria, String processInstanceMonitorFilterCondition, String filterValue){
		super(processInstanceMonitorFilterCriteria, processInstanceMonitorFilterCondition, filterValue);
	}

	public boolean match(ProcessInstanceMonitor processInstanceMonitor) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(processInstanceMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
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
