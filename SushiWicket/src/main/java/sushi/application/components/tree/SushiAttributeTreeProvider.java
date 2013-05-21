package sushi.application.components.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.event.attribute.SushiAttribute;

/**
 * wraps the given tree nodes
 */
public class SushiAttributeTreeProvider extends AbstractDataProvider implements ISortableTreeProvider<SushiAttribute, String> {

	private static final long serialVersionUID = 1L;
	private List<SushiAttribute> treeNodes;
	private List<SushiAttribute> selectedTreeNodes;
	
	/**
	 * constructor
	 */
	public SushiAttributeTreeProvider() {
		this.treeNodes = new ArrayList<SushiAttribute>();
		this.selectedTreeNodes = new ArrayList<SushiAttribute>();
	}
	
	/**
	 * constructor
	 * 
	 * @param list root nodes of the tree, child nodes are accessed by this component automatically
	 */
	public SushiAttributeTreeProvider(List<SushiAttribute> list) {
		this();
		this.treeNodes = list;
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends SushiAttribute> getRoots() {
		return treeNodes.iterator();
	}

	@Override
	public boolean hasChildren(SushiAttribute node) {
		return node.getParent() == null || !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends SushiAttribute> getChildren(SushiAttribute node) {
		return node.getChildren().iterator();
	}

	@Override
	public SushiAttributeTreeModel model(SushiAttribute node) {
		return new SushiAttributeTreeModel(treeNodes, node);
	}

	@Override
	public void selectEntry(int entryId) {
		for (SushiAttribute node : treeNodes) {
			if (node.getID() == entryId) {
				selectedTreeNodes.add(node);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (SushiAttribute node : treeNodes) {
			if (node.getID() == entryId) {
				selectedTreeNodes.remove(node);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (SushiAttribute node : selectedTreeNodes) {
			if (node.getID() == entryId) {
				return true;
			}
		}
		return false;
	}
	
	public List<SushiAttribute> getSelectedAttributes() {
		return selectedTreeNodes;
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public Object getEntry(int entryId) {
		for (SushiAttribute node : treeNodes) {
			if (node.getID() == entryId) {
				return node;
			}
		}
		return null;
	}
}
