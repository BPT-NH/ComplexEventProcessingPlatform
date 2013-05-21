package sushi.application.pages.correlation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.process.modal.ProcessEditorModal;
import sushi.correlation.Correlator;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

@SuppressWarnings("serial")
public class CorrelationPage extends AbstractSushiPage {
	
	private SimpleCorrelationPanel simpleCorrelationPanel;
	private AdvancedCorrelationPanel advancedCorrelationPanel;
	private final DropDownChoice<String> processSelect;
	private ArrayList<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
	private ProcessEditorModal processEditorModal;
	private ArrayList<String> processNameList;
	private ExistingCorrelationAlert existingCorrelationAlert;
	private CorrelationPage correlationPage;
	private CheckBoxMultipleChoice<SushiEventType> eventTypesCheckBoxMultipleChoice;
	protected ArrayList<SushiAttribute> commonCorrelationAttributes = new ArrayList<SushiAttribute>();
	private Form<Void> layoutForm;
	
	public CorrelationPage() {
		super();
		this.correlationPage = this;

		processNameList = new ArrayList<String>();
		for (SushiProcess process : SushiProcess.findAll()) {
			processNameList.add(process.getName());
		}
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);

		processSelect = new DropDownChoice<String>("processSelect", new Model<String>(), processNameList);
		processSelect.setOutputMarkupId(true);
		layoutForm.add(processSelect);
		
		addEventTypeCheckBoxMultipleChoice(layoutForm);
//		addEventTypePaletteSelection(layoutForm);
		
		addApplyButton(layoutForm);

		addCorrelationTabs();
		
		addProcessEditorModal();
		
		existingCorrelationAlert = new ExistingCorrelationAlert("warning", "Correlation exists! Do you want to override it?", this);
		existingCorrelationAlert.setVisible(false);
		existingCorrelationAlert.setOutputMarkupId(true);
		existingCorrelationAlert.setOutputMarkupPlaceholderTag(true);
		add(existingCorrelationAlert);
	}

	private void addProcessEditorModal() {
        processEditorModal = new ProcessEditorModal("processEditorModal", processSelect);
		add(processEditorModal);

		layoutForm.add(new AjaxLink<Void>("showProcessEditModal"){

		private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target){
					processEditorModal.show(target);
				}
  		});
		
	}

	private void addApplyButton(Form<Void> layoutForm) {
		BlockingAjaxButton applyButton= new BlockingAjaxButton("applyButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				if(isSimpleCorrelationPanelFilled() && isAdvancedCorrelationPanelFilled()){
					addEventTypesToProcessAndCorrelate(target);
				}
				target.add(correlationPage.getFeedbackPanel());
			}
	    };
	    
	    layoutForm.add(applyButton);
	}
	
	private boolean isSimpleCorrelationPanelFilled() {
		if(processSelect.getValue().isEmpty()){
			correlationPage.getFeedbackPanel().error("No process selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		if(selectedEventTypes.isEmpty()){
			correlationPage.getFeedbackPanel().error("No event types selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		if(simpleCorrelationPanel.getSelectedCorrelationAttributes().isEmpty()){
			correlationPage.getFeedbackPanel().error("No correlation attributes selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		return true;
	}
	
	private boolean isAdvancedCorrelationPanelFilled() {
		if(advancedCorrelationPanel.isTimeCorrelationSelected()){
			if(advancedCorrelationPanel.getTimeCondition().getSelectedEventType() == null){
				correlationPage.getFeedbackPanel().error("No event type for time correlation selected!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
			if(advancedCorrelationPanel.getTimeCondition().getTimePeriod() == 0){
				correlationPage.getFeedbackPanel().error("No minutes for time correlation inserted!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
			if(advancedCorrelationPanel.getTimeCondition().getConditionString().isEmpty()){
				correlationPage.getFeedbackPanel().error("No condition for time correlation inserted!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
		}
		return true;
	}
	
	private void addEventTypeCheckBoxMultipleChoice(Form layoutForm) {
        
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		
		eventTypesCheckBoxMultipleChoice = new CheckBoxMultipleChoice<SushiEventType>("eventTypesCheckBoxMultipleChoice", new PropertyModel<ArrayList<SushiEventType>>(this, "selectedEventTypes"), eventTypes) {
			@Override
		 	protected boolean isDisabled(SushiEventType eventType, int index, String selected) {
				// true for event types without matching attributes
				if (selectedEventTypes.isEmpty()) {
					return false;
				} else {
					for (SushiAttribute commonAttribute : commonCorrelationAttributes) {
						/*
						 *  eventType.getValueTypes().contains(commonAttribute) is not sufficient 
						 *  because equality does not consider attribute type
						 */
						for (SushiAttribute attributeOfEventType : eventType.getValueTypes()) {
							if (attributeOfEventType.equals(commonAttribute) && (attributeOfEventType.getType() == commonAttribute.getType())) {
								return false;
							}
						}
					}
					return true;
				}
			}
		};
		eventTypesCheckBoxMultipleChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				commonCorrelationAttributes.clear();
				clearPanelComponents();
				if (!selectedEventTypes.isEmpty()) {
					// advanced correlation - time
					advancedCorrelationPanel.getTimeCorrelationEventTypeSelect().setChoices(selectedEventTypes);
					// simple correlation
					commonCorrelationAttributes.addAll(selectedEventTypes.get(0).getValueTypes());
					for (SushiEventType actualEventType : selectedEventTypes) {
						commonCorrelationAttributes.retainAll(actualEventType.getValueTypes());
					}
					simpleCorrelationPanel.getCorrelationAttributesSelect().setChoices(commonCorrelationAttributes);
					advancedCorrelationPanel.getTimeCorrelationConditionAttributeSelect().setChoices(selectedEventTypes.get(0).getAttributeExpressions());
					advancedCorrelationPanel.getTimeCorrelationConditionValueSelect().setChoices(selectedEventTypes.get(0).findAttributeValues(selectedEventTypes.get(0).getValueTypes().get(0).getAttributeExpression()));
				}
				updatePanelComponents(target);
				target.add(eventTypesCheckBoxMultipleChoice);
			}
		});
		eventTypesCheckBoxMultipleChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypesCheckBoxMultipleChoice);
	}

	private void clearPanelComponents() {
		advancedCorrelationPanel.getTimeCorrelationEventTypeSelect().setChoices(new ArrayList<SushiEventType>());
		simpleCorrelationPanel.clearCorrelationAttributesSelect();
		advancedCorrelationPanel.getTimeCorrelationConditionAttributeSelect().setChoices(new ArrayList<String>());
		advancedCorrelationPanel.getTimeCorrelationConditionValueSelect().setChoices(new ArrayList<Serializable>());
	}

	private void updatePanelComponents(AjaxRequestTarget target) {
		target.add(simpleCorrelationPanel.getCorrelationAttributesSelect());
		target.add(advancedCorrelationPanel.getTimeCorrelationEventTypeSelect());
		target.add(advancedCorrelationPanel.getTimeCorrelationConditionAttributeSelect());
		target.add(advancedCorrelationPanel.getTimeCorrelationConditionValueSelect());
	}

	private void addCorrelationTabs() {
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Simple correlation")) {
			public Panel getPanel(String panelId) {
				simpleCorrelationPanel = new SimpleCorrelationPanel(panelId);
				return simpleCorrelationPanel;
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Advanced correlation")) {
			public Panel getPanel(String panelId) {
				advancedCorrelationPanel = new AdvancedCorrelationPanel(panelId);
				return advancedCorrelationPanel;
			}
		});

		add(new Collapsible("collapsible", tabs, Model.of(-1)));
	}
	
	private void addEventTypesToProcessAndCorrelate(AjaxRequestTarget target) {
		SushiProcess selectedProcess = SushiProcess.findByName(processSelect.getChoices().get(Integer.parseInt(processSelect.getValue()))).get(0);
		addEventTypesToSelectedProcess(selectedProcess);
		
		tryToCorrelateEvents(selectedProcess, target);
	}
	
	private void addEventTypesToSelectedProcess(SushiProcess selectedProcess) {
		selectedProcess.setEventTypes(new HashSet<SushiEventType>(selectedEventTypes));
		selectedProcess.merge();
	}
	
	/**
	 * Prüfung, ob bereits eine Korrelation existiert und 
	 * Abfrage, ob bestehende Korrelation gelöscht werden soll.
	 * Falls ja, bestehende Korrelation löschen und korrelieren.
	 */
	private void tryToCorrelateEvents(SushiProcess selectedProcess, AjaxRequestTarget target){
		if(SushiProcessInstance.findByProcess(selectedProcess).size() > 0){
			showCorrelationExistsWarningModal(selectedProcess, target);
		} else{
			correlateEvents(selectedProcess);
		}
	}
	
	private void showCorrelationExistsWarningModal(SushiProcess selectedProcess, AjaxRequestTarget target) {
		existingCorrelationAlert.setVisible(true);
		existingCorrelationAlert.setSelectedProcess(selectedProcess);
		target.add(existingCorrelationAlert);
		System.out.println("Correlate existing");
	}
	
	public void correlateEvents(SushiProcess selectedProcess){
		List<SushiAttribute> correlationAttributes = simpleCorrelationPanel.getSelectedCorrelationAttributes();
		if(advancedCorrelationPanel.isTimeCorrelationSelected()){
			Correlator.correlate(selectedEventTypes, correlationAttributes, selectedProcess, advancedCorrelationPanel.getTimeCondition());
		}
		else{
			Correlator.correlate(selectedEventTypes, correlationAttributes, selectedProcess, null);
		}
		correlationPage.getFeedbackPanel().success("Correlation finished! " + SushiProcessInstance.findByProcess(selectedProcess).size() + " process instances created!");
		correlationPage.getFeedbackPanel().setVisible(true);
	}

	public DropDownChoice<String> getProcessSelect() {
		return processSelect;
	}
	
	public void addProcessToProcessNameList(String processName){
		this.processNameList.add(processName);
	}
	
	public void removeProcessFromProcessNameList(String processName){
		this.processNameList.remove(processName);
	}

	public ExistingCorrelationAlert getAlert() {
		return existingCorrelationAlert;
	}

}
