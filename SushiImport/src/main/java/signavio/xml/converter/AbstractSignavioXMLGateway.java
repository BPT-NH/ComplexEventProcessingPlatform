package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSignavioXMLGateway extends AbstractSignavioXMLElement{


	public AbstractSignavioXMLGateway(String resourceId) {
		super(resourceId);
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("categories", "");
		
		properties.put("enable", "");
		properties.put("begin", "");
		properties.put("terminate", "");
		properties.put("skip", "");
		
		properties.put("assignements", "");
		properties.put("pool", "");
		properties.put("lanes", "");
		properties.put("gates", "");
		properties.put("gates_outgoingsequenceflow", "");
		properties.put("gates_assignments", "");
		properties.put("bgcolor", "#ffffff");
		properties.put("processid", "");
		
		properties.put("bordercolor", "#000000");
		
		return properties;
	}

}
