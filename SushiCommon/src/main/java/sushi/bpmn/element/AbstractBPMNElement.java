package sushi.bpmn.element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jbpt.hypergraph.abs.IGObject;
import org.jbpt.hypergraph.abs.IVertex;

import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.persistence.Persistable;

/**
 * This class is a logical representation for a BPMN element.
 * @author micha
 */
@Entity
@Table(name = "BPMNElement")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AbstractBPMNElement extends Persistable implements IVertex {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	protected int ID;
	
	@Column(name = "BPMN_ID")
	private String BPMN_ID;
	
	@Column(name = "Name")
	private String name;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="BPMNElement_Predecessors")
	private Set<AbstractBPMNElement> predecessors = new HashSet<AbstractBPMNElement>();
	 
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="BPMNElement_Successors")
	private Set<AbstractBPMNElement> successors = new HashSet<AbstractBPMNElement>();
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="BPMNElement_MonitoringPoints")
	private List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
	
	
	public AbstractBPMNElement() {
		this.ID = 0;
		this.name = "";
	}
	 
	public AbstractBPMNElement(String ID, String name) {
		this.BPMN_ID = ID;
		this.name = name;
	}
	
	public AbstractBPMNElement(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		this.BPMN_ID = ID;
		this.name = name;
		if(monitoringPoints != null){
			this.monitoringPoints = monitoringPoints;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AbstractBPMNElement> getPredecessors() {
		return predecessors;
	}
	
	/**
	 * Returns all elements, that are predecessor of the current element and its predecessor (all indirect predecessors).
	 * @return
	 */
	public Set<AbstractBPMNElement> getIndirectPredecessors(){
		Set<AbstractBPMNElement> elements = new HashSet<AbstractBPMNElement>();
		elements = getIndirectPredecessorsWithSelf(this, elements);
		return elements;
	}
	
	private Set<AbstractBPMNElement> getIndirectPredecessorsWithSelf(AbstractBPMNElement element, Set<AbstractBPMNElement> elements){
		if(element instanceof BPMNStartEvent){
			if(!elements.contains(element)){
				elements.add(element);
			}
		} else {
			elements.add(element);
			for(AbstractBPMNElement predecessor : element.getPredecessors()){
				if(!elements.contains(predecessor)){
					elements.addAll(getIndirectPredecessorsWithSelf(predecessor, elements));
				}
			}
		}
		return elements;
	}
	
	/**
	 * Returns all elements, that are on a path starting from the current element (all indirect successors).
	 * @return
	 */
	public Set<AbstractBPMNElement> getIndirectSuccessors(){
		Set<AbstractBPMNElement> elements = new HashSet<AbstractBPMNElement>();
		for(AbstractBPMNElement element : this.getSuccessors()){
			elements.addAll(getIndirectSuccessors(element, elements));
		}
		return elements;
	}
	
	/**
	 * Proofs, if the given element is a indirect successor of the current element.
	 * @param element
	 * @return
	 */
	public boolean isIndirectSuccessor(AbstractBPMNElement element){
		if(!this.equals(element)){
			return this.getIndirectSuccessors().contains(element);
		}
		return false;
	}
	
	private Set<AbstractBPMNElement> getIndirectSuccessors(AbstractBPMNElement element, Set<AbstractBPMNElement> elements){
		if(element instanceof BPMNEndEvent){
			if(!elements.contains(element)){
				elements.add(element);
			}
		} else {
			elements.add(element);
			for(AbstractBPMNElement successor : element.getSuccessors()){
				if(!elements.contains(successor)){
					elements.add(successor);
					elements.addAll(getIndirectSuccessors(successor, elements));
				}
			}
		}
		return elements;
	}

	public void setPredecessor(Set<AbstractBPMNElement> predecessor) {
		this.predecessors = predecessor;
	}

	/**
	 * Returns the direct successors.
	 * @return
	 */
	public Set<AbstractBPMNElement> getSuccessors() {
		return successors;
	}

	public void setSuccessors(Set<AbstractBPMNElement> successors) {
		this.successors = successors;
	}
	
	/**
	 * Adds an element as a predecessor for the current element.
	 * @param element
	 */
	public void addPredecessor(AbstractBPMNElement element) {
		if(!predecessors.contains(element)){
			this.predecessors.add(element);
		}
	}

	public void removePredecessor(AbstractBPMNElement element) {
		this.predecessors.remove(element);
	}
	
	public void removeAllPredecessors() {
		this.predecessors = new HashSet<AbstractBPMNElement>();
	}
	
	/**
	 * Proofs, if this element has any predecessors.
	 * @return
	 */
	public boolean hasPredecessor() {
		return (! this.predecessors.isEmpty());
	}

	/**
	 * Proofs, if this element has any successors.
	 * @return
	 */
	public boolean hasSuccessors() {
		return (! this.successors.isEmpty());
	}
	
	public boolean isSequenceFlow() {
		return false;
	}
	
	public boolean isBoundaryEvent() {
		return false;
	}
	
	public boolean isProcess() {
		return false;
	}
	
	/**
	 * Adds an element as a successor for the current element.
	 * @param element
	 */
	public void addSuccessor(AbstractBPMNElement element) {
		if(!successors.contains(element)){
			this.successors.add(element);
		}
	}

	public void removeSuccessor(AbstractBPMNElement element) {
		this.successors.remove(element);
	}
	
	public void removeAllSuccessors() {
		this.successors = new HashSet<AbstractBPMNElement>();
	}

	public String getBPMN_ID() {
		return BPMN_ID;
	}

	public void setBPMN_ID(String id) {
		this.BPMN_ID = id;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int id) {
		this.ID = id;
	}
	
	public List<MonitoringPoint> getMonitoringPoints() {
		return monitoringPoints;
	}
	
	/**
	 * Returns the monitoring point with the given {@link MonitoringPointStateTransition} or null.
	 * @param transitionType
	 * @return
	 */
	public MonitoringPoint getMonitoringPointByStateTransitionType(MonitoringPointStateTransition transitionType){
		for(MonitoringPoint monitoringPoint : monitoringPoints){
			if(monitoringPoint.getStateTransitionType().equals(transitionType)){
				return monitoringPoint;
			}
		}
		return null;
	}
	
	public void addMonitoringPoint(MonitoringPoint monitoringPoint){
		if(getMonitoringPointByStateTransitionType(monitoringPoint.getStateTransitionType()) != null){
			monitoringPoints.remove(getMonitoringPointByStateTransitionType(monitoringPoint.getStateTransitionType()));
		} 
		monitoringPoints.add(monitoringPoint);
	}
	
	public void removeMonitoringPoint(MonitoringPoint monitoringPoint){
		monitoringPoints.remove(monitoringPoint);
	}
	
	public void setMonitoringPoints(List<MonitoringPoint> monitoringPoints) {
		this.monitoringPoints = monitoringPoints;
	}
	
	public boolean hasMonitoringPoints() {
		return monitoringPoints != null && !monitoringPoints.isEmpty();
	}
	
	/**
	 * Returns true, if an element has monitoring points and if the monitoring points have an assigned event type. 
	 * @return
	 */
	public boolean hasMonitoringPointsWithEventType() {
		if(hasMonitoringPoints()){
			boolean monitoringPointWithEventType = false;
			for(MonitoringPoint monitoringPoint : monitoringPoints){
				if(monitoringPoint.getEventType() != null){
					monitoringPointWithEventType = true;
				}
			}
			return monitoringPointWithEventType;
		}
		return false;
	}
	
	public String toString(){
		return (this.name.isEmpty()) ? this.getClass().getSimpleName() : this.getClass().getSimpleName() + ": " + this.name;
	}

	public AbstractBPMNElement clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (AbstractBPMNElement) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public String getId() {
		return BPMN_ID;
	}

	public void setId(String id) {
		this.BPMN_ID = id;
	}
	
	/**
	 * Returns all elements on a path between the given start and end element. 
	 * Their must exist a path between these elements.
	 * @param startElement - excluded
	 * @param endElement - excluded
	 * @return
	 */
	public static Set<AbstractBPMNElement> getElementsOnPathBetween(AbstractBPMNElement startElement, AbstractBPMNElement endElement){
		Set<AbstractBPMNElement> elements = new HashSet<AbstractBPMNElement>();
		if(startElement.equals(endElement)){
			return elements;
		}
		elements.add(startElement);
		
		for(AbstractBPMNElement successor : startElement.getSuccessors()){
			if(successor.equals(endElement)){
				elements.add(successor);
				return elements;
			}
			if(successor.getIndirectSuccessors().contains(endElement) && !elements.contains(successor)){
				elements.add(successor);
				elements.addAll(getElementsOnPathBetweenWithStartAndEnd(successor, endElement));
			}
		}
		elements.remove(startElement);
		elements.remove(endElement);
		return elements;
	}
	
	private static Set<AbstractBPMNElement> getElementsOnPathBetweenWithStartAndEnd(AbstractBPMNElement startElement, AbstractBPMNElement endElement){
		Set<AbstractBPMNElement> elements = new HashSet<AbstractBPMNElement>();
		elements.add(startElement);
		for(AbstractBPMNElement successor : startElement.getSuccessors()){
			if(successor.equals(endElement)){
				elements.add(successor);
				return elements;
			}
			if(successor.getIndirectSuccessors().contains(endElement) && !elements.contains(successor)){
				elements.add(successor);
				elements.addAll(getElementsOnPathBetweenWithStartAndEnd(successor, endElement));
			}
		}
		return elements;
	}
	
	/**
	 * Returns all elements, that belong to the shortest path between the given start and end element. 
	 * @param sourceElement
	 * @param destinationElement
	 * @return
	 */
	public static List<AbstractBPMNElement> getShortestPathBetween(AbstractBPMNElement sourceElement, AbstractBPMNElement destinationElement){
		Map<AbstractBPMNElement, Boolean> visitedElements = new HashMap<AbstractBPMNElement, Boolean>();
		Map<AbstractBPMNElement, AbstractBPMNElement> previousElements = new HashMap<AbstractBPMNElement, AbstractBPMNElement>();
		
	    List<AbstractBPMNElement> directions = new LinkedList<AbstractBPMNElement>();
	    Queue<AbstractBPMNElement> queue = new LinkedList<AbstractBPMNElement>();
	    AbstractBPMNElement current = sourceElement;
	    queue.add(current);
	    visitedElements.put(current, true);
	    while(!queue.isEmpty()){
	        current = queue.remove();
	        if (current.equals(destinationElement)){
	            break;
	        }else{
	            for(AbstractBPMNElement node : current.getSuccessors()){
	                if(!visitedElements.containsKey(node)){
	                    queue.add(node);
	                    visitedElements.put(node, true);
	                    previousElements.put(node, current);
	                }
	            }
	        }
	    }
	    if (!current.equals(destinationElement)){
	        System.out.println("can't reach destination");
	    }
	    for(AbstractBPMNElement node = destinationElement; node != null; node = previousElements.get(node)) {
	        directions.add(node);
	    }
	    Collections.reverse(directions);
		return directions;
	}
	
	@Override
	public int getX() {
		return 0;
	}

	@Override
	public void setX(int x) {
		
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public void setY(int y) {
		
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public void setWidth(int w) {
		
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setHeight(int h) {
		
	}

	@Override
	public void setLocation(int x, int y) {
		
	}

	@Override
	public void setSize(int w, int h) {
		
	}

	@Override
	public void setLayout(int x, int y, int w, int h) {
		
	}

	@Override
	public Object getTag() {
		return null;
	}

	@Override
	public void setTag(Object tag) {
		
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String desc) {
		
	}

	@Override
	public int compareTo(IGObject o) {
		return 0;
	}

	@Override
	public String getLabel() {
		return null;
	}
	
	/**
	 * Connects the two given elements as predecessor and successor.
	 * @param predecessor
	 * @param successor
	 */
	public static void connectElements(AbstractBPMNElement predecessor, AbstractBPMNElement successor){
		if (predecessor != null && successor != null) {
			predecessor.addSuccessor(successor);
			successor.addPredecessor(predecessor);
		}
	}
	
	/**
	 * Disconnects the two given elements as predecessor and successor.
	 * @param predecessor
	 * @param successor
	 */
	public static void disconnectElements(AbstractBPMNElement predecessor, AbstractBPMNElement successor){
		if (predecessor != null && successor != null) {
			predecessor.removeSuccessor(successor);
			successor.removePredecessor(predecessor);
		}
	}
	
	/**
	 * Returns all the elements, that have monitoring points.
	 * @param elements
	 * @return
	 */
	public static List<AbstractBPMNElement> getElementsWithMonitoringPoints(List<AbstractBPMNElement> elements) {
		Set<AbstractBPMNElement> monitorableElements = new HashSet<AbstractBPMNElement>();
		for(AbstractBPMNElement element : elements){
			if(element.hasMonitoringPoints()){
				monitorableElements.add(element);
			}
		}
		return new ArrayList<AbstractBPMNElement>(monitorableElements);
	}
	
	
}
