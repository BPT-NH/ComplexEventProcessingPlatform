package sushi.application.pages.querying.bpmn.modal;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import sushi.application.components.form.BootstrapModal;

public class BPMNQueryEditorHelpModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private static final ResourceReference MODAL_SIZE_CSS = new PackageResourceReference(BootstrapModal.class, "modal_size.css");
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(MODAL_SIZE_CSS));
	}
	
    public BPMNQueryEditorHelpModal(String id) {
    	super(id, "Help: BPMN Query Editor");
	}
}
