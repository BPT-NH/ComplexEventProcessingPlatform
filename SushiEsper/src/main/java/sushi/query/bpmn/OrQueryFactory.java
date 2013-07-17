package sushi.query.bpmn;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiQueryTypeEnum;


/**
 * This query factory creates queries for components of type XOR.
 * @author micha
 */
public class OrQueryFactory extends AbstractPatternQueryFactory {

	/**
	 * Constructor to create XOR queries with a query factory.
	 * @param patternQueryGenerator
	 */
	public OrQueryFactory(PatternQueryGenerator patternQueryGenerator) {
		super(patternQueryGenerator);
	}

	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof Component){
			Component component = (Component) element;
			//TODO: Prüfen, ob auf allen Pfaden der Component Monitoring-Points liegen, sonst kann keine richtige Abfrage erzeugt werden
			if(!allPathesContainMonitoringPoints(component)){
				System.err.println("Query creation failed for: " + element + " Reason: On all pathes of an exclusive component should be a monitorable element!");
				return null;
			}
			//Component sollte mehrere Polygone beinhalten
			//Operator: OR
			SushiPatternQuery query = new SushiPatternQuery(generateQueryName("XOR"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.XOR, this.orderElements(component));
			
			String queryString = generateQueryString(component, EsperPatternOperators.XOR, catchingMonitorableElement, query);
			query.setQueryString(queryString);
			addQueryRelationship(parentQuery, query);
			
			System.out.println(query.getTitle() + ": " + query.getQueryString());
			
			registerQuery(query);
			if(query != null && query.getListener() != null){
				query.getListener().setCatchingElement(catchingMonitorableElement);
			}
			
			return query;
		} else {
			System.err.println("Input element should be a component for an OR-query!");
			return null;
		}
	}

	/**
	 * Proofs, that at least one monitoring point is on all pathes of the component.
	 * @param component
	 * @return
	 */
	private boolean allPathesContainMonitoringPoints(Component component) {
		boolean allChildsHaveMonitoringPoint = true;
		for(AbstractBPMNElement child : patternQueryGenerator.getSushiRPSTTree().getProcessDecompositionTree().getChildren(component)){
			if(!child.hasMonitoringPointsWithEventType()){
				allChildsHaveMonitoringPoint = false;
			}
		}
		return allChildsHaveMonitoringPoint;
	}


}
