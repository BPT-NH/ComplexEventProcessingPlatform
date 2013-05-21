package sushi.application.components.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import sushi.event.SushiEventType;
import sushi.notification.SushiCondition;

/*
 * This Component can be used to let the user define a condition.
 * It contains an AttributeSelect and a ValueSelect. At the moment only "=" is possible, but this could be extended.
 * Correspond with this Component by setting the attribute "selectedEventTypes". This will update the attributes and values.
 */

public class FlexConditionInputPanel extends Panel{

	private DropDownChoice<String> conditionAttributeSelect;
	private TextField conditionValueInput;
	private String selectedConditionAttribute;
	private List<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
	private String selectedConditionValue;
	
	public FlexConditionInputPanel(String id) {
		super(id);
		
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
		}
	});
	add(conditionAttributeSelect);
	
	conditionValueInput = new TextField("conditionValueInput", new PropertyModel<String>(this, "selectedConditionValue"));
	conditionValueInput.setOutputMarkupId(true);
	add(conditionValueInput);
	}

	public DropDownChoice<String> getConditionAttributeSelect() {
		return conditionAttributeSelect;
	}

	public void setConditionAttributeSelect(DropDownChoice<String> conditionAttributeSelect) {
		this.conditionAttributeSelect = conditionAttributeSelect;
	}

	public TextField getConditionValueSelect() {
		return conditionValueInput;
	}

	public void setConditionValueSelect(TextField conditionValueInput) {
		this.conditionValueInput = conditionValueInput;
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
		return new SushiCondition(selectedConditionAttribute, selectedConditionValue);
	}

	public void updateAttributesValues() {
		Set<String> attributeValues = new HashSet();
		for (SushiEventType eventType : selectedEventTypes) {
			attributeValues.addAll(eventType.getAttributeKeysFromMap());
		}
		conditionAttributeSelect.setChoices(new ArrayList(attributeValues));
	}
}
