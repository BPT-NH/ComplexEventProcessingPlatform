package sushi.query.bpmn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.SushiEventType;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiPatternQueryListener;
import sushi.query.SushiQueryTypeEnum;


/**
 * @author micha
 */
public class LoopQueryFactory extends AbstractPatternQueryFactory {

	public LoopQueryFactory(SushiRPSTTree sushiRPSTTree) {
		super(sushiRPSTTree);
	}
	
	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof Component){
			Component component = (Component) element;
			// Operator: UNTIL
			//Sequence-Polygone darin wie immer
			//Until-Bedingung: beobachtbares Element nach der Loop-Bond-Component
			List<AbstractBPMNElement> untilConditionElements = this.getSuccessingMonitorableElement(component);
			//QUERY bauen Elemente der Component (ein Polygon?) UNTIL (conditionElement1 OR conditionElement2 OR ...)
			SushiPatternQuery query = new SushiPatternQuery(generateQueryName("LOOP"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.LOOP, this.orderElements(component));
			
			String queryString = generateLoopQueryString(component, untilConditionElements, catchingMonitorableElement, query);
			
			addQueryRelationship(parentQuery, query);
			query.setQueryString(queryString);
			System.out.println(query.getTitle() + ": " + query.getQueryString());
			
			registerQuery(query);
			if(query != null && query.getListener() != null){
				query.getListener().setCatchingElement(catchingMonitorableElement);
			}
			
			SushiPatternQueryListener listener = query.getListener();
			
			Set<SushiEventType> loopBreakEventTypes = new HashSet<SushiEventType>();
			for(AbstractBPMNElement conditionElement : untilConditionElements){
				loopBreakEventTypes.add(conditionElement.getMonitoringPoints().get(0).getEventType());
			}
			listener.setLoopBreakEventTypes(new ArrayList<SushiEventType>(loopBreakEventTypes));
			
			return query;
			//Element nochmal zu Esper schicken?
		} else {
			System.err.println("Input element should be a component for a LOOP-query!");
			return null;
		}
	}

	private String generateLoopQueryString(Component component, List<AbstractBPMNElement> untilConditionElements, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		//EVERY (([1]S0=SmallSequence) UNTIL (EVERY U0=SecondEvent))
		int elementsWithMonitoringPoints = 0;
		StringBuilder sequencePatternQueryString = new StringBuilder();
		//TODO: Liefert auch noch Gateways am Rand, die nicht betrachtet werden sollten
		List<AbstractBPMNElement> orderedChildren = this.orderElements(component);
		for(AbstractBPMNElement element : orderedChildren){
			//Falls Element Component rekursiv tiefer aufrufen
			StringBuilder queryPart = new StringBuilder();
			if(element instanceof Component){
				SushiPatternQuery subQuery = new PatternQueryFactory(sushiRPSTTree).generateQuery(element, catchingMonitorableElement, parentQuery);
				queryPart.append("[1] S" + elementsWithMonitoringPoints + "=");
				queryPart.append(subQuery.getTitle());
			} else {
				continue;
			}
			if(elementsWithMonitoringPoints == 0){ //Erstes Element
				sequencePatternQueryString.append("SELECT * FROM PATTERN [(EVERY ((");
				sequencePatternQueryString.append(queryPart);
			} else {
				sequencePatternQueryString.append(" " + EsperPatternOperators.XOR.operator + " " + queryPart);
			}
			elementsWithMonitoringPoints++;
		}
		sequencePatternQueryString.append(") " + EsperPatternOperators.LOOP.operator + " (");
		//Eines der UntilConditionElemente sollte als Abbruch ausgelöst werden
		int untilConditionElementsCounter = 0;
		for(AbstractBPMNElement element : untilConditionElements){
			StringBuilder queryPart = new StringBuilder();
			
			queryPart.append("U" + untilConditionElementsCounter + "=");
			queryPart.append(element.getMonitoringPoints().get(0).getEventType().getTypeName());
			
			if(untilConditionElementsCounter == 0){ //Erstes Element
				sequencePatternQueryString.append(queryPart);
			} else {
				sequencePatternQueryString.append(" " + EsperPatternOperators.XOR.operator + " " + queryPart);
			}
			untilConditionElementsCounter++;
		}
		if(catchingMonitorableElement == null){
			sequencePatternQueryString.append(")))]");
		} else {
			sequencePatternQueryString.append(") " + EsperPatternOperators.XOR.operator + " EVERY C1=");
			sequencePatternQueryString.append(catchingMonitorableElement.getMonitoringPoints().get(0).getEventType().getTypeName());
			sequencePatternQueryString.append("))]");
		}
		
		//gleiche ProcessInstanceID-Bedingung anhängen
		//TODO: Funktioniert noch nicht! Es kann nicht auf Pattern-Elemente vor dem Until zugegriffen werden :(
// 		sequencePatternQueryString.append(" WHERE sushi.esper.SushiUtils.isIntersectionNotEmpty({");
//		for(int j = 0; j < elementsWithMonitoringPoints; j++){
//			sequencePatternQueryString.append("S" + j + ".ProcessInstances,");
//		}
//		for(int j = 0; j < untilConditionElementsCounter; j++){
//			if(j != untilConditionElementsCounter - 1){ //letztes Element --> kein Komma
//				sequencePatternQueryString.append("U" + j + ".ProcessInstances,");
//			} else {
//				sequencePatternQueryString.append("U" + j + ".ProcessInstances");
//			}
//		}
//		sequencePatternQueryString.append("})");
		
		return sequencePatternQueryString.toString();
	}
	
	private List<AbstractBPMNElement> getSuccessingMonitorableElement(Component component) {
		Set<AbstractBPMNElement> successingMonitorableElements = new HashSet<AbstractBPMNElement>();
		Set<AbstractBPMNElement> visitedElements = new HashSet<AbstractBPMNElement>();
		traverseSuccessingMonitorableElements(component.getExitPoint(), visitedElements, successingMonitorableElements);
		return new ArrayList<AbstractBPMNElement>(successingMonitorableElements);
	}
	
}
