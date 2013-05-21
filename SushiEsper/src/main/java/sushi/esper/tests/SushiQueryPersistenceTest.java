package sushi.esper.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sushi.persistence.Persistor;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

@FixMethodOrder(MethodSorters.JVM)
public class SushiQueryPersistenceTest {

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}

	@Test
	public void testSaveQueries(){
		SushiQuery q1 = new SushiQuery("testquery", "select * from stuff", SushiQueryTypeEnum.ONDEMAND);
		q1 = q1.save();
		assertTrue(q1 != null);
	}
	
	@Test
	public void testRemoveQueries(){
		SushiQuery q1 = new SushiQuery("testquery", "select * from stuff", SushiQueryTypeEnum.ONDEMAND);
		q1 = q1.save();
		q1.remove();
		assertTrue(SushiQuery.findQueryByTitle("testquery") == null);
		SushiQuery q2 = new SushiQuery("testquery2", "select * from stuff2", SushiQueryTypeEnum.ONDEMAND);
		q2 = q2.save();
		SushiQuery q3 = new SushiQuery("testquery3", "select * from stuff3", SushiQueryTypeEnum.ONDEMAND);
		q3 = q3.save();
		SushiQuery.removeAll();
		assertTrue(SushiQuery.findQueryByTitle("testquery2") == null);
		assertTrue(SushiQuery.findQueryByTitle("testquery3") == null);
	}
	
	@Test
	public void testRemoveQueryWithTitle(){
		SushiQuery q1 = new SushiQuery("testquery", "select * from stuff", SushiQueryTypeEnum.ONDEMAND);
		q1 = q1.save();
		SushiQuery.removeQueryWithTitle("testquery");
		assertTrue(SushiQuery.findQueryByTitle("testquery") == null);
	}
	
	@Test
	public void testGetAllTitlesOfQueries(){
		SushiQuery q1 = new SushiQuery("testquery", "select * from stuff", SushiQueryTypeEnum.ONDEMAND);
		q1 = q1.save();
		SushiQuery q2 = new SushiQuery("testquery2", "select * from stuff2", SushiQueryTypeEnum.ONDEMAND);
		q2 = q2.save();
		SushiQuery q3 = new SushiQuery("testquery3", "select * from stuff3", SushiQueryTypeEnum.ONDEMAND);
		q3 = q3.save();
		System.out.println(SushiQuery.getAllTitlesOfOnDemandQueries());
		ArrayList<String> names = new ArrayList<String>(Arrays.asList("testquery", "testquery2", "testquery3"));
		assertTrue(SushiQuery.getAllTitlesOfQueries().containsAll(names));
		assertTrue(SushiQuery.getAllTitlesOfOnDemandQueries().containsAll(names));
	}
	
	@Test
	public void testGetQueryByTitle(){
		SushiQuery q1 = new SushiQuery("testquery", "select * from stuff", SushiQueryTypeEnum.ONDEMAND);
		q1 = q1.save();
		assertTrue("should find query in db, but did not", SushiQuery.findQueryByTitle("testquery")==q1);
	}
	
}
