package sushi.application.components.tree;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import sushi.event.collection.SushiTreeElement;

public class TreeElementModel<T> extends LoadableDetachableModel<SushiTreeElement<T>> {
	
	private static final long serialVersionUID = 1L;
	private final String ID;
	private List<SushiTreeElement<T>> treeNodes;
	
	public TreeElementModel(List<SushiTreeElement<T>> treeNodes, SushiTreeElement<T> node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getID() + node.getXPath();
	}

	public String getID() {
		return ID;
	}

	@Override
	protected SushiTreeElement<T> load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof TreeElementModel) {
			return ((TreeElementModel<T>) object).getID().equals(ID);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	private SushiTreeElement<T> findTreeElement(List<SushiTreeElement<T>> treeNodes, String id) {
		for (SushiTreeElement<T> treeElement : treeNodes) {
			if ((treeElement.getID() + treeElement.getXPath()).equals(id)) {
				return treeElement;
			}
			SushiTreeElement<T> child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
