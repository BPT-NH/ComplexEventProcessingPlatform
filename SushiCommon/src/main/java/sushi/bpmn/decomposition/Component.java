package sushi.bpmn.decomposition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AbstractBPMNGateway;

/**
 * This class is a container for {@link AbstractBPMNElement}s and results from a process decomposition with the RPST.
 * A {@link Component} is created for each canonical fragment of the RPST.
 * @author micha
 */
public class Component extends AbstractBPMNElement {
	
	private static final long serialVersionUID = 1L;
	
	protected IPattern type;

	/**
	 * Single entry point to the component, not included in the components children.
	 */
	protected AbstractBPMNElement entryPoint;
	
	/**
	 * Single exit point to the component, not included in the components children.
	 */
	protected AbstractBPMNElement exitPoint;
	
	/**
	 * Entry element to the component, that belongs to its elements and is the successor of the entry point.
	 */
	protected AbstractBPMNElement sourceElement;
	
	/**
	 * Exit element of the component, that belongs to its elements and is the predecessor of the exit point.
	 */
	protected AbstractBPMNElement sinkElement;
	
	protected Set<AbstractBPMNElement> children;
	
	public Component(){
		this.children = new HashSet<AbstractBPMNElement>();
	}
	
	public Component(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement){
		this.entryPoint = entryPoint;
		this.sourceElement = sourceElement;
		this.children = new HashSet<AbstractBPMNElement>(Arrays.asList(sourceElement));
	}
	
	/**
	 * Creates a new component as a container.
	 * @param entryPoint - not included
	 * @param sourceElement - included
	 * @param exitPoint - not included
	 * @param sinkElement - included
	 */
	public Component(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement){
		this.entryPoint = entryPoint;
		this.sourceElement = sourceElement;
		this.children = new HashSet<AbstractBPMNElement>(Arrays.asList(sourceElement));
		this.exitPoint = exitPoint;
		this.sinkElement = sinkElement;
		this.children.add(sinkElement);
	}
	
	/**
	 * Returns the single entry point to the component, not included in the components children.
	 */
	public AbstractBPMNElement getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(AbstractBPMNElement entryPoint) {
		this.entryPoint = entryPoint;
	}

	public AbstractBPMNElement getExitPoint() {
		return exitPoint;
	}

	public void setExitPoint(AbstractBPMNElement exitPoint) {
		this.exitPoint = exitPoint;
	}

	public Set<AbstractBPMNElement> getChildren() {
		return children;
	}

	public void setChildren(Set<AbstractBPMNElement> children) {
		this.children = children;
	}
	
	public void addChildren(Collection<AbstractBPMNElement> children) {
		for(AbstractBPMNElement child : children){
			addChild(child);
		}
	}
	
	public void addChild(AbstractBPMNElement child) {
		if(!children.contains(child)){
			this.children.add(child);
		}
	}
	
	public void removeChild(AbstractBPMNElement child) {
		this.children.remove(child);
	}
	
	public void removeChildren(List<AbstractBPMNElement> innerElements) {
		this.children.removeAll(innerElements);
	}
	
	public boolean contains(AbstractBPMNElement element){
		for(AbstractBPMNElement child : children){
			if(child.equals(element)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMonitoringPoints() {
		for(AbstractBPMNElement child : children){
			if(child.hasMonitoringPoints()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMonitoringPointsWithEventType() {
		for(AbstractBPMNElement child : children){
			if(child.hasMonitoringPointsWithEventType()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the source element of the component, that belongs to its elements and is the successor of the entry point.
	 */
	public AbstractBPMNElement getSourceElement() {
		return sourceElement;
	}

	public void setSourceElement(AbstractBPMNElement sourceElement) {
		this.sourceElement = sourceElement;
	}

	/**
	 * Returns the sink element of the component, that belongs to its elements and is the predecessor of the exit point.
	 */
	public AbstractBPMNElement getSinkElement() {
		return sinkElement;
	}

	public void setSinkElement(AbstractBPMNElement sinkElement) {
		this.sinkElement = sinkElement;
	}
	
	public boolean includesGateways(){
		for(AbstractBPMNElement child : children){
			if(child instanceof AbstractBPMNGateway) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks, if a component contains gateways, that are not the source or sink element
	 * @return
	 */
	public boolean includesInnerGateways() {
		for(AbstractBPMNElement child : children){
			if(child instanceof AbstractBPMNGateway && !(child.equals(sourceElement) || child.equals(sinkElement))) {
				return true;
			}
		}
		return false;
	}
	
	public IPattern getType() {
		return type;
	}

	public void setType(IPattern type) {
		this.type = type;
	}


}
