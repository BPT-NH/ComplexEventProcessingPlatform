package sushi.esper;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;

/**
 * This class is a helper class providing methods to useful for the communication with esper
 */
public class EsperUtils {
	
	/**
	 * Translates a java datatype to an XPath-Constants datatype. 
	 * @param clazz
	 * @return QName
	 */
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
	
	/**
	 * Translates a java class to a cast-String
	 * @param clazz
	 * @return cast-String
	 */
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
	
	/**
	 * Transforms a sushi event to an xml-dom event.
	 * @param eventType
	 * @return esper formed xml-dom event
	 */
	public static ConfigurationEventTypeXMLDOM eventTypeToXMLDom(SushiEventType eventType) {
		SushiAttributeTree tree = eventType.getValueTypeTree();
		ConfigurationEventTypeXMLDOM dom = new ConfigurationEventTypeXMLDOM();
		dom.setRootElementName(eventType.getTypeName());
		dom.addXPathProperty("Timestamp", "/" + eventType.getTypeName() + "/Timestamp", XPathConstants.STRING, "java.util.Date");
		dom.addXPathProperty("ProcessInstances", "/" + eventType.getTypeName() + "/" + "ProcessInstances", XPathConstants.STRING, "java.util.List");
		
		for (SushiAttribute element: tree.getAttributes()) {
			SushiAttributeTypeEnum attType = element.getType();
			System.out.println(element.toString());
			if (attType == SushiAttributeTypeEnum.DATE) {
				dom.addXPathProperty(element.getAttributeExpression(), "/" +  eventType.getTypeName() + element.getXPath(), XPathConstants.STRING, "java.util.Date");
			}
			else if (attType == SushiAttributeTypeEnum.INTEGER) {
				dom.addXPathProperty(element.getAttributeExpression(), "/" + eventType.getTypeName() + element.getXPath(), XPathConstants.NUMBER, "int");
			}
			else {
				dom.addXPathProperty(element.getAttributeExpression(), "/" + eventType.getTypeName() + element.getXPath(), XPathConstants.STRING);
			}
		}
		return dom;
	}

}
