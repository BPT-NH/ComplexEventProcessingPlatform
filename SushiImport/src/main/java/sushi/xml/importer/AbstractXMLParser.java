package sushi.xml.importer;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class centralizes methods for the parsing of XML files.
 * @author micha
 */
public class AbstractXMLParser {
	
	public static final String CURRENT_TIMESTAMP = "Current timestamp";
	public static final String GENERATED_TIMESTAMP_COLUMN_NAME = "Import_time";
	
	/**
	 * Returns a {@link Document} for a XML file from the given file path.
	 * @param filePath
	 */
	protected static Document readXMLDocument(String filePath) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = builder.parse(filePath);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

}
