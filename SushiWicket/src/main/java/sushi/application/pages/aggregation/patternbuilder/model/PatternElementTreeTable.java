package sushi.application.pages.aggregation.patternbuilder.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.model.IModel;

import sushi.application.components.tree.SushiMultiSelectTreeTable;
import sushi.application.components.tree.TreeLinkLabel;
import sushi.application.pages.aggregation.patternbuilder.PatternBuilderPanel;
import sushi.event.collection.SushiTreeElement;

/**
 * tree table component for visualization of hierarchical elements
 * nodes are selectable
 * 
 * @param <T> the type of nodes to be stored in the tree
 * @param <S> the type of the sort property
 */
public class PatternElementTreeTable extends SushiMultiSelectTreeTable<SushiTreeElement<Serializable>, String> {
	
	private static final long serialVersionUID = 1L;
	private PatternElementTreeTable patternElementTree;
	private PatternBuilderPanel patternBuilderPanel;

	/**
	 * constructor
	 * 
	 * @param id wicket identifier used in the corresponding HTML file
	 * @param columns list of IColumn objects
	 * @param provider provider see sushi.application.components.tree.NestedTreeProvider
	 * @param rowsPerPage number of rows per page
	 * @param state state see sushi.application.components.tree.NestedTreeExpansionModel
	 */
	public PatternElementTreeTable(String id, List<? extends IColumn<SushiTreeElement<Serializable>, String>> columns, ITreeProvider<SushiTreeElement<Serializable>> provider, long rowsPerPage, IModel<Set<SushiTreeElement<Serializable>>> state, PatternBuilderPanel patternBuilderPanel) {
		super(id, columns, provider, rowsPerPage, state);
		this.patternElementTree = this;
		this.patternBuilderPanel = patternBuilderPanel;
	}
	
	@Override
	protected void toggle(SushiTreeElement<Serializable> element, AbstractTree<SushiTreeElement<Serializable>> tree, final AjaxRequestTarget target) {
		if (isSelected(element)) {
			selectedElements.remove(element);
		} else if (selectedElements.size() <= 1) {
			SushiTreeElement<Serializable> selectedElement;
			if (!selectedElements.isEmpty()) {
				selectedElement = selectedElements.iterator().next();
				if (selectedElement.getLevel() != element.getLevel()
						|| ((selectedElement.hasParent() && element.hasParent()) 
								&& !selectedElement.getParent().equals(element.getParent()))) {
					selectedElements.remove(selectedElement);
					tree.updateNode(selectedElement, target);
				}
			}
			selectedElements.add(element);
		}
		tree.updateNode(element, target);
	}
	
	@Override
	protected Component newContentComponent(String id, IModel<SushiTreeElement<Serializable>> model) {
		return new TreeLinkLabel<SushiTreeElement<Serializable>>(id, this, model) {

			private static final long serialVersionUID = 4384788964095089896L;

			@Override
            protected void onClick(AjaxRequestTarget target) {
                patternElementTree.toggle(getModelObject(), patternElementTree, target);
				patternBuilderPanel.updateOnTreeElementSelection(target);
            }
			
			@Override
			protected boolean isSelected() {
				return patternElementTree.isSelected(getModelObject());
			}
		};
	}
}
