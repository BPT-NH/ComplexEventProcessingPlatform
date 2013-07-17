package sushi.monitoring.bpmn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNEndEvent;
import sushi.event.collection.SushiTree;
import sushi.query.PatternQueryType;
import sushi.query.SushiPatternQuery;

/**
 * The ViolationMonitor tries to reveal violations while the execution of process instances.
 * For instance order, exclusiveness or cooccurence violations.
 * @author micha
 */
public class ViolationMonitor {

	private ProcessInstanceMonitor processInstanceMonitor;
	private SushiTree<AbstractBPMNElement> processDecompositionTree;

	/**
	 * Creates a new ViolationMonitor for the given {@link ProcessInstanceMonitor} to monitor execution violations.
	 * @param processInstanceMonitor
	 */
	public ViolationMonitor(ProcessInstanceMonitor processInstanceMonitor) {
		this.processInstanceMonitor = processInstanceMonitor;
		this.processDecompositionTree = this.processInstanceMonitor.getProcessInstance().getProcess().getProcessDecompositionTree();
	}
	
	/**
	 * Searches for order, exclusiveness and cooccurence violations, during the execution of the specified process instance.
	 */
	public void searchForViolations(){
		searchForOrderViolations();
		searchForExclusivenessViolations();
		searchForOccurenceViolations();
		searchForLoopViolations();
	}

	/**
	 * This method searches for order violations in sequential components. So if the elements in the component are triggered 
	 * in the false order, the sequential components has an order violation
	 */
	private void searchForOrderViolations() {
		//TODO: OrderViolation: Elemente in einer Sequenz, Reihenfolge der Elemente ermitteln, falls Elemente alle getriggert, aber in falscher Reihenfolge
		for(QueryMonitor queryMonitor : processInstanceMonitor.getQueryMonitorsWithQueryType(PatternQueryType.SEQUENCE)){
			if(queryMonitor.isRunning()){
				List<QueryMonitor> subQueryMonitors = processInstanceMonitor.getSubQueryMonitors(queryMonitor);
				if(!subQueryMonitors.contains(null) && subQueryMonitors.size() > 1){
					subQueryMonitors = orderQueryMonitorsSequential(queryMonitor);
					boolean allSubQueriesTerminated = true;
					boolean timeViolation = false;
					
					int queryExecution = subQueryMonitors.get(0).getExecutionCount();
					Date queryEndTime = subQueryMonitors.get(0).getEndTime();
					
					for(QueryMonitor subQueryMonitor : subQueryMonitors){
						//TODO: Sollte Status terminate gefragt werden, also auch skipped oder nur finished?
						//Alle terminated und gleiche Anzahl von Executions
						if(!subQueryMonitor.isTerminated() || subQueryMonitor.getExecutionCount() != queryExecution){
							allSubQueriesTerminated = false;
							break;
						}
						if(queryEndTime.after(subQueryMonitor.getEndTime())){
							timeViolation = true;
						}
						queryEndTime = subQueryMonitor.getEndTime();
					}
					//Alle Subqueries sind durchgelaufen
					if(allSubQueriesTerminated && timeViolation){
						queryMonitor.addViolationStatus(ViolationStatus.Order);
						queryMonitor.setQueryStatus(QueryStatus.Finished);
						//TODO: sollte adaptQueries hier nochmal gerufen werden?
					}
				}
			}
		}
	}
	
	/**
	 * Tries to order {@link QueryMonitor}s which belong to a sequence.
	 * @param sequentialQueryMonitors
	 * @return
	 */
	private List<QueryMonitor> orderQueryMonitorsSequential(QueryMonitor sequentialQueryMonitor) {
		List<QueryMonitor> subQueryMonitors = processInstanceMonitor.getSubQueryMonitors(sequentialQueryMonitor);
		List<AbstractBPMNElement> sequentialParents = processDecompositionTree.getParents(sequentialQueryMonitor.getQuery().getMonitoredElements());
		if(sequentialParents.size() == 1 && sequentialParents.get(0) instanceof Component){
			List<QueryMonitor> orderedQueryMonitors = new ArrayList<QueryMonitor>();
			Component sequentialComponent = (Component) sequentialParents.get(0);
			//Hier sollte jeweils nur ein Element zurückkommen
			orderedQueryMonitors.addAll(processInstanceMonitor.getQueryMonitorsWithMonitoredElements(Arrays.asList(sequentialComponent.getSourceElement())));
			orderedQueryMonitors.addAll(processInstanceMonitor.getQueryMonitorsWithMonitoredElements(Arrays.asList(sequentialComponent.getSinkElement())));
			for(QueryMonitor queryMonitor : subQueryMonitors){
				if(!orderedQueryMonitors.contains(queryMonitor)){
					QueryMonitor orderQueryMonitor;
					if(searchPredecessor(queryMonitor, orderedQueryMonitors) != null){
						orderQueryMonitor = searchPredecessor(queryMonitor, orderedQueryMonitors);
						orderedQueryMonitors.add(orderedQueryMonitors.indexOf(orderQueryMonitor) + 1, queryMonitor);

					} else if(searchSuccessor(queryMonitor, orderedQueryMonitors) != null){
						orderQueryMonitor = searchSuccessor(queryMonitor, orderedQueryMonitors);
						orderedQueryMonitors.add(orderedQueryMonitors.indexOf(orderQueryMonitor), queryMonitor);
						
					} else {
						orderedQueryMonitors.add(queryMonitor);
					}
				}
			}
			return orderedQueryMonitors;
		} else {
			System.err.println("Elements could not be ordered!");
			return subQueryMonitors;
		}
	}
	
	/**
	 * Searches for an QueryMonitor from the list of orderedQueryMonitors, that could be the predecessor of the specified QueryMonitor.
	 * @param queryMonitor
	 * @param orderedQueryMonitors
	 * @return
	 */
	private QueryMonitor searchPredecessor(QueryMonitor queryMonitor, List<QueryMonitor> orderedQueryMonitors) {
		Set<AbstractBPMNElement> predecessors;
		for(QueryMonitor orderedQueryMonitor : orderedQueryMonitors){
			predecessors = new HashSet<AbstractBPMNElement>();
			if(queryMonitor.getQuery().getPatternQueryType().equals(PatternQueryType.STATETRANSITION)){
				for(AbstractBPMNElement monitoredElement : queryMonitor.getQuery().getMonitoredElements()){
					predecessors.addAll(monitoredElement.getPredecessors());
				}
			//Wenn es keine StateTransition ist, beobacht die Query eine Component, also bekommt man den Vorgänger als EntryPoint der Component
			}else{
				for(AbstractBPMNElement parent : processDecompositionTree.getParents(queryMonitor.getQuery().getMonitoredElements())){
					if(parent instanceof Component){
						Component parentComponent = (Component) parent;
						predecessors.add(parentComponent.getEntryPoint());
					}
				}
			}
			predecessors.retainAll(orderedQueryMonitor.getQuery().getMonitoredElements());
			if(!predecessors.isEmpty()){
				return orderedQueryMonitor;
			}
		}
		return null;
	}
	
	/**
	 * Searches for an QueryMonitor from the list of orderedQueryMonitors, that could be the successor of the specified QueryMonitor.
	 * @param queryMonitor
	 * @param orderedQueryMonitors
	 * @return
	 */
	private QueryMonitor searchSuccessor(QueryMonitor queryMonitor, List<QueryMonitor> orderedQueryMonitors) {
		Set<AbstractBPMNElement> successors;
		for(QueryMonitor orderedQueryMonitor : orderedQueryMonitors){
			successors = new HashSet<AbstractBPMNElement>();
			if(queryMonitor.getQuery().getPatternQueryType().equals(PatternQueryType.STATETRANSITION)){
				for(AbstractBPMNElement monitoredElement : queryMonitor.getQuery().getMonitoredElements()){
					successors.addAll(monitoredElement.getSuccessors());
				}
			//Wenn es keine StateTransition ist, beobacht die Query eine Component, also bekommt man den Nachfolger als ExitPoint der Component
			}else{
				for(AbstractBPMNElement parent : processDecompositionTree.getParents(queryMonitor.getQuery().getMonitoredElements())){
					if(parent instanceof Component){
						Component parentComponent = (Component) parent;
						successors.add(parentComponent.getExitPoint());
					}
				}
			}
			successors.retainAll(orderedQueryMonitor.getQuery().getMonitoredElements());
			if(!successors.isEmpty()){
				return orderedQueryMonitor;
			}
		}
		return null;
	}

	/**
	 * The method searches for exclusiveness violations between several pathes in a XOR component. If a second execution path 
	 * is monitored for a XOR component, it will be treated as a exclusiveness-violation.
	 */
	private void searchForExclusivenessViolations() {
		for(QueryMonitor queryMonitor : processInstanceMonitor.getQueryMonitorsWithQueryType(PatternQueryType.XOR)){
			List<QueryMonitor> subQueryMonitors = processInstanceMonitor.getSubQueryMonitors(queryMonitor);
			if(!subQueryMonitors.contains(null) && !queryMonitor.isInLoop()){
				int subQueriesFinished = 0;
				for(QueryMonitor subQueryMonitor : subQueryMonitors){
					if(subQueryMonitor.isFinished()){
						subQueriesFinished++;
					}
				}
				if(subQueriesFinished > 1){
					for(QueryMonitor subQueryMonitor : subQueryMonitors){
						subQueryMonitor.addViolationStatus(ViolationStatus.Exclusiveness);
					}
				}
			}
		}
	}
	
	/**
	 * This method searches for cooccurence-Violations. If a process instance reaches the last monitorable element
	 * and some elements are still running, these elements are missing and are marked with this violation status.
	 */
	private void searchForOccurenceViolations() {
		// Misssing kann sich nur auf StateTransitions beziehen?
		//Ohne Schleife: Falls Element noch Running ist --> Missing
		AbstractBPMNElement endEvent = null;
		for (AbstractBPMNElement element : processDecompositionTree.getLeafElements()) {
			if(element instanceof BPMNEndEvent) {
				endEvent = element;
			}
		}
		if(endEvent != null && processDecompositionTree != null){
			AbstractBPMNElement lastMonitorableElement = getNearestMonitorablePredecessor(endEvent, processDecompositionTree.getLeafElements());
			Set<QueryMonitor> lastElementQueryMonitors = processInstanceMonitor.getQueryMonitorsWithMonitoredElements(Arrays.asList(lastMonitorableElement));
			if(!lastElementQueryMonitors.isEmpty()){
				QueryMonitor lastElementQueryMonitor = lastElementQueryMonitors.iterator().next();
				if(lastElementQueryMonitor.isTerminated()){
					//Ohne Schleife
					for(QueryMonitor runningQueryMonitor : processInstanceMonitor.getQueryMonitorsWithStatus(QueryStatus.Started)){
						//StateTransitions 
						if(runningQueryMonitor.getQuery().getPatternQueryType().equals(PatternQueryType.STATETRANSITION)){
							runningQueryMonitor.addViolationStatus(ViolationStatus.Missing);
							runningQueryMonitor.setQueryStatus(QueryStatus.Aborted);
							abortParentQueries(runningQueryMonitor);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Searches for cooccurence and exclusiveness violations in loop components. 
	 * A more accurate distinction is not possible for the monitoring of loop components.
	 */
	private void searchForLoopViolations() {
		//LoopComponents untersuchen: Wenn ExecutionCount unterschiedlich in einer LoopComponent, dann LoopViolation für LoopComponent
		//außer SubComponent ist wieder eine Loop
		for(QueryMonitor queryMonitor : processInstanceMonitor.getQueryMonitorsWithQueryType(PatternQueryType.LOOP)){
			int loopExecutionCount = queryMonitor.getExecutionCount();
			List<QueryMonitor> subQueryMonitors = processInstanceMonitor.getSubQueryMonitors(queryMonitor);
			if(!subQueryMonitors.contains(null)){
				for(QueryMonitor subQueryMonitor : subQueryMonitors){
					if(!subQueryMonitor.getQuery().getPatternQueryType().equals(PatternQueryType.LOOP)){
						if(subQueryMonitor.getExecutionCount() != loopExecutionCount){
							queryMonitor.addViolationStatus(ViolationStatus.Loop);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Aborts recursevily all parent queries for the specified query, that are running.
	 * @param queryMonitor
	 */
	private void abortParentQueries(QueryMonitor queryMonitor) {
		SushiPatternQuery parentQuery = queryMonitor.getQuery().getParentQuery();
		if(parentQuery != null){
			QueryMonitor parentQueryMonitor = processInstanceMonitor.getQueryMonitorForQuery(parentQuery);
			if(parentQueryMonitor != null){
				if(parentQueryMonitor.isRunning()){
					parentQueryMonitor.setQueryStatus(QueryStatus.Aborted);
				}
				abortParentQueries(parentQueryMonitor);
			}
		}
	}

	private AbstractBPMNElement getNearestMonitorablePredecessor(AbstractBPMNElement sourceElement, Set<AbstractBPMNElement> considerableElements) {
		if(sourceElement.hasMonitoringPointsWithEventType() && !(sourceElement instanceof Component)){
			return sourceElement;
		}
		//Map aufbauen mit möglichen Elementen und Abstand/Rekursionstiefe zum Startelement
		Map<AbstractBPMNElement, Integer> elements = new HashMap<AbstractBPMNElement, Integer>();
		Set<AbstractBPMNElement> visitedElements = new HashSet<AbstractBPMNElement>();
		visitedElements.add(sourceElement);
		getMonitorablePredecessors(sourceElement.getPredecessors(), elements, considerableElements, 1, visitedElements);
		//Nähstes Element ermitteln
		int minDepth = Integer.MAX_VALUE;
		AbstractBPMNElement nearestElement = null;
		for(AbstractBPMNElement element : elements.keySet()){
			if(elements.get(element) < minDepth){
				minDepth = elements.get(element);
				nearestElement = element;
			}
		}
		return nearestElement;
	}

	private void getMonitorablePredecessors(Set<AbstractBPMNElement> predecessors, Map<AbstractBPMNElement, Integer> elements, Set<AbstractBPMNElement> considerableElements, int depth, Set<AbstractBPMNElement> visitedElements) {
		for(AbstractBPMNElement predecessor : predecessors){
			if(considerableElements.contains(predecessor) && !visitedElements.contains(predecessor)){
				visitedElements.add(predecessor);
				if(predecessor.hasMonitoringPointsWithEventType() && !(predecessor instanceof Component)){
					elements.put(predecessor, depth);
				}
				getMonitorablePredecessors(predecessor.getPredecessors(), elements, considerableElements, ++depth, visitedElements);
			}
		}
	}

	public ProcessInstanceMonitor getProcessInstanceMonitor() {
		return processInstanceMonitor;
	}

	public void setProcessInstanceMonitor(ProcessInstanceMonitor processInstanceMonitor) {
		this.processInstanceMonitor = processInstanceMonitor;
	}

}
