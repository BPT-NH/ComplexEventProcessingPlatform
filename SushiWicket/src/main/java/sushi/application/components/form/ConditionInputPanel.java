package sushi.application.components.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.event.SushiEventType;
import sushi.notification.SushiCondition;

/**
 * This Component can be used to let the user define a condition.
 * It contains an AttributeSelect and a ValueSelect. At the moment only "=" is possible, but this could be extended.
 * Correspond with this Component by setting the attribute "selectedEventTypes". This will update the attributes and values.
 */

public class ConditionInputPanel extends Panel {

	private DropDownChoice<String> conditionAttributeSelect;
	private DropDownChoice<Serializable> conditionValueSelect;
	private String selectedConditionAttribute;
	private List<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
	private String selectedConditionValue;
	private TextField<String> conditionValueTextField;
	private Label conditionValueLabel;
	
	public ConditionInputPanel(String id, boolean isConditionValueTextFieldVisible) {
		super(id);
		
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		
		conditionAttributeSelect = new DropDownChoice<String>("conditionAttributeSelect", new PropertyModel<String>(this, "selectedConditionAttribute"), new ArrayList<String>());
		conditionAttributeSelect.setOutputMarkupId(true);
		conditionAttributeSelect.add(new AjaxFormComponentUpdatingBehavior("onChange"){ 
	
			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				//collect all attributes
				Set<Serializable> attributes = new HashSet<Serializable>();
				for (SushiEventType eventType : selectedEventTypes) {
					attributes.addAll(eventType.findAttributeValues(selectedConditionAttribute));
				}
				ArrayList<Serializable> choices = new ArrayList<Serializable>();
				choices.add(null);
				choices.addAll(attributes);
				conditionValueSelect.setChoices(choices);
				target.add(conditionValueSelect);
			}
		});
		layoutForm.add(conditionAttributeSelect);
		
		conditionValueSelect = new DropDownChoice<Serializable>("conditionValueSelect", new PropertyModel<Serializable>(this, "selectedConditionValue"), new ArrayList<Serializable>());
		conditionValueSelect.setOutputMarkupId(true);
		conditionValueSelect.add(new AjaxFormComponentUpdatingBehavior("onChange"){ 
	
			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(conditionValueSelect);
				if (selectedConditionValue != null && !selectedConditionValue.isEmpty()) {
					conditionValueTextField.setEnabled(false);
				} else {
					conditionValueTextField.setEnabled(true);
				}
				target.add(conditionValueTextField);
			}
		});
		
		layoutForm.add(conditionValueSelect);
		
		conditionValueLabel = new Label("conditionValueLabel", "or");
		conditionValueLabel.setVisible(isConditionValueTextFieldVisible);
		layoutForm.add(conditionValueLabel);
		
		conditionValueTextField = new TextField<String>("conditionValueTextField", new Model<String>());
		conditionValueTextField.setOutputMarkupId(true);
		
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -5737941362786901904L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (conditionValueTextField.getModelObject() != null && !conditionValueTextField.getModelObject().isEmpty()) {
					conditionValueSelect.setEnabled(false);
				} else {
					conditionValueSelect.setEnabled(true);
				}
				target.add(conditionValueSelect);
			}
        };
        conditionValueTextField.add(onChangeAjaxBehavior);
        conditionValueTextField.setVisible(isConditionValueTextFieldVisible);
		layoutForm.add(conditionValueTextField);	
	}

	public DropDownChoice<String> getConditionAttributeSelect() {
		return conditionAttributeSelect;
	}

	public void setConditionAttributeSelect(DropDownChoice<String> conditionAttributeSelect) {
		this.conditionAttributeSelect = conditionAttributeSelect;
	}

	public DropDownChoice<Serializable> getConditionValueSelect() {
		return conditionValueSelect;
	}

	public void setConditionValueSelect(DropDownChoice<Serializable> conditionValueSelect) {
		this.conditionValueSelect = conditionValueSelect;
	}

	public String getSelectedConditionAttribute() {
		return selectedConditionAttribute;
	}

	public void setSelectedConditionAttribute(String selectedConditionAttribute) {
		this.selectedConditionAttribute = selectedConditionAttribute;
	}

	public List<SushiEventType> getSelectedEventTypes() {
		return selectedEventTypes;
	}

	public void setSelectedEventTypes(List<SushiEventType> selectedEventTypes) {
		this.selectedEventTypes = selectedEventTypes;
		updateAttributesValues();
	}

	public void addSelectedEventTypes(List<SushiEventType> selectedEventTypes) {
		this.selectedEventTypes = selectedEventTypes;
		updateAttributesValues();
	}

	public void addSelectedEventType(SushiEventType selectedEventType) {
		this.selectedEventTypes.add(selectedEventType);
		updateAttributesValues();
	}
	
	public void clearSelectedEventType() {
		this.selectedEventTypes.clear();
	}
	
	public String getSelectedConditionValue() {
		return selectedConditionValue;
	}

	public void setSelectedConditionValue(String selectedConditionValue) {
		this.selectedConditionValue = selectedConditionValue;
	}

	public SushiCondition getCondition() {
		if (conditionValueSelect.isEnabled()) {
			return new SushiCondition(selectedConditionAttribute, selectedConditionValue);
		} else if (conditionValueTextField.isEnabled()) {
			return new SushiCondition(selectedConditionAttribute, conditionValueTextField.getModelObject());
		}
		return new SushiCondition();
	}

	public void updateAttributesValues() {
		Set<String> attributes = new HashSet();
		Set<String> attributeValues = new HashSet();
		
		for (SushiEventType eventType : selectedEventTypes) {
			List<String> newAttributes = eventType.getAttributeKeysFromMap();
			//update attributes
			attributes.addAll(newAttributes);
			//update values
			for (String attribute : newAttributes) {
				for (Serializable value : eventType.findAttributeValues(attribute)) {
					attributeValues.add(value.toString());
				}
			}
		}
		conditionAttributeSelect.setChoices(new ArrayList(attributes));
		conditionValueSelect.setChoices(new ArrayList(attributeValues));
	}

	public void enableAllComponents(AjaxRequestTarget target) {
		conditionAttributeSelect.setEnabled(true);
		conditionValueSelect.setEnabled(true);
		conditionValueTextField.setEnabled(true);
		updateAllComponents(target);
	}

	public void disableAllComponents(AjaxRequestTarget target) {
		conditionAttributeSelect.setEnabled(false);
		conditionValueSelect.setEnabled(false);
		conditionValueTextField.setEnabled(false);
		updateAllComponents(target);
	}
	
	private void updateAllComponents(AjaxRequestTarget target) {
		target.add(conditionAttributeSelect);
		target.add(conditionValueSelect);
		target.add(conditionValueTextField);
	}
}
