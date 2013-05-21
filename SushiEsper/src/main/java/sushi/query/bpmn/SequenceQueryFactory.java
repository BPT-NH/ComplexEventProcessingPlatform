package sushi.query.bpmn;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiQueryTypeEnum;


/**
 * @author micha
 */
public class SequenceQueryFactory extends AbstractPatternQueryFactory {

	public SequenceQueryFactory(SushiRPSTTree sushiRPSTTree) {
		super(sushiRPSTTree);
	}
	
	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof Component){
			Component component = (Component) element;
			//Operator: ->
			SushiPatternQuery query = new SushiPatternQuery(generateQueryName("Sequence"), null, SushiQueryTypeEnum.LIVE, PatternQueryType.SEQUENCE, this.orderElements(component));
			
			String queryString = generateQueryString(component, EsperPatternOperators.SEQUENCE, catchingMonitorableElement, query);
			query.setQueryString(queryString);
			addQueryRelationship(parentQuery, query);
			
			System.out.println(query.getTitle() + ": " + query.getQueryString());
			
			registerQuery(query);
			if(query != null && query.getListener() != null){
				query.getListener().setCatchingElement(catchingMonitorableElement);
			}
			
			return query;
		} else {
			System.err.println("Input element should be a component for a sequential query!");
			return null;
		}
	}

}
