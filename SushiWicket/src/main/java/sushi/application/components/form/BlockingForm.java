package sushi.application.components.form;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class BlockingForm extends Form<Void> {

	private static final long serialVersionUID = 1L;
	private JavaScriptResourceReference blockPageJS = new JavaScriptResourceReference(WarnOnExitForm.class, "blockUI.js");

	public BlockingForm(String id) {
		super(id);
	}

	public BlockingForm(String id, IModel model) {
		super(id, model);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptReferenceHeaderItem.forReference(blockPageJS));
	}

}