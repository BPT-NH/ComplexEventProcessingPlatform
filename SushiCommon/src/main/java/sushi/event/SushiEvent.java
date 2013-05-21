package sushi.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiMapTree;
import sushi.notification.SushiNotification;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

@Entity
@Table(name = "Event")
public class SushiEvent extends Persistable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;
	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date timestamp = null;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="MapTreeID")
	protected SushiMapTree<String, Serializable> values;
	
	@ManyToMany(fetch=FetchType.EAGER)
	List<SushiProcessInstance> processInstances;
	
	@ManyToOne
	private SushiEventType eventType;
	
	/**
	 * Default-Constructor for JPA.
	 */
	private SushiEvent() {
		this.ID = 0;
		this.eventType = null;
		this.timestamp = null;
		this.values = new SushiMapTree<String, Serializable>();
		this.processInstances = new ArrayList<SushiProcessInstance>();
	}
	
	private SushiEvent(Date timestamp) {
		this();
		this.timestamp = timestamp;
	}

	public SushiEvent(SushiEventType eventType, Date timestamp) {
		this(timestamp);
		this.eventType = eventType;
	}
	
	public SushiEvent(Date timestamp, SushiMapTree<String, Serializable> values) {
		this(timestamp);
		this.values = values;
	}
	
	public SushiEvent(SushiEventType eventType, Date timestamp, SushiMapTree<String, Serializable> values) {
		this(eventType, timestamp);
		this.values = values;
	}
	
	public SushiMapTree<String, Serializable> getValues() {
		return values;
	}

	public void setValues(SushiMapTree<String, Serializable> values) {
		this.values = values;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public SushiEventType getEventType() {
		return eventType;
	}
	
	public void setEventType(SushiEventType eventType) {
		this.eventType = eventType;
	}
	
	public List<SushiProcessInstance> getProcessInstances() {
		return processInstances;
	}

	public void setProcessInstances(List<SushiProcessInstance> processInstance) {
		this.processInstances = processInstance;
	}
	
	public boolean addProcessInstance(SushiProcessInstance processInstance) {
		if(!processInstances.contains(processInstance)) {
			return processInstances.add(processInstance);
		}
		return false;
	}
	
	public boolean addAllProcessInstances(List<SushiProcessInstance> processInstances) {
		boolean allInserted = true;
		boolean inserted;
		for(SushiProcessInstance processInstance : processInstances){
			inserted = this.addProcessInstance(processInstance);
			allInserted = allInserted ? inserted : false;
		}
		return allInserted;
	}

	public boolean removeProcessInstance(SushiProcessInstance processInstance) {
		return processInstances.remove(processInstance);
	}
	
	@Override
	public boolean equals(Object o) {
		SushiEvent e;
		if (o instanceof SushiEvent) {
			e = (SushiEvent) o;
			if (this.ID == e.getID() && this.timestamp.compareTo(e.getTimestamp()) == 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer eventText = new StringBuffer();
		eventText.append("Event with ID=" + this.ID);
		eventText.append(", Timestamp=" + this.timestamp.toString()); 
		eventText.append(", Event Type=" + this.eventType);
		Map<String, Serializable> values = this.getValues();
		Iterator<String> valueIterator = values.keySet().iterator();
		while (valueIterator.hasNext()) {
			String valueKey = valueIterator.next();
			eventText.append(", " + valueKey + "=" + values.get(valueKey));
		}
		return eventText.toString();
	}
	
	public String shortenedString() {
		StringBuffer eventText = new StringBuffer();
		eventText.append("Event with ID=" + this.ID);
		eventText.append("of Event Type=" + this.eventType);
		return eventText.toString();
	}
	
	public String fullEvent() {
		String eventText = "Event with ID: " + this.ID + " time: " + this.timestamp.toString();
		eventText = eventText + System.getProperty("line.separator") + this.getValues();
		return eventText;
	}
	
	public boolean isHierarchical() {
		return values.isHierarchical();
	}
	
	/**
	 * Method returns events, where the specified column name has the specified value.
	 */
	private static List<SushiEvent> findByAttribute(String columnName, Object value){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM Event " +
				"WHERE " + columnName + " = '" + value + "'", SushiEvent.class);
		return query.getResultList();
	}
	
	public static List<SushiEvent> findByEventTypeAndAttributeExpressionsAndValues(SushiEventType eventType, Map<String, Serializable> attributeExpressionsAndValues){
		StringBuffer sb = new StringBuffer();
		sb.append("" +
				"SELECT * FROM Event JOIN SushiMapTree_SushiMapTreeElements mte " +
					"ON mte.SushiMapTree_SushiMapID=Event.MapTreeID " +
					"JOIN SushiMapTree_SushiMapTreeRootElements mtre " +
					"ON mtre.SushiMapTree_SushiMapID = Event.MapTreeID " +
					"JOIN SushiMapElement me " +
					"ON (me.ID = mtre.treeRootElements_ID OR me.ID = mtre.treeRootElements_ID) " +
					"WHERE EVENTTYPE_ID = '" + eventType.getID() + "'");
		Iterator<String> iterator = attributeExpressionsAndValues.keySet().iterator();
		if (iterator.hasNext()) {
			sb.append(" AND ");
		}
		while (iterator.hasNext()) {
			String attributeExpression = iterator.next();
			Serializable value = attributeExpressionsAndValues.get(attributeExpression);
			if (value instanceof Date) {
				sb.append("(me.MapKey = " + attributeExpression + " AND me.MapValue = {ts '"  + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format((Date) value) + "'})");
			} else {
				sb.append("(me.MapKey = " + attributeExpression + " AND me.MapValue = '" + value + "')");
			}
			if (iterator.hasNext()) {
				sb.append(" OR ");
			}
		}
		Query query = Persistor.getEntityManager().createNativeQuery(sb.toString(), SushiEvent.class);
		return query.getResultList();
	}
	
	public static Serializable findValueByEventTypeAndAttributeExpressionsAndValues(SushiEventType eventType, String attributeExpressionOfValue, Map<String, Serializable> attributeExpressionsAndValuesForSearch){
		StringBuffer sb = new StringBuffer();
		sb.append("" +
					"SELECT me.MapValue FROM Event " +
					"INNER JOIN SushiMapTree_SushiMapTreeElements mte " +
					"ON (mte.SushiMapTree_SushiMapID = Event.MapTreeID) " +
					"INNER JOIN SushiMapElement me " +
					"ON (me.ID = mte.treeElements_ID) " +
					"WHERE EVENTTYPE_ID = '" + eventType.getID() + "' " +
					"AND me.MapKey = '" + attributeExpressionOfValue + "'");
		Iterator<String> iterator = attributeExpressionsAndValuesForSearch.keySet().iterator();
		int indexOfMapPair = 1;
		if (iterator.hasNext()) {
			sb.append(" AND mte.SushiMapTree_SushiMapID IN (SELECT id" + indexOfMapPair + ".SushiMapTree_SushiMapID FROM");
		}
		while (iterator.hasNext()) {
			String attributeExpression = iterator.next();
			Serializable value = attributeExpressionsAndValuesForSearch.get(attributeExpression);
			if (indexOfMapPair > 1) {
				sb.append(" INNER JOIN");
			}
			if (value instanceof Date) {	
				sb.append(" " +
						"(SELECT SushiMapTree_SushiMapID FROM SushiMapTree_SushiMapTreeElements " +
						"WHERE treeElements_ID IN " +
							"(SELECT ID FROM SushiMapElement " +
								"WHERE MapKey = '" + attributeExpression + "' " +
									"AND me.MapValue = {ts '"  + (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format((Date) value) + "'})" +
						") id" + indexOfMapPair);
			} else {
				sb.append(" " +
						"(SELECT SushiMapTree_SushiMapID FROM SushiMapTree_SushiMapTreeElements " +
						"WHERE treeElements_ID IN " +
							"(SELECT ID FROM SushiMapElement " +
								"WHERE MapKey = '" + attributeExpression + "' " +
									"AND MapValue = '" + value + "')" +
						") id" + indexOfMapPair);
			}
			if (indexOfMapPair > 1) {
				sb.append(" ON id" + (indexOfMapPair - 1) + ".SushiMapTree_SushiMapID = id" + indexOfMapPair + ".SushiMapTree_SushiMapID");
			}
			indexOfMapPair++;
		}
		sb.append(")");
		Query query = Persistor.getEntityManager().createNativeQuery(sb.toString());
		if (query.getResultList().isEmpty()) {
			return null;
		}
		return (Serializable) query.getResultList().get(0);
	}
	
	private static List<SushiEvent> findByAttributeGreaterThan(String columnName, String value) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM Event " +
				"WHERE " + columnName + " > '" + value + "'", SushiEvent.class);
		return query.getResultList();
	}
	
	private static List<SushiEvent> findByAttributeLessThan(String columnName, String value) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * FROM Event " +
				"WHERE " + columnName + " < '" + value + "'", SushiEvent.class);
		return query.getResultList();
	}
	
	public static List<SushiEvent> findByValue(String key, Serializable value){
		HashMap<String, Serializable> attributes = new HashMap<String, Serializable>();
		attributes.put(key, value);
		return findByValues(attributes);
	}
	
	public static List<SushiEvent> findByValues(Map<String, Serializable> eventAttributes){
		//TODO ineffizient!
		List<SushiEvent> allEvents = SushiEvent.findAll();
		List<SushiEvent> matchingEvents = new ArrayList<SushiEvent>();
		for(SushiEvent event : allEvents){
			if(event.containsAllValues(eventAttributes)){
				matchingEvents.add(event);
			}
		}
		return matchingEvents;
//		int entryCount = 1;
//		String subFromClause = "";
//		String subWhereClause = "";
//		for(Entry<String, String> attributesEntry : attributes.entrySet()){
//			if(entryCount != attributes.entrySet().size()){
//				subFromClause += "Eventvalues AS EV" + entryCount + ", ";
//				subWhereClause += "EV" + entryCount + ".EVENTATTRIBUTES = '" + attributesEntry.getKey() + "' AND EV" + entryCount + ".EVENTVALUES = '" + attributesEntry.getValue() + "' AND ";
//			}
//			else{
//				subFromClause += "Eventvalues AS EV" + entryCount + " ";
//				subWhereClause += "EV" + entryCount + ".EVENTATTRIBUTES = '" + attributesEntry.getKey() + "' AND EV" + entryCount + ".EVENTVALUES = '" + attributesEntry.getValue() + "'";
//			}
//			entryCount++;
//		}
//		String queryString = 
//				"SELECT * " +
//				"FROM Event As E " +
//				"WHERE E.ID IN ( " +
//					"SELECT EV1.Id " +
//					"FROM " + subFromClause +
//					"WHERE " + subWhereClause + ")";
//		System.err.println(queryString);
//		Query query = Persistor.getEntityManager().createNativeQuery(queryString, SushiEvent.class);
//		return query.getResultList();
	}
	
	private boolean containsAllValues(Map<String, Serializable> eventAttributes) {
		for(Entry<String, Serializable> comparedValue : eventAttributes.entrySet()){
			Object value = values.get(comparedValue.getKey());
			if(value == null || !values.containsKey(comparedValue.getKey()) || !value.equals(comparedValue.getValue())){
				return false;
			}
		}
		return true;
	}

	public static List<SushiEvent> findByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM Event WHERE EVENTTYPE_ID = '" + eventType.getID() + "'", SushiEvent.class);
		return query.getResultList();
	}

	public static long getNumberOfEventsByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT count(*) FROM Event WHERE EVENTTYPE_ID = '" + eventType.getID() + "'");
	    long value = (Long) query.getSingleResult();
	    
		return value;
	}

	public static long getNumberOfEvents(){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT count(*) FROM Event");
	    long value = (Long) query.getSingleResult();
	    
		return value;
	}

	
	public static List<SushiEvent> findByProcessInstance (SushiProcessInstance processInstance){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"Select * " +
				"FROM Event " +
				"WHERE ID IN (" +
					"Select SushiEvent_ID " +
					"FROM Event_ProcessInstance " +
					"WHERE processInstances_ID = '" + processInstance.getID()+ "')", SushiEvent.class);
		return query.getResultList();
	}
	
	public static SushiEvent findByID(int ID){
		List<SushiEvent> events = findByAttribute("ID", Integer.toString(ID));
		if(!events.isEmpty()) {
			return events.get(0);
		} else {
			return null;
		}
	}
	
	public static List<SushiEvent> setEventType(List<SushiEvent> events, SushiEventType eventType) {
		for (SushiEvent event: events) {
			event.setEventType(eventType);
		}
		return events;
	}
	
	public static List<SushiEvent> findByIDGreaterThan(int ID){
		return findByAttributeGreaterThan("ID", Integer.toString(ID));
	}
	
	public static List<SushiEvent> findByIDLessThan(int ID){
		return findByAttributeLessThan("ID", Integer.toString(ID));
	}
	
	public static List<SushiEvent>  findBetween(Date startDate, Date endDate){
		return findBetween(startDate, endDate, null);
	}
	
	@SuppressWarnings("unchecked")
	public static List<SushiEvent> findBetween(Date startDate, Date endDate, SushiEventType eventType){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		Query query;
		if(eventType != null){
			query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM Event WHERE TIMESTAMP BETWEEN '" + dateFormat.format(startDate) + "' AND '" + dateFormat.format(endDate) + "' AND EVENTTYPE_ID ='" + eventType.getID() + "'", SushiEvent.class);
		}
		else{
			query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM Event WHERE TIMESTAMP BETWEEN '" + dateFormat.format(startDate) + "' AND '" + dateFormat.format(endDate) + "'", SushiEvent.class);
		}
		return query.getResultList();
	}
	
	public static List<SushiEvent> findAll() {
		Query q = Persistor.getEntityManager().createQuery("select t from SushiEvent t");
		return q.getResultList();
	}
	
	@Override
	public SushiEvent save() {
		return (SushiEvent) super.save();
	}
	
	@Override
	public SushiEvent merge() {
		return (SushiEvent) super.merge();
	}
	
	public static List<SushiEvent> save(List<SushiEvent> events) {
		//TODO: what happens if eventType for event that shall be saved is unknown to database?
		//catch error and register eventType?
		//check each eventtype beforehand
//		if (SushiEventType.findByID(events.get(0).eventType.getID()) == null){
//			events.get(0).eventType.save();
//		}
		try {
			Persistor.getEntityManager().getTransaction().begin();
			for (SushiEvent event : events) {
				Persistor.getEntityManager().persist(event);
			}
			Persistor.getEntityManager().getTransaction().commit();
			return events;
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SushiEvent remove() {
		//TODO: von Hand loeschen Mist
		//dieses Event aus Prozessinstanzen loeschen
		for(SushiProcessInstance processInstance : this.getProcessInstances()){
			processInstance.removeEvent(this);
			if(processInstance.getTimerEvent() != null && processInstance.getTimerEvent().equals(this)){
				processInstance.setTimerEvent(null);
				if(processInstance.getProcess().getTimeCondition() != null){
					processInstance.getProcess().getTimeCondition().removeTimerEvent(this);
					processInstance.getProcess().getTimeCondition().merge();
				}
			}
			processInstance.merge();
		}
		//Notifications fuer dieses Event loeschen
		for (SushiNotification notification : SushiNotification.findForEvent(this)) {
			notification.remove();
		}
		
		return (SushiEvent) super.remove();
	}
	
	/**
	 * Deletes the specified events from the database.
	 * @return 
	 */
	public static boolean remove(ArrayList<SushiEvent> events) {
		boolean removed = true;
		for(SushiEvent event : events){
			removed = (event.remove() != null);
		}
		return removed;
	}
	
	public static void removeAll() {
		//TODO: Das per Hand zu löschen, ist irgendwie Mist
		for(SushiEvent actualEvent : SushiEvent.findAll()){
			actualEvent.remove();
		}
//		try {
//			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
//			entr.begin();
//			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiEvent");
//			int deleteRecords = query.executeUpdate();
//			entr.commit();
//			System.out.println(deleteRecords + " records are deleted.");
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
	}
	
	public static ArrayList<String> findAllEventAttributes() {
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		Set<String> attributeSet = new HashSet<String>();
		for (SushiEventType actualEventType : eventTypes) {
			for (SushiAttribute attribute : actualEventType.getValueTypes()) {
				attributeSet.add(attribute.getAttributeExpression());
			}
		}
		return new ArrayList<String>(attributeSet);
	}

	public static List<String> findDistinctValuesOfAttributeOfType(String attributeName, SushiEventType type) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT DISTINCT me.MapValue " +
				"FROM Event JOIN SushiMapTree_SushiMapTreeElements mte " +
					"ON mte.SushiMapTree_SushiMapID=Event.MapTreeID " +
					"JOIN SushiMapTree_SushiMapTreeRootElements mtre " +
					"ON mtre.SushiMapTree_SushiMapID = Event.MapTreeID " +
					"JOIN SushiMapElement me " +
					"ON (me.ID = mtre.treeRootElements_ID OR me.ID = mtre.treeRootElements_ID) " +
				"WHERE EVENTTYPE_ID = '" + type.getID() + "' " +
						"AND me.MapKey = '" + attributeName + "'");
		return query.getResultList();
		
	}

	public static long findNumberOfAppearancesByAttributeValue(String attributeName, String value, SushiEventType type) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT count(DISTINCT Event.ID) " +
				"FROM Event JOIN SushiMapTree_SushiMapTreeElements mte " +
					"ON mte.SushiMapTree_SushiMapID=Event.MapTreeID " +
					"JOIN SushiMapTree_SushiMapTreeRootElements mtre " +
					"ON mtre.SushiMapTree_SushiMapID = Event.MapTreeID " +
					"JOIN SushiMapElement me " +
					"ON (me.ID = mtre.treeRootElements_ID OR me.ID = mtre.treeRootElements_ID) " +
				"WHERE EVENTTYPE_ID = '" + type.getID() + "' " +
						"AND me.MapKey = '" + attributeName + "' "+
						"AND me.MapValue = '" + value + "'");
		return (long) query.getSingleResult();
	}
	
	public static long getMinOfAttributeValue(String attribute, SushiEventType eventType) {
		List<String> values = SushiEvent.findDistinctValuesOfAttributeOfType(attribute, eventType);
		if (values.size() > 0) {
			int min = Integer.parseInt(values.get(0));
			for (String value : values) {
				int akt = Integer.parseInt(value);
				if (akt < min ) min = akt;
			}
			return min;
		}
		return (Long) null;
	}

	public static long getMaxOfAttributeValue(String attribute, SushiEventType eventType) {
		List<String> values = SushiEvent.findDistinctValuesOfAttributeOfType(attribute, eventType);
		if (values.size() > 0) {
			int max = Integer.parseInt(values.get(0));
			for (String value : values) {
				int akt = Integer.parseInt(value);
				if (akt > max ) max = akt;
			}
			return max;
		}
		return (Long) null;
	}

	
}
