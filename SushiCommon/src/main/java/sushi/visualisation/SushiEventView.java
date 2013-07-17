package sushi.visualisation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * This class represents an event view.
 * It saves the information needed to visualizes the occurrence of events of certain event types in a certain period of time.
 */
@Entity
@Table(name = "SushiEventView")
public class SushiEventView extends Persistable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@ManyToMany(fetch=FetchType.EAGER)
	List<SushiEventType> eventTypes;

	@Column(name = "timeRadius")
	@Enumerated(EnumType.STRING)
	private SushiTimePeriodEnum timePeriod;

	@ManyToOne
	private SushiUser user;
	
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Default-Constructor for JPA.
	 */
	private SushiEventView() {
	}
	
	/**
	 * Creates an event view.
	 * @param user
	 * @param eventTypes
	 * @param timePeriod
	 */
	public SushiEventView(SushiUser user, ArrayList<SushiEventType> eventTypes, SushiTimePeriodEnum timePeriod) {
		this.user = user;
		this.eventTypes = eventTypes;
		this.timePeriod = timePeriod;
	}

	//Getter and Setter
	
	public List<SushiEventType> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(List<SushiEventType> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public SushiTimePeriodEnum getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(SushiTimePeriodEnum timePeriod) {
		this.timePeriod = timePeriod;
	}

	public SushiUser getUser() {
		return user;
	}

	public void setUser(SushiUser user) {
		this.user = user;
	}

	//JPA-Methods
	
	/**
	 * Finds all event views form the database.
	 * @return all event views
	 */
	public static List<SushiEventView> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiEventView t");
		return q.getResultList();
	}
	
	/**
	 * Finds all event views that match a certain condition.
	 * @param columnName
	 * @param value
	 * @return event views that matches the condition
	 */
	private static List<SushiEventView> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiEventView WHERE " + columnName + " = '" + value + "'", SushiEventView.class);
		return query.getResultList();
	}

	/**
	 * Finds all event views that contain a certain event type.
	 * @param eventType
	 * @return event views that contain a certain event type
	 */
	public static List<SushiEventView> findByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiEventView WHERE ID IN ( SELECT SushiEventView_ID FROM SushiEventView_EventType WHERE eventTypes_ID  = '" + eventType.getID() + "' )", SushiEventView.class);
		return query.getResultList();
	}

	/**
	 * Finds an event view by ID from database.
	 * @param ID
	 * @return event view
	 */
	public static SushiEventView findByID(int ID){
		List<SushiEventView> list = SushiEventView.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Deletes all event views from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiEventView");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
