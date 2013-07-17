package sushi.event.attribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sushi.correlation.CorrelationRule;
import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

/**
 *  Representation of an Attribute/Datatype element of the SushiAttributTree
 */
@Entity
@Table(
	name = "SushiAttribute"
)
public class SushiAttribute extends Persistable {
	
	private static final long serialVersionUID = -3804228219409837851L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;
	
	@ManyToOne
	@JoinColumn(name="AttributeTreeID")
	private SushiAttributeTree attributeTree;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private SushiAttribute parent;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<SushiAttribute> children = new ArrayList<SushiAttribute>();
	
	@OneToMany(mappedBy = "firstAttribute")
	private Set<CorrelationRule> correlationRulesFirst = new HashSet<CorrelationRule>();
	
	@OneToMany(mappedBy = "secondAttribute")
	private Set<CorrelationRule> correlationRulesSecond = new HashSet<CorrelationRule>();
	
	@Column(name = "Name")
	protected String name;

	@Column(name = "AttributeType")
	@Enumerated(EnumType.STRING)
	private SushiAttributeTypeEnum type = SushiAttributeTypeEnum.STRING;
	
	/**
	 * for temporary use in TransformationRuleEditor only
	 * not persisted by JPA
	 */
	@Transient
	private boolean isTimestamp;
	
	public SushiAttribute() {
		this.ID = 0;
		this.isTimestamp = false;
	}
	
	/**
	 * creates a root attribute without data type 
	 * (use this constructor if you add child attributes to the root attribute)
	 * 
	 * @param name allowed characters: a-z, A-Z, 0-9, _; whitespace(s) converted to underscore
	 */
	public SushiAttribute(String name) throws RuntimeException {
		this();
		if (name.equals("ProcessInstance")) {
			throw new RuntimeException("The attribute name 'ProcessInstance' is reserved. Please choose another one.");
		} 
		this.name = name.trim().replaceAll(" +","_").replaceAll("[^a-zA-Z0-9_]+","");
	}
	
	/**
	 * creates a root attribute with data type
	 * 
	 * @param name
	 * @param type
	 */
	public SushiAttribute(String name, SushiAttributeTypeEnum type) throws RuntimeException {
		this(name);
		this.type = type;
	}
	
	/**
	 * creates a child node without data type and adds it to the given parent
	 * (use this constructor if you add child attributes to the root attribute)
	 * 
	 * @param parent
	 * @param name
	 */
	public SushiAttribute(SushiAttribute parent, String name) throws RuntimeException {
		this.parent = parent;
		this.name = name;
		parent.setType(null);
		this.parent.addChild(this);
	}
	
	/**
	 * creates a child node with data type and adds it to the given parent
	 * 
	 * @param parent
	 * @param name
	 * @param type
	 */
	public SushiAttribute(SushiAttribute parent, String name, SushiAttributeTypeEnum type) throws RuntimeException {
		this(parent, name);
		this.type = type;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * Use getAttributeExpression() instead if you want to use it 
	 * as a key for the SushiMapTree (= values) for the SushiEvent.
	 * 
	 * @return the name of the attribute
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public SushiAttribute getParent() {
		return parent;
	}
	
	public void setParent(SushiAttribute parent) {
		this.parent = parent;
		this.parent.setType(null);
		this.parent.addChild(this);
	}
	
	public boolean isRootElement() {
		return parent == null;
	}
	
	public SushiAttributeTypeEnum getType() {
		return type;
	}

	public void setType(SushiAttributeTypeEnum attributeType) {
		this.type = attributeType;
	}

	
	public boolean isTimestamp() {
		return isTimestamp;
	}

	public void setTimestamp(boolean isTimestamp) {
		this.isTimestamp = isTimestamp;
	}

	public ArrayList<SushiAttribute> getChildren() {
		return new ArrayList<SushiAttribute>(children);
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}
	
	/**
	 * adds child node to attribute element 
	 */
	public boolean addChild(SushiAttribute attribute) {
		if (!attribute.hasParent()) {
			attribute.setParent(attribute);
		}
		return children.add(attribute);
	}

	public void removeAttribute() {
		children.clear();
		if (hasParent()) {
			parent.removeChild(this);
			if (!parent.hasChildren()) {
				// because leaf attributes without types are not allowed
				parent.setType(SushiAttributeTypeEnum.STRING);
			}
		}
	}
	
	public void removeChild(SushiAttribute attribute) {
		children.remove(attribute);
	}
	
	public void removeAllChildren() {
		children.clear();
	}
	
	/**
	 * Generates an identifier composed of [level of this attribute in tree]-[name of parent attribute]-[name of this attribute] and returns it.
	 * Does not return the ID under which the attribute is stored in the database!
	 *  
	 * @return generated identifier as String
	 */
	public String getIdentifier() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelInTree(this, 0));
		if (parent != null) {
			sb.append("-" + parent.getName().trim());
		}
		sb.append(name.trim());
		return sb.toString();
	}
	
	/**
	 * @return level of this attribute in tree as an intenger
	 */
	private int getLevelInTree(SushiAttribute attribute, int rootLevel) {
		if (!attribute.hasParent()) {
			return rootLevel;
		}
		return getLevelInTree(attribute.getParent(), rootLevel + 1);
	}

	/**
	 * 
	 * @return returns recursive the path to this element as XPath
	 */
	public String getXPath() {
		if (parent == null) {
			return "/" + name.toString().replaceAll(" ", "");
		}
		return parent.getXPath() + "/" + name.toString();
		
	}
	
	public String getAttributeExpression() {
		if (isRootElement()) {
			return name;
		}
		return parent.getAttributeExpression() + "." +  name;
	}
	
	private SushiAttribute getRootLevelParent() {
		if (parent == null) {
			return this;
		}
		return parent.getRootLevelParent();
	}
	
	@JsonIgnore
	public SushiEventType getEventType() {
		SushiAttribute rootLevelParent = getRootLevelParent();
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM EventType " +
				"WHERE Attributes = '" + rootLevelParent.getAttributeTree().getID() + "'", SushiEventType.class);
		return (SushiEventType) query.getSingleResult();
	}
	
	/**
	 * attribute identifier consisting of event type name plus attribute expression
	 * example: Order.orderId for first level attribute with name "orderId" of event type "Order"
	 * 
	 * @return qualified attribute name
	 */
	public String getQualifiedAttributeName() {
		return getEventType().getTypeName() + "." + getAttributeExpression();
	}
	
	@JsonIgnore
	public SushiAttributeTree getAttributeTree() {
		return attributeTree;
	}
	
	public void setAttributeTree(SushiAttributeTree attributeTree) {
		this.attributeTree = attributeTree;
	}
	
	public boolean addToCorrelationRulesFirst(CorrelationRule rule) {
		return correlationRulesFirst.add(rule);
	}
	
	public boolean addToCorrelationRulesSecond(CorrelationRule rule) {
		return correlationRulesSecond.add(rule);
	}

	/**
	 * attributes are equal if their parent attributes and attribute names are equal
	 * NOTE: use equalsWithEventType(SushiAttribute element) for including the event type
	 */
	@Override
	public boolean equals(Object element) {
		if (element instanceof SushiAttribute) {
			SushiAttribute attribute = (SushiAttribute) element;
			/*
			 * attributes are equal irrespective of their types
			 * because it would be else possible to add attributes
			 * with the same name to a parent attribute
			 */
			return getAttributeExpression().equals(attribute.getAttributeExpression());
		} else {
			return false;
		}
	}
	
	/**
	 * equality by parent attributes and attribute names
	 */
	public boolean equalsWithEventType(SushiAttribute attribute) {
		boolean bool1 = false, bool2 = false;
		if (getRootLevelParent().getAttributeTree() == null) {
			bool1 = (attribute.getAttributeTree() == null) ? true : false;
		} else {
			bool1 = getRootLevelParent().getAttributeTree().equals(attribute.getAttributeTree());
		}
		bool2 = getAttributeExpression().equals(attribute.getAttributeExpression());
		return bool1 && bool2;
	}
	
	/**
	 * attributes have the same hashcode if their types and names are equal
	 * and if they belong to the same attribute tree (-> event type)
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		if (type != null) hashCode += type.hashCode();
		if (getXPath() != null) hashCode += getXPath().hashCode();
		if (attributeTree != null) hashCode += attributeTree.hashCode();
		return hashCode;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (isTimestamp()) {
			sb.append(" : Timestamp");
		} else if (type != null) {
			sb.append(" : " + type);
		}
		return sb.toString();
	}
}