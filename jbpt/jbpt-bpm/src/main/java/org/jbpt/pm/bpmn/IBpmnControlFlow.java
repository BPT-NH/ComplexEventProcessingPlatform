/**
 * 
 */
package org.jbpt.pm.bpmn;

import java.util.Collection;

import org.jbpt.pm.IControlFlow;
import org.jbpt.pm.IDataNode;
import org.jbpt.pm.IFlowNode;


/**
 * Interface for BPMN Control Flow.
 * @author Cindy Fähnrich, Tobias Hoppe
 *
 */
public interface IBpmnControlFlow<V extends IFlowNode> extends IControlFlow<V> {
	
	/**
	 * Attaches a BPMN event to this control flow.
	 * @param event
	 */
	public void attachEvent(BpmnEvent event);
	
	/**
	 * Detaches the current BPMN event from this control flow and return it.
	 */
	public BpmnEvent detachEvent();
	
	/**
	 * @return <code>true</code> if this {@link BpmnControlFlow} edge has an attached event.
	 * <code>false</code> otherwise.
	 */
	public boolean hasAttachedEvent();
	
	/**
	 * @return the {@link BpmnEvent} attached to this {@link BpmnControlFlow} edge.
	 * Returns <code>null</code> if non exists.
	 */
	public BpmnEvent getAttachedEvent();
	
	/**
	 * Sets the control flow's condition (it will then turn into a conditional control flow).
	 * @param condition
	 */
	public void setCondition(String condition);
	
	/**
	 * @return the control flow's condition
	 */
	public String getCondition();
	
	/**
	 * Checks whether this is a conditional control flow.
	 */
	public boolean hasCondition();
	
	/**
	 * Checks whether this is a default sequence flow.
	 * @return
	 */
	public boolean isDefault();
	
	/**
	 * Sets the control flow as default flow.
	 */
	public void setDefault();
	
	/**
	 * @return a {@link Collection} of all {@link IDataNode}s, that are read by this {@link IFlowNode}.
	 */
	Collection<IDataNode> getReadDocuments();
	
	/**
	 * @return a {@link Collection} of all {@link IDataNode}s, that are written by this {@link IFlowNode}.
	 */
	Collection<IDataNode> getWriteDocuments();
	
	/**
	 * @return a {@link Collection} of all {@link IDataNode}s, that are read and written by this {@link IFlowNode}.
	 */
	Collection<IDataNode> getReadWriteDocuments();
	
	/**
	 * @return a {@link Collection} of all {@link IDataNode}s, where read or write access is not specified.
	 */
	Collection<IDataNode> getUnspecifiedDocuments();
	
	/**
	 * adds a {@link IDataNode} that is read by this {@link IFlowNode}.
	 * @param document to add
	 */
	void addReadDocument(IDataNode document);
	
	/**
	 * adds a {@link IDataNode} that is written by this {@link IFlowNode}.
	 * @param document to add
	 */
	void addWriteDocument(IDataNode document);
	
	/**
	 * adds a {@link IDataNode} that is read and written by this {@link IFlowNode}.
	 * @param document to add
	 */
	void addReadWriteDocument(IDataNode document);
	
	/**
	 * adds a {@link IDataNode} where the access is not specified.
	 * @param document to add
	 */
	void addUnspecifiedDocument(IDataNode document);

}
