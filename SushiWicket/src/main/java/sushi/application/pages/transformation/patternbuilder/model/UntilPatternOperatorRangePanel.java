package sushi.application.pages.transformation.patternbuilder.model;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.transformation.element.PatternOperatorElement;
import sushi.transformation.element.RangeElement;

public class UntilPatternOperatorRangePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Boolean leftEndpointEnabled;
	private Boolean rightEndpointEnabled;
	private Form<Void> layoutForm;
	private int leftEndpoint;
	private AjaxCheckBox leftEndpointEnabledCheckBox;
	private TextField<Integer> leftEndpointInput;
	private int rightEndpoint;
	private TextField<Integer> rightEndpointInput;
	private AjaxCheckBox rightEndpointEnabledCheckBox;

	public UntilPatternOperatorRangePanel(String id, PatternOperatorElement element) {
		super(id);
		
		this.leftEndpointEnabled = true;
		this.rightEndpointEnabled = true;
		
		layoutForm = new Form<Void>("layoutForm");
		
		final RangeElement rangeElement = element.getRangeElement();
		
		leftEndpoint = rangeElement.getLeftEndpoint();
		
		leftEndpointInput = new TextField<Integer>("leftEndpointInput", new PropertyModel<Integer>(this, "leftEndpoint")) {
			private static final long serialVersionUID = -3575218222042227551L;
			@Override
			public boolean isEnabled() {
				return leftEndpointEnabledCheckBox.getModelObject();
			}
		};
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 8789007504544472059L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				rangeElement.setLeftEndpoint(leftEndpoint);
			}
        };
        leftEndpointInput.add(onChangeAjaxBehavior);
        leftEndpointInput.setOutputMarkupId(true);
		layoutForm.add(leftEndpointInput);
		
		leftEndpointEnabledCheckBox = new AjaxCheckBox("leftEndpointEnabledCheckbox", Model.of(rangeElement.getLeftEndpoint() != -1)) {
			private static final long serialVersionUID = -8207035371422899809L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (leftEndpointEnabledCheckBox.getModelObject()) {
					leftEndpoint = 0;
				} else {
					leftEndpoint = -1;
				}
				rangeElement.setLeftEndpoint(leftEndpoint);
				target.add(leftEndpointInput);
			}
		};
		leftEndpointEnabledCheckBox.setOutputMarkupId(true);
		layoutForm.add(leftEndpointEnabledCheckBox);
		
		rightEndpoint = rangeElement.getRightEndpoint();
		
		rightEndpointInput = new TextField<Integer>("rightEndpointInput", new PropertyModel<Integer>(this, "rightEndpoint")) {
			private static final long serialVersionUID = 4121692531784473397L;
			@Override
			public boolean isEnabled() {
				return rightEndpointEnabledCheckBox.getModelObject();
			}
		};
		onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 68845840865685483L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				rangeElement.setRightEndpoint(rightEndpoint);
			}
        };
        rightEndpointInput.add(onChangeAjaxBehavior);
        rightEndpointInput.setOutputMarkupId(true);
		layoutForm.add(rightEndpointInput);
	
		rightEndpointEnabledCheckBox = new AjaxCheckBox("rightEndpointEnabledCheckbox", Model.of(rangeElement.getLeftEndpoint() != -1)) {
			private static final long serialVersionUID = -7937834776333473869L;
			protected void onUpdate(AjaxRequestTarget target) {
				if (rightEndpointEnabledCheckBox.getModelObject()) {
					rightEndpoint = 1;
				} else {
					rightEndpoint = -1;
				}
				rangeElement.setRightEndpoint(rightEndpoint);
				target.add(rightEndpointInput);
			}
		};
		rightEndpointEnabledCheckBox.setOutputMarkupId(true);
		layoutForm.add(rightEndpointEnabledCheckBox);
		
		add(layoutForm);
	}
}
