package sushi.application.pages.querying.bpmn.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.RPSTBuilder;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.event.collection.SushiTree;

/**
 * Wraps the tree of BPMN components and their event types from the monitoring points for a tree table visualization.
 * @author micha
 *
 * @param 
 */
public class BPMNTreeTableProvider extends AbstractDataProvider implements ISortableTreeProvider<BPMNTreeTableElement, String> {

	private static final long serialVersionUID = 1L;
	private List<BPMNTreeTableElement> treeTableElements;
	private List<BPMNTreeTableElement> treeTableRootElements;
	private List<BPMNTreeTableElement> selectedTreeTableElements = new ArrayList<BPMNTreeTableElement>();
	private BPMNProcess process;
	private SushiTree<AbstractBPMNElement> bpmnComponentTree;
	
	public BPMNTreeTableProvider(BPMNProcess process) {
		this.treeTableElements = new ArrayList<BPMNTreeTableElement>();
		this.process = process;
		if(process != null){
			bpmnComponentTree = new RPSTBuilder(process).getProcessDecompositionTree();
			createTreeTableElements(bpmnComponentTree);
		}
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends BPMNTreeTableElement> getRoots() {
		return getRootElements().iterator();
	}
	
	private List<BPMNTreeTableElement> getRootElements(){
		treeTableRootElements  = new ArrayList<BPMNTreeTableElement>();
		for(BPMNTreeTableElement element : treeTableElements){
			if(element.getParent() == null){
				treeTableRootElements.add(element);
			}
		}
		return treeTableRootElements;
	}

	@Override
	public boolean hasChildren(BPMNTreeTableElement node) {
		return !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends BPMNTreeTableElement> getChildren(BPMNTreeTableElement node) {
		return node.getChildren().iterator();
	}

	@Override
	public BPMNTreeTableElementModel model(BPMNTreeTableElement node) {
		return new BPMNTreeTableElementModel(getRootElements(), node);
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public void selectEntry(int entryId) {
		for (BPMNTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.add(treeTableElement);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (BPMNTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.remove(treeTableElement);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (BPMNTreeTableElement treeTableElement : selectedTreeTableElements) {
			if(treeTableElement.getID() == entryId) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns the next free ID for an new element.
	 * @return
	 */
	public int getNextID(){
		int highestNumber = 0;
		for(BPMNTreeTableElement element : treeTableElements ){
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}

	public List<BPMNTreeTableElement> getTreeTableElements() {
		return treeTableElements;
	}
	
	public void addTreeTableElement(BPMNTreeTableElement treeTableElement) {
		this.treeTableElements.add(treeTableElement);
		if(!selectedTreeTableElements.isEmpty()){
			BPMNTreeTableElement parent = selectedTreeTableElements.get(0);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
		} 
	}
	
	public void addTreeTableElementWithParent(BPMNTreeTableElement treeTableElement, BPMNTreeTableElement parent) {
		this.treeTableElements.add(treeTableElement);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
	}

	public void setTreeTableElements(List<BPMNTreeTableElement> treeTableElements) {
		this.treeTableElements = treeTableElements;
	}

	public void deleteSelectedEntries() {
		for(BPMNTreeTableElement element : selectedTreeTableElements){
			element.remove();
		}
		treeTableElements.removeAll(selectedTreeTableElements);
		selectedTreeTableElements.clear();
	}

	public List<BPMNTreeTableElement> getSelectedTreeTableElements() {
		return selectedTreeTableElements;
	}
	
	public List<BPMNTreeTableElement> getRootTreeTableElements() {
		return getRootElements();
	}
	

	private void createTreeTableElements(SushiTree<AbstractBPMNElement> bpmnComponentTree) {
		for(AbstractBPMNElement rootElement : bpmnComponentTree.getRootElements()){
			addElementToTree(null, rootElement, bpmnComponentTree);
		}
	}
	
	private void addElementToTree(BPMNTreeTableElement parent, AbstractBPMNElement bpmnElement, SushiTree<AbstractBPMNElement> bpmnComponentTree){
		BPMNTreeTableElement treeTableElement = createTreeTableElement(bpmnElement);
		treeTableElement.setParent(parent);
		treeTableElements.add(treeTableElement);
		if(parent == null){
			treeTableRootElements.add(treeTableElement);
		}
		if(bpmnComponentTree.hasChildren(bpmnElement)){
			for(AbstractBPMNElement child : bpmnComponentTree.getChildren(bpmnElement)){
				addElementToTree(treeTableElement, child, bpmnComponentTree);
			}
		}
	}

	private BPMNTreeTableElement createTreeTableElement(AbstractBPMNElement bpmnElement) {
		BPMNTreeTableElement element = new BPMNTreeTableElement(getNextID(), bpmnElement);
		if(bpmnElement.hasMonitoringPoints() && !(bpmnElement instanceof Component)){
			//TODO: Was ist bei mehreren Monitoring-Points?
			element.addMonitoringPoints(bpmnElement.getMonitoringPoints());
		}
		return element;
	}
	


	public void deleteAllEntries() {
		for(BPMNTreeTableElement element : treeTableElements){
			element.remove();
		}
		selectedTreeTableElements.clear();
		treeTableElements.clear();
	}

	public BPMNProcess getProcess() {
		return process;
	}

	public void setProcess(BPMNProcess process) {
		this.process = process;
		if(process != null){
			bpmnComponentTree = new RPSTBuilder(process).getProcessDecompositionTree();
			createTreeTableElements(bpmnComponentTree);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(BPMNTreeTableElement treeTableElement : treeTableElements){
			if(treeTableElement.getID() == entryId){
				return treeTableElement;
			}
		}
		return null;
	}
	
}
