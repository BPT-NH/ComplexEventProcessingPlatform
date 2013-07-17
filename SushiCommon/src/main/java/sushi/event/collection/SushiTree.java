package sushi.event.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

/**
 * @author micha
 *
 * @param <T>
 */
@Entity
@Table(name = "SushiTree")
public class SushiTree<T> extends Persistable implements Collection<T> {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SushiTreeID")
	protected int ID;
	
	//essentiell, da sonst keine (leeren) SushiTrees gespeichert werden können
	@Column(name="Auxiliary")
	private String auxiliary = "Auxiliary";
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="SushiTree_SushiTreeElements")
	private List<SushiTreeElement<T>> treeElements = new ArrayList<SushiTreeElement<T>>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="SushiTree_SushiTreeRootElements")
	private List<SushiTreeElement<T>> treeRootElements = new ArrayList<SushiTreeElement<T>>();
	
	public SushiTree() {
		this.ID = 0;
	}
	
	public SushiTree(T rootElementValue){
		assert(rootElementValue != null);
		SushiTreeElement<T> element = new SushiTreeElement<T>(null, rootElementValue);
		treeElements = new ArrayList<SushiTreeElement<T>>();
		treeRootElements = new ArrayList<SushiTreeElement<T>>();
		treeElements.add(element);
		treeRootElements.add(element);
	}
	
	public T getParent(T treeElementValue){
		for(SushiTreeElement<T> currentTreeElement : treeElements){
			if(currentTreeElement.getValue() == treeElementValue){
				return currentTreeElement.getParent().getValue();
			}
		}
		return null;
	}
	
	/**
	 * Returns all parents in the tree for the given element. 
	 * @param element
	 * @return
	 */
	public Set<T> getIndirectParents(T element){
		Set<T> parentValues = new HashSet<T>();
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(element);
		if(currentTreeElement != null){
			parentValues.addAll(getIndirectParents(currentTreeElement));
		}
		return parentValues;
	}
	
	private Set<T> getIndirectParents(SushiTreeElement<T> currentTreeElement) {
		Set<T> parentValues = new HashSet<T>();
		if(currentTreeElement.getParent() != null){
			parentValues.add(currentTreeElement.getParent().getValue());
			parentValues.addAll(getIndirectParents(currentTreeElement.getParent()));
		}
		return parentValues;
	}
	
	/**
	 * Returns all elements from the tree, which contain exactly these children. 
	 * @param element
	 * @return
	 */
	public Set<T> getIndirectParents(Collection<T> children){
		Set<T> parentValues = new HashSet<T>();
		Set<SushiTreeElement<T>> parents = new HashSet<SushiTreeElement<T>>();
		for(SushiTreeElement<T> treeElement : treeElements){
			if(treeElement.getChildValues().containsAll(children) && children.containsAll(treeElement.getChildValues())){
				parents.add(treeElement);
				parentValues.add(treeElement.getValue());
			}
		}
		for(SushiTreeElement<T> parent : parents){
			parentValues.addAll(getIndirectParents(parent));
		}
		return parentValues;
	}
	
	/**
	 * Returns all descendants for the given element. 
	 * @param element
	 * @return
	 */
	public Set<T> getIndirectChildren(T element){
		Set<T> childrenValues = new HashSet<T>();
		Set<SushiTreeElement<T>> children = new HashSet<SushiTreeElement<T>>();
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(element);
		if(currentTreeElement != null){
			children.addAll(getIndirectChildren(currentTreeElement));
		}
		for(SushiTreeElement<T> child  : children){
			childrenValues.add(child.getValue());
		}
		return childrenValues;
	}
	
	private Set<SushiTreeElement<T>> getIndirectChildren(SushiTreeElement<T> currentTreeElement) {
		Set<SushiTreeElement<T>> children = new HashSet<SushiTreeElement<T>>();
		for(SushiTreeElement<T> childElement : currentTreeElement.getChildren()){
			children.add(childElement);
			if(childElement.hasChildren()){
				children.addAll(getIndirectChildren(childElement));
			}
		}
		return children;
	}

	public List<T> getChildren(T element){
		List<T> childrenValues = new ArrayList<T>();
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(element);
		if(currentTreeElement != null){
			for(SushiTreeElement<T> childElement : currentTreeElement.getChildren()){
				childrenValues.add(childElement.getValue());
			}
		}
		return childrenValues;
	}
	
	/**
	 * Returns a list of BPMN elements, which are parents for the specified elements.
	 * @param elements
	 * @return
	 */
	public List<T> getParents(Collection<T> elements){
		List<T> parentValues = new ArrayList<T>();
		for(SushiTreeElement<T> treeElement : treeElements){
			List<T> childrenOfElement = this.getChildren(treeElement.getValue());
			if(childrenOfElement.containsAll(elements) && elements.containsAll(childrenOfElement)){
				parentValues.add(treeElement.getValue());
			}
		}
		return parentValues;
	}
	
	/**
	 * Returns the values for all elements of the tree that are leaves 
	 * (elements that have no children).
	 * @return
	 */
	public List<T> getValuesOfLeaves(){
		List<T> leafValues = new ArrayList<T>();
		for(SushiTreeElement<T> element : treeElements){
			if(!element.hasChildren()){
				leafValues.add(element.getValue());
			}
		}
		return leafValues;
	}
	
	/**
	 * Returns all leaf elements descending from the specified element.
	 * @param element
	 * @return
	 */
	public Set<T> getLeafs(T element){
		Set<T> leaveValues = new HashSet<T>();
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(element);
		if(currentTreeElement != null){
			for(SushiTreeElement<T> treeElement : getIndirectChildren(currentTreeElement)){
				if(!treeElement.hasChildren()){
					leaveValues.add(treeElement.getValue());
				}
			}
		}
		return leaveValues;
	}
	
	/**
	 * Returns all elements of the tree, that have no children.
	 * @return
	 */
	public Set<T> getLeafElements(){
		Set<T> leaves = new HashSet<T>();
		for(SushiTreeElement<T> element : getLeafs()){
			leaves.add(element.getValue());
		}
		return leaves;
	}
	
	/**
	 * Returns all tree elements, that have no children.
	 * @return
	 */
	private Set<SushiTreeElement<T>> getLeafs(){
		Set<SushiTreeElement<T>> leaves = new HashSet<SushiTreeElement<T>>();
		for(SushiTreeElement<T> element : treeElements){
			if(!element.hasChildren()){
				leaves.add(element);
			}
		}
		return leaves;
	}
	
	public boolean isInLeaves(T treeElement){
		Set<SushiTreeElement<T>> leaves = getLeafs();
		for(SushiTreeElement<T> element : leaves){
			if(element.getValue().equals(treeElement)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasChildren(T treeElement){
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(treeElement);
		return currentTreeElement.hasChildren();
	}
	
	/**
	 * Adds a child to the specified parent.
	 * @param parent
	 * @param child
	 */
	public void addChild(T parent, T child) {
		SushiTreeElement<T> treeElement = findTreeElementByValue(parent);
		SushiTreeElement<T> childTreeElement = new SushiTreeElement<T>(treeElement, child);
		treeElements.add(childTreeElement);
		if(parent == null){
			treeRootElements.add(childTreeElement);
		}
	}
	
//	public void addChild(T parent, T child, SushiAttributeTypeEnum type) {
//		
//		SushiTreeElement<T> treeElement = findTreeElementByValue(parent);
//		SushiAttribute<T> childTreeElement = new SushiAttribute<T>(treeElement, child, type);
//		treeElements.add(childTreeElement);
//		if(parent == null){
//			treeRootElements.add(childTreeElement);
//		}
//	}

	/**
	 * Returns true if the parent contains the specified child.
	 * @param parent
	 * @param child
	 */
	public boolean containsChild(T parent, T child){
		return this.getChildren(parent).contains(child);
	}
	
	public boolean addRootElement(T rootElement){
		SushiTreeElement<T> element = new SushiTreeElement<T>(rootElement);
		treeElements.add(element);
		return treeRootElements.add(element);
	}
//	
//	public boolean addRootElement(T rootElement, SushiAttributeTypeEnum type){
//		SushiAttribute<T> element = new SushiAttribute<T>(null, rootElement, type);
//		treeElements.add(element);
//		return treeRootElements.add(element);
//	}
	
	/**
	 * Removes all children from the specified tree element.
	 * @param treeElement
	 * @return
	 */
	public void removeChildren(T treeElement){
		SushiTreeElement<T> currentTreeElement = findTreeElementByValue(treeElement);
		List<SushiTreeElement<T>> children = currentTreeElement.getChildren();
		treeElements.removeAll(children);
		currentTreeElement.removeChildren();
	}

	@Override
	public int size() {
		return treeElements.size();
	}

	@Override
	public boolean isEmpty() {
		return treeElements.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		try{
			for(T element  : this.getElements()){
				if(element.equals(o)){
					return true;
				}
			}
			return false;
		} catch(ClassCastException c){
			return false;
		}
	}

	public List<SushiTreeElement<T>> getTreeElements() {
		return treeElements;
	}
	
	
	@Override
	public Iterator<T> iterator() {
		return getTreeElementValues(treeElements).iterator();
	}

	@Override
	public Object[] toArray() {
		return treeElements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("Was soll das denn zurückgeben!?");
	}

	@Override
	public boolean add(T e) {
		return addRootElement(e);
	}

	@Override
	public boolean remove(Object removeElement) {
		//TODO: assert(removeElement instanceOf T);
		SushiTreeElement<T> removeTreeElement = findTreeElementByValue((T) removeElement);
		return remove(removeTreeElement);
	}
	
	public boolean remove(SushiTreeElement<T> removeTreeElement) {
		if(removeTreeElement != null){
			if(removeTreeElement.hasChildren()){
				List<SushiTreeElement<T>> children = new ArrayList<SushiTreeElement<T>>(removeTreeElement.getChildren());
				for(SushiTreeElement<T> child : children){
					remove(child);
				}
			}
			if (!(removeTreeElement.getParent() == null)){
				removeTreeElement.getParent().getChildren().remove(removeTreeElement);
			}
			return (treeElements.remove(removeTreeElement) && treeRootElements.remove(removeTreeElement));
		}
		return false;
	}
	
	/**
	 * @author tsun
	 * 
	 * @param parent Will be removed as well if it has no childs.
	 * @param child
	 */
	public boolean removeChild(T parent, T child) {
		SushiTreeElement<T> parentElement;
		parentElement = findTreeRootElementByValue(parent);
		if(parentElement == null){
			parentElement = findTreeElementByValue(parent);
		}
		if(parentElement != null){
			if(parentElement.hasChildren()){
				List<SushiTreeElement<T>> children = new ArrayList<SushiTreeElement<T>>(parentElement.getChildren());
				for(SushiTreeElement<T> childTreeElement : children){
					if(childTreeElement.getValue().equals(child)){
						remove(childTreeElement);
						if(!parentElement.hasChildren()){
							remove(parentElement);
						}
						return (treeElements.remove(childTreeElement) && treeElements.remove(parentElement) && treeRootElements.remove(parentElement));
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @author tsun
	 * 
	 * @param parent Will not be removed in any case.
	 * @param child
	 */
	public boolean removeChildOnly(T parent, T child) {
		SushiTreeElement<T> parentElement;
		parentElement = findTreeRootElementByValue(parent);
		if(parentElement == null){
			parentElement = findTreeElementByValue(parent);
		}
		if(parentElement != null){
			if(parentElement.hasChildren()){
				List<SushiTreeElement<T>> children = new ArrayList<SushiTreeElement<T>>(parentElement.getChildren());
				for(SushiTreeElement<T> childTreeElement : children){
					if(childTreeElement.getValue().equals(child)){
						remove(childTreeElement);
						return (treeElements.remove(childTreeElement) && treeElements.remove(parentElement) && treeRootElements.remove(parentElement));
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getTreeElementValues(treeElements).containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean success = true;
		for(T element : collection){
			success = success ? (this.addRootElement(element)) : false;
		}
		return success;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean removeSuccess = true;
		for(Object  element : collection){
			boolean elementRemoveSuccess = collection.remove(element);
			removeSuccess = removeSuccess ? elementRemoveSuccess : false;
		}
		return removeSuccess;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean retainSuccess = true;
//		TODO: assert(collection instanceof T);
		List<SushiTreeElement<T>> copyTreeList = new ArrayList<SushiTreeElement<T>>(treeElements);
		for(SushiTreeElement<T> element : copyTreeList){
			if(!collection.contains(element.getValue())){
				remove(element.getValue());
			}
		}
		return retainSuccess;
	}

	@Override
	public void clear() {
		treeElements.clear();
		treeRootElements.clear();
	}
	
	private ArrayList<T> getTreeElementValues(List<SushiTreeElement<T>>treeElements) {
		ArrayList<T> valueElements = new ArrayList<T>();
		for(SushiTreeElement<T> element : treeElements){
			valueElements.add(element.getValue());
		}
		return valueElements;
	}
	
	private SushiTreeElement<T> findTreeElementByValue(T treeElementValue){
		if(treeElementValue == null){
			return null;
		}
		for(SushiTreeElement<T> currentTreeElement : treeElements){
			if(currentTreeElement.getValue().equals(treeElementValue)){
				return currentTreeElement;
			}
		}
		return null;
	}
	
	private List<SushiTreeElement<T>> findTreeElementsByValue(T treeElementValue){
		if(treeElementValue == null){
			return null;
		}
		List<SushiTreeElement<T>> elements = new ArrayList<SushiTreeElement<T>>();
		for(SushiTreeElement<T> currentTreeElement : treeElements){
			if(currentTreeElement.getValue().equals(treeElementValue)){
				elements.add(currentTreeElement);
			}
		}
		return elements;
	}
	
	public boolean isHierarchical() {
		return (! treeRootElements.containsAll(treeElements));
	}
	
	private SushiTreeElement<T> findTreeRootElementByValue(T treeElementValue){
		if(treeElementValue == null){
			return null;
		}
		for(SushiTreeElement<T> currentTreeElement : treeRootElements){
			if(currentTreeElement.getValue().equals(treeElementValue)){
				return currentTreeElement;
			}
		}
		return null;
	}

	public List<T> getRootElements() {
		return getTreeElementValues(treeRootElements);
	}

	public T findElement(T value) {
		SushiTreeElement<T> element = findTreeElementByValue(value);
		if(element != null){
			return findTreeElementByValue(value).getValue();
		}
		else{
			return null;
		}
	}

	public static List<SushiTree> findAll() {
		Query q = Persistor.getEntityManager().createQuery("select t from SushiTree t");
		return q.getResultList();
	}
	
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiTree");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public ArrayList<T> getElements() {
		return getTreeElementValues(treeElements);
	}
	
	/**
	 * Returns the depth of an element from the tree. If the element is not contained, -1 will be returned.
	 * @param element
	 * @return
	 */
	public int getElementDepth(T element){
		int depth = 0;
		SushiTreeElement<T> treeElement = this.findTreeElementByValue(element);
		if(treeElement == null){
			return -1;
		}
		SushiTreeElement<T> parent = treeElement.getParent();
		while(parent != null){
			parent = parent.getParent();
			depth++;
		}
		return depth;
	}
	
	@Override
	public String toString(){
		return printTreeLevel(treeRootElements, 0);
	}

	private String printTreeLevel(List<SushiTreeElement<T>> treeElements, int count) {
		String tree = "";
		for(SushiTreeElement<T> element : treeElements){
			for(int i = 0; i < count; i++){
				tree += "\t";
			}
			tree += element.getValue() + System.getProperty("line.separator");
			if(element.hasChildren()){
				tree += printTreeLevel(element.getChildren(), count + 1);
			}
		}
		return tree;
	}

	public Boolean retainAllLeafs(Collection<?> collection) {
		boolean retainSuccess = true;
//		TODO: assert(collection instanceof T);
		List<SushiTreeElement<T>> copyTreeList = new ArrayList<SushiTreeElement<T>>(getLeafs());
		for(SushiTreeElement<T> element : copyTreeList){
			if(!collection.contains(element.getValue())){
				remove(element.getValue());
			}
		}
		return retainSuccess;
	}

	public boolean containsRootElement(T eventTypeName) {
		for (SushiTreeElement<T> element : treeRootElements) {
			if (element.getValue().equals(eventTypeName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a node with the given value exists in the children of a parent node.
	 * @param parent
	 * @param child the node that shall be in the children of the parent
	 * 
	 * @return true if the node with the given value exists in the children of the given parent node
	 */
	public boolean isInChildrenOfNode(T parent, T child) {
		if (parent != null) {
			for (T attribute : getElements()) {
				if (attribute.equals(parent)) {
					return getChildren(attribute).contains(child); 
				}
			}
			return false;
		} else {
			return getRootElements().contains(child);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}
