package sushi.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "ProcessInstance")
public class SushiProcessInstance extends Persistable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	int ID;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	List<SushiEvent> events;
	
//	@ElementCollection(fetch=FetchType.EAGER)
//	@CollectionTable(name="CorrelationAttributesAndValues", joinColumns=@JoinColumn(name="ID"))
//	@MapKeyColumn(name="CorrelationAttribute", length = 100)
//	@Column(name="CorrelationValue", length = 100)
//	private Map<String, Serializable> correlationAttributesAndValues;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="MapTreeID")
	private SushiMapTree<String, Serializable> correlationAttributesAndValues;
	
	//TODO: Sinnvoll?
	@OneToOne(cascade=CascadeType.MERGE)
	private SushiEvent timerEvent;
	
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
	
	public List<SushiEvent> getEvents() {
		return events;
	}

	public void setEvents(List<SushiEvent> events) {
		this.events = events;
	}
	
	public boolean addEvent(SushiEvent event) {
		if(!events.contains(event)){
			return events.add(event);
		}
		return false;
	}

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
	
	public SushiProcess getProcess() {
		List<SushiProcess> processes = SushiProcess.findByProcessInstanceID(ID);
		if(!processes.isEmpty()){
			return SushiProcess.findByProcessInstanceID(ID).get(0);
		}
		return null;
	}
	
	public static List<SushiProcessInstance> findAll() {
		Query q = Persistor.getEntityManager().createQuery("select t from SushiProcessInstance t");
		return q.getResultList();
	}
	
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
	
	public static List<SushiProcessInstance> findByIDGreaterThan(int ID){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID > '" + ID + "'", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	public static List<SushiProcessInstance> findByIDLessThan(int ID){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID < '" + ID + "'", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	public static List<SushiProcessInstance> findByContainedEvent(SushiEvent event){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM ProcessInstance " +
				"WHERE ID IN (" +
					"Select SushiProcessInstance_ID " +
					"FROM ProcessInstance_Event " +
					"WHERE events_ID = '" + event.getID()+ "')", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	public static List<SushiProcessInstance> findByProcess(SushiProcess process){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"Select * " +
				"FROM ProcessInstance " +
				"WHERE ID IN (" +
					"Select Id " +
					"FROM Process_ProcessInstance " +
					"WHERE SushiProcess_ID = '" + process.getID()+ "')", SushiProcessInstance.class);
		return query.getResultList();
	}
	
	public static List<SushiProcessInstance> findByContainedEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
			"SELECT * " +
			"FROM ProcessInstance " +
			"WHERE ID IN (" +
				"SELECT SushiProcessInstance_ID " +
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
		//TODO: Das per Hand zu löschen, ist irgendwie Mist
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
		List<SushiProcessInstance> processInstancesCopy = new ArrayList<>(processInstances);
		for(int i = 0; i < processInstances.size(); i++){
			removed = processInstancesCopy.get(i).remove() != null;
		}
		return removed;
	}
	
	public static void removeAll() {
		//TODO: Das per Hand zu löschen, ist irgendwie Mist
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
	
	@Override
	public String toString() {
		return "Process Instance " + this.ID;
	}

}
