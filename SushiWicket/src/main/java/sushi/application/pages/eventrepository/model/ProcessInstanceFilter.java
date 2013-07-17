package sushi.application.pages.eventrepository.model;

import java.util.ArrayList;
import java.util.List;

import sushi.application.pages.eventrepository.ProcessInstancePanel;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * This class filters {@link SushiProcessInstance}es in the {@link ProcessInstancePanel}.
 * @author micha
 */
public class ProcessInstanceFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the class, which filters {@link SushiProcessInstance}s in the {@link ProcessInstancePanel}.
	 */
	public ProcessInstanceFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link SushiProcessInstance}s in the {@link ProcessInstancePanel}.
	 * @param filterCriteria
	 * @param filterCondition
	 * @param filterValue
	 */
	public ProcessInstanceFilter(String filterCriteria, String filterCondition, String filterValue){
		super(filterCriteria, filterCondition, filterValue);
	}

	public boolean match(SushiProcessInstance processInstance) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(processInstance.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
					if(processInstance.getID() > Integer.parseInt(filterValue)) return true;
				} else {
					if(processInstance.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(filterCriteria.equals("Process")){
			List<SushiProcess> filterProcesses = SushiProcess.findByName(filterValue);
			if(!filterProcesses.isEmpty()){
				List<SushiProcessInstance> filteredProcessInstances = new ArrayList<SushiProcessInstance>();
				for(SushiProcess process : filterProcesses){
					filteredProcessInstances.addAll(process.getProcessInstances());
				}
				return filteredProcessInstances.contains(processInstance);
			}
		} else if(filterCriteria.equals("Process (ID)")){
			try {
				if(filterCondition.equals("<")){
					if(processInstance.getProcess().getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
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
