package sushi.application.pages.aggregation.patternbuilder.model;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import sushi.aggregation.element.EventTypeElement;
import sushi.application.pages.aggregation.AdvancedRuleEditorPanel;


public class EventTypeAliasPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private EventTypeElement element;
	private String alias;
	private Form<Void> layoutForm;
	private AdvancedRuleEditorPanel panel;

	public EventTypeAliasPanel(String id, EventTypeElement element, AdvancedRuleEditorPanel panel) {
		super(id);
		
		this.element = element;
		this.alias = element.getAlias();
		this.panel = panel;
		
		layoutForm = new Form<Void>("layoutForm");
		buildEventTypeAliasInput();
		
		add(layoutForm);
	}

	private void buildEventTypeAliasInput() {
		TextField<String> eventTypeAliasInput = new TextField<String>("eventTypeAliasInput", new PropertyModel<String>(this, "alias"));
		eventTypeAliasInput.setOutputMarkupId(true);
		
		OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = -1427433442511094442L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO: make sure that alias does not already exist somewhere
				element.setAlias(alias);
				target.add(panel.getAttributeTreePanel().getAttributeTreeTable());
			}
        };
        eventTypeAliasInput.add(onChangeAjaxBehavior);
		
		layoutForm.add(eventTypeAliasInput);
	}
}
