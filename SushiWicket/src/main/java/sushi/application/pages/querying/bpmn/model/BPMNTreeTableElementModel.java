package sushi.application.pages.querying.bpmn.model;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

public class BPMNTreeTableElementModel extends LoadableDetachableModel<BPMNTreeTableElement> {
	
	private static final long serialVersionUID = 1L;
	private final int ID;
	private List<BPMNTreeTableElement> treeNodes;
	
	public BPMNTreeTableElementModel(List<BPMNTreeTableElement> treeNodes, BPMNTreeTableElement node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getID();
	}

	public int getID() {
		return ID;
	}

	@Override
	protected BPMNTreeTableElement load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof BPMNTreeTableElementModel) {
			return ((BPMNTreeTableElementModel) object).getID() == ID;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	private BPMNTreeTableElement findTreeElement(Collection<BPMNTreeTableElement> treeNodes, int id) {
		for (BPMNTreeTableElement treeElement : treeNodes) {
			if (treeElement.getID() == id) {
				return treeElement;
			}
			BPMNTreeTableElement child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
