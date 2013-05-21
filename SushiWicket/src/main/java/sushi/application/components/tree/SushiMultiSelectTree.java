package sushi.application.components.tree;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.ProviderSubset;
import org.apache.wicket.model.IModel;

/**
 * tree component for visualization of hierarchical elements
 * multiple can be selected at a time
 * 
 * @param <T> the type of nodes to be stored in the tree
 */
public class SushiMultiSelectTree<T> extends SushiLabelTree<T> {

	private static final long serialVersionUID = 1L;
	protected ProviderSubset<T> selectedElements;
	private SushiMultiSelectTree<T> multiSelectTree;

	/**
	 * constructor
	 * 
	 * @param id wicket identifier used in the corresponding HTML file
	 * @param provider see sushi.application.components.tree.NestedTreeProvider
	 * @param state see sushi.application.components.tree.NestedTreeExpansionModel
	 */
	public SushiMultiSelectTree(String id, ITreeProvider<T> provider,	IModel<Set<T>> state) {
		super(id, provider, state);
		this.multiSelectTree = this;
		this.selectedElements = new ProviderSubset<T>(provider, false);
	}
	
	public ProviderSubset<T> getSelectedElements() {
		return selectedElements;
	}

	protected boolean isSelected(T element) {
        return selectedElements.contains(element);
    }
	
	protected void toggle(T element, AbstractTree<T> tree, final AjaxRequestTarget target) {
		if (isSelected(element)) {
			selectedElements.remove(element);
		} else {
			selectedElements.add(element);
		}
		tree.updateNode(element, target);
	}
	
	@Override
	protected Component newContentComponent(String id, IModel<T> model) {
		return new TreeLinkLabel<T>(id, this, model) {
			
			private static final long serialVersionUID = 9187088363688868766L;

			@Override
            protected void onClick(AjaxRequestTarget target) {
                multiSelectTree.toggle(getModelObject(), multiSelectTree, target);
            }
			
			@Override
			protected boolean isSelected() {
				return multiSelectTree.isSelected(getModelObject());
			}
		};
	}

}
