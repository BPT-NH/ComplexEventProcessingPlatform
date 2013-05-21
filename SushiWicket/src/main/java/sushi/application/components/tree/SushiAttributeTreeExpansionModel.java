package sushi.application.components.tree;

import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;

import sushi.event.attribute.SushiAttribute;

/**
 * model that wraps the expansion state handler of a tree component
 */
public class SushiAttributeTreeExpansionModel extends AbstractReadOnlyModel<Set<SushiAttribute>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Set<SushiAttribute> getObject() {
		return SushiAttributeTreeExpansion.get();
	}
}
