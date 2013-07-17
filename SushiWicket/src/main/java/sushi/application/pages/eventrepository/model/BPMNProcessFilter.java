package sushi.application.pages.eventrepository.model;

import sushi.application.pages.eventrepository.BPMNProcessPanel;
import sushi.bpmn.element.BPMNProcess;

/**
 * This class filters {@link BPMNProcess} in the {@link BPMNProcessPanel}.
 * @author micha
 */
public class BPMNProcessFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	public BPMNProcessFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link BPMNProcess} in the {@link BPMNProcessPanel}.
	 * @param processFilterCriteria
	 * @param processFilterCondition
	 * @param filterValue
	 */
	public BPMNProcessFilter(String processFilterCriteria, String processFilterCondition, String filterValue){
		super(processFilterCriteria, processFilterCondition, filterValue);
	}

	public boolean match(BPMNProcess process) {
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
		} 
		return false;
	}
}
