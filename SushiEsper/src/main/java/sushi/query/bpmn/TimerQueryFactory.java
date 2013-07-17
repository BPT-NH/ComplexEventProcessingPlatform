package sushi.query.bpmn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AttachableElement;
import sushi.bpmn.element.BPMNEventType;
import sushi.bpmn.element.BPMNIntermediateEvent;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEventType;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiQueryTypeEnum;


/**
 * This class creates Esper-Queries to observe BPMN timer-events, like attached timer events or intermediate timer events.
 * @author micha
 */
public class TimerQueryFactory extends AbstractPatternQueryFactory {

	public TimerQueryFactory(PatternQueryGenerator patternQueryGenerator) {
		super(patternQueryGenerator);
	}
	
	/**
	 * This method should generate a query for a single BPMN element that has an attached timer, 
	 * this query makes it possible to fire an event after a specified time interval.
	 * @param element
	 * @param catchingMonitorableElement
	 * @return
	 */
	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof AttachableElement){
			return generateAttachedTimerQuery(element, catchingMonitorableElement, parentQuery);
		} else if(element instanceof BPMNIntermediateEvent){
			return generateInterMediateTimerQuery(element, catchingMonitorableElement, parentQuery);
		} else {
			System.err.println("Input element should be a attachable element for an TIMER-query!");
			return null;
		}
	}

	private SushiPatternQuery generateAttachedTimerQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		StringBuilder sequencePatternQueryString = new StringBuilder();
		sequencePatternQueryString.append("SELECT * FROM PATTERN [(");
		
		SushiPatternQuery query = new SushiPatternQuery(generateQueryName("Timer"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.TIMER, Arrays.asList(element));
		addQueryRelationship(parentQuery, query);
		//TODO: Hier die Timer-Query
		SushiPatternQuery subQuery = new StateTransitionQueryFactory(patternQueryGenerator).generateQuery(element, catchingMonitorableElement, query);
		sequencePatternQueryString.append("EVERY S0=" + subQuery.getTitle());
		
		String timerEventTypeName = element.getMonitoringPoints().get(0).getEventType().getTypeName();
		SushiEventType boundaryTimerEventType = addPatternEventTypeToEsper(timerEventTypeName + "BoundaryTimer");
		
		sequencePatternQueryString.append(" " + EsperPatternOperators.SEQUENCE.operator + " EVERY S1=" + boundaryTimerEventType.getTypeName());
		
		sequencePatternQueryString.append(") ");
		
		if(catchingMonitorableElement == null){
			sequencePatternQueryString.append("]");
		} else {
			sequencePatternQueryString.append(" " + EsperPatternOperators.XOR.operator + " EVERY C1=");
			sequencePatternQueryString.append(catchingMonitorableElement.getMonitoringPoints().get(0).getEventType().getTypeName());
			sequencePatternQueryString.append("]");
		}
		
		sequencePatternQueryString.append(" WHERE sushi.esper.SushiUtils.isIntersectionNotEmpty({S0.ProcessInstances, S1.ProcessInstances})");
		
		String queryString = sequencePatternQueryString.toString();
		query.setQueryString(queryString);
		System.out.println(query.getTitle() + ": " + queryString);
		
		
		
		registerQuery(query);
		if(query != null && query.getListener() != null){
			query.getListener().setCatchingElement(catchingMonitorableElement);
		}
		
		//Wenn, die SubQuery (StateTransition der Aktivität) gefeuert hat, soll der Timer ausgelöst werden
		SushiEventType attachableElementEventType = element.getMonitoringPoints().get(0).getEventType();
		subQuery.getListener().setTimer(attachableElementEventType.getTypeName(), boundaryTimerEventType, ((AttachableElement) element).getAttachedIntermediateEvent().getTimeDuration());
		
		return query;
	}
	
	private SushiPatternQuery generateInterMediateTimerQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		BPMNIntermediateEvent intermediateEvent = (BPMNIntermediateEvent) element;
		SushiPatternQuery timerQuery = null;
		if(intermediateEvent.getIntermediateEventType().equals(BPMNEventType.Timer)){
			StringBuilder sequencePatternQueryString = new StringBuilder();
			
			//SELECT * FROM PATTERN [((EVERY VOR1 OR VOR2 ... ) -> Timer)]
			timerQuery = new SushiPatternQuery(generateQueryName("Timer"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.TIMER, Arrays.asList(element));
			addQueryRelationship(parentQuery, timerQuery);
			
			//Nach Vorgängern mit MonitoringPoints suchen
			Set<AbstractBPMNElement> monitorablePredecessors = new HashSet<AbstractBPMNElement>();
			Set<AbstractBPMNElement> visitedpredecessors = new HashSet<AbstractBPMNElement>();
			getNearestMonitorablePredecessors(intermediateEvent, monitorablePredecessors, visitedpredecessors);
			
			List<SushiPatternQuery> predecessorQueries = new ArrayList<SushiPatternQuery>(findStateTransitionQueriesForElements(monitorablePredecessors));
			
			if(!predecessorQueries.isEmpty()){
				
				sequencePatternQueryString.append("SELECT * FROM PATTERN [(");
				
				//Für alle Vorgänger eine OR-Query erzeugen, sodass jeder der Vorgänger den Timer triggern kann
				SushiPatternQuery predecessorOrQuery = createPredecessorORQuery(predecessorQueries);
				
				sequencePatternQueryString.append("EVERY S1=" + predecessorOrQuery.getTitle() + " ");
				
				SushiEventType interMediateTimerEventType = addPatternEventTypeToEsper(intermediateEvent.getName() + "Timer");
				sequencePatternQueryString.append(EsperPatternOperators.SEQUENCE.operator  + " EVERY S2=" + interMediateTimerEventType.getTypeName() + ")");
				
				if(catchingMonitorableElement == null){
					sequencePatternQueryString.append("]");
				} else {
					sequencePatternQueryString.append(" " + EsperPatternOperators.XOR.operator + " EVERY C1=");
					sequencePatternQueryString.append(catchingMonitorableElement.getMonitoringPoints().get(0).getEventType().getTypeName());
					sequencePatternQueryString.append("]");
				}
				
				//Gleiche Prozessinstanzbedingung anhängen
				sequencePatternQueryString.append(" WHERE sushi.esper.SushiUtils.isIntersectionNotEmpty({S1.ProcessInstances, S2.ProcessInstances})");

				String queryString = sequencePatternQueryString.toString();
				timerQuery.setQueryString(queryString);
				System.out.println(timerQuery.getTitle() + ": " + queryString);
				
				registerQuery(timerQuery);
				if(timerQuery != null && timerQuery.getListener() != null){
					timerQuery.getListener().setCatchingElement(catchingMonitorableElement);
				}
				
				//Wenn die beobachtbaren Vorgängerqueries (StateTransition der Elemente) gefeuert haben, soll der Timer ausgelöst werden
				for(SushiPatternQuery predecessorQuery : predecessorQueries){
					AbstractBPMNElement timerTrigger = predecessorQuery.getMonitoredElements().get(0);
					SushiEventType timmerTriggerEventType = timerTrigger.getMonitoringPoints().get(0).getEventType();
					predecessorQuery.getListener().setTimer(timmerTriggerEventType.getTypeName(), interMediateTimerEventType, intermediateEvent.getTimeDuration());
				}
			}
		}
		return timerQuery;
	}
	
	private SushiPatternQuery createPredecessorORQuery(List<SushiPatternQuery> predecessorQueries){
		SushiPatternQuery predecessorOrQuery = new SushiPatternQuery(generateQueryName("TimerPredecessor"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.XOR, new ArrayList<AbstractBPMNElement>());
		StringBuilder sequencePatternQueryString = new StringBuilder();
		for(int i = 0; i < predecessorQueries.size(); i++){
			SushiPatternQuery predecessorQuery = predecessorQueries.get(i);
			sequencePatternQueryString.append("SELECT * FROM PATTERN [(");
			if(i < predecessorQueries.size() - 1){
				sequencePatternQueryString.append("EVERY S" + i + "=" + predecessorQuery.getTitle() + EsperPatternOperators.XOR.operator);
			} else { //Letztes Element, daher kein OR-Operator mehr
				sequencePatternQueryString.append("EVERY S" + i + "=" + predecessorQuery.getTitle());
			}
		}
		sequencePatternQueryString.append(")]");
		
		predecessorOrQuery.setQueryString(sequencePatternQueryString.toString());
		
		addPatternEventTypeToEsper(predecessorOrQuery.getTitle());
		predecessorOrQuery.setListener(predecessorOrQuery.addToEsper(SushiStreamProcessingAdapter.getInstance()));
		
		return predecessorOrQuery;
	}

	private Set<SushiPatternQuery> findStateTransitionQueriesForElements(Set<AbstractBPMNElement> elements) {
		Set<SushiPatternQuery> matchingQueries = new HashSet<SushiPatternQuery>();
		for(AbstractBPMNElement element : elements){
			SushiPatternQuery query = this.patternQueryGenerator.getQueryForElement(element);
			if(query != null){
				matchingQueries.add(query);
			}
		}
		return matchingQueries;
	}

	/**
	 * Searches for all monitorable predecessors of the specified element and adds them to monitorablePredecessors, but only the nearest on every path.
	 * @param element
	 * @param monitorablePredecessors
	 * @param visitedpredecessors
	 */
	private void getNearestMonitorablePredecessors(AbstractBPMNElement element, Set<AbstractBPMNElement> monitorablePredecessors, Set<AbstractBPMNElement> visitedpredecessors) {
		if(!visitedpredecessors.contains(element)){
			visitedpredecessors.add(element);
			for(AbstractBPMNElement predecessor : element.getPredecessors()){
				if(predecessor.hasMonitoringPointsWithEventType()){
					monitorablePredecessors.add(predecessor);
				} else {
					getNearestMonitorablePredecessors(predecessor, monitorablePredecessors, visitedpredecessors);
				}
			}
		}
		
	}

}
