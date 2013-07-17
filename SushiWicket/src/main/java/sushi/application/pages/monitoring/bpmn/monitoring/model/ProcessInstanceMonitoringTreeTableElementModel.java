package sushi.application.pages.monitoring.bpmn.monitoring.model;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * This model provides a tree of {@link ProcessInstanceMonitoringTreeTableElement}s.
 * @author micha
 */
public class ProcessInstanceMonitoringTreeTableElementModel extends LoadableDetachableModel<ProcessInstanceMonitoringTreeTableElement> {
	
	private static final long serialVersionUID = 1L;
	private final int ID;
	private List<ProcessInstanceMonitoringTreeTableElement> treeNodes;
	
	public ProcessInstanceMonitoringTreeTableElementModel(List<ProcessInstanceMonitoringTreeTableElement> treeNodes, ProcessInstanceMonitoringTreeTableElement node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getID();
	}

	public int getID() {
		return ID;
	}

	@Override
	protected ProcessInstanceMonitoringTreeTableElement load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ProcessInstanceMonitoringTreeTableElementModel) {
			return ((ProcessInstanceMonitoringTreeTableElementModel) object).getID() == ID;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	private ProcessInstanceMonitoringTreeTableElement findTreeElement(Collection<ProcessInstanceMonitoringTreeTableElement> treeNodes, int id) {
		for (ProcessInstanceMonitoringTreeTableElement treeElement : treeNodes) {
			if (treeElement.getID() == id) {
				return treeElement;
			}
			ProcessInstanceMonitoringTreeTableElement child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
