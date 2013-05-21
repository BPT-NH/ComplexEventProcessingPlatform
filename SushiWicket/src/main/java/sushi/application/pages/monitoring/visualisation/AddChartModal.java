package sushi.application.pages.monitoring.visualisation;

import sushi.application.components.form.BootstrapModal;

public class AddChartModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private ChartConfigurationPanel panel;

	/**
     * 
     * @param visualisationPage
     * @param window
     */
    public AddChartModal(String id, final VisualisationPage visualisationPage) {
    	super(id, "Add Chart");
    	panel = new ChartConfigurationPanel("chartConfigurationPanel", visualisationPage);
    	add(panel);
    	panel.updateSlider();
	}
}
