package sushi.bpmn.decomposition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNSubProcess;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.event.collection.SushiTree;

/**
 * This class searches for bond components in the given tree and tries to assign {@link IPattern} as the {@link BondComponent#setType(IPattern)}.
 * @author micha
 *
 */
public class PatternUtil {
	
	/**
	 * Searches in the given tree for bond components and try to assign {@link IPattern} to the components.
	 * @param processDecompositionTree
	 */
	public static void determinePatternForTreeComponents(SushiTree<AbstractBPMNElement> processDecompositionTree){
		for(AbstractBPMNElement element : processDecompositionTree.getElements()){
			if(element instanceof BondComponent){
				BondComponent bond = (BondComponent) element;
				List<AbstractBPMNElement> bondChildren = processDecompositionTree.getChildren(bond);
				determinePatternForComponent(bond, bondChildren, processDecompositionTree);
			} else if(element instanceof PolygonComponent){
				((PolygonComponent) element).setType(IPattern.SEQUENCE);
			} else if(element instanceof SubProcessComponent){
				((SubProcessComponent) element).setType(IPattern.SUBPROCESS);
			}
		}
	}

	private static void determinePatternForComponent(BondComponent bond, List<AbstractBPMNElement> bondChildren, SushiTree<AbstractBPMNElement> processDecompositionTree) {
		AbstractBPMNElement sourceElement = bond.getSourceElement();
		AbstractBPMNElement sinkElement = bond.getSinkElement();
		
		//Detection of different patterns
		if(sourceElement instanceof BPMNAndGateway && sourceElement.getPredecessors().size() == 1){
			bond.setType(IPattern.AND);
		} else if(sourceElement instanceof BPMNXORGateway && sourceElement.getPredecessors().size() == 1 || sinkElement instanceof BPMNXORGateway && sinkElement.getSuccessors().size() == 1){
			bond.setType(IPattern.XOR);
		} else if(sourceElement instanceof BPMNXORGateway && isCyclic(bond, processDecompositionTree)){
			bond.setType(IPattern.LOOP);
		}
	}
	
	private static boolean isCyclic(BondComponent bond, SushiTree<AbstractBPMNElement> processDecompositionTree){
		Set<AbstractBPMNElement> bondChildren = processDecompositionTree.getLeafs(bond);
		AbstractBPMNElement sourceElement = bond.getSourceElement();
		Set<AbstractBPMNElement> visitedElements = new HashSet<AbstractBPMNElement>();
		visitSuccessors(sourceElement, bondChildren, visitedElements);
		return visitedElements.contains(sourceElement);
	}

	/**
	 * Adds all indirect successors of the startElement to visitedElements, if the element is contained in elements.
	 * @param startElement
	 * @param elements
	 * @param visitedElements
	 */
	private static void visitSuccessors(AbstractBPMNElement startElement, Set<AbstractBPMNElement> elements, Set<AbstractBPMNElement> visitedElements) {
		if(elements.contains(startElement) && !visitedElements.contains(startElement)){
			visitedElements.add(startElement);
			for(AbstractBPMNElement successor : startElement.getSuccessors()){
				visitSuccessors(successor, elements, visitedElements);
			}
		}
	}

}
