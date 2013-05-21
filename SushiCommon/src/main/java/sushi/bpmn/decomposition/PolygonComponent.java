package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;

public class PolygonComponent extends Component {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new polygon component as a container.
	 * @param entryPoint - not included
	 * @param sourceElement - included
	 * @param exitPoint - not included
	 * @param sinkElement - included
	 */
	public PolygonComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
	}

}
