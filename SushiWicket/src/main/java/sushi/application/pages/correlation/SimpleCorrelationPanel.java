package sushi.application.pages.correlation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.WarnOnExitForm;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;

/**
 * Panel representing the content panel for the first tab.
 */
public class SimpleCorrelationPanel extends Panel {
	
	private static final long serialVersionUID = 573672364803879784L;
	private ListMultipleChoice<SushiAttribute> correlationAttributesSelect;
	private ArrayList<SushiAttribute> correlationAttributes = new ArrayList<SushiAttribute>();
	private ArrayList<SushiAttribute> selectedCorrelationAttributes = new ArrayList<SushiAttribute>();
	private CheckBoxMultipleChoice<SushiEventType> eventTypesCheckBoxMultipleChoice;
	private ArrayList<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
	private ArrayList<SushiAttribute> commonCorrelationAttributes = new ArrayList<SushiAttribute>();
	private CorrelationPage correlationPage;
	
	public SimpleCorrelationPanel(String id, final CorrelationPage correlationPage) {
		super(id);
		
		this.correlationPage = correlationPage;
		
		Form<Void> form = new WarnOnExitForm("simpleCorrelationForm");
		add(form);
		
		addEventTypeCheckBoxMultipleChoice(form);
		
		correlationAttributesSelect = new ListMultipleChoice<SushiAttribute>("correlationEventTypesSelect", new Model<ArrayList<SushiAttribute>>(selectedCorrelationAttributes), correlationAttributes) {
			private static final long serialVersionUID = 1353243674818396947L;

			@Override
			public boolean isEnabled() {
				return !correlationPage.isSimpleCorrelationWithRules();
			}
		};
		correlationAttributesSelect.setOutputMarkupId(true);
		
		correlationAttributesSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			private static final long serialVersionUID = -6739995621796236402L;
			@Override 
			protected void onUpdate(AjaxRequestTarget target) { 
				System.out.println(selectedCorrelationAttributes);
			}
		});
		
		form.add(correlationAttributesSelect);
	}
	
	private void addEventTypeCheckBoxMultipleChoice(Form<Void> layoutForm) {
        
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		
		eventTypesCheckBoxMultipleChoice = new CheckBoxMultipleChoice<SushiEventType>("eventTypesCheckBoxMultipleChoice", new PropertyModel<ArrayList<SushiEventType>>(this, "selectedEventTypes"), eventTypes) {
			private static final long serialVersionUID = 5379816935206577577L;
			@Override
		 	protected boolean isDisabled(SushiEventType eventType, int index, String selected) {
				if (!correlationPage.isSimpleCorrelationWithRules()) {
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
								if (attributeOfEventType.getName().equals(commonAttribute.getName()) && (attributeOfEventType.getType() == commonAttribute.getType())) {
									return false;
								}
							}
						}
						return true;
					}
				} else {
					return true;
				}
			}
		};
		eventTypesCheckBoxMultipleChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				commonCorrelationAttributes.clear();
				correlationPage.clearAdvancedCorrelationPanelComponents();
				correlationPage.getSimpleCorrelationPanel().getCorrelationAttributesSelect().setChoices(new ArrayList<SushiAttribute>());
				if (!selectedEventTypes.isEmpty()) {
					// simple correlation
					commonCorrelationAttributes.addAll(selectedEventTypes.get(0).getValueTypes());
					for (SushiEventType actualEventType : selectedEventTypes) {
						commonCorrelationAttributes.retainAll(actualEventType.getValueTypes());
					}
					correlationAttributesSelect.setChoices(commonCorrelationAttributes);
					// advanced correlation - time
					correlationPage.setValuesOfAdvancedCorrelationPanelComponents(selectedEventTypes);
				}
				correlationPage.updateAdvancedCorrelationPanelComponents(target);
				target.add(correlationAttributesSelect);
				target.add(eventTypesCheckBoxMultipleChoice);
			}
		});
		eventTypesCheckBoxMultipleChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypesCheckBoxMultipleChoice);
	}

	public ListMultipleChoice<SushiAttribute> getCorrelationAttributesSelect() {
		return correlationAttributesSelect;
	}

	public void setCorrelationAttributesSelect(ListMultipleChoice<SushiAttribute> correlationAttributesSelect) {
		this.correlationAttributesSelect = correlationAttributesSelect;
	}

	public CheckBoxMultipleChoice<SushiEventType> getEventTypesCheckBoxMultipleChoice() {
		return eventTypesCheckBoxMultipleChoice;
	}

	public void setEventTypesCheckBoxMultipleChoice(
			CheckBoxMultipleChoice<SushiEventType> eventTypesCheckBoxMultipleChoice) {
		this.eventTypesCheckBoxMultipleChoice = eventTypesCheckBoxMultipleChoice;
	}

	public ArrayList<SushiAttribute> getSelectedCorrelationAttributes() {
		return selectedCorrelationAttributes;
	}

	public Set<SushiEventType> getCorrelationEventTypes() {
		return new HashSet<SushiEventType>(selectedEventTypes);
	}

	public void setCorrelationAttributes(ArrayList<SushiAttribute> correlationAttributes) {
		this.correlationAttributes = correlationAttributes;
	}

};
