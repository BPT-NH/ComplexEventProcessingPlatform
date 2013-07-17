package sushi.application.pages.simulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.simulator.model.SimulationTreeTableProvider;
import sushi.bpmn.element.BPMNTask;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;

public abstract class SimulationPanel extends Panel{

	protected AbstractSushiPage abstractSushiPage;
	protected TextField<String> instanceNumberInput;
	protected TextField<String> daysNumberInput;
	protected Form<Void> layoutForm;
	protected SimulationPanel simulationPanel;
	protected AdvancedValuesPanel advancedValuesPanel;
	protected IndependentUnexpectedEventPanel indipendentUnexpectedEventPanel;
	protected SimulationTreeTableProvider<Object> treeTableProvider;
	
	public SimulationPanel(String id, AbstractSushiPage abstractSushiPage) {
		super(id);
		this.simulationPanel = this;
		this.abstractSushiPage = abstractSushiPage;
		this.treeTableProvider = new SimulationTreeTableProvider<Object>();
	}
	
	protected void addTabs() {
		List<ITab> tabs = new ArrayList<ITab>();
		addAdvancedValuesPanel(tabs);
		addUnexpectedEventPanel(tabs);
		addIndependentUnexpectedEventPanel(tabs);

		layoutForm.add(new Collapsible("collapsible", tabs, Model.of(-1)));
	}

	private void addAdvancedValuesPanel(List<ITab> tabs) {
		tabs.add(new AbstractTab(new Model<String>("Advanced values for attributes")) {

			public Panel getPanel(String panelId) {
				advancedValuesPanel = new AdvancedValuesPanel(panelId, simulationPanel);
				return advancedValuesPanel;
			}
		});
	}
	
	protected abstract void addUnexpectedEventPanel(List<ITab> tabs);
	
	protected void addIndependentUnexpectedEventPanel(List<ITab> tabs) {
		tabs.add(new AbstractTab(new Model<String>("Unexpected Events (instance-independent)")) {

			public Panel getPanel(String panelId) {
				indipendentUnexpectedEventPanel = new IndependentUnexpectedEventPanel(panelId);
				return indipendentUnexpectedEventPanel;
			}
		});
		
	}
	
	public List<SushiEventType> getUsedEventTypes(){
		return treeTableProvider.getEventTypes();
	}
	
	public List<BPMNTask> getTasks(){
		return treeTableProvider.getTasks();
	}
	
	public List<SushiAttribute> getAttributesFromTable() {
		return treeTableProvider.getAttributes();
	}

}
