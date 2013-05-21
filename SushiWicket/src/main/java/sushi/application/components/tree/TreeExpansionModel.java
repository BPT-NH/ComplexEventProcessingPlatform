package sushi.application.components.tree;

import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;

import sushi.event.collection.SushiTreeElement;

/**
 * model that wraps the expansion state handler of a tree component
 */
public class TreeExpansionModel<T> extends AbstractReadOnlyModel<Set<SushiTreeElement<T>>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Set<SushiTreeElement<T>> getObject() {
		return TreeExpansion.get();
	}
}
