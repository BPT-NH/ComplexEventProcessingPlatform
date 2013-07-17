package sushi.xml.importer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;

/**
 * This class parses XSD files, which describes the schema of XML files.
 */
public class XSDParser extends AbstractXMLParser {
	
	private static SushiAttributeTree eventTree;
	
	/**
	 * Creates a new {@link SushiEventType} with the given name from a XSD from the given file path.
	 * @param filePath
	 * @param eventTypeName
	 * @return
	 * @throws XMLParsingException
	 */
	public static SushiEventType generateEventTypeFromXSD(String filePath, String eventTypeName) throws XMLParsingException {
		Document doc = readXMLDocument(filePath);
		if (doc == null) {
			throw new XMLParsingException("could not read XSD: " + filePath);
		}
		return generateEventType(doc, eventTypeName);
	}

	/**
	 * Creates a new {@link SushiEventType} with the given name from a XSD from the given document.
	 * @param doc
	 * @param schemaName
	 * @return
	 */
	public static SushiEventType generateEventType(Document doc, String schemaName) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new XSDNameSpaceContext());
		// XPath Query for showing all nodes value
		XPathExpression rootElementExpression = null;
		try {
			rootElementExpression = xpath.compile("//xs:schema/child::xs:element");
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
		//Jedes Root-Element wird ein EventType
		//Im Root-Element nach weiteren Unterelementen suchen
		if(!(rootElementNodes.getLength() == 1)) {
			System.err.println("Number of root elements is wrong, should be 1 but was " + rootElementNodes.getLength());
		}
		Node actualRootElement = rootElementNodes.item(0);
		eventTree = new SushiAttributeTree();
//		String eventTypeName = actualRootElement.getAttributes().getNamedItem("name").getNodeValue(); 
		addChildElementsFromElement(actualRootElement, null, null);
		
		return new SushiEventType(schemaName, eventTree, null, schemaName);
	}

	/**
	 * Creates a {@link SushiAttributeTree} for the given nodes.
	 * @param actualRootElement
	 * @param realRootElement
	 * @param realRootAttribute
	 * @return
	 */
	private static SushiAttributeTree addChildElementsFromElement(Node actualRootElement, Node realRootElement, SushiAttribute realRootAttribute) {
		NodeList childNodeList = actualRootElement.getChildNodes();
		for (int i = 0; i < childNodeList.getLength(); i++) {
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() == 1) {
				if (childNode.getNodeName().equals("xs:complexType") || childNode.getNodeName().equals("xs:sequence")) {
					addChildElementsFromElement(childNode, realRootElement, realRootAttribute);
				} else if(childNode.getNodeName().equals("xs:element")) {
					SushiAttributeTypeEnum attributeType = null;
					String xsElementType = null;
					if (childNode.getAttributes().getNamedItem("type") != null) {
						xsElementType = childNode.getAttributes().getNamedItem("type").getNodeValue();
					}
					if (xsElementType == null) {
						// chooses String if no 'type' attribute in xs:element found
						attributeType = SushiAttributeTypeEnum.STRING;
					} else {
						if (xsElementType.equals("xs:decimal") || xsElementType.equals("xs:integer") || xsElementType.equals("xs:int") || xsElementType.equals("xs:float")) {
							attributeType = SushiAttributeTypeEnum.INTEGER;
						} else if (xsElementType.equals("xs:date") || xsElementType.equals("xs:time")) {
							attributeType = SushiAttributeTypeEnum.DATE;						
						} else {
							attributeType = SushiAttributeTypeEnum.STRING;
						}
					}
					SushiAttribute newAttribute;
					String attributeName = childNode.getAttributes().getNamedItem("name").getNodeValue().trim().replaceAll(" +","_").replaceAll("[^a-zA-Z0-9_]+","");
					if (realRootElement == null) {
						newAttribute = new SushiAttribute(attributeName, attributeType);
						eventTree.addRoot(newAttribute);
					} else {
						newAttribute = new SushiAttribute(realRootAttribute, attributeName, attributeType);
					}
					addChildElementsFromElement(childNode, childNode, newAttribute);
				}
			}
		}
		return eventTree;
	}
	
}
