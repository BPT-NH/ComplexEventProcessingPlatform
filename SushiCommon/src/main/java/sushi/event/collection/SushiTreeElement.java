package sushi.event.collection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sushi.persistence.Persistable;

/**
 * @author micha
 *
 * @param <T>
 */
@Entity
@Inheritance
@DiscriminatorColumn(name="ElementType")
@Table(name = "SushiTreeElement")
public class SushiTreeElement<T> extends Persistable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;
	
	@Column(name = "Value")
	protected T value;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private SushiTreeElement<T> parent;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<SushiTreeElement<T>> children = new ArrayList<SushiTreeElement<T>>();
	
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "patternTree_ID")
//	private SushiPatternTree patternTree;
		
	public SushiTreeElement() {
		this.ID = 0;
	}

	/**
	 * creates a root node
	 * 
	 * @param content the content to be stored in the new node
	 */
	public SushiTreeElement(T value) {
		this();
		this.value = value;
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public SushiTreeElement(SushiTreeElement<T> parent, T value) {
		this(value);
		this.parent = parent;
		if(this.parent != null){
			this.parent.addChild(this);
		}
	}
	
	/**
	 * creates a root node
	 * 
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public SushiTreeElement(int id, T value) {
		this(value);
		this.ID = id;
	}
	
	/**
	 * creates a root node
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public SushiTreeElement(SushiTreeElement<T> parent, int id, T value) {
		this(id, value);
		this.parent = parent;
		this.parent.addChild(this);
	}

	public int getID() {
		return ID;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public SushiTreeElement<T> getParent() {
		return parent;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public void setParent(SushiTreeElement<T> parent) {
		this.parent = parent;
		if (parent != null && !parent.getChildren().contains(this)) {
			parent.addChild(this);
		}
	}

	public List<SushiTreeElement<T>> getChildren() {
		return children;
	}
	
	public boolean hasChildren(){
		return !children.isEmpty();
	}

	public void setChildren(List<SushiTreeElement<T>> children) {
		this.children = children;
	}
	
	/**
	 * may be used as an alternate identifier for this element 
	 * since it is dependent on its parent(s)
	 * @return XPath expression as String
	 */
	public String getXPath() {
		if (parent == null) {
			return "/" + value.toString().replaceAll(" ", "");
		} else {
			return parent.getXPath() + "/" + value.toString();
		}	
	}
	
	/**
	 * root level is 0
	 * 
	 * @return level of element as int
	 */
	public int getLevel() {
		if (parent == null) {
			return 0;
		} else {
			return parent.getLevel() + 1;
		}	
	}
	
	/**
	 * use removeElement()
	 * @return 
	 */
	@Override
	public Persistable remove() {
		removeElement();
		return super.remove();
	}
	
	public void removeElement() {
		for (SushiTreeElement<T> child : children) {
			child.setParent(null);
		}
		children.clear();
		if (hasParent()) {
			parent.removeChild(this);
		}
	}
	
	private boolean addChild(SushiTreeElement<T> childTreeElement) {
		return children.add(childTreeElement);
	}
	
	public boolean removeChild(SushiTreeElement<T> nestedTreeElement) {
		return children.remove(nestedTreeElement);
	}

	public void removeChildren() {
		children.clear();
	}
	
	@Override
	public String toString(){
		return value.toString();
	}
}
