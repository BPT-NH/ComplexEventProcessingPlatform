package sushi.traffic.importer.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.collection.SushiMapTree;
import sushi.traffic.importer.TomTomHelper;
import sushi.traffic.importer.TomTomTrafficimporter;

public class TestTrafficImporter {
	
	private String traffic_2poi = "tomtom/traffic_2poi.json";
	private String traffic_2poi_both_cpoi = "tomtom/traffic_2poi_both_cpoi.json";
	private String traffic_empty = "tomtom/traffic_empty.json";
	private TomTomTrafficimporter ttti;

	@Before
	public void setUp() throws Exception {
		ttti = new TomTomTrafficimporter();
	}

	@Test
	public void testFlattening2Poi1withCpoi() throws JSONException {
		String jsonstring = getStringFromInputStream(getClass().getClassLoader().getResourceAsStream(traffic_2poi));
		JSONObject traffic_2poi_json = new JSONObject(jsonstring);
		List <JSONObject> flattenIncidents = ttti.getFlattenTrafficIncidents(traffic_2poi_json.getJSONObject("tm"));
		assertTrue("expected 6, got " + flattenIncidents.size(), flattenIncidents.size() == 6);
	}
	
	@Test
	public void testFlattening2PoiBothwithCpoi() throws JSONException {
		String jsonstring = getStringFromInputStream(getClass().getClassLoader().getResourceAsStream(traffic_2poi_both_cpoi));
		JSONObject traffic_2poi_both_cpoi_json = new JSONObject(jsonstring);
		List <JSONObject> flattenIncidents = ttti.getFlattenTrafficIncidents(traffic_2poi_both_cpoi_json.getJSONObject("tm"));
		assertTrue("expected 10, got "+ flattenIncidents.size(),flattenIncidents.size() == 10);
	}
	
	@Test
	public void testFlatteningForEmptyRespond() throws JSONException{
		String jsonstring = getStringFromInputStream(getClass().getClassLoader().getResourceAsStream(traffic_empty));
		JSONObject traffic_empty_json = new JSONObject(jsonstring);
		List <JSONObject> flattenIncidents = ttti.getFlattenTrafficIncidents(traffic_empty_json.getJSONObject("tm"));
		assertTrue(flattenIncidents.size() == 0);
	}
	
	
	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {
 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	@Test
	public void testTomTomHelper() throws IOException{
		assertTrue("StreetnumbersOfExampleUseCase should have 6 Streets",TomTomHelper.getStreetnumbersOfExampleUsecase().size() == 6);
	}
	
	@Test
	public void testFilterEvents() throws IOException{
		// event is should not be filtered away cause B109 is in the streetnumbersofexampleusecase.txt
		SushiMapTree<String, Serializable> values1 = new SushiMapTree<>();
		values1.addRootElement("startStreet", "B109");
		values1.addRootElement("endStreet", "-");
		values1.addRootElement("onRoadnumbers", "-");
		SushiEvent trafficEvent1 = new SushiEvent(ttti.getTrafficEventtype(), new Date(), values1);
		
		// event is ok, cause A10 is in the streetnumbersofexampleusecase.txt
		SushiMapTree<String, Serializable> values2 = new SushiMapTree<>();
		values2.addRootElement("startStreet", "B110");
		values2.addRootElement("endStreet", "A10");
		values2.addRootElement("onRoadnumbers", "A10");
		SushiEvent trafficEvent2 = new SushiEvent(ttti.getTrafficEventtype(), new Date(), values2);
		
		// B1 is not in the list, therefore filter it away
		SushiMapTree<String, Serializable> values3 = new SushiMapTree<>();
		values3.addRootElement("startStreet", "B1");
		values3.addRootElement("endStreet", "-");
		values3.addRootElement("onRoadnumbers", "-");
		SushiEvent trafficEvent3 = new SushiEvent(ttti.getTrafficEventtype(), new Date(), values3);
		
		ArrayList<SushiEvent> trafficevents =  new ArrayList<SushiEvent>(Arrays.asList(trafficEvent1, trafficEvent2, trafficEvent3));
		ArrayList<SushiEvent> filteredEvents = ttti.filterEvents(trafficevents);
		assertTrue("should be 2 but was " + filteredEvents.size(), filteredEvents.size() == 2);
		
		
	}
}
