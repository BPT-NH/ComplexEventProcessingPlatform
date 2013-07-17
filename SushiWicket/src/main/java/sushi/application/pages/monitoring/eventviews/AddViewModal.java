package sushi.application.pages.monitoring.eventviews;

import sushi.application.components.form.BootstrapModal;

/**
 * This modal contains the @see ViewConfigurationPanel to create new event views.
 */
public class AddViewModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private ViewConfigurationPanel panel;

	/**
     * 
     * @param visualisation
     * @param window
     */
    public AddViewModal(String id, final EventViewPage visualisation) {
    	super(id, "Add View");
    	panel = new ViewConfigurationPanel("viewConfigurationPanel", visualisation);
    	add(panel);
	}
}
