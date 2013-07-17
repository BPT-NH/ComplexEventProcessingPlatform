package sushi.querymonitoring;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.monitoring.QueryMonitoringPoint;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

public class QueryMonitoringPointPersistenceTest{

	private SushiEventType type1;
	private SushiEventType type2;
	private SushiQuery query1;
	private SushiQuery query2;
	private SushiProcess process1;
	private SushiProcess process2;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void testStoreAndRetrieve() {
		storeExampleQueryMonitoringPoints();
		assertTrue("Value should be 2, but was " + QueryMonitoringPoint.findAll().size(), QueryMonitoringPoint.findAll().size()==2);
		QueryMonitoringPoint.removeAll();
		assertTrue("Value should be 0, but was " + QueryMonitoringPoint.findAll().size(), QueryMonitoringPoint.findAll().size()==0);
	}

	@Test
	public void testFind() {
		storeExampleQueryMonitoringPoints();
		assertTrue(QueryMonitoringPoint.findAll().size() == 2);
		
		assertTrue(QueryMonitoringPoint.findByQuery(query1).size() == 1);
		assertTrue(QueryMonitoringPoint.findByQuery(query1).get(0).getProcess().getName().equals(process1.getName()));
	}

	@Test
	public void testRemove() {
		storeExampleQueryMonitoringPoints();
		List<QueryMonitoringPoint> points;
		points = QueryMonitoringPoint.findAll();
		assertTrue(points.size() == 2);

		QueryMonitoringPoint deletedPoint = points.get(0);
		deletedPoint.remove();

		points = QueryMonitoringPoint.findAll();
		assertTrue(points.size() == 1);
		
		assertTrue(points.get(0).getID() != deletedPoint.getID());
	}
	
	@Test
	public void testRemoveQueryWithPoint() {
		storeExampleQueryMonitoringPoints();
		List<QueryMonitoringPoint> points = QueryMonitoringPoint.findAll();
		assertTrue(points.size() == 2);

		QueryMonitoringPoint deletedPoint = points.get(0);
		SushiQuery query = deletedPoint.getQuery();
		query.remove();

		List<SushiQuery> queries = SushiQuery.getAllLiveQueries();
		assertTrue(queries.size() == 1);
		assertTrue(queries.get(0).getID() != query.getID());
		
		//monitoringpoint was deleted as well
		points = QueryMonitoringPoint.findAll();
		assertTrue(points.size() == 1);
		assertTrue(points.get(0).getID() != deletedPoint.getID());
	}

	
	
	private void storeExampleQueryMonitoringPoints() {
		type1 = new SushiEventType("ToNotify");
		type1.save();
		query1 = new SushiQuery("allToNotify1", "Select * from ToNotify", SushiQueryTypeEnum.LIVE);
		query1.save();
		process1 = new SushiProcess("testProcess");
		process1.save();
		QueryMonitoringPoint point1 = new QueryMonitoringPoint(process1, query1, 30, false);
		point1.save();
		
		type2 = new SushiEventType("ToNotify2");
		type2.save();
		query2 = new SushiQuery("allToNotify2", "Select * from ToNotify2", SushiQueryTypeEnum.LIVE);
		query2.save();
		process2 = new SushiProcess("testProcess2");
		process2.save();
		QueryMonitoringPoint point2 = new QueryMonitoringPoint(process2, query2, 40, true);
		point2.save();
		
	}

}
