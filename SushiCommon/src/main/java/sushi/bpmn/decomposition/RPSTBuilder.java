package sushi.bpmn.decomposition;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jbpt.algo.tree.rpst.IRPSTNode;
import org.jbpt.algo.tree.rpst.RPST;
import org.jbpt.algo.tree.tctree.TCType;

import sushi.bpmn.DirectedBPMNEdge;
import sushi.bpmn.MultiDirectedBPMNGraph;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNSubProcess;
import sushi.event.collection.SushiTree;

/**
 * This class constructs a RPST and a {@link SushiTree} with the {@link AbstractBPMNElement}s of the process derived from the RPST.
 * @author micha
 *
 */
public class RPSTBuilder {

	private BPMNProcess process;
	
	/**
	 * Graph derived from the BPMNProcess.
	 */
	private MultiDirectedBPMNGraph graph;
	
	/**
	 * The RPST tree.
	 */
	private RPST<DirectedBPMNEdge, AbstractBPMNElement> rpst;
	
	/**
	 * Tree of the RPST nodes. RPST nodes are edges of the original process.
	 */
	private SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> rpstNodesTree;
	
	/**
	 * Tree, which contains the BPMNProcess elements in the decomposition hierarchy of the RPST.
	 */
	private SushiTree<AbstractBPMNElement> processDecompositionTree;

	/**
	 * @param process
	 */
 	public RPSTBuilder(BPMNProcess process) {
 		this.process = (BPMNProcess) process.clone();
		this.process = BPMNProcessPreprocessor.structureProcess(this.process);
		this.graph = convertBPMNToGraph(this.process);
		this.rpst = new RPST<DirectedBPMNEdge,AbstractBPMNElement>(graph);
		this.buildRPSTNodesTree();
		this.buildProcessDecompositionTree();
		PatternUtil.determinePatternForTreeComponents(processDecompositionTree);
	}
	
	/**
	 * Builds a {@link SushiTree} from the components of the RPST for better handling.
	 */
	private void buildRPSTNodesTree() {
		IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rootNode = rpst.getRoot();
		
		rpstNodesTree = new SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>>();
		
		addElementToRPSTNodesTree(rootNode, null);
	}
	
	/**
	 * Determines if a node in the RPST has children.
	 * @param node
	 * @param rpst
	 * @return
	 */
	private boolean hasChildren(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> node, RPST<DirectedBPMNEdge,AbstractBPMNElement> rpst){
		return !rpst.getChildren(node).isEmpty();
	}
	
	private void addElementToRPSTNodesTree(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> element, IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> parent){
		rpstNodesTree.addChild(parent, element);
		if(hasChildren(element, rpst)){
			for(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> child : rpst.getChildren(element)){
				addElementToRPSTNodesTree(child, element);
			}
		}
	}
		
	/**
	 * This method converts an node-oriented {@link BPMNProcess} to an edge-oriented {@link MultiDirectedBPMNGraph}.
	 * @param process
	 * @return
	 */
	private MultiDirectedBPMNGraph convertBPMNToGraph(BPMNProcess process){
		MultiDirectedBPMNGraph g = new MultiDirectedBPMNGraph();
		for(AbstractBPMNElement element : process.getBPMNElementsWithOutSequenceFlows()){
			for(AbstractBPMNElement successor : element.getSuccessors()){
				g.addEdge(element,successor);
			}
		}
		return g;
	}
	
	/**
	 * Builds the processDecompositionTree from the RPST.
	 */
	private void buildProcessDecompositionTree() {
		processDecompositionTree = new SushiTree<AbstractBPMNElement>();
		addElementsToProcessDecompositionTree(rpstNodesTree, rpstNodesTree.getRootElements(), null, null);
		//Children für Components im ProcessDecompositionTree setzen
		for(AbstractBPMNElement rootElement : processDecompositionTree.getRootElements()){
			setChildrenForProcessDecompositionTreeElements(rootElement);
		}
		
		determineEntryAndExitPoints();
	}
	
	private void setChildrenForProcessDecompositionTreeElements(AbstractBPMNElement element) {
		if(element instanceof Component && processDecompositionTree.hasChildren(element)){
			Component component = (Component) element;
			component.addChildren(processDecompositionTree.getChildren(component));
			for(AbstractBPMNElement child : processDecompositionTree.getChildren(component)){
				setChildrenForProcessDecompositionTreeElements(child);
			}
		}
	}

	/**
	 * Sets the entry and exit point for every element of the tree, that is a component.
	 */
	private void determineEntryAndExitPoints() {
		for(AbstractBPMNElement element : processDecompositionTree.getElements()){
			if(element instanceof Component){
				Component component = (Component) element;
				
				component.setEntryPoint(determineEntryPoint(component));
				component.setExitPoint(determineExitPoint(component));
			}
		}
	}
	
	private AbstractBPMNElement determineEntryPoint(Component component){
		AbstractBPMNElement sourceElement = component.getSourceElement();
		//TODO: Testen: Kann noch falsch sein bei Schleifen
		AbstractBPMNElement entryPoint = null;
		if(!sourceElement.getPredecessors().isEmpty()){
			//Direkte Vorgänger des SourceElement
			Set<AbstractBPMNElement> predecessors = new HashSet<AbstractBPMNElement>(sourceElement.getPredecessors());
			
			//Alle Childs des aktuellen Parent
			Set<AbstractBPMNElement> parentIndirectChildElements = processDecompositionTree.getIndirectChildren(processDecompositionTree.getParent(component));
			
			//Alle Childs der aktuellen Component
			Set<AbstractBPMNElement> componentChildren = processDecompositionTree.getIndirectChildren(component);
			
			//Sinnvolle Entrypoints sind Vorgänger der Component, die nicht Kinder der Component sind
			parentIndirectChildElements.removeAll(componentChildren);
			predecessors.retainAll(parentIndirectChildElements);
			
			if(!predecessors.isEmpty()){
				entryPoint = predecessors.iterator().next();
			}
			
		}
		return entryPoint;
	}
	
	private AbstractBPMNElement determineExitPoint(Component component){
		//TODO: Testen: Kann noch falsch sein bei Schleifen
		AbstractBPMNElement sinkElement = component.getSinkElement();
		AbstractBPMNElement exitPoint = null;
		if(!sinkElement.getSuccessors().isEmpty()){
			//Direkte Nachfolger des SinkElements
			Set<AbstractBPMNElement> successors = new HashSet<AbstractBPMNElement>(sinkElement.getSuccessors());
			
			//Alle Childs des aktuellen Parent
			Set<AbstractBPMNElement> parentIndirectChildElements = processDecompositionTree.getIndirectChildren(processDecompositionTree.getParent(component));
			
			//Alle Childs der aktuellen Component
			Set<AbstractBPMNElement> componentChildren = processDecompositionTree.getIndirectChildren(component);
			
			//Sinnvolle Exitpoints sind Nachfolger der Component, die nicht Kinder der Component sind
			parentIndirectChildElements.removeAll(componentChildren);
			successors.retainAll(parentIndirectChildElements);
			
			if(!successors.isEmpty()){
				exitPoint = successors.iterator().next();
			}
		}
		return exitPoint;
	}

	/**
	 * Adds the elements of the RPSTNodesTree to the ProcessDecompositionTree with the specified parent elements.
	 * @param rpstNodesTree
	 * @param rpstNodesTreeElements
	 * @param rpstNodesTreeParentElement
	 * @param processDecompositionTreeParent
	 */
	private void addElementsToProcessDecompositionTree(
			SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> rpstNodesTree,
			Collection<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> rpstNodesTreeElements, 
			IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeParentElement, 
			AbstractBPMNElement processDecompositionTreeParent){
		Set<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> trivialElements = new HashSet<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>>();
		
		for(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeElement: rpstNodesTreeElements){
			if(rpstNodesTreeElement.getType() == TCType.TRIVIAL){
				trivialElements.add(rpstNodesTreeElement);
				continue;
			}
			
			AbstractBPMNElement createdElement = null;
			
			AbstractBPMNElement sourceElement = rpstNodesTreeElement.getEntry();
			
			AbstractBPMNElement sinkElement = rpstNodesTreeElement.getExit();
			
			if(rpstNodesTreeElement.getType() == TCType.POLYGON){
				createdElement = new PolygonComponent(null, sourceElement, null, sinkElement);
				createdElement.setName(rpstNodesTreeElement.getName());
			} else if(rpstNodesTreeElement.getType() == TCType.BOND){
				createdElement = new BondComponent(null, sourceElement, null, sinkElement);
				createdElement.setName(rpstNodesTreeElement.getName());
			} else if(rpstNodesTreeElement.getType() == TCType.RIGID){
				throw new RuntimeException("RIGIDs sind noch nicht durchdacht ;)");
			} 
			
			processDecompositionTree.addChild(processDecompositionTreeParent, createdElement);
			
			if(rpstNodesTree.hasChildren(rpstNodesTreeElement)){
				addElementsToProcessDecompositionTree(rpstNodesTree, rpstNodesTree.getChildren(rpstNodesTreeElement), rpstNodesTreeElement, createdElement);
			}
		}
		
		if(!trivialElements.isEmpty()){
			addTrivialElementsToProcessDecompositionTree(trivialElements, rpstNodesTreeParentElement, processDecompositionTreeParent);
		}
	}
	
	private void addTrivialElementsToProcessDecompositionTree(Set<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> trivialElements, IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeParentElement, AbstractBPMNElement processDecompositionTreeParent) {
		Map<AbstractBPMNElement, Integer> elementsMap = new HashMap<AbstractBPMNElement, Integer>();
		elementsMap.put(rpstNodesTreeParentElement.getEntry(), 1);
		elementsMap.put(rpstNodesTreeParentElement.getExit(), 1);
		for(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeElement : trivialElements){
			for(DirectedBPMNEdge directedBPMNEdge : rpstNodesTreeElement.getFragment()){
				for(AbstractBPMNElement element : directedBPMNEdge.getVertices()){
					if(!elementsMap.containsKey(element)){
						elementsMap.put(element, 1);
					} else {
						elementsMap.put(element, elementsMap.get(element) + 1);
					}
				}
			}
		}
		for(AbstractBPMNElement element : elementsMap.keySet()){
			if(elementsMap.get(element) > 1){
				if(!(element instanceof BPMNSubProcess)){
					processDecompositionTree.addChild(processDecompositionTreeParent, element);
				} else {
					//TODO: in eigene Methode auslagern
					//SubProcess zerlegt in den RPST
					BPMNSubProcess subProcess = (BPMNSubProcess) element;
					RPSTBuilder subProcessRPST = new RPSTBuilder(subProcess);
					
					//Sollte möglich sein, da im PreProcessingStep beim RPST bauen alle Start- und Endevents vereinigt werden
					AbstractBPMNElement sourceElement = subProcessRPST.getProcess().getStartEvent();
					AbstractBPMNElement sinkElement = subProcessRPST.getProcess().getEndEvent();
					
					AbstractBPMNElement entryPoint = getPredecessorFromEdges(subProcess, trivialElements);
					if(entryPoint == null){ /* dann ist Element erstes Element der Component und hat nur einen Vorgänger */
						if(subProcess.getPredecessors().size() > 1){
							throw new RuntimeException("Fehler in der Logik!");
						} else {
							entryPoint = (!subProcess.getPredecessors().isEmpty()) ? subProcess.getPredecessors().iterator().next() : null;
						}
					}
					AbstractBPMNElement exitPoint = getSuccessorFromEdges(subProcess, trivialElements);
					if(exitPoint == null){ /* dann ist Element letztes Element der Component und hat nur einen Nachfolger */
						if(subProcess.getSuccessors().size() > 1){
							throw new RuntimeException("Fehler in der Logik!");
						} else {
							exitPoint = (!subProcess.getSuccessors().isEmpty()) ? subProcess.getSuccessors().iterator().next() : null;
						}
					}
					//TODO: Vielleicht keine eigene SubProcess-Component, sondern eher Component-Eigenschaft SubProcess hinzufügen?
					SubProcessComponent subProcessComponent = new SubProcessComponent(entryPoint, sourceElement, exitPoint, sinkElement);
					subProcessComponent.setSubProcess(subProcess);
					//RPST des SubProcess unter der SubProcessComponent einhängen
					if(processDecompositionTreeParent instanceof Component){
						Component parentComponent = (Component) processDecompositionTreeParent;
						if(parentComponent.getSourceElement().equals(subProcess)){
							parentComponent.setSourceElement(subProcessComponent);
						} else if(parentComponent.getSinkElement().equals(subProcess)){
							parentComponent.setSinkElement(subProcessComponent);
						}
					}
					processDecompositionTree.addChild(processDecompositionTreeParent, subProcessComponent);
					addElementsToProcessDecompositionTree(subProcessRPST.getRPSTNodesTree(), subProcessRPST.getRPSTNodesTree().getRootElements(), null, subProcessComponent);
				}
				
			}
		}
	}

	private AbstractBPMNElement getPredecessorFromEdges(AbstractBPMNElement element, Set<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> trivialElements) {
		for(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeElement : trivialElements){
			for(DirectedBPMNEdge directedBPMNEdge : rpstNodesTreeElement.getFragment()){
				if(directedBPMNEdge.getTarget().equals(element)){
					return directedBPMNEdge.getSource();
				}
			}
		}
		return null;
	}
	
	private AbstractBPMNElement getSuccessorFromEdges(AbstractBPMNElement element, Set<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> trivialElements) {
		for(IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rpstNodesTreeElement : trivialElements){
			for(DirectedBPMNEdge directedBPMNEdge : rpstNodesTreeElement.getFragment()){
				if(directedBPMNEdge.getSource().equals(element)){
					return directedBPMNEdge.getTarget();
				}
			}
		}
		return null;
	}

	public BPMNProcess getProcess() {
		return process;
	}

	public MultiDirectedBPMNGraph getGraph() {
		return graph;
	}

	public RPST<DirectedBPMNEdge, AbstractBPMNElement> getRpst() {
		return rpst;
	}

	public SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> getRPSTNodesTree() {
		return rpstNodesTree;
	}

	public SushiTree<AbstractBPMNElement> getProcessDecompositionTree() {
		return processDecompositionTree;
	}

}
