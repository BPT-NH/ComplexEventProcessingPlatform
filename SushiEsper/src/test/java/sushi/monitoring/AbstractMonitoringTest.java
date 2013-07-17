package sushi.monitoring;

import java.util.List;

import sushi.bpmn.decomposition.RPSTBuilder;
import sushi.bpmn.element.BPMNProcess;
import sushi.correlation.AttributeCorrelator;
import sushi.correlation.TimeCondition;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.monitoring.querycreation.IQueryCreationTest;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.query.bpmn.PatternQueryGenerator;
import sushi.xml.importer.BPMNParser;

/**
 * This class centralizes methods for monitoring and query creation tests.
 * All these tests should derive from these class. 
 * It provides a template method for the query creation and monitoring testing process.  
 * @author micha
 */
public abstract class AbstractMonitoringTest implements IQueryCreationTest{
	
	protected String filePath;
	protected BPMNProcess BPMNProcess;
	protected SushiProcess process;
	protected List<SushiEventType> eventTypes;
	
	/**
	 * Template method for the query creation for Esper.
	 * @param filePath
	 * @param processName
	 * @param correlationAttributes
	 */
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
	
	/**
	 * Creates the event types used in the test for query creation.
	 * @return
	 */
	protected abstract List<SushiEventType> createEventTypes();
	
	/**
	 * Sends the given event types to Esper and to the database.
	 * @param eventTypes
	 */
	protected void sendEventTypes(List<SushiEventType> eventTypes) {
		for(SushiEventType eventType : eventTypes){
			Broker.send(eventType);
		}
	}
	
	/**
	 * Creates a sample {@link BPMNProcess} and saves it in the database.
	 * @param filePath
	 * @return
	 */
	protected BPMNProcess createBPMNProcess(String filePath){
		BPMNProcess BPMNProcess = BPMNParser.generateProcessFromXML(filePath);
		BPMNProcess.save();
		return BPMNProcess;
	}
	
	/**
	 * Creates a sample {@link SushiProcess} with the given eventtypes, processName and BPMNProcess and saves it in the database.
	 * @param eventTypes
	 * @param bpmnProcess
	 * @param processName
	 * @return
	 */
	protected SushiProcess createProcess(List<SushiEventType> eventTypes, BPMNProcess bpmnProcess, String processName){
		SushiProcess process = new SushiProcess(processName, eventTypes);
		process.setBpmnProcess(bpmnProcess);
		process.save();
		return process;
	}
	
	/**
	 * Creates a correlation for the sample process.
	 * @param eventTypes
	 * @param correlationAttributes
	 * @param process
	 * @param timeCondition
	 */
	protected void correlate(List<SushiEventType> eventTypes, List<SushiAttribute> correlationAttributes, SushiProcess process, TimeCondition timeCondition) {
		AttributeCorrelator.correlate(eventTypes, correlationAttributes, process, timeCondition);
	}
	
	/**
	 * Creates the queries from the given {@link BPMNProcess}.
	 * Therefore, computes the RPST of the BPMNProcess and derives queries from that.
	 * Finally, the queries are registered at Esper.
	 * @param BPMNProcess
	 */
	protected void generateQueries(BPMNProcess BPMNProcess) {
		RPSTBuilder rpst = new RPSTBuilder(BPMNProcess);
		System.out.println(rpst.getProcessDecompositionTree());
		
		process.setProcessDecompositionTree(rpst.getProcessDecompositionTree());
		
		PatternQueryGenerator queryGenerator = new PatternQueryGenerator(rpst);
		queryGenerator.generateQueries();
	}
	
	/**
	 * Creates events for the different process instances and send them to the {@link Broker}.
	 * The ordering of the events is important for the monitoring of the execution and is assured because of the usage of a ordered list.
	 * @param eventTypes
	 */
	protected abstract void simulate(List<SushiEventType> eventTypes);

	/**
	 * Asserts, that all monitored queries have the right status after the simulation.
	 */
	protected abstract void assertQueryStatus();

	/**
	 * Removes all created test data from the database.
	 */
	public static void resetDatabase() {
		SushiEvent.removeAll();
		SushiEventType.removeAll();
		SushiProcess.removeAll();
		SushiProcessInstance.removeAll();
		//TODO: Ist es sinnvoll für den Test jedesmal wieder von einem frischen QueryEditor auszugehen?
		BPMNQueryMonitor.reset();
	}
	
	/**
	 * Creates the tree of attributes for the event types.
	 * @return
	 */
	protected SushiAttributeTree createAttributeTree() {
		SushiAttributeTree values = new SushiAttributeTree();
		values.addRoot(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER));
		values.addRoot(new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING));
		return values;
	}
	
}
