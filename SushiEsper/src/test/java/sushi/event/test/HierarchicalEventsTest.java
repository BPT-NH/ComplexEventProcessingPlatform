package sushi.event.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sushi.FileUtils;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.query.SushiQueryTypeEnum;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiQuery;
import sushi.util.XMLUtils;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

public class HierarchicalEventsTest {

	SushiStreamProcessingAdapter esper;
	private static String pathToEventTaxonomyXSD = "src/test/resources/EventTaxonomy.xsd";
	private static String pathToEventXML = "src/test/resources/Event1.xml";	
	private static String pathToDoubleTagEventXML = "src/test/resources/EventDoubleTags.xml";	
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		SushiStreamProcessingAdapter.clearInstance();
		esper = SushiStreamProcessingAdapter.getInstance();
	}
	
	@Test
	public void testHierarchicalEventtypes() throws XMLParsingException, SAXException, IOException, ParserConfigurationException {
		SushiEventType eventType = XSDParser.generateEventTypeFromXSD(pathToEventTaxonomyXSD, FileUtils.getFileNameWithoutExtension(pathToEventTaxonomyXSD));
		Broker.send(eventType);
		//create direct esper event from xml
		Document doc = transformXMLFileToEventNode(pathToDoubleTagEventXML);
		Node rootElement = doc.getChildNodes().item(0);
		doc.renameNode(rootElement, "", eventType.getTypeName());
		esper.getEsperRuntime().sendEvent(doc);
	}
	
	@Test
	public void testEventsWithDoubleTags() throws XMLParsingException {
		SushiEventType eventType = XSDParser.generateEventTypeFromXSD(pathToEventTaxonomyXSD, FileUtils.getFileNameWithoutExtension(pathToEventTaxonomyXSD));
		Broker.send(eventType);

		//query
		String query = "Select ID, vehicle_information.goods[0], vehicle_information.goods[1], vehicle_information.goods[2] From " + eventType.getTypeName();
		SushiQuery liveQuery = new SushiQuery("All", query, SushiQueryTypeEnum.LIVE);
		liveQuery.save();
		SushiLiveQueryListener listener = liveQuery.addToEsper();

		//prepare Event
		SushiEvent event = XMLParser.generateEventFromXML(pathToDoubleTagEventXML);
		Broker.send(event);
		
		assertTrue("did not find an event, but" + listener.getNumberOfLogEntries(), listener.getNumberOfLogEntries() == 1);
		System.out.println(listener.getLog());
	}

	@Test
	public void testHierarchicalEvents() throws XMLParsingException, SAXException, IOException {
		//prepare EventType
		SushiEventType eventType = XSDParser.generateEventTypeFromXSD(pathToEventTaxonomyXSD, FileUtils.getFileNameWithoutExtension(pathToEventTaxonomyXSD));
		Broker.send(eventType);
		
		//query
		String query = "Select ID, vehicle_information.vehicle_identifier, count(vehicle_information.vehicle_identifier) From " + eventType.getTypeName();
		SushiQuery liveQuery = new SushiQuery("All", query, SushiQueryTypeEnum.LIVE);
		liveQuery.save();
		SushiLiveQueryListener listener = liveQuery.addToEsper();

		//prepare Event
		SushiEvent event = XMLParser.generateEventFromXML(pathToEventXML);
		Broker.send(event);
		
		assertTrue("did not find an event, but" + listener.getNumberOfLogEntries(), listener.getNumberOfLogEntries() == 1);
		System.out.println(listener.getLog());
	}
	
	@Test
	public void testEventTransformation() throws ParserConfigurationException, XMLParsingException{
		//prepare EventType
		SushiEventType eventType = XSDParser.generateEventTypeFromXSD(pathToEventTaxonomyXSD, FileUtils.getFileNameWithoutExtension(pathToEventTaxonomyXSD));
		Broker.send(eventType);
		
		SushiEvent event = XMLParser.generateEventFromXML(pathToEventXML);
		Node node = XMLUtils.eventToNode(event);
		int numberOfChildren = node.getFirstChild().getChildNodes().getLength();
		assertTrue("event should have 8 elements, but had " + numberOfChildren, numberOfChildren == 8);	
	}
	
	@Test
	public void testAttributeWithSpaces() {
		esper.clearInstance();
		ArrayList<SushiAttribute> attributes = new ArrayList<SushiAttribute>(Arrays.asList(new SushiAttribute("Attribute 1", SushiAttributeTypeEnum.STRING)));
		SushiEventType eventType = new SushiEventType("newEventType", attributes);
		Broker.send(eventType);
		
		SushiMapTree<String, Serializable> tree = new SushiMapTree<String, Serializable>();
		tree.addRootElement("Attribute 1", "Wert 1");
		SushiEvent event = new SushiEvent(eventType, new Date());
		event.setValues(tree);
		Broker.send(event);
	}

	
	public Document transformXMLFileToEventNode(String filePath) {
		String eventString = "";
		try {
			eventString = FileUtils.getFileContentAsString(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("The InputXML-Filepath was not valid.");			
		}
		InputSource source = new InputSource(new StringReader(eventString));
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);
		Document doc = null;
		try {
			doc = builderFactory.newDocumentBuilder().parse(source);
		} catch (SAXException e) {
				e.printStackTrace();
				fail("The InputXML-String was not valid.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("The InputXML-String was not valid.");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail("The InputXML-String was not valid.");
		}
		return doc;
	}
	
}
