package sushi.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.esper.SushiEsper;

/**
 * @author micha
 */
public class SushiPatternQuery extends SushiQuery {
	
	private static final long serialVersionUID = 1L;
	private PatternQueryType patternQueryType;
	private SushiPatternQueryListener patternQueryListener;
	private List<AbstractBPMNElement> monitoredElements;
	private SushiPatternQuery parentQuery;
	private Set<SushiPatternQuery> childQueries = new HashSet<SushiPatternQuery>();
	
	public SushiPatternQuery(String title, String queryString, SushiQueryTypeEnum queryType, PatternQueryType patternQueryType) {
		super(title, queryString, queryType);
		this.patternQueryType = patternQueryType;
	}
	
	public SushiPatternQuery(String title, String queryString, SushiQueryTypeEnum queryType, PatternQueryType patternQueryType, List<AbstractBPMNElement> monitoredElements) {
		this(title, queryString, queryType, patternQueryType);
		this.setMonitoredElements(monitoredElements);
	}

	public PatternQueryType getPatternQueryType() {
		return patternQueryType;
	}

	public void setPatternQueryType(PatternQueryType patternQueryType) {
		this.patternQueryType = patternQueryType;
	}
	
	public SushiPatternQueryListener addToEsper(SushiEsper sushiEsper){
		return sushiEsper.addPatternQuery(this);
	}

	public void setListener(SushiPatternQueryListener patternQueryListener) {
		this.patternQueryListener = patternQueryListener;
	}
	
	public SushiPatternQueryListener getListener() {
		return this.patternQueryListener;
	}

	public void setMonitoredElements(List<AbstractBPMNElement> monitoredElements) {
		this.monitoredElements = new ArrayList<AbstractBPMNElement>(monitoredElements);
	}

	/**
	 * Returns a ordered list of the monitored elements of the query.
	 * @return
	 */
	public List<AbstractBPMNElement> getMonitoredElements() {
		return monitoredElements;
	}
	
	@Override
	public String toString() {
		return super.title;
	}

	public SushiPatternQuery getParentQuery() {
		return parentQuery;
	}

	public void setParentQuery(SushiPatternQuery parentQuery) {
		this.parentQuery = parentQuery;
	}

	public Set<SushiPatternQuery> getChildQueries() {
		return childQueries;
	}
	
	public boolean hasChildQueries() {
		return !childQueries.isEmpty();
	}
	
	public void addChildQueries(SushiPatternQuery childQuery) {
		childQueries.add(childQuery);
	}
	
	public void removeChildQueries(SushiPatternQuery childQuery) {
		childQueries.remove(childQuery);
	}

	public void setChildQueries(Set<SushiPatternQuery> childQueries) {
		this.childQueries = childQueries;
	}

}
