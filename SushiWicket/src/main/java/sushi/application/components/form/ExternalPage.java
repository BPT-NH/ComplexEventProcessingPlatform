package sushi.application.components.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * This component can be used to load an external page with Wicket.
 * The specified URL will be loaded.
 * In the markup there should be an iFrame for these component.
 * @author micha
 *
 */
public class ExternalPage extends WebMarkupContainer {

	private static final long serialVersionUID = 1L;
	private String URL;

	public ExternalPage(String id, String URL) {
		super(id);
		this.URL = URL;
	}
	
	/**
	 * Handles this frame's tag.
	 * 
	 * @param tag
	 *            the component tag
	 * @see org.apache.wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected final void onComponentTag(final ComponentTag tag){
		checkComponentTag(tag, "iframe");

		// generate the src attribute
		tag.put("src", URL);

		super.onComponentTag(tag);
	}

	

	@Override
	protected boolean getStatelessHint()
	{	
		return false;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}


}
