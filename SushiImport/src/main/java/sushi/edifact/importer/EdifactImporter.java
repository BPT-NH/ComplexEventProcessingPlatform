package sushi.edifact.importer;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.milyn.Smooks;
import org.milyn.smooks.edi.unedifact.UNEdifactReaderConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sushi.FileUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

/**
 * adapter for Edifact files 
 */
public class EdifactImporter {

  private static Smooks smooks;

  /**
   * @param args
   * @throws Exception 
   * @throws XMLParsingException 
   * @throws SAXException
   * @throws IOException
   */
  private static EdifactImporter instance = null;
  
  /**
   * singleton 
   */
  public static EdifactImporter getInstance() {
    if (instance == null) {
      instance = new EdifactImporter();
    }
    return instance;
  }

  /**
   * generates Events from Edifact file 
   */
  public SushiEvent generateEventFromEdifact(String filePath) throws XMLParsingException, Exception {
    StreamResult result = convertEdiFileToXML(filePath);
    Document doc = FileUtils.createDocumentFromResult(result);
    return XMLParser.generateEventFromDoc(doc);
  }
  
  /**
   * convert edifact file located in path to XML
   */
  public StreamResult convertEdiFileToXML(String path) throws Exception {
    StreamSource messageIn = new StreamSource(new File(path));

    Writer outWriter = new StringWriter();
    StreamResult result = new StreamResult(outWriter);
    
    try {
      String message = FileUtils.getFileContentAsString(path);
      smooks = getSmooksForEdifactFile(message);
      smooks.filterSource(messageIn, result);
    } catch(Exception e) {  
      e.printStackTrace();
      throw e;
    } 
    finally {
      if (smooks != null) smooks.close();
    }
    return result;
  }
  
  private Smooks getSmooksForEdifactFile(String message) throws Exception {
    if (message.contains("COPINO:D:95B:UN:INT10I")) {
      //This file is momentarily located at  Dropbox
      //since the path cannot be resolved by smooks if java is started from a different Project than SushiImport
      //Move file to server (but then it of course would only be accessible within the HPI or find other solution
      Smooks smooks_copino = new Smooks("https://dl.dropboxusercontent.com/u/18481312/SushiResources/smooks-config-copino.xml");      
//      This only works within SushiImport...
//      Smooks smooks_copino = new Smooks("smooks-config-copino.xml");
      System.out.println("use modified d95b");
      return smooks_copino;
    }
    
    if (message.contains(":D:03A")) {
      Smooks smooks_95b = new Smooks();
      smooks_95b.setReaderConfig(new UNEdifactReaderConfigurator("urn:org.milyn.edi.unedifact:d03a-mapping:1.5-SNAPSHOT"));
      System.out.println("use do3a");
      return smooks_95b;
    }
    if (message.contains(":D:95B")) {
      Smooks smooks_95b = new Smooks();
      smooks_95b.setReaderConfig(new UNEdifactReaderConfigurator("urn:org.milyn.edi.unedifact:d95b-mapping:1.5-SNAPSHOT"));
      System.out.println("use d95b");
      return smooks_95b;
    }
    if (message.contains(":D:00B")) {
      Smooks smooks_00b = new Smooks();
      smooks_00b.setReaderConfig(new UNEdifactReaderConfigurator("urn:org.milyn.edi.unedifact:d00b-mapping:1.5-SNAPSHOT"));
      System.out.println("use d00b");
      return smooks_00b;
    }   
    throw new Exception("The edifact standard used in this document is not supported.");
    
  }

  /**
   * returns Eventtyp for given Edifact document
   *  
   */
  public SushiEventType getEventTypeForEdifact(Document doc) throws XMLParsingException {
    /**
     *  <env:interchangeMessage xmlns:c="urn:org.milyn.edi.unedifact:un:d03a:common" xmlns:berman="urn:org.milyn.edi.unedifact:un:d03a:berman">
    <env:UNH>
      <env:messageRefNum>123827613X</env:messageRefNum>
      <env:messageIdentifier>
        <env:id>BERMAN</env:id>
        <env:versionNum>D</env:versionNum>
        <env:releaseNum>03A</env:releaseNum>
     */
    String id = null;
    String version = null;
    String release = null;
    
    if (doc == null) System.err.println("no document given");
    
    XPath xpath = XPathFactory.newInstance().newXPath();
    XPathExpression miIdExpression = null;
    XPathExpression miVersionExpression = null;
    XPathExpression miReleaseExpression = null;
    try {
      miIdExpression = xpath.compile("//*[local-name() = 'interchangeMessage']/" +
          "*[local-name() = 'UNH']/" +
          "*[local-name() = 'messageIdentifier']/" +
          "*[local-name() = 'id']");
      miVersionExpression = xpath.compile("//*[local-name() = 'interchangeMessage']/" +
          "*[local-name() = 'UNH']/" +
          "*[local-name() = 'messageIdentifier']/" +
          "*[local-name() = 'versionNum']");
      miReleaseExpression =  xpath.compile("//*[local-name() = 'interchangeMessage']/" +
          "*[local-name() = 'UNH']/" +
          "*[local-name() = 'messageIdentifier']/" +
          "*[local-name() = 'releaseNum']");
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }

    try {
      Object idResult = miIdExpression.evaluate(doc, XPathConstants.NODE);
      id = ((Node) idResult).getTextContent();    
      Object versionResult = miVersionExpression.evaluate(doc, XPathConstants.NODE);
      version = ((Node) versionResult).getTextContent();
      Object releaseResult = miReleaseExpression.evaluate(doc, XPathConstants.NODE);
      release = ((Node) releaseResult).getTextContent();
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    } catch (NullPointerException e1) {
      throw new XMLParsingException("no edifact-formatted event");
    }
  
    System.out.println(getFilePathForEdifactXSD(id, version, release));
    SushiEventType eventType = XSDParser.generateEventTypeFromXSD(getFilePathForEdifactXSD(id, version, release), FileUtils.getFileNameWithoutExtension(getFilePathForEdifactXSD(id, version, release))); 
    return eventType;
  }

  private static String getFilePathForEdifactXSD(String id, String version,String release) {
    String path = System.getProperty("user.dir")+"/src/main/resources/";
    
    if (id.equals("BERMAN") && version.equals("D") && release.equals("03A")) {
      return path + "xsd-definitions/berman.xsd";
    }
        
    if (id.equals("IFTMCS") && version.equals("D") && release.equals("00B")) {
      return path + "xsd-definitions/iftmcs.xsd";
    }
    
    if (id.equals("COPRAR") && version.equals("D") && release.equals("95B")) {
      return path + "xsd-definitions/coprar.xsd";
    }

    if (id.equals("COARRI") && version.equals("D") && release.equals("95B")) {
      return path + "xsd-definitions/coarri.xsd";
    }
    
    if (id.equals("COPINO") && version.equals("D") && release.equals("95B")) {
      return path + "xsd-definitions/copino.xsd";
    }

    return null;
  }
  
  
  

}
