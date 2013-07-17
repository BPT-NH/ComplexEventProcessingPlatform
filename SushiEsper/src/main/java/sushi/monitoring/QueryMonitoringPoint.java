package sushi.monitoring;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.query.SushiQuery;

/**
 * Connects a query and a process to enable monitoring without BPMN-model.
 * This monitoring point saves a percentage that represents the percent of how far a process instance is when the query is triggered.
 */
@Entity
@Table(name = "QueryMonitoringPoint")
public class QueryMonitoringPoint extends Persistable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	protected int ID;

	@ManyToOne
	private SushiProcess process;
	
	@ManyToOne
	private SushiQuery query;
	
	@Column(name="percentage")
	private int percentage;
	
	@Column(name="isAbsolute")
	private boolean isAbsolute;
	
	/**
	 * JPA-default constructor
	 */
	public QueryMonitoringPoint() {
	}
	
	/**
	 * Creates a new query monitoring point.
	 * @param process
	 * @param query
	 * @param percentage
	 * @param isAbsolute
	 */
	public QueryMonitoringPoint(SushiProcess process, SushiQuery query, int percentage, boolean isAbsolute) {
		this.process = process;
		this.query = query;
		this.percentage = percentage;
		this.isAbsolute = isAbsolute;
	}

	/**
	 * This method is called when a query is triggered.
	 * It updates the progress of the process instance.
	 * @param instance
	 */
	public void trigger(SushiProcessInstance instance) {
		if (isAbsolute) {
			instance.setProgress(this.percentage);
			System.out.println(instance + " was updated to" + percentage + "%");
		}
		else {
			instance.addToProgress(this.percentage);
			System.out.println(instance + " was updated with " + percentage + "%");
		}
		
	}
	
	//Getter and Setter
	
	public SushiProcess getProcess() {
		return process;
	}

	public void setProcess(SushiProcess process) {
		this.process = process;
	}

	public SushiQuery getQuery() {
		return query;
	}

	public void setQuery(SushiQuery query) {
		this.query = query;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public boolean isAbsolute() {
		return isAbsolute;
	}

	public void setAbsolute(boolean isAbsolute) {
		this.isAbsolute = isAbsolute;
	}

	@Override
	public int getID() {
		return ID;
	}
	
	//JPA-Methods
	
	/**
	 * Returns all {@link QueryMonitoringPoint}s from the database.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<QueryMonitoringPoint> findAll() {
		Query q = Persistor.getEntityManager().createQuery("select t from QueryMonitoringPoint t");
		return q.getResultList();
	}
	
	/**
	 * Deletes all query monitoring points from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM QueryMonitoringPoint");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Finds a query monitoring point with a certain ID. 
	 * @param ID
	 * @return
	 */
	public static QueryMonitoringPoint findByID(int ID){
		return Persistor.getEntityManager().find(QueryMonitoringPoint.class, ID);
	}
		
	/**
	 * Finds query monitoring points that are connected with a certain query.
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<QueryMonitoringPoint> findByQuery(SushiQuery query){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM QueryMonitoringPoint WHERE QUERY_ID = '" + query.getID() + "'", QueryMonitoringPoint.class);
		return q.getResultList();
	}

}
