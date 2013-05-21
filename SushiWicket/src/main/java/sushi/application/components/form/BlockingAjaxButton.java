package sushi.application.components.form;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public class BlockingAjaxButton extends AjaxButton {

	private static final long serialVersionUID = 1L;

	public BlockingAjaxButton(String id, Form<?> form) {
		super(id, form);
		addOnClickBehavior();
	}

	public BlockingAjaxButton(String id, Model<String> model) {
		super(id, model);
		addOnClickBehavior();

	}

	private void addOnClickBehavior(){

		add(new AjaxEventBehavior("onclick") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				target.appendJavaScript("$.blockUI();");
			}
		});
	}

	@Override
	public void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		target.appendJavaScript("$.unblockUI();");				
	}
	
}