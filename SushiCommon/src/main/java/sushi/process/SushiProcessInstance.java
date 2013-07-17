package sushi.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

/**
 * Represents an instance of a process. It is unique per process by the values of the correlation attributes.
 * References the events belonging to the process instance and holds a timer event if correlation over time is enabled.
 */
@Entity
@Table(name = "ProcessInstance")
public class SushiProcessInstance extends Persistable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int ID;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	private List<SushiEvent> events;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="MapTreeID")
	private SushiMapTree<String, Serializable> correlationAttributesAndValues;
	
	@OneToOne(cascade=CascadeType.MERGE)
	private SushiEvent timerEvent;
	
	@Column(name="progress")
	private int progress;
	
	/**
	 * JPA-default constructor.
	 */
	public SushiProcessInstance() {
		this.ID = 0;
		this.events = new ArrayList<SushiEvent>();
		this.correlationAttributesAndValues = new SushiMapTree<String, Serializable>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param correlationAttributesAndValues map of attribute keys and values - 
	 * key must be the expression of the attribute (e.g. location for a root attribute, 
	 * vehicle_information.railway for a second level attribute with vehicle_information as parent)
	 */
	public SushiProcessInstance(SushiMapTree<String, Serializable> correlationAttributesAndValues) {
		this();
		this.correlationAttributesAndValues = correlationAttributesAndValues;
	}
	
	/**
	 * Returns the associated {@link SushiEvent}s.
	 * @return
	 */
	public List<SushiEvent> getEvents() {
		return events;
	}

	/**
	 * Sets the associated {@link SushiEvent}s.
	 * @return
	 */
	public void setEvents(List<SushiEvent> events) {
		this.events = events;
	}
	
	/**
	 * Adds an {@link SushiEvent} to the associated events.
	 * @return
	 */
	public boolean addEvent(SushiEvent event) {
		if(!events.contains(event)){
			return events.add(event);
		}
		return false;
	}

	/**
	 * Removes an {@link SushiEvent} from the associated events.
	 * @return
	 */
	public boolean removeEvent(SushiEvent event) {
		return events.remove(event);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SushiMapTree<String, Serializable> getCorrelationAttributesAndValues() {
		return correlationAttributesAndValues;
	}

	public void setCorrelationAttributesAndValues(SushiMapTree<String, Serializable> correlationAttributesAndValues) {
		this.correlationAttributesAndValues = correlationAttributesAndValues;
	}
	
	/**
	 * Returns the {@link SushiProcess} for this instance of it from the database.
	 * @return
	 */
	public SushiProcess getProcess() {
		List<SushiProcess> processes = SushiProcess.findByProcessInstanceID(ID);
		if(!processes.isEmpty()){
			return SushiProcess.findByProcessInstanceID(ID).get(0);
		}
		return null;
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}s from the database.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findAll() {
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM ProcessInstance", SushiProcessInstance.class);
		return q.getResultList();
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}s from the database, which have the given correlation attribute.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByCorrelationAttribute(String correlationAttribute) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE MapTreeID IN (" +
					"SELECT SushiMapTree_SushiMapID " + 
					"FROM SushiMapTree_SushiMapTreeRootElements " +
					"WHERE treeRootElements_ID IN (" +
						"SELECT ID " +
						"FROM SushiMapElement " + 
						"WHERE MapKey = '" + correlationAttribute + "'))", SushiProcessInstance.class);
//					"Select Id " +
//					"FROM CorrelationAttributesAndValues " +
//					"WHERE CorrelationAttribute = '" + correlationAttribute + "')", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}s from the database, which have the given correlation attribute and associate value.
	 * @return
	 */
	public static List<SushiProcessInstance> findByCorrelationAttributeAndValue(String correlationAttribute, Serializable correlationValue) {
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"Select * FROM ProcessInstance " +
				"WHERE MapTreeID IN (" +
					"SELECT SushiMapTree_SushiMapID " + 
					"FROM SushiMapTree_SushiMapTreeRootElements " +
					"WHERE treeRootElements_ID IN (" +
						"SELECT ID " +
						"FROM SushiMapElement " + 
						"WHERE MapKey = '" + correlationAttribute + "' AND MapValue = '" + correlationValue + "'))", SushiProcessInstance.class);
//					"Select Id " +
//					"FROM CorrelationAttributesAndValues " +
//					"WHERE CorrelationAttribute = '" + correlationAttribute + "')", SushiProcessInstance.class);
		List<SushiProcessInstance> returnList= new ArrayList<SushiProcessInstance>();
		for (Object instance : query.getResultList()) {
			SushiProcessInstance processInstance = (SushiProcessInstance) instance;
			if (processInstance.getCorrelationAttributesAndValues().get(correlationAttribute).equals(correlationValue))
				returnList.add(processInstance);
		}
		return returnList;
	}
	
	/**
	 * Returns the {@link SushiProcessInstance} from the database, which has the given ID, if any.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SushiProcessInstance findByID(int ID){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID = '" + ID + "'", SushiProcessInstance.class);
		List<SushiProcessInstance> processInstances = query.getResultList();
		if(!processInstances.isEmpty()){
			return processInstances.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByIDGreaterThan(int ID){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID > '" + ID + "'", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByIDLessThan(int ID){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID < '" + ID + "'", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}es from the database, which contain the given {@link SushiEvent}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByContainedEvent(SushiEvent event){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID IN (" +
					"Select processInstances_ID " +
					"FROM ProcessInstance_Event " +
					"WHERE events_ID = '" + event.getID()+ "')", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}es from the database, which are associated to the given {@link SushiProcess}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByProcess(SushiProcess process){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"Select * " +
				"FROM ProcessInstance " +
				"WHERE ID IN (" +
					"Select processInstances_ID " +
					"FROM Process_ProcessInstance " +
					"WHERE SushiProcess_ID = '" + process.getID()+ "')", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	/**
	 * Returns all {@link SushiProcessInstance}es from the database, which contain the given {@link SushiEventType}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiProcessInstance> findByContainedEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
			"SELECT * " +
			"FROM ProcessInstance " +
			"WHERE ID IN (" +
				"SELECT processInstances_ID " +
				"FROM ProcessInstance_Event " +
				"WHERE events_ID IN (" +
					"SELECT ID " +
					"FROM Event " +
					"WHERE EVENTTYPE_ID = '" + eventType.getID() + "'))", SushiProcessInstance.class);
		return query.getResultList();
	}

	@Override
	public SushiProcessInstance save() {
		return (SushiProcessInstance) super.save();
	}
	
	public static boolean save(ArrayList<SushiProcessInstance> processInstances) {
		try {
			Persistor.getEntityManager().getTransaction().begin();
			for (SushiProcessInstance processInstance : processInstances) {
				Persistor.getEntityManager().persist(processInstance);
			}
			Persistor.getEntityManager().getTransaction().commit();
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Deletes the specified process instance from the database.
	 * @return 
	 */
	@Override
	public SushiProcessInstance remove() {
		List<SushiEvent> events = SushiEvent.findByProcessInstance(this);
		for(SushiEvent event : events){
			event.removeProcessInstance(this);
			event.save();
		}
		List<SushiProcess> processes = SushiProcess.findByProcessInstance(this);
		for(SushiProcess process : processes){
			process.removeProcessInstance(this);
			process.save();
		}
		return (SushiProcessInstance) super.remove();
	}
	
	/**
	 * Deletes the specified eventtypes from the database.
	 * @return 
	 */
	public static boolean remove(List<SushiProcessInstance> processInstances) {
		boolean removed = true;
		List<SushiProcessInstance> processInstancesCopy = new ArrayList<SushiProcessInstance>(processInstances);
		Iterator<SushiProcessInstance> iterator = processInstancesCopy.iterator();
		while (iterator.hasNext()) {
			removed = iterator.next().remove() != null;
		}
		return removed;
	}
	
	public static void removeAll() {
		for(SushiProcessInstance actualInstance : SushiProcessInstance.findAll()){
			List<SushiProcess> processes = SushiProcess.findByProcessInstance(actualInstance);
			for(SushiProcess process : processes){
				process.removeProcessInstance(actualInstance);
				process.save();
			}
			List<SushiEvent> events = SushiEvent.findByProcessInstance(actualInstance);
			for(SushiEvent event : events){
				event.removeProcessInstance(actualInstance);
				event.save();
			}
		}
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiProcessInstance");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public SushiEvent getTimerEvent() {
		return timerEvent;
	}

	public void setTimerEvent(SushiEvent timerEvent) {
		this.timerEvent = timerEvent;
	}

	public int getProgress() {
		return this.progress;
	}
	
	public void setProgress(int percentage) {
		if(percentage > 100) percentage = 100;
		if(percentage < 0) percentage = 0;
		this.progress = percentage;
		this.merge();
	}
	
	public void addToProgress(int percentage) {
		this.progress += percentage;
		if(progress > 100) progress = 100;
		if(progress < 0) progress = 0;
		this.merge();
	}
	
	@Override
	public String toString() {
		return "Process Instance " + this.ID;
	}

}
