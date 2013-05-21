package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public class SignavioStartEvent extends AbstractSignavioXMLElement{

	
	
	private String stencilId;

	public SignavioStartEvent(String resourceId) {
		super(resourceId);
		stencilId = "StartNoneEvent";
	}

	@Override
	protected String createStencilId() {
		return stencilId;
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("dataoutputassociations", "");
		properties.put("dataoutput", "");
		properties.put("outputset", "");
		properties.put("bgcolor", "");
		properties.put("bordercolor", "");
		properties.put("processid", "");
		properties.put("trigger", "None");
		return properties;
	}

	@Override
	protected String generateAdditionalString() {
		// TODO Auto-generated method stub
		return "";
	}
	
	public void setMessageEvent(){
		stencilId = "StartMessageEvent";
		properties.put("messagename", "");
		properties.put("operationname", "");
		properties.put("trigger", "Message");
		otherProperties.put("isinterrupting", true);
	}
	
	public void setTimerEvent(){
		stencilId = "StartTimerEvent";
	}
	
	public void setEscalationEvent(){
		stencilId = "StartEscalationEvent";
	}
	
	public void setConditionalEvent(){
		stencilId = "StartConditionalEvent";
	}
	
	public void setErrorEvent(){
		stencilId = "StartErrorEvent";
	}
	
	public void setCompensateEvent(){
		stencilId = "StartCompensationEvent";
	}
	
	public void setSignalEvent(){
		stencilId = "StartSignalEvent";
	}
	//TODO: das wird wahrscheinlich nicht alles sein

}
