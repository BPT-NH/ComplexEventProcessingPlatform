package sushi.application.pages.simulator;

import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.table.model.AbstractDataProvider;

/**
 * This panel is intended as a spacer in tables to let them render larger.
 */
public class EmptyPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public EmptyPanel(String id, final int entryId, final AbstractDataProvider dataProvider) {
		super(id);
	}

}
