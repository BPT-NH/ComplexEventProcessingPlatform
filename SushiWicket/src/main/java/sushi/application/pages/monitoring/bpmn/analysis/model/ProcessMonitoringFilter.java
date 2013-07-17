package sushi.application.pages.monitoring.bpmn.analysis.model;

import sushi.application.pages.eventrepository.model.AbstractFilter;
import sushi.monitoring.bpmn.ProcessMonitor;

/**
 * This class filters {@link ProcessMonitor}s.
 * @author micha
 */
public class ProcessMonitoringFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the class, which filters {@link ProcessMonitor}s.
	 */
	public ProcessMonitoringFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link ProcessMonitor}s.
	 */
	public ProcessMonitoringFilter(String processMonitorFilterCriteria, String processMonitorFilterCondition, String filterValue){
		super(processMonitorFilterCriteria, processMonitorFilterCondition, filterValue);
	}

	public boolean match(ProcessMonitor processMonitor) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		} 
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(processMonitor.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
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
