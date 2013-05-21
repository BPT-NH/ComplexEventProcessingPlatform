package sushi.application.pages.aggregation.patternbuilder.model;

import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.aggregation.collection.SushiPatternTree;

public class CriteriaValuePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Form<Void> layoutForm;
	private String attributeExpression;
	private Map<String, String> criteriaAttributesAndValues;
	private SushiPatternTree patternTree;

	public CriteriaValuePanel(String id, String attributeExpression, Map<String, String> criteriaAttributesAndValues, SushiPatternTree patternTree) {
		super(id);
		
		this.attributeExpression = attributeExpression;
		this.criteriaAttributesAndValues = criteriaAttributesAndValues;
		this.patternTree = patternTree;
		
		layoutForm = new Form<Void>("layoutForm");
		buildCriteriaValueInput();
		
		add(layoutForm);
	}

	private void buildCriteriaValueInput() {
		final AttributeExpressionTextField criteriaValueInput = new AttributeExpressionTextField("criteriaValueInput", new Model<String>(), patternTree);
		criteriaValueInput.setOutputMarkupId(true);
		
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -5737941362786901904L;
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (criteriaValueInput.getModelObject() == null || criteriaValueInput.getModelObject().trim().isEmpty()) {
					criteriaAttributesAndValues.remove(attributeExpression);
				} else {
					criteriaAttributesAndValues.put(attributeExpression, criteriaValueInput.getModelObject());
				}
			}
        };
        criteriaValueInput.add(onChangeAjaxBehavior);
        criteriaValueInput.setModelObject(criteriaAttributesAndValues.get(attributeExpression));
		
		layoutForm.add(criteriaValueInput);
	}
}
