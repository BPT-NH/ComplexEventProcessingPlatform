package sushi.querycreation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jbpt.algo.tree.rpst.IRPSTNode;
import org.jbpt.algo.tree.rpst.RPST;
import org.jbpt.algo.tree.tctree.TCType;
import org.junit.Test;

import sushi.bpmn.DirectedBPMNEdge;
import sushi.bpmn.MultiDirectedBPMNGraph;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.xml.importer.SignavioBPMNParser;

 
 public class ComplexProcessTest implements IQueryCreationTest{
	
	private static String filePath = System.getProperty("user.dir") + "/src/test/resources/bpmn/complexProcess.bpmn20.xml";
	
	private BPMNProcess BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
	
	@Test
	@Override
	public void testImport() {
		BPMNProcess BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 21);
		assertTrue(BPMNProcess.getStartEvent().getId().equals("sid-EC585815-8EAC-411C-89C2-553ACA85CF5A"));
	}
	
	@Test
	@Override
	public void testQueryCreation() {
		// TODO Auto-generated method stub
		MultiDirectedBPMNGraph g = new MultiDirectedBPMNGraph();
		for(AbstractBPMNElement element : BPMNProcess.getBPMNElementsWithOutSequenceFlows()){
			for(AbstractBPMNElement successor : element.getSuccessors()){
				g.addEdge(element,successor);
			}
		}
		
		RPST<DirectedBPMNEdge,AbstractBPMNElement> rpst = new RPST<DirectedBPMNEdge,AbstractBPMNElement>(g);
		
		for (IRPSTNode<DirectedBPMNEdge,AbstractBPMNElement> node : rpst.getRPSTNodes()) {
			System.out.print(node.getName() + ": ");
			for (IRPSTNode<DirectedBPMNEdge,AbstractBPMNElement> child : rpst.getPolygonChildren(node)) {
				System.out.print(child.getName() + " ");	
			}
			System.out.println();
		}
		
		System.out.println("ROOT:" + rpst.getRoot());
		
		assertNotNull(rpst.getRoot());
		assertEquals(9,rpst.getRPSTNodes(TCType.POLYGON).size());
		assertEquals(24,rpst.getRPSTNodes(TCType.TRIVIAL).size());
		assertEquals(0,rpst.getRPSTNodes(TCType.RIGID).size());
		assertEquals(4,rpst.getRPSTNodes(TCType.BOND).size());
		assertEquals(TCType.POLYGON, rpst.getRoot().getType());
		
		System.out.println("-----------------------------------------------------------------------");
	}
 }
