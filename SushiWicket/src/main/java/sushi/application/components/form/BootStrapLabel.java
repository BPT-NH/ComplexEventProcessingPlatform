package sushi.application.components.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This class provides an extension to the {@link Label} to render the text with Bootstrap text emphasis.
 * The emphasis type must be provided with {@link BootStrapTextEmphasisClass}.
 * @author micha
 */
public class BootStrapLabel extends Label {

	private static final long serialVersionUID = 1L;
	private BootStrapTextEmphasisClass textEmphasisClass;

	public BootStrapLabel(String id, String label, BootStrapTextEmphasisClass textEmphasisClass) {
		super(id, label);
		this.textEmphasisClass = textEmphasisClass;
	}
	
	
	
	@Override
	protected final void onComponentTag(final ComponentTag tag){
		checkComponentTag(tag, "span");

		// generate the class attribute
		tag.put("class", textEmphasisClass.getClassValue());

		super.onComponentTag(tag);
	}



	/**
	 * Returns the {@link BootStrapTextEmphasisClass} for the current label.
	 * @return
	 */
	public BootStrapTextEmphasisClass getTextEmphasisClass() {
		return textEmphasisClass;
	}


	/**
	 * Sets the {@link BootStrapTextEmphasisClass} for the current label.
	 * @return
	 */
	public void setTextEmphasisClass(BootStrapTextEmphasisClass textEmphasisClass) {
		this.textEmphasisClass = textEmphasisClass;
	}

}
