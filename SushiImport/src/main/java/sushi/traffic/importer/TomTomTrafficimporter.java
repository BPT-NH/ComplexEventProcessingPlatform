package sushi.traffic.importer;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.event.collection.SushiTree;
import sushi.weather.importer.DWDHelper;
import sushi.event.collection.SushiTree;

/**
 * Adapter for traffic incidents from tomtom api 
 */
public class TomTomTrafficimporter {

	String TOMTOMAPIKEY = "INSERT YOUR API KEY";
	SushiEventType trafficEventType = null;
	
	public final static void main(String[] args) throws Exception {
		TomTomTrafficimporter t = new TomTomTrafficimporter();
		System.out.println("###############################################");
		System.out.println(t.getTrafficSushiEventHamburgToBerlin());
		System.out.println("###############################################");
	}

	/**
	 * returns list of SushiEvents which represent the trafficincidents
	 * expire time is set to 2min, but events will not be deleted
	 */
	public ArrayList<SushiEvent> getTrafficSushiEventsPotsdam() throws JSONException{
		return getTrafficSushiEventsForArea("52.355264", "12.997513", "52.430688", "13.137932", "16", "Potsdam");
	}
	
	/**
	 * returns list of SushiEvents which represent the trafficincidents
	 * expire time is set to 2min, but events will not be deleted
	 */
	public ArrayList<SushiEvent> getTrafficSushiEventsBerlin() throws JSONException{
		return getTrafficSushiEventsForArea("52.393201", "13.119049", "52.601379", "13.702698", "16", "Berlin");
	}
	
	/**
	 * returns a list of TrafficEvents from each area descriped in the DWDHelper 
	 */
	public ArrayList<SushiEvent> getTrafficSushiEventHamburgToBerlin() throws JSONException, XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		ArrayList<SushiEvent> incidents = new ArrayList<>();
		// get the areaDescriptions with their rectangle provided by deutscher wetterdienst, the same used for the dwdimporter
		HashMap<String, HashMap<String, String>> dwdAreas = DWDHelper.getAreasAndCoordinatesFromXMLFile();
		for (String areaDesc : dwdAreas.keySet()){
			HashMap<String, String> minMaxLatLon = dwdAreas.get(areaDesc);
			incidents.addAll(
					getTrafficSushiEventsForArea(minMaxLatLon.get("minLat"),
																minMaxLatLon.get("minLon"),
																minMaxLatLon.get("maxLat"),
																minMaxLatLon.get("maxLon"), 
																"16",
																areaDesc));
		}
		// remove all trafficevents which do not affect the main route
		return filterEvents(incidents);
	}
	
	/**
	 * returns the TrafficEvents which are in the given rectangle 
	 */
	public ArrayList<SushiEvent> getTrafficSushiEventsForArea(String maxLat, String maxLon, String minLat, String minLon, String zoomlevel, String areaDesc) throws JSONException{
		JSONObject mainRespondElement = getTrafficEventsJSON(maxLat, maxLon, minLat, minLon, zoomlevel);
		ArrayList<JSONObject> trafficincidents = getFlattenTrafficIncidents(mainRespondElement);
		return getEventFromJSON(trafficincidents, areaDesc);
	}
	
	/**
	 * return the mapping of incidents type to english description 
	 */
	public HashMap<String, String> getNameOfIncidentsMap(){
		HashMap<String, String> incidentsMap = new HashMap<String, String>();
		incidentsMap.put("-", "-");
		incidentsMap.put("0", "Unknown");
		incidentsMap.put("1", "Accident");
		incidentsMap.put("2", "Fog");
		incidentsMap.put("3", "Dangerous Conditions");
		incidentsMap.put("4", "Rain");
		incidentsMap.put("5", "Ice");
		incidentsMap.put("6", "Jam");
		incidentsMap.put("7", "Lane Closed");
		incidentsMap.put("8", "Road Closed");
		incidentsMap.put("9", "Road Works");
		incidentsMap.put("10", "Wind");
		incidentsMap.put("11", "Flooding");
		incidentsMap.put("12", "Detour");
		incidentsMap.put("13", "Cluster – returned if  a cluster contains incidents with different icon categories");
		return incidentsMap;
	}

	/**
	 * for internal usage / debugging 
	 */
	private JSONObject getTrafficEventsJSONPotsdam(){
		return getTrafficEventsJSON("52.355264", "12.997513", "52.430688", "13.137932", "16");
	}

	/**
	 * for internal usage / debugging 
	 */
	private JSONObject getTrafficEventsJSONBerlin(){
		return getTrafficEventsJSON("52.393201", "13.119049", "52.601379", "13.702698", "16");
	}
	
	/**
	 * return the respond element ("tm") of the traffic request send to tomtom
	 * 
	 * lat/lon = latidude/longitude from Google maps (lat is the first value)
	 * zoomfactor stands for the visible size of the map, use 10 or higher for cities and lower for rural area
	 * example for potsdam: getTraffic(52.355264, 12.997513, 52.430688, 13.137932, 11)
	 */
	private JSONObject getTrafficEventsJSON(String maxLat, String maxLon, String minLat, String minLon, String zoomlevel) {
		HttpClient httpclient = new DefaultHttpClient();
		JSONObject mainRespondElement = null;
		String requerstString = "https://api.tomtom.com/lbs/services/trafficIcons/3/s2/" +
				maxLat + "," +
				maxLon+ "," +
				minLat 	+ "," +
				minLon +"/" +
				zoomlevel +
				"/-1/json?language=de&projection=EPSG4326&expandCluster=true&key=" + TOMTOMAPIKEY;
		String responseBody = "";
        try {
            HttpGet httpget = new HttpGet(requerstString);
           System.out.println("executing request " + httpget.getURI());
            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httpget, responseHandler);
            JSONObject jsonRespond = new JSONObject(responseBody);
            mainRespondElement = jsonRespond.getJSONObject("tm");
        } catch (Exception e) {
        		System.err.println("ERROR: unexpected tomtom respond or no connection possible: " + responseBody);
        		e.printStackTrace();
        }  finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return mainRespondElement;
	}
	
	/**
	 * returns the eventtype for traffic incidents 
	 */
	public SushiEventType getTrafficEventtype(){
		// singelton
		if (trafficEventType == null){
			// try to find the event type in db
			trafficEventType = SushiEventType.findByTypeName("TomTomTrafficEvent");
			// create new if undefined
			if (trafficEventType == null){
				SushiAttributeTree valueTree = new SushiAttributeTree();
				valueTree.addRoot("expires", SushiAttributeTypeEnum.DATE);
				valueTree.addRoot("duration", SushiAttributeTypeEnum.INTEGER);
				valueTree.addRoot("length", SushiAttributeTypeEnum.INTEGER);
				valueTree.addRoot("startStreet", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("endStreet", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("onRoadnumbers", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("type", SushiAttributeTypeEnum.INTEGER);
				valueTree.addRoot("areaDesc", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("onCountry", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("xvalue", SushiAttributeTypeEnum.STRING);
				valueTree.addRoot("yvalue", SushiAttributeTypeEnum.STRING);
				trafficEventType = new SushiEventType("TomTomTrafficEvent", valueTree, "detectedAt");
			}
		} 
		return trafficEventType;
	}
	
	/**
	 * generates Events from TOMTOM JSON respond
	 * just germany
	 * location is used to name the area
	 */
	private ArrayList<SushiEvent> getEventFromJSON(ArrayList<JSONObject> trafficincidents, String areaDesc) throws JSONException {
		ArrayList<SushiEvent> sushiEvents = new ArrayList<SushiEvent>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 2);
		String newTime = df.format(cal.getTime());
		for (JSONObject incident : trafficincidents){
			SushiMapTree<String, Serializable> values = new SushiMapTree<>();
			String from = incident.has("f") ? incident.getString("f") : "-";
			String to = incident.has("t") ? incident.getString("t") : "-";
			DecimalFormat f = new DecimalFormat("#0.00"); 
			String duration = incident.has("dl") ? f.format((incident.getInt("dl")/60.0)) : "-"; 
			String type = incident.has("ic") ? incident.getString("ic") : "-";
			String length = incident.has("l") ? incident.getString("l") : "-";
			String roadNumbersAffected = incident.has("r") ? incident.getString("r") : "-";
			String xvalue = incident.has("p") ? incident.getJSONObject("p").getString("x") : "-";
			String yvalue = incident.has("p") ? incident.getJSONObject("p").getString("y") : "-";
			values.addRootElement("expires", newTime);
			values.addRootElement("duration", duration);
			values.addRootElement("length", length);
			values.addRootElement("startStreet", from);
			values.addRootElement("endStreet", to);
			values.addRootElement("onRoadnumbers", roadNumbersAffected);
			values.addRootElement("xvalue", xvalue);
			values.addRootElement("yvalue", yvalue);
			values.addRootElement("type", type); 
			values.addRootElement("areaDesc", areaDesc);
			values.addRootElement("onCountry", "germany");
			SushiEvent trafficEvent = new SushiEvent(getTrafficEventtype(), new Date(), values);
			sushiEvents.add(trafficEvent);
		}
		return sushiEvents;
	}
	
	/**
	 * generates list of JSONObjects out of JSON respond 
	 */
	public ArrayList<JSONObject> getFlattenTrafficIncidents(JSONObject mainRespondElement) throws JSONException{
		ArrayList<JSONObject> trafficIncidents = new ArrayList<JSONObject>();
		// if there is no poi element, then there are no traffic incidents on this zoomlevel or location 
		if (!mainRespondElement.has("poi")) return trafficIncidents;

		// mainRespondElement has incidents or cluster of incidents, if it has just one Cluster/incident then poi has just 1 JsonObject, otherwise it has a JsonArray of Objects
		Object poi = mainRespondElement.get("poi");
		if (poi instanceof JSONArray) {
			// there are more than one traffic incident cluster (more then 1 poi element)
			JSONArray pois = (JSONArray) poi;
			for (int i = 0; i < pois.length(); ++i){
				trafficIncidents.addAll(getFlattenTrafficIncidentsFromPoi(pois.getJSONObject(i)));
			}
		}
		else if (poi instanceof JSONObject) {
			// just one cluster (one 1 element)
			trafficIncidents.addAll(getFlattenTrafficIncidentsFromPoi((JSONObject)poi));
			poi = (JSONObject) poi;
		}
		else {
			// ERROR 
			// It's something else, like a string or number
		}
		return trafficIncidents;
	}
	
	/**
	 * filters the traffic incidents out of the poi elements of the tomtom json respond, 
	 * breaks down the hierachical representation provided by tomtom,
	 * used by getFlattenTrafficIncidents
	 */
	private ArrayList<JSONObject> getFlattenTrafficIncidentsFromPoi(JSONObject poi) throws JSONException {
		// poi elements can have nested incidents "cpois" or is itself an traffic incident, if it is nested, use just the nested incidents cause the clustering elemets has no information
		ArrayList<JSONObject> trafficIncidents = new ArrayList<JSONObject>();
		int clustersize = poi.getInt("cs"); //clustersize
		if (clustersize == 0){
			// single traffic incident, no cluster, clustersize = 0
			trafficIncidents.add(poi);
		} else if (clustersize == 1){
				// just one incident in cluster, therefore cpoi element is single JSONObject
				trafficIncidents.add(poi.getJSONObject("cpoi"));
		} else {
			// more than 1 incident in cluster, extract all, therefore cpoi element is JSONArray of JSONObjects
			JSONArray incidentsCluster = poi.getJSONArray("cpoi");
			for (int i = 0; i < incidentsCluster.length(); ++i){
				trafficIncidents.add(incidentsCluster.getJSONObject(i));					
			}
		}
		return trafficIncidents;
	}
	
	/**
	 *  removes trafficevents which do not affect example usecase/route
	 */
	public ArrayList<SushiEvent> filterEvents(ArrayList<SushiEvent> trafficEvents) throws IOException{
		ArrayList<String> useOnlyTheseStreets = TomTomHelper.getStreetnumbersOfExampleUsecase();
		ArrayList<SushiEvent> filteredEvents = new ArrayList<SushiEvent>();
		for (SushiEvent trafficEvent : trafficEvents){
			SushiMapTree<String, Serializable> map = trafficEvent.getValues();
			if (	areStreetsInSet((String)map.getValueOfAttribute("startStreet"), useOnlyTheseStreets) ||
					areStreetsInSet((String)map.getValueOfAttribute("endStreet"), useOnlyTheseStreets) ||
					areStreetsInSet((String)map.getValueOfAttribute("onRoadnumbers"), useOnlyTheseStreets)){
				filteredEvents.add(trafficEvent);
			}
		}
		return filteredEvents;
	}
	
	/**
	 * checks if the given streets string (e.g. "Berliner Straße / Neue Straße") includes a street that is in the useOnlyTheseStreets  
	 */
	private boolean areStreetsInSet(String streets, ArrayList<String> useOnlyTheseStreets){
		if (streets.equals("-")) return false;
		String[] splittedStreets = streets.split("/");
		for (String street : splittedStreets){
			for (String filterStreet : useOnlyTheseStreets){
				if (street.contains(filterStreet)) return true;
			}
		}
		return false;
	}
	
	/**
	 * prettyprints the trafficincidents
	 */
	private void prettyPrint(ArrayList<JSONObject> trafficIncidents) throws JSONException{
		int maxLengthf = 0;
		int maxLengtht = 0;
		HashMap<String, String> incidentsMap = getNameOfIncidentsMap();
				
		if (trafficIncidents.isEmpty()){
			System.out.println("no traffic Incidents");
			return;
		}
		for (JSONObject incident : trafficIncidents){
			
			if (incident.has("f") && incident.getString("f").length() > maxLengthf) maxLengthf = incident.getString("f").length();
			if (incident.has("t") && incident.getString("t").length() > maxLengtht) maxLengtht = incident.getString("t").length();
		}
		System.out.format("%-"+maxLengthf+"s%-"+maxLengtht+"s%-8s%-20s%-9s%-5s%n", "from", "to", "street#", "type", "duration", "length");
		for (JSONObject incident : trafficIncidents){
			String from = incident.has("f") ? incident.getString("f") : "-";
			String to = incident.has("t") ? incident.getString("t") : "-";
			DecimalFormat f = new DecimalFormat("#0.00"); 
			String duration = incident.has("dl") ? f.format((incident.getInt("dl")/60.0)) : "-"; 
			String type = incident.has("ic") ? incident.getString("ic") : "-";
			type = type + " (" + incidentsMap.get(type) + ")";
			String length = incident.has("l") ? incident.getString("l") : "-";
			String roadNumbersAffected = incident.has("r") ? incident.getString("r") : "-";
			System.out.format("%-"+maxLengthf+"s%-"+maxLengtht+"s%-8s%-20s%-9s%-5s%n", from, to, roadNumbersAffected, type, duration, length);
		}
	}
}