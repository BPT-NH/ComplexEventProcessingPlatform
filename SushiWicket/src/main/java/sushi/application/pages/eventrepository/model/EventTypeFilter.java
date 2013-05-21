package sushi.application.pages.eventrepository.model;

import java.io.Serializable;

import sushi.event.SushiEventType;

public class EventTypeFilter implements Serializable {
	
	String filterValue;
	String eventFilterCriteria;
	String eventFilterCondition;
	
	public EventTypeFilter(){

	}
	
	public EventTypeFilter(String eventFilterCriteria, String eventFilterCondition, String filterValue){
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


	public boolean match(SushiEventType eventType) {
		if(eventFilterCriteria == null || eventFilterCondition == null || filterValue == null){
			return true;
		}
		if(eventFilterCriteria.equals("ID")){
			try{
				if(eventFilterCondition.equals("<")){
					if(eventType.getID() < Integer.parseInt(filterValue)) return true;
				} else if(eventFilterCondition.equals(">")){
					if(eventType.getID() < Integer.parseInt(filterValue)) return true;
				} else {
					if(eventType.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(eventFilterCriteria.equals("Name")){
			if(eventType.getTypeName().equals(filterValue)) return true;
		} 
		return false;
	}
}
