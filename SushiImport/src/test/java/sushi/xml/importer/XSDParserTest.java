package sushi.xml.importer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import sushi.event.SushiEventType;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

/**
 * @author micha
 *
 */
public class XSDParserTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/EventTaxonomy.xsd";
	
	@Test
	public void testXSDParsing() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		SushiEventType eventType = null;
		try {
			eventType = XSDParser.generateEventTypeFromXSD(filePath, "EventTaxonomy");
		} catch (XMLParsingException e) {
			fail();
		}
		assertNotNull(eventType);
	}

}
