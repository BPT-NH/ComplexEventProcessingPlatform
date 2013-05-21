package sushi.esper.tests;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sushi.FileUtils;
import sushi.edifact.importer.EdifactParser;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.query.SushiQueryTypeEnum;
import sushi.query.SushiLiveQueryListener;
import sushi.query.SushiQuery;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

public class EdifactXMLImporterTest {

	
	private static String edifactPath = "src/test/resources/1_BERMAN.txt";
	private static String copinoPath = "src/test/resources/6_COPINO.txt";
	private static String xmlPath = "src/test/resources/1_BERMAN.xml";
	private static String xsdPath = "src/test/resources/berman.xsd";
	private SushiEsper esper;
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		esper = SushiEsper.getInstance();
	}
	
	@Test
	public void importEdifactXML() throws XMLParsingException {
		SushiEventType eventType = XSDParser.generateEventTypeFromXSD(xsdPath, FileUtils.getFileNameWithoutExtension(xsdPath));
		Broker.send(eventType);
//		System.out.println(eventType.getValueTypeTree());
		assertTrue("not found eventType, but found " +  SushiEventType.findAll(), SushiEventType.findByID(eventType.getID())==eventType);
		SushiEvent event = XMLParser.generateEventFromXML(xmlPath);
		SushiQuery query = new SushiQuery("xmlEvent", "Select * from " + eventType.getTypeName(), SushiQueryTypeEnum.LIVE);
		SushiQuery testAtts = new SushiQuery("testAtts", "Select env_syntaxIdentifier.env_id as SyntaxID, env_interchangeMessage.env_UNH.env_messageIdentifier.env_id as messageIdentifier "
										+ "from " + eventType.getTypeName(), SushiQueryTypeEnum.LIVE);
		SushiLiveQueryListener listener = query.addToEsper(esper);
		SushiLiveQueryListener listenerAtts = testAtts.addToEsper(esper);
		esper.addEvent(event);
		System.out.println(listenerAtts.getLog());
//		System.out.println(event.fullEvent());
		 Set<String> remainingatts = event.getValues().keySet();
		remainingatts.removeAll(eventType.getValueTypes());
		assertTrue("There was a value in the event unknown to eventType: " + remainingatts, remainingatts.isEmpty());
		
		assertTrue("should have found 1 event in eventType, but found " + listener.getNumberOfLogEntries(), listener.getNumberOfLogEntries() == 1);
	}
	
	@Test
	public void testGenerateEventFromEdifact() throws XMLParsingException, Exception {
		SushiEvent event = EdifactParser.getInstance().generateEventFromEdifact(edifactPath);
		assertTrue("not created event", event != null);
		Broker.send(event.getEventType());
		assertTrue(SushiEsper.getInstance().isEventType(event.getEventType()));
		System.out.println("EventType: ");
		System.out.println(event.getEventType().getValueTypeTree().toString());
		Broker.send(event);
	}
	
	@Test
	public void testCopinoEventUpload() throws XMLParsingException, Exception {
		SushiEvent event = EdifactParser.getInstance().generateEventFromEdifact(copinoPath);
		assertTrue("not created event", event != null);
		Broker.send(event.getEventType());
		assertTrue(SushiEsper.getInstance().isEventType(event.getEventType()));
		System.out.println("EventType: ");
		System.out.println(event.getEventType().getValueTypeTree().toString());
		Broker.send(event);
	}

}
