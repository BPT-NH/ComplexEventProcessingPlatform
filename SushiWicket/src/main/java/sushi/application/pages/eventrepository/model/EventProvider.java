package sushi.application.pages.eventrepository.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.event.SushiEvent;
import sushi.eventhandling.Broker;

public class EventProvider extends AbstractDataProvider implements ISortableDataProvider<SushiEvent, String>, IFilterStateLocator {
	
	private static final long serialVersionUID = 1L;
	private static List<SushiEvent> events;
	private static List<SushiEvent> selectedEvents;
	private ISortState sortState = new SingleSortState();
	private EventFilter eventFilter = new EventFilter();
	
	public EventProvider() {
		events = filterEvents(SushiEvent.findAll(), eventFilter);
		selectedEvents = new ArrayList<SushiEvent>();
	}
	
	@Override
	public void detach() {
//		events = null;
	}
	
	@Override
	public Iterator<? extends SushiEvent> iterator(long first, long count) {
		List<SushiEvent> data = events;
		Collections.sort(data, new Comparator<SushiEvent>() {
			public int compare(SushiEvent e1, SushiEvent e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	private List<SushiEvent> filterEvents(List<SushiEvent> eventsToFilter, EventFilter eventFilter) {
		List<SushiEvent> returnedEvents = new ArrayList<SushiEvent>();
		for(SushiEvent event: eventsToFilter){
			if(eventFilter.match(event)){
				returnedEvents.add(event);
			}
		}
		return returnedEvents;
	}

	@Override
	public IModel<SushiEvent> model(SushiEvent event) {
		return Model.of(event);
	}

	@Override
	public long size() {
		return events.size();
	}

	public List<SushiEvent> getEvents() {
		return events;
	}
	
	public List<SushiEvent> getSelectedEvents(){
		return selectedEvents;
	}

	public static void setEvents(List<SushiEvent> eventList) {
		events = eventList;
	}

	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}

	@Override
	public Object getFilterState() {
		return eventFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.eventFilter = (EventFilter) state;
	}

	public EventFilter getEventFilter() {
		return eventFilter;
	}

	public void setEventFilter(EventFilter eventFilter) {
		this.eventFilter = eventFilter;
		events = filterEvents(SushiEvent.findAll(), eventFilter);
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			SushiEvent event = (SushiEvent) iter.next();
			if(event.getID() == entryId) {
				selectedEvents.add(event);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<SushiEvent> iter = events.iterator(); iter.hasNext();) {
			SushiEvent event = (SushiEvent) iter.next();
			if(event.getID() == entryId) {
				selectedEvents.remove(event);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for (SushiEvent event : selectedEvents) {
			if(event.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(SushiEvent event : selectedEvents){
			events.remove(event);
			Broker.remove(event);
		}
	}

	public void selectAllEntries() {
		for(SushiEvent event : events){
			selectedEvents.add(event);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(SushiEvent event : events){
			if(event.getID() == entryId){
				return event;
			}
		}
		return null;
	}

}
