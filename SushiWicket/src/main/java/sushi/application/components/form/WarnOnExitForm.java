package sushi.application.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * This is a form, that raises an alert, if there are unsaved changes in the form.
 * The proper way is to call submit to avoid the raising of an alert.
 * @author micha
 *
 */
public class WarnOnExitForm extends BlockingForm {
	
	static final JavaScriptResourceReference warnOnExit_JS = new JavaScriptResourceReference(WarnOnExitForm.class, "WarnOnExit.js");
	
	public WarnOnExitForm(String id) {
		super(id);
	}
	
	@Override
	public MarkupContainer add(Component... childs) {
		super.add(childs);
		for(Component component : childs){
			if(component instanceof Button){
				component.add(new AttributeAppender("onclick", new Model("dontConfirm()"), ";"));
			} else if (component instanceof FormComponent){
				component.add(new AttributeAppender("onChange", new Model("setDirty()"), ";"));
			}
		}
		return this;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptReferenceHeaderItem.forReference(warnOnExit_JS));
	}

}
