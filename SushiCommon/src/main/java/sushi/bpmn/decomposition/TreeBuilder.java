package sushi.bpmn.decomposition;

import java.util.Collection;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.event.collection.SushiTree;

/**
 * This class takes an BPMN process and tries to identify components with the {@link ComponentBuilder}.
 * The TreeBuilder returns a tree structure of the given BPMN process.
 * @author micha
 *
 */
public class TreeBuilder {
	
	private BPMNProcess process;
	private SushiTree<AbstractBPMNElement> processTree;
	
	public TreeBuilder(BPMNProcess process){
		this.process = (BPMNProcess) process.clone();
	}
	
	public SushiTree<AbstractBPMNElement> buildComponentTree(){
		this.buildComponents();
		this.processTree = generateProcessTree(this.process);
		return processTree;
	}

	private SushiTree<AbstractBPMNElement> generateProcessTree(BPMNProcess process) {
		SushiTree<AbstractBPMNElement> processTree = new SushiTree<AbstractBPMNElement>();
		addElementsToTree(process.getBPMNElementsWithOutSequenceFlows(), null, processTree);
		return processTree;
	}

	private void addElementsToTree(Collection<AbstractBPMNElement> elements, AbstractBPMNElement parent, SushiTree<AbstractBPMNElement> processTree) {
		for(AbstractBPMNElement element : elements){
			if(!processTree.contains(element)){
				if(element instanceof Component){
					processTree.addChild(parent, element);
					Component component = (Component) element;
					addElementsToTree(component.getChildren(), component, processTree);
				} else {
					processTree.addChild(parent, element);
				}
			}
		}
	}

	public void setProcess(BPMNProcess process) {
		this.process = process;
	}
	
	public BPMNProcess buildComponents(){
		ComponentBuilder componentBuilder = new ComponentBuilder(process);
		this.process = componentBuilder.buildComponents();
		return this.process;
	}

}
