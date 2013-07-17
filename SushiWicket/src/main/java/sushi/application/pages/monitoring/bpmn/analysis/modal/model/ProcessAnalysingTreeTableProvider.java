package sushi.application.pages.monitoring.bpmn.analysis.modal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.collection.SushiTree;
import sushi.monitoring.bpmn.ProcessMonitor;
import sushi.query.SushiPatternQuery;

/**
 * Wraps the tree of BPMN components and their event types from the monitoring points for a tree table visualization.
 * @author micha
 *
 * @param 
 */
public class ProcessAnalysingTreeTableProvider extends AbstractDataProvider implements ISortableTreeProvider<ProcessAnalysingTreeTableElement, String> {

	private static final long serialVersionUID = 1L;
	private List<ProcessAnalysingTreeTableElement> treeTableElements = new ArrayList<ProcessAnalysingTreeTableElement>();
	private List<ProcessAnalysingTreeTableElement> treeTableRootElements = new ArrayList<ProcessAnalysingTreeTableElement>();
	private List<ProcessAnalysingTreeTableElement> selectedTreeTableElements = new ArrayList<ProcessAnalysingTreeTableElement>();
	private ProcessMonitor processMonitor;
	private SushiTree<SushiPatternQuery> queryTree;
	private SushiTree<AbstractBPMNElement> bpmnProcessDecompositionTree;
	
	public ProcessAnalysingTreeTableProvider(ProcessMonitor processMonitor) {
		this.processMonitor = processMonitor;
		refreshTreeTable();
	}

	private void refreshTreeTable() {
		if(processMonitor != null){
			createQueryTree();
			createTreeTableElements(queryTree);
		}
	}

	private void createQueryTree() {
		bpmnProcessDecompositionTree = processMonitor.getProcess().getProcessDecompositionTree();
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
		for(SushiPatternQuery query : processMonitor.getQueries()){
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
	public Iterator<? extends ProcessAnalysingTreeTableElement> getRoots() {
		return getRootElements().iterator();
	}
	
	private List<ProcessAnalysingTreeTableElement> getRootElements(){
		treeTableRootElements  = new ArrayList<ProcessAnalysingTreeTableElement>();
		for(ProcessAnalysingTreeTableElement element : treeTableElements){
			if(element.getParent() == null){
				treeTableRootElements.add(element);
			}
		}
		return treeTableRootElements;
	}

	@Override
	public boolean hasChildren(ProcessAnalysingTreeTableElement node) {
		return !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends ProcessAnalysingTreeTableElement> getChildren(ProcessAnalysingTreeTableElement node) {
		return node.getChildren().iterator();
	}

	@Override
	public ProcessAnalysingTreeTableElementModel model(ProcessAnalysingTreeTableElement node) {
		return new ProcessAnalysingTreeTableElementModel(getRootElements(), node);
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public void selectEntry(int entryId) {
		for (ProcessAnalysingTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.add(treeTableElement);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (ProcessAnalysingTreeTableElement treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.remove(treeTableElement);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (ProcessAnalysingTreeTableElement treeTableElement : selectedTreeTableElements) {
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
		for(ProcessAnalysingTreeTableElement element : treeTableElements ){
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}

	public List<ProcessAnalysingTreeTableElement> getTreeTableElements() {
		return treeTableElements;
	}
	
	public void addTreeTableElement(ProcessAnalysingTreeTableElement treeTableElement) {
		this.treeTableElements.add(treeTableElement);
		if(!selectedTreeTableElements.isEmpty()){
			ProcessAnalysingTreeTableElement parent = selectedTreeTableElements.get(0);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
		} 
	}
	
	public void addTreeTableElementWithParent(ProcessAnalysingTreeTableElement treeTableElement, ProcessAnalysingTreeTableElement parent) {
		this.treeTableElements.add(treeTableElement);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
	}

	public void setTreeTableElements(List<ProcessAnalysingTreeTableElement> treeTableElements) {
		this.treeTableElements = treeTableElements;
	}

	public void deleteSelectedEntries() {
		for(ProcessAnalysingTreeTableElement element : selectedTreeTableElements){
			element.remove();
		}
		treeTableElements.removeAll(selectedTreeTableElements);
		selectedTreeTableElements.clear();
	}

	public List<ProcessAnalysingTreeTableElement> getSelectedTreeTableElements() {
		return selectedTreeTableElements;
	}
	
	public List<ProcessAnalysingTreeTableElement> getRootTreeTableElements() {
		return getRootElements();
	}
	

	private void createTreeTableElements(SushiTree<SushiPatternQuery> queryTree) {
		for(SushiPatternQuery rootElement : queryTree.getRootElements()){
			addElementToTree(null, rootElement, queryTree);
		}
	}
	
	private void addElementToTree(ProcessAnalysingTreeTableElement parent, SushiPatternQuery query, SushiTree<SushiPatternQuery> queryTree){
		ProcessAnalysingTreeTableElement treeTableElement = createTreeTableElement(query);
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

	private ProcessAnalysingTreeTableElement createTreeTableElement(SushiPatternQuery query) {
		ProcessAnalysingTreeTableElement element = new ProcessAnalysingTreeTableElement(getNextID(), query, processMonitor);
		return element;
	}
	


	public void deleteAllEntries() {
		for(ProcessAnalysingTreeTableElement element : treeTableElements){
			element.remove();
		}
		selectedTreeTableElements.clear();
		treeTableElements.clear();
	}

	public ProcessMonitor getProcessMonitor() {
		return processMonitor;
	}

	@Override
	public Object getEntry(int entryId) {
		for(ProcessAnalysingTreeTableElement treeTableElement : treeTableElements){
			if(treeTableElement.getID() == entryId){
				return treeTableElement;
			}
		}
		return null;
	}

	public void setProcessMonitor(ProcessMonitor processMonitor) {
		this.processMonitor = processMonitor;
		//Alte Werte entfernen
		this.treeTableElements = new ArrayList<ProcessAnalysingTreeTableElement>();
		this.treeTableRootElements = new ArrayList<ProcessAnalysingTreeTableElement>();
		this.selectedTreeTableElements = new ArrayList<ProcessAnalysingTreeTableElement>();
		refreshTreeTable();
		
	}
	
}
