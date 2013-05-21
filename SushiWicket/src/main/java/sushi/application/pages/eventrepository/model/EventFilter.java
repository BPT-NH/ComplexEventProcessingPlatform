package sushi.application.pages.eventrepository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.process.SushiProcessInstance;

public class EventFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String filterValue;
	String eventFilterCriteria;
	String eventFilterCondition;
	
	public EventFilter(){

	}
	
	public EventFilter(String eventFilterCriteria, String eventFilterCondition, String filterValue){
		this.eventFilterCondition = eventFilterCondition;
		this.eventFilterCriteria = eventFilterCriteria;
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getEventFilterCriteria() {
		return eventFilterCriteria;
	}


	public void setEventFilterCriteria(String eventFilterCriteria) {
		this.eventFilterCriteria = eventFilterCriteria;
	}


	public String getEventFilterCondition() {
		return eventFilterCondition;
	}


	public void setEventFilterCondition(String eventFilterCondition) {
		this.eventFilterCondition = eventFilterCondition;
	}


	public boolean match(SushiEvent event) {
		if(eventFilterCriteria == null || eventFilterCondition == null || filterValue == null){
			return true;
		}
		if(eventFilterCriteria.equals("ID")){
			try{
				if(eventFilterCondition.equals("<")){
					if(event.getID() < Integer.parseInt(filterValue)) return true;
				} else if(eventFilterCondition.equals(">")){
					if(event.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(event.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(eventFilterCriteria.equals("Event Type (ID)")){
			if(eventFilterCondition.equals("<")){
				if(event.getEventType().getID() < Integer.parseInt(filterValue)) return true;
			} else if(eventFilterCondition.equals(">")){
				if(event.getEventType().getID() > Integer.parseInt(filterValue)) return true;
			} else {
				if(event.getEventType().getID() == Integer.parseInt(filterValue)) return true;
			}
			return false;
		} else if(eventFilterCriteria.equals("Process Instance")){
			List<SushiProcessInstance> processInstances = new ArrayList<SushiProcessInstance>();
			if(eventFilterCondition.equals("<")){
				for(SushiProcessInstance processInstance : event.getProcessInstances()){
					if(!(processInstance.getID() < event.getID())) return false;
				}
				return true;
			} else if(eventFilterCondition.equals(">")){
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
		} else if(SushiEvent.findAllEventAttributes().contains(eventFilterCriteria)){
			return SushiEvent.findByValue(eventFilterCriteria, filterValue).contains(event);
		} else {
			return false;
		}
		return false;
	}
}
