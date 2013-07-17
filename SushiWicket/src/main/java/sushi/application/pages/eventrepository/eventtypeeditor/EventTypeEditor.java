package sushi.application.pages.eventrepository.eventtypeeditor;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

/**
 * This page contains the {@link NewEventTypeEditor} and {@link ExistingEventTypeEditor} as tabs.
 * @author micha
 */
public class EventTypeEditor extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AbstractSushiPage eventTypeEditor;

	/**
	 * Constructor for the EventTypeEditor page, which contains the {@link NewEventTypeEditor} and {@link ExistingEventTypeEditor} as tabs.
	 */
	public EventTypeEditor(){
		super();
		this.eventTypeEditor = this;
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Create new event type")) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new NewEventTypeEditor(panelId, eventTypeEditor);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Create from existing event type")) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new ExistingEventTypeEditor(panelId, eventTypeEditor);
			}
		});

		add(new BootstrapTabbedPanel<ITab>("tabs", tabs));
	}

}
