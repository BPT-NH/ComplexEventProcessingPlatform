package sushi.query.bpmn;

import java.util.HashSet;
import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.RPSTBuilder;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.collection.SushiTree;
import sushi.query.SushiPatternQuery;

/**
 * This class is intended as a controller to generate queries for Esper from a BPMN process.
 * @author micha
 */
public class PatternQueryGenerator {
	
	private RPSTBuilder sushiRPSTTree;
	private SushiTree<AbstractBPMNElement> processDecompositionTree;
	private Set<SushiPatternQuery> queries = new HashSet<SushiPatternQuery>();

	/**
	 * Constructor for the PatternQueryGenerator, which is intended as a controller to generate queries from a BPMN process.
	 * @param sushiRPSTTree
	 */
	public PatternQueryGenerator(RPSTBuilder sushiRPSTTree){
		this.sushiRPSTTree = sushiRPSTTree;
		this.processDecompositionTree = this.sushiRPSTTree.getProcessDecompositionTree();
	}
	
	/**
	 * This method generates queries for the given BPMN process.
	 */
	public void generateQueries(){
		//StateTransitionQueries werden hier schon einmal erstellt, dass sie zum Beispiel 
		//für einen IntermediateTimer in der TimerQueryFactory, schon bekannt sind und 
		//berücksichtig werden können
		for(AbstractBPMNElement treeElement : processDecompositionTree.getLeafElements()){
			if(treeElement.hasMonitoringPointsWithEventType()){
				SushiPatternQuery stateTransitionQuery = new StateTransitionQueryFactory(this).generateQuery(treeElement, null, null);
				queries.add(stateTransitionQuery);
			}
		}
		for(AbstractBPMNElement rootElement : processDecompositionTree.getRootElements()){
			if(rootElement instanceof Component){
				new PatternQueryFactory(this).generateQuery(rootElement, null, null);
			} else {
				throw new RuntimeException("Queries can only be generated from components!");
			}
		}
	}

	public RPSTBuilder getSushiRPSTTree() {
		return sushiRPSTTree;
	}

	public void setSushiRPSTTree(RPSTBuilder sushiRPSTTree) {
		this.sushiRPSTTree = sushiRPSTTree;
	}

	public SushiTree<AbstractBPMNElement> getProcessDecompositionTree() {
		return processDecompositionTree;
	}

	public void setProcessDecompositionTree(
			SushiTree<AbstractBPMNElement> processDecompositionTree) {
		this.processDecompositionTree = processDecompositionTree;
	}

	public Set<SushiPatternQuery> getQueries() {
		return queries;
	}

	public void setQueries(Set<SushiPatternQuery> queries) {
		this.queries = queries;
	}
	
	public void addQuery(SushiPatternQuery query) {
		this.queries.add(query);
	}
	
	public void removeQuery(SushiPatternQuery query) {
		this.queries.remove(query);
	}

	/**
	 * This method searches for {@link SushiPatternQuery}s, which contain only the given element.
	 * @param element
	 * @return
	 */
	public SushiPatternQuery getQueryForElement(AbstractBPMNElement element) {
		for(SushiPatternQuery query : queries){
			if(query.getMonitoredElements().contains(element) && query.getMonitoredElements().size() == 1){
				return query;
			}
		}
		return null;
	}

}
