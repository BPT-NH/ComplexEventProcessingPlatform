package signavio.xml.converter;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sushi.xml.importer.XMLParser;

public class SignavioSequenceFlow extends AbstractSignavioXMLElement{

	private String target;
	private AbstractSignavioXMLElement predecessor, sucessor;
	
	public SignavioSequenceFlow(String resourceId) {
		super(resourceId);
		target = "";
	}

	@Override
	protected String createStencilId() {
		return "SequenceFlow";
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("conditiontype", "None");
		properties.put("conditiontype", "None");
		properties.put("conditionexpression", "");
		properties.put("isimmediate", "");
		properties.put("showdiamondmarker", "");
		properties.put("bordercolor", "#000000");
		return properties;
	}
	
	@Override
	public void getBoundsFromXMLNode(Node elementNode) {
		
		List<Node> relevantChilds = XMLParser.getAllChildWithNameFromNode("omgdi:waypoint", elementNode);
		double x, y;
		lower_right_x = 0;
		lower_right_y = 0;
		upper_left_x = 1485;
		upper_left_y = 1050;
		for(Node waypointNode : relevantChilds){
			x = Double.parseDouble(waypointNode.getAttributes().getNamedItem("x").getNodeValue());
			y = Double.parseDouble(waypointNode.getAttributes().getNamedItem("y").getNodeValue());
			addDocker(x, y);
			if(x > lower_right_x) lower_right_x = x;
			if(x < upper_left_x) upper_left_x = x;
			if(y > lower_right_y) lower_right_y = y;
			if(y > upper_left_y) upper_left_y = y;
		}
		addBounds(upper_left_x, upper_left_y, lower_right_x, lower_right_y);
	}
	
	@Override
	public void addOutgoing(String outgoingId){
		super.addOutgoing(outgoingId);
		this.target = outgoingId;
	}
	
	@Override
	protected String generateAdditionalString(){
		return ",\"target\":{\"resourceId\":\"" + target + "\"}";
	}

	public AbstractSignavioXMLElement getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(AbstractSignavioXMLElement predecessor) {
		this.predecessor = predecessor;
	}

	public AbstractSignavioXMLElement getSucessor() {
		return sucessor;
	}

	public void setSucessor(AbstractSignavioXMLElement sucessor) {
		this.sucessor = sucessor;
	}
	
	@Override
	protected String generateDockersString() {
		if(predecessor != null){
			dockers.set(0, predecessor.generateRelativeDockerFromPoint(dockers.get(0)));
		}
		if(sucessor != null){
			int lastIndex = dockers.size() -1;
			dockers.set(lastIndex, sucessor.generateRelativeDockerFromPoint(dockers.get(lastIndex)));
		}
		return super.generateDockersString();
	}
}
