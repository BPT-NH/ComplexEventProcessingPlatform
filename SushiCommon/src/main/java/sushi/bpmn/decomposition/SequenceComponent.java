package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;

public class SequenceComponent extends Component {
	
	public SequenceComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
	}

}
