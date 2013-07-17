package sushi.query.bpmn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.decomposition.BondComponent;
import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.SubProcessComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AttachableElement;
import sushi.bpmn.element.BPMNBoundaryEvent;
import sushi.bpmn.element.BPMNEventType;
import sushi.bpmn.element.BPMNIntermediateEvent;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.collection.SushiTree;
import sushi.eventhandling.Broker;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.process.SushiProcess;
import sushi.query.SushiPatternQuery;

/**
 * This class defines the abstract factory methods to generate the {@link SushiPatternQuery}s. 
 * The queries are created in the concrete subclasses.
 * @author micha
 */
public abstract class AbstractPatternQueryFactory {
	
	protected SushiTree<AbstractBPMNElement> processDecompositionTree;
	protected PatternQueryGenerator patternQueryGenerator;
	private static List<String> queryNames = new ArrayList<String>();
	
	/**
	 * Constructor to create queries with a query factory.
	 * @param patternQueryGenerator
	 */
	public AbstractPatternQueryFactory(PatternQueryGenerator patternQueryGenerator){
		this.patternQueryGenerator = patternQueryGenerator;
		this.processDecompositionTree = patternQueryGenerator.getProcessDecompositionTree();
	}
	
	/**
	 * This method generates a {@link SushiPatternQuery} for the element. 
	 * The type of the query is depending on the concrete PatternQueryFactory. 
	 * If the element is a component, then the  children of the element are used for the query.
	 * The catchingMonitorableElement can specify an attached element for this query. 
	 * If the catchingMonitorableElement is monitored, the query finishes immediately.
	 * The parentQuery is the query, that contains this new created one.
	 * @param element
	 * @param catchingMonitorableElement
	 * @param parentQuery
	 * @return
	 */
	protected abstract SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery);
	
	/**
	 * Registers the query at Esper and for the {@link BPMNQueryMonitor}.
	 * @param query
	 */
	protected void registerQuery(SushiPatternQuery query) {
		this.addPatternEventTypeToEsper(query.getTitle());
		
		BPMNQueryMonitor.getInstance().addQueryForProcess(query, SushiProcess.findByBPMNProcess(patternQueryGenerator.getSushiRPSTTree().getProcess()));
		
		query.setListener(query.addToEsper(SushiStreamProcessingAdapter.getInstance()));
	}
	
	/**
	 * This method is indicated to update a query that is already registered at Esper.
	 * @param query
	 */
	protected void updateQuery(SushiPatternQuery query) {
		query.updateForEsper(SushiStreamProcessingAdapter.getInstance());
	}
	
	/**
	 * @param name
	 * @return
	 */
	public SushiEventType addPatternEventTypeToEsper(String name){
		name = name.replaceAll(" ", "");
		SushiEventType patternEventType = new SushiEventType(name, new SushiAttributeTree(), "Timestamp");
		//TODO: Muss EventType wirklich gespeichert werden?
		Broker.send(patternEventType);
		return patternEventType;
	}
	
	/**
	 * This method generates the actual query, that is needed for Esper 
	 * and can be stored as the queryString in the {@link SushiPatternQuery}.
	 * The concrete generated string depends on the patternOperator, 
	 * which is used as an infix operator while query creation.  
	 * @param component
	 * @param patternOperator
	 * @param catchingMonitorableElement
	 * @param parentQuery
	 * @return
	 */
	protected String generateQueryString(Component component, EsperPatternOperators patternOperator, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		int elementsWithMonitoringPoints = 0;
		StringBuilder sequencePatternQueryString = new StringBuilder();
		//TODO: Liefert auch noch Gateways am Rand, die nicht betrachtet werden sollten
		//TODO: Timer-Events 
		List<AbstractBPMNElement> orderedChildren = this.orderElements(component);
		for(AbstractBPMNElement element : orderedChildren){
			//Falls Element Component rekursiv tiefer aufrufen
			StringBuilder queryPart = new StringBuilder();
			if(element instanceof Component && element.hasMonitoringPoints() && element.hasMonitoringPointsWithEventType()){
				SushiPatternQuery subQuery = new PatternQueryFactory(patternQueryGenerator).generateQuery(element, catchingMonitorableElement, parentQuery);
				addQueryRelationship(parentQuery, subQuery);
				
				queryPart.append("EVERY S" + elementsWithMonitoringPoints + "=");
				queryPart.append(subQuery.getTitle());
			} 
			//Element hat Attached Timer
			else if(
					element instanceof AttachableElement && 
					element.hasMonitoringPointsWithEventType() &&
					((AttachableElement)element).hasAttachedIntermediateEvent() && 
					((AttachableElement)element).getAttachedIntermediateEvent().getIntermediateEventType() == BPMNEventType.Timer &&
					orderedChildren.contains(((AttachableElement)element).getAttachedIntermediateEvent())){
				//Püfen, ob Boundary Event auch im Polygon ist, sonst wird der Timer hier nicht abgefragt
				AttachableElement attachableElement = (AttachableElement)element;
				BPMNBoundaryEvent boundaryEvent = attachableElement.getAttachedIntermediateEvent();
				//Timer-Query bauen
				System.err.println("Timer Query");
				SushiPatternQuery subQuery = new TimerQueryFactory(patternQueryGenerator).generateQuery(element, catchingMonitorableElement, parentQuery);
				addQueryRelationship(parentQuery, subQuery);
				
				queryPart.append("EVERY S" + elementsWithMonitoringPoints + "=");
				queryPart.append(subQuery.getTitle());
				//Timer-Element und Boundary-Timer aus den orderedChildren entfernen (ModifyException?)
				orderedChildren.removeAll(Arrays.asList(attachableElement, boundaryEvent));
			} 
			//Element ist IntermediateTimer
			else if(element instanceof BPMNIntermediateEvent && ((BPMNIntermediateEvent)element).getIntermediateEventType().equals(BPMNEventType.Timer)){
				//Timer-Query bauen
				System.err.println("Timer Query");
				SushiPatternQuery subQuery = new TimerQueryFactory(patternQueryGenerator).generateQuery(element, catchingMonitorableElement, parentQuery);
				addQueryRelationship(parentQuery, subQuery);
				
				queryPart.append("EVERY S" + elementsWithMonitoringPoints + "=");
				queryPart.append(subQuery.getTitle());
			}
			//Normales Element: Activity Lifecycle Query
			else if(element.hasMonitoringPointsWithEventType()){
				SushiPatternQuery subQuery = new StateTransitionQueryFactory(patternQueryGenerator).generateQuery(element, catchingMonitorableElement, parentQuery);
				
				System.out.println(subQuery.getTitle() + ": " + subQuery.getQueryString());
				
				queryPart.append("EVERY S" + elementsWithMonitoringPoints + "=");
				queryPart.append(subQuery.getTitle());
			} else {
				continue;
			}
			if(elementsWithMonitoringPoints == 0){ //Erstes Element
				sequencePatternQueryString.append("SELECT * FROM PATTERN [(");
				sequencePatternQueryString.append(queryPart);
			} else {
				sequencePatternQueryString.append(" " + patternOperator.operator + " " + queryPart);
			}
			elementsWithMonitoringPoints++;
		}
		if(catchingMonitorableElement == null){
			sequencePatternQueryString.append(")]");
		} else {
			sequencePatternQueryString.append(") " + EsperPatternOperators.XOR.operator + " EVERY C1=");
			sequencePatternQueryString.append(catchingMonitorableElement.getMonitoringPoints().get(0).getEventType().getTypeName());
			sequencePatternQueryString.append("]");
		}
		
		if(patternOperator != EsperPatternOperators.XOR){
			//gleiche ProcessInstanceID-Bedingung anhängen
			sequencePatternQueryString.append(" WHERE sushi.esper.SushiUtils.isIntersectionNotEmpty({");
			for(int j = 0; j < elementsWithMonitoringPoints; j++){
				if(j == elementsWithMonitoringPoints - 1){ //letztes Element --> kein Komma
					sequencePatternQueryString.append("S" + j + ".ProcessInstances");
				} else {
					sequencePatternQueryString.append("S" + j + ".ProcessInstances,");
				}
			}
			sequencePatternQueryString.append("})");
		}
		return sequencePatternQueryString.toString();
	}
	
	/**
	 * Orders the elements in a list in their sequential order in a process.
	 * Elements have to be consecutive.
	 * @param component
	 * @return
	 */
	protected List<AbstractBPMNElement> orderElements(Component component) {
		List<AbstractBPMNElement> componentChildren = new ArrayList<AbstractBPMNElement>(processDecompositionTree.getChildren(component));
		List<AbstractBPMNElement> orderedElements = new ArrayList<AbstractBPMNElement>();
		
		int componentChildrenAmount = componentChildren.size();
		
		//Bonds brauchen nicht sortiert werden
		if(component instanceof BondComponent){
			return processDecompositionTree.getChildren(component);
		}

		//Startelement einfügen
		AbstractBPMNElement startElement = component.getSourceElement();
		if(startElement == null){
			throw new RuntimeException("No source element for component!");
		}
		orderedElements.add(startElement);
		componentChildren.remove(startElement);
		
		for(int i = 0; i < componentChildrenAmount; i++){
			AbstractBPMNElement lastOrderedElement = orderedElements.get(orderedElements.size() - 1);
//			Set<AbstractBPMNElement> successors = lastOrderedElement.getSuccessors();
			Set<AbstractBPMNElement> successors = getSuccessorsWithComponents(lastOrderedElement, componentChildren);
			List<AbstractBPMNElement> componentChildrenCopy = new ArrayList<AbstractBPMNElement>(componentChildren);
			componentChildrenCopy.retainAll(successors);
			if(componentChildrenCopy.size() > 0){
				orderedElements.add(componentChildrenCopy.get(0));
			}
		}
		
		if(componentChildrenAmount != orderedElements.size()){
			throw new RuntimeException("Elements could not be ordered!");
		}
		return orderedElements;
	}
	
	private Set<AbstractBPMNElement> getSuccessorsWithComponents(AbstractBPMNElement predecessor, List<AbstractBPMNElement> elements) {
		Set<AbstractBPMNElement> successors;
		if(predecessor instanceof Component){
			Component component = (Component) predecessor;
			if(predecessor instanceof SubProcessComponent){
				SubProcessComponent subProcessComponent = (SubProcessComponent) predecessor;
				successors = new HashSet<AbstractBPMNElement>(subProcessComponent.getSubProcess().getSuccessors());
			} else {
				successors = new HashSet<AbstractBPMNElement>(component.getSinkElement().getSuccessors());
				//Falls Component eine Schleife ist, könnten hier auch Elemente aus der Component als Nachfolger auftauchen
				successors.removeAll(component.getChildren());
			}
			//Falls Component eine Schleife ist, könnten hier auch Elemente aus der Component als Nachfolger auftauchen
			successors.removeAll(component.getChildren());
			
		} else {
			successors = new HashSet<AbstractBPMNElement>(predecessor.getSuccessors());
		}
		for(AbstractBPMNElement element : elements){
			if(element instanceof Component){
				Component component = (Component) element;
				if(successors.contains(component.getSourceElement())){
					successors.add(element);
				}
			}
		}
		return successors;
	}
	
	/**
	 * @param element
	 * @param visitedElements
	 * @param successingMonitorableElements
	 */
	protected void traverseSuccessingMonitorableElements(AbstractBPMNElement element, Set<AbstractBPMNElement> visitedElements, Set<AbstractBPMNElement> successingMonitorableElements) {
		if(!visitedElements.contains(element)){
			visitedElements.add(element);
			if(element.hasMonitoringPoints()){
				successingMonitorableElements.add(element);
			} else {
				for(AbstractBPMNElement child : element.getSuccessors()){
					traverseSuccessingMonitorableElements(child, visitedElements, successingMonitorableElements);
				}
			}
		}
		
		
	}
	
	/**
	 * Connects the two queries as parent and child.
	 * @param parent
	 * @param child
	 */
	protected void addQueryRelationship(SushiPatternQuery parent, SushiPatternQuery child){
		child.setParentQuery(parent);
		if(parent != null){
			parent.addChildQueries(child);
		}
	}
	
	/**
	 * This method generates a unique name for each query. 
	 * So that query names can be used as event types and do not intersect.
	 * @param prefix
	 * @return
	 */
	protected String generateQueryName(String prefix){
		String queryName;
		do{
			queryName = prefix + new Date().getTime();
		} while(queryNames.contains(queryName));
		queryNames.add(queryName);
		return queryName;
	}

}
