package sushi.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.aggregation.SushiAggregationRule;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpression;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.visualisation.SushiSimpleChartOptions;

@Entity
@Table(name = "EventType")
public class SushiEventType extends Persistable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@Column(name = "TypeName", unique = true)
	private String typeName;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="Attributes")
	private SushiAttributeTree attributes;
	
	/**
	 * must be an attribute expression
	 */
	@Column(name = "TimestampName")
	private String timestampName;
	
	@Column(name = "XMLEvent")
	private boolean isXMLEvent = false;
	
	@Column(name = "SchemaName")
	private String schemaName;
	    
	private SushiEventType() {
		this.typeName = "";
		this.attributes = new SushiAttributeTree();
		this.timestampName = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName) throws RuntimeException {
		this();
		
		/*
		 *  remove leading and trailing whitespace 
		 *  and replace each sequence of whitespace with an underscore		
		 */
		String strippedTypeName = typeName.trim().replaceAll(" +", "_");
		if (!isValidName(strippedTypeName)) {
			throw new RuntimeException("Event type name is not valid. Only characters [a-z], [A-Z], [0-9], - and _ are allowed!");
		} 
		this.typeName = strippedTypeName;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @param attributeTree
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName, SushiAttributeTree attributeTree) throws RuntimeException {
		this(typeName);
		this.attributes = attributeTree;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @param attributeTree
	 * @param timestampName must be an attribute expression 
	 * (e.g. 'timestamp' for a timestamp that is in root level, 
	 * 'someroot.time' for a timestamp that is in the second level etc.
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName, SushiAttributeTree attributeTree, String timestampName) throws RuntimeException {
		this(typeName, attributeTree);
		/*
		 *  remove leading and trailing whitespace 
		 *  and replace each sequence of whitespace with an underscore		
		 */
		if (timestampName != null) {
			String strippedTimestampName = timestampName.trim().replaceAll(" +", "_");
			if (!isValidName(strippedTimestampName)) {
				throw new RuntimeException("Timestamp name is not valid. Only characters [a-z], [A-Z], [0-9], - and _ are allowed!");
			} 
			this.timestampName = strippedTimestampName;
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @param attributeTree
	 * @param timestampName must be an attribute expression 
	 * (e.g. 'timestamp' for a timestamp that is in root level, 
	 * 'someroot.time' for a timestamp that is in the second level etc.
	 * @param schemaName
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName, SushiAttributeTree attributeTree, String timestampName, String schemaName) throws RuntimeException {
		this(typeName, attributeTree, timestampName);
		this.isXMLEvent = true;
		this.schemaName = schemaName;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @param attributes list of root level attributes
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName, List<SushiAttribute> attributes) throws RuntimeException {
		this(typeName);
		for (SushiAttribute attribute : attributes) {
			this.attributes.addRoot(attribute);
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param typeName
	 * @param attributes list of root level attributes
	 * @param timestampName must be an attribute expression 
	 * (e.g. 'timestamp' for a timestamp that is in root level, 
	 * 'someroot.time' for a timestamp that is in the second level etc.
	 * @throws RuntimeException
	 */
	public SushiEventType(String typeName, List<SushiAttribute> attributes, String timestampName) throws RuntimeException {
		this(typeName, attributes);
		/*
		 *  remove leading and trailing whitespace 
		 *  and replace each sequence of whitespace with an underscore		
		 */
		if (timestampName != null) {
			String strippedTimestampName = timestampName.trim().replaceAll(" +", "_");
			if (!isValidName(strippedTimestampName)) {
				throw new RuntimeException("Timestamp name is not valid. Only characters [a-z], [A-Z], [0-9], - and _ are allowed!");
			} 
			this.timestampName = strippedTimestampName;
		}
	}
	
	public void addValueType(SushiAttribute attribute) {
		attributes.addRoot(attribute);
	}
	
	public void addValueTypes(List<SushiAttribute> rootAttributes) {
		for (SushiAttribute root: rootAttributes) {
			attributes.addRoot(root);
		}
	}
		
	public boolean containsValues(List<String> attributeNames) {
		for (String name: attributeNames) {
			if (!containsValue(name)) return false;
		}
		return true;
	}
	
	public boolean containsValue(String attributeName) {
		return attributes.contains(attributeName);
	}
	
	/**
	 * @param attributeExpression
	 * @return true if the first found attribute has children
	 */
	public boolean hasChildren(String attributeExpression) {
		return attributes.hasChildren(attributeExpression);
	}
	
	public boolean isHierarchical(){
		return attributes.isHierarchical();
	}
	
	public boolean isValidName(String string) {
		return string != null && string.matches("^[-a-zA-Z0-9._]+"); // a-z A-Z 0-9 _ - sind erlaubt
	}
	
	/*
	 * getters / setters
	 */
	
	/**
	 * @return list of root attribute names from the value type tree plus the timestamp name
	 */
	public ArrayList<String> getRootAttributeNames() {
		ArrayList<String> attributeNames = new ArrayList<String>();
		List<SushiAttribute> rootAttributes = attributes.getRoots();
		attributeNames.add(timestampName);
		for (SushiAttribute attribute : rootAttributes) {
			attributeNames.add(attribute.getName());
		}
		return attributeNames;
	}
	
	/**
	 * @return list of attribute expressions from the value type tree plus the timestamp name
	 */
	public ArrayList<String> getAttributeExpressions() {
		ArrayList<String> attributeExpressions = getAttributeExpressionsWithoutTimestampName();
		attributeExpressions.add(timestampName);
		return attributeExpressions;
	}
	
	/**
	 * @return list of attribute expressions from the value type tree
	 */
	public ArrayList<String> getAttributeExpressionsWithoutTimestampName() {
		ArrayList<String> attributeExpressions = new ArrayList<String>();
		List<SushiAttribute> allAttributes = attributes.getAttributes();
		for (SushiAttribute attribute : allAttributes) {
			attributeExpressions.add(attribute.getAttributeExpression());
		}
		return attributeExpressions;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String name) {
		if (!isValidName(name)){
			throw new RuntimeException("Event type name is invalid. Allowed characters: [a-z] [A-Z] [0-9] - _");
		} 
		this.typeName = name;
	}
	
	/**
	 * @return timestamp name as attribute expression
	 */
	public String getTimestampName() {
		return timestampName;
	}
	
	public String getTimestampNameAsXPath() {
		if (timestampName == null) {
			return null;
		}
		return "/" + timestampName.replace(".", "/");
	}

	public void setTimestampName(String timestampName) {
		if (timestampName != null) {
			String strippedTimestampName = timestampName.trim().replaceAll(" +", "_");
			if (!isValidName(strippedTimestampName)) {
				throw new RuntimeException("Timestamp name is not valid. Only characters [a-z], [A-Z], [0-9], - and _ are allowed!");
			} 
			this.timestampName = strippedTimestampName;
		} else {
			this.timestampName = null;
		}
	}
	
	public List<SushiAttribute> getRootLevelValueTypes() {
		return attributes.getRoots();
	}
	
	public List<SushiAttribute> getValueTypes() {
		return attributes.getAttributes();
	}
	
	public SushiAttributeTree getValueTypeTree() {
		return attributes;
	}
	
	public void setValueTypeTree(SushiAttributeTree attributes) {
		this.attributes = attributes;
	}

	public void setXMLEvent(boolean isXMLEvent) {
		this.isXMLEvent = isXMLEvent;
	}
	
	public boolean isXMLEvent() {
		return isXMLEvent;
	}
	
	public String getXMLName() {
		return schemaName;
	}

	public void setXMLName(String xmlName) {
		this.schemaName = xmlName;
	}
	
	@Override
	public String toString() {
		String processText = this.typeName + " (" + this.ID + ")";
		return processText;
	}
	
	public static SushiEventType findByID(int ID){
		 List<SushiEventType> eventTypes = findByAttribute("ID", Integer.toString(ID));
		 if(!eventTypes.isEmpty()){
			 return eventTypes.get(0);
		 }else{
			 return null;
		 }
	}
	
	public static SushiEventType findBySchemaName(String schemaName){
		 List<SushiEventType> eventTypes = findByAttribute("SchemaName", schemaName);
		 if(!eventTypes.isEmpty()){
			 return eventTypes.get(0);
		 }else{
			 return null;
		 }
	}
	
	public static List<SushiEventType> findByIDGreaterThan(int ID){
		return findByAttributeGreaterThan("ID", Integer.toString(ID));
	}
	
	public static List<SushiEventType> findByIDLessThan(int ID){
		return findByAttributeLessThan("ID", Integer.toString(ID));
	}
	
	// TODO: Queries anpassen, da typisierte Attribute unterstützt werden
	public static List<SushiEventType> findByAttribute(String columnName, String value) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM EventType WHERE " + columnName + " = '" + value + "'", SushiEventType.class);
		return query.getResultList();
	}
	
	private static List<SushiEventType> findByAttributeGreaterThan(String columnName, String value) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM EventType " +
				"WHERE " + columnName + " > '" + value + "'", SushiEventType.class);
		return query.getResultList();
	}
	
	private static List<SushiEventType> findByAttributeLessThan(String columnName, String value) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM EventType " +
				"WHERE " + columnName + " < '" + value + "'", SushiEventType.class);
		return query.getResultList();
	}
	
	public static SushiEventType findByTypeName(String typeName) {
		Query query = Persistor.getEntityManager().createNativeQuery("" + 
				"SELECT * FROM EventType " + 
				"WHERE TypeName = '" + typeName + "'", SushiEventType.class);
//		Type names should be distinct!
		assert(query.getResultList().size() < 2);
		try{
			if (query.getResultList().size() > 0) {
				return (SushiEventType) query.getResultList().get(0);
			} else {
				return null;
			}
		} catch(Exception e){
			System.err.println(e);
			return null;
		}
	}
	
	public List<String> getAttributeKeysFromMap() {
		String queryString = "" + 
				"SELECT DISTINCT MapKey FROM SushiMapElement " + 
				"WHERE ID IN (SELECT treeRootElements_ID FROM SushiMapTree_SushiMapTreeRootElements " + // hier nur auf flachen Events
						"WHERE SushiMapTree_SushiMapID IN (SELECT MapTreeID FROM Event " +
							"WHERE EVENTTYPE_ID = '" + ID + "'))";
//		System.out.println(queryString);
		Query query = Persistor.getEntityManager().createNativeQuery(queryString);
		if (query.getResultList() == null) return new ArrayList<String>();
		return query.getResultList();
	}
	
	public List<Serializable> findAttributeValues(String selectedConditionAttribute) {
		String queryString = "" + 
				"SELECT DISTINCT MapValue FROM SushiMapElement " + 
				"WHERE MapKey = '" + selectedConditionAttribute + "' AND " +
					"ID IN (SELECT treeRootElements_ID FROM SushiMapTree_SushiMapTreeRootElements " + // hier nur auf flachen Events
						"WHERE SushiMapTree_SushiMapID IN (SELECT MapTreeID FROM Event " +
							"WHERE EVENTTYPE_ID = '" + ID + "'))";
		Query query = Persistor.getEntityManager().createNativeQuery(queryString);
		return query.getResultList();
	}
	
	public static List<SushiEventType> findAll() {
		Query query = Persistor.getEntityManager().createQuery("select t from SushiEventType t");
		return query.getResultList();
	}
	
	
	public static List<SushiEventType> findMatchingEventTypes(List<String> stringValues, String importTimeName) {
		List<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();
		for (SushiEventType eventType: SushiEventType.findAll()) {
			//prepare attributes without import Time
			ArrayList<String> attributes = eventType.getRootAttributeNames();
			attributes.remove(importTimeName);
			if (stringValues.containsAll(attributes)) {
				selectedEventTypes.add(eventType);
			}
		}
		return selectedEventTypes;
	}
	

	@Override
	public SushiEventType save() {
		return (SushiEventType) super.save();
	}
	
	public static boolean save(List<SushiEventType> eventTypes) {
		try {
			Persistor.getEntityManager().getTransaction().begin();
			for (SushiEventType eventType : eventTypes) {
				Persistor.getEntityManager().persist(eventType);
			}
			Persistor.getEntityManager().getTransaction().commit();
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Deletes this event type from the database.
	 * @return 
	 */
	@Override
	public SushiEventType remove() {
		try {
			// remove eventtype from process
			for (SushiProcess process : SushiProcess.findByEventType(this)) {
				process.removeEventType(this);
				if(process.getTimeCondition() != null && process.getTimeCondition().getSelectedEventType().equals(this)){
					process.getTimeCondition().remove();
				}
				process.save();
			}
			
			//delete events of eventType
			for (SushiEvent event : SushiEvent.findByEventType(this)) {
				event.remove();
			}
			//delete eventTypeRule, that create this eventType
			EventTypeRule rule = EventTypeRule.findEventTypeRuleForCreatedEventType(this);
			if (rule != null){
				rule.remove();
			}
			//update eventTypeRule, remove if no usedType remains
			List<EventTypeRule> containingRules = EventTypeRule.findEventTypeRuleForContainedEventType(this);
			for(EventTypeRule containingRule : containingRules){
				containingRule.getUsedEventTypes().remove(this);
				if (containingRule.getUsedEventTypes().isEmpty()) {
					containingRule.remove();
				}
			}
			//remove ChartOptions
			List<SushiSimpleChartOptions> charts = SushiSimpleChartOptions.findByEventType(this);
			for(SushiSimpleChartOptions chart : charts){
				chart.remove();
			}
			//remove event types from monitoring points
			List<MonitoringPoint> monitoringPoints = MonitoringPoint.findByEventType(this);
			for(MonitoringPoint monitoringPoint : monitoringPoints){
				monitoringPoint.setEventType(null);
				monitoringPoint.merge();
			}
			
			//remove aggregation rules referencing this event type
			List<SushiAggregationRule> aggregationRules = SushiAggregationRule.findByEventType(this);
			for (SushiAggregationRule aggregationRule : aggregationRules) {
				aggregationRule.remove();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SushiEventType) super.remove();
}

/**
	 * Deletes the specified eventtypes from the database.
	 * @return 
	 */
	public static boolean remove(List<SushiEventType> eventTypes) {
		boolean removed = true;
		for(SushiEventType eventType : eventTypes){
			removed = (eventType.remove() != null);
		}
		return removed;
	}
	
	public static void removeAll() {
		List<SushiEventType> allEventTypes = SushiEventType.findAll();
		SushiEventType.remove(allEventTypes);
	}

	public static List<String> getAllTypeNames() {
		ArrayList<String> eventTypeNames = new ArrayList<String>();
		for(SushiEventType eventType : SushiEventType.findAll()){
			eventTypeNames.add(eventType.getTypeName());
		}
		return eventTypeNames;
	}

}


