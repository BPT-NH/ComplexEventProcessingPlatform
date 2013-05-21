package sushi.xml.importer;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sushi.bpmn.element.BPMNProcess;

/**
 * @author micha
 *
 */
public class SignavioBPMNParserTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/Kinomodell.bpmn20.xml";
	private static String complexfilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/complexProcess.bpmn20.xml";

	@Test
	public void testXPathParsing() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(false); 
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(filePath);
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		XPathExpression expr = xpath.compile("//process/*");

		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		printNodes(nodes, 1);
	}
	
	private void printNodes(NodeList nodes, int level) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node actualNode = nodes.item(i);
			if(actualNode.getNodeType() == 1){
				System.out.println(actualNode.getNodeName() + " " + level);
			}
			if(actualNode.hasChildNodes()){
				printNodes(actualNode.getChildNodes(), level + 1);
			}
		}
	}
	
	@Test
	public void testComplexProcess() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		BPMNProcess bpmnProcess = SignavioBPMNParser.generateProcessFromXML(complexfilePath);
		System.out.println(bpmnProcess.printProcess());
	}

}
