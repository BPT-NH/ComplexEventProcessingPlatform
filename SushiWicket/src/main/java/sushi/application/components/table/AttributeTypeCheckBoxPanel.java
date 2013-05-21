package sushi.application.components.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.input.model.EventAttributeProvider;
import sushi.event.attribute.SushiAttribute;

public class AttributeTypeCheckBoxPanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	public AttributeTypeCheckBoxPanel(String id, final SushiAttribute attribute, final boolean checkBoxEnabled, final EventAttributeProvider dataProvider, final Component tableContainer) {
		super(id);
		Form<Void> form = new Form<Void>("layoutForm");
		
		CheckBox checkBox = new CheckBox("attributeTypeCheckBox", Model.of(dataProvider.isEntrySelected(attribute)));
		checkBox.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				if (dataProvider.isEntrySelected(attribute)) {
					dataProvider.deselectEntry(attribute);
				} else if (!dataProvider.isEntrySelected(attribute)) {
					dataProvider.selectEntry(attribute);
				}
				target.add(tableContainer);
			}
		});
		
		checkBox.setEnabled(checkBoxEnabled);
		
		form.add(checkBox);
		add(form);
	}
}
