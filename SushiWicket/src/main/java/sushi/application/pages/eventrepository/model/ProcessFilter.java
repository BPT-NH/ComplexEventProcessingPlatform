package sushi.application.pages.eventrepository.model;

import sushi.application.pages.eventrepository.ProcessPanel;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * This class filters {@link SushiProcess}es in the {@link ProcessPanel}.
 * @author micha
 */
public class ProcessFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the class, which filters {@link SushiProcess}es in the {@link ProcessPanel}.
	 */
	public ProcessFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link SushiProcess}es in the {@link ProcessPanel}.
	 * @param processFilterCriteria
	 * @param processFilterCondition
	 * @param filterValue
	 */
	public ProcessFilter(String processFilterCriteria, String processFilterCondition, String filterValue){
		super(processFilterCriteria, processFilterCondition, filterValue);
	}

	public boolean match(SushiProcess process) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(process.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
					if(process.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(process.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(filterCriteria.equals("Name")){
			return (process.getName().equals(filterValue));
		} else if(filterCriteria.equals("Process Instance")){
			try{
				int processInstanceID = Integer.parseInt(filterValue);
				boolean match = true;
				if(filterCondition.equals("<")){
					for(SushiProcessInstance instance : process.getProcessInstances()){
						match = instance.getID() < processInstanceID ? true : false;
					}
					return match;
				} else if(filterCondition.equals(">")){
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
		} else if(filterCriteria.equals("Correlation Attribute")){
//			return SushiProcess.getCorrelationAttributesForProcess(process).contains(filterValue);
			return process.getCorrelationAttributes().contains(filterValue);
		}
		return false;
	}
}
