package sushi.esper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import sushi.adapter.SushiTrafficAdapter;
import sushi.adapter.SushiWeatherAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiPatternQuery;
import sushi.query.SushiPatternQueryListener;
import sushi.query.SushiQuery;
import sushi.transformation.TransformationListener;
import sushi.util.XMLUtils;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.TimerControlEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;

/**
 * Adapter for the Esper event proccesing engine.
 */
@SuppressWarnings("serial")
public class SushiStreamProcessingAdapter implements Serializable {

	private static SushiStreamProcessingAdapter instance = null;
	private Configuration esperConfiguration;
	private EPServiceProviderSPI esperServiceProvider;
	private EPRuntime esperRuntime;
	private HashMap<SushiQuery, SushiLiveQueryListener> queryListeners;
	private boolean activatedTomTomAdapter = false;
	private boolean activatedWeatherAdapter = false; 
	private SushiTrafficAdapter trafficAdapter;
	private SushiWeatherAdapter weatherAdapter;

	private SushiStreamProcessingAdapter() {
		initialize();
	}
	
	public static SushiStreamProcessingAdapter getInstance() {
		//lazy initialize
		if (instance == null) {
                instance = new SushiStreamProcessingAdapter();
                instance.initializeAdapter();
            }		
		return instance;
	}
	
	public static boolean instanceIsCleared() { 
		return (instance == null);
	}
	
	public static void clearInstance() {
		if (instance != null) {
			instance.weatherAdapter.deleteQuartzJob();
			instance.trafficAdapter.deleteQuartzJob();
		}
		instance = null;
	}
	
	private void initialize() {
		esperConfiguration = new Configuration();
		esperConfiguration.addPlugInSingleRowFunction("currentDate", "sushi.esper.SushiUtils", "currentDate");
		esperConfiguration.addPlugInSingleRowFunction("formatDate", "sushi.esper.SushiUtils", "formatDate");
		esperConfiguration.addPlugInSingleRowFunction("getIntersection", "sushi.esper.SushiUtils", "getIntersection");
		esperConfiguration.addPlugInSingleRowFunction("isIntersectionNotEmpty", "sushi.esper.SushiUtils", "isIntersectionNotEmpty");
		esperConfiguration.addPlugInSingleRowFunction("integerValueFromEvent", "sushi.esper.SushiUtils", "integerValueFromEvent");
		esperConfiguration.addPlugInSingleRowFunction("doubleValueFromEvent", "sushi.esper.SushiUtils", "doubleValueFromEvent");
		esperConfiguration.addPlugInSingleRowFunction("stringValueFromEvent", "sushi.esper.SushiUtils", "stringValueFromEvent");
		esperConfiguration.addPlugInSingleRowFunction("dateValueFromEvent", "sushi.esper.SushiUtils", "dateValueFromEvent");
		esperConfiguration.addPlugInSingleRowFunction("sumFromEventList", "sushi.esper.SushiUtils", "sumFromEventList");
//		esperConfiguration.addPlugInSingleRowFunction("intersection", "sushi.esper.SushiUtils", "intersection");
		esperServiceProvider = (EPServiceProviderSPI)EPServiceProviderManager.getProvider(EPServiceProviderSPI.DEFAULT_ENGINE_URI, esperConfiguration);
		esperServiceProvider.initialize();
		esperRuntime = esperServiceProvider.getEPRuntime();
		esperRuntime.sendEvent(new TimerControlEvent(TimerControlEvent.ClockType.CLOCK_EXTERNAL));
		queryListeners = new HashMap<SushiQuery, SushiLiveQueryListener>();
		registerExistingEventTypes();
		
		List<SushiQuery> queries = SushiQuery.getAllLiveQueries();
		for(int i = 0; i < queries.size(); i++){
			addLiveQuery(queries.get(i));
		}
	}

	private void initializeAdapter() {
		this.weatherAdapter = new SushiWeatherAdapter();
		this.weatherAdapter.scheduleQuartzJob();
		this.trafficAdapter = new SushiTrafficAdapter();
		this.trafficAdapter.scheduleQuartzJob();
	}

	/**
	 * checks database for eventtypes and register them to EPP
	 * creates window for eventtyp
	 * useful if server was shut down 
	 */
	public void registerExistingEventTypes() {
		for (SushiEventType eventType: SushiEventType.findAll()) {
			System.out.println("Registered event type '" + eventType + "' from database.");
			createWindow(eventType);
		}
	}
	
	/**
	 * checks if eventtype is already registered 
	 */
	public boolean isEventType(SushiEventType eventType) {
		return esperServiceProvider.getEPAdministrator().getConfiguration().isEventTypeExists(eventType.getTypeName());
	}

	/**
	 * returns information of eventtyp from {@link EventType.getPropertyType(String)}
	 */
	public Class getEventTypeInfo(SushiEventType eventType, String attribute) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		if (type.isProperty(attribute) == false) return null;
		return type.getPropertyType(attribute);
	}
	
	/**
	 * checks if eventyp has attribut in Esper
	 * @param eventType
	 * @param attribute
	 * @return
	 */
	public Boolean eventTypeHasAttribute(SushiEventType eventType, String attribute) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		return type.isProperty(attribute);
	}

	/**
	 * returns attribute names of eventtyp
	 * @param eventType
	 * @return
	 */
	public String[] getAttributesOfEventType(SushiEventType eventType) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		return type.getPropertyNames();
	}

	/**
	 * add event
	 * @param events
	 */
	public void addEvents(List<SushiEvent> events) {
		for (SushiEvent event: events) {
			addEvent(event);
		}	
	}
	
	/**
	 * converts the SushiEvents in XMLEvents and send them to esper
	 * @param event
	 */
	public void addEvent(SushiEvent event) {
		Node node = XMLUtils.eventToNode(event);
//		XMLUtils.printDocument((Document) node);
		if (node == null) System.err.println("Event was not parseable!");
		long timeInMilliseconds = event.getTimestamp().getTime();
		this.esperRuntime.sendEvent(new CurrentTimeEvent(timeInMilliseconds));
		this.esperRuntime.sendEvent(node);
	}
	
	/**
	 * converts SushiEventtyp to XML and send it to Esper
	 * @param eventType
	 */
	public void addEventType(SushiEventType eventType) {
		if (isEventType(eventType)) return;
		ConfigurationEventTypeXMLDOM dom = EsperUtils.eventTypeToXMLDom(eventType);		
		this.esperServiceProvider.getEPAdministrator().getConfiguration().addEventType(eventType.getTypeName(), dom);
		this.createWindow(eventType);
	}
	
	/**
	 * deletes given events from event window
	 * @param events
	 */
	public void removeEvents(List<SushiEvent> events) {
		for (SushiEvent event: events) {
			removeEvent(event);
		}	
	}
	
	/**
	 * delete given event from event window
	 * @param event
	 */
	public void removeEvent(SushiEvent event) {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM " + event.getEventType().getTypeName() + "Window WHERE ");
		sb.append("(Timestamp.getTime() = " + event.getTimestamp().getTime() + ")");
		Iterator<String> iterator = event.getValues().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Serializable value = event.getValues().get(key);
			if (value instanceof String) {
				sb.append(" AND (" + key + " = '" + value + "')");
			} else if (value instanceof Date) {
				sb.append(" AND (" + key + ".getTime() = " + ((Date) value).getTime() + ")");
			} else {
				sb.append(" AND (" + key + " = " + value + ")");
			}
		}	
		this.esperRuntime.executeQuery(sb.toString());
	}
	
	/**
	 * deletes Eventype from Esper
	 * @param eventType
	 */
	public void removeEventType(SushiEventType eventType) {
		this.esperServiceProvider.getEPAdministrator().getConfiguration().removeEventType(eventType.getTypeName(), true);
	}
	
	/**
	 * returns live query with title as name
	 * @param title
	 * @return
	 */
	public SushiQuery getLiveQueryByTitle(String title){
		for (SushiQuery query : queryListeners.keySet()){
			if (query.getTitle().equals(title)){
				return query;
			}
		}
		return null;
	}

	/**
	 * registers live query to Esper and starts a listener to it
	 *  
	 * @param liveQuery
	 * @return Listener which will get notifications if live-query gets triggered
	 */
	public SushiLiveQueryListener addLiveQuery(SushiQuery liveQuery) {
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(liveQuery.getQueryString());
		SushiLiveQueryListener listener = new SushiLiveQueryListener(liveQuery);
		newStatement.addListener(listener);
		queryListeners.put(liveQuery, listener);
		return listener;
	}
	
	public SushiPatternQueryListener addPatternQuery(SushiPatternQuery sushiPatternQuery) {
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(sushiPatternQuery.getQueryString());
		SushiLiveQueryListener listener = new SushiPatternQueryListener(sushiPatternQuery);
		newStatement.addListener(listener);
		sushiPatternQuery.setEPStatement(newStatement);
		queryListeners.put(sushiPatternQuery, listener);
		return (SushiPatternQueryListener) listener;
	}
	
	public SushiPatternQueryListener updatePatternQuery(SushiPatternQuery sushiPatternQuery) {
		//Erstes altes Statement löschen
		esperServiceProvider.getEPAdministrator().getStatement(sushiPatternQuery.getEPStatement().getName()).destroy();
		
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(sushiPatternQuery.getQueryString());
		newStatement.addListener(sushiPatternQuery.getListener());
		sushiPatternQuery.setEPStatement(newStatement);
		return (SushiPatternQueryListener) sushiPatternQuery.getListener();
	}

	/**
	 * remove live query from Esper
	 * @param queryName
	 * @return
	 */
	public boolean removeLiveQuery(String queryName) {
		SushiQuery query = getLiveQueryByTitle(queryName);
		queryListeners.remove(query);
		return query.remove() != null;
	}

	/**
	 * returns names of live queries which are currently registered
	 * @return
	 */
	public List<String> getAllLiveQueryTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		for (SushiQuery query: queryListeners.keySet()){
			if(query.isLiveQuery()){
				titles.add(query.getTitle());
			}
		}
		return titles;
	}
	
	/**
	 * get names of saved ondemand queries
	 * @return
	 */
	public List<String> getAllOnDemandQueryTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		for (SushiQuery query: queryListeners.keySet()){
			if(query.isOnDemanQuery()){
				titles.add(query.getTitle());
			}
		}
		return titles;
	}

	/**
	 * returns query string of saved query 
	 * @param queryName
	 * @return
	 * @throws Exception
	 */
	public String getQueryByQueryName(String queryName) throws Exception {
		for (SushiQuery query : queryListeners.keySet()){
			if (query.getTitle() == queryName){
				return query.getQueryString();
			}
		}
		throw new Exception("NoQueryFoundError");
	}

	/**
	 * return SushiLiveQueryListener which has the given name
	 * @param queryName
	 * @return
	 */
	public SushiLiveQueryListener getListenerByName(String queryName) {
		SushiQuery query = getLiveQueryByTitle(queryName);
		return queryListeners.get(query);
	}

	/**
	 * return SushiLiveQueryListener which has the given query
	 * @param queryString
	 * @return
	 */
	public SushiLiveQueryListener getListenerByQuery(String queryString) {
		SushiQuery query;
		try {
			query = getLiveQueriesByQueryString(queryString);
			return queryListeners.get(query);
		} catch (Exception e) {
			return null;
		}
	}

	public SushiQuery getLiveQueriesByQueryString(String queryString) throws Exception {
		for (SushiQuery query : queryListeners.keySet()){
			if (query.getQueryString() == queryString){
				return query;
			}
		}
		throw new Exception("NoQueryFoundError");
	}

	/**
	 * creates window in esper for the given eventtype
	 * @param eventType
	 */
	public void createWindow(SushiEventType eventType) {
		if (!isEventType(eventType)) {
			addEventType(eventType);
			return;
		}
		if (hasWindow(eventType.getTypeName() + "Window")) return;
		esperServiceProvider.getEPAdministrator().createEPL("CREATE WINDOW " + eventType.getTypeName() + "Window.win:keepall() AS " + eventType.getTypeName() );
		esperServiceProvider.getEPAdministrator().createEPL("INSERT INTO " + eventType.getTypeName() + "Window SELECT * FROM " + eventType.getTypeName());
	}

	public EPRuntime getEsperRuntime() {
		return esperRuntime;
	}

	public void setEsperRuntime(EPRuntime esperRuntime) {
		this.esperRuntime = esperRuntime;
	}

	/**
	 * return the names of the current active windows
	 * @return
	 */
	public String[] getWindowNames() {
		String[] names = esperServiceProvider.getNamedWindowService().getNamedWindows();
		return names;
	}

	/**
	 * checks if window already exists
	 * @param windowName
	 * @return
	 */
	public boolean hasWindow(String windowName){
		return esperServiceProvider.getNamedWindowService().isNamedWindow(windowName);
	}

	public EPAdministrator getEsperAdministrator() {
		return esperServiceProvider.getEPAdministrator();
	}

	public boolean isActivatedTomTomAdapter() {
		return activatedTomTomAdapter;
	}

	public void setActivatedTomTomAdapter(boolean activatedTomTomAdapter) {
		this.activatedTomTomAdapter = activatedTomTomAdapter;
	}

	public boolean isActivatedWeatherAdapter() {
		return activatedWeatherAdapter;
	}

	public void setActivatedWeatherAdapter(boolean activatedWeatherAdapter) {
		this.activatedWeatherAdapter = activatedWeatherAdapter;
	}

	public TransformationListener createTransformationListener(SushiEventType eventType) {
		return new TransformationListener(eventType);
	}

	public EPStatement createStatement(String query, String name) {
		return getEsperAdministrator().createEPL(query, name);
	}

	public EPStatement getStatement(String name) {
		return getEsperAdministrator().getStatement(name);
	}
}