package sushi.xml.importer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNSubProcess;
import sushi.xml.importer.SignavioBPMNParser;

public class SubProcessSignavioBPMNParserTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/Automontage_TwoTerminal.bpmn20.xml";
	private BPMNProcess BPMNProcess;
	
	@Test
	public void testImport() {
		BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 7);
		assertTrue(BPMNProcess.hasSubProcesses());
		BPMNSubProcess subProcess = BPMNProcess.getSubProcesses().get(0);
		assertNotNull(subProcess);
		assertFalse(subProcess.getStartEvent().getSuccessors().isEmpty());
	}

}
