package sushi.query.bpmn;

import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.query.SushiPatternQuery;


/**
 * A factory to generate the pattern queries for a BPMN process to monitor and analyze its execution.
 * @author micha
 */
public class PatternQueryFactory extends AbstractPatternQueryFactory {

	/**
	 * Constructor to create queries with a query factory.
	 * This query factory only delegates to a concrete query factory, 
	 * which corresponds to the current elements pattern type.
	 * @param patternQueryGenerator
	 */
	public PatternQueryFactory(PatternQueryGenerator patternQueryGenerator) {
		super(patternQueryGenerator);
	}
	
	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof Component){
			Component component = (Component) element;
			SushiPatternQuery query = null;
			
			//Falls alle indirekten Children keine Monitoringspoints haben, braucht man auch keine Query
			//AggregationRule für zusammengesetzte Queries
//			if(hasIndirectChildrenMonitoringPoints(component)){
			if(component.hasMonitoringPointsWithEventType()){
				switch(component.getType()){
				case AND:
					query = new AndQueryFactory(patternQueryGenerator).generateQuery(component, catchingMonitorableElement, parentQuery);
					break;
				case LOOP:
					query = new LoopQueryFactory(patternQueryGenerator).generateQuery(component, catchingMonitorableElement, parentQuery);
					break;
				case SEQUENCE:
					query = new SequenceQueryFactory(patternQueryGenerator).generateQuery(component, catchingMonitorableElement, parentQuery);
					break;
				case XOR:
					query = new OrQueryFactory(patternQueryGenerator).generateQuery(component, catchingMonitorableElement, parentQuery);
					break;
				case SUBPROCESS:
					//TODO: Kann SubProcess noch ein externes Catching-Event haben?
					query = new SubProcessQueryFactory(patternQueryGenerator).generateQuery(component, catchingMonitorableElement, parentQuery);
					break;
				default:
					throw new RuntimeException("No supported pattern!");
					//break;
				}
			}
			return query;
		} else {
			System.err.println("Input element should be a component for a LOOP-query!");
			return null;
		}
	}
	
	/**
	 * Searches in all leaf elements of the given node from the ProcessDecompositionTree for monitoring points. 
	 * @param component
	 * @return
	 */
	private boolean hasIndirectChildrenMonitoringPoints(Component component){
		Set<AbstractBPMNElement> children = processDecompositionTree.getLeafs(component);
		for(AbstractBPMNElement child : children){
			if(child.hasMonitoringPoints()){
				return true;
			}
		}
		return false;
	}

	
}
