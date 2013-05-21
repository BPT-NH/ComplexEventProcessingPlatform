package sushi.event.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.util.XMLUtils;

/**
 * @author micha
 *
 * @param <K>
 * @param <V>
 */
@Entity
@Table(name = "SushiMapElement")
public class SushiMapElement<K, V> extends Persistable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SushiMapElement<K, V> parent;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<SushiMapElement<K, V>> children = new ArrayList<SushiMapElement<K, V>>();
	
	@Column(name = "MapKey")
	K key;
	
	@Column(name = "MapValue")
	V value;
	
	public SushiMapElement(){
		this.ID = 0;
	}
	
	public SushiMapElement(K key, V value){
		this.key = key;
		this.value = value;
	}
	
	public SushiMapElement(SushiMapElement<K, V> parent, K key, V value){
		this.parent = parent;
		this.key = key;
		this.value = value;
		if(this.parent != null){
			this.parent.addChild(this);
		}
	}
	
	// TODO: consider putting this into a subclass of SushiMapElement<String, Serializable>
	/**
	 * use this only if the SushiMapElement is used as attribute name/value mapping
	 * 
	 * @return attribute name expression 
	 * (examples: 'ETA' for first level attribute named 'ETA', 
	 * 'vehicle_information.transport' for second level attribute named 'transport' which is child of 'vehicle_information'
	 */
	public String getAttributeExpression() {
		if (isRootElement()) {
			return (String) key;
		}
		return parent.getAttributeExpression() + "." + (String) key;
	}
	
	public Document getNodeWithChildnodes() {
		 DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		  domFactory.setNamespaceAware(true);
		  DocumentBuilder builder = null;
		  try {
		   builder = domFactory.newDocumentBuilder();
		  } catch (ParserConfigurationException e) {
		   e.printStackTrace();
		  }
		  // need document from xml for the xml parser
		  Document doc = builder.newDocument();
		  Element root = doc.createElement(((String) key).replaceAll(" ", ""));
		  if (getChildren().isEmpty()) {
			  System.out.print(key + " ");
			  V content = (V) value;
			  //save typed stuff
			  if (content instanceof Date) {
				  root.setTextContent(XMLUtils.getFormattedDate((Date) content));
			  }
			  else if (content instanceof Integer) {
				 root.setTextContent(XMLUtils.getXMLInteger((Integer) content));
				 System.out.println(XMLUtils.getXMLInteger((Integer) content));
			  }
			  else {
				  root.setTextContent((String) content);
				  System.out.println(content);
			  }
		  }
		  doc.appendChild(root);
		  
		  for (SushiMapElement child : getChildren()){
			  Node importedNode =  doc.importNode(child.getNodeWithChildnodes().getFirstChild(), true);
			  root.appendChild(importedNode);
		  }
		  return doc;
	}
	
	public V getValue(){
		return this.value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V setValue(V value) {
		return this.value = value;
	}
	
	public SushiMapElement<K, V> getParent() {
		return parent;
	}

	public void setParent(SushiMapElement<K, V> parent) {
		this.parent = parent;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public List<SushiMapElement<K, V>> getChildren() {
		return children;
	}

	public void setChildren(List<SushiMapElement<K, V>> children) {
		this.children = children;
	}
	
	public boolean hasChildren(){
		return !children.isEmpty();
	}
	
	public boolean isRootElement(){
		return parent == null;
	}

	private void addChild(SushiMapElement<K, V> childTreeMapElement) {
		if(children == null){
			children = new ArrayList<SushiMapElement<K, V>>();
		}
		children.add(childTreeMapElement);
	}

	public void removeChildren() {
		this.children.clear();
	}
	
	@Override
	public String toString(){
		return "TreeMapElement: [" + key + " -> " + value + "]";
	}
	
	public static List<SushiMapTree> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t from SushiMapElement t");
		return q.getResultList();
	}
	
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiMapElement");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
