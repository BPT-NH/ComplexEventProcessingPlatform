package org.jbpt.test.petri.unfolding;

import junit.framework.TestCase;


public class SoundUnfoldingTest extends TestCase {
	
	/*public void test1() throws TransformationException, FileNotFoundException {
		ProcessModel p = new ProcessModel();
		
		Activity ti = new Activity("I");
		Activity to = new Activity("O");
		Activity ta = new Activity("A");
		Activity tb = new Activity("B");
		Activity tc = new Activity("C");
		Activity td = new Activity("D");
	
		Gateway s1 = new AndGateway();
		Gateway s2 = new AndGateway();
		Gateway s3 = new XorGateway();
		
		Gateway j1 = new XorGateway();
		Gateway j2 = new AndGateway();
		Gateway j3 = new AndGateway();
		
		p.addFlowNode(ti);
		p.addFlowNode(to);
		p.addFlowNode(ta);
		p.addFlowNode(tb);
		p.addFlowNode(tc);
		p.addFlowNode(td);
		p.addFlowNode(s1);
		p.addFlowNode(s2);
		p.addFlowNode(s3);
		p.addFlowNode(j1);
		p.addFlowNode(j2);
		p.addFlowNode(j3);
		
		p.addControlFlow(ti,s1);
		p.addControlFlow(s1,s2);
		p.addControlFlow(s1,s3);
		p.addControlFlow(s2,ta);
		p.addControlFlow(s2,tb);
		p.addControlFlow(ta,j1);
		p.addControlFlow(tb,j1);
		p.addControlFlow(s3,tc);
		p.addControlFlow(s3,td);
		p.addControlFlow(tc,j2);
		p.addControlFlow(td,j2);
		p.addControlFlow(j1,j3);
		p.addControlFlow(j2,j3);
		p.addControlFlow(j3,to);
		
		IOUtils.toFile("model.dot", p.toDOT());
		
		NetSystem net = ProcessModel2NetSystem.transform(p);
		int cp = 1; int ct = 1;
		for (Place place : net.getPlaces()) place.setName("p"+cp++);
		for (Transition trans : net.getTransitions()) trans.setName("t"+ct++);
		net.loadNaturalMarking();
		IOUtils.toFile("net.dot", net.toDOT());
		
		SoundUnfolding unf = new SoundUnfolding(net);
		IOccurrenceNet bpnet = unf.getOccurrenceNet();
		IOUtils.toFile("unf.dot", bpnet.toDOT());
		IOUtils.toFile("unf_unsafe.dot", bpnet.toDOTcs(unf.getLocallyUnsafeConditions()));
		IOUtils.toFile("unf_deadlock.dot", bpnet.toDOTcs(unf.getLocalDeadlockConditions()));
		
		System.out.println(unf.getLocallyUnsafeConditions());
		System.out.println(unf.getLocalDeadlockConditions());
	}*/
}
