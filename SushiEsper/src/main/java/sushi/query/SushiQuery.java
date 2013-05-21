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

import sushi.esper.SushiEsper;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPStatementSyntaxException;
import com.espertech.esper.client.EventBean;

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
		this.save();
	}

	public String execute(SushiEsper sushiEsper){
		if (isLiveQuery()) return null;
		EPOnDemandQueryResult result = null;
		result = sushiEsper.getEsperRuntime().executeQuery(queryString);

		//print results
		StringBuffer buffer = new StringBuffer();
		Iterator<EventBean> i;
		i = result.iterator();
		while(i.hasNext()){
			EventBean next = i.next();
			Object resultObject = next.getUnderlying();
			buffer.append(next.getUnderlying() + System.getProperty("line.separator"));
		}
		buffer.append("Number of events found: " + result.getArray().length);
		return buffer.toString();
	}

	public SushiLiveQueryListener addToEsper(SushiEsper sushiEsper){
		return sushiEsper.addLiveQuery(this);
	}

	public void validate(EPRuntime epRuntime) throws EPStatementSyntaxException {
		epRuntime.prepareQuery(queryString);
	}

	public static SushiQuery findQueryByTitle(String title){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("SELECT * FROM SushiQuery WHERE Title = '" + title + "'", SushiQuery.class);
		try {
			return (SushiQuery) query.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
//	public static SushiQuery findLiveQueryByTitle(String title, QueryTypeEnum type){
//		EntityManager em = Persistor.getEntityManager();
//		Query query = em.createNativeQuery("SELECT * FROM SushiQuery WHERE Title = '" + title + "' AND TYPE = 'LIVE'", SushiQuery.class);
//		try {
//			return (SushiQuery) query.getResultList().get(0);
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
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

	public static SushiQuery removeQueryWithTitle(String title){
		return findQueryByTitle(title).remove();
	}

	public static List<String> getAllTitlesOfOnDemandQueries(){
		EntityManager em = Persistor.getEntityManager();
		System.out.println("select TITLE from SushiQuery where type = '"+ SushiQueryTypeEnum.ONDEMAND +"'");
		Query query = em.createNativeQuery("select TITLE from SushiQuery where type = '"+ SushiQueryTypeEnum.ONDEMAND +"'");
		return query.getResultList();
	}

	public static List<String> getAllTitlesOfQueries(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("select TITLE from SushiQuery");
		return query.getResultList();
	}

	
	public static List<String> getAllTitlesOfLiveQueries(){
		EntityManager em = Persistor.getEntityManager();
		Query query = em.createNativeQuery("select TITLE from SushiQuery where type = 'LIVE'");
		return query.getResultList();
	}

	// save
	
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
	
	// remove
	
	@Override
	public SushiQuery remove() {
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