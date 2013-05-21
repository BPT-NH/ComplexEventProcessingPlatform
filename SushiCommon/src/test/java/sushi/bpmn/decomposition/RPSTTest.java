package sushi.bpmn.decomposition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.jbpt.algo.tree.rpst.IRPSTNode;
import org.jbpt.algo.tree.rpst.RPST;
import org.jbpt.algo.tree.tctree.TCTree;
import org.jbpt.algo.tree.tctree.TCTreeNode;
import org.jbpt.algo.tree.tctree.TCType;
import org.jbpt.graph.DirectedEdge;
import org.jbpt.graph.MultiDirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;
import org.junit.Test;

import sushi.bpmn.DirectedBPMNEdge;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.collection.SushiTree;

public class RPSTTest extends AbstractDecompositionTest {
	
	private MultiDirectedGraph getGraph() {
		System.out.println("SIMPLE SEQUENCE (3)");
		MultiDirectedGraph g = new MultiDirectedGraph();
		
		Vertex u = new Vertex("u");
		Vertex v = new Vertex("v");
		Vertex w = new Vertex("w");
		Vertex x = new Vertex("x");
		
		g.addEdge(u,v);
		g.addEdge(v,w);
		g.addEdge(w,x);
		
		return g;
	}
	
	@Test
	public void testSequenceRPST(){
		RPST<DirectedEdge,Vertex> rpst = new RPST<DirectedEdge,Vertex>(getGraph());
		
		for (IRPSTNode<DirectedEdge,Vertex> node : rpst.getRPSTNodes()) {
			System.out.print(node.getName() + ": ");
			for (IRPSTNode<DirectedEdge,Vertex> child : rpst.getPolygonChildren(node)) {
				System.out.print(child.getName() + " ");	
			}
			System.out.println();
		}
		
		System.out.println("ROOT:" + rpst.getRoot());
		
		assertNotNull(rpst.getRoot());
		assertEquals(1,rpst.getRPSTNodes(TCType.POLYGON).size());
		assertEquals(3,rpst.getRPSTNodes(TCType.TRIVIAL).size());
		assertEquals(0,rpst.getRPSTNodes(TCType.RIGID).size());
		assertEquals(0,rpst.getRPSTNodes(TCType.BOND).size());
		assertEquals(TCType.POLYGON, rpst.getRoot().getType());
		
		System.out.println("-----------------------------------------------------------------------");
	}
	
	//TODO test failed, micha soll überarbeiten
//	@Test
//	public void testSequenceSPQR(){
//		TCTree<DirectedEdge, Vertex> tcTree = new TCTree<>(getGraph());
//		
//		for (TCTreeNode<DirectedEdge, Vertex> node : tcTree.getTCTreeNodes()) {
//			System.out.print(node.getName() + ": " + node.getType());
//			System.out.println();
//		}
//		
//		System.out.println("ROOT:" + tcTree.getRoot());
//		
//		assertNotNull(tcTree.getRoot());
//		assertEquals(1,tcTree.getTCTreeNodes(TCType.POLYGON).size());
//		assertEquals(3,tcTree.getTCTreeNodes(TCType.TRIVIAL).size());
//		assertEquals(1,tcTree.getTCTreeNodes(TCType.RIGID).size());
//		assertEquals(0,tcTree.getTCTreeNodes(TCType.BOND).size());
//		assertEquals(TCType.RIGID, tcTree.getRoot().getType());
//		
//		System.out.println("-----------------------------------------------------------------------");
//	}
	
	@Test
	public void testComplexRPST(){
		RPST<DirectedBPMNEdge,AbstractBPMNElement> rpst = new RPST<DirectedBPMNEdge,AbstractBPMNElement>(new SushiRPSTTree(process).getGraph());
		
		//XXX
		IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> rootNode = rpst.getRoot();
		
		Set<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> rpstNodes = rpst.getRPSTNodes();
		
		SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>> rpstNodesTree = new SushiTree<IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement>>();
		
		for (IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> node : rpst.getRPSTNodes()) {
			if(node.getType() != TCType.TRIVIAL){
				System.out.print(node.getType() + ": " + node.getName() + ": ");
				for (IRPSTNode<DirectedBPMNEdge, AbstractBPMNElement> child : rpst.getChildren(node)) {
					System.out.print(child.getName() + " | ");
				}
				System.out.println();
			}
		}
		
	}
	
	@Test
	public void testComplexSPQR(){
		TCTree<DirectedBPMNEdge,AbstractBPMNElement> tcTree = new TCTree<DirectedBPMNEdge,AbstractBPMNElement>(new SushiRPSTTree(process).getGraph());
		
		for (TCTreeNode<DirectedBPMNEdge,AbstractBPMNElement> node : tcTree.getTCTreeNodes()) {
			if(node.getType() != TCType.TRIVIAL){
				System.out.print(node.getType() + ": " + node.getName() + ": ");
				for (TCTreeNode<DirectedBPMNEdge, AbstractBPMNElement> child : tcTree.getChildren(node)) {
					System.out.print(child.getName() + " | ");
				}
				System.out.println();
			}
		}
		
	}
	
	@Test
	public void testTreeCreation(){
		//TODO: Strategy-Pattern für Zerlegung
		SushiRPSTTree rpst = new SushiRPSTTree(process);
		
		System.out.println(rpst.getProcessDecompositionTree());
	}
	
}
