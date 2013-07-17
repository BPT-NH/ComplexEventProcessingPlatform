package sushi.util;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sushi.event.SushiEvent;
import sushi.event.collection.SushiMapElement;
import sushi.event.collection.SushiMapTree;
import sushi.process.SushiProcessInstance;

/**
 * Utils for XML Document manipulation and generation.
 * Transforms SushiEvents in XML representation.
 */
public class XMLUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public enum XPathConstantsMapping {

		STRING (XPathConstants.STRING), 
		DATE (DatatypeConstants.DATETIME), 
		INTEGER (XPathConstants.NUMBER);

		private QName qName;

		XPathConstantsMapping(QName type){
			this.qName = type;
		}

		public QName getQName() {
			return qName;
		}
	}

	/**
	 * transforms SushiEvent into typed XML-Node. 
	 * @return XML Representation of the given SushiEvent
	 */
	public static Node eventToNode(SushiEvent event) {
		SushiMapTree<String, Serializable> values = event.getValues();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		// need document from XML for the XML parser
		Document doc = builder.newDocument();
		Element root = doc.createElement(event.getEventType().getTypeName());
		doc.appendChild(root);
		Element time = doc.createElement("Timestamp");
		time.setTextContent(getFormattedDate(event.getTimestamp()));
		root.appendChild(time);
		
		List<Integer> processInstanceIDs = new ArrayList<Integer>();
		for (SushiProcessInstance processInstance : event.getProcessInstances()) {
			processInstanceIDs.add(processInstance.getID());
		}
		Element processInst = doc.createElement("ProcessInstances");
		processInst.setTextContent(processInstanceIDs.toString());
		root.appendChild(processInst);
		
		for (SushiMapElement<String, Serializable> element : values.getTreeRootElements()){
			Node importedNode = doc.importNode(element.getNodeWithChildnodes().getFirstChild(), true);
			root.appendChild(importedNode);
		}
		return doc;
	}

	public static String getFormattedDate(Date date) {
		return sdf.format(date);
	}
	
	/**
	 * Converts Date into XML-Tag with type xsd:dateTime
	 */
	  public static String getXMLDate(Date date) {
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  return DatatypeConverter.printDateTime(calendar);
	  }
	  
	  /**
	   * Converts Integer into XML-Tag with type xsd:int
	   */
	  public static String getXMLInteger(Integer integer) {
		  return DatatypeConverter.printInt(integer);
	  }
	  
	  
	  public static void printDocument(Document doc) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}