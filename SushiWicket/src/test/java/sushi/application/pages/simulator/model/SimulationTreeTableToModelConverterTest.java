package sushi.application.pages.simulator.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.bpmn.decomposition.ANDComponent;
import sushi.bpmn.decomposition.LoopComponent;
import sushi.bpmn.decomposition.SequenceComponent;
import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTree;
import sushi.util.SetUtil;

public class SimulationTreeTableToModelConverterTest {
	
	private SushiTree<Object> tree;
	private SushiEventType e1;
	private SushiEventType e2;

	@Before
	public void setup(){
		tree = new SushiTree<Object>();
		SequenceComponent sequence1 = new SequenceComponent(null, null, null, null);
		tree.add(sequence1);
		XORComponent xor1 = new XORComponent(null, null, null, null);
		tree.addChild(sequence1, xor1);
		e1 = new SushiEventType("E1");
		e2 = new SushiEventType("E2");
		tree.addChild(xor1, e1);
		tree.addChild(xor1, e2);
		ANDComponent and1 = new ANDComponent(null, null, null, null);
		tree.addChild(sequence1, and1);
		LoopComponent loop1 = new LoopComponent(null, null, null, null);
		tree.addChild(and1, loop1);
		tree.addChild(loop1, e2);
		tree.addChild(loop1, e1);
		SequenceComponent sequence2 = new SequenceComponent(null, null, null, null);
		tree.addChild(and1, sequence2);
		tree.addChild(sequence2, e1);
		tree.addChild(sequence2, e2);
	}
	
	@Test
	public void testConversion(){
		SimulationTreeTableToModelConverter converter = new SimulationTreeTableToModelConverter();
		BPMNProcess process = converter.convertTreeToModel(tree);
		assertTrue("Should be 14, but was " + process.getBPMNElementsWithOutSequenceFlows().size(), process.getBPMNElementsWithOutSequenceFlows().size() == 14);
		assertNotNull(process.getStartEvent());
		BPMNStartEvent startEvent = process.getStartEvent();
		assertTrue(startEvent.getSuccessors().size() == 1);
		assertTrue(startEvent.getSuccessors().iterator().next() instanceof BPMNXORGateway);
		BPMNXORGateway xor1 = (BPMNXORGateway) startEvent.getSuccessors().iterator().next();
		List<AbstractBPMNElement> successors = SetUtil.asList(xor1.getSuccessors());
		assertTrue(successors.size() == 2);
		assertTrue(successors.get(0) instanceof BPMNTask);
		assertTrue(successors.get(1) instanceof BPMNTask);
		assertTrue(successors.get(0).getSuccessors().iterator().next() instanceof BPMNXORGateway);
		BPMNXORGateway xor2 = (BPMNXORGateway) successors.get(0).getSuccessors().iterator().next();
		
		assertTrue(xor2.getSuccessors().size() == 1);
		assertTrue("Should be BPMNAndGateway, but was " + xor2.getSuccessors().iterator().next(),xor2.getSuccessors().iterator().next() instanceof BPMNAndGateway);
		BPMNAndGateway and1 = (BPMNAndGateway) xor2.getSuccessors().iterator().next();
		assertTrue(and1.getSuccessors().size() == 2);
		successors = SetUtil.asList(and1.getSuccessors());
		for(AbstractBPMNElement successor : successors){
			if(successor instanceof BPMNTask){
				BPMNTask task = (BPMNTask)successor;
				if(task.getMonitoringPoints().get(0).getEventType().equals(e1)){
					assertTrue(task.getSuccessors().iterator().next() instanceof BPMNTask);
				}
			} else {
				assertTrue(successor instanceof BPMNXORGateway);
				BPMNXORGateway loopEntry = (BPMNXORGateway) successor;
				assertTrue(loopEntry.getPredecessors().size() == 2);
				assertTrue(loopEntry.getSuccessors().size() == 1);
				assertTrue(loopEntry.getSuccessors().iterator().next() instanceof BPMNTask);
				BPMNTask loopTask1 = (BPMNTask) loopEntry.getSuccessors().iterator().next();
				assertTrue(loopTask1.getSuccessors().size() == 1);
				assertTrue(loopTask1.getSuccessors().iterator().next() instanceof BPMNTask);
				BPMNTask loopTask2 = (BPMNTask) loopTask1.getSuccessors().iterator().next();
				assertTrue(loopTask2.getSuccessors().size() == 1);
				assertTrue(loopTask2.getSuccessors().iterator().next() instanceof BPMNXORGateway);
				BPMNXORGateway loopExit = (BPMNXORGateway) loopTask2.getSuccessors().iterator().next();
				assertTrue(loopExit.getSuccessors().size() == 2);
				for(AbstractBPMNElement loopExitSuccessor : loopExit.getSuccessors()){
					if(loopExitSuccessor instanceof BPMNAndGateway){
						assertTrue(loopExitSuccessor.getPredecessors().size() == 2);
						assertTrue(loopExitSuccessor.getSuccessors().size() == 1);
						assertTrue(loopExitSuccessor.getSuccessors().contains(process.getEndEvent()));
					}
				}
			}
		}
	}

}
