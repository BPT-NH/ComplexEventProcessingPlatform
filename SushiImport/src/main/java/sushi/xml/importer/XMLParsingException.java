package sushi.xml.importer;

/**
 * A exception for error while parsing XML files.
 * @author micha
 */
@SuppressWarnings("serial")
public class XMLParsingException extends Exception {

	public XMLParsingException() {

	}

	public XMLParsingException(String s) {
		super(s);
	}

}
