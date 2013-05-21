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
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;

public class EventTypeProvider extends AbstractDataProvider implements ISortableDataProvider<SushiEventType, String>, IFilterStateLocator {
	
	private static List<SushiEventType> eventTypes;
	private ISortState sortState = new SingleSortState();
	private EventTypeFilter eventTypeFilter = new EventTypeFilter();
	private List<SushiEventType> selectedEventTypes;
	
	public EventTypeProvider() {
		eventTypes = filterEventTypes(SushiEventType.findAll(), eventTypeFilter);
		selectedEventTypes = new ArrayList<SushiEventType>();
	}
	
	@Override
	public void detach() {
//		events = null;
	}
	
	@Override
	public Iterator<? extends SushiEventType> iterator(long first, long count) {
		List<SushiEventType> data = eventTypes;
		Collections.sort(data, new Comparator<SushiEventType>() {
			public int compare(SushiEventType e1, SushiEventType e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	private List<SushiEventType> filterEventTypes(List<SushiEventType> eventTypesToFilter, EventTypeFilter eventTypeFilter) {
		List<SushiEventType> returnedEventTypes = new ArrayList<SushiEventType>();
		for(SushiEventType eventType: eventTypesToFilter){
			if(eventTypeFilter.match(eventType)){
				returnedEventTypes.add(eventType);
			}
		}
		return returnedEventTypes;
	}

	@Override
	public IModel<SushiEventType> model(SushiEventType eventType) {
		return Model.of(eventType);
	}

	@Override
	public long size() {
		return eventTypes.size();
	}

	public static List<SushiEventType> getEventTypes() {
		return eventTypes;
	}

	public static void setEventTypes(List<SushiEventType> eventTypeList) {
		eventTypes = eventTypeList;
	}

	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}

	@Override
	public Object getFilterState() {
		return eventTypeFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.eventTypeFilter = (EventTypeFilter) state;
	}

	public EventTypeFilter getEventTypeFilter() {
		return eventTypeFilter;
	}

	public void setEventTypeFilter(EventTypeFilter eventTypeFilter) {
		this.eventTypeFilter = eventTypeFilter;
		eventTypes = filterEventTypes(SushiEventType.findAll(), eventTypeFilter);
	}

	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = eventTypes.iterator(); iter.hasNext();) {
			SushiEventType eventType = (SushiEventType) iter.next();
			if(eventType.getID() == entryId) {
				selectedEventTypes.add(eventType);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<SushiEventType> iter = eventTypes.iterator(); iter.hasNext();) {
			SushiEventType eventType = (SushiEventType) iter.next();
			if(eventType.getID() == entryId) {
				selectedEventTypes.remove(eventType);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for(SushiEventType eventType : selectedEventTypes){
			if(eventType.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(SushiEventType eventType : selectedEventTypes){
			eventTypes.remove(eventType);
			Broker.remove(eventType);
		}
	}
	
	public void selectAllEntries() {
		for(SushiEventType eventType : eventTypes){
			selectedEventTypes.add(eventType);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(SushiEventType eventType : eventTypes){
			if(eventType.getID() == entryId){
				return eventType;
			}
		}
		return null;
	}

}
