package sushi.xml.importer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import signavio.xml.converter.AbstractSignavioXMLElement;
import signavio.xml.converter.SignavioBPMNProcess;
import signavio.xml.converter.SignavioEndEvent;
import signavio.xml.converter.SignavioExclusiveGateway;
import signavio.xml.converter.SignavioParallelGateway;
import signavio.xml.converter.SignavioSequenceFlow;
import signavio.xml.converter.SignavioStartEvent;
import signavio.xml.converter.SignavioTask;
import sushi.event.SushiEvent;
import sushi.event.collection.SushiMapTree;

/**
 * Converts BPM2XML to Signavio.xml to visualize it with the CoreComponents.
 * @author Ben
 *
 */
public class BPM2XMLToSignavioXMLConverter extends AbstractXMLParser{
	
	private String bpm2FilePath, fileNameWithoutExtenxions;
	private SignavioBPMNProcess process;
	private final String pathToCoreComponentsFolder = "/home/platformaccount/signaviocore-workspace";

	/**
	 * @param bpm2FilePath
	 */
	public BPM2XMLToSignavioXMLConverter(String bpm2FilePath){
		this.bpm2FilePath = bpm2FilePath;
		File file = new File(bpm2FilePath);
		String fileName = file.getName();
		fileNameWithoutExtenxions = fileName.replaceAll("\\..*", "");
	}
	
	
	/**
	 * @param bpm2FilePath
	 * @return
	 */
	public String generateSignavioXMLFromBPM2XML(){
		SignavioBPMNProcess process = parseBPM2XML();
//		File file = new File(System.getProperty("user.dir")+"/src/test/resources/bpmn/" + fileNameWithoutExtenxions + ".signavio.xml");
		File file = new File(pathToCoreComponentsFolder + fileNameWithoutExtenxions + ".signavio.xml");
		try {
			FileWriter writer = new FileWriter(file ,false);
			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			writer.write(System.getProperty("line.separator"));
			
			writer.write("<oryxmodel>");
			writer.write(System.getProperty("line.separator"));
			
			writer.write("<description></description>");
			writer.write(System.getProperty("line.separator"));
			
			writer.write("<type>BPMN 2.0</type>");
			writer.write(System.getProperty("line.separator"));
			
			writer.write("<json-representation><![CDATA[");
			writer.write(process.generateSignavioXMLString());
			writer.write("]]></json-representation>");
			
			writer.write(System.getProperty("line.separator"));
			writer.write("</oryxmodel>");
			
			//Stream in Datei schreiben
			writer.flush();
	       // Schließt den Stream
	       writer.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return file.getName(); 
	}
	
	/**
	 * @return
	 */
	public SignavioBPMNProcess parseBPM2XML() {
		Document doc = readXMLDocument(bpm2FilePath);
		//XXX
		process = new SignavioBPMNProcess();
		Node definitionNode = getDefinitionsElementFromBPM(doc);
		if(definitionNode != null){
			process.addPropertyValue("targetnamespace", definitionNode.getAttributes().getNamedItem("targetNamespace").getNodeValue());
			process.addPropertyValue("expressionlanguage", definitionNode.getAttributes().getNamedItem("expressionLanguage").getNodeValue());
			process.addPropertyValue("typelanguage", definitionNode.getAttributes().getNamedItem("typeLanguage").getNodeValue());
		}
		//XXX
		NodeList processChilds = getProcessElementsFromBPM(doc);
		for(int i = 0; i <  processChilds.getLength(); i++){
			process.addChildShape(getProcessChildFromXML(processChilds.item(i), doc));
		}
		return process;
	}
	
	private AbstractSignavioXMLElement getProcessChildFromXML(Node item, Document doc) {
		AbstractSignavioXMLElement signavioXMLElement;
		if(item.getNodeName().equals("startEvent")){
			signavioXMLElement = generateStartEventFromXML(item);
		}
		else if(item.getNodeName().equals("endEvent")){
			signavioXMLElement = generateEndEventFromXML(item);
		}
		else if(item.getNodeName().equals("sequenceFlow")){
			signavioXMLElement = generateSequenceFlowFromXML(item);
		}
		else if(item.getNodeName().equals("exclusiveGateway")){
			signavioXMLElement = generateExclusiveGatewayFromXML(item);
		}
		else if(item.getNodeName().equals("parallelGateway")){
			signavioXMLElement = generateParallelGatewayFromXML(item);
		}
		else{
			signavioXMLElement = generateTaskFromXML(item);
		}
		signavioXMLElement.addPropertyValue("name", item.getAttributes().getNamedItem("name").getNodeValue());
		
		Node planeNode = getPlaneElementFromBPM(doc);
		NodeList childNodes = planeNode.getChildNodes();
		for(int i = 0; i < childNodes.getLength(); i++){
			Node child = childNodes.item(i);
			if(child.hasAttributes() && child.getAttributes().getNamedItem("bpmnElement").getNodeValue().equals(signavioXMLElement.getResourceId())){
				signavioXMLElement.getBoundsFromXMLNode(child);
				break;
			}
		}
		return signavioXMLElement;
	}


	private AbstractSignavioXMLElement generateParallelGatewayFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioParallelGateway parallelGateway = new SignavioParallelGateway(itemId);
		return parallelGateway;
	}


	private AbstractSignavioXMLElement generateExclusiveGatewayFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioExclusiveGateway exclusiveGateway = new SignavioExclusiveGateway(itemId);
		return exclusiveGateway;
	}


	private AbstractSignavioXMLElement generateSequenceFlowFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioSequenceFlow sequenceFlow = new SignavioSequenceFlow(itemId);
		Node sourceNode = item.getAttributes().getNamedItem("sourceRef");
		if(sourceNode != null) {
			String source = sourceNode.getNodeValue();
			for(AbstractSignavioXMLElement child : process.getChildShapes()){
				if(child.getResourceId().equals(source)){
					child.addOutgoing(itemId);
					sequenceFlow.setPredecessor(child);
				}
			}
		}
		Node targetNode = item.getAttributes().getNamedItem("targetRef");
		if(targetNode != null){
			String target = targetNode.getNodeValue();
			sequenceFlow.addOutgoing(target);
			for(AbstractSignavioXMLElement child : process.getChildShapes()){
				if(child.getResourceId().equals(target)){
					sequenceFlow.setSucessor(child);
				}
			}
		}
		return sequenceFlow;
	}


	private AbstractSignavioXMLElement generateEndEventFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioEndEvent endEvent = new SignavioEndEvent(itemId);
		return endEvent;
	}


	private AbstractSignavioXMLElement generateTaskFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioTask task = new SignavioTask(itemId);
		return task;
	}


	private AbstractSignavioXMLElement generateStartEventFromXML(Node item) {
		String itemId = item.getAttributes().getNamedItem("id").getNodeValue();
		SignavioStartEvent startEvent = new SignavioStartEvent(itemId);
		NodeList children = item.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node child = children.item(i);
			if(child.getNodeName().equals("messageEventDefinition")){
				startEvent.setMessageEvent();
			}
			if(child.getNodeName().equals("timerEventDefinition")){
				startEvent.setTimerEvent();
			}
			if(child.getNodeName().equals("escalationEventDefinition")){
				startEvent.setEscalationEvent();
			}
			if(child.getNodeName().equals("conditionalEventDefinition")){
				startEvent.setConditionalEvent();
			}
			if(child.getNodeName().equals("errorEventDefinition")){
				startEvent.setErrorEvent();
			}
			if(child.getNodeName().equals("compensateEventDefinition")){
				startEvent.setCompensateEvent();
			}
			if(child.getNodeName().equals("signalEventDefinition")){
				startEvent.setSignalEvent();
			}
		}
		return startEvent;
	}


	/**
	 * @param doc
	 */
	private Node getDefinitionsElementFromBPM(Document doc){
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new SignavioBPMNNameSpaceContext());
		// XPath Query for showing all nodes value
		XPathExpression definitionsExpression = null;
		try {
			definitionsExpression = xpath.compile("//ns:definitions");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		Object definitionsResult = null;
		try {
			definitionsResult = definitionsExpression.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		NodeList definitions = (NodeList)definitionsResult;
		if(definitions.getLength() > 0){
			return definitions.item(0);
		} else {
			return null;
		}
	}
	
	
	/**
	 * @param doc
	 */
	private NodeList getProcessElementsFromBPM(Document doc){
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new SignavioBPMNNameSpaceContext());
		// XPath Query for showing all nodes value
		XPathExpression processElementsExpression = null;
		try {
			processElementsExpression = xpath.compile("//ns:process/*");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		Object processElementsResult = null;
		try {
			processElementsResult = processElementsExpression.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return (NodeList) processElementsResult;
	}
	
	private Node getDiagramElementFromBPM(Document doc){
		Node definitionNode = getDefinitionsElementFromBPM(doc);
		if(definitionNode != null){
			return XMLParser.getFirstChildWithNameFromNode("bpmndi:BPMNDiagram", definitionNode);
		} 
		return null;
	}
	
	private Node getPlaneElementFromBPM(Document doc){
		Node diagramNode = getDiagramElementFromBPM(doc);
		if(diagramNode != null){
			return XMLParser.getFirstChildWithNameFromNode("bpmndi:BPMNPlane", diagramNode);
		} 
		return null;
	}
}
