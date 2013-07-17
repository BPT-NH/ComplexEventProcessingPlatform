package sushi.bpmn.element;

/**
 * This interface should be implemented from BPMN elements that could have attached intermediate events.
 * @author micha
 */
public interface AttachableElement {
	
	public boolean hasAttachedIntermediateEvent();
	
	public BPMNBoundaryEvent getAttachedIntermediateEvent();
	
	public void setAttachedIntermediateEvent(BPMNBoundaryEvent attachedEvent);

}
