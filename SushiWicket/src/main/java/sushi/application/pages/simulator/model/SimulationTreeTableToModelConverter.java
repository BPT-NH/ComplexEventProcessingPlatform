package sushi.application.pages.simulator.model;

import java.util.ArrayList;
import java.util.List;

import sushi.bpmn.decomposition.ANDComponent;
import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.LoopComponent;
import sushi.bpmn.decomposition.SequenceComponent;
import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNEndEvent;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTree;

/**
 * This class converts a tree from the simple simulation page to a BPMN model for the simulator.
 * @author micha
 */
public class SimulationTreeTableToModelConverter {
	
	SushiTree<Object> tree;
	BPMNProcess process = new BPMNProcess();
	SushiTree<AbstractBPMNElement> bpmnTree = new SushiTree<AbstractBPMNElement>();
	int IDCounter = 0;
	
	public BPMNProcess convertTreeToModel(SushiTree<Object> tree){
		this.tree = tree;
		BPMNStartEvent startEvent = new BPMNStartEvent(Integer.toString(IDCounter++), "Start", null);
		process.addBPMNElement(startEvent);
		BPMNEndEvent endEvent = new BPMNEndEvent(Integer.toString(IDCounter++), "End", null);
		process.addBPMNElement(endEvent);
		convertTreeToBPMNTree();
		//Annahme, dass Tree immer mit einer Component (Sequence, XOR, AND oder LOOP) beginnt
		List<AbstractBPMNElement> subStartAndEnd = createSubBranch(bpmnTree.getRootElements().get(0));
		AbstractBPMNElement.connectElements(startEvent, subStartAndEnd.get(0));
		AbstractBPMNElement.connectElements(subStartAndEnd.get(subStartAndEnd.size() - 1), endEvent);
		return process;
	}
	
	/**
	 * Tries to create the whole subbrach recursively starting with the given branchStartElement.
	 * @param branchStartElement
	 * @return a list with the start and end element of the created branch
	 */
	private List<AbstractBPMNElement> createSubBranch(AbstractBPMNElement branchStartElement) {
		List<AbstractBPMNElement> startAndEndElements = new ArrayList<AbstractBPMNElement>();
		if(branchStartElement instanceof Component){
			if(branchStartElement instanceof ANDComponent){
				startAndEndElements = createAndSubBranch((ANDComponent)branchStartElement);
			} else if(branchStartElement instanceof XORComponent){
				startAndEndElements = createXORSubBranch((XORComponent)branchStartElement);
			} else if(branchStartElement instanceof SequenceComponent){
				startAndEndElements = createSequenceSubBranch((SequenceComponent)branchStartElement);
			} else if(branchStartElement instanceof LoopComponent){
				startAndEndElements = createLoopSubBranch((LoopComponent)branchStartElement);
			}
		} else {
			BPMNTask task = (BPMNTask) branchStartElement;
			process.addBPMNElement(task);
			startAndEndElements.add(task);	//Start
			startAndEndElements.add(task);	//End
		}
		return startAndEndElements;
	}

	private List<AbstractBPMNElement> createLoopSubBranch(LoopComponent branchStartElement) {
		List<AbstractBPMNElement> startAndEnd = new ArrayList<AbstractBPMNElement>();
		List<AbstractBPMNElement> children = bpmnTree.getChildren(branchStartElement);
		String ID = Integer.toString(IDCounter++);
		BPMNXORGateway startGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(startGateway);
		startAndEnd.add(startGateway);
		ID = Integer.toString(IDCounter++);
		BPMNXORGateway endGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(endGateway);
		//Schleife einbauen
		AbstractBPMNElement.connectElements(endGateway, startGateway);
		startAndEnd.add(endGateway);
		AbstractBPMNElement predecessor = startGateway;
		for(int i = 0; i < children.size(); i++){
			List<AbstractBPMNElement> subStartAndEnd = createSubBranch(children.get(i));
			AbstractBPMNElement.connectElements(predecessor, subStartAndEnd.get(0));
			if(i == children.size() - 1){ //letztes Element
				AbstractBPMNElement.connectElements(subStartAndEnd.get(1), endGateway);
			} 
			predecessor = subStartAndEnd.get(1);
		}
		return startAndEnd;
	}

	private List<AbstractBPMNElement> createSequenceSubBranch(SequenceComponent branchStartElement) {
		List<AbstractBPMNElement> startAndEnd = new ArrayList<AbstractBPMNElement>();
		List<AbstractBPMNElement> children = bpmnTree.getChildren(branchStartElement);
		AbstractBPMNElement predecessor = null;
		for(int i = 0; i < children.size(); i++){
			List<AbstractBPMNElement> subStartAndEnd = createSubBranch(children.get(i));
			if(i == 0){ // erstes Element
				startAndEnd.add(subStartAndEnd.get(0));
			} else { //Elemente dazwischen
				AbstractBPMNElement.connectElements(predecessor, subStartAndEnd.get(0));
				if(i == children.size() - 1){ //letztes Element
					startAndEnd.add(subStartAndEnd.get(1));
				} 
			}
			predecessor = subStartAndEnd.get(1);
		}
		return startAndEnd;
	}

	private List<AbstractBPMNElement> createAndSubBranch(ANDComponent branchStartElement) {
		List<AbstractBPMNElement> startAndEnd = new ArrayList<AbstractBPMNElement>();
		String ID = Integer.toString(IDCounter++);
		BPMNAndGateway startGateway = new BPMNAndGateway(ID, "AND" + ID, null);
		process.addBPMNElement(startGateway);
		startAndEnd.add(startGateway);
		ID = Integer.toString(IDCounter++);
		BPMNAndGateway endGateway = new BPMNAndGateway(ID, "AND" + ID, null);
		process.addBPMNElement(endGateway);
		startAndEnd.add(endGateway);
		for(AbstractBPMNElement child : bpmnTree.getChildren(branchStartElement)){
			List<AbstractBPMNElement> subStartAndEnd = createSubBranch(child);
			AbstractBPMNElement.connectElements(startGateway, subStartAndEnd.get(0));
			AbstractBPMNElement.connectElements(subStartAndEnd.get(1), endGateway);
		}
		return startAndEnd;
	}
	
	private List<AbstractBPMNElement> createXORSubBranch(XORComponent branchStartElement) {
		List<AbstractBPMNElement> startAndEnd = new ArrayList<AbstractBPMNElement>();
		String ID = Integer.toString(IDCounter++);
		BPMNXORGateway startGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(startGateway);
		startAndEnd.add(startGateway);
		ID = Integer.toString(IDCounter++);
		BPMNXORGateway endGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(endGateway);
		startAndEnd.add(endGateway);
		for(AbstractBPMNElement child : bpmnTree.getChildren(branchStartElement)){
			List<AbstractBPMNElement> subStartAndEnd = createSubBranch(child);
			AbstractBPMNElement.connectElements(startGateway, subStartAndEnd.get(0));
			AbstractBPMNElement.connectElements(subStartAndEnd.get(subStartAndEnd.size() -1), endGateway);
		}
		return startAndEnd;
	}

	/**
	 * Converts the tree of eventTypes and IPattern objects to BPMNTasks and gateway components.
	 */
	private void convertTreeToBPMNTree() {
		for(Object treeElement : tree.getRootElements()){
			convertAndAddElementToBPMNTree(treeElement, null);
		}
	}

	private void convertAndAddElementToBPMNTree(Object treeElement, AbstractBPMNElement bpmnParent) {
		if(treeElement instanceof SushiEventType){
			String ID = Integer.toString(++IDCounter);
			MonitoringPoint monitoringPoint = new MonitoringPoint((SushiEventType) treeElement, MonitoringPointStateTransition.terminate, "");
			ArrayList<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
			monitoringPoints.add(monitoringPoint);
			BPMNTask task = new BPMNTask(ID, "Task " + ID, monitoringPoints);
			bpmnTree.addChild(bpmnParent, task);
		} else if(treeElement instanceof Component){
			if(tree.hasChildren(treeElement)){
				AbstractBPMNElement bpmnElement = null;
				if(treeElement instanceof ANDComponent){
					bpmnElement = (ANDComponent) treeElement;
				} else if(treeElement instanceof XORComponent){
					bpmnElement = (XORComponent) treeElement;
				} else if(treeElement instanceof LoopComponent){
					bpmnElement = (LoopComponent) treeElement;
				} else if(treeElement instanceof SequenceComponent){
					bpmnElement = (SequenceComponent) treeElement;
				}
				bpmnTree.addChild(bpmnParent, bpmnElement);
				for(Object child : tree.getChildren(treeElement)){
					convertAndAddElementToBPMNTree(child, bpmnElement);
				}
			}
		}
	}

}
