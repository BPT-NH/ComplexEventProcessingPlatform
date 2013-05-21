package sushi.application.pages.aggregation.patternbuilder.externalknowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.aggregation.collection.SushiPatternTree;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpression;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpressionSet;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.table.SelectEntryPanel;
import sushi.application.pages.aggregation.patternbuilder.model.AttributeSelectionPanel;
import sushi.application.pages.aggregation.patternbuilder.model.CriteriaValuePanel;
import sushi.application.pages.input.model.EventAttributeProvider;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTreeElement;

public class ExternalKnowledgePanel extends Panel {

	private static final long serialVersionUID = -1960020152680528731L;
	private WarnOnExitForm layoutForm;
	private SushiAttribute attributeToFill;
	private SushiPatternTree patternTree;
	private ListView<ExternalKnowledgeExpression> criteriaAttributesAndValuesListView;
	private WebMarkupContainer container;
	private String attributeIdentifier;
	private Map<String, String> attributeIdentifiersAndExpressions;
	private Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge;
	private List<ExternalKnowledgeExpression> coalesceExpressions;
	private TextField<String> defaultValueInput;
	private ExternalKnowledgeExpressionSet externalKnowledgeExpressionSet;
	private AttributeSelectionPanel parentPanel;
	
//	public ExternalKnowledgePanel(String id, SushiAttribute attributeToFill, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) {
//    	this(id);
//		this.attributeToFill = attributeToFill;
//		if (attributeToFill.isTimestamp()) {
//			this.attributeIdentifier = "Timestamp";
//		} else {
//			this.attributeIdentifier = attributeToFill.getAttributeExpression();
//		}
//		this.attributeIdentifiersWithExternalKnowledge = attributeIdentifiersWithExternalKnowledge;
//		this.externalKnowledgeExpressionSet = attributeIdentifiersWithExternalKnowledge.get(attributeIdentifier);
//		
//		this.patternTree = patternTree;
//    }

	public ExternalKnowledgePanel(String id) {
    	super(id);
    	this.patternTree = new SushiPatternTree();
    	this.attributeToFill = new SushiAttribute("default", SushiAttributeTypeEnum.STRING);
    	this.attributeIdentifier = attributeToFill.getAttributeExpression();
    	
    	layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		this.coalesceExpressions = new ArrayList<ExternalKnowledgeExpression>();
		this.coalesceExpressions.add(new ExternalKnowledgeExpression());
		
		buildHeader();
		buildCriteriaAttributesAndValuesListView();
		buildFooter();
//		updateExpression();
		
//		criteriaAttributesAndValuesListView = new ListView<String>("criteriaAttributesAndValuesListView", new ArrayList<String>(criteriaAttributesAndValues.keySet())) {
//
//			private static final long serialVersionUID = -553434279906525757L;
//			private String criteriaValue;
//			
//			@Override
//			protected void populateItem(ListItem<String> item) {
//				buildComponents(item);
//			}
//
//			private void buildComponents(final ListItem<String> item) {
//				Label criteriaAttributeExpressionLabel = new Label("criteriaAttributeExpressionLabel", item.getModelObject());
//				criteriaAttributeExpressionLabel.setOutputMarkupId(true);
//				item.add(criteriaAttributeExpressionLabel);
//				
//				AttributeExpressionTextField criteriaValueInput = new AttributeExpressionTextField("criteriaValueInput", new PropertyModel<String>(this, "criteriaValue"), patternTree);
//				criteriaValueInput.setOutputMarkupId(true);
//				
//				OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
//					private static final long serialVersionUID = -5737941362786901904L;
//					
//					@Override
//					protected void onUpdate(AjaxRequestTarget target) {
//						if (criteriaValue == null || criteriaValue.trim().isEmpty()) {
//							criteriaAttributesAndValues.put(item.getModelObject(), null);
//						} else {
//							criteriaAttributesAndValues.put(item.getModelObject(), criteriaValue);
//						}
//					}
//		        };
//		        criteriaValueInput.add(onChangeAjaxBehavior);
//				item.add(criteriaValueInput);
//			}
//		};
//		criteriaAttributesAndValuesListView.setOutputMarkupId(true);
//		
//		container = new WebMarkupContainer("criteriaAttributesAndValuesContainer");
//		container.add(criteriaAttributesAndValuesListView);
//		container.setOutputMarkupId(true);
//		
//		add(container);
	}

	private void buildHeader() {
		
		Label attributeToFillLabel = new Label("attributeToFillLabel", "Attribute: " + 
				attributeToFill.getAttributeExpression() + " (" + attributeToFill.getType() + ")");
		layoutForm.addOrReplace(attributeToFillLabel);
		
		AjaxButton addCriteriaAttributesAndValuesButton = new AjaxButton("addCriteriaAttributesAndValuesButton", layoutForm) {
			private static final long serialVersionUID = 6456362459418575615L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				coalesceExpressions.add(new ExternalKnowledgeExpression());
				criteriaAttributesAndValuesListView.setList(coalesceExpressions);
				target.add(container);
			}
		};
		layoutForm.addOrReplace(addCriteriaAttributesAndValuesButton);
	}

	private void buildCriteriaAttributesAndValuesListView() {
		criteriaAttributesAndValuesListView = new ListView<ExternalKnowledgeExpression>("criteriaAttributesAndValuesListView", coalesceExpressions) {

			private static final long serialVersionUID = -553434279906525757L;
			private DropDownChoice<SushiEventType> eventTypeDropDownChoice;
			private DropDownChoice<SushiAttribute> desiredAttributeDropDownChoice;
			private Component criteriaAttributesAndValuesTable;
			private ArrayList<IColumn<SushiAttribute, String>> criteriaAttributeAndValueColumns;
//			private String criteriaValue;
//			
			@Override
			protected void populateItem(ListItem<ExternalKnowledgeExpression> item) {
				ExternalKnowledgeExpression expression = item.getModelObject();
				buildComponents(item, expression);
			}

			private void buildComponents(final ListItem<ExternalKnowledgeExpression> item, final ExternalKnowledgeExpression expression) {
				ListView<ExternalKnowledgeExpression> listView = this;
				List<SushiEventType> eventTypes = SushiEventType.findAll();
				final EventAttributeProvider eventAttributeProvider;
				final Map<String, String> criteriaAttributesAndValues;
				if (expression.getCriteriaAttributesAndValues() == null || expression.getCriteriaAttributesAndValues().isEmpty()) {
					criteriaAttributesAndValues = new HashMap<String, String>();
				} else {
					criteriaAttributesAndValues = expression.getCriteriaAttributesAndValues();
				}
				
		    	eventTypeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeDropDownChoice", new Model<SushiEventType>(), eventTypes);
				eventTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
						if (eventTypeDropDownChoice.getModelObject() != null) {
							desiredAttributeDropDownChoice.setChoices(eventTypeDropDownChoice.getModelObject().getValueTypes());
						} else {
							desiredAttributeDropDownChoice.setChoices(new ArrayList<SushiAttribute>());
						}
						target.add(desiredAttributeDropDownChoice);
						
						final EventAttributeProvider eventAttributeProvider = new EventAttributeProvider(new ArrayList<SushiAttribute>());
						renderOrUpdateTable(item, eventAttributeProvider);
						target.add(criteriaAttributesAndValuesTable);
					}
				});
				eventTypeDropDownChoice.setModelObject(expression.getEventType());
				item.add(eventTypeDropDownChoice);
				
				desiredAttributeDropDownChoice = new DropDownChoice<SushiAttribute>("desiredAttributeDropDownChoice", new Model<SushiAttribute>(), new ArrayList<SushiAttribute>(),
					new ChoiceRenderer<SushiAttribute>() {
						private static final long serialVersionUID = -1940950340293620814L;
							@Override
							public Object getDisplayValue(SushiAttribute element) {
								return element.getAttributeExpression() + " : " + element.getType();
							}
						}
				);
				desiredAttributeDropDownChoice.setOutputMarkupId(true);
				desiredAttributeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onChange"){ 

					private static final long serialVersionUID = -6864036894506127410L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						List<SushiAttribute> attributes = eventTypeDropDownChoice.getModelObject().getValueTypes();
						attributes.remove(desiredAttributeDropDownChoice.getModelObject());
						final EventAttributeProvider eventAttributeProvider = new EventAttributeProvider(attributes);
						renderOrUpdateTable(item, eventAttributeProvider);
						target.add(criteriaAttributesAndValuesTable);
						for (SushiAttribute expression : attributes) {
							criteriaAttributesAndValues.put(expression.getAttributeExpression(), null);
						}
					}
				});
				if (eventTypeDropDownChoice.getModelObject() != null) {
					desiredAttributeDropDownChoice.setChoices(eventTypeDropDownChoice.getModelObject().getValueTypes());
					desiredAttributeDropDownChoice.setModelObject(expression.getDesiredAttribute());
				}
				item.add(desiredAttributeDropDownChoice);
				
				if (eventTypeDropDownChoice.getModelObject() == null) {
					eventAttributeProvider = new EventAttributeProvider(new ArrayList<SushiAttribute>());
				} else {
					List<SushiAttribute> attributes = eventTypeDropDownChoice.getModelObject().getValueTypes();
					attributes.remove(desiredAttributeDropDownChoice.getModelObject());
					ArrayList<SushiAttribute> selectedAttributes = new ArrayList<SushiAttribute>();
					for (SushiAttribute attribute : attributes) {
						if (expression.getCriteriaAttributesAndValues().get(attribute.getAttributeExpression()) != null) {
							selectedAttributes.add(attribute);
						}
					}
					eventAttributeProvider = new EventAttributeProvider(attributes, selectedAttributes);
				}
				
				criteriaAttributeAndValueColumns = new ArrayList<IColumn<SushiAttribute, String>>();
				criteriaAttributeAndValueColumns.add(new AbstractColumn<SushiAttribute, String>(new Model<String>("")) {
					private static final long serialVersionUID = -9120188492434788547L;
					@Override
					public void populateItem(Item cellItem, String componentId, IModel rowModel) {
						int entryId = ((SushiAttribute) rowModel.getObject()).getID();
						cellItem.add(new SelectEntryPanel(componentId, entryId, eventAttributeProvider));
					}
				});
				criteriaAttributeAndValueColumns.add(new PropertyColumn<SushiAttribute, String>(Model.of("Attribute"), "attributeExpression"));
				criteriaAttributeAndValueColumns.add(new AbstractColumn<SushiAttribute, String>(new Model<String>("Value")) {
					private static final long serialVersionUID = -5994858051827872697L;
					@Override
					public void populateItem(Item cellItem, String componentId, final IModel rowModel) {
						String attributeExpression = ((SushiAttribute) rowModel.getObject()).getAttributeExpression();
						cellItem.add(new CriteriaValuePanel(componentId, attributeExpression, criteriaAttributesAndValues, patternTree));
					}
				});
				
				renderOrUpdateTable(item, eventAttributeProvider);
				
				AjaxButton saveCoalesceExpressionButton = new AjaxButton("saveCoalesceExpressionButton", layoutForm) {
					private static final long serialVersionUID = 6456362459418575615L;
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						Map<String, String> newCriteriaAttributesAndValues = new HashMap<String, String>();
						for (String criteriaAttribute : criteriaAttributesAndValues.keySet()) {
							if (eventAttributeProvider.getSelectedAttributeExpressions().contains(criteriaAttribute)) {
								newCriteriaAttributesAndValues.put(criteriaAttribute, criteriaAttributesAndValues.get(criteriaAttribute));
//							} else {
//								newCriteriaAttributesAndValues.put(criteriaAttribute, null);
							}
						}
						coalesceExpressions.remove(item.getModelObject());
						coalesceExpressions.add(new ExternalKnowledgeExpression(eventTypeDropDownChoice.getModelObject(), desiredAttributeDropDownChoice.getModelObject(), newCriteriaAttributesAndValues));
						criteriaAttributesAndValuesListView.setList(coalesceExpressions);
						target.add(container);
					}
				};
				item.add(saveCoalesceExpressionButton);
				
				AjaxButton removeCoalesceExpressionButton = new AjaxButton("removeCoalesceExpressionButton", layoutForm) {
					private static final long serialVersionUID = 6456362459418575615L;
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						coalesceExpressions.remove(item.getModelObject());
						criteriaAttributesAndValuesListView.setList(coalesceExpressions);
						target.add(container);
					}
				};
				item.add(removeCoalesceExpressionButton);
			}

			private void renderOrUpdateTable(ListItem<ExternalKnowledgeExpression> item, EventAttributeProvider eventAttributeProvider) {
				criteriaAttributesAndValuesTable = new DefaultDataTable<SushiAttribute, String>("criteriaAttributesAndValuesTable", criteriaAttributeAndValueColumns, eventAttributeProvider, 20);
				criteriaAttributesAndValuesTable.setOutputMarkupId(true);
				
				item.addOrReplace(criteriaAttributesAndValuesTable);
			}
		};
		criteriaAttributesAndValuesListView.setOutputMarkupId(true);
		
		container = new WebMarkupContainer("criteriaAttributesAndValuesContainer");
		container.addOrReplace(criteriaAttributesAndValuesListView);
		container.setOutputMarkupId(true);
		
		layoutForm.addOrReplace(container);
	}

	private void buildFooter() {
		defaultValueInput = new TextField<String>("defaultValueInput", new Model<String>());
		defaultValueInput.setOutputMarkupId(true);
        if (externalKnowledgeExpressionSet != null) {
    		defaultValueInput.setModelObject(externalKnowledgeExpressionSet.getDefaultValue());
        } else {
        	defaultValueInput.setModelObject("");
        }
		layoutForm.addOrReplace(defaultValueInput);
		
		AjaxButton removeExpressionButton = new AjaxButton("removeExpressionButton", layoutForm) {
			private static final long serialVersionUID = 6456362459418575615L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				attributeIdentifiersWithExternalKnowledge.remove(attributeIdentifier);
				parentPanel.enableAllComponents(target);
			}
		};
		layoutForm.addOrReplace(removeExpressionButton);
		
		AjaxButton saveExpressionButton = new AjaxButton("saveExpressionButton", layoutForm) {
			private static final long serialVersionUID = 6456362459418575615L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (externalKnowledgeExpressionSet == null) {
					externalKnowledgeExpressionSet = new ExternalKnowledgeExpressionSet(attributeToFill.getType(), attributeIdentifier);
				}
				externalKnowledgeExpressionSet.setDefaultValue(defaultValueInput.getModelObject());
				externalKnowledgeExpressionSet.setResultingType(attributeToFill.getType());
				externalKnowledgeExpressionSet.setExternalKnowledgeExpressions(coalesceExpressions);
				attributeIdentifiersWithExternalKnowledge.put(attributeIdentifier, externalKnowledgeExpressionSet);
				attributeIdentifiersAndExpressions.put(attributeIdentifier, externalKnowledgeExpressionSet.generateExpressionForAggregationRule());
				parentPanel.disableAllComponents(target);
			}
		};
		layoutForm.addOrReplace(saveExpressionButton);
	}

	public SushiAttribute getAttributeToFill() {
		return attributeToFill;
	}

	public void setAttributeToFill(SushiAttribute attributeToFill) {
		this.attributeToFill = attributeToFill;
		if (attributeToFill.isTimestamp()) {
			this.attributeIdentifier = "Timestamp";
		} else {
			this.attributeIdentifier = attributeToFill.getAttributeExpression();
		}
	}

	public Map<String, String> getAttributeIdentifiersAndExpressions() {
		return attributeIdentifiersAndExpressions;
	}

	public void setAttributeIdentifiersAndExpressions(
			Map<String, String> attributeIdentifiersAndExpressions) {
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
	}

	public Map<String, ExternalKnowledgeExpressionSet> getAttributeIdentifiersWithExternalKnowledge() {
		return attributeIdentifiersWithExternalKnowledge;
	}

	public void setAttributeIdentifiersWithExternalKnowledge(Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge) {
		this.attributeIdentifiersWithExternalKnowledge = attributeIdentifiersWithExternalKnowledge;
		this.externalKnowledgeExpressionSet = attributeIdentifiersWithExternalKnowledge.get(attributeIdentifier);
		if (externalKnowledgeExpressionSet == null) {
			this.coalesceExpressions = new ArrayList<ExternalKnowledgeExpression>();
			this.coalesceExpressions.add(new ExternalKnowledgeExpression());
		} else {
			this.coalesceExpressions = externalKnowledgeExpressionSet.getExternalKnowledgeExpressions();
		}
	}

	public SushiPatternTree getPatternTree() {
		return patternTree;
	}

	public void setPatternTree(SushiPatternTree patternTree) {
		this.patternTree = patternTree;
	}

	public AttributeSelectionPanel getParentPanel() {
		return parentPanel;
	}

	public void setParentPanel(AttributeSelectionPanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	public void update(AjaxRequestTarget target) {
		buildHeader();
		buildCriteriaAttributesAndValuesListView();
		buildFooter();
		target.add(container);
		target.add(defaultValueInput);
	}

}
