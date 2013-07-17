package sushi.application.pages.simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.antlr.grammar.v3.ANTLRParser.finallyClause_return;

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
import sushi.util.Tuple;

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
		BPMNStartEvent startEvent = new BPMNStartEvent(createID(), "Start", null);
		process.addBPMNElement(startEvent);
		BPMNEndEvent endEvent = new BPMNEndEvent(createID(), "End", null);
		process.addBPMNElement(endEvent);
		convertTreeToBPMNTree();
		//Annahme, dass Tree immer mit einer Component (Sequence, XOR, AND oder LOOP) beginnt
		Tuple<AbstractBPMNElement, AbstractBPMNElement> subStartAndEnd = createSubBranch(bpmnTree.getRootElements().get(0));
		AbstractBPMNElement.connectElements(startEvent, subStartAndEnd.x);
		AbstractBPMNElement.connectElements(subStartAndEnd.y, endEvent);
		return process;
	}
	
	/**
	 * Tries to create the whole subbrach recursively starting with the given branchStartElement.
	 * @param branchStartElement
	 * @return a list with the start and end element of the created branch
	 */
	private Tuple<AbstractBPMNElement, AbstractBPMNElement> createSubBranch(AbstractBPMNElement branchStartElement) {
		if(branchStartElement instanceof Component){
			if(branchStartElement instanceof SequenceComponent){
				return createSequenceSubBranch((SequenceComponent)branchStartElement);
			} else if(branchStartElement instanceof ANDComponent){
				return createAndSubBranch((ANDComponent)branchStartElement);
			} else if(branchStartElement instanceof XORComponent){
				return createXORSubBranch((XORComponent)branchStartElement);
			} else if(branchStartElement instanceof LoopComponent){
				return createLoopSubBranch((LoopComponent)branchStartElement);
			}
		}
		BPMNTask task = (BPMNTask) branchStartElement;
		process.addBPMNElement(task);
		return(new Tuple<AbstractBPMNElement, AbstractBPMNElement>(task, task));
	}

	private Tuple<AbstractBPMNElement, AbstractBPMNElement> createSequenceSubBranch(SequenceComponent branchStartElement) {
		List<AbstractBPMNElement> children = bpmnTree.getChildren(branchStartElement);
		AbstractBPMNElement firstElement = null;
		AbstractBPMNElement predecessor = null;
		for(int i = 0; i < children.size(); i++){
			Tuple<AbstractBPMNElement, AbstractBPMNElement> subStartAndEnd = createSubBranch(children.get(i));
			if(i == 0){ // erstes Element
				firstElement = (subStartAndEnd.x);
			} else { //Elemente dazwischen
				AbstractBPMNElement.connectElements(predecessor, subStartAndEnd.x);
			}
			predecessor = subStartAndEnd.y;
		}
		//letztes Element der letzten Subkomponente ist gleichzeitig letztes Element der Sequenz
		return new Tuple<AbstractBPMNElement, AbstractBPMNElement>(firstElement, predecessor);
	}

	private Tuple<AbstractBPMNElement, AbstractBPMNElement> createAndSubBranch(ANDComponent branchStartElement) {
		String ID = createID();
		BPMNAndGateway startGateway = new BPMNAndGateway(ID , "AND" + ID, null);
		process.addBPMNElement(startGateway);
		ID = createID();
		BPMNAndGateway endGateway = new BPMNAndGateway(ID , "AND" + ID, null);
		process.addBPMNElement(endGateway);
		//alle Unterelemente erstellen und mit Gateways verbinden
		for(AbstractBPMNElement child : bpmnTree.getChildren(branchStartElement)){
			Tuple<AbstractBPMNElement, AbstractBPMNElement> subStartAndEnd = createSubBranch(child);
			AbstractBPMNElement.connectElements(startGateway, subStartAndEnd.x);
			AbstractBPMNElement.connectElements(subStartAndEnd.y, endGateway);
		}
		return new Tuple<AbstractBPMNElement, AbstractBPMNElement>(startGateway, endGateway);
	}
	
	private Tuple<AbstractBPMNElement, AbstractBPMNElement> createXORSubBranch(XORComponent branchStartElement) {
		String ID = createID();
		BPMNXORGateway startGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(startGateway);
		ID = createID();
		BPMNXORGateway endGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(endGateway);
		for(AbstractBPMNElement child : bpmnTree.getChildren(branchStartElement)){
			Tuple<AbstractBPMNElement, AbstractBPMNElement> subStartAndEnd = createSubBranch(child);
			AbstractBPMNElement.connectElements(startGateway, subStartAndEnd.x);
			AbstractBPMNElement.connectElements(subStartAndEnd.y, endGateway);
		}
		return new Tuple<AbstractBPMNElement, AbstractBPMNElement>(startGateway, endGateway);
	}
	
	private Tuple<AbstractBPMNElement, AbstractBPMNElement> createLoopSubBranch(LoopComponent branchStartElement) {
		List<AbstractBPMNElement> children = bpmnTree.getChildren(branchStartElement);
		String ID = createID();
		BPMNXORGateway startGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(startGateway);
		ID = createID();
		BPMNXORGateway endGateway = new BPMNXORGateway(ID, "XOR" + ID, null);
		process.addBPMNElement(endGateway);
		//Schleife einbauen
		AbstractBPMNElement.connectElements(endGateway, startGateway);
		AbstractBPMNElement predecessor = startGateway;
		for(int i = 0; i < children.size(); i++){
			Tuple<AbstractBPMNElement, AbstractBPMNElement> subStartAndEnd = createSubBranch(children.get(i));
			AbstractBPMNElement.connectElements(predecessor, subStartAndEnd.x);
			if(i == children.size() - 1){ //letztes Element
				AbstractBPMNElement.connectElements(subStartAndEnd.y, endGateway);
			} 
			predecessor = subStartAndEnd.y;
		}
		return new Tuple<AbstractBPMNElement, AbstractBPMNElement>(startGateway, endGateway);
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
	
	private String createID(){
		IDCounter++;
		return Integer.toString(IDCounter);
	}

}
