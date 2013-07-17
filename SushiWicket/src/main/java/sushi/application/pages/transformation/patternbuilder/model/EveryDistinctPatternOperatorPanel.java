package sushi.application.pages.transformation.patternbuilder.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.pages.transformation.AdvancedTransformationRuleEditorPanel;
import sushi.correlation.CorrelationRule;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.transformation.element.PatternOperatorElement;

public class EveryDistinctPatternOperatorPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Form<Void> layoutForm;
	private WebMarkupContainer distinctAttributesMarkupContainer;
	private List<String> distinctAttributes;

	public EveryDistinctPatternOperatorPanel(String id, PatternOperatorElement element, final AdvancedTransformationRuleEditorPanel panel) {
		super(id);
		
		layoutForm = new Form<Void>("layoutForm");
		
		distinctAttributes = element.getDistinctAttributes();
		
		AjaxButton addDistinctAttributeButton = new AjaxButton("addDistinctAttributeButton", layoutForm) {
			private static final long serialVersionUID = -118988274912205531L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				distinctAttributes.add(new String());
				target.add(distinctAttributesMarkupContainer);
			}
		};
		addDistinctAttributeButton.setOutputMarkupId(true);
		layoutForm.add(addDistinctAttributeButton);
		
		ListView<String> distinctAttributesListView = new ListView<String>("distinctAttributesListView", distinctAttributes) {
			private static final long serialVersionUID = 4168798264053898499L;
			
			@Override
			protected void populateItem(final ListItem<String> item) {
				
				final AttributeExpressionTextField distinctAttributeInput = new AttributeExpressionTextField("distinctAttributeInput", new Model<String>(), panel.getPatternBuilderPanel().getPatternTree());
				OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {

					private static final long serialVersionUID = 2339672763583311932L;
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						item.setModelObject(distinctAttributeInput.getModelObject());
					}
		        };
		        distinctAttributeInput.setModelObject(item.getModelObject());
		        distinctAttributeInput.add(onChangeAjaxBehavior);
		        distinctAttributeInput.setOutputMarkupId(true);
				item.add(distinctAttributeInput);
				
				AjaxButton removeDistinctButton = new AjaxButton("removeDistinctAttributeButton", layoutForm) {
					private static final long serialVersionUID = -4244320500409194238L;
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						distinctAttributes.remove(item.getModelObject());
						target.add(distinctAttributesMarkupContainer);
					}
				};
				item.add(removeDistinctButton);
		    }
		};
		
		distinctAttributesMarkupContainer = new WebMarkupContainer("distinctAttributesMarkupContainer");
		distinctAttributesMarkupContainer.add(distinctAttributesListView);
		distinctAttributesMarkupContainer.setOutputMarkupId(true);
		layoutForm.addOrReplace(distinctAttributesMarkupContainer);
		
		add(layoutForm);
	}
}
