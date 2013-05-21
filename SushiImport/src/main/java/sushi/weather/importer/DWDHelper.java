package sushi.weather.importer;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DWDHelper {
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		System.out.println(getAreasAndCoordinatesFromXMLFile());
	}

	/**
	 *  returns a hashmap of the min/max lat/lon of each areadesc in the xml file
	 *  {"Berlin" => {"maxLat" => "50.2", "minLat" => "10.2", "maxLon"=> ...}, "Hamburg" => {...}} 
	 */
	public static HashMap<String, HashMap<String, String>> getAreasAndCoordinatesFromXMLFile() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		return parseCoordinatesXMLFile();
	}
	
	/**
	 * parses the xml file Coordinates_DWD_Regions.xml to get for each location the max/min lat/lon values 
	 */
	public static HashMap<String, HashMap<String, String>> parseCoordinatesXMLFile() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		// key = areaname, value = {maxLat => Value, maxLon => Value, minLat => Value, minLon => Value}
		HashMap<String, HashMap<String, String>> coordinatesPerArea= new HashMap<String, HashMap<String, String>>();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); 
	    DocumentBuilder builder = domFactory.newDocumentBuilder();
	    Document doc = builder.parse(System.getProperty("user.dir")+"/src/main/resources/Coordinates_DWD_Regions.xml");
	    XPath xpath = XPathFactory.newInstance().newXPath();
	       // XPath Query for showing all nodes value
	    XPathExpression exprArea = xpath.compile("/areas/area");
	    XPathExpression exprChilds = xpath.compile("./*");

	    // result = each area node
	    Object result = exprArea.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    for (int i = 0; i < nodes.getLength(); i++) {
	    		// childs = [areadesc, polygon]
	    		NodeList childs = (NodeList) exprChilds.evaluate(nodes.item(i), XPathConstants.NODESET);
	    		HashMap<String, String> minMaxLatLon = getMinMaxLatLon(childs.item(1).getTextContent());
	    		// areadesc => {minMaxHash}
	    		coordinatesPerArea.put(childs.item(0).getTextContent(), minMaxLatLon);
	    }
	    return coordinatesPerArea;
	}
	
	/**
	 * parses the polygon string from the xml file to get the max/min lat/lon
	 * keys: "maxLat", "minLat", "maxLon", "minLon"  
	 */
	public static HashMap<String, String> getMinMaxLatLon(String polygon){
		HashMap<String, String> maxMinLatLon = new HashMap<String, String>();
		Float maxLat = Float.MIN_VALUE;
		Float maxLon = Float.MIN_VALUE;
		Float minLat = Float.MAX_VALUE;
		Float minLon = Float.MAX_VALUE;
		String[] splittetPolygon = polygon.split("\\s"); // split by non character symbols
		for (String latLon : splittetPolygon){
			String[] splitLatLon = latLon.split(",");
			// check if there are more than 2 elements
			if (splitLatLon.length != 2) continue;
			float lat = Float.parseFloat(splitLatLon[0]);
			float lon = Float.parseFloat(splitLatLon[1]);
			maxLat = (maxLat < lat)? lat : maxLat;
			minLat = (minLat > lat)? lat : minLat;
			maxLon = (maxLon < lon)? lon : maxLon;
			minLon = (minLon > lon)? lon : minLon;
		}
		maxMinLatLon.put("maxLat", String.valueOf(maxLat));
		maxMinLatLon.put("minLat", String.valueOf(minLat));
		maxMinLatLon.put("maxLon", String.valueOf(maxLon));
		maxMinLatLon.put("minLon", String.valueOf(minLon));
		return maxMinLatLon;
	}
}