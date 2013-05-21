package sushi.application;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class StringChoiceRenderer implements IChoiceRenderer<String> {

	@Override
	public Object getDisplayValue(final String t) {
		return t;
	}

	@Override
	public String getIdValue(final String t, final int i) {
		return t;
	}

}
