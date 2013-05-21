package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public class SignavioBPMNProcess extends AbstractSignavioXMLElement {

	private String stencilset_url, stencilset_namespace, ssextensions;
	
	public SignavioBPMNProcess() {
		//Processes always have the ressourceId "canvas"
		super("canvas");
		upper_left_x = 0;
		upper_left_y = 0;
		lower_right_x = 1485;
		lower_right_y = 1050;
		stencilset_url = "/signaviocore/editor/stencilsets//bpmn2.0/bpmn2.0.json";
		stencilset_namespace = "http://b3mn.org/stencilset/bpmn2.0#";
		ssextensions = "http://oryx-editor.org/stencilsets/extensions/bpmn2.0basicsubset#";
		
		//TODO: check if stencilset, namespace and ssextensions are static
		
	}
	
	@Override
	protected String createStencilId() {
		return "BPMNDiagram";
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("version", "");
		properties.put("author", "");
		properties.put("language", "English");
		properties.put("namespaces", "");
		properties.put("targetnamespace", "");
		properties.put("expressionlanguage", "");
		properties.put("typelanguage", "");
		properties.put("creationdate", "");
		properties.put("modificationdate", "");
		properties.put("orientation", "horizontal");
		return properties;
	}
	
	@Override
	protected String generateOutgoingString(){
		//Prozesse besitzen das Attribut "outgoing" nicht
		return "";
	}
	
	@Override
	protected String generateDockersString() {
		//Prozesse besitzen das Attribut "dockers" nicht
		return "";
	}

	@Override
	protected String generateAdditionalString() {
		// Add stencilset, namespace and ssextensions
		StringBuffer sb = new StringBuffer();
		sb.append("\"stencilset\":{");
			sb.append("\"url\":\"" + stencilset_url + "\", ");
			sb.append("\"namespace\":\"" + stencilset_namespace + "\"");
		sb.append("}, ");
		sb.append("\"ssextensions\":[");
			sb.append("\"" + ssextensions + "\"");
		sb.append("]");
		return sb.toString();
	}

}
