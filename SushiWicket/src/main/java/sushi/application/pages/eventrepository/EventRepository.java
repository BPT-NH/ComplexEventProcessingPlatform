package sushi.application.pages.eventrepository;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.eventview.EventViewModal;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

/**
 * The event repository gives information about events, event types, processes and process instances, stored in the application.
 * @author micha
 */
@SuppressWarnings("serial")
public class EventRepository extends AbstractSushiPage {

	private EventRepository eventRepository;
	private EventViewModal eventViewModal;

	/**
	 * Constructor for the {@link EventRepository}.
	 * Initializes and adds the {@link EventPanel}, {@link EventTypePanel}, {@link ProcessPanel}, {@link ProcessInstancePanel} and {@link BPMNProcessPanel}}.
	 */
	public EventRepository() {
		super();
		this.eventRepository = this;
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Event")) {
			@Override
			public Panel getPanel(String panelId) {
				return new EventPanel(panelId, eventRepository);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Event Type")) {
			@Override
			public Panel getPanel(String panelId) {
				return new EventTypePanel(panelId, eventRepository);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Process")) {
			@Override
			public Panel getPanel(String panelId) {
				return new ProcessPanel(panelId, eventRepository);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Process Instance")) {
			@Override
			public Panel getPanel(String panelId) {
				return new ProcessInstancePanel(panelId, eventRepository);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("BPMN")) {
			@Override
			public Panel getPanel(String panelId) {
				return new BPMNProcessPanel(panelId, eventRepository);
			}
		});

		add(new BootstrapTabbedPanel<ITab>("tabs", tabs));
		
		//add modal
		eventViewModal = new EventViewModal("eventViewModal");
		eventViewModal.setOutputMarkupId(true);
		add(eventViewModal);

		
		
	}

	public EventViewModal getEventViewModal() {
		return eventViewModal;
	}

	public void setEventViewModal(EventViewModal eventViewModal) {
		this.eventViewModal = eventViewModal;
	}
	
}
