package sushi.application.pages.eventrepository.eventview;

import sushi.application.components.form.BootstrapModal;
import sushi.event.SushiEvent;

public class EventViewModal extends BootstrapModal {

	private SushiEvent event;
	private EventViewPanel panel;
	
	public EventViewModal(String id, SushiEvent event) {
    	super(id, "Event View");
    	panel = new EventViewPanel("eventViewPanel", event);
    	panel.setOutputMarkupId(true);
    	add(panel);
    }
    
    public EventViewModal(String id) {
    	super(id, "Event View");
    	panel = new EventViewPanel("eventViewPanel");
    	panel.setOutputMarkupId(true);
    	add(panel);
	}

    public EventViewPanel getPanel() {
		return panel;
	}

	public void setPanel(EventViewPanel panel) {
		this.panel = panel;
	}

    
}
