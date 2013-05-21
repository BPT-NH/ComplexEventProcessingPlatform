package sushi.application.components.table;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import sushi.application.pages.input.model.EventAttributeProvider;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;

public class AttributeTypeDropDownChoicePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private List<SushiAttributeTypeEnum> attributeTypes = Arrays.asList(SushiAttributeTypeEnum.values());
	protected SushiAttributeTypeEnum attributeType;
	
	public AttributeTypeDropDownChoicePanel(String id, final SushiAttribute attribute, final boolean dropDownChoiceEnabled, final EventAttributeProvider dataProvider) {
		super(id);
		Form<Void> layoutForm = new Form<Void>("layoutForm");

		attributeType = attribute.getType();
		
		DropDownChoice<SushiAttributeTypeEnum> attributeTypeDropDownChoice = new DropDownChoice<SushiAttributeTypeEnum>("attributeTypeDropDownChoice", new PropertyModel<SushiAttributeTypeEnum>(this, "attributeType"), attributeTypes);
		attributeTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				attribute.setType(attributeType);
			}
		});
		
		attributeTypeDropDownChoice.setEnabled(dropDownChoiceEnabled);
		
		layoutForm.add(attributeTypeDropDownChoice);		
		add(layoutForm);
	}
}
