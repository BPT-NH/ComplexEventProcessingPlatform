package sushi.bpmn.decomposition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNEndEvent;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNXORGateway;

public class BPMNProcessPreprocessor {

	/**
	 * Tries to adapt parts of the BPMN process for a processing with the RPST.
	 * @param process
	 * @return
	 */
	public static BPMNProcess structureProcess(BPMNProcess process) {
		mergeStartEvents(process);
		mergeEndEvents(process);
		return process;
	}

	/**
	 * Creates one start event for the process, if there is more than one.
	 * @param process
	 */
	private static void mergeStartEvents(BPMNProcess process) {
		//TODO: Was ist, wenn StartEvent MonitoringPoints hat
		if(process.getStartEvents().size() > 1){
			BPMNStartEvent newStartEvent = new BPMNStartEvent("Start1", "MergedStartEvent", null);
			List<BPMNStartEvent> startEvents = new ArrayList<BPMNStartEvent>(process.getStartEvents());
			for(BPMNStartEvent startEvent : startEvents){
				for(AbstractBPMNElement successor : startEvent.getSuccessors()){
					AbstractBPMNElement.disconnectElements(startEvent, successor);
					AbstractBPMNElement.connectElements(newStartEvent, successor);
				}
				process.removeBPMNElement(startEvent);
			}
			process.addBPMNElement(newStartEvent);
		}
	}
	
	/**
	 * Creates one end event for the process, if there is more than one.
	 * The old events are removed and all predecessors of old end events are
	 * joined in one XOR-Gateway and a succeding new end event.
	 * @param process
	 */
	private static void mergeEndEvents(BPMNProcess process) {
		//TODO: MonitoringPoints der EndEvents in neues EndEvent übernehmen
		//Eigentlich müsste man die alten EndEvents mit XOR --> alte EndEvents --> XOR --> neues EndEvent zusammenführen, 
		//um nicht die Information über die MonitoringPoints zu verlieren 
		if(process.getEndEvents().size() > 1){
			BPMNEndEvent newEndEvent = new BPMNEndEvent("End1", "MergedEndEvent", null);
			BPMNXORGateway mergingXOR = new BPMNXORGateway("MergingXOR" + new Date().getTime(), "MergeXORBeforeEndEvent", null);
			List<BPMNEndEvent> endEvents = new ArrayList<BPMNEndEvent>(process.getEndEvents());
			for(BPMNEndEvent endEvent : endEvents){
				for(AbstractBPMNElement predecessor : endEvent.getPredecessors()){
					AbstractBPMNElement.disconnectElements(predecessor, endEvent);
					AbstractBPMNElement.connectElements(predecessor, mergingXOR);
				}
				process.removeBPMNElement(endEvent);
			}
			process.addBPMNElement(mergingXOR);
			process.addBPMNElement(newEndEvent);
			AbstractBPMNElement.connectElements(mergingXOR, newEndEvent);
		}
	}

}
