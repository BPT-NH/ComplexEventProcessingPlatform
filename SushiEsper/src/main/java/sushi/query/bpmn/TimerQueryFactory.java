package sushi.query.bpmn;

import java.util.Arrays;

import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AttachableElement;
import sushi.event.SushiEventType;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiQueryTypeEnum;


/**
 * @author micha
 */
public class TimerQueryFactory extends AbstractPatternQueryFactory {

	public TimerQueryFactory(SushiRPSTTree sushiRPSTTree) {
		super(sushiRPSTTree);
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
			AttachableElement attachableElement = (AttachableElement) element;
			StringBuilder sequencePatternQueryString = new StringBuilder();
			sequencePatternQueryString.append("SELECT * FROM PATTERN [(");
			
			SushiPatternQuery query = new SushiPatternQuery(generateQueryName("Timer"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.TIMER, Arrays.asList(element));
			addQueryRelationship(parentQuery, query);
			//TODO: Hier die Timer-Query
			SushiPatternQuery subQuery = new StateTransitionQueryFactory(sushiRPSTTree).generateQuery(element, catchingMonitorableElement, query);
			sequencePatternQueryString.append("EVERY S0=" + subQuery.getTitle());
			
			AbstractBPMNElement timerEvent = (AbstractBPMNElement) attachableElement;
			
			String timerEventTypeName = timerEvent.getMonitoringPoints().get(0).getEventType().getTypeName();
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
			System.out.println(queryString);
			
			
			
			registerQuery(query);
			if(query != null && query.getListener() != null){
				query.getListener().setCatchingElement(catchingMonitorableElement);
			}
			
			//Wenn, die SubQuery (StateTransition der Aktivität) gefeuert hat, soll der Timer ausgelöst werden
			subQuery.getListener().setTimer(attachableElement, boundaryTimerEventType, attachableElement.getAttachedIntermediateEvent().getTimeDuration());
			
			return query;
		} else {
			System.err.println("Input element should be a attachable element for an TIMER-query!");
			return null;
		}
	}

}
