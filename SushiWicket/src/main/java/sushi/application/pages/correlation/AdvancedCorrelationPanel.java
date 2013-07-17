package sushi.application.pages.correlation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import sushi.application.components.form.ConditionInputPanel;
import sushi.application.components.form.WarnOnExitForm;
import sushi.correlation.TimeCondition;
import sushi.event.SushiEventType;

/**
 * Panel representing the content panel for the first tab.
 */
public class AdvancedCorrelationPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private List<String> timeCorrelationRadioValues = new ArrayList<String>(Arrays.asList("after","before"));
	private String selectedTimeRadioOption = timeCorrelationRadioValues.get(0);
	private final AjaxCheckBox timeCorrelationCheckBox;
	private boolean timeCorrelationSelected = false;
	private String timeCorrelationMinutes = new String();
	private SushiEventType selectedEventType;
	private final TextField<String> timeCorrelationMinutesInput;
	private final DropDownChoice<SushiEventType> timeCorrelationEventTypeSelect;
	private final RadioChoice<String> timeCorrelationAfterOrBeforeType;
	private ConditionInputPanel conditionPanel;
	private TextField<String> multipleConditionsTextField;
	private static final ResourceReference Label_CSS = new PackageResourceReference(AdvancedCorrelationPanel.class, "label.css");
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(Label_CSS));
	}
	
	public AdvancedCorrelationPanel(String id) {
		super(id);
		
		Form<Void> advancedCorrelationForm = new WarnOnExitForm("advancedCorrelationForm");
		add(advancedCorrelationForm);
		
		timeCorrelationCheckBox = new AjaxCheckBox("timeCheckBox", Model.of(Boolean.FALSE)) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				timeCorrelationSelected = timeCorrelationSelected ? false : true;
			}
		};
		timeCorrelationCheckBox.setOutputMarkupId(true);
		advancedCorrelationForm.add(timeCorrelationCheckBox);
		
		timeCorrelationEventTypeSelect = new DropDownChoice<SushiEventType>("timeEventTypeSelect", new PropertyModel<SushiEventType>(this, "selectedEventType"), new ArrayList<SushiEventType>());
		timeCorrelationEventTypeSelect.setOutputMarkupId(true);
		timeCorrelationEventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onChange"){ 

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				if (selectedEventType != null) {
					conditionPanel.setSelectedEventTypes(Arrays.asList((selectedEventType)));
					conditionPanel.updateAttributesValues();
				} else {
					conditionPanel.getConditionAttributeSelect().setChoices(new ArrayList<String>());
					conditionPanel.getConditionValueSelect().setChoices(new ArrayList<Serializable>());
				}
				target.add(conditionPanel.getConditionAttributeSelect());
				target.add(conditionPanel.getConditionValueSelect());
			}
		});
		advancedCorrelationForm.add(timeCorrelationEventTypeSelect);
		
		timeCorrelationMinutesInput = new TextField<String>("timeMinutesInput", Model.of(""));
		timeCorrelationMinutesInput.setOutputMarkupId(true);
		timeCorrelationMinutesInput.add(new AjaxFormComponentUpdatingBehavior("onChange"){ 

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				timeCorrelationMinutes = timeCorrelationMinutesInput.getValue();
			}
		});
		advancedCorrelationForm.add(timeCorrelationMinutesInput);
		
		timeCorrelationAfterOrBeforeType = new RadioChoice<String>("afterOrBeforeRadioGroup", new PropertyModel<String>(this, "selectedTimeRadioOption"), timeCorrelationRadioValues) {
			public String getSuffix() { 
				return "&nbsp;&nbsp;&nbsp;"; 
			}
		};
		advancedCorrelationForm.add(timeCorrelationAfterOrBeforeType);
		
		conditionPanel = new ConditionInputPanel("conditionInput", true);
		advancedCorrelationForm.add(conditionPanel);
		
		multipleConditionsTextField = new TextField<String>("multipleConditionsTextField", new Model<String>());
		multipleConditionsTextField.setOutputMarkupId(true);
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -5737941362786901904L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (isMultipleConditionsTextFieldFilled()) {
					conditionPanel.disableAllComponents(target);
				} else {
					conditionPanel.enableAllComponents(target);
				}
			}
        };
        multipleConditionsTextField.add(onChangeAjaxBehavior);
		advancedCorrelationForm.add(multipleConditionsTextField);
	}
	
	private boolean isMultipleConditionsTextFieldFilled() {
		return multipleConditionsTextField.getModelObject() != null && !multipleConditionsTextField.getModelObject().isEmpty();
	}

	public String getSelectedTimeRadioOption() {
		return selectedTimeRadioOption;
	}

	public void setSelectedTimeRadioOption(String selectedTimeRadioOption) {
		this.selectedTimeRadioOption = selectedTimeRadioOption;
	}

	public TextField<String> getMultipleConditionsTextField() {
		return multipleConditionsTextField;
	}

	public void setMultipleConditionsTextField(
			TextField<String> multipleConditionsTextField) {
		this.multipleConditionsTextField = multipleConditionsTextField;
	}

	public CheckBox getTimeCorrelationCheckBox() {
		return timeCorrelationCheckBox;
	}

	public TextField<String> getTimeCorrelationMinutesInput() {
		return timeCorrelationMinutesInput;
	}

	public DropDownChoice<SushiEventType> getTimeCorrelationEventTypeSelect() {
		return timeCorrelationEventTypeSelect;
	}

	public RadioChoice<String> getTimeCorrelationAfterOrBeforeType() {
		return timeCorrelationAfterOrBeforeType;
	}

	public TimeCondition getTimeCondition() {
		int minutes;
		String conditionString;
		if(!timeCorrelationMinutes.isEmpty()){
			minutes = Integer.valueOf(timeCorrelationMinutes);
		} else {
			minutes = 0;
		}
		
		boolean isAfter = selectedTimeRadioOption.equals("after") ? true : false;
		if (isMultipleConditionsTextFieldFilled()) {
			conditionString = multipleConditionsTextField.getModelObject();
		} else {
			conditionString = conditionPanel.getCondition().getConditionString();
		}
//		System.out.println(conditionString);
		TimeCondition timeCondition = new TimeCondition(selectedEventType, minutes, isAfter, conditionString);
		return timeCondition;
	}

	public boolean isTimeCorrelationSelected() {
		return timeCorrelationSelected;
	}

	public DropDownChoice<String> getTimeCorrelationConditionAttributeSelect() {
		return conditionPanel.getConditionAttributeSelect();
	}

	public DropDownChoice<Serializable> getTimeCorrelationConditionValueSelect() {
		return conditionPanel.getConditionValueSelect();
	}
	
};
