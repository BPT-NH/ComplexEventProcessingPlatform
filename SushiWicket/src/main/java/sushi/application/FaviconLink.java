package sushi.application;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.ExternalLink;

public class FaviconLink extends ExternalLink {

	private static final long serialVersionUID = 1L;

	public FaviconLink(String id, String href) {
		super(id, href);
		add(new AttributeModifier("type", "image/x-icon"));
		add(new AttributeModifier("rel", "shortcut icon"));
	}
	

}
