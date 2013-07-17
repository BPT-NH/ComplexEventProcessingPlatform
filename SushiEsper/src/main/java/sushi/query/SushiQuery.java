package sushi.query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.monitoring.QueryMonitoringPoint;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPStatementSyntaxException;
import com.espertech.esper.client.EventBean;

/**
 * encapsulate Queries for saving and logging
 */
@Entity
@Table(name = "SushiQuery")
public class SushiQuery extends Persistable {

	private static final long serialVersionUID = 1L;

	public static final int maxContentSize = 2097152; //2Mb
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	@Temporal(TemporalType.TIMESTAMP)
	Date timestamp = null;

	@Column(name = "TITLE", unique=true)
	protected String title;

	@Column(name = "QUERYSTRING", length=15000) // not unique anymore cause (errno 150) Specified key was too long; max key length is 767 bytes
	private String queryString;

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private SushiQueryTypeEnum type;

	@ElementCollection
	@Column(name ="QueryLogs", length=15000)
	private List<String> log;

	/**
	 * Default-Constructor for JPA.
	 */
	public SushiQuery() {
		this.ID = 0;
		this.title = "";
		this.queryString = "";
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.log = new ArrayList<String>();
	}

	public SushiQuery(String title, String queryString, SushiQueryTypeEnum type) {
		this();
		this.title = title;
		this.queryString = queryString;
		this.type = type;
	}

	public SushiQuery(String title, String queryString, SushiQueryTypeEnum type, Timestamp timestamp) {
		this(title, queryString, type);
		this.timestamp = timestamp;
	}

	//Getter and Setter
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public SushiQueryTypeEnum getType(){
		return this.type;
	}

	public boolean isLiveQuery(){
		return this.type == SushiQueryTypeEnum.LIVE;
	}

	public boolean isOnDemanQuery(){
		return this.type == SushiQueryTypeEnum.ONDEMAND;
	}

	public List<String> getLog(){
		return log;
	}

	public void addEntryToLog(String logentry){
		log.add(logentry);
		this.merge();
	}

	public String toString() {
		return this.title + "(" + this.ID + ")";
	}
	
	/**
	 * Executes the query and returns the result.
	 * This only works for on-demand queries.
	 * Live queries will return null 
	 * @return
	 */
	public String execute(){
		SushiStreamProcessingAdapter sushiEsper = SushiStreamProcessingAdapter.getInstance();
		if (isLiveQuery()) return null;
		EPOnDemandQueryResult result = null;
		result = sushiEsper.getEsperRuntime().executeQuery(queryString);

		//print results
		StringBuffer buffer = new StringBuffer();
		Iterator<EventBean> i;
		i = result.iterator();
		while(i.hasNext()){
			EventBean next = i.next();
			buffer.append(next.getUnderlying() + System.getProperty("line.separator"));
		}
		buffer.append("Number of events found: " + result.getArray().length);
		return buffer.toString();
	}

	/**
	 * register query to SushiEsper if the query is a live query
	 * @return
	 */
	public SushiLiveQueryListener addToEsper(){
		return SushiStreamProcessingAdapter.getInstance().addLiveQuery(this);
	}

	/**
	 * checks the syntax of on-demand queries.
	 * @throws EPStatementSyntaxException
	 */
	public void validate() throws EPStatementSyntaxException {
		SushiStreamProcessingAdapter.getInstance().getEsperRuntime().prepareQuery(queryString);
	}

	//JPA-Methods
	
	/**
	 * search query with the title in the database and returns it
	 * @param title
	 * @return
	 */
	public static SushiQuery findQueryByTitle(String title){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM SushiQuery WHERE Title = '" + title + "'", SushiQuery.class);
		try {
			return (SushiQuery) query.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * returns all livequeries on the database
	 * @return
	 */
	public static List<SushiQuery> getAllLiveQueries(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM SushiQuery", SushiQuery.class);
		List<SushiQuery> liveQueryList = new ArrayList<SushiQuery>();
		try {
			for(int i = 0; i < query.getResultList().size(); i++){
				liveQueryList.add((SushiQuery) query.getResultList().get(i));
			}
			return liveQueryList;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * delete query which has the title
	 * @param title
	 * @return
	 */
	public static SushiQuery removeQueryWithTitle(String title){
		return findQueryByTitle(title).remove();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getAllTitlesOfOnDemandQueries(){
		EntityManager em = Persistor.getEntityManager();
		System.out.println("select TITLE from SushiQuery where type = '"+ SushiQueryTypeEnum.ONDEMAND +"'");
		Query query = em.createNativeQuery("select TITLE from SushiQuery where type = '"+ SushiQueryTypeEnum.ONDEMAND +"'");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getAllTitlesOfQueries(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("select TITLE from SushiQuery");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SushiNotificationRuleForQuery> findNotificationForQuery(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("select * from SushiNotificationRule WHERE Disc = 'Q' AND QUERY_ID = '" + this.getID() + "'", SushiNotificationRuleForQuery.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAllTitlesOfLiveQueries(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("select TITLE from SushiQuery where type = 'LIVE'");
		return query.getResultList();
	}

	@Override
	public SushiQuery save() {
		return (SushiQuery) super.save();
	}
	
	public static ArrayList<SushiQuery> save(ArrayList<SushiQuery> queries) {
		try {
			Persistor.getEntityManager().getTransaction().begin();
			for (SushiQuery query : queries) {
				Persistor.getEntityManager().persist(query);
			}
			Persistor.getEntityManager().getTransaction().commit();
			return queries;
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean remove(ArrayList<SushiQuery> queries) {
		boolean removed = true;
		for(SushiQuery query : queries){
			removed = (query.remove() != null);
		}
		return removed;
	}
	
	@Override
	public SushiQuery remove() {
		//remove NotificationRules
		for (SushiNotificationRuleForQuery notification : this.findNotificationForQuery()) notification.remove();
		//remove MonitoringPoints
		for (QueryMonitoringPoint point: QueryMonitoringPoint.findByQuery(this)) point.remove();
		return (SushiQuery) super.remove();
	}
	
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiQuery");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}