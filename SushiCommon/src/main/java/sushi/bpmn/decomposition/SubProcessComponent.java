package sushi.bpmn.decomposition;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNSubProcess;

public class SubProcessComponent extends Component {

	private static final long serialVersionUID = 1L;
	private BPMNSubProcess subProcess;

	public SubProcessComponent(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement exitPoint, AbstractBPMNElement sinkElement) {
		super(entryPoint, sourceElement, exitPoint, sinkElement);
		this.type = IPattern.SUBPROCESS;
	}

	public void setSubProcess(BPMNSubProcess subProcess) {
		this.subProcess = subProcess;
	}

	public BPMNSubProcess getSubProcess() {
		return subProcess;
	}

}
