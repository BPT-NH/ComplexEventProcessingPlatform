package sushi.application.components.tree;

import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * tree table component for visualization of hierarchical elements
 * nodes are selectable
 * 
 * @param <T> the type of nodes to be stored in the tree
 * @param <S> the type of the sort property
 */
public class SushiSelectTreeTable<T, S> extends SushiLabelTreeTable<T, S> {
	
	private static final long serialVersionUID = 1L;
	private IModel<T> selectedElement;
	private SushiSelectTreeTable<T, S> selectTree;
	private ITreeProvider<T> provider;

	/**
	 * constructor
	 * 
	 * @param id wicket identifier used in the corresponding HTML file
	 * @param columns list of IColumn objects
	 * @param provider provider see sushi.application.components.tree.NestedTreeProvider
	 * @param rowsPerPage number of rows per page
	 * @param state state see sushi.application.components.tree.NestedTreeExpansionModel
	 */
	public SushiSelectTreeTable(String id, List<? extends IColumn<T, S>> columns, ITreeProvider<T> provider, long rowsPerPage, IModel<Set<T>> state) {
		super(id, columns, provider, rowsPerPage, state);
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

			private static final long serialVersionUID = 1569507778098604348L;

			@Override
            protected void onClick(AjaxRequestTarget target) {
                selectTree.select(getModelObject(), selectTree, target);
            }
			
			@Override
			protected boolean isSelected() {
				return selectTree.isSelected(getModelObject());
			}
		};
	}
}
