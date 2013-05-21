package sushi.application.pages.monitoring.bpmn.monitoring.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.event.collection.SushiTree;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;
import sushi.query.SushiPatternQuery;

/**
 * Wraps the tree of BPMN components and their event types from the monitoring points for a tree table visualization.
 * @author micha
 *
 * @param 
 */
public class ProcessInstanceMonitoringTreeTableProvider extends AbstractDataProvider implements ISortableTreeProvider<ProcessInstanceMonitoringTreeTableElement, String> {

	private static final long serialVersionUID = 1L;
	private List<ProcessInstanceMonitoringTreeTableElement> treeTableElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
	private List<ProcessInstanceMonitoringTreeTableElement> treeTableRootElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
	private List<ProcessInstanceMonitoringTreeTableElement> selectedTreeTableElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
	private ProcessInstanceMonitor processInstanceMonitor;
	private SushiTree<SushiPatternQuery> queryTree;
	private SushiTree<AbstractBPMNElement> bpmnProcessDecompositionTree;
	
	public ProcessInstanceMonitoringTreeTableProvider(ProcessInstanceMonitor processInstanceMonitor) {
		this.processInstanceMonitor = processInstanceMonitor;
		refreshTreeTable();
	}

	private void refreshTreeTable() {
		if(processInstanceMonitor != null){
			createQueryTree();
			createTreeTableElements(queryTree);
		}
	}

	private void createQueryTree() {
		BPMNProcess BPMNProcess = processInstanceMonitor.getProcessInstance().getProcess().getBpmnProcess();
		SushiRPSTTree rpst = new SushiRPSTTree(BPMNProcess);
		bpmnProcessDecompositionTree = rpst.getProcessDecompositionTree();
		queryTree = new SushiTree<SushiPatternQuery>();
		//Query enthält ihre BPMN-Elemente --> And-Component, enthält Childs
		for(AbstractBPMNElement rootElement : bpmnProcessDecompositionTree.getRootElements()){
			addQueryToTree(null, bpmnProcessDecompositionTree.getChildren(rootElement));
		}
	}
	
	private void addQueryToTree(SushiPatternQuery parentQuery, List<AbstractBPMNElement> bpmnElements){
		SushiPatternQuery query = findQueryWithElements(new HashSet<AbstractBPMNElement>(bpmnElements));
		if(query != null){
			queryTree.addChild(parentQuery, query);
		}
		//Childs
		for(AbstractBPMNElement element : bpmnElements){
			if(bpmnProcessDecompositionTree.hasChildren(element)){
				addQueryToTree(query, bpmnProcessDecompositionTree.getChildren(element));
			} else if(element.hasMonitoringPoints()){ /* Element (Task oder Event) mit MonitoringPoints */
				Set<AbstractBPMNElement> elements = new HashSet<AbstractBPMNElement>();
				elements.add(element);
				SushiPatternQuery elementQuery = findQueryWithElements(elements);
				if(elementQuery != null){
					queryTree.addChild(query, elementQuery);
				}
			}
		}
	}
	
	private SushiPatternQuery findQueryWithElements(Set<AbstractBPMNElement> bpmnElements){
		for(SushiPatternQuery query : processInstanceMonitor.getQueries()){
			if(query.getMonitoredElements().containsAll(bpmnElements) && bpmnElements.containsAll(query.getMonitoredElements())){
				return query;
			}
		}
		return null;
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends ProcessInstanceMonitoringTreeTableElement> getRoots() {
		return getRootElements().iterator();
	}
	
	private List<ProcessInstanceMonitoringTreeTableElement> getRootElements(){
		treeTableRootElements  = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
		for(ProcessInstanceMonitoringTreeTableElement element : treeTableElements){
			if(element.getParent() == null){
				treeTableRootElements.add(element);
			}
		}
		return treeTableRootElements;
	}

	@Override
	public boolean hasChildren(ProcessInstanceMonitoringTreeTableElement node) {
		return !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends ProcessInstanceMonitoringTreeTableElement> getChildren(ProcessInstanceMonitoringTreeTableElement node) {
		return node.getChildren().iterator();
	}

	@Override
	public ProcessInstanceMonitoringTreeTableElementModel model(ProcessInstanceMonitoringTreeTableElement node) {
		return new ProcessInstanceMonitoringTreeTableElementModel(getRootElements(), node);
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public void selectEntry(int entryId) {
		for (ProcessInstanceMonitoringTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.add(treeTableElement);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (ProcessInstanceMonitoringTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.remove(treeTableElement);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (ProcessInstanceMonitoringTreeTableElement treeTableElement : selectedTreeTableElements) {
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
		for(ProcessInstanceMonitoringTreeTableElement element : treeTableElements ){
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}

	public List<ProcessInstanceMonitoringTreeTableElement> getTreeTableElements() {
		return treeTableElements;
	}
	
	public void addTreeTableElement(ProcessInstanceMonitoringTreeTableElement treeTableElement) {
		this.treeTableElements.add(treeTableElement);
		if(!selectedTreeTableElements.isEmpty()){
			ProcessInstanceMonitoringTreeTableElement parent = selectedTreeTableElements.get(0);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
		} 
	}
	
	public void addTreeTableElementWithParent(ProcessInstanceMonitoringTreeTableElement treeTableElement, ProcessInstanceMonitoringTreeTableElement parent) {
		this.treeTableElements.add(treeTableElement);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
	}

	public void setTreeTableElements(List<ProcessInstanceMonitoringTreeTableElement> treeTableElements) {
		this.treeTableElements = treeTableElements;
	}

	public void deleteSelectedEntries() {
		for(ProcessInstanceMonitoringTreeTableElement element : selectedTreeTableElements){
			element.remove();
		}
		treeTableElements.removeAll(selectedTreeTableElements);
		selectedTreeTableElements.clear();
	}

	public List<ProcessInstanceMonitoringTreeTableElement> getSelectedTreeTableElements() {
		return selectedTreeTableElements;
	}
	
	public List<ProcessInstanceMonitoringTreeTableElement> getRootTreeTableElements() {
		return getRootElements();
	}
	

	private void createTreeTableElements(SushiTree<SushiPatternQuery> queryTree) {
		for(SushiPatternQuery rootElement : queryTree.getRootElements()){
			addElementToTree(null, rootElement, queryTree);
		}
	}
	
	private void addElementToTree(ProcessInstanceMonitoringTreeTableElement parent, SushiPatternQuery query, SushiTree<SushiPatternQuery> queryTree){
		ProcessInstanceMonitoringTreeTableElement treeTableElement = createTreeTableElement(query);
		treeTableElement.setParent(parent);
		treeTableElements.add(treeTableElement);
		if(parent == null){
			treeTableRootElements.add(treeTableElement);
		}
		if(queryTree.hasChildren(query)){
			for(SushiPatternQuery child : queryTree.getChildren(query)){
				addElementToTree(treeTableElement, child, queryTree);
			}
		}
	}

	private ProcessInstanceMonitoringTreeTableElement createTreeTableElement(SushiPatternQuery query) {
		ProcessInstanceMonitoringTreeTableElement element = new ProcessInstanceMonitoringTreeTableElement(getNextID(), query, processInstanceMonitor);
		return element;
	}
	


	public void deleteAllEntries() {
		for(ProcessInstanceMonitoringTreeTableElement element : treeTableElements){
			element.remove();
		}
		selectedTreeTableElements.clear();
		treeTableElements.clear();
	}

	public ProcessInstanceMonitor getProcessInstanceMonitor() {
		return processInstanceMonitor;
	}

	public void setProcessInstanceMonitor(ProcessInstanceMonitor processInstanceMonitor) {
		this.processInstanceMonitor = processInstanceMonitor;
		//Alte Werte entfernen
		this.treeTableElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
		this.treeTableRootElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
		this.selectedTreeTableElements = new ArrayList<ProcessInstanceMonitoringTreeTableElement>();
		refreshTreeTable();
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(ProcessInstanceMonitoringTreeTableElement treeTableElement : treeTableElements){
			if(treeTableElement.getID() == entryId){
				return treeTableElement;
			}
		}
		return null;
	}
	
}
