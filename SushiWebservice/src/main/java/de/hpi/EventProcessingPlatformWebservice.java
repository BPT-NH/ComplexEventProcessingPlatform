package de.hpi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.espertech.esper.client.EPStatementSyntaxException;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;
import sushi.user.SushiUser;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

/**
 * 
 * provides webservice methods
 * registered in src/main/webapps/WEB-INF/services/XMLService/META-INF/services.xml
 * to start webserver run: mvn jetty:run (WSDL ist deployed to: localhost:8080/SushiWebservice/services/EventProcessingPlatformWebservice?wsdl) 
 * 
 */
public class EventProcessingPlatformWebservice {

	/**
	 * imort xml-event if eventtyp of event is registered to the EPP 
	 */
	public boolean importEvents(String xml){
		// generate document from xml String
		Document doc = stringToDoc(xml);
		if (doc == null) return false;
		
		// generate the Event from the doc via XML Parser
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
	
	/**
	 * 
	 * register XSD Eventtyp
	 */
	public boolean registerEventType(String xsd, String schemaName){
		// generate input stream from xml for creating the doc
		Document doc = stringToDoc(xsd);
		if (doc == null) return false;
		//test for already existing
		if (SushiEventType.findBySchemaName(schemaName) != null) return false;
		
		// generate the EventType from the xml string via XML Parser
		SushiEventType newEventType;
		newEventType = XSDParser.generateEventType(doc, schemaName);
		Broker.send(newEventType);
		return true;
	}
	
	/**
	 * adds Esper EPL-query to the EPP and if it is triggered, it sends an email to @param email  
	 */
	public String addQueryNotification(String title, String queryString, String email) {
		//addQuery
		SushiQuery query = new SushiQuery(title, queryString, SushiQueryTypeEnum.LIVE);
		try {
			// query.validate();
		} catch(EPStatementSyntaxException e) {
			return e.getExpression();
		}
		query.save();
		query.addToEsper();
		
		//add User
		SushiUser user;
		if (SushiUser.findByMail(email).isEmpty()) {
			user = new SushiUser(email, "1234", email);
			user.save();
		} else {
			user = SushiUser.findByMail(email).get(0);
		}
		
		//addNotification
		SushiNotificationRuleForQuery notificationRule = new SushiNotificationRuleForQuery(query, user, SushiNotificationPriorityEnum.HIGH);
		notificationRule.save();
		return "ok";		
	}
	
	private Document stringToDoc(String xml) {
		InputStream xsdInputStream;
		try {
			xsdInputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
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
		System.out.println("received EventType: \n" + xml);
		try {
			doc = builder.parse(xsdInputStream);
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return doc;
	}
}