package sushi.application.components.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import sushi.event.attribute.SushiAttribute;

public class SushiAttributeTreeModel extends LoadableDetachableModel<SushiAttribute> {
	
	private static final long serialVersionUID = 1L;
	private final String ID;
	private List<SushiAttribute> treeNodes;
	
	public SushiAttributeTreeModel(List<SushiAttribute> treeNodes, SushiAttribute node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getIdentifier();
	}

	public String getID() {
		return ID;
	}

	@Override
	protected SushiAttribute load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof SushiAttributeTreeModel) {
			return ((SushiAttributeTreeModel) object).getID().equals(ID);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	private SushiAttribute findTreeElement(List<SushiAttribute> treeNodes, String id) {
		for (SushiAttribute treeElement : treeNodes) {
			if (treeElement.getIdentifier().equals(id)) {
				return treeElement;
			}
			SushiAttribute child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
