package sushi.event.collection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * TreeStructure used for structured key value mapping. Each node can have childes. Its possible to have many root nodes.
 * @param <K>
 * @param <V>
 */
@Entity
@Table(name = "SushiMapTree")
public class SushiMapTree<K, V> extends Persistable implements Map<K,V> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SushiMapID")
	protected int ID;
	
	// splitted root elements and hierachical for more convinience
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name="SushiMapTree_SushiMapTreeElements")
	private List<SushiMapElement<K,V>> treeElements = new ArrayList<SushiMapElement<K,V>>();
	
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name="SushiMapTree_SushiMapTreeRootElements")
	private List<SushiMapElement<K,V>> treeRootElements = new ArrayList<SushiMapElement<K,V>>();
	
	 // JPA cannot save empty objects. this element is carrying  
	@Column(name="Test")
	private String test = "Test";
	
	public SushiMapTree(){
		this.ID = 0;
	}
	
	public SushiMapTree(K rootElementKey, V rootElementValue){
		this.ID = 0;
		SushiMapElement<K, V> element = new SushiMapElement<K, V>(rootElementKey, rootElementValue);
		treeElements.add(element);
		treeRootElements.add(element);
	}
	
	public boolean isHierarchical() {
		return (! (treeRootElements.size() == treeElements.size()));
	}
	
	public V getValueOfAttribute(String attribute) {
		for (SushiMapElement<K, V>  element : treeElements) {
			if (element.getKey().equals(attribute)) {
				return element.getValue();
			}
		}
		return null;
	}
	
	/**
	 * @return Values of the Elements in the first hierarchy
	 */
	public List<V> getRootElementValues(){
		List<V> rootElementValues = new ArrayList<V>();
		for(SushiMapElement<K, V> currentTreeElement : treeRootElements){
			rootElementValues.add(currentTreeElement.getValue());
		}
		return rootElementValues;
	}
	
	/**
	 * @param treeElementKey
	 * @return value of the partent of the node of the given key
	 */
	public V getParentValue(K treeElementKey){
		for(SushiMapElement<K, V> currentTreeElement : treeElements){
			if(currentTreeElement.getKey() == treeElementKey){
				return currentTreeElement.getParent().getValue();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param treeElementKey
	 * @return key of the node of the given key
	 */
	public K getParentKey(K treeElementKey){
		for(SushiMapElement<K, V> currentTreeElement : treeElements){
			if(currentTreeElement.getKey() == treeElementKey){
				return currentTreeElement.getParent().getKey();
			}
		}
		return null;
	}
	
	/**
	 * @param treeElementKey
	 * @return all children keys for a specific element in the tree with the key treeElementKey.
	 */
	public List<K> getChildrenKeys(K treeElementKey){
		List<K> childrenKeys = new ArrayList<K>();
		SushiMapElement<K,V> currentMapElement = findMapElementByKey(treeElementKey);
		for(SushiMapElement<K,V> childElement : currentMapElement.getChildren()){
			childrenKeys.add(childElement.getKey());
		}
		return childrenKeys;
	}
	
	/**
	 * @param treeElementKey
	 * @return all children values for a specific element in the tree with the key treeElementKey.
	 */
	public List<V> getChildrenValues(K treeElementKey){
		List<V> childrenValues = new ArrayList<V>();
		SushiMapElement<K,V> currentMapElement = findMapElementByKey(treeElementKey);
		for(SushiMapElement<K,V> childElement : currentMapElement.getChildren()){
			childrenValues.add(childElement.getValue());
		}
		return childrenValues;
	}
	
	/**
	 * checks if the node of the given key has children
	 * @param treeElementKey
	 * @return
	 */
	public boolean hasChildren(K treeElementKey){
		SushiMapElement<K,V> currentMapElement = findMapElementByKey(treeElementKey);
		return currentMapElement.hasChildren();
	}
	
	/**
	 * Adds a child to the specified parent.
	 * @param parentKey
	 * @param child
	 */
	public void addChild(K parentKey, K childKey, V childValue){
		SushiMapElement<K,V> parentElement = findMapElementByKey(parentKey);
		SushiMapElement<K,V> childMapElement = new SushiMapElement<K,V>(parentElement, childKey, childValue);
		treeElements.add(childMapElement);
		if(parentElement == null){
			treeRootElements.add(childMapElement);
		}
	}
	
	/**
	 * adds root node with the given key and value
	 * @param childKey
	 * @param childValue
	 * @return
	 */
	public boolean addRootElement(K childKey, V childValue){
		SushiMapElement<K,V> element = new SushiMapElement<K,V>(childKey, childValue);
		return (treeRootElements.add(element) && treeElements.add(element));
	}
	
	/**
	 * Removes all children from the specified map element.
	 * @param treeElement
	 * @return
	 */
	public void removeChildren(K mapElementKey){
		SushiMapElement<K,V> currentMapElement = findMapElementByKey(mapElementKey);
		for(SushiMapElement<K,V> child : currentMapElement.getChildren()){
			remove(child.getKey());
		}
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
	public boolean containsKey(Object key) {
		return keySet().contains(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	@Override
	public V get(Object key) {
		SushiMapElement<K, V> element = findMapElementByKey((K) key);
		if(element != null){
			return element.getValue();
		} else {
			return null;
		}
		
	}

	@Override
	public V put(K key, V value) {
		treeElements.add(new SushiMapElement<K, V>(key, value));
		treeRootElements.add(new SushiMapElement<K, V>(key, value));
		return value;
	}

	@Override
	public V remove(Object key) {
		SushiMapElement<K, V> removeTreeElement = findMapElementByKey((K) key);
		V removeTreeElementValue = removeTreeElement(removeTreeElement);
		return removeTreeElementValue;
	}
	
	/**
	 * removes the given node from the tree
	 * @param removeTreeElement
	 * @return
	 */
	private V removeTreeElement(SushiMapElement<K, V> removeTreeElement) {
		V removeTreeElementValue = null;
		if(removeTreeElement != null){
			removeTreeElementValue = removeTreeElement.getValue();
		}
		if(removeTreeElement != null){
			if(removeTreeElement.hasChildren()){
				List<SushiMapElement<K, V>> children = new ArrayList<SushiMapElement<K, V>>(removeTreeElement.getChildren());
				for(SushiMapElement<K, V> child : children){
					remove(child.getKey());
				}
			}
			if (removeTreeElement.getParent() != null){
				removeTreeElement.getParent().getChildren().remove(removeTreeElement);
			}
			treeElements.remove(removeTreeElement);
			treeRootElements.remove(removeTreeElement);
		}
		return removeTreeElementValue;
		
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(java.util.Map.Entry<? extends K, ? extends V> element  : m.entrySet()){
			treeRootElements.add(new SushiMapElement<K, V>(element.getKey(), element.getValue()));
		}
	}

	@Override
	public void clear() {
		treeElements.clear();
		treeRootElements.clear();
	}

	@Override
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for(SushiMapElement mapElement : treeElements){
			keySet.add((K) mapElement.getKey());
		}
		return keySet;
	}

	@Override
	public Collection<V> values() {
		Collection<V> valueCollection = new ArrayList<V>();
		for(SushiMapElement mapElement : treeElements){
			valueCollection.add((V) mapElement.getValue());
		}
		return valueCollection;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> elementSet = new HashSet<Map.Entry<K,V>>();
		for(SushiMapElement<K, V> element : treeElements){
			elementSet.add(new AbstractMap.SimpleEntry<K, V>(element.getKey(), element.getValue()));
		}
		return elementSet;
	}
	
	/**
	 * return value of the node of the given key
	 * @param elementKey
	 * @return
	 */
	public V findElement(K elementKey){
		SushiMapElement<K, V> element = findMapElementByKey(elementKey);
		if(element != null){
			return element.getValue();
		} else {
			return null;
		}
		
	}
	
	/**
	 * return node with the given key
	 * @param treeElementKey
	 * @return
	 */
	private SushiMapElement<K, V> findMapElementByKey(K treeElementKey){
		if(treeElementKey == null){
			return null;
		}
		for(SushiMapElement<K, V> currentMapElement : treeElements){
			if(currentMapElement.getKey().equals(treeElementKey)){
				return currentMapElement;
			}
		}
		return null;
	}
	
	/**
	 * @return all maptrees
	 */
	public static List<SushiMapTree> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t from SushiMapTree t");
		return q.getResultList();
	}
	
	/**
	 * removes all maptrees from DB
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiMapTree");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public String toString(){
		return printMapLevel(treeRootElements, 0);
	}

	private String printMapLevel(List<SushiMapElement<K, V>> treeElements, int count) {
		String tree = "";
		for(SushiMapElement<K, V> element : treeElements){
			for(int i = 0; i < count; i++){
				tree += "\t";
			}
			tree += element.getKey() + " : " + element.getValue() + "\r\n";
			if(element.hasChildren()){
				tree += printMapLevel(element.getChildren(), ++count);
			}
		}
		return tree;
	}
	
	/**
	 * deletes all elements except the given
	 * @param collection
	 * @return
	 */
	public boolean retainAll(Collection collection){
		boolean retainSuccess = true;
		List<SushiMapElement<K, V>> copyTreeList = new ArrayList<SushiMapElement<K, V>>(treeElements);
		for(SushiMapElement<K, V> element : copyTreeList){
			if(!collection.contains(element.getValue())){
				remove(element.getValue());
			}
		}
		return retainSuccess;
	}
	
	/**
	 * use this only if the SushiMapTree is used as attribute name/value mapping
	 * 
	 */
	public void retainAllByAttributeExpression(ArrayList<String> retainableAttributeExpressions) {
		List<SushiMapElement<K, V>> treeElementsToBeRemoved = new ArrayList<SushiMapElement<K, V>>();
		for (SushiMapElement<K, V> element : treeElements) {
			if(!retainableAttributeExpressions.contains(element.getAttributeExpression())){
				treeElementsToBeRemoved.add(element);
			}
		}
		for (SushiMapElement<K, V> element : treeElementsToBeRemoved) {
			this.removeTreeElement(element);
		}
	}

	/**
	 * delete all node except those which keys are mentioned
	 * @param retainableKeys
	 */
	public void retainAllKeys(ArrayList<String> retainableKeys) {
		for(K key : keySet()){
			if(!retainableKeys.contains(key)){
				this.remove(key);
			}
		}
	}
	
	public List<SushiMapElement<K, V>> getTreeRootElements() {
		return treeRootElements;
	}

	public void setTreeRootElements(List<SushiMapElement<K, V>> treeRootElements) {
		this.treeRootElements = treeRootElements;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return deepClone();
	}
	
	private SushiMapTree<K, V> deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (SushiMapTree<K, V>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public int getID() {
		return ID;
	}
}