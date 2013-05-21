package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;

public class LoopComponent extends Component {

	private static final long serialVersionUID = 1L;

	public LoopComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint,
			AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
	}
	
	
}
