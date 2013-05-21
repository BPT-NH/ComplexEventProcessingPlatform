package sushi.xml.importer;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;


/**
 * @author micha
 *
 */
public class XSDNameSpaceContext implements NamespaceContext {
	
	public String getNamespaceURI(String prefix) {
		if (prefix.equals("xs"))
			return "http://www.w3.org/2001/XMLSchema";
		else
			return XMLConstants.NULL_NS_URI;
	}

	public String getPrefix(String namespace) {
		if (namespace.equals("http://www.w3.org/2001/XMLSchema"))
			return "xs";
		else
			return null;
	}

	public Iterator getPrefixes(String namespace) {
		return null;
	}
	
}
