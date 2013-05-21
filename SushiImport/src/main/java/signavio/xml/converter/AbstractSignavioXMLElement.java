package signavio.xml.converter;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import sushi.xml.importer.XMLParser;

public abstract class AbstractSignavioXMLElement {

	private String resourceId;
	protected Map<String, String> properties;
	protected Map<String, Object> otherProperties;
	protected List<AbstractSignavioXMLElement> childshapes; 
	protected List<String> outgoing;
	protected double lower_right_x, lower_right_y, upper_left_x, upper_left_y;
	protected List<Point2D.Double> dockers;

	public AbstractSignavioXMLElement(String resourceId){
			setResourceId(resourceId);
			otherProperties = new HashMap<String, Object>();
			properties = createPropertiesMap();
			childshapes = new ArrayList<AbstractSignavioXMLElement>();
			outgoing = new ArrayList<String>();
			dockers = new ArrayList<Point2D.Double>();
	}
	
	protected abstract String createStencilId();

	protected abstract Map<String, String> createPropertiesMap();
	

	protected String generateAdditionalString(){
		return "";
	}
	
	public String generateSignavioXMLString(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
	//	"ressourceId": "x"
		sb.append("\"resourceId\"" + ":\"" + getResourceId() + "\",");
		sb.append(generatePropertiesString());
	//	stencil:{
		sb.append("\"stencil\"" + ":{\"id\":\"" + createStencilId() + "\"}, ");
		sb.append(generateChildShapesString());
		sb.append(generateOutgoingString());
		sb.append(generateBoundsString());
		sb.append(generateDockersString());
		sb.append(generateAdditionalString());
		sb.append("}");
		return sb.toString();
	}

	protected String generateDockersString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"dockers\":[");
		for(Point2D.Double point : dockers){
			sb.append("{\"x\":");
			sb.append(point.x);
			sb.append(",\"y\":");
			sb.append(point.y);
			sb.append("},");
		}
		sb.append("]");
		return sb.toString();
	}
	
	protected String generateBoundsString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"bounds\":{");
			sb.append("\"lowerRight\":{");
				sb.append("\"x\":" + lower_right_x + ",");
				sb.append("\"y\":" + lower_right_y);
			sb.append("},");
			sb.append("\"upperLeft\":{");
			sb.append("\"x\":" + upper_left_x + ",");
			sb.append("\"y\":" + upper_left_y);
			sb.append("}");
		sb.append("}, ");
		return sb.toString();
	}
	
	protected String generateOutgoingString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"outgoing\":[");
		for(String outgoingID : outgoing){
			sb.append("{\"resourceId\":\"" + outgoingID + "\"},");
		}
		sb.append("], ");
		return sb.toString();
	}
	
	protected String generateChildShapesString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"childShapes\":[");
		for(AbstractSignavioXMLElement childElement : childshapes){
			sb.append(childElement.generateSignavioXMLString());
			sb.append(",");
		}
		sb.append("],");
		return sb.toString();
	}
	
	protected String generatePropertiesString() {
		StringBuffer sb = new StringBuffer();
	//	"properties":{
		sb.append("\"properties\":{");
		for(String key : properties.keySet()){
	//	"attribute":"value",	
			sb.append("\"" + key + "\":\"" + properties.get(key) + "\",");
		}
		for(String key : otherProperties.keySet()){
	//	"attribute":value,	
			sb.append("\"" + key + "\":" + otherProperties.get(key).toString() + ",");
		}
		sb.append("},");
		return sb.toString();
	}
	
	public void addBoundsWidthHeight(double upper_left_x, double upper_left_y, double width, double height){
		this.upper_left_x = upper_left_x;
		this.upper_left_y = upper_left_y;
		this.lower_right_x = upper_left_x + width;
		this.lower_right_y = upper_left_y + height;
	}
	
	public void addBounds(double upper_left_x, double upper_left_y, double lower_right_x, double lower_right_y){
		this.upper_left_x = upper_left_x;
		this.upper_left_y = upper_left_y;
		this.lower_right_x = lower_right_x;
		this.lower_right_y = lower_right_y;
	}

	public void addChildShape(AbstractSignavioXMLElement child){
		childshapes.add(child);
	}
	public List<AbstractSignavioXMLElement> getChildShapes(){
		return childshapes;
	}
	
	public void addOutgoing(String outgoingId){
		outgoing.add(outgoingId);
	}
	
	public void addPropertyValue(String key, String value){
		properties.put(key, value);
	}
	
	public void addDocker(double docker_x, double docker_y){
		Point2D.Double newPoint = new Point2D.Double(docker_x, docker_y);
		this.dockers.add(newPoint);
	}

	public String getResourceId() {
		return resourceId;
	}

	protected void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public void getBoundsFromXMLNode(Node elementNode) {
		Node boundNode = XMLParser.getFirstChildWithNameFromNode("omgdc:Bounds", elementNode);
		double x, y, height, width;
		x = Double.parseDouble(boundNode.getAttributes().getNamedItem("x").getNodeValue());
		y = Double.parseDouble(boundNode.getAttributes().getNamedItem("y").getNodeValue());
		height = Double.parseDouble(boundNode.getAttributes().getNamedItem("height").getNodeValue());
		width = Double.parseDouble(boundNode.getAttributes().getNamedItem("width").getNodeValue());
		addBoundsWidthHeight(x, y, width, height);
	}

	public Point2D.Double generateRelativeDockerFromPoint(Point2D.Double absolutePoint) {
		//TODO: muss nicht immer auf die mitte zeigen
		Point2D.Double relativePoint = new Point2D.Double();
		double half_width = (lower_right_x - upper_left_x) / 2;
		double half_height = (lower_right_y - upper_left_y) / 2;
		relativePoint.setLocation(half_width, half_height);
		return relativePoint;
	}
}
