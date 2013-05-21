package sushi.monitoring.bpmn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.query.SushiPatternQuery;


/**
 * The central instance to get information for monitoring and analysing of the status of BPMN queries.
 * @author micha
 */
public class BPMNQueryMonitor {
	
	/*Eingehende Informationen:
	 * 		- ProcessInstances nach Korrelation
	 * 		- Queries für Process aus BPMN-Modell
	 * 		- Query-Matches von Esper-Listener
	 * */
	
	private static BPMNQueryMonitor instance;
	private Set<ProcessMonitor> processMonitors;

	public BPMNQueryMonitor(){
		processMonitors = new HashSet<ProcessMonitor>();
	}
	
	public static BPMNQueryMonitor getInstance() {
		//lazy initialize
		if (instance == null) {
			instance = new BPMNQueryMonitor();
		}		
		return instance;
	}
	
	public ProcessInstanceStatus getStatus(SushiProcessInstance processInstance){
		if(processInstance != null){
			ProcessMonitor processMonitor = getProcessMonitorForProcess(processInstance.getProcess());
			return processMonitor.getProcessInstanceStatus(processInstance);
		}
		return null;
	}
	
	public void getDetailedStatus(SushiProcessInstance processInstance){
		//TODO: Implementieren, sollte einen Tree mit den Queries zurückgeben und dem Status der einzelnen Queries/Components
	}

	public void addQueryForProcess(SushiPatternQuery query, SushiProcess process) {
		if(query != null && process != null){
			ProcessMonitor processMonitor = getProcessMonitorForProcess(process);
			processMonitor.addQuery(query);
		}
	}
	
	public void setQueryFinishedForProcessInstance(SushiPatternQuery query, SushiProcessInstance processInstance){
		if(query != null && processInstance != null){
			ProcessMonitor processMonitor = getProcessMonitorForProcess(processInstance.getProcess());
			processMonitor.setQueryFinishedForProcessInstance(query, processInstance);
		}
	}
	
	public ProcessMonitor getProcessMonitorForProcess(SushiProcess process){
		if(process != null){
			for(ProcessMonitor processMonitor : processMonitors){
				/*TODO: equals auf dem Process funktioniert nicht, da gleiche Prozesse mit verschiedenen IDs als Parameter kommen können:
				 * SushiProcess.equals überschreiben?
				 * Erstmal mit ID prüfen
				 */
				if(processMonitor.getProcess() != null && processMonitor.getProcess().getID() == process.getID()){
					return processMonitor;
				}
			}
			ProcessMonitor processMonitor = new ProcessMonitor(process);
			processMonitors.add(processMonitor);
			return processMonitor;
		}
		return null;
	}
	
	public List<ProcessInstanceMonitor> getProcessInstanceMonitors(SushiProcess process){
		if(process != null){
			ProcessMonitor processMonitor = getProcessMonitorForProcess(process);
			return new ArrayList<>(processMonitor.getProcessInstanceMonitors());
		} else {
			return null;
		}
		
	}

	public List<ProcessMonitor> getProcessMonitors() {
		return new ArrayList<ProcessMonitor>(processMonitors);
	}
	
	public static void reset(){
		instance = null;
	}

}
