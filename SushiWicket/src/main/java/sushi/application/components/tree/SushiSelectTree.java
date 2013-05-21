package sushi.application.components.tree;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.model.IModel;

/**
 * tree component for visualization of hierarchical elements
 * one node can be selected at a time
 * 
 * @param <T> the type of nodes to be stored in the tree
 */
public class SushiSelectTree<T> extends SushiLabelTree<T> {

	private static final long serialVersionUID = 1L;
	private IModel<T> selectedElement;
	private SushiSelectTree<T> selectTree;
	private ITreeProvider<T> provider;
	
	/**
	 * constructor
	 * 
	 * @param id wicket identifier used in the corresponding HTML file
	 * @param provider see sushi.application.components.tree.NestedTreeProvider
	 * @param state see sushi.application.components.tree.NestedTreeExpansionModel
	 */
	public SushiSelectTree(String id, ITreeProvider<T> provider, IModel<Set<T>> state) {
		super(id, provider, state);
		this.provider = provider;
		this.selectTree = this;
	}

	public T getSelectedElement() {
		if (selectedElement == null) {
			return null;
		}
		return selectedElement.getObject();
	}

	protected boolean isSelected(T element) {
        return selectedElement != null && selectedElement.equals(provider.model(element));
    }
	
	protected void select(T element, AbstractTree<T> tree, final AjaxRequestTarget target) {
		if (isSelected(element)) {
			selectedElement = null;
		} else {
			if (selectedElement != null) {
				tree.updateNode(selectedElement.getObject(), target);
				selectedElement = null;
			}
			selectedElement = provider.model(element);
		}
		tree.updateNode(element, target);
	}
	
	@Override
	protected Component newContentComponent(String id, IModel<T> model) {
		return new TreeLinkLabel<T>(id, this, model) {
			
			private static final long serialVersionUID = -5152504744506997839L;

			@Override
            protected void onClick(AjaxRequestTarget target) {
                selectTree.select(getModelObject(), selectTree, target);
                if (selectedElement != null) {
                    System.out.println("Selected element: " + selectedElement.getObject());
                } else {
                    System.out.println("No element selected");
                }
            }
			
			@Override
			protected boolean isSelected() {
				return selectTree.isSelected(getModelObject());
			}
		};
	}

}
