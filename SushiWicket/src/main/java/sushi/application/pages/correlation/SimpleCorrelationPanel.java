package sushi.application.pages.correlation;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.WarnOnExitForm;
import sushi.event.attribute.SushiAttribute;

/**
 * Panel representing the content panel for the first tab.
 */
public class SimpleCorrelationPanel extends Panel {
	
	private ListMultipleChoice<SushiAttribute> correlationAttributesSelect;
	private ArrayList<SushiAttribute> correlationAttributes = new ArrayList<SushiAttribute>();
	private ArrayList<SushiAttribute> selectedCorrelationAttributes = new ArrayList<SushiAttribute>();
	
	public SimpleCorrelationPanel(String id) {
		super(id);
		
		Form<Void> form = new WarnOnExitForm("simpleCorrelationForm");
		add(form);
		
		correlationAttributesSelect = new ListMultipleChoice<SushiAttribute>("correlationEventTypesSelect", new Model<ArrayList<SushiAttribute>>(selectedCorrelationAttributes), correlationAttributes);
		correlationAttributesSelect.setOutputMarkupId(true);
		
		correlationAttributesSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			@Override 
			protected void onUpdate(AjaxRequestTarget target) { 
				System.out.println(selectedCorrelationAttributes);
			}
		});
		
		form.add(correlationAttributesSelect);
	}
	
	public void clearCorrelationAttributesSelect() {
		correlationAttributesSelect.setChoices(new ArrayList<SushiAttribute>());
	}

	public ListMultipleChoice<SushiAttribute> getCorrelationAttributesSelect() {
		return correlationAttributesSelect;
	}

	public void setCorrelationAttributesSelect(ListMultipleChoice<SushiAttribute> correlationAttributesSelect) {
		this.correlationAttributesSelect = correlationAttributesSelect;
	}

	public ArrayList<SushiAttribute> getSelectedCorrelationAttributes() {
		return selectedCorrelationAttributes;
	}

	public void setCorrelationAttributes(ArrayList<SushiAttribute> correlationAttributes) {
		this.correlationAttributes = correlationAttributes;
	}

};
