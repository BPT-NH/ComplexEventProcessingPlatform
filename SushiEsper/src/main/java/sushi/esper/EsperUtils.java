package sushi.esper;

import java.util.List;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTree;
import sushi.event.collection.SushiTreeElement;

import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;

public class EsperUtils {
	
	public static QName mapClassToQName(SushiAttributeTypeEnum clazz) {
		if (clazz == SushiAttributeTypeEnum.STRING) {
			return XPathConstants.STRING;
		}
		if (clazz == SushiAttributeTypeEnum.INTEGER) {
			return XPathConstants.NUMBER;
		}
		if (clazz == SushiAttributeTypeEnum.DATE) {
			return DatatypeConstants.DATETIME;
		}
		return null;
	}
	
	public static String mapClassToCast(SushiAttributeTypeEnum clazz) {
		if (clazz == SushiAttributeTypeEnum.STRING) {
			return "java.lang.String";
		}
		if (clazz == SushiAttributeTypeEnum.INTEGER) {
			return "java.lang.Integer";
		}
		if (clazz == SushiAttributeTypeEnum.DATE) {
			return "java.util.Date";
		}
		return null;
	}
	
	public static ConfigurationEventTypeXMLDOM eventTypeToXMLDom(SushiEventType sushiEventType) {
		SushiAttributeTree tree = sushiEventType.getValueTypeTree();
		ConfigurationEventTypeXMLDOM dom = new ConfigurationEventTypeXMLDOM();
		dom.setRootElementName(sushiEventType.getTypeName());
//		String timestampName = sushiEventType.getTimestampName();
//		if (timestampName != null) {
			dom.addXPathProperty("Timestamp", "/" + sushiEventType.getTypeName() + "/Timestamp", XPathConstants.STRING, "java.util.Date");
//			dom.addXPathProperty("Timestamp", "/" + sushiEventType.getTypeName() + "/" + timestampName.replaceAll("\\s","_").replaceAll("[^a-zA-Z0-9_]+",""), XPathConstants.STRING, "java.util.Date");
//		}
		dom.addXPathProperty("ProcessInstances", "/" + sushiEventType.getTypeName() + "/" + "ProcessInstances", XPathConstants.STRING, "java.util.List");
		
		System.out.println(tree.getAttributes().size());
		for (SushiAttribute element: tree.getAttributes()) {
			SushiAttributeTypeEnum attType = element.getType();
			System.out.println(element.toString());
			if (attType == SushiAttributeTypeEnum.DATE) {
				dom.addXPathProperty(element.getAttributeExpression(), "/" +  sushiEventType.getTypeName() + element.getXPath(), XPathConstants.STRING, "java.util.Date");
			}
			else if (attType == SushiAttributeTypeEnum.INTEGER) {
				dom.addXPathProperty(element.getAttributeExpression(), "/" + sushiEventType.getTypeName() + element.getXPath(), XPathConstants.NUMBER, "int");
			}
			else {
				dom.addXPathProperty(element.getAttributeExpression(), "/" + sushiEventType.getTypeName() + element.getXPath(), XPathConstants.STRING);
			}
		}
		return dom;
	}


}
