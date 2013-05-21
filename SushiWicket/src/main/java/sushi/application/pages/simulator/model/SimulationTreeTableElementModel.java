package sushi.application.pages.simulator.model;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

public class SimulationTreeTableElementModel<T> extends LoadableDetachableModel<SimulationTreeTableElement<T>> {
	
	private static final long serialVersionUID = 1L;
	private final int ID;
	private List<SimulationTreeTableElement<T>> treeNodes;
	
	public SimulationTreeTableElementModel(List<SimulationTreeTableElement<T>> treeNodes, SimulationTreeTableElement<T> node) {
		super(node);
		this.treeNodes = treeNodes;
		this.ID = node.getID();
	}

	public int getID() {
		return ID;
	}

	@Override
	protected SimulationTreeTableElement<T> load() {
		return findTreeElement(treeNodes, ID);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof SimulationTreeTableElementModel) {
			return ((SimulationTreeTableElementModel<T>) object).getID() == ID;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	private SimulationTreeTableElement<T> findTreeElement(List<SimulationTreeTableElement<T>> treeNodes, int id) {
		for (SimulationTreeTableElement<T> treeElement : treeNodes) {
			if (treeElement.getID() == id) {
				return treeElement;
			}
			SimulationTreeTableElement<T> child = findTreeElement(treeElement.getChildren(), id);
			if (child != null) {
				return child;
			}
		}
		return null;
	}
}
