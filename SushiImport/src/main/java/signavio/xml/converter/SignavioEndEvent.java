package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public class SignavioEndEvent extends AbstractSignavioXMLElement{

	public SignavioEndEvent(String resourceId) {
		super(resourceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createStencilId() {
		// TODO  außer ... <messageEventDefinition id="sid-e7d850e7-0b95-4bc9-923c-d6e69f5b8669"/>
		return "EndNoneEvent";
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("datainputassociations", "");
		properties.put("datainput", "");
		properties.put("inputset", "");
		properties.put("bgcolor", "");
		properties.put("bordercolor", "");
		properties.put("processid", "");
		properties.put("trigger", "None");
		return properties;
	}

}
