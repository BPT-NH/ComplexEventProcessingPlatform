package sushi.bpmn;

import org.jbpt.graph.abs.AbstractDirectedEdge;
import org.jbpt.graph.abs.AbstractMultiDirectedGraph;

import sushi.bpmn.element.AbstractBPMNElement;

/**
 * Edge between two {@link AbstractBPMNElement}s in a {@link MultiDirectedBPMNGraph}.
 * @author micha
 */
public class DirectedBPMNEdge extends AbstractDirectedEdge<AbstractBPMNElement> {
	
	protected DirectedBPMNEdge(AbstractMultiDirectedGraph<?, AbstractBPMNElement> g, AbstractBPMNElement source, AbstractBPMNElement target) {
		super(g, source, target);
	}
	
}