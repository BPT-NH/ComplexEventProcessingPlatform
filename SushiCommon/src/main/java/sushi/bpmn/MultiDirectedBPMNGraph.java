package sushi.bpmn;

import org.jbpt.graph.abs.AbstractMultiDirectedGraph;

import sushi.bpmn.element.AbstractBPMNElement;

/**
 * Directed multi graph implementation
 * @author micha
 *
 */
public class MultiDirectedBPMNGraph extends AbstractMultiDirectedGraph<DirectedBPMNEdge,AbstractBPMNElement>
{
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.hypergraph.abs.AbstractMultiDirectedHyperGraph#addEdge(de.hpi.bpt.hypergraph.abs.IVertex, de.hpi.bpt.hypergraph.abs.IVertex)
	 */
	@Override
	public DirectedBPMNEdge addEdge(AbstractBPMNElement s, AbstractBPMNElement t) {
		DirectedBPMNEdge e = new DirectedBPMNEdge(this,s,t);
		return e;
	}
}
