package sushi.event.attribute;

import java.util.ArrayList;
import java.util.HashSet;
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

@Entity
@Table(name = "SushiAttributeTree")
public class SushiAttributeTree extends Persistable {

	private static final long serialVersionUID = 4641263893746532464L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SushiAttributeTreeID")
	protected int ID;
	
	@Column(name="Auxiliary")
	private String auxiliary = "Auxiliary";
	
//	@OneToMany(cascade = CascadeType.PERSIST)
//	@JoinTable(name="SushiAttributeTree_SushiAttributes")
//	private List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
	
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="SushiAttributeTree_SushiRootAttributes")
	private List<SushiAttribute> rootAttributes = new ArrayList<SushiAttribute>();
	
	public SushiAttributeTree() {
		this.ID = 0;
	}
	
//	public SushiAttributeTree(String nameOfRootAttribute) {
//		assert(!nameOfRootAttribute.isEmpty());
//		SushiAttribute root = new SushiAttribute(nameOfRootAttribute);
//		rootAttributes.add(root);
//		// attributes.add(root);
//	}
	
	public SushiAttributeTree(SushiAttribute rootAttribute) {
		this();
		assert(rootAttribute != null);
		this.rootAttributes.add(rootAttribute);
//		attributes.add(root);
	}
	
	public SushiAttributeTree(List<SushiAttribute> rootAttributes) {
		this();
		assert(rootAttributes != null);
		assert(!rootAttributes.isEmpty());
		this.rootAttributes.addAll(rootAttributes);
//		attributes.add(root);
	}
	
//	public SushiAttributeTree(String nameOfRootAttribute, SushiAttributeTypeEnum typeOfRootAttribute) {
//		assert(!nameOfRootAttribute.isEmpty());
//		SushiAttribute root = new SushiAttribute(nameOfRootAttribute, typeOfRootAttribute);
//		rootAttributes.add(root);
//		// attributes.add(root);
//	}
	
	public boolean isHierarchical() {
		for (SushiAttribute root : rootAttributes) {
			if (root.hasChildren()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addRoot(String rootAttributeName, SushiAttributeTypeEnum type){
		SushiAttribute rootAttribute = new SushiAttribute(rootAttributeName, type);
		return rootAttributes.add(rootAttribute);
	}

	public boolean addRoot(SushiAttribute rootAttribute) {
//		attributes.add(root);
		return rootAttributes.add(rootAttribute);
	}
	
	public boolean addRoots(List<SushiAttribute> rootAttributes) {
//		attributes.add(root);
		return rootAttributes.addAll(rootAttributes);
	}

	public ArrayList<SushiAttribute> getRoots() {
		return new ArrayList<SushiAttribute>(rootAttributes);
	}
	
	public List<String> getRootsAsXPath() {
		List<String> xPaths = new ArrayList<String>();
		for (SushiAttribute attribute : rootAttributes) {
			xPaths.add(attribute.getXPath());
		}
		return xPaths;
	}
	
	public boolean removeRoot(SushiAttribute rootAttribute) {
//		attributes.add(root);
		return rootAttributes.remove(rootAttribute);
	}
	
	public boolean contains(String attributeExpression) {
		List<SushiAttribute> attributes = getAttributes();
		for (SushiAttribute attribute : attributes) {
			if (attribute.getAttributeExpression().equals(attributeExpression)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasChildren(String attributeExpression) {
		SushiAttribute attribute = getAttributeByExpression(attributeExpression);
		return attribute.hasChildren();
	}

	public List<SushiAttribute> getAttributes() {
		List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
		for (SushiAttribute root : rootAttributes) {
			if (root.hasChildren()) {
				addAttributeToSet(root, attributes);
			}
			attributes.add(root);
		}
		return attributes;
	}
	
	public List<SushiAttribute> getAttributesByName(String attributeName) {
		List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
		for (SushiAttribute root : rootAttributes) {
			if (root.hasChildren()) {
				addAttributeToSet(root, attributes, attributeName);
			}
			if (root.getName().equals(attributeName)) {
				attributes.add(root);
			}
		}
		return attributes;
	}
	
	public List<String> getAttributesByExpression() {
		List<SushiAttribute> attributes = getAttributes();
		List<String> attributeExpressions = new ArrayList<String>();
		for (SushiAttribute attribute : attributes) {
			attributeExpressions.add(attribute.getAttributeExpression());
		}
		return attributeExpressions;
	}
	
	public List<String> getAttributesByXPath() {
		List<SushiAttribute> attributes = getAttributes();
		List<String> attributeXPaths = new ArrayList<String>();
		for (SushiAttribute attribute : attributes) {
			attributeXPaths.add(attribute.getXPath());
		}
		return attributeXPaths;
	}
	
	private void addAttributeToSet(SushiAttribute attribute, List<SushiAttribute> attributes, String attributeName) {
		for (SushiAttribute child : attribute.getChildren()) {
			if (child.hasChildren()) {
				addAttributeToSet(child, attributes);
			}
			if (child.getName().equals(attributeName)) {
				attributes.add(child);
			}
		}
	}
	
	private void addAttributeToSet(SushiAttribute attribute, List<SushiAttribute> attributes) {
		for (SushiAttribute child : attribute.getChildren()) {
			if (child.hasChildren()) {
				addAttributeToSet(child, attributes);
			}
			attributes.add(child);
		}
	}
	
	public SushiAttribute getAttributeByExpression(String attributeExpression) {
		List<SushiAttribute> attributes = getAttributes();
		for (SushiAttribute attribute : attributes) {
			if (attribute.getAttributeExpression().equals(attributeExpression)) {
				return attribute;
			}
		}
		return null;
	}
	
	public SushiAttribute getAttributeByXPath(String xPath) {
		List<SushiAttribute> attributes = getAttributes();
		for (SushiAttribute attribute : attributes) {
			if (attribute.getXPath().equals(xPath)) {
				return attribute;
			}
		}
		return null;
	}
	
	public void retainAllAttributes(Set<SushiAttribute> attributes) {
		ArrayList<SushiAttribute> copiedAttributesList = new ArrayList<SushiAttribute>(getAttributes());
		for (SushiAttribute attribute : copiedAttributesList) {
			if (!attributes.contains(attribute)) {
				attribute.removeAttribute();
				if (!attribute.hasParent()) {
					rootAttributes.remove(attribute);
				}
			}
		}
	}
	
	public void retainAllAttributes(List<SushiAttribute> attributes) {
		retainAllAttributes(new HashSet<SushiAttribute>(attributes));
	}
	
//	public boolean add(SushiAttribute root) {
//		return attributes.add(root);
//	}
//
//	public List<SushiAttribute> getAll() {
//		return attributes;
//	}
//	
//	public boolean remove(SushiAttribute root) {
//		return attributes.remove(root);
//	}

	public List<SushiAttribute> getLeafAttributes() {
		List<SushiAttribute> leafs = new ArrayList<SushiAttribute>();
		for (SushiAttribute root : rootAttributes) {
			if (!root.hasChildren()) {
				leafs.add(root);
			} else {
				addAttributeToLeafs(root, leafs);
			}
		}
		return leafs;
	}

	private void addAttributeToLeafs(SushiAttribute attribute, List<SushiAttribute> leafs) {
		for (SushiAttribute child : attribute.getChildren()) {
			if (!child.hasChildren()) {
				leafs.add(child);
			} else {
				addAttributeToLeafs(child, leafs);
			}
		}
	}
	
	public static List<SushiAttributeTree> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiAttributeTree t");
		return q.getResultList();
	}
	
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiAttributeTree");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public String toString() {
		return printTreeLevel(rootAttributes, 0);
	}

	private String printTreeLevel(List<SushiAttribute> attributes, int count) {
		String tree = "";
		for (SushiAttribute attribute : attributes) {
			for (int i = 0; i < count; i++) {
				tree += "\t";
			}
			tree += attribute + System.getProperty("line.separator");
			if (attribute .hasChildren()) {
				tree += printTreeLevel(attribute.getChildren(), count + 1);
			}
		}
		return tree;
	}
	
}
