package sushi.application.components.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.Bootstrap;

public abstract class BootstrapModal extends Panel {

	private static final long serialVersionUID = 1L;
	private String heading;
	private String id;
	
	Model<String> visibleModel = new Model<String>("display: block;");
	
	@Override
	public void renderHead(IHeaderResponse response) {
	    super.renderHead(response);
		Bootstrap.renderHead(response);
	}

	public BootstrapModal(String id, String heading) {
		super(id);
		this.id = id;
		this.heading = heading;
		this.add(AttributeModifier.replace("id", id));
		this.add(AttributeModifier.replace("class", "modal hide fade in"));
		this.add(AttributeModifier.replace("style", "display: none;"));
		buildMainLayout();
	}

	private void buildMainLayout() {
		add(new Label("modalHeadline", heading));
	}

	public void show(AjaxRequestTarget target) {
		target.appendJavaScript("$(\"#" + id + "\").modal('toggle'); ");
	}
	
	public void close(AjaxRequestTarget target) {
		target.appendJavaScript("$(\"#" + id + "\").modal('hide'); ");
	}

}
