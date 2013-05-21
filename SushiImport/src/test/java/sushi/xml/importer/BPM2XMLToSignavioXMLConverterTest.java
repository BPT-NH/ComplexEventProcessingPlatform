package sushi.xml.importer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import signavio.xml.converter.SignavioBPMNProcess;
import sushi.xml.importer.BPM2XMLToSignavioXMLConverter;

public class BPM2XMLToSignavioXMLConverterTest {
	
	private String emptyBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/empty_process.bpmn20.xml";
	private String startEventBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/onlyStartEventProcess.bpmn20.xml";
	private String endEventBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/onlyEndEventProcess.bpmn20.xml";
	private String taskBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/onlyTaskProcess.bpmn20.xml";
	private String edgeBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/onlyEdgeProcess.bpmn20.xml";
	private String simpleBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/simpleProcess.bpmn20.xml";
	private String testBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/newTestModel.bpmn20.xml";
	private String startMessageEventBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/onlyStartMessageEvent.bpmn20.xml";
	private String multipleStartEventsBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/multipleStartEvents.bpmn20.xml";
	private String xorBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/xorGateway.bpmn20.xml";
	private String andBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/and.bpmn20.xml";
	private String complexBpmn2FilePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/complexProcess.bpmn20.xml";
	@Test
	public void testConversion(){
//		SignavioBPMNProcess process = BPM2XMLToSignavioXMLConverter.parseBPM2XML(bpm2FilePath);
//		SignavioBPMNProcess process = new SignavioBPMNProcess();
//		process.addPropertyValue("targetnamespace", "http://www.signavio.com/bpmn20");
//		process.addPropertyValue("expressionlanguage", "http://www.w3.org/1999/XPath");
//		process.addPropertyValue("typelanguage", "http://www.w3.org/2001/XMLSchema");
//		
//		System.out.println(process.generateSignavioXMLString());
		
		BPM2XMLToSignavioXMLConverter converter = new BPM2XMLToSignavioXMLConverter(emptyBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(startEventBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(taskBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(endEventBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(edgeBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(simpleBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(testBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(startMessageEventBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(multipleStartEventsBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(xorBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(andBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
		
		converter = new BPM2XMLToSignavioXMLConverter(complexBpmn2FilePath);
		converter.generateSignavioXMLFromBPM2XML();
	}
	
	@Test
	public void testParsing(){
		BPM2XMLToSignavioXMLConverter converter = new BPM2XMLToSignavioXMLConverter(emptyBpmn2FilePath);
		converter.parseBPM2XML();
	}

}
