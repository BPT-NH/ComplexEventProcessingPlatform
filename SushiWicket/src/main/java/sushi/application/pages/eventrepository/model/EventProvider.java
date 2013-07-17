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


import sushi.application.components.table.SushiProvider;
import sushi.application.components.table.model.AbstractDataProvider;
import sushi.event.SushiEvent;
import sushi.eventhandling.Broker;

/**
 * This class is the provider for {@link SushiEvent}s.
 * A filter can be specified to return only some events.
 * @author micha
 */
public class EventProvider extends SushiProvider<SushiEvent> implements ISortableDataProvider<SushiEvent, String>, IFilterStateLocator {
	
	private static final long serialVersionUID = 1L;
	private EventFilter eventFilter = new EventFilter();
	
	/**
	 * Constructor for providing {@link SushiEvent}s.
	 */
	public EventProvider() {
		super(SushiEvent.findAll());
		entities = filterEvents(SushiEvent.findAll(), eventFilter);
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
		entities = filterEvents(SushiEvent.findAll(), eventFilter);
	}
		
}
