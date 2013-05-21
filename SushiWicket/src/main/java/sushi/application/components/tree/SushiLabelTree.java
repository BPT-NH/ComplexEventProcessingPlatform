package sushi.application.components.tree;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.NestedTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * tree component for visualization of hierarchical elements
 * nodes are not selectable
 * 
 * @param <T> the type of nodes to be stored in the tree
 */
public class SushiLabelTree<T> extends NestedTree<T> {
	
	/**
	 * constructor
	 * 
	 * @param id wicket identifier used in the corresponding HTML file
	 * @param provider see sushi.application.components.tree.NestedTreeProvider
	 * @param state see sushi.application.components.tree.NestedTreeExpansionModel
	 */
	public SushiLabelTree(String id, ITreeProvider<T> provider, IModel<Set<T>> state) {
		super(id, provider, state);
		setTheme();
	}
	
	@Override
	protected Component newContentComponent(String id, IModel<T> model) {
		return new Label(id, model);
	}

	protected void setTheme() {
		this.add(new Behavior() {
			Behavior theme = new HumanTheme();
			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				theme.onComponentTag(component, tag);
			}
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				theme.renderHead(component, response);
			}
		});
	}

}
