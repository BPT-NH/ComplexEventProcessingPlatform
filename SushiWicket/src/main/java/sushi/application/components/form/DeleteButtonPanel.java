package sushi.application.components.form;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * This component creates a Button within a Panel, usable e.g. within a DataTable.
 * Button needs to have the wicket-id "button".
 */
public class DeleteButtonPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public DeleteButtonPanel(String id, Button button) throws Exception {
		super(id);
		Form<Void> form = new Form<Void>("form");
		if (button.getId().equals("button")) {
			form.add(button);
		} else {
			throw new Exception("Button-id needs to be 'button'!");
		}
		add(form);
		
	}

}
