package sushi.bpmn.decomposition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AbstractBPMNGateway;
import sushi.bpmn.element.BPMNAndGateway;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNTask;
import sushi.bpmn.element.BPMNXORGateway;
import sushi.util.SetUtil;

public class ComponentBuilder {
	
	private BPMNProcess process;
	
	public ComponentBuilder(BPMNProcess process){
		this.process = process;
	}
	
	public BPMNProcess buildComponents(){
		boolean processChanged;
		do{
			processChanged = false;
			processChanged = this.generateComponents();
		} while(processChanged);
		return this.process;
	}
	
	private boolean generateComponents(){
		//Alle Tasks zu Componenten oder SequenceComponents zusammenfügen
		boolean processChanged = false;
		boolean sequenceChanged =false;
		boolean minimalComponentChanged = false;
		boolean loopComponentChanged = false;
		do{
			//Tasks zu Sequences zusammenfassen
			sequenceChanged = buildTaskSequence();
			if(sequenceChanged){
				processChanged = true;
			}
		} while (sequenceChanged);
		//
		do{
			//Tasks zu Sequences zusammenfassen
			minimalComponentChanged = buildMinimalComponents();
			if(minimalComponentChanged){
				processChanged = true;
			}
		} while (minimalComponentChanged);
		do{
			//Tasks zu Sequences zusammenfassen
			loopComponentChanged = buildLoopComponents();
			if(loopComponentChanged){
				processChanged = true;
			}
		} while (loopComponentChanged);
		return processChanged;
	}

	private boolean buildLoopComponents() {
		List<AbstractBPMNGateway> splitGateways = getLoopGatewaysFromList(process.getAllSplitGateways());
		List<AbstractBPMNGateway> joinGateways = getLoopGatewaysFromList(process.getAllJoinGateways());
		for(AbstractBPMNGateway splitGateway : splitGateways) {
			for(AbstractBPMNGateway joinGateway : joinGateways){
				Set<AbstractBPMNElement> splitSuccessors = new HashSet<>();
				splitSuccessors.addAll(splitGateway.getSuccessors());
				splitSuccessors.add(splitGateway);
				splitSuccessors.add(joinGateway);
				Set<AbstractBPMNElement> joinPredecessors = new HashSet<>();
				joinPredecessors.addAll(joinGateway.getPredecessors());
				joinPredecessors.add(joinGateway);
				joinPredecessors.add(splitGateway);
				Set<AbstractBPMNElement> union = SetUtil.union(Arrays.asList(splitSuccessors, joinPredecessors));
				Set<AbstractBPMNElement> intersection = SetUtil.intersection(Arrays.asList(splitSuccessors, joinPredecessors));
				if(union.size() == (intersection.size() + 2)){
					Set<AbstractBPMNElement> splitPredecessors = new HashSet<>();
					splitPredecessors.addAll(splitGateway.getPredecessors());
					splitPredecessors.add(joinGateway);
					splitPredecessors.add(splitGateway);
					Set<AbstractBPMNElement> joinSuccessors = new HashSet<>();
					joinSuccessors.addAll(joinGateway.getSuccessors());
					joinSuccessors.add(joinGateway);
					joinSuccessors.add(splitGateway);
					if(splitPredecessors.containsAll(joinSuccessors) && joinSuccessors.containsAll(splitPredecessors)){
						//Entry-Point und Exit-Point außerhalb der Component ermitteln
						//Predecessor finden, der vor der Component liegt
						Set<AbstractBPMNElement> innerElements = AbstractBPMNElement.getElementsOnPathBetween(joinGateway, splitGateway);
						innerElements.addAll(AbstractBPMNElement.getElementsOnPathBetween(splitGateway, joinGateway));
						innerElements.addAll(Arrays.asList(joinGateway, splitGateway));
						AbstractBPMNElement entryPoint = null;
						for(AbstractBPMNElement predecessor : joinGateway.getPredecessors()){
							if(!innerElements.contains(predecessor)){
								entryPoint = predecessor;
							}
						}
						//Successor finden, der aus der Component herausführt
						AbstractBPMNElement exitPoint = null;
						for(AbstractBPMNElement successor : splitGateway.getSuccessors()){
							if(!innerElements.contains(successor)){
								exitPoint = successor;
							}
						}
						fold(entryPoint, joinGateway, splitGateway, exitPoint);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean buildMinimalComponents() {
		List<AbstractBPMNGateway> splitGateways = process.getAllSplitGateways();
		List<AbstractBPMNGateway> joinGateways = process.getAllJoinGateways();
		for(AbstractBPMNGateway splitGateway : splitGateways) {
			for(AbstractBPMNGateway joinGateway: joinGateways){
				Set<AbstractBPMNElement> directSuccessors = new HashSet<AbstractBPMNElement>();
				directSuccessors.addAll(splitGateway.getSuccessors());
				directSuccessors.add(splitGateway);
				directSuccessors.add(joinGateway);
				Set<AbstractBPMNElement> directPredecessors = new HashSet<AbstractBPMNElement>();
				directPredecessors.addAll(joinGateway.getPredecessors());
				directPredecessors.add(splitGateway);
				directPredecessors.add(joinGateway);
				if(directSuccessors.containsAll(directPredecessors) && directPredecessors.containsAll(directSuccessors)){
					fold(splitGateway, joinGateway);
					return true;
				}
			}
		}
		return false;
	}
	
	private List<AbstractBPMNGateway> getAcyclicGatewaysFromList(List<AbstractBPMNGateway> gateways) {
		List<AbstractBPMNGateway> matchingGateways = new ArrayList<AbstractBPMNGateway>();
		for(AbstractBPMNGateway gateway : gateways){
			if(!gateway.isLoopGateway(process)){
				matchingGateways.add(gateway);
			}
		}
		return matchingGateways;
	}
	
	private List<AbstractBPMNGateway> getLoopGatewaysFromList(List<AbstractBPMNGateway> gateways) {
		List<AbstractBPMNGateway> matchingGateways = new ArrayList<AbstractBPMNGateway>();
		for(AbstractBPMNGateway gateway : gateways){
			if(gateway.isLoopGateway(process)){
				matchingGateways.add(gateway);
			}
		}
		return matchingGateways;
	}
	
	private boolean buildTaskSequence() {
		List<BPMNTask> tasks = process.getAllTasks();
		if(!tasks.isEmpty()){
			BPMNTask selectedTask = tasks.get(0);
			BPMNTask firstTask = selectedTask;
			BPMNTask lastTask = selectedTask;
			//Prüfen, ob Vorgänger oder Nachfolger auch Task ist
			//TODO: Prüfung, dass nur ein Vorgänger existiert
			BPMNTask currentTask = null;
			AbstractBPMNElement previous = selectedTask.getPredecessors().iterator().next();
			if(previous instanceof BPMNTask){
				currentTask = (BPMNTask) selectedTask.getPredecessors().iterator().next();
			}
			while(currentTask != null) {
				firstTask = currentTask;
				previous = currentTask.getPredecessors().iterator().next();
				if(previous instanceof BPMNTask){
					currentTask = (BPMNTask) previous;
				} else {
					currentTask = null;
				}
			};
			
			AbstractBPMNElement succedding = selectedTask.getSuccessors().iterator().next();
			if(succedding instanceof BPMNTask){
				currentTask = (BPMNTask) selectedTask.getSuccessors().iterator().next();
			}
			while(currentTask != null) {
				lastTask = currentTask;
				succedding = currentTask.getSuccessors().iterator().next();
				if(succedding instanceof BPMNTask){
					currentTask = (BPMNTask) succedding;
				} else {
					currentTask = null;
				}
			};
			//TODO: SequnceComponent zurückgeben
			fold(firstTask, lastTask);
			return true;
		}
		return false;
	}

	private void fold(AbstractBPMNElement sourceElement, AbstractBPMNElement sinkElement) {
		fold(null, sourceElement, sinkElement, null);
	}
	
	private void fold(AbstractBPMNElement entryPoint, AbstractBPMNElement sourceElement, AbstractBPMNElement sinkElement, AbstractBPMNElement exitPoint) {
		if(entryPoint == null){
			entryPoint = sourceElement.getPredecessors().iterator().next();
		}
		if(exitPoint == null){
			exitPoint = sinkElement.getSuccessors().iterator().next();
		}
		Set<AbstractBPMNElement> children = AbstractBPMNElement.getElementsOnPathBetween(sourceElement, sinkElement);
		Component component;
		if(sourceElement instanceof BPMNXORGateway && ((BPMNXORGateway) sourceElement).isJoinGateway()){
			component = new LoopComponent(entryPoint, sourceElement, exitPoint, sinkElement);
			children.addAll(AbstractBPMNElement.getElementsOnPathBetween(sinkElement, sourceElement));
		} else if(sourceElement instanceof BPMNXORGateway){
			component = new XORComponent(entryPoint, sourceElement, exitPoint, sinkElement);
		} else if(sourceElement instanceof BPMNAndGateway){
			component = new ANDComponent(entryPoint, sourceElement, exitPoint, sinkElement);
		} else {
			component = new Component(entryPoint, sourceElement, exitPoint, sinkElement);
		}
		
		component.addChildren(children);
		process.removeBPMNElements(children);
		process.removeBPMNElement(sourceElement);
		process.removeBPMNElement(sinkElement);
		
		entryPoint.removeSuccessor(sourceElement);
		entryPoint.addSuccessor(component);
		component.addPredecessor(entryPoint);
		exitPoint.removePredecessor(sinkElement);
		exitPoint.addPredecessor(component);
		component.addSuccessor(exitPoint);
		process.addBPMNElement(component);
	}
	
//	private boolean buildComponentSequence() {
//		List<BPMNTask> tasks = process.getAllTasks();
//		if(!tasks.isEmpty()){
//			BPMNTask selectedTask = tasks.get(0);
//			BPMNTask firstTask = selectedTask;
//			BPMNTask lastTask = selectedTask;
//			//Prüfen, ob Vorgänger oder Nachfolger auch Task ist
//			//TODO: Prüfung, dass nur ein Vorgänger existiert
//			BPMNTask currentTask = null;
//			AbstractBPMNElement previous = selectedTask.getPredecessors().get(0);
//			if(previous instanceof BPMNTask){
//				currentTask = (BPMNTask) selectedTask.getPredecessors().get(0);
//			}
//			while(currentTask != null) {
//				firstTask = currentTask;
//				previous = currentTask.getPredecessors().get(0);
//				if(previous instanceof BPMNTask){
//					currentTask = (BPMNTask) previous;
//				} else {
//					currentTask = null;
//				}
//			};
//			
//			AbstractBPMNElement succedding = selectedTask.getSuccessors().get(0);
//			if(succedding instanceof BPMNTask){
//				currentTask = (BPMNTask) selectedTask.getSuccessors().get(0);
//			}
//			while(currentTask != null) {
//				lastTask = currentTask;
//				succedding = currentTask.getSuccessors().get(0);
//				if(succedding instanceof BPMNTask){
//					currentTask = (BPMNTask) succedding;
//				} else {
//					currentTask = null;
//				}
//			};
//			//TODO: SequnceComponent zurückgeben
//			fold(firstTask, lastTask);
//			return true;
//		}
//		return false;
//	}
	
	public BPMNProcess getProcess() {
		return process;
	}

	public void setProcess(BPMNProcess process) {
		this.process = process;
	}

}
