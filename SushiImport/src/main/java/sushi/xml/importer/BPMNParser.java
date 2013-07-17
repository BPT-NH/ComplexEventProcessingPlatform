package sushi.xml.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AttachableElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNBoundaryEvent;
import sushi.bpmn.element.BPMNEndEvent;
import sushi.bpmn.element.BPMNEventBasedGateway;
import sushi.bpmn.element.BPMNEventBasedGatewayType;
import sushi.bpmn.element.BPMNIntermediateEvent;
import sushi.bpmn.element.BPMNEventType;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNSequenceFlow;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNSubProcess;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.event.SushiEventType;

/**
 * This class generates a logical BPMN representation from a
 * (Signavio-)BPMN-2.0-XML
 * 
 * @author micha
 * 
 */
public class BPMNParser extends AbstractXMLParser {

	private static ArrayList<String> VALID_BPMN_XML_ELEMENTS = new ArrayList<String>(Arrays.asList("startEvent", "task", "sendTask", "subProcess", "boundaryEvent", "endEvent", "parallelGateway", "exclusiveGateway", "intermediateCatchEvent", "eventBasedGateway"));

	/**
	 * Parses a BPMN-2.0-XML from the given file path to a {@link BPMNProcess}.
	 * @param filePath
	 * @return
	 */
	public static BPMNProcess generateProcessFromXML(String filePath) {
		Document doc = readXMLDocument(filePath);
		return generateBPMNProcess(doc);
	}

	/**
	 * Parses a {@link BPMNProcess} from the {@link Document}.
	 * @param doc
	 * @return
	 */
	private static BPMNProcess generateBPMNProcess(Document doc) {
		if(doc != null){
			XPath xpath = XPathFactory.newInstance().newXPath();
			xpath.setNamespaceContext(new SignavioBPMNNameSpaceContext());
			// XPath Query for showing all nodes value
			XPathExpression processElementsExpression = null;
			XPathExpression processExpression = null;
			try {
				processElementsExpression = xpath.compile("//ns:process/*");
				processExpression = xpath.compile("//ns:process");
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			Object elementsResult = null;
			Object processResult = null;
			try {
				elementsResult = processElementsExpression.evaluate(doc, XPathConstants.NODESET);
				processResult = processExpression.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList processNode = (NodeList) processResult;

			BPMNProcess process = new BPMNProcess(extractID(processNode.item(0)), "", new ArrayList<MonitoringPoint>());

			NodeList processElements = (NodeList) elementsResult;
			for (int i = 0; i < processElements.getLength(); i++) {
				process.addBPMNElement(extractBPMNElement(processElements.item(i)));
			}
			linkProcessElements(process, processElements);
			return process;
		} else {
			System.err.println("Document was null!");
			return null;
		}
	}

	/**
	 * Extraction of BPMN-Elements from the given {@link Node}. <br>
	 * Supported elements: <br>
	 * <ul>
	 * <li>boundaryEvent</li>
	 * <li>endEvent</li>
	 * <li>eventBasedGateway</li>
	 * <li>exclusiveGateway</li>
	 * <li>intermediateCatchEvent</li>
	 * <li>parallelGateway</li>
	 * <li>sequenceFlow</li>
	 * <li>startEvent</li>
	 * <li>subProcess</li>
	 * <li>task</li>
	 * </ul>
	 * @param element
	 * @return
	 */
	private static AbstractBPMNElement extractBPMNElement(Node element) {
		if (element.getNodeName().equals("startEvent")) {
			return extractStartEvent(element);
		} else if (element.getNodeName().equals("task") || element.getNodeName().equals("sendTask")) {
			return extractTask(element);
		} else if (element.getNodeName().equals("subProcess")) {
			return extractSubProcess(element);
		} else if (element.getNodeName().equals("boundaryEvent")) {
			return extractBoundaryEvent(element);
		} else if (element.getNodeName().equals("endEvent")) {
			return extractEndEvent(element);
		} else if (element.getNodeName().equals("exclusiveGateway")) {
			return extractExclusiveGateway(element);
		} else if (element.getNodeName().equals("parallelGateway")) {
			return extractParallelGateway(element);
		} else if (element.getNodeName().equals("eventBasedGateway")) {
			return extractEventBasedGateway(element);
		} else if (element.getNodeName().equals("intermediateCatchEvent")) {
			return extractIntermediateCatchEvent(element);
		} else if (element.getNodeName().equals("sequenceFlow")) {
			return extractSequenceFlow(element);
		}
		return null;
	}

	/**
	 * Parses an {@link BPMNEndEvent} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNEndEvent extractEndEvent(Node element) {
		return new BPMNEndEvent(extractID(element), extractName(element), extractMonitoringPoints(element));
	}

	/**
	 * Parses an {@link BPMNBoundaryEvent} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNBoundaryEvent extractBoundaryEvent(Node element) {
		BPMNBoundaryEvent boundaryEvent = new BPMNBoundaryEvent(extractID(element), extractName(element), extractMonitoringPoints(element), extractEventType(element));
		boundaryEvent.setCancelActivity(extractCancelActivity(element));
		switch(boundaryEvent.getIntermediateEventType()){
		case Cancel:
			break;
		case Compensation:
			break;
		case Error:
			break;
		case Link:
			break;
		case Message:
			break;
		case Signal:
			break;
		case Timer:
			extractEventTimerDefinition(element, boundaryEvent);
			break;
		default:
			break;
		}
		return boundaryEvent;
	}
	
	/**
	 * Parses an {@link BPMNEventType} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNEventType extractEventType(Node element) {
		if(!getChildNodesByNodeName(element, "timerEventDefinition").isEmpty()){
			return BPMNEventType.Timer;
		} else if(!getChildNodesByNodeName(element, "errorEventDefinition").isEmpty()){
			return BPMNEventType.Error;
		} else if(!getChildNodesByNodeName(element, "messageEventDefinition").isEmpty()){
			return BPMNEventType.Message;
		}
		return BPMNEventType.Blank;
	}

	/**
	 * Parses the timer definition for the given intermediate event and assigns the definition to this element.
	 * @param element
	 * @param intermediateEvent
	 */
	private static void extractEventTimerDefinition(Node element, BPMNIntermediateEvent intermediateEvent) {
		if(!getChildNodesByNodeName(element, "timerEventDefinition").isEmpty()){
			List<Node> timerDefinitions = getChildNodesByNodeName(element, "timerEventDefinition");
			for(Node timerDefinition : timerDefinitions){
				//TimeDuration ermitteln
				if(!getChildNodesByNodeName(timerDefinition, "timeDuration").isEmpty()){
					String timeDuration = getChildNodesByNodeName(timerDefinition, "timeDuration").get(0).getTextContent();
					if(timeDuration != null){
						float duration;
						try{
							duration = Float.parseFloat(timeDuration);
						} catch(NumberFormatException n){
							System.err.println("Time duration could not be parsed!");
							duration = 0;
						}
						intermediateEvent.setTimeDuration(duration);
					}
				}
			}
		}
		
	}

	/**
	 * Parses an {@link BPMNIntermediateEvent} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNIntermediateEvent extractIntermediateCatchEvent(Node element) {
		BPMNIntermediateEvent intermediateEvent = new BPMNIntermediateEvent(extractID(element), extractName(element), extractMonitoringPoints(element), extractIntermediateEventType(element));
		intermediateEvent.setCatchEvent(true);
		if(intermediateEvent.getIntermediateEventType().equals(BPMNEventType.Timer)){
			extractEventTimerDefinition(element, intermediateEvent);
		}
		return intermediateEvent;
	}

	/**
	 * Proofs, if the given {@link Node} has an cancelActivity attribute.
	 * @param element
	 * @return
	 */
	private static boolean extractCancelActivity(Node element) {
		String cancelValue = element.getAttributes().getNamedItem("cancelActivity").getNodeValue();
		boolean isCancelActivity = (cancelValue.equals("true")) ? true : false;
		return isCancelActivity;
	}

	/**
	 * Parses an {@link BPMNSequenceFlow} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNSequenceFlow extractSequenceFlow(Node element) {
		return new BPMNSequenceFlow(extractID(element), extractName(element), extractSourceRef(element), extractTargetRef(element));
	}

	/**
	 * Parses an {@link BPMNSubProcess} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNSubProcess extractSubProcess(Node element) {
		BPMNSubProcess subProcess = new BPMNSubProcess(extractID(element), extractName(element), extractMonitoringPoints(element));
		NodeList subProcessElements = (NodeList) element.getChildNodes();
		for (int i = 0; i < subProcessElements.getLength(); i++) {
			subProcess.addBPMNElement(extractBPMNElement(subProcessElements.item(i)));
		}
		return subProcess;
	}

	/**
	 * Parses an {@link BPMNTask} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNTask extractTask(Node element) {
		List<MonitoringPoint> monitoringPoints = extractMonitoringPoints(element);
		return new BPMNTask(extractID(element), extractName(element), monitoringPoints);
	}
	
	/**
	 * Parses an {@link BPMNAndGateway} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNAndGateway extractParallelGateway(Node element) {
		List<MonitoringPoint> monitoringPoints = extractMonitoringPoints(element);
		return new BPMNAndGateway(extractID(element), extractName(element), monitoringPoints);
	}
	
	/**
	 * Parses an {@link BPMNEventBasedGateway} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNEventBasedGateway extractEventBasedGateway(Node element) {
		List<MonitoringPoint> monitoringPoints = extractMonitoringPoints(element);
		return new BPMNEventBasedGateway(extractID(element), extractName(element), monitoringPoints, extractEventGatewayType(element));
	}
	
	/**
	 * Parses an {@link BPMNXORGateway} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNXORGateway extractExclusiveGateway(Node element) {
		List<MonitoringPoint> monitoringPoints = extractMonitoringPoints(element);
		return new BPMNXORGateway(extractID(element), extractName(element), monitoringPoints);
	}

	/**
	 * Parses an {@link BPMNStartEvent} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNStartEvent extractStartEvent(Node element) {
		//TODO: MessageStartEvent unterstützen
		return new BPMNStartEvent(extractID(element), extractName(element), extractMonitoringPoints(element), extractEventType(element));
	}

	/**
	 * Returns the value of the name attribute for the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static String extractName(Node element) {
		if(element.getAttributes().getNamedItem("name") != null){
			return element.getAttributes().getNamedItem("name").getNodeValue();
		} else{
			return "";
		}
	}
	
	/**
	 * Parses an {@link BPMNEventBasedGatewayType} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNEventBasedGatewayType extractEventGatewayType(Node element) {
		String eventGatewayType = element.getAttributes().getNamedItem("eventGatewayType").getNodeValue();
		if(eventGatewayType.equals("Parallel")){
			return BPMNEventBasedGatewayType.Parallel;
		} else {
			return BPMNEventBasedGatewayType.Exclusive;
		}
	}

	/**
	 * Returns the value of the sourceRef attribute for the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static String extractSourceRef(Node element) {
		return element.getAttributes().getNamedItem("sourceRef").getNodeValue();
	}

	/**
	 * Returns the value of the targetRef attribute for the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static String extractTargetRef(Node element) {
		return element.getAttributes().getNamedItem("targetRef").getNodeValue();
	}

	/**
	 * Returns the value of the id attribute for the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static String extractID(Node element) {
		return element.getAttributes().getNamedItem("id").getNodeValue();
	}
	
	/**
	 * Parses an {@link BPMNEventType} from the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static BPMNEventType extractIntermediateEventType(Node element) {
		//TODO: Implement other intermediate event type
		if(!getChildNodesByNodeName(element, "messageEventDefinition").isEmpty()){
			return BPMNEventType.Message;
		} else if(!getChildNodesByNodeName(element, "timerEventDefinition").isEmpty()){
			return BPMNEventType.Timer;
		}
		return null;
	}
	
	/**
	 * Parses a list of {@link MonitoringPoint}s for the given {@link Node}.
	 * @param element
	 * @return
	 */
	private static List<MonitoringPoint> extractMonitoringPoints(Node element) {
		List<MonitoringPoint> monitoringPoints = new ArrayList<MonitoringPoint>();
		
		ArrayList<Node> extensionElementNodes = getChildNodesByNodeName(element, "extensionElements");
		assert(extensionElementNodes.size() < 2);
		if(extensionElementNodes.size() == 0){
			return monitoringPoints;
		}
		Node extensionElementNode = extensionElementNodes.get(0);
		
		ArrayList<Node> monitoringPointNodes = getChildNodesByNodeName(extensionElementNode, "sushi:transition");
		
		for(Node actualTransitionNode : monitoringPointNodes){
			//TODO: Bei Einlesen des Types Groß- und Kleinschreibung abfangen
			String monitoringTypeString = actualTransitionNode.getAttributes().getNamedItem("type").getNodeValue();
			for(MonitoringPointStateTransition actualMonitoringPointType : MonitoringPointStateTransition.values()){
				if(monitoringTypeString.equals(actualMonitoringPointType.toString())){
					SushiEventType eventType = SushiEventType.findByTypeName(actualTransitionNode.getAttributes().getNamedItem("regularExpression").getNodeValue());
					monitoringPoints.add(new MonitoringPoint(eventType, actualMonitoringPointType, ""));
				}
			}
		}
		return monitoringPoints;
	}

	/**
	 * Creates a successor and predecessor relationship for the given {@link AbstractBPMNElement} based on the {@link BPMNSequenceFlow}s.
	 * @param process
	 * @param element
	 * @param childNodes
	 */
	private static void linkActualElement(BPMNProcess process,	AbstractBPMNElement element, NodeList childNodes) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node actualNode = childNodes.item(i);
			if (actualNode.getNodeType() == 1) {
				if (actualNode.getNodeName().equals("incoming")) {
					BPMNSequenceFlow sequenceFlow = (BPMNSequenceFlow) process.getBPMNElementById(actualNode.getTextContent());
					AbstractBPMNElement.connectElements(process.getBPMNElementById(sequenceFlow.getSourceRef()), element);
				} else if (actualNode.getNodeName().equals("outgoing")) {
					BPMNSequenceFlow sequenceFlow = (BPMNSequenceFlow) process.getBPMNElementById(actualNode.getTextContent());
					AbstractBPMNElement.connectElements(element, process.getBPMNElementById(sequenceFlow.getTargetRef()));
				}
			}
			if (actualNode.getNodeName().equals("subProcess")) {
				linkProcessElements(process, actualNode.getChildNodes());
			}
		}
	}

	/**
	 * Creates a successor and predecessor relationship between the parsed {@link AbstractBPMNElement}s based on the {@link BPMNSequenceFlow}s.
	 * @param process
	 * @param processElementNodes
	 */
	private static void linkProcessElements(BPMNProcess process, NodeList processElementNodes) {
		for (int i = 0; i < processElementNodes.getLength(); i++) {
			Node actualNode = processElementNodes.item(i);
			if (actualNode.getNodeType() == 1 && VALID_BPMN_XML_ELEMENTS.contains(actualNode.getNodeName())) {
				if (actualNode.hasChildNodes()) {
					AbstractBPMNElement element = process.getBPMNElementById(actualNode.getAttributes().getNamedItem("id").getNodeValue());
					if(element != null){
						linkActualElement(process, element, actualNode.getChildNodes());
						if(element instanceof BPMNBoundaryEvent){
							attachBoundaryEvent(process, (BPMNBoundaryEvent) element, actualNode);
						} else if(element instanceof BPMNSubProcess){
							linkProcessElements((BPMNSubProcess) element, actualNode.getChildNodes());
						}
					}
				}
			}
		}
	}

	/**
	 * Attaches a boundary event to the given {@link Node}.
	 * @param process
	 * @param boundaryEvent
	 * @param actualNode
	 */
	private static void attachBoundaryEvent(BPMNProcess process, BPMNBoundaryEvent boundaryEvent, Node actualNode) {
		String attachedToElementID = actualNode.getAttributes().getNamedItem("attachedToRef").getNodeValue();
		AbstractBPMNElement attachedToElement = process.getBPMNElementById(attachedToElementID);
		boundaryEvent.setAttachedToElement(attachedToElement);
		AbstractBPMNElement.connectElements(attachedToElement, boundaryEvent);
		if(attachedToElement instanceof AttachableElement){
			AttachableElement attachedElement = (AttachableElement) attachedToElement;
			attachedElement.setAttachedIntermediateEvent(boundaryEvent);
		}
	}

	/**
	 * Returns all child nodes for a given node with the given name, if any.
	 * @param element
	 * @param nodeName
	 * @return
	 */
	private static ArrayList<Node> getChildNodesByNodeName(Node element, String nodeName){
		ArrayList<Node> resultList = new ArrayList<Node>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node actualChildNode = childNodes.item(i);
			if(actualChildNode.getNodeType() == 1 && actualChildNode.getNodeName().equals(nodeName)){
				resultList.add(actualChildNode);
			}
		}
		return resultList;
	}
}
