package sushi.application.pages.eventrepository.model;

import sushi.application.pages.eventrepository.EventPanel;
import sushi.event.SushiEvent;
import sushi.process.SushiProcessInstance;

/**
 * This class filters {@link SushiEvent}s in the {@link EventPanel}.
 * @author micha
 */
public class EventFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	public EventFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link SushiEvent} in the {@link EventPanel}.
	 * @param filterCriteria
	 * @param filterCondition
	 * @param filterValue
	 */
	public EventFilter(String filterCriteria, String filterCondition, String filterValue){
		super(filterCriteria, filterCondition, filterValue);
	}


	public boolean match(SushiEvent event) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(event.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
					if(event.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(event.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(filterCriteria.equals("Event Type (ID)")){
			if(filterCondition.equals("<")){
				if(event.getEventType().getID() < Integer.parseInt(filterValue)) return true;
			} else if(filterCondition.equals(">")){
				if(event.getEventType().getID() > Integer.parseInt(filterValue)) return true;
			} else {
				if(event.getEventType().getID() == Integer.parseInt(filterValue)) return true;
			}
			return false;
		} else if(filterCriteria.equals("Process Instance")){
			if(filterCondition.equals("<")){
				for(SushiProcessInstance processInstance : event.getProcessInstances()){
					if(!(processInstance.getID() < event.getID())) return false;
				}
				return true;
			} else if(filterCondition.equals(">")){
				for(SushiProcessInstance processInstance : event.getProcessInstances()){
					if(!(processInstance.getID() > event.getID())) return false;
				}
				return true;
			} else {
				for(SushiProcessInstance processInstance : event.getProcessInstances()){
					if(!(processInstance.getID() == event.getID())) return false;
				}
				return true;
			}
		} else if(SushiEvent.findAllEventAttributes().contains(filterCriteria)){
			return SushiEvent.findByValue(filterCriteria, filterValue).contains(event);
		} else {
			return false;
		}
		return false;
	}
}
