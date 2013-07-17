package sushi.application.pages.eventrepository.eventtypeeditor.model;

import java.util.List;

import org.apache.wicket.model.IModel;

import sushi.event.SushiEventType;

/**
 * This class is a {@link IModel} and provides the names of the event types in the database.
 * @author micha
 */
public class EventTypeNamesProvider implements IModel<List<String>> {

	private static final long serialVersionUID = -8008561704069525479L;

	@Override
	public void detach() {
		
	}

	@Override
	public List<String> getObject() {
		return SushiEventType.getAllTypeNames();
	}

	@Override
	public void setObject(List<String> object) {
		
	}

}
