package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public class SignavioExclusiveGateway extends AbstractSignavioXMLGateway{

	public SignavioExclusiveGateway(String resourceId) {
		super(resourceId);
	}

	@Override
	protected String createStencilId() {
		return "Exclusive_Databased_Gateway";
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = super.createPropertiesMap();
		properties.put("gatewaytype", "XOR");
		properties.put("xortype", "Data");
		
		properties.put("markervisible", "true");
		properties.put("defaultgate", "");
		properties.put("gate_outgoingsequenceflow", "");
		properties.put("gate_assignments", "");
		
		return properties;
	}

}
