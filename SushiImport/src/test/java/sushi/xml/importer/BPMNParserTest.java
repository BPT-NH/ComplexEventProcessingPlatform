package sushi.xml.importer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import sushi.bpmn.element.BPMNSubProcess;

/**
 * This class tests the import of BPMN processes and subprocesses from XML.
 * @author micha
 */
public class BPMNParserTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/Kinomodell.bpmn20.xml";
	private static String complexfilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/complexProcess.bpmn20.xml";
	private static String subProcessfilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/Automontage_TwoTerminal.bpmn20.xml";

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
		BPMNProcess BPMNProcess = BPMNParser.generateProcessFromXML(complexfilePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 21);
		assertTrue(BPMNProcess.getStartEvent().getId().equals("sid-EC585815-8EAC-411C-89C2-553ACA85CF5A"));
	}
	
	@Test
	public void testSubProcessImport() {
		BPMNProcess BPMNProcess = BPMNParser.generateProcessFromXML(subProcessfilePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 7);
		assertTrue(BPMNProcess.hasSubProcesses());
		BPMNSubProcess subProcess = BPMNProcess.getSubProcesses().get(0);
		assertNotNull(subProcess);
		assertFalse(subProcess.getStartEvent().getSuccessors().isEmpty());
	}

}
