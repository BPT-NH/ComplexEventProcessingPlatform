package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;

public class RigidComponent extends Component {

	private static final long serialVersionUID = 1L;

	public RigidComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
	}

}
