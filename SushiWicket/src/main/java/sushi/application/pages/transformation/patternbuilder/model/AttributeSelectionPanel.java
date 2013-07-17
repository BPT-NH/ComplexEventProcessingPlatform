package sushi.application.pages.transformation.patternbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.pages.transformation.patternbuilder.PatternBuilderPanel;
import sushi.application.pages.transformation.patternbuilder.externalknowledge.ExternalKnowledgeModal;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTreeElement;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.PatternOperatorEnum;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpressionSet;

public class AttributeSelectionPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Form<Void> layoutForm;
	private SushiPatternTree tree;
	private DropDownChoice<EventTypeElement> eventTypeElementDropDownChoice;
	private DropDownChoice<SushiAttribute> attributeDropDownChoice;
	private AttributeExpressionTextField expressionInput;
	private String userDefinedExpression;
	private Map<String, String> attributeIdentifiersAndExpressions;
	private Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge;
	private SushiAttribute attributeToFill;
	protected String expressionFromDropDownChoices;
	private String attributeIdentifier;
	private PatternBuilderPanel patternBuilderPanel;
	private AjaxCheckBox currentDateUsedCheckbox;
	private Boolean currentDateUsed;
	private Boolean allComponentsEnabled;
	private TextField<Integer> arrayElementIndexInput;
	private Label arrayElementIndexLabel;
	private Integer arrayElementIndex;
	private AttributeSelectionPanel panel;
	private Label currentDateUsedLabel;

	public AttributeSelectionPanel(String id, SushiAttribute attributeToFill, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, PatternBuilderPanel patternBuilderPanel) {
		super(id);
		
		this.panel = this;
		this.currentDateUsed = false;
		this.userDefinedExpression = new String();
		this.expressionFromDropDownChoices = new String();
		this.patternBuilderPanel = patternBuilderPanel;
		this.tree = patternBuilderPanel.getPatternTree();
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
		this.attributeIdentifiersWithExternalKnowledge = attributeIdentifiersWithExternalKnowledge;
		this.attributeToFill = attributeToFill;
		if (attributeToFill.isTimestamp()) {
			this.attributeIdentifier = "Timestamp";
		} else {
			this.attributeIdentifier = attributeToFill.getAttributeExpression();
		}
		if (attributeToFill.getType() == null || attributeIdentifiersWithExternalKnowledge.get(attributeIdentifier) != null) {
			this.allComponentsEnabled = false;
		} else {
			this.allComponentsEnabled = true;
		}
		if (attributeIdentifiersAndExpressions.containsKey(attributeIdentifier)) {
			userDefinedExpression = attributeIdentifiersAndExpressions.get(attributeIdentifier);
		}
		layoutForm = new Form<Void>("layoutForm");
		buildUseCurrentDateCheckbox();
		buildEventTypeDropDownChoice();
		buildArrayElementIndexComponents();
		buildAttributeDropDownChoice();
		buildExpressionInput();
		buildUseExternalKnowledgeButton();
		
		add(layoutForm);
	}
	
	private void buildUseExternalKnowledgeButton() {
		AjaxButton useExternalKnowledgeButton = new AjaxButton("useExternalKnowledgeButton", layoutForm) {
			private static final long serialVersionUID = -2611608162033482853L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ExternalKnowledgeModal modal = patternBuilderPanel.getAdvancedRuleEditorPanel().getTransformationPage().getExternalKnowledgeModal();
				modal.getPanel().setAttributeIdentifiersAndExpressions(attributeIdentifiersAndExpressions);
				modal.getPanel().setAttributeIdentifiersWithExternalKnowledge(attributeIdentifiersWithExternalKnowledge);
				modal.getPanel().setPatternTree(patternBuilderPanel.getPatternTree());
				modal.getPanel().setAttributeToFill(attributeToFill);
				modal.getPanel().setParentPanel(panel);
				modal.getPanel().detach();
				modal.getPanel().update(target);
				target.add(modal.getPanel());
				modal.show(target);
//				eventRepository.getEventViewModal().getPanel().setEvent(rowModel.getObject());
//           	 eventRepository.getEventViewModal().getPanel().detach();
//           	 target.add(eventRepository.getEventViewModal().getPanel());
//           	 eventRepository.getEventViewModal().show(target);
			}
		};
		layoutForm.add(useExternalKnowledgeButton);
	}

	private void buildUseCurrentDateCheckbox() {
		
		currentDateUsedLabel = new Label("currentDateUsedLabel", "Time of transformation") {
			private static final long serialVersionUID = 7258389748479790432L;
			@Override
			public boolean isVisible() {
				return attributeToFill.getType() == SushiAttributeTypeEnum.DATE;
			}
		};
		layoutForm.add(currentDateUsedLabel);
		
		currentDateUsedCheckbox = new AjaxCheckBox("currentDateUsedCheckbox", new PropertyModel<Boolean>(this, "currentDateUsed")) {
			private static final long serialVersionUID = -8207035371422899809L;
			@Override
			public boolean isEnabled() {
				return allComponentsEnabled;
			}
			@Override
			public boolean isVisible() {
				return attributeToFill.getType() == SushiAttributeTypeEnum.DATE;
			}
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (currentDateUsed) {
					attributeIdentifiersAndExpressions.put(attributeIdentifier, "currentDate()");
				} else {
					if (eventTypeElementDropDownChoice.getModelObject() != null) {
						attributeIdentifiersAndExpressions.put(attributeIdentifier, expressionFromDropDownChoices);
					} else {
						attributeIdentifiersAndExpressions.put(attributeIdentifier, userDefinedExpression);
					}
				}
				target.add(eventTypeElementDropDownChoice);
				target.add(attributeDropDownChoice);
				target.add(expressionInput);
			}
		};
		currentDateUsedCheckbox.setOutputMarkupId(true);
		layoutForm.add(currentDateUsedCheckbox);
	}

	private void buildEventTypeDropDownChoice() {
		List<EventTypeElement> eventTypeElements = new ArrayList<EventTypeElement>();
		for (SushiTreeElement<Serializable> element : tree.getElements()) {
			if (element instanceof EventTypeElement) {
				EventTypeElement eventTypeElement = (EventTypeElement) element;
				eventTypeElements.add(eventTypeElement);
			}
		}
		eventTypeElementDropDownChoice = new DropDownChoice<EventTypeElement>("eventTypeElementDropDownChoice", new Model<EventTypeElement>(), eventTypeElements, 
			new ChoiceRenderer<EventTypeElement>() {
				private static final long serialVersionUID = 1L;
					@Override
					public Object getDisplayValue(EventTypeElement element) {
						StringBuffer sb = new StringBuffer();
						if (element.getAlias() == null || element.getAlias().isEmpty()) {
							sb.append("No alias");
						} else {
							sb.append(element.getAlias());
						}
						sb.append(" (" + ((SushiEventType) element.getValue()).getTypeName() + ")");
						return sb.toString();
					}
				}
		) {
			private static final long serialVersionUID = -6808132238575181809L;
			@Override
			public boolean isEnabled() {
				return allComponentsEnabled && !currentDateUsed;
			}
			@Override
			public boolean isDisabled(final EventTypeElement element, int index, String selected) {
				if (element.getAlias() == null) {
					return true;
				}
				return element.getAlias().isEmpty();
			}
		};
		eventTypeElementDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (eventTypeElementDropDownChoice.getModelObject() != null) {
					SushiEventType eventType = (SushiEventType) eventTypeElementDropDownChoice.getModelObject().getValue();
					ArrayList<SushiAttribute> potentialAttributes = new ArrayList<SushiAttribute>();
					if (attributeToFill.getType() == SushiAttributeTypeEnum.DATE) {
						potentialAttributes.add(new SushiAttribute("Timestamp", SushiAttributeTypeEnum.DATE));
					}
					for (SushiAttribute currentAttribute : eventType.getValueTypes()) {
						if (attributeToFill.getType() == currentAttribute.getType()) {
							potentialAttributes.add(currentAttribute);
						}
					}
					attributeIdentifiersAndExpressions.put(attributeIdentifier, expressionFromDropDownChoices);
					attributeDropDownChoice.setChoices(potentialAttributes);
					updateExpressionFromDropDownChoice();
				} else {
					attributeIdentifiersAndExpressions.put(attributeIdentifier, userDefinedExpression);
					attributeDropDownChoice.setChoices(new ArrayList<SushiAttribute>());
				}
				target.add(arrayElementIndexInput);
				target.add(arrayElementIndexLabel);
				updateAllComponents(target);
			}
		});
		eventTypeElementDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypeElementDropDownChoice);
	}
	
	/**
	 * for event types with pattern operator REPEAT as parent
	 */
	private void buildArrayElementIndexComponents() {
		arrayElementIndexInput = new TextField<Integer>("arrayElementIndexInput", new PropertyModel<Integer>(this, "arrayElementIndex")) {
			private static final long serialVersionUID = 7106359506546529349L;
			@Override
			public boolean isVisible() {
				return eventTypeElementDropDownChoice.getModelObject() != null && 
						eventTypeElementDropDownChoice.getModelObject().hasParent() && 
						eventTypeElementDropDownChoice.getModelObject().getParent().getValue() == PatternOperatorEnum.REPEAT;
			}
		};
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -5737941362786901904L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateExpressionFromDropDownChoice();
				attributeIdentifiersAndExpressions.put(attributeIdentifier, expressionFromDropDownChoices);
			}
        };
        arrayElementIndexInput.add(onChangeAjaxBehavior);
//		if (eventTypeElementDropDownChoice.getModelObject() != null && eventTypeElementDropDownChoice.getModelObject().hasParent() && eventTypeElementDropDownChoice.getModelObject().getParent().getValue() == PatternOperatorEnum.REPEAT) {
//			int matchCount = ((PatternOperatorElement) eventTypeElementDropDownChoice.getModelObject().getParent()).getRangeElement().getLeftEndpoint();
//			RangeValidator<Integer> rangeValidator = new RangeValidator<Integer>(0, matchCount);
//			arrayElementIndexInput.add(rangeValidator);
//		}
		arrayElementIndexInput.setOutputMarkupPlaceholderTag(true);
		arrayElementIndexInput.setOutputMarkupId(true);
		layoutForm.add(arrayElementIndexInput);
		
		arrayElementIndexLabel = new Label("arrayElementIndexLabel", "Element #") {
			private static final long serialVersionUID = 7890155448902992129L;
			@Override
			public boolean isVisible() {
				return eventTypeElementDropDownChoice.getModelObject() != null && 
						eventTypeElementDropDownChoice.getModelObject().hasParent() && 
						eventTypeElementDropDownChoice.getModelObject().getParent().getValue() == PatternOperatorEnum.REPEAT;
			}
		};
		arrayElementIndexLabel.setOutputMarkupPlaceholderTag(true);
		arrayElementIndexLabel.setOutputMarkupId(true);
		layoutForm.add(arrayElementIndexLabel);
	}

	private void buildAttributeDropDownChoice() {
		attributeDropDownChoice = new DropDownChoice<SushiAttribute>("attributeDropDownChoice", new Model<SushiAttribute>(), new ArrayList<SushiAttribute>(), 
				new ChoiceRenderer<SushiAttribute>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Object getDisplayValue(SushiAttribute attribute) {
						return attribute.getAttributeExpression();
					}
				}
		) {
			private static final long serialVersionUID = 474559809405809953L;
			@Override
			public boolean isEnabled() {
				return allComponentsEnabled && eventTypeElementDropDownChoice.getModelObject() != null && !currentDateUsed;
			}
		};
		attributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (attributeDropDownChoice.getModelObject() != null) {
					updateExpressionFromDropDownChoice();
					attributeIdentifiersAndExpressions.put(attributeIdentifier, expressionFromDropDownChoices);
				}
			}
		});
		attributeDropDownChoice.setEnabled(false);
		attributeDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(attributeDropDownChoice);
	}

	private void buildExpressionInput() {
		expressionInput = new AttributeExpressionTextField("expressionInput", new PropertyModel<String>(this, "userDefinedExpression"), patternBuilderPanel.getPatternTree()) {
			private static final long serialVersionUID = -5212591175918436633L;
			@Override
			public boolean isEnabled() {
				return allComponentsEnabled && eventTypeElementDropDownChoice.getModelObject() == null && !currentDateUsed;
			}
		};
		expressionInput.setOutputMarkupId(true);
		
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -5737941362786901904L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (userDefinedExpression == null) {
					attributeIdentifiersAndExpressions.put(attributeIdentifier, "");
				} else {
					attributeIdentifiersAndExpressions.put(attributeIdentifier, userDefinedExpression);
				}
			}
        };
        expressionInput.add(onChangeAjaxBehavior);
		layoutForm.add(expressionInput);
	}
	
	private void updateExpressionFromDropDownChoice() {
		if (attributeDropDownChoice.getModelObject() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(eventTypeElementDropDownChoice.getModelObject().getAlias());
			if (arrayElementIndexInput.isVisible()) {
				sb.append("[" + String.valueOf(arrayElementIndex) + "]");
			}
			sb.append("." + attributeDropDownChoice.getModelObject().getAttributeExpression());
			expressionFromDropDownChoices = sb.toString();
		}
	}

	public void enableAllComponents(AjaxRequestTarget target) {
		allComponentsEnabled = true;
		updateAllComponents(target);
		if (eventTypeElementDropDownChoice.getModelObject() != null) {
			attributeIdentifiersAndExpressions.put(attributeIdentifier, expressionFromDropDownChoices);
		} else {
			if (userDefinedExpression == null) {
				attributeIdentifiersAndExpressions.put(attributeIdentifier, "");
			} else {
				attributeIdentifiersAndExpressions.put(attributeIdentifier, userDefinedExpression);
			}
		}
	}
	
	public void disableAllComponents(AjaxRequestTarget target) {
		allComponentsEnabled = false;
		updateAllComponents(target);
	}
	
	public void updateAllComponents(AjaxRequestTarget target) {
		target.add(currentDateUsedCheckbox);
		target.add(eventTypeElementDropDownChoice);
		target.add(attributeDropDownChoice);
		target.add(expressionInput);
	}
}
