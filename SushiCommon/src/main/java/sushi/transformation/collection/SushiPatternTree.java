package sushi.transformation.collection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.collection.SushiTreeElement;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.transformation.TransformationRule;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.FilterExpressionConnectorElement;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.PatternOperatorElement;

/**
 * Container object for the pattern elements of a transformation rule.
 * One pattern tree per transformation rule.
 */
@Entity
@Table(name = "SushiPatternTree")
public class SushiPatternTree extends Persistable {

	private static final long serialVersionUID = 4641263893746532464L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SushiPatternTreeID")
	protected int ID;
	
	@Column(name="Auxiliary")
	private String auxiliary = "Auxiliary";
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "patternTree")
	private TransformationRule transformationRule;
	
//	@OneToMany(cascade = CascadeType.PERSIST)
//	@JoinTable(name="SushiTreeElementTree_SushiTreeElements")
//	private List<SushiTreeElement<Serializable>> elements = new ArrayList<SushiTreeElement<Serializable>>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="patternTreeID", referencedColumnName="SushiPatternTreeID")
	private List<SushiTreeElement<Serializable>> elements = new ArrayList<SushiTreeElement<Serializable>>();
	
	public SushiPatternTree() {
		this.ID = 0;
	}
	
	public SushiPatternTree(SushiTreeElement<Serializable> element) {
		this();
		assert(element != null);
		this.elements.add(element);
	}
	
	public SushiPatternTree(List<SushiTreeElement<Serializable>> elements) {
		this();
		assert(elements != null);
		assert(!elements.isEmpty());
		this.elements.addAll(elements);
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Method to retrieve all elements of the pattern tree.
	 * @return list of elements
	 */
	public List<SushiTreeElement<Serializable>> getElements() {
		return elements;
	}
	
	/**
	 * Method to retrieve all elements without parents.
	 * @return list of root elements in an adequate order
	 */
	public List<SushiTreeElement<Serializable>> getRoots() {
		List<SushiTreeElement<Serializable>> rootElements = new ArrayList<SushiTreeElement<Serializable>>();
		for (SushiTreeElement<Serializable> currentElement : elements) {
			if (!currentElement.hasParent()) {
				rootElements.add(currentElement);
			}
		}
		return rootElements;
	}
	
	/**
	 * Method to retrieve all elements without children.
	 * @return list of root elements in an adequate order
	 */
	public List<SushiTreeElement<Serializable>> getLeafs() {
		List<SushiTreeElement<Serializable>> leafElements = new ArrayList<SushiTreeElement<Serializable>>();
		for (SushiTreeElement<Serializable> currentElement : elements) {
			if (!currentElement.hasChildren()) {
				leafElements.add(currentElement);
			}
		}
		return leafElements;
	}
	
	/** 
	 * Method to retrieve all pattern operator elements of the pattern tree.
	 * @return list of pattern operator elements
	 */
	public List<PatternOperatorElement> getPatternOperatorElements() {
		List<PatternOperatorElement> patternOperatorElements = new ArrayList<PatternOperatorElement>();
		for (SushiTreeElement<Serializable> element : elements) {
			if (element instanceof PatternOperatorElement) {
				patternOperatorElements.add((PatternOperatorElement) element);
			}
		}
		return patternOperatorElements;
	}
	
	/** 
	 * Method to retrieve all event type elements of the pattern tree.
	 * @return list of event type elements
	 */
	public List<EventTypeElement> getEventTypeElements() {
		List<EventTypeElement> eventTypeElements = new ArrayList<EventTypeElement>();
		for (SushiTreeElement<Serializable> element : elements) {
			if (element instanceof EventTypeElement) {
				eventTypeElements.add((EventTypeElement) element);
			}
		}
		return eventTypeElements;
	}
	
	/** 
	 * Method to retrieve all filter expression elements of the pattern tree.
	 * @return list of filter expression elements
	 */
	public List<FilterExpressionElement> getFilterExpressionElements() {
		List<FilterExpressionElement> filterExpressionElements = new ArrayList<FilterExpressionElement>();
		for (SushiTreeElement<Serializable> element : elements) {
			if (element instanceof FilterExpressionElement) {
				filterExpressionElements.add((FilterExpressionElement) element);
			}
		}
		return filterExpressionElements;
	}

	public boolean addElement(SushiTreeElement<Serializable> element) {
		return elements.add(element);
	}
	
	public boolean addElements(List<SushiTreeElement<Serializable>> elements) {
		return elements.addAll(elements);
	}
	
	/**
	 * Removes the given element from the tree.
	 * If an element has parent and child elements, the child elements will be connected to the parent elements.
	 * 
	 * @param element the element to be removed
	 * @return true if removal was successful
	 */
	public boolean removeElement(SushiTreeElement<Serializable> element) {
			if (element instanceof FilterExpressionElement) {
				SushiTreeElement<Serializable> parentElement = element.getParent();
				parentElement.removeChild(element);
			} else if (element instanceof EventTypeElement) {
				List<SushiTreeElement<Serializable>> allChildrenOfElement = getAllChildrenFromElement(element);
				for (SushiTreeElement<Serializable> child : allChildrenOfElement) {
					child.removeElement();
					elements.remove(child);
				}
				List<SushiTreeElement<Serializable>> allParentsOfElement = getAllParentsFromElement(element);
				for (SushiTreeElement<Serializable> parent : allParentsOfElement) {
					parent.removeElement();
					elements.remove(parent);
				}
				element.removeElement();
			} else if (element instanceof PatternOperatorElement || element instanceof FilterExpressionConnectorElement) {
				for (SushiTreeElement<Serializable> childElement : element.getChildren()) {
					if (element.hasParent()) {
						SushiTreeElement<Serializable> parentElement = element.getParent();
						childElement.setParent(parentElement);
						parentElement.removeChild(element);
					} else {
						childElement.setParent(null);
					}
				}
			}
		return elements.remove(element);
	}
	
	/**
	 * Method to retrieve all elements from lower levels that are referenced directly or indirectly by the given element.
	 * 
	 * @param element the element from which all lower level elements shall be found
	 * @return list of elements from lower levels that are referenced directly or indirectly by the given element
	 */
	private List<SushiTreeElement<Serializable>> getAllChildrenFromElement(SushiTreeElement<Serializable> element) {
		List<SushiTreeElement<Serializable>> allChildren = new ArrayList<SushiTreeElement<Serializable>>();
		for (SushiTreeElement<Serializable> child : element.getChildren()) {
			allChildren.add(child);
			if (child.hasChildren()) {
				for (SushiTreeElement<Serializable> childOfChild : child.getChildren()) {
					getAllChildrenFromChild(allChildren, childOfChild);
				}
			}
		}
		return allChildren;
	}

	private void getAllChildrenFromChild(List<SushiTreeElement<Serializable>> allChildren, SushiTreeElement<Serializable> element) {
		allChildren.add(element);
		if (element.hasChildren()) {
			for (SushiTreeElement<Serializable> child : element.getChildren()) {
				getAllChildrenFromChild(allChildren, child);
			}
		}
	}
	
	/**
	 * Method to retrieve all elements from higher levels that are referenced directly or indirectly by the given element.
	 * 
	 * @param element the element from which all higher shall be found
	 * @return list of elements from higher levels that are referenced directly or indirectly by the given element
	 */
	private List<SushiTreeElement<Serializable>> getAllParentsFromElement(SushiTreeElement<Serializable> element) {
		List<SushiTreeElement<Serializable>> allParents = new ArrayList<SushiTreeElement<Serializable>>();
		if (element.hasParent()) {
			SushiTreeElement<Serializable> parent = element.getParent();
			allParents.add(parent);
			if (parent.hasParent()) {
				getAllParentsFromParent(allParents, parent.getParent());
			}
		}
		return allParents;
	}
	
	private void getAllParentsFromParent(List<SushiTreeElement<Serializable>> allParents, SushiTreeElement<Serializable> element) {
		allParents.add(element);
		if (element.hasParent()) {
			getAllChildrenFromChild(allParents, element.getParent());
		}
	}

	public static List<SushiPatternTree> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiPatternTree t");
		return q.getResultList();
	}
	
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiPatternTree");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public SushiPatternTree clone() {
		return deepClone();
	}
	
	private SushiPatternTree deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (SushiPatternTree) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return printTreeLevel(getRoots(), 0);
	}
	
	private String printTreeLevel(List<SushiTreeElement<Serializable>> elements, int count) {
		String tree = "";
		for (SushiTreeElement<Serializable> element : elements) {
			for (int i = 0; i < count; i++) {
				tree += "\t";
			}
			tree += element + System.getProperty("line.separator");
			if (element.hasChildren()) {
				tree += printTreeLevel(element.getChildren(), count + 1);
			}
		}
		return tree;
	}	
}
