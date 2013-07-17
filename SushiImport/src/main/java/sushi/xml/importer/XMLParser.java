package sushi.xml.importer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sushi.DateUtils;
import sushi.FileUtils;
import sushi.edifact.importer.EdifactImporter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiMapTree;

/**
 * This class parses events from XML files.
 */
public class XMLParser extends AbstractXMLParser {

	private static SushiMapTree<String, Serializable> eventValueTree;

	/**
	 * Parses a single event from a XML file from the given file path.
	 * @param filePath
	 * @return
	 * @throws XMLParsingException
	 */
	public static SushiEvent generateEventFromXML(String filePath) throws XMLParsingException {
		Document doc = readXMLDocument(filePath);
		return generateEvent(doc, null);
	}
	
	/**
	 * Parses a single event from a XML file from the given file path under consideration of the given XSD.
	 * The XSD defines the contained nodes and attributes of the XML file.
	 * @param filePath
	 * @return
	 * @throws XMLParsingException
	 */
	public static SushiEvent generateEventFromXML(String filePath, String pathToXSD) throws XMLParsingException {
		Document doc = readXMLDocument(filePath);
		return generateEvent(doc, pathToXSD);
	}

	/**
	 * Parses a single event from a {@link Document}.
	 * @param filePath
	 * @return
	 * @throws XMLParsingException
	 */
	public static SushiEvent generateEventFromDoc(Document xmlDoc) throws XMLParsingException {
		return generateEvent(xmlDoc, null);
	}

	/**
	 * Parses a single event from a {@link Document} under consideration of the given XSD.
	 * The XSD defines the contained nodes and attributes of the XML file.
	 * @param filePath
	 * @return
	 * @throws XMLParsingException
	 */
	private static SushiEvent generateEvent(Document doc, String pathToXSD) throws XMLParsingException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(new XSDNameSpaceContext());
		// XPath Query for showing all nodes value
		XPathExpression rootElementExpression = null;
		try {
			rootElementExpression = xPath.compile("/./child::*");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		Object rootElementsResult = null;
		try {
			rootElementsResult = rootElementExpression.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		NodeList rootElementNodes = (NodeList) rootElementsResult;
		assert(rootElementNodes.getLength() == 1);
		Node actualRootElement = rootElementNodes.item(0);
		eventValueTree = new SushiMapTree<String, Serializable>();
		SushiEventType eventType = null;
		if (pathToXSD == null){
			String XSDName = getXSDNameFromNode(actualRootElement);
			if (XSDName == null) {
				//do stuff for getting eventType for edifact
				eventType = EdifactImporter.getInstance().getEventTypeForEdifact(doc);
			} else {
				eventType = SushiEventType.findBySchemaName(XSDName);
			}	
		} else {
			eventType = SushiEventType.findBySchemaName(FileUtils.getFileNameWithoutExtension(pathToXSD));
			if (eventType == null) eventType = XSDParser.generateEventTypeFromXSD(pathToXSD, FileUtils.getFileNameWithoutExtension(pathToXSD)); 
		}
		// eventValueTree befuellen
		generateEventTreeFromElement(actualRootElement);

		if(eventType == null){
			throw new XMLParsingException("No matching eventtype was found; please upload corresponding XSD");
		} else {
			Date eventTimestamp;
			// Timestamp richtig einlesen
			String timestampName = eventType.getTimestampNameAsXPath();
			String time = "";

			if(timestampName == null || timestampName.equals(AbstractXMLParser.CURRENT_TIMESTAMP) || timestampName.equals(AbstractXMLParser.GENERATED_TIMESTAMP_COLUMN_NAME)){
				eventTimestamp = new Date();
			} else {

				// Timestamp aus XML-Doc holen
				try {
					XPathExpression timeElementExpression = xPath.compile("/" + actualRootElement.getNodeName() + timestampName);
					Object timeElementsResult = timeElementExpression.evaluate(doc, XPathConstants.NODE);
					Node timeNode = (Node) timeElementsResult;
					time = timeNode.getTextContent();
					System.out.println(time);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				}

				eventTimestamp = (DateUtils.parseDate(time) != null) ? DateUtils.parseDate(time) : new Date();
			}

			ArrayList<String> attributeExpressions = new ArrayList<String>();
			for (SushiAttribute attribute : eventType.getValueTypes()) {
				attributeExpressions.add(attribute.getAttributeExpression());
			}
			eventValueTree.retainAllByAttributeExpression(attributeExpressions);
			return new SushiEvent(eventType, eventTimestamp, eventValueTree);
		}
	}

	private static void generateEventTreeFromElement(Node actualRootElement) {
		getChildNodesFromEvent(actualRootElement, true);
	}

	/**
	 * Parses the attributes of the event from the given {@link Node}.
	 * @param actualRootElement
	 */
	private static SushiMapTree<String, Serializable> getChildNodesFromEvent(Node actualRootElement, Boolean shouldBeRoot) {
		NodeList childNodeList = actualRootElement.getChildNodes();
		for(int i = 0; i < childNodeList.getLength(); i++){
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				//				String nodeName = childNode.getNodeName().replace(":", "_");
				String nodeName = childNode.getNodeName().trim().replaceAll(" +","_").replaceAll("[^a-zA-Z0-9_]+","");
				String nodeText = null;
				if(!hasRealChildNodes(childNode)){
					nodeText = childNode.getTextContent();
				}
				if(shouldBeRoot){
					eventValueTree.addChild(null, nodeName, nodeText);
				}
				else{
					//					eventValueTree.addChild(actualRootElement.getNodeName().replace(":", "_"), nodeName, nodeText);
					eventValueTree.addChild(actualRootElement.getNodeName().trim().replaceAll(" +","_").replaceAll("[^a-zA-Z0-9_]+",""), nodeName, nodeText);
				}
				getChildNodesFromEvent(childNode, false);
			}
		}
		return eventValueTree;
	}

	/**
	 * Returns true, if this node has child nodes from the Node.ELEMENT_NODE type.
	 * @param node
	 * @return
	 */
	private static boolean hasRealChildNodes(Node node) {
		boolean hasChildNodes = false;
		NodeList childNodeList = node.getChildNodes();
		for(int i = 0; i < childNodeList.getLength(); i++){
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				hasChildNodes = true;
			}
		}
		return hasChildNodes;
	}

	/**
	 * Returns the XSD name for a given node.
	 * @param element
	 * @return
	 */
	private static String getXSDNameFromNode(Node element) {
		Node xsdAttribute = element.getAttributes().getNamedItem("xsi:noNamespaceSchemaLocation");
		if (xsdAttribute == null) {
			System.err.println("no xsd stated in xml");
			return null;
		};
		String filePath = xsdAttribute.getNodeValue();
		int begin = filePath.lastIndexOf("/");
		int end = filePath.lastIndexOf(".");
		return filePath.substring(begin + 1, end);
	}

	/**
	 * Returns the first child with the given name from a given node.
	 * @param name
	 * @param parentNode
	 * @return
	 */
	public static Node getFirstChildWithNameFromNode(String name, Node parentNode){
		NodeList list = parentNode.getChildNodes();
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals(name)){
				return list.item(i);
			}
		}
		return null;
	}

	/**
	 * Returns the last child with the given name from a given node.
	 * @param name
	 * @param parentNode
	 * @return
	 */
	public static Node getLastChildWithNameFromNode(String name, Node parentNode){
		NodeList list = parentNode.getChildNodes();
		Node namedNode = null;
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals(name)){
				namedNode = list.item(i);
			}
		}
		return namedNode;
	}

	/**
	 * Returns all childs with the given name from a given node.
	 * @param name
	 * @param parentNode
	 * @return
	 */
	public static List<Node> getAllChildWithNameFromNode(String name, Node parentNode){
		List<Node> resultList = new ArrayList<Node>();
		NodeList list = parentNode.getChildNodes();
		for(int i = 0; i < list.getLength(); i++){
			if(list.item(i).getNodeName().equals(name)){
				resultList.add(list.item(i));
			}
		}
		return resultList;
	}
}
