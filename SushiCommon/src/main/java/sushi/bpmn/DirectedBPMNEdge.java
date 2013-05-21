package sushi.bpmn;

import org.jbpt.graph.abs.AbstractDirectedEdge;
import org.jbpt.graph.abs.AbstractMultiDirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;

import sushi.bpmn.element.AbstractBPMNElement;

/**
 * Directed edge implementation
 * 
 */
public class DirectedBPMNEdge extends AbstractDirectedEdge<AbstractBPMNElement>
{
	protected DirectedBPMNEdge(AbstractMultiDirectedGraph<?, AbstractBPMNElement> g, AbstractBPMNElement source, AbstractBPMNElement target) {
		super(g, source, target);
	}
}