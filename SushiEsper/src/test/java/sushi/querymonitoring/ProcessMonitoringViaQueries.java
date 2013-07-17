package sushi.querymonitoring;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.eventhandling.Broker;
import sushi.monitoring.QueryMonitoringPoint;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

public class ProcessMonitoringViaQueries {

	private SushiQuery query1;
	private SushiProcess process1;
	private SushiProcessInstance processInstance1;

	@Before
	public void setup() {
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void monitoringTest(){
		SushiAttribute attribute = new SushiAttribute("TestAttribute", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree attributes = new SushiAttributeTree(attribute);
		SushiEventType eventType = new SushiEventType("TestType", attributes);
		Broker.send(eventType);
		
		query1 = new SushiQuery("NotifyTestType", "Select * from TestType", SushiQueryTypeEnum.LIVE);
		query1.save();
		query1.addToEsper();
		
		process1 = new SushiProcess("process1");
		process1.save();
		
		QueryMonitoringPoint.removeAll();
		QueryMonitoringPoint point = new QueryMonitoringPoint(process1, query1, 40, false);
		point.save();
		assertTrue(QueryMonitoringPoint.findAll().size() == 1);
		
		SushiMapTree tree = new SushiMapTree(attribute.getAttributeExpression(), "Wert");
		SushiEvent event = new SushiEvent(eventType, new Date(), tree);
		event.save();
		processInstance1 = new SushiProcessInstance();
		processInstance1.addEvent(event);
		processInstance1.save();
		event.addProcessInstance(processInstance1);
		event.merge();
		SushiStreamProcessingAdapter.getInstance().addEvent(event);
		
		assertTrue(SushiProcessInstance.findByID(processInstance1.getID()).getProgress() == 40);
	}

}
