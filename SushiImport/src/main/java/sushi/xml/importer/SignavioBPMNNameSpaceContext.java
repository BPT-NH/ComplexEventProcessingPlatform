package sushi.xml.importer;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;


/**
 * @author micha
 *
 */
public class SignavioBPMNNameSpaceContext implements NamespaceContext {
	
	public String getNamespaceURI(String prefix) {
		if (prefix.equals("ns"))
			return "http://www.omg.org/spec/BPMN/20100524/MODEL";
		else
			return XMLConstants.NULL_NS_URI;
	}

	public String getPrefix(String namespace) {
		if (namespace.equals("http://www.omg.org/spec/BPMN/20100524/MODEL"))
			return "ns";
		else
			return null;
	}

	public Iterator getPrefixes(String namespace) {
		return null;
	}
	
}
