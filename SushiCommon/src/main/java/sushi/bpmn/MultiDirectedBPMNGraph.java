package sushi.bpmn;

import org.jbpt.graph.abs.AbstractMultiDirectedGraph;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;

/**
 * This class is intented as a edge-oriented representation of a {@link BPMNProcess}.
 * It produces a directed multi-graph for the RPST computation.
 * @author micha
 */
public class MultiDirectedBPMNGraph extends AbstractMultiDirectedGraph<DirectedBPMNEdge,AbstractBPMNElement> {
	
	@Override
	public DirectedBPMNEdge addEdge(AbstractBPMNElement s, AbstractBPMNElement t) {
		DirectedBPMNEdge e = new DirectedBPMNEdge(this,s,t);
		return e;
	}
	
}
