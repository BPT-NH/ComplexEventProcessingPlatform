package de.hpi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import sushi.event.SushiEvent;
import sushi.eventhandling.Broker;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;

public class XMLImportWebservice {


	public boolean importEvents(String xml){
		// generate input stream from xml for creating the doc
		InputStream xmlInputStream;
		try {
			xmlInputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		}
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		// need document from xml for the xml parser
		Document doc = null;
		System.out.println("received Event: \n" + xml);
		try {
			doc = builder.parse(xmlInputStream);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		// generate the Event from the xml string via XML Parser
		SushiEvent newEvent;
		try {
			newEvent = XMLParser.generateEventFromDoc(doc);
		} catch (XMLParsingException e) {
			e.printStackTrace();
			return false;
		}
		Broker.send(newEvent);
		return true;
	}
}