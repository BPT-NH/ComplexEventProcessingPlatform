package sushi.application.pages.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.table.SelectEntryPanel;
import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.input.bpmn.BPMNProcessUploadPanel;
import sushi.application.pages.simulator.model.SimulationTreeTableElement;
import sushi.application.pages.simulator.model.SimulationTreeTableExpansion;
import sushi.application.pages.simulator.model.SimulationTreeTableExpansionModel;
import sushi.application.pages.simulator.model.SimulationTreeTableProvider;
import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AbstractBPMNGateway;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.process.SushiProcess;
import sushi.simulation.DerivationType;
import sushi.simulation.SimulationUtils;
import sushi.simulation.Simulator;
import sushi.util.SetUtil;
import sushi.util.Tuple;

/**
 * Panel representing the content panel for the first tab.
 */
public class BPMNSimulationPanel extends SimulationPanel {
	
	private static final long serialVersionUID = -7896431319431474548L;
//	private AbstractSushiPage abstractSushiPage;
//	private TextField<String> instanceNumberInput;
//	private TextField<String> daysNumberInput;
//	private Form<Void> layoutForm;
	private BPMNProcessUploadPanel processUploadPanel;
	private SimulationTreeTableProvider<Object> treeTableProvider;
	private SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable;
	private BPMNSimUnexpectedEventPanel unexpectedEventPanel;

	public BPMNSimulationPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id, abstractSushiPage);
		
		layoutForm = new Form("outerLayoutForm");
		
		processUploadPanel = new BPMNProcessUploadPanel("bpmnProcessUploadPanel", abstractSushiPage);
		layoutForm.add(processUploadPanel);
		
		treeTableProvider = new SimulationTreeTableProvider<Object>();
		createTreeTable(layoutForm);
		
		addTabs();
		
		instanceNumberInput = new TextField<String>("instanceNumberInput", Model.of(""));
		layoutForm.add(instanceNumberInput);
		
		daysNumberInput = new TextField<String>("daysNumberInput", Model.of(""));
		layoutForm.add(daysNumberInput);
		
		AjaxButton simulateButton = new BlockingAjaxButton("simulateButton", layoutForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				String instanceNumber = instanceNumberInput.getValue();
				int numberOfInstances;
				if(instanceNumber != null && !instanceNumber.isEmpty()){
					numberOfInstances = Integer.parseInt(instanceNumberInput.getValue());
				} else {
					numberOfInstances = 1;
				}
				SushiProcess process = SushiProcess.findByName(processUploadPanel.getSelectedProcessName()).get(0);
				BPMNProcess model = process.getBpmnProcess();
				if(model == null){
					//TODO: warning window
					System.out.println("no model found");
				}
				else{
					if(!treeTableProvider.hasEmptyFields()){
						Map<SushiAttribute, List<Serializable>> attributeValues = treeTableProvider.getAttributeValuesFromModel();
						Map<AbstractBPMNElement, String> elementDurationStrings = treeTableProvider.getBPMNElementWithDuration();
						Map<AbstractBPMNElement, DerivationType> elementDerivationTypes = treeTableProvider.getBPMNElementWithDerivationType();
						Map<AbstractBPMNElement, String> elementDerivations = treeTableProvider.getBPMNElementWithDerivation();
						Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, String>>> xorSplitsWithSuccessorProbabilityStrings = treeTableProvider.getXorSuccessorsWithProbability();
						Map<BPMNXORGateway, List<Tuple<AbstractBPMNElement, Integer>>> xorSplitsWithSuccessorProbabilities = SimulationUtils.convertProbabilityStrings(xorSplitsWithSuccessorProbabilityStrings);
						
						Simulator simulator = new Simulator(process, model, attributeValues, elementDurationStrings, elementDerivations, elementDerivationTypes, xorSplitsWithSuccessorProbabilities);
						simulator.simulate(numberOfInstances);
					} else {
						abstractSushiPage.getFeedbackPanel().error("Please fill all attribute fields.");
						target.add(abstractSushiPage.getFeedbackPanel());
					}
					
				}

			}
	    };
	    layoutForm.add(simulateButton);
		
		add(layoutForm);
		processUploadPanel.setSimulationPanel(this);
	}

	public void updateMonitoringPoints(AjaxRequestTarget target) {
		//TODO: Werteeingaben für EventTypes der Monitoringpoints ermöglichen
		BPMNProcess bpmnProcess = processUploadPanel.getProcessModel();
		List<AbstractBPMNElement> elementsWithMonitorinPoints = bpmnProcess.getBPMNElementsWithOutSequenceFlows();
		SimulationTreeTableElement<Object> taskTreeTableElement;
		SimulationTreeTableElement<Object> monitoringPointTreeTableElement;
		SimulationTreeTableElement<Object> eventTypeTreeTableElement;
		SimulationTreeTableElement<Object> attributeTreeTableElement;
		treeTableProvider.deleteAllEntries();
		for(AbstractBPMNElement bpmnElement : elementsWithMonitorinPoints){
			taskTreeTableElement = new SimulationTreeTableElement<Object>(treeTableProvider.getNextID(), bpmnElement);
			treeTableProvider.addTreeTableElement(taskTreeTableElement);
			for(MonitoringPoint monitoringPoint : bpmnElement.getMonitoringPoints()){
				if(monitoringPoint.getEventType() != null){
					monitoringPointTreeTableElement = new SimulationTreeTableElement<Object>(treeTableProvider.getNextID(), monitoringPoint);
					treeTableProvider.addTreeTableElementWithParent(monitoringPointTreeTableElement, taskTreeTableElement);
					eventTypeTreeTableElement = new SimulationTreeTableElement<Object>(treeTableProvider.getNextID(), monitoringPoint.getEventType());
					treeTableProvider.addTreeTableElementWithParent(eventTypeTreeTableElement, monitoringPointTreeTableElement);
					for(SushiAttribute attribute : monitoringPoint.getEventType().getValueTypes()){
						attributeTreeTableElement = new SimulationTreeTableElement<Object>(treeTableProvider.getNextID(), attribute);
						treeTableProvider.addTreeTableElementWithParent(attributeTreeTableElement, eventTypeTreeTableElement);
					}
				} else {
					treeTableProvider.deleteAllEntries();
					abstractSushiPage.getFeedbackPanel().error("Monitoring point with no matching event type!");
					if(target != null){
						target.add(treeTable);
					}
					return;
				}
				
			}
		}
		treeTableProvider.setCorrelationAttributes(processUploadPanel.getProcess().getCorrelationAttributes());
		if(target != null){
			target.add(abstractSushiPage.getFeedbackPanel());
		}
	}
	
	private void createTreeTable(Form<Void> layoutForm) {
		List<IColumn<SimulationTreeTableElement<Object>, String>> columns = createColumns();
		
		treeTable = new SushiLabelTreeTable<SimulationTreeTableElement<Object>, String>(
					"monitoringPointTree", 
					columns, 
					treeTableProvider, 
					Integer.MAX_VALUE, 
					new SimulationTreeTableExpansionModel<Object>());
		
		treeTable.setOutputMarkupId(true);

		treeTable.getTable().addTopToolbar(new HeadersToolbar<String>(treeTable.getTable(), treeTableProvider));
		
		SimulationTreeTableExpansion.get().expandAll();
		
		layoutForm.add(treeTable);
	}
	
	private List<IColumn<SimulationTreeTableElement<Object>, String>> createColumns() {
		List<IColumn<SimulationTreeTableElement<Object>, String>> columns = new ArrayList<IColumn<SimulationTreeTableElement<Object>, String>>();
    
		columns.add(new PropertyColumn<SimulationTreeTableElement<Object>, String>(Model.of("ID"), "ID"));
		columns.add(new TreeColumn<SimulationTreeTableElement<Object>, String>(Model.of("Sequence"), "content"));
		
		columns.add(new AbstractColumn(new Model("Probability")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				int entryId = ((SimulationTreeTableElement<Object>) rowModel.getObject()).getID();
				Object content = ((SimulationTreeTableElement<Object>) rowModel.getObject()).getContent();
				if(content instanceof AbstractBPMNElement && SetUtil.containsXorSplit(((AbstractBPMNElement) content).getPredecessors())){
					
					ProbabilityEntryPanel probabilityEntryPanel = new ProbabilityEntryPanel(componentId, entryId, treeTableProvider);
					cellItem.add(probabilityEntryPanel);
					probabilityEntryPanel.setTable(treeTable);
				}
				else{
					cellItem.add(new EmptyPanel(componentId, entryId, treeTableProvider));
				}
			}
		});
		
		columns.add(new AbstractColumn(new Model("Value")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				int entryId = ((SimulationTreeTableElement<Object>) rowModel.getObject()).getID();
				if(((SimulationTreeTableElement<Object>) rowModel.getObject()).editableColumnsVisible()){
					TextFieldEntryPanel textFieldEntryPanel = new TextFieldEntryPanel(componentId, entryId, treeTableProvider);
					cellItem.add(textFieldEntryPanel);
					textFieldEntryPanel.setTable(treeTable);
				}
				else{
					cellItem.add(new EmptyPanel(componentId, entryId, treeTableProvider));
				}
			}
		});
		
		columns.add(new AbstractColumn(new Model("Derivation-Type")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				int entryId = ((SimulationTreeTableElement<Object>) rowModel.getObject()).getID();
				if(((SimulationTreeTableElement<Object>) rowModel.getObject()).getContent() instanceof AbstractBPMNElement){
					DerivationTypeDropDownChoicePanel derivationChoicePanel = new DerivationTypeDropDownChoicePanel(componentId, entryId, treeTableProvider);
					cellItem.add(derivationChoicePanel);
					derivationChoicePanel.setTable(treeTable);
				}
				else{
					cellItem.add(new EmptyPanel(componentId, entryId, treeTableProvider));
				}
			}
		});
		
		columns.add(new AbstractColumn(new Model("Duration")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				int entryId = ((SimulationTreeTableElement<Object>) rowModel.getObject()).getID();
				if(((SimulationTreeTableElement<Object>) rowModel.getObject()).getContent() instanceof AbstractBPMNElement){
					DurationEntryPanel durationEntryPanel = new DurationEntryPanel(componentId, entryId, treeTableProvider);
					cellItem.add(durationEntryPanel);
					durationEntryPanel.setTable(treeTable);
				}
				else{
					cellItem.add(new EmptyPanel(componentId, entryId, treeTableProvider));
				}
			}
		});

		return columns;
	}
	
	public Component getMonitoringPointTable() {
		return treeTable;
	}

	@Override
	protected void addUnexpectedEventPanel(List<ITab> tabs) {
		tabs.add(new AbstractTab(new Model<String>("Unexpected Events (instance-dependent)")) {

			public Panel getPanel(String panelId) {
				unexpectedEventPanel = new BPMNSimUnexpectedEventPanel(panelId, simulationPanel);
				return unexpectedEventPanel;
			}
		});
		
	}
};
