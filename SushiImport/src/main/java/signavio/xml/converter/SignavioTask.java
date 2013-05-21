package signavio.xml.converter;

import java.util.HashMap;
import java.util.Map;

public class SignavioTask extends AbstractSignavioXMLElement{

	public SignavioTask(String resourceId) {
		super(resourceId);
		otherProperties.put("startquantity", 1);
		otherProperties.put("completionquantity", 1);
	}

	@Override
	protected String createStencilId() {
		return "Task";
	}

	@Override
	protected Map<String, String> createPropertiesMap() {
		properties = new HashMap<String, String>();
		properties.put("processid", "");
		properties.put("name", "");
		properties.put("documentation", "");
		properties.put("auditing", "");
		properties.put("monitoring", "");
		properties.put("categories", "");
		
		properties.put("isforcompensation", "");
		properties.put("assignments", "");
		properties.put("callacitivity", "");
		properties.put("tasktype", "None");
		properties.put("implementation", "webService");
		properties.put("resources", "");
		properties.put("messageref", "");
		properties.put("operationref", "None");
		properties.put("instantiate", "");
		properties.put("script", "");
		properties.put("scriptformat", "");
		properties.put("bgcolor", "#ffffcc");
		properties.put("looptype", "None");
		properties.put("testbefore", "");
		
		properties.put("loopcondition", "");
		properties.put("loopmaximum", "");
		properties.put("loopcardinality", "");
		properties.put("loopdatainput", "");
		properties.put("loopdataoutput", "");
		properties.put("inputdataitem", "");
		properties.put("outputdataitem", "");
		properties.put("behavior", "all");
		properties.put("complexbehaviordefinition", "");
		properties.put("completioncondition", "");
		properties.put("onebehavioreventref", "signal");
		properties.put("nonebehavioreventref", "signal");
		properties.put("enable", "");
		properties.put("begin", "");
		properties.put("terminate", "");
		properties.put("skip", "");
		properties.put("properties", "");
		properties.put("datainputset", "");
		properties.put("dataoutputset", "");
		properties.put("operationname", "");
		
		properties.put("inmessagename", "");
		properties.put("inmsgitemkind", "Information");
		properties.put("inmsgstructure", "");
		properties.put("inmsgimport", "");
		properties.put("inmsgiscollection", "");
		properties.put("outmessagename", "");
		properties.put("outmsgitemkind", "Information");
		properties.put("outmsgstructure", "");
		properties.put("outmsgimport", "");
		properties.put("outmsgiscollection", "");
		properties.put("bordercolor", "#000000");
		
		properties.put("processid", "");
		properties.put("trigger", "None");
		return properties;
	}

}
