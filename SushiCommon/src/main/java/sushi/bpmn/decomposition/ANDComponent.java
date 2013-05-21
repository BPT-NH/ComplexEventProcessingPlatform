package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;

public class ANDComponent extends Component {
	
	private static final long serialVersionUID = 1L;

	public ANDComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
	}

}
