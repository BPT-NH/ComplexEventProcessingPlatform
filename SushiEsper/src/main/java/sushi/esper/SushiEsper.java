package sushi.esper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.EventTypeRule;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.monitoring.MonitoringListener;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiPatternQueryListener;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
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

public class SushiEsper implements Serializable {

	private static SushiEsper instance = null;
	private Configuration esperConfiguration;
	private EPServiceProviderSPI esperServiceProvider;
	private EPRuntime esperRuntime;
	private HashMap<SushiQuery, SushiLiveQueryListener> queryListeners;
	private List<MonitoringListener> monitoringListeners;
	private boolean activatedTomTomAdapter = false;
	private boolean activatedWeatherAdapter = false; 

	private SushiEsper() {
		initialize();
	}
	
	public static SushiEsper getInstance() {
		//lazy initialize
		if (instance == null) {
                instance = new SushiEsper();
            }		
		return instance;
	}
	
	public static boolean instanceIsCleared() {
		return (instance == null);
	}
	
	public static void clearInstance() {
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
		monitoringListeners = new ArrayList<MonitoringListener>();
		registerExistingEventTypes();
		
		List<SushiQuery> queries = SushiQuery.getAllLiveQueries();
		for(int i = 0; i < queries.size(); i++){
			addLiveQuery(queries.get(i));
		}
	}

	public void registerExistingEventTypes() {
		for (SushiEventType eventType: SushiEventType.findAll()) {
			System.out.println("Registered event type '" + eventType + "' from database.");
			createWindow(eventType);
		}
	}

	public boolean isEventType(SushiEventType eventType) {
		return esperServiceProvider.getEPAdministrator().getConfiguration().isEventTypeExists(eventType.getTypeName());
	}

	public Class getEventTypeInfo(SushiEventType eventType, String attribute) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		if (type.isProperty(attribute) == false) return null;
		return type.getPropertyType(attribute);
	}
	
	public Boolean eventTypeHasAttribute(SushiEventType eventType, String attribute) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		return type.isProperty(attribute);
	}

	public String[] getAttributesOfEventType(SushiEventType eventType) {
		EventType type = esperServiceProvider.getEPAdministrator().getConfiguration().getEventType(eventType.getTypeName());
		return type.getPropertyNames();
	}
	
	
//	public void addNonHierarchicalEvents(List<SushiEvent> events){
//		int numberOfAddedEvents = 0;
//		for(SushiEvent event : events){
//			addNonHierarchicalEvent(event);
//			numberOfAddedEvents++;
//		}
//		System.out.println(numberOfAddedEvents + " events added to EventType: " + events.get(0).getEventType().getTypeName());
//	}
//	
//	public void addNonHierarchicalEvent(SushiEvent event) {
//		//prepare values
//		Map<String, Serializable> values = new HashMap<String, Serializable>();
//		values.putAll(event.getValues());
//		for ( Entry<String, Serializable> entry  : values.entrySet()) {
//			
//			if (entry.getKey().contains("time") | entry.getKey().contains("Time")) {
//				String key = entry.getKey().replace(" ", "");
//				Date value = new Date();
//				try {
//					value = (Date) entry.getValue();
//				} catch(Exception e) {
//				}
//				try {
//					String valueString = (String) entry.getValue();
//					Double valueDouble = Double.parseDouble(valueString);
//					value = DateUtil.getJavaDate(valueDouble,false);
//				} catch(Exception e) {
//				}
//				values.remove(entry.getKey());
//				values.put(key, value);
//				continue;
//			}
//			
//			if (entry.getKey().contains(" ")) {
//				values.remove(entry.getKey());
//				values.put(entry.getKey().replaceAll(" ", ""), entry.getValue());
//			}
//			
//		}
//		values.put("Timestamp", event.getTimestamp());
//		long timeInMilliseconds = event.getTimestamp().getTime();
//		//send to esper
//		this.esperRuntime.sendEvent(new CurrentTimeEvent(timeInMilliseconds));
//		this.esperRuntime.sendEvent(values, event.getEventType().getTypeName());
//	}
	
	public void addEvents(List<SushiEvent> events) {
		for (SushiEvent event: events) {
			addEvent(event);
		}	
	}
	
	public void addEvent(SushiEvent event) {
		Node node = XMLUtils.eventToNode(event);
//		XMLUtils.printDocument((Document) node);
		if (node == null) System.err.println("Event was not parseable!");
		long timeInMilliseconds = event.getTimestamp().getTime();
		this.esperRuntime.sendEvent(new CurrentTimeEvent(timeInMilliseconds));
		this.esperRuntime.sendEvent(node);
	}
	
	public void addEventType(SushiEventType eventType) {
		if (isEventType(eventType)) return;
		ConfigurationEventTypeXMLDOM dom = EsperUtils.eventTypeToXMLDom(eventType);		
		this.esperServiceProvider.getEPAdministrator().getConfiguration().addEventType(eventType.getTypeName(), dom);
		this.createWindow(eventType);
	}
	
	public void removeEvents(List<SushiEvent> events) {
		for (SushiEvent event: events) {
			removeEvent(event);
		}	
	}
	
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
	
	public void removeEventType(SushiEventType eventType) {
		this.esperServiceProvider.getEPAdministrator().getConfiguration().removeEventType(eventType.getTypeName(), true);
	}
	
	public void addEventsForEventType(EventTypeRule eventTypeRule) {
		ArrayList<SushiEvent> newEvents = eventTypeRule.findEventsForEventType();
		for (SushiEvent event : newEvents) {
			event.setEventType(eventTypeRule.getCreatedEventType());
			addEvent(event);
		}
	}

	public SushiQuery getLiveQueryByTitle(String title){
		for (SushiQuery query : queryListeners.keySet()){
			if (query.getTitle().equals(title)){
				return query;
			}
		}
		return null;
	}

	public SushiLiveQueryListener addLiveQuery(SushiQuery liveQuery) {
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(liveQuery.getQueryString());
		SushiLiveQueryListener listener = new SushiLiveQueryListener(liveQuery);
		newStatement.addListener(listener);
		queryListeners.put(liveQuery, listener);
		return listener;
	}
	
	public SushiPatternQueryListener addPatternQuery(SushiQuery sushiPatternQuery) {
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(sushiPatternQuery.getQueryString());
		SushiLiveQueryListener listener = new SushiPatternQueryListener(sushiPatternQuery);
		newStatement.addListener(listener);
		queryListeners.put(sushiPatternQuery, listener);
		return (SushiPatternQueryListener) listener;
	}

	public boolean removeLiveQuery(String queryName) {
		SushiQuery query = getLiveQueryByTitle(queryName);
		queryListeners.remove(query);
		return query.remove() != null;
	}

	/** momentan nicht verwendet **/
	public boolean addMonitoringListener(String elementID, MonitoringPointStateTransition type, String condition) {
		String queryString = "Select * From Event WHERE " + condition;
		SushiQuery query = new SushiQuery(queryString, queryString, SushiQueryTypeEnum.LIVE);
		EPStatement newStatement = esperServiceProvider.getEPAdministrator().createEPL(queryString);
		SushiLiveQueryListener listener = new SushiLiveQueryListener(query);
		newStatement.addListener(listener);
		MonitoringListener monitoringListener = new MonitoringListener(elementID, condition, type, listener);
		monitoringListeners.add(monitoringListener);
		return true;
	}

	public List<String> getAllLiveQueryTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		for (SushiQuery query: queryListeners.keySet()){
			if(query.isLiveQuery()){
				titles.add(query.getTitle());
			}
		}
		return titles;
	}
	
	public List<String> getAllOnDemandQueryTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		for (SushiQuery query: queryListeners.keySet()){
			if(query.isOnDemanQuery()){
				titles.add(query.getTitle());
			}
		}
		return titles;
	}

	public String getQueryByQueryName(String queryName) throws Exception {
		for (SushiQuery query : queryListeners.keySet()){
			if (query.getTitle() == queryName){
				return query.getQueryString();
			}
		}
		throw new Exception("NoQueryFoundError");
	}

	public SushiLiveQueryListener getListenerByName(String queryName) {
		SushiQuery query = getLiveQueryByTitle(queryName);
		return queryListeners.get(query);
	}

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

	public List<MonitoringListener> getMonitoringListeners() {
		return monitoringListeners;
	}

	public void setMonitoringListeners(List<MonitoringListener> monitoringListeners) {
		this.monitoringListeners = monitoringListeners;
	}

	private boolean checkForm(SushiEvent event, SushiEventType eventType) {

		for (String string: event.getValues().keySet()) {
			//only first dimension checked so far
			if (!eventType.containsValue(string)) return false;
		}
		return true;
	}

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

	public String[] getWindowNames() {
		String[] names = esperServiceProvider.getNamedWindowService().getNamedWindows();
		return names;
	}

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

}
