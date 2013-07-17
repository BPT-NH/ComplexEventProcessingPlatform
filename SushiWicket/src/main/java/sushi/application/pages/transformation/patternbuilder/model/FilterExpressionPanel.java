package sushi.application.pages.transformation.patternbuilder.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.pages.transformation.patternbuilder.PatternBuilderPanel;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.FilterExpressionOperatorEnum;
import sushi.transformation.element.RangeElement;

public class FilterExpressionPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Form<Void> layoutForm;
	private String leftHandSideExpression;
	private String rightHandSideExpression;
	private AttributeExpressionTextField rightHandSideExpressionInput;
	private FilterExpressionElement element;
	private PatternElementTreeTable table;
	private int leftEndpoint;
	private int rightEndpoint;
	private FilterExpressionOperatorEnum filterExpressionOperator;
	private RadioChoice<String> typeOfValuesRadioChoice;
	private String typeOfValues;
	private PatternBuilderPanel panel;

	public FilterExpressionPanel(String id, FilterExpressionElement element, PatternBuilderPanel panel) {
		super(id);
		
		layoutForm = new Form<Void>("layoutForm");
		
		this.element = element;
		this.table = panel.getPatternTreeTable();
		this.panel = panel;
		
		buildLeftHandSideExpressionInput();
		buildFilterExpressionOperatorDropDownChoice();
		buildRightHandSideExpressionInput();
		buildRightHandSideValuesBasedComponents();
			
//		List<String> expressions = new ArrayList<String>();
//		expressions.addAll(element.getFilterExpressions());
//		if (!expressions.contains("")) {
//			expressions.add("");
//		}
		
//		filterExpressionListView = new ListView<String>("filterExpressionListView", expressions) { 
//
//			private static final long serialVersionUID = -8698730823614901057L;
//			
//			@Override
//			protected void populateItem(ListItem<String> item) {
//				String expression = item.getModelObject();
//				buildComponents(item, expression);
//			}
//			
//			private void buildComponents(ListItem<String> item, final String expression) {
//				
//				final TextField<String> filterExpressionInput = new TextField<String>("filterExpressionInput", new Model<String>()) {
//					private static final long serialVersionUID = 5931500662562159353L;
//
//					@Override
//					public boolean isEnabled() {
//						return expression.isEmpty();
//					}
//				};
//				filterExpressionInput.setModelObject(expression);
//				filterExpressionInput.setOutputMarkupId(true);
//			    item.add(filterExpressionInput);
//			    
////				AjaxButton deleteButton = new AjaxButton("deleteButton", layoutForm) {
////					private static final long serialVersionUID = 1609842059851860853L;
////
////					@Override
////					public boolean isVisible() {
////						return !expression.isEmpty();
////					}
////					
////					@Override
////					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
////						element.removeFilterExpression(filterExpressionInput.getModelObject());
////						target.add(table);
////			        }
////			    };
////			    item.add(deleteButton);
//			    
////			    AjaxButton editButton = new AjaxButton("editButton", layoutForm) {
////					private static final long serialVersionUID = 1L;
////					@Override
////					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
////						element.removeFilterExpression(filterExpressionInput.getModelObject());
////						filterExpressionInput.setEnabled(true);
////						target.add(filterExpressionInput);
////			        }
////			    };
////			    item.add(editButton);
//				
//				AjaxButton saveButton = new AjaxButton("saveButton", layoutForm) {
//					private static final long serialVersionUID = 1928837801022392147L;
//
//					@Override
//					public boolean isVisible() {
//						return expression.isEmpty();
//					}
//					
//					@Override
//					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						String rawInput = filterExpressionInput.getModelObject();
//						if (rawInput != null) {
//							String newExpression = rawInput.trim().replaceAll(" +", " ");
//							if (element.getFilterExpressions().contains(newExpression)) {
//								// TODO: show error that it exists already
//							} else {
//								element.addFilterExpression(newExpression);
//								filterExpressionInput.setEnabled(false);
//								target.add(filterExpressionInput);
//								target.add(table);
//							}
//						}
//			        }
//			    };
//			    item.add(saveButton);
//			}
//		};
//		filterExpressionListView.setOutputMarkupId(true);
//		layoutForm.add(filterExpressionListView);
		
		add(layoutForm);
	}

	private void buildLeftHandSideExpressionInput() {
		
		leftHandSideExpression = element.getLeftHandSideExpression();
		
		AttributeExpressionTextField leftHandSideExpressionInput = new AttributeExpressionTextField("leftHandSideExpressionInput", new PropertyModel<String>(this, "leftHandSideExpression"), panel.getPatternTree());
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {

			private static final long serialVersionUID = 2339672763583311932L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				element.setLeftHandSideExpression(leftHandSideExpression);
			}
        };
        leftHandSideExpressionInput.add(onChangeAjaxBehavior);
		leftHandSideExpressionInput.setOutputMarkupId(true);
		layoutForm.add(leftHandSideExpressionInput);
	}

	private void buildFilterExpressionOperatorDropDownChoice() {		
		
		filterExpressionOperator = (FilterExpressionOperatorEnum) element.getValue();

		DropDownChoice<FilterExpressionOperatorEnum> filterExpressionOperatorDropDownChoice = new DropDownChoice<FilterExpressionOperatorEnum>("filterExpressionOperatorDropDownChoice", new PropertyModel<FilterExpressionOperatorEnum>(this, "filterExpressionOperator"), Arrays.asList(FilterExpressionOperatorEnum.values()), 
			new ChoiceRenderer<FilterExpressionOperatorEnum>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object getDisplayValue(FilterExpressionOperatorEnum element) {
					return element.getValue();
				}
			}
		);
		filterExpressionOperatorDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = -5452061293278720695L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				element.setValue(filterExpressionOperator);
				table.getSelectedElements().remove(element);
				target.add(table);
			}
		});
		filterExpressionOperatorDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(filterExpressionOperatorDropDownChoice);
	}

	private void buildRightHandSideExpressionInput() {
		
		rightHandSideExpression = element.getRightHandSideExpression();
		
		rightHandSideExpressionInput = new AttributeExpressionTextField("rightHandSideExpressionInput", new PropertyModel<String>(this, "rightHandSideExpression"), panel.getPatternTree()) {
			private static final long serialVersionUID = 5931500662562159353L;
			
			@Override
			public boolean isVisible() {
				return !isFilterExpressionOperatorWithValues();
			}
		};
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -4319775721171622640L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				element.setRightHandSideExpression(rightHandSideExpression);
			}
        };
        rightHandSideExpressionInput.add(onChangeAjaxBehavior);
		rightHandSideExpressionInput.setOutputMarkupId(true);
		layoutForm.add(rightHandSideExpressionInput);
	}

	private void buildRightHandSideValuesBasedComponents() {
		if (element.isRightHandSideRangeBased()) {
			typeOfValues = "range based";
		} else {
			typeOfValues = "list based";
		}
		
		typeOfValuesRadioChoice = new RadioChoice<String>("typeOfValuesRadioChoice", new PropertyModel<String>(this, "typeOfValues"), new ArrayList<String>(Arrays.asList("range based", "list based"))) {
			private static final long serialVersionUID = 2134778179415091830L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues();
			}
		};
		typeOfValuesRadioChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1479085520139021981L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (typeOfValues.equals("range based")) {
					element.setRightHandSideRangeBased(true);
				} else {
					element.setRightHandSideRangeBased(false);
				}
				target.add(table);
			}
		});
//		typeOfValuesRadioChoice.setSuffix("&nbsp;");
		typeOfValuesRadioChoice.setOutputMarkupId(true);
		layoutForm.add(typeOfValuesRadioChoice);
		
		buildRightHandSideRangeBasedComponent();
		buildRightHandSideListBasedComponent();
	}

	private void buildRightHandSideRangeBasedComponent() {
		final RangeElement rangeElement = element.getRightHandSideRangeOfValues();
		
		leftEndpoint = rangeElement.getLeftEndpoint();
		
		Label leftEndpointInputLabel = new Label("leftEndpointInputLabel", "Left endpoint") {
			private static final long serialVersionUID = 7258389748479790432L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		layoutForm.add(leftEndpointInputLabel);
		
		TextField<Integer> leftEndpointInput = new TextField<Integer>("leftEndpointInput", new PropertyModel<Integer>(this, "leftEndpoint")) {
			private static final long serialVersionUID = -8395573703349094639L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -872013504057729558L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				rangeElement.setLeftEndpoint(leftEndpoint);
			}
        };
        leftEndpointInput.add(onChangeAjaxBehavior);
        leftEndpointInput.setOutputMarkupId(true);
		layoutForm.add(leftEndpointInput);
		
		CheckBox leftEndpointOpenCheckBox = new CheckBox("leftEndpointOpenCheckbox", Model.of(rangeElement.isLeftEndpointOpen())) {
			private static final long serialVersionUID = 7637316203250432004L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		leftEndpointOpenCheckBox.setOutputMarkupId(true);
		layoutForm.add(leftEndpointOpenCheckBox);
		
		Label leftEndpointOpenLabel = new Label("leftEndpointOpenLabel", "open range") {
			private static final long serialVersionUID = 7258389748479790432L;

			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		layoutForm.add(leftEndpointOpenLabel);
		
		Label rightEndpointInputLabel = new Label("rightEndpointInputLabel", "Right endpoint") {	
			private static final long serialVersionUID = -6342679018392936070L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		layoutForm.add(rightEndpointInputLabel);
		
		rightEndpoint = rangeElement.getRightEndpoint();
		
		TextField<Integer> rightEndpointInput = new TextField<Integer>("rightEndpointInput", new PropertyModel<Integer>(this, "rightEndpoint")) {
			private static final long serialVersionUID = -1841846175664472901L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 7688362699342944026L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				rangeElement.setRightEndpoint(rightEndpoint);
			}
        };
        rightEndpointInput.add(onChangeAjaxBehavior);
        rightEndpointInput.setOutputMarkupId(true);
		layoutForm.add(rightEndpointInput);
		
		CheckBox rightEndpointOpenCheckBox = new CheckBox("rightEndpointOpenCheckbox", Model.of(rangeElement.isRightEndpointOpen())) {
			private static final long serialVersionUID = -8677379162792099253L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		rightEndpointOpenCheckBox.setOutputMarkupId(true);
		layoutForm.add(rightEndpointOpenCheckBox);
		
		Label rightEndpointOpenLabel = new Label("rightEndpointOpenLabel", "open range") {
			private static final long serialVersionUID = -9209530337733632817L;
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && element.isRightHandSideRangeBased();
			}
		};
		layoutForm.add(rightEndpointOpenLabel);
	}

	private void buildRightHandSideListBasedComponent() {
		List<String> expressions = new ArrayList<String>();
		expressions.addAll(element.getRightHandSideListOfValues());
		if (!expressions.contains("")) {
			expressions.add("");
		}
		
		ListView<String> filterExpressionListView = new ListView<String>("filterExpressionListView", expressions) { 

			private static final long serialVersionUID = -8698730823614901057L;
			
			@Override
			public boolean isVisible() {
				return isFilterExpressionOperatorWithValues() && !element.isRightHandSideRangeBased();
			}
			
			@Override
			protected void populateItem(ListItem<String> item) {
				String expression = item.getModelObject();
				buildComponents(item, expression);
			}
			
			private void buildComponents(ListItem<String> item, final String expression) {
				
				final TextField<String> filterExpressionInput = new TextField<String>("filterExpressionInput", new Model<String>()) {
					private static final long serialVersionUID = 5931500662562159353L;

					@Override
					public boolean isEnabled() {
						return expression.isEmpty();
					}
				};
				filterExpressionInput.setModelObject(expression);
				filterExpressionInput.setOutputMarkupId(true);
			    item.add(filterExpressionInput);
			    
				AjaxButton deleteButton = new AjaxButton("deleteButton", layoutForm) {
					private static final long serialVersionUID = 1609842059851860853L;

					@Override
					public boolean isVisible() {
						return !expression.isEmpty();
					}
					
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						element.getRightHandSideListOfValues().remove(filterExpressionInput.getModelObject());
						target.add(table);
			        }
			    };
			    item.add(deleteButton);
			    
//			    AjaxButton editButton = new AjaxButton("editButton", layoutForm) {
//					private static final long serialVersionUID = 1L;
//					@Override
//					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						element.removeFilterExpression(filterExpressionInput.getModelObject());
//						filterExpressionInput.setEnabled(true);
//						target.add(filterExpressionInput);
//			        }
//			    };
//			    item.add(editButton);
				
				AjaxButton saveButton = new AjaxButton("saveButton", layoutForm) {
					private static final long serialVersionUID = 1928837801022392147L;

					@Override
					public boolean isVisible() {
						return expression.isEmpty();
					}
					
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						String rawInput = filterExpressionInput.getModelObject();
						if (rawInput != null) {
							String newExpression = rawInput.trim().replaceAll(" +", " ");
							if (element.getRightHandSideListOfValues().contains(newExpression)) {
								// TODO: show error that it exists already
							} else {
								element.getRightHandSideListOfValues().add(newExpression);
								filterExpressionInput.setEnabled(false);
								target.add(filterExpressionInput);
								target.add(table);
							}
						}
			        }
			    };
			    item.add(saveButton);
			}
		};
		filterExpressionListView.setOutputMarkupId(true);
		layoutForm.add(filterExpressionListView);
	}
	
	private boolean isFilterExpressionOperatorWithValues() {
		return ((FilterExpressionOperatorEnum) element.getValue() == FilterExpressionOperatorEnum.IN) || ((FilterExpressionOperatorEnum) element.getValue()  == FilterExpressionOperatorEnum.NOT_IN);
	}
}
