package sushi.application.pages.simulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class SimulationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AbstractSushiPage simulationPage;

	public SimulationPage() {
		super();
		this.simulationPage = this;
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Simple")) {
			@Override
			public Panel getPanel(String panelId) {
				return new SimpleSimulationPanel(panelId, simulationPage);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("BPMN")) {
			@Override
			public Panel getPanel(String panelId) {
				return new BPMNSimulationPanel(panelId, simulationPage);
			}
		});
		
		add(new BootstrapTabbedPanel<ITab>("tabs", tabs));
	}

}
