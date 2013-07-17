package sushi.application.pages.querying;

import org.apache.wicket.markup.html.basic.MultiLineLabel;

import sushi.application.components.form.BootstrapModal;

/**
 * This class is a Modal that displays the help-text for query creation.
 */
public class QueryEditorHelpModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	
    public QueryEditorHelpModal(String id, String helpText) {
    	super(id, "Help: Query Editor");
    	add(new MultiLineLabel("helpTextLabel", helpText));
	}
}
