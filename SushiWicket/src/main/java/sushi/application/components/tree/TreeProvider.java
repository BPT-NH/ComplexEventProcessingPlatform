package sushi.application.components.tree;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;

import sushi.event.collection.SushiTreeElement;

/**
 * wraps the given tree nodes
 *
 * @param <T>
 */
public class TreeProvider<T> implements ITreeProvider<SushiTreeElement<T>> {

	private static final long serialVersionUID = 1L;
	private ArrayList<SushiTreeElement<T>> treeNodes;
	
	/**
	 * constructor
	 * 
	 * @param treeNodes root nodes of the tree, child nodes are accessed by this component automatically
	 */
	public TreeProvider(ArrayList<SushiTreeElement<T>> treeNodes) {
		this.treeNodes = treeNodes;
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends SushiTreeElement<T>> getRoots() {
		return treeNodes.iterator();
	}
	
	public void setRoots(ArrayList<SushiTreeElement<T>> treeNodes) {
		this.treeNodes = treeNodes;
	}

	@Override
	public boolean hasChildren(SushiTreeElement<T> node) {
		return node.getParent() == null || !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends SushiTreeElement<T>> getChildren(SushiTreeElement<T> node) {
		return node.getChildren().iterator();
	}

	@Override
	public TreeElementModel<T> model(SushiTreeElement<T> node) {
		return new TreeElementModel<T>(treeNodes, node);
	}
	
	/**
	 * Returns the next free ID for an new element.
	 * @return
	 */
	public int getNextID() {
		int highestNumber = 0;
		for(SushiTreeElement<T> element : treeNodes) {
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}
	
}
