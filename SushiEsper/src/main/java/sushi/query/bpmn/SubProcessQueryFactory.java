package sushi.query.bpmn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.decomposition.SubProcessComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.query.SushiPatternQuery;


/**
 * @author micha
 */
public class SubProcessQueryFactory extends AbstractPatternQueryFactory {

	public SubProcessQueryFactory(PatternQueryGenerator patternQueryGenerator) {
		super(patternQueryGenerator);
	}

	@Override
	protected SushiPatternQuery generateQuery(AbstractBPMNElement element, AbstractBPMNElement catchingMonitorableElement, SushiPatternQuery parentQuery) {
		if(element instanceof Component){
			Component component = (Component) element;
			SushiPatternQuery query = null;
			if(component instanceof SubProcessComponent){
				SubProcessComponent subProcessComponent = (SubProcessComponent) component;
				
				List<AbstractBPMNElement> catchingMonitorableElements = getCatchingMonitorableElement(subProcessComponent);
				
				AbstractBPMNElement subProcessCatchingMonitorableElement = null;
				if(!catchingMonitorableElements.isEmpty()){
					subProcessCatchingMonitorableElement = catchingMonitorableElements.get(0);
				}
				
				//Sollte nur ein Element (Polygon-Component) sein, wenn SubProcess wohlstrukturiert ist
				List<AbstractBPMNElement> subProcessChildren = processDecompositionTree.getChildren(subProcessComponent);
				
				if(subProcessChildren.size() == 1 && subProcessChildren.get(0) instanceof Component){
					query = new PatternQueryFactory(patternQueryGenerator).generateQuery(subProcessChildren.get(0), subProcessCatchingMonitorableElement, parentQuery);
				} else {
					throw new RuntimeException("SubProcess is not well structured.");
				}
				
			}
			
			return query;
		} else {
			System.err.println("Input element should be a component for an OR-query!");
			return null;
		}
	}
	
	/**
	 * Tries to find the first monitorable element on a path after a subprocess starting with its catching intermediate event.
	 * @param component
	 * @return
	 */
	private List<AbstractBPMNElement> getCatchingMonitorableElement(SubProcessComponent component) {
		Set<AbstractBPMNElement> successingMonitorableElements = new HashSet<AbstractBPMNElement>();
		Set<AbstractBPMNElement> visitedElements = new HashSet<AbstractBPMNElement>();
		AbstractBPMNElement attachedEvent = component.getSubProcess().getAttachedIntermediateEvent();
		if(attachedEvent != null){
			traverseSuccessingMonitorableElements(attachedEvent, visitedElements, successingMonitorableElements);
		}
		return new ArrayList<AbstractBPMNElement>(successingMonitorableElements);
	}


}
