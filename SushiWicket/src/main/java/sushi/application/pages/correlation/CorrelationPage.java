package sushi.application.pages.correlation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.process.modal.ProcessEditorModal;
import sushi.correlation.AttributeCorrelator;
import sushi.correlation.CorrelationRule;
import sushi.correlation.RuleCorrelator;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

@SuppressWarnings("serial")
public class CorrelationPage extends AbstractSushiPage {
	
	private SimpleCorrelationPanel simpleCorrelationPanel;
	private SimpleCorrelationWithRulesPanel simpleCorrelationWithRulesPanel;
	private boolean simpleCorrelationWithRules = false;
	private AdvancedCorrelationPanel advancedCorrelationPanel;
	private final DropDownChoice<String> processSelect;
	private ProcessEditorModal processEditorModal;
	private ArrayList<String> processNameList;
	private ExistingCorrelationAlert existingCorrelationAlert;
	private CorrelationPage correlationPage;
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
		
		final RadioChoice<String> simpleCorrelationWithRulesRadioChoice = new RadioChoice<String>("simpleCorrelationWithRulesRadioChoice", new Model<String>(), Arrays.asList("same-name attributes", "correlation rules"));
		simpleCorrelationWithRulesRadioChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				simpleCorrelationWithRules = simpleCorrelationWithRulesRadioChoice.getModelObject().equals("correlation rules");
				updateSimpleCorrelationPanelComponents(target);
				updateSimpleCorrelationWithRulesPanelComponents(target);
			}
		});
		simpleCorrelationWithRulesRadioChoice.setModelObject("same-name attributes");
		layoutForm.add(simpleCorrelationWithRulesRadioChoice);
		
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
		BlockingAjaxButton applyButton = new BlockingAjaxButton("applyButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				if ((simpleCorrelationWithRules && isSimpleCorrelationWithRulesPanelFilled()) || (!simpleCorrelationWithRules && isSimpleCorrelationPanelFilled())
					&& isAdvancedCorrelationPanelFilled()) {
					addEventTypesToProcessAndCorrelate(target);
				}
				target.add(correlationPage.getFeedbackPanel());
			}
	    };
	    
	    layoutForm.add(applyButton);
	}
	
	private boolean isSimpleCorrelationPanelFilled() {
		if (processSelect.getValue().isEmpty()) {
			correlationPage.getFeedbackPanel().error("No process selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		if (simpleCorrelationPanel.getCorrelationEventTypes().isEmpty()) {
			correlationPage.getFeedbackPanel().error("No event types for correlation selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		if (simpleCorrelationPanel.getSelectedCorrelationAttributes().isEmpty()) {
			correlationPage.getFeedbackPanel().error("No correlation attributes selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		return true;
	}
	
	private boolean isSimpleCorrelationWithRulesPanelFilled() {
		if (processSelect.getValue().isEmpty()) {
			correlationPage.getFeedbackPanel().error("No process selected!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		}
		if (simpleCorrelationWithRulesPanel.getCorrelationRules().isEmpty()) {
			correlationPage.getFeedbackPanel().error("No correlation rules provided!");
			correlationPage.getFeedbackPanel().setVisible(true);
			return false;
		} else {
			for (CorrelationRule correlationRule : simpleCorrelationWithRulesPanel.getCorrelationRules()) {
				if (correlationRule.getFirstAttribute() == null || correlationRule.getSecondAttribute() == null) {
					correlationPage.getFeedbackPanel().error("Some of the correlation rules are missing attributes!");
					correlationPage.getFeedbackPanel().setVisible(true);
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isAdvancedCorrelationPanelFilled() {
		if (advancedCorrelationPanel.isTimeCorrelationSelected()) {
			if (advancedCorrelationPanel.getTimeCondition().getSelectedEventType() == null) {
				correlationPage.getFeedbackPanel().error("No event type for time correlation selected!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
			if (advancedCorrelationPanel.getTimeCondition().getTimePeriod() == 0) {
				correlationPage.getFeedbackPanel().error("No minutes for time correlation inserted!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
			if (advancedCorrelationPanel.getTimeCondition().getConditionString().isEmpty()) {
				correlationPage.getFeedbackPanel().error("No condition for time correlation inserted!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
			if (advancedCorrelationPanel.getTimeCondition().getConditionString().startsWith("=") || advancedCorrelationPanel.getTimeCondition().getConditionString().endsWith("=")) {
				correlationPage.getFeedbackPanel().error("Malformed condition for time correlation!");
				correlationPage.getFeedbackPanel().setVisible(true);
				return false;
			}
		}
		return true;
	}
	
	public void clearAdvancedCorrelationPanelComponents() {
		correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationEventTypeSelect().setChoices(new ArrayList<SushiEventType>());
		correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionAttributeSelect().setChoices(new ArrayList<String>());
		correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionValueSelect().setChoices(new ArrayList<Serializable>());
	}

	public void updateAdvancedCorrelationPanelComponents(AjaxRequestTarget target) {
		target.add(correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationEventTypeSelect());
		target.add(correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionAttributeSelect());
		target.add(correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionValueSelect());
	}

	private void addCorrelationTabs() {
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Simple correlation with same-name attributes")) {
			public Panel getPanel(String panelId) {
				simpleCorrelationPanel = new SimpleCorrelationPanel(panelId, correlationPage);
				return simpleCorrelationPanel;
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Simple correlation with correlation rules")) {
			public Panel getPanel(String panelId) {
				simpleCorrelationWithRulesPanel = new SimpleCorrelationWithRulesPanel(panelId, correlationPage);
				return simpleCorrelationWithRulesPanel;
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Advanced correlation")) {
			public Panel getPanel(String panelId) {
				advancedCorrelationPanel = new AdvancedCorrelationPanel(panelId);
				return advancedCorrelationPanel;
			}
		});

		layoutForm.add(new Collapsible("collapsible", tabs, Model.of(-1)));
	}
	
	private void addEventTypesToProcessAndCorrelate(AjaxRequestTarget target) {
		SushiProcess selectedProcess = SushiProcess.findByName(processSelect.getChoices().get(Integer.parseInt(processSelect.getValue()))).get(0);
		addEventTypesToSelectedProcess(selectedProcess);
		
		tryToCorrelateEvents(selectedProcess, target);
	}
	
	private void addEventTypesToSelectedProcess(SushiProcess selectedProcess) {
		if (simpleCorrelationWithRules) {
			selectedProcess.setEventTypes(simpleCorrelationWithRulesPanel.getCorrelationEventTypes());
		} else {
			selectedProcess.setEventTypes(simpleCorrelationPanel.getCorrelationEventTypes());
		}
		selectedProcess.merge();
	}
	
	/**
	 * If correlation settings have been already determined for a process, 
	 * the user is asked if the correlation settings shall be overwritten.
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
	
	public void correlateEvents(SushiProcess selectedProcess) {
		List<SushiAttribute> correlationAttributes = simpleCorrelationPanel.getSelectedCorrelationAttributes();
		Set<CorrelationRule> correlationRules = new HashSet<CorrelationRule>(simpleCorrelationWithRulesPanel.getCorrelationRules());
		if (simpleCorrelationWithRules) {
			if (advancedCorrelationPanel.isTimeCorrelationSelected()) {
				RuleCorrelator.correlate(correlationRules, selectedProcess, advancedCorrelationPanel.getTimeCondition());
			} else {
				RuleCorrelator.correlate(correlationRules, selectedProcess, null);
			}
		} else {
			if (advancedCorrelationPanel.isTimeCorrelationSelected()) {
				AttributeCorrelator.correlate(selectedProcess.getEventTypes(), correlationAttributes, selectedProcess, advancedCorrelationPanel.getTimeCondition());
			} else {
				AttributeCorrelator.correlate(selectedProcess.getEventTypes(), correlationAttributes, selectedProcess, null);
			}
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

	public SimpleCorrelationPanel getSimpleCorrelationPanel() {
		return simpleCorrelationPanel;
	}

	public SimpleCorrelationWithRulesPanel getSimpleCorrelationWithRulesPanel() {
		return simpleCorrelationWithRulesPanel;
	}

	public AdvancedCorrelationPanel getAdvancedCorrelationPanel() {
		return advancedCorrelationPanel;
	}

	protected void updateSimpleCorrelationPanelComponents(AjaxRequestTarget target) {
		target.add(simpleCorrelationPanel.getCorrelationAttributesSelect());
		target.add(simpleCorrelationPanel.getEventTypesCheckBoxMultipleChoice());
	}
	
	protected void updateSimpleCorrelationWithRulesPanelComponents(AjaxRequestTarget target) {
		target.add(simpleCorrelationWithRulesPanel.getAddCorrelationRuleButton());
		target.add(simpleCorrelationWithRulesPanel.getCorrelationRuleMarkupContainer());
	}

	public void setValuesOfAdvancedCorrelationPanelComponents(ArrayList<SushiEventType> eventTypes) {
		correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationEventTypeSelect().setChoices(eventTypes);
		if (eventTypes.isEmpty()) {
			correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionAttributeSelect().setChoices(new ArrayList<String>());
			correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionValueSelect().setChoices(new ArrayList<Serializable>());
		} else {
			correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionAttributeSelect().setChoices(eventTypes.get(0).getAttributeExpressions());
			correlationPage.getAdvancedCorrelationPanel().getTimeCorrelationConditionValueSelect().setChoices(eventTypes.get(0).findAttributeValues(eventTypes.get(0).getValueTypes().get(0).getAttributeExpression()));
		}
	}

	public boolean isSimpleCorrelationWithRules() {
		return simpleCorrelationWithRules;
	}

}
