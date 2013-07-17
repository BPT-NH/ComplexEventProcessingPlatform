package sushi.application.pages.correlation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.WarnOnExitForm;
import sushi.correlation.CorrelationRule;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;

/**
 * Panel representing the content panel for the first tab.
 */
public class SimpleCorrelationWithRulesPanel extends Panel {
	
	private static final long serialVersionUID = -4523105587173220532L;
	private List<CorrelationRule> correlationRules = new ArrayList<CorrelationRule>();
	private ListView<CorrelationRule> correlationRuleListView;
	private WebMarkupContainer correlationRuleMarkupContainer;
	private CorrelationPage correlationPage;
	private AjaxButton addCorrelationRuleButton;
	private Set<SushiEventType> correlationEventTypes = new HashSet<SushiEventType>();
	
	public SimpleCorrelationWithRulesPanel(String id, final CorrelationPage correlationPage) {
		super(id);
		this.correlationPage = correlationPage;
		
		final Form<Void> layoutForm = new WarnOnExitForm("simpleCorrelationWithRulesForm");
		add(layoutForm);
		
		correlationRules.add(new CorrelationRule());
		
		addCorrelationRuleButton = new AjaxButton("addCorrelationRuleButton", layoutForm) {
			private static final long serialVersionUID = -118988274959205111L;
			
			@Override
			public boolean isEnabled() {
				return correlationPage.isSimpleCorrelationWithRules();
			}
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				correlationRules.add(new CorrelationRule());
				target.add(correlationRuleMarkupContainer);
			}
		};
		addCorrelationRuleButton.setOutputMarkupId(true);
		layoutForm.add(addCorrelationRuleButton);
		
		correlationRuleListView = new ListView<CorrelationRule>("correlationRuleListView", correlationRules) {
			private static final long serialVersionUID = 4168798264053898499L;
			
			@Override
			public boolean isEnabled() {
				return correlationPage.isSimpleCorrelationWithRules();
			}
			
			@Override
			protected void populateItem(final ListItem<CorrelationRule> item) {
				
				final CorrelationRule correlationRule = item.getModelObject();
				
				final DropDownChoice<SushiAttribute> firstAttributeDropDownChoice = new DropDownChoice<SushiAttribute>("firstAttributeDropDownChoice", new Model<SushiAttribute>(), new ArrayList<SushiAttribute>());
				firstAttributeDropDownChoice.setOutputMarkupId(true);
				firstAttributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						correlationRule.setFirstAttribute(firstAttributeDropDownChoice.getModelObject());
						target.add(correlationRuleMarkupContainer);
					}
				});
				if (correlationRule.getEventTypeOfFirstAttribute() != null) {
					firstAttributeDropDownChoice.setChoices(correlationRule.getEventTypeOfFirstAttribute().getValueTypes());
				}
				firstAttributeDropDownChoice.setModelObject(correlationRule.getFirstAttribute());
				item.add(firstAttributeDropDownChoice);
		    	
		    	List<SushiEventType> eventTypes = SushiEventType.findAll();
				final DropDownChoice<SushiEventType> eventTypeOfFirstAttributeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeOfFirstAttributeDropDownChoice", new Model<SushiEventType>(), eventTypes);
				eventTypeOfFirstAttributeDropDownChoice.setOutputMarkupId(true);
				eventTypeOfFirstAttributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						SushiEventType selectedEventType = eventTypeOfFirstAttributeDropDownChoice.getModelObject();
						correlationRule.setEventTypeOfFirstAttribute(selectedEventType);
						target.add(correlationRuleMarkupContainer);
						updateAdvancedCorrelationPanel(target);
					}
				});
//				if (correlationRule.getFirstAttribute() != null) {
//					eventTypeOfFirstAttributeDropDownChoice.setModelObject(correlationRule.getFirstAttribute().getEventType());
//				}
				eventTypeOfFirstAttributeDropDownChoice.setModelObject(correlationRule.getEventTypeOfFirstAttribute());
				correlationRule.setEventTypeOfFirstAttribute(eventTypeOfFirstAttributeDropDownChoice.getModelObject());

				item.add(eventTypeOfFirstAttributeDropDownChoice);
				
				final DropDownChoice<SushiAttribute> secondAttributeDropDownChoice = new DropDownChoice<SushiAttribute>("secondAttributeDropDownChoice", new Model<SushiAttribute>(), new ArrayList<SushiAttribute>()) {
					private static final long serialVersionUID = 7107102900826509015L;
					@Override
					public boolean isEnabled() {
						return firstAttributeDropDownChoice.getModelObject() != null;
					}
				};
				secondAttributeDropDownChoice.setOutputMarkupId(true);
				secondAttributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						correlationRule.setSecondAttribute(secondAttributeDropDownChoice.getModelObject());
					}
				});
				if (correlationRule.getEventTypeOfSecondAttribute() != null) {
					List<SushiAttribute> possibleSecondAttributes = new ArrayList<SushiAttribute>();
					for (SushiAttribute attribute : correlationRule.getEventTypeOfSecondAttribute().getValueTypes()) {
						if (attribute.getType() == firstAttributeDropDownChoice.getModelObject().getType()) {
							possibleSecondAttributes.add(attribute);
						}
					}
					secondAttributeDropDownChoice.setChoices(possibleSecondAttributes);
				}
				secondAttributeDropDownChoice.setModelObject(correlationRule.getSecondAttribute());
				item.add(secondAttributeDropDownChoice);
				
				final DropDownChoice<SushiEventType> eventTypeOfSecondAttributeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeOfSecondAttributeDropDownChoice", new Model<SushiEventType>(), eventTypes) {
					private static final long serialVersionUID = 3720572018390164569L;
					@Override
					public boolean isEnabled() {
						return firstAttributeDropDownChoice.getModelObject() != null;
					}
				};
				eventTypeOfSecondAttributeDropDownChoice.setOutputMarkupId(true);
				eventTypeOfSecondAttributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						SushiEventType selectedEventType = eventTypeOfSecondAttributeDropDownChoice.getModelObject();
						correlationRule.setEventTypeOfSecondAttribute(selectedEventType);
						target.add(correlationRuleMarkupContainer);
						updateAdvancedCorrelationPanel(target);
					}
				});
//				if (correlationRule.getSecondAttribute() != null) {
//					eventTypeOfSecondAttributeDropDownChoice.setModelObject(correlationRule.getSecondAttribute().getEventType());
//				}
				eventTypeOfSecondAttributeDropDownChoice.setModelObject(correlationRule.getEventTypeOfSecondAttribute());
				correlationRule.setEventTypeOfSecondAttribute(eventTypeOfSecondAttributeDropDownChoice.getModelObject());
				item.add(eventTypeOfSecondAttributeDropDownChoice);
				
				AjaxButton removeCorrelationRuleButton = new AjaxButton("removeCorrelationRuleButton", layoutForm) {
					private static final long serialVersionUID = -4244320500409194238L;
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						correlationRules.remove(item.getModelObject());
						target.add(correlationRuleMarkupContainer);
						updateAdvancedCorrelationPanel(target);
					}
				};
				item.add(removeCorrelationRuleButton);
		    }
		};
		correlationRuleListView.setEnabled(!correlationPage.isSimpleCorrelationWithRules());
		
		correlationRuleMarkupContainer = new WebMarkupContainer("correlationRuleMarkupContainer");
		correlationRuleMarkupContainer.add(correlationRuleListView);
		correlationRuleMarkupContainer.setOutputMarkupId(true);
		layoutForm.addOrReplace(correlationRuleMarkupContainer);
	}
	
//	private List<CorrelationRule> getCorrelationRulesForListView() {
//		List<CorrelationRule> correlationRulesForListView = new ArrayList<CorrelationRule>(correlationRules);
//		correlationRulesForListView.add(new CorrelationRule());
//		return correlationRulesForListView;
//	}

	public void clearCorrelationAttributesListView(AjaxRequestTarget target) {
		correlationRules.clear();
		target.add(correlationRuleMarkupContainer);
	}

	public List<CorrelationRule> getCorrelationRules() {
		return correlationRules;
	}

	public void setCorrelationRules(ArrayList<CorrelationRule> correlationRules) {
		this.correlationRules = correlationRules;
	}

	public AjaxButton getAddCorrelationRuleButton() {
		return addCorrelationRuleButton;
	}

	public void setAddCorrelationRuleButton(AjaxButton addCorrelationRuleButton) {
		this.addCorrelationRuleButton = addCorrelationRuleButton;
	}

	public WebMarkupContainer getCorrelationRuleMarkupContainer() {
		return correlationRuleMarkupContainer;
	}

	public void setCorrelationRuleMarkupContainer(WebMarkupContainer correlationRuleMarkupContainer) {
		this.correlationRuleMarkupContainer = correlationRuleMarkupContainer;
	}

	private void updateAdvancedCorrelationPanel(AjaxRequestTarget target) {
		correlationEventTypes.clear();
		for (CorrelationRule correlationRule : correlationRules) {
			if (correlationRule.getEventTypeOfFirstAttribute() != null) {
				correlationEventTypes.add(correlationRule.getEventTypeOfFirstAttribute());
			}
			if (correlationRule.getEventTypeOfSecondAttribute() != null) {
				correlationEventTypes.add(correlationRule.getEventTypeOfSecondAttribute());
			}
		}
		correlationPage.setValuesOfAdvancedCorrelationPanelComponents(new ArrayList<SushiEventType>(correlationEventTypes));
		correlationPage.updateAdvancedCorrelationPanelComponents(target);
	}

	public Set<SushiEventType> getCorrelationEventTypes() {
		return correlationEventTypes;
	}

};
