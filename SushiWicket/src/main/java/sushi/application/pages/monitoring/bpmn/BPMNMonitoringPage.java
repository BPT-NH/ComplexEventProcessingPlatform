package sushi.application.pages.monitoring.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.monitoring.bpmn.analysis.BPMNAnalysisPanel;
import sushi.application.pages.monitoring.bpmn.monitoring.BPMNMonitoringPanel;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

/**
 * This page facilitates the monitoring of the execution of BPMN processes.
 * It provides a {@link BPMNMonitoringPanel} and a {@link BPMNAnalysisPanel}.
 * @author micha
 */
public class BPMNMonitoringPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AbstractSushiPage monitoringPage;

	/**
	 * Constructor for a page, which facilitates the monitoring of the execution of BPMN processes.
	 * It provides a {@link BPMNMonitoringPanel} and a {@link BPMNAnalysisPanel}.
	 */
	public BPMNMonitoringPage() {
		super();
		this.monitoringPage = this;
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Monitoring")) {
			@Override
			public Panel getPanel(String panelId) {
				return new BPMNMonitoringPanel(panelId, monitoringPage);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Analysis")) {
			@Override
			public Panel getPanel(String panelId) {
				return new BPMNAnalysisPanel(panelId, monitoringPage);
			}
		});

		add(new BootstrapTabbedPanel<ITab>("tabs", tabs));
	}

}
