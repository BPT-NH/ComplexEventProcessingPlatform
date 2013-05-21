package signavio.xml.converter;

import java.util.Map;

public class SignavioParallelGateway extends AbstractSignavioXMLGateway{

	public SignavioParallelGateway(String resourceId) {
		super(resourceId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createStencilId() {
		return "ParallelGateway";
	}
	
	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = super.createPropertiesMap();
		properties.put("gatewaytype", "AND");
		return properties;
	}

}
