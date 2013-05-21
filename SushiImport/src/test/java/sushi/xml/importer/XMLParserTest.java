package sushi.xml.importer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import sushi.DateUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.persistence.Persistor;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;

/**
 * @author micha
 *
 */

public class XMLParserTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/Event1.xml";
	private static String filePathToXMLWithHierarchicalTimestamp = System.getProperty("user.dir")+"/src/test/resources/Event2.xml";
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
		
	}
	
	@Test
	public void testXMLParsing() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, XMLParsingException{
		SushiEventType eventTyp = new SushiEventType("EventTaxonomy");
		eventTyp.setXMLName("EventTaxonomy");
		eventTyp.setTimestampName("timestamp");
		eventTyp.save();
		SushiEvent event = XMLParser.generateEventFromXML(filePath);
		assertNotNull(event);
	}
	
	@Test
	public void testHierarchicalTimestampParsing() throws XMLParsingException {
		SushiEventType eventTyp = new SushiEventType("EventTaxonomy");
		eventTyp.setXMLName("EventTaxonomy");
		eventTyp.setTimestampName("location.timestamp");
		eventTyp.save();
		SushiEvent event = XMLParser.generateEventFromXML(filePathToXMLWithHierarchicalTimestamp);
		assertNotNull(event);
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2013, 11, 25, 20, 25, 00);
		assertTrue("Should be " + cal.getTime() + " but was " + event.getTimestamp(), event.getTimestamp().equals(cal.getTime()));
	}
	
	@Test
	public void testNonHierarchicalTimestampParsing() throws XMLParsingException {
		SushiEventType eventTyp = new SushiEventType("EventTaxonomy");
		eventTyp.setXMLName("EventTaxonomy");
		eventTyp.setTimestampName("timestamp");
		eventTyp.save();
		SushiEvent event = XMLParser.generateEventFromXML(filePathToXMLWithHierarchicalTimestamp);
		assertNotNull(event);
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2013, 11, 24, 20, 25, 00);
		assertTrue("Should be " + cal.getTime() + " but was " + event.getTimestamp(), event.getTimestamp().equals(cal.getTime()));
	}
	
	@Test
	public void testDateParsing() {
		String timeStampString = "24.12.2013 20:25";
		Date timeStamp = DateUtils.parseDate(timeStampString);
		assertTrue(timeStamp.getDate() == 24);
		assertTrue(timeStamp.getMonth() == 12 - 1);
		assertTrue(timeStamp.getYear() == 2013 - 1900);
	}

}
