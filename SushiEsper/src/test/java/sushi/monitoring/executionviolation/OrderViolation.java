package sushi.monitoring.executionviolation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.monitoring.AbstractMonitoringTest;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.monitoring.bpmn.DetailedQueryStatus;
import sushi.monitoring.bpmn.ProcessInstanceStatus;
import sushi.monitoring.bpmn.ViolationStatus;
import sushi.monitoring.querycreation.AbstractQueryCreationTest;
import sushi.persistence.Persistor;
import sushi.process.SushiProcessInstance;
import sushi.query.PatternQueryType;
import sushi.xml.importer.BPMNParser;

/**
 * The test proofs the monitoring and detection of a violation of the order of process elements.
 * @author micha
 */
public class OrderViolation extends AbstractMonitoringTest{

	@Before
	public void setup(){
		filePath = System.getProperty("user.dir")+"/src/test/resources/bpmn/SimpleSequence.bpmn20.xml";
		Persistor.useTestEnviroment();
	}

	@Test
	@Override
	public void testImport() {
		BPMNProcess = BPMNParser.generateProcessFromXML(filePath);
		assertNotNull(BPMNProcess);
		assertTrue(BPMNProcess.getBPMNElementsWithOutSequenceFlows().size() == 5);
	}

	@Test
	@Override
	public void testQueryCreation() {
		queryCreationTemplateMethod(filePath, "SimpleProcess", Arrays.asList(new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER)));
	}

	@Override
	protected List<SushiEventType> createEventTypes() {
		List<SushiEventType> eventTypes = new ArrayList<SushiEventType>();
		
		SushiAttributeTree values;
		
		values = createAttributeTree();
		SushiEventType first = new SushiEventType("FirstEvent", values, "Timestamp");
		
		values = createAttributeTree();
		SushiEventType second = new SushiEventType("SecondEvent", values, "Timestamp");
		
		values = createAttributeTree();
		SushiEventType third = new SushiEventType("ThirdEvent", values, "Timestamp");
		
		eventTypes.add(first);
		
		eventTypes.add(third);
		//Order-Violation
		eventTypes.add(second);
		
		return eventTypes;
	}

	@Override
	protected void simulate(List<SushiEventType> eventTypes) {
		for(SushiEventType eventType : eventTypes){
			SushiMapTree<String, Serializable> values;
			SushiEvent event;
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 1);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
			
			values = new SushiMapTree<String, Serializable>();
			values.put("Location", 2);
			values.put("Movie", "Movie Name");
			event = new SushiEvent(eventType, new Date(), values);
			Broker.send(event);
		}
	}
	
	@AfterClass
	public static void tearDown() {
		AbstractQueryCreationTest.resetDatabase();
	}

	@Override
	protected void assertQueryStatus() {
		//Auf Listener hören
		BPMNQueryMonitor queryMonitor = BPMNQueryMonitor.getInstance();
		for(SushiProcessInstance processInstance : SushiProcessInstance.findAll()){
			assertTrue(queryMonitor.getStatus(processInstance) == ProcessInstanceStatus.Finished);
			boolean orderViolationStatusContained = false;
			for(DetailedQueryStatus detailedQueryStatus : queryMonitor.getDetailedStatus(processInstance).getElements()){
				if(detailedQueryStatus.getViolationStatus().contains(ViolationStatus.Order) && detailedQueryStatus.getQuery().getPatternQueryType().equals(PatternQueryType.SEQUENCE)){
					orderViolationStatusContained = true;
				}
			}
			assertTrue(orderViolationStatusContained);
		}
	}

}
