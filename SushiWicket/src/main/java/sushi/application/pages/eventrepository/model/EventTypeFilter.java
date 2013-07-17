package sushi.application.pages.eventrepository.model;

import sushi.application.pages.eventrepository.EventTypePanel;
import sushi.event.SushiEventType;

/**
 * This class filters {@link SushiEventType}s in the {@link EventTypePanel}.
 * @author micha
 */
public class EventTypeFilter extends AbstractFilter {
	
	private static final long serialVersionUID = 1L;
	
	public EventTypeFilter(){
		super();
	}
	
	/**
	 * Constructor for the class, which filters {@link SushiEventType}s in the {@link EventTypePanel}.
	 * @param filterCriteria
	 * @param filterCondition
	 * @param filterValue
	 */
	public EventTypeFilter(String filterCriteria, String filterCondition, String filterValue){
		super(filterCriteria, filterCondition, filterValue);
	}

	public boolean match(SushiEventType eventType) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(eventType.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
					if(eventType.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(eventType.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(filterCriteria.equals("Name")){
			if(eventType.getTypeName().equals(filterValue)) return true;
		} 
		return false;
	}
}
