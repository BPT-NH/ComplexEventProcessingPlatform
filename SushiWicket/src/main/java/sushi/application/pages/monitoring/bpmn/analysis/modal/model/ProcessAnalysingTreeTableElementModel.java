package sushi.application.pages.monitoring.bpmn.analysis.modal.model;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

public class ProcessAnalysingTreeTableElementModel extends LoadableDetachableModel<ProcessAnalysingTreeTableElement> {
	
	private static final long serialVersionUID = 1L;
	private final int ID;
	private List<ProcessAnalysingTreeTableElement> treeNodes;
	
	public ProcessAnalysingTreeTableElementModel(List<ProcessAnalysingTreeTableElement> treeNodes, ProcessAnalysingTreeTableElement node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getID();
	}

	public int getID() {
		return ID;
	}

	@Override
	protected ProcessAnalysingTreeTableElement load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ProcessAnalysingTreeTableElementModel) {
			return ((ProcessAnalysingTreeTableElementModel) object).getID() == ID;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	private ProcessAnalysingTreeTableElement findTreeElement(Collection<ProcessAnalysingTreeTableElement> treeNodes, int id) {
		for (ProcessAnalysingTreeTableElement treeElement : treeNodes) {
			if (treeElement.getID() == id) {
				return treeElement;
			}
			ProcessAnalysingTreeTableElement child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
