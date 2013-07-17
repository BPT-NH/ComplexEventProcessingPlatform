package sushi.application.pages.monitoring.visualisation;

import sushi.application.components.form.BootstrapModal;

/**
 * Modal that encapsulates the panel for the creation of attribute charts
 */
public class AddChartModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private ChartConfigurationPanel panel;

    public AddChartModal(String id, final AttributeChartPage visualisationPage) {
    	super(id, "Add Chart");
    	panel = new ChartConfigurationPanel("chartConfigurationPanel", visualisationPage);
    	add(panel);
    	panel.updateSlider();
	}
}
