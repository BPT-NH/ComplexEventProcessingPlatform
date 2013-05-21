package sushi.querycreation.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import sushi.bpmn.decomposition.SushiRPSTTree;
import sushi.bpmn.element.BPMNProcess;
import sushi.correlation.Correlator;
import sushi.correlation.TimeCondition;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.monitoring.bpmn.ProcessInstanceStatus;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.query.bpmn.PatternQueryGenerator;
import sushi.xml.importer.SignavioBPMNParser;

public abstract class AbstractQueryCreationTest implements IQueryCreationTest{
	
	protected String filePath;
	protected BPMNProcess BPMNProcess;
	protected SushiProcess process;
	protected List<SushiEventType> eventTypes;
	
	protected void queryCreationTemplateMethod(String filePath, String processName, List<SushiAttribute> correlationAttributes){
		this.filePath = filePath;
		eventTypes = createEventTypes();
		sendEventTypes(eventTypes);
		
		BPMNProcess = createBPMNProcess(filePath);
		
		//Prozess und Corelation anlegen
		process = createProcess(eventTypes, BPMNProcess, processName);
		
		correlate(eventTypes, correlationAttributes, process, null);
		 
		generateQueries(BPMNProcess);
		
		simulate(eventTypes);
		
		assertQueryStatus();
	}
	
	protected abstract List<SushiEventType> createEventTypes();
	
	protected void sendEventTypes(List<SushiEventType> eventTypes) {
		for(SushiEventType eventType : eventTypes){
			Broker.send(eventType);
		}
	}
	
	protected BPMNProcess createBPMNProcess(String filePath){
		BPMNProcess BPMNProcess = SignavioBPMNParser.generateProcessFromXML(filePath);
		BPMNProcess.save();
		return BPMNProcess;
	}
	
	protected SushiProcess createProcess(List<SushiEventType> eventTypes, BPMNProcess bpmnProcess, String processName){
		SushiProcess process = new SushiProcess(processName, eventTypes);
		process.setBpmnProcess(bpmnProcess);
		process.save();
		return process;
	}
	
	protected void correlate(List<SushiEventType> eventTypes, List<SushiAttribute> correlationAttributes, SushiProcess process, TimeCondition timeCondition) {
		Correlator.correlate(eventTypes, correlationAttributes, process, timeCondition);
	}
	
	/**
	 * BPMNProcess in RPST umwandeln
	 * RPST in Queries umwandeln
	 * Queries erzeugen und bei Esper registrieren
	 * @param BPMNProcess
	 */
	protected void generateQueries(BPMNProcess BPMNProcess) {
		SushiRPSTTree rpst = new SushiRPSTTree(BPMNProcess);
		System.out.println(rpst.getProcessDecompositionTree());
		PatternQueryGenerator queryGenerator = new PatternQueryGenerator(rpst);
		queryGenerator.generateQueries();
	}
	
	/**
	 * Events für verschiedene Prozessinstanzen erzeugen und an Broker senden
	 * Reihenfolge der Events ist wichtig (über Liste abgebildet)
	 * @param eventTypes
	 */
	protected abstract void simulate(List<SushiEventType> eventTypes);

	protected void assertQueryStatus() {
		//Auf Listener hören
		BPMNQueryMonitor queryMonitor = BPMNQueryMonitor.getInstance();
		for(SushiProcessInstance processInstance : SushiProcessInstance.findAll()){
			assertTrue(queryMonitor.getStatus(processInstance) == ProcessInstanceStatus.Finished);
		}
	}

	public static void resetDatabase() {
		SushiEvent.removeAll();
		SushiEventType.removeAll();
		SushiProcess.removeAll();
		SushiProcessInstance.removeAll();
		//TODO: Ist es sinnvoll für den Test jedesmal wieder von einem frischen QueryEditor auszugehen?
		BPMNQueryMonitor.reset();
	}
	
	protected SushiAttributeTree createAttributeTree() {
		SushiAttributeTree values = new SushiAttributeTree();
		values.addRoot(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER));
		values.addRoot(new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING));
		return values;
	}
	
}
