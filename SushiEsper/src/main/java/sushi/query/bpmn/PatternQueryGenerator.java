package sushi.query.bpmn;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.collection.SushiTree;

/**
 * Generates queries for esper from the given BPMN process.
 * @author micha
 */
public class PatternQueryGenerator {
	
	private SushiRPSTTree sushiRPSTTree;
	private SushiTree<AbstractBPMNElement> processDecompositionTree;

	public PatternQueryGenerator(SushiRPSTTree sushiRPSTTree){
		this.sushiRPSTTree = sushiRPSTTree;
		this.processDecompositionTree = this.sushiRPSTTree.getProcessDecompositionTree();
	}
	
	public void generateQueries(){
		for(AbstractBPMNElement rootElement : processDecompositionTree.getRootElements()){
			if(rootElement instanceof Component){
				new PatternQueryFactory(sushiRPSTTree).generateQuery(rootElement, null, null);
			} else {
				throw new RuntimeException("Queries can only be generated from components!");
			}
		}
	}

}
