package sushi.bpmn.element;

/**
 * @author micha
 * This interface should be implemented from BPMN elements that could have attached intermediate events.
 */
public interface AttachableElement {
	
	public boolean hasAttachedIntermediateEvent();
	
	public BPMNBoundaryEvent getAttachedIntermediateEvent();
	
	public void setAttachedIntermediateEvent(BPMNBoundaryEvent attachedEvent);

}
