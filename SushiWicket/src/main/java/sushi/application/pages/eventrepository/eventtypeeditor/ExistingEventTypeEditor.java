package sushi.application.pages.eventrepository.eventtypeeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.markup.html.form.palette.component.Recorder;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.ConditionInputPanel;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.main.MainPage;
import sushi.event.EventTypeRule;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.notification.SushiCondition;

/**
 * This page allows the creation of new {@link SushiEventType}s from existing ones.
 * @author micha
 */
public class ExistingEventTypeEditor extends Panel {
	
	private static final long serialVersionUID = 1L;
	private ConditionInputPanel conditionInput;
	private TextField<String> eventTypeInput;
	private Palette<SushiAttribute> relevantEventTypeColumnsPalette;
	private AbstractSushiPage abstractSushiPage;
	private CheckBoxMultipleChoice<SushiEventType> eventTypesCheckBoxMultipleChoice;
	private List<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
	private List<SushiAttribute> selectedEventTypeAttributes = new ArrayList<SushiAttribute>();
	private List<SushiAttribute> commonCorrelationAttributes = new ArrayList<SushiAttribute>();
	private List<SushiEventType> eventTypes = SushiEventType.findAll();

	/**
	 * Constructor for a page to create new {@link SushiEventType}s from existing ones.
	 * @param id
	 * @param abstractSushiPage
	 */
	public ExistingEventTypeEditor(String id, AbstractSushiPage abstractSushiPage){
		super(id);
		
		this.abstractSushiPage = abstractSushiPage;
		
		Form<Void> layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		layoutForm.add(addEventTypeInput());
		
		layoutForm.add(addConditionInput());
		
		layoutForm.add(addExistingEventTypeSelect());
		
		layoutForm.add(addRelevantEventTypeColumnsPalette());
		
		addButtonsToForm(layoutForm);
	}

	private Component addEventTypeInput() {
		eventTypeInput = new TextField<String>("eventTypeInput", Model.of(""));
		eventTypeInput.setOutputMarkupId(true);
		return eventTypeInput;
	}
	
	private Component addConditionInput() {
		conditionInput = new ConditionInputPanel("conditionInput", true);
		conditionInput.setOutputMarkupId(true);
		return conditionInput;
	}
	
	private Component addExistingEventTypeSelect() {

		eventTypesCheckBoxMultipleChoice = new CheckBoxMultipleChoice<SushiEventType>("eventTypesCheckBoxMultipleChoice", new PropertyModel<ArrayList<SushiEventType>>(this, "selectedEventTypes"), eventTypes) {
			@Override
		 	protected boolean isDisabled(SushiEventType eventType, int index, String selected) {
				// true for event types without matching attributes
				if (selectedEventTypes.isEmpty()) {
					return false;
				} else {
					for (SushiAttribute commonAttribute : commonCorrelationAttributes) {
						if (eventType.getValueTypes().contains(commonAttribute)) {
							return false;
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
				if (!selectedEventTypes.isEmpty()) {
					commonCorrelationAttributes.addAll(selectedEventTypes.get(0).getValueTypes());
					for (SushiEventType actualEventType : selectedEventTypes) {
						commonCorrelationAttributes.retainAll(actualEventType.getValueTypes());
					}
				}
				conditionInput.setSelectedEventTypes(selectedEventTypes);
				conditionInput.updateAttributesValues();
				target.add(conditionInput.getConditionAttributeSelect());
				target.add(conditionInput.getConditionValueSelect());
				target.add(relevantEventTypeColumnsPalette);
				target.add(eventTypesCheckBoxMultipleChoice);
			}
		});
		eventTypesCheckBoxMultipleChoice.setOutputMarkupId(true);
		return eventTypesCheckBoxMultipleChoice;
	}
	
	private Component addRelevantEventTypeColumnsPalette() {
		IModel<List<? extends SushiAttribute>> eventTypeAttributeModel = new AbstractReadOnlyModel<List<? extends SushiAttribute>>() {
			@Override
			public List<SushiAttribute> getObject() {
				//in the columnsPalette should only be attributes, that are contained in all selected event Types
				Set<SushiAttribute> attributes = new HashSet<SushiAttribute>();
				boolean first = true;
				for (SushiEventType eventType : selectedEventTypes) {
					if (eventType != null) {
						if (first) {
							attributes.addAll(eventType.getValueTypes());
							first = false;
						} else {
							attributes.retainAll(eventType.getValueTypes());
						}
					}
				}
				return new ArrayList<SushiAttribute>(attributes);
			}
		};

		relevantEventTypeColumnsPalette = new Palette<SushiAttribute>("relevantEventTypePalette", 
						new ListModel<SushiAttribute>(new ArrayList<SushiAttribute>()), 
						eventTypeAttributeModel,
						new ChoiceRenderer(), 5, false) {

			private static final long serialVersionUID = 1L;

			protected Recorder newRecorderComponent() {
				Recorder recorder = super.newRecorderComponent();
				recorder.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						Iterator<SushiAttribute> selectedColumns = getSelectedChoices();
						selectedEventTypeAttributes = new ArrayList<SushiAttribute>();
						while(selectedColumns.hasNext()){
							SushiAttribute eventTypeAttribute = selectedColumns.next();
							selectedEventTypeAttributes.add(eventTypeAttribute);
						}
						
					}
				});
				return recorder;
			}
		};
		relevantEventTypeColumnsPalette.setOutputMarkupId(true);
		return relevantEventTypeColumnsPalette;
	}
	
	private void addButtonsToForm(Form layoutForm) {
		AjaxButton clearButton = new AjaxButton("clearButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				eventTypeInput.setModel(Model.of(""));
				conditionInput.setSelectedEventTypes(new  ArrayList());
				target.add(conditionInput);
				target.add(eventTypeInput);
	        }
	    };
		
	    layoutForm.add(clearButton);
	    
	    AjaxButton deleteButton = new AjaxButton("deleteButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				for(SushiEventType selectedEventType : selectedEventTypes){
					selectedEventType.remove();
				}
				target.add(eventTypesCheckBoxMultipleChoice);
	        }
	    };
		
	    layoutForm.add(deleteButton);
	    
	    BlockingAjaxButton createButton = new BlockingAjaxButton("createButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				String newEventTypeName = (String) eventTypeInput.getValue();
				if(!newEventTypeName.isEmpty() && SushiEventType.findByTypeName(newEventTypeName) == null){
					//create EventType
					SushiEventType newEventType = new SushiEventType(newEventTypeName);
					newEventType.addValueTypes(selectedEventTypeAttributes);
					Broker.send(newEventType);
					
					//create EventTypeRule
					Set<SushiEventType> usedEventTypes = new HashSet<SushiEventType>();
					for(SushiEventType selectedEventType: selectedEventTypes){
						usedEventTypes.add(selectedEventType);
					}
					SushiCondition condition = conditionInput.getCondition();
					EventTypeRule newEventTypeRule = new EventTypeRule(new ArrayList<SushiEventType>(usedEventTypes), condition, newEventType); 
					newEventTypeRule.save();
					
					//execute on existing data
					ArrayList<SushiEvent> newEvents = newEventTypeRule.execute();
					Broker.send(newEvents);
					
					target.add(eventTypesCheckBoxMultipleChoice);
					PageParameters pageParameters = new PageParameters();
					pageParameters.add("successFeedback", newEvents.size() + " events have been added to " + newEventTypeName);
					setResponsePage(MainPage.class, pageParameters);
				} else if (SushiEventType.getAllTypeNames().contains(newEventTypeName)) {
					target.appendJavaScript("$.unblockUI();");
					abstractSushiPage.getFeedbackPanel().error("Event type already exists.");
					target.add(abstractSushiPage.getFeedbackPanel());
				} else {
					target.appendJavaScript("$.unblockUI();");
					abstractSushiPage.getFeedbackPanel().error("You did not provide correct information.");
					target.add(abstractSushiPage.getFeedbackPanel());
				}
	        }
	    };
		
	    layoutForm.add(createButton);
	}

}
