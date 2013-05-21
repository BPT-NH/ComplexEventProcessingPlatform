package sushi.application.components.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiTree;
import sushi.event.collection.SushiTreeElement;

/**
 * wraps the given tree nodes
 *
 * @param <T>
 */
/**
 * @author micha
 *
 * @param <T>
 */
public class TreeTableProvider<T> extends AbstractDataProvider implements ISortableTreeProvider<SushiTreeElement<T>, String> {

	private static final long serialVersionUID = 1L;
	private List<SushiTreeElement<T>> rootElements;
	private List<SushiTreeElement<T>> selectedElements = new ArrayList<SushiTreeElement<T>>();
	
	public TreeTableProvider() {
		this.rootElements = new ArrayList<SushiTreeElement<T>>();
	}
	
	/**
	 * constructor
	 * 
	 * @param treeNodes root nodes of the tree, child nodes are accessed by this component automatically
	 */
	public TreeTableProvider(List<SushiTreeElement<T>> treeNodes) {
		this.rootElements = treeNodes;
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends SushiTreeElement<T>> getRoots() {
		return getRootElements().iterator();
	}
	
	private List<SushiTreeElement<T>> getRootElements() {
		return rootElements;
	}
	
	private List<SushiTreeElement<T>> getElements() {
		List<SushiTreeElement<T>> elements = new ArrayList<SushiTreeElement<T>>();
		for (SushiTreeElement<T> root : rootElements) {
			if (root.hasChildren()) {
				addElementToSet(root, elements);
			}
			elements.add(root);
		}
		return elements;
	}
	
	public void setRootElements(List<SushiTreeElement<T>> rootElements) {
		this.rootElements = rootElements;
	}

	private void addElementToSet(SushiTreeElement<T> element, List<SushiTreeElement<T>> elements) {
		for (SushiTreeElement<T> child : element.getChildren()) {
			if (child.hasChildren()) {
				addElementToSet(child, elements);
			}
			elements.add(child);
		}
	}

	@Override
	public boolean hasChildren(SushiTreeElement<T> node) {
		return node.hasChildren();
	}

	@Override
	public Iterator<? extends SushiTreeElement<T>> getChildren(SushiTreeElement<T> node) {
		return node.getChildren().iterator();
	}

	@Override
	public TreeElementModel<T> model(SushiTreeElement<T> node) {
		return new TreeElementModel<T>(getRootElements(), node);
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public void selectEntry(int entryId) {
		for (SushiTreeElement<T> treeTableElement : getElements()) {
			if(treeTableElement.getID() == entryId) {
				selectedElements.add(treeTableElement);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (SushiTreeElement<T> treeTableElement : getElements()) {
			if(treeTableElement.getID() == entryId) {
				selectedElements.remove(treeTableElement);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (SushiTreeElement<T> treeTableElement : selectedElements) {
			if(treeTableElement.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getEntry(int entryId) {
		for (SushiTreeElement<T> treeTableElement : getElements()) {
			if(treeTableElement.getID() == entryId) {
				return treeTableElement;
			}
		}
		return null;
	}
	
	/**
	 * Returns the next free ID for an new element.
	 * @return
	 */
	public int getNextID() {
		int highestNumber = 0;
		for(SushiTreeElement<T> element : getElements()) {
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}

	public List<SushiTreeElement<T>> getTreeTableElements() {
		return getElements();
	}

	public List<SushiTreeElement<T>> getSelectedTreeTableElements() {
		return selectedElements;
	}
	
	public List<SushiTreeElement<T>> getRootTreeTableElements() {
		return getRootElements();
	}
	
	public SushiTree<T> getModelAsTree() {
		SushiTree<T> tree = new SushiTree<T>();
		for(SushiTreeElement<T> element : rootElements){
			addElementToTree(null, element, tree);
		}
		return tree;
	}

	private void addElementToTree(SushiTreeElement<T> parent, SushiTreeElement<T> element, SushiTree<T> tree) {
		if (parent != null) {
			tree.addChild(parent.getValue(), element.getValue());
		} else {
			tree.addChild(null, element.getValue());
		}
		for (SushiTreeElement<T> child : element.getChildren()) {
			addElementToTree(element, child, tree);
		}
	}
	
}
