package sushi.persistence.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sushi.correlation.TimeCondition;
import sushi.event.SushiEventType;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;

@FixMethodOrder(MethodSorters.JVM)
public class ProcessPersistorTest implements PersistenceTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve(){
		storeExampleProcesses();
		assertTrue("Value should be 2, but was " + SushiProcess.findAll().size(),SushiProcess.findAll().size()==2);
		SushiProcess.removeAll();
		assertTrue("Value should be 0, but was " + SushiProcess.findAll().size(),SushiProcess.findAll().size()==0);
	}
	
	private void storeExampleProcesses() {
		SushiProcess firstProcess = new SushiProcess("Kino");
		firstProcess.addEventType(new SushiEventType("KinoEvent"));

		SushiProcess secondProcess = new SushiProcess("GET-Transport");
		secondProcess.addEventType(new SushiEventType("GET-Transport"));
		
		ArrayList<SushiProcess> processes = new ArrayList<SushiProcess>(Arrays.asList(firstProcess, secondProcess));
		assertTrue(SushiProcess.save(processes));
	}
	
	@Override
	@Test
	public void testFind(){
		storeExampleProcesses();
		assertTrue(SushiProcess.findAll().size() == 2);
		SushiEventType kino = SushiEventType.findByTypeName("KinoEvent");
		SushiProcess process = SushiProcess.findByEventType(kino).get(0);
		assertTrue(process.getEventTypes().size() == 1);
		assertTrue(process.getEventTypes().get(0).getTypeName().equals("KinoEvent"));
		assertTrue(process.getName().equals("Kino"));
		assertTrue(SushiProcess.findByName("GET-Transport" ).size() == 1);
	}
	
	@Override
	@Test
	public void testRemove(){
		storeExampleProcesses();
		List<SushiProcess> processes;
		processes = SushiProcess.findAll();
		assertTrue("Value should be 2, but was " + SushiProcess.findAll().size(),SushiProcess.findAll().size()==2);

		SushiProcess deleteProcess = processes.get(0);
		deleteProcess.remove();

		processes = SushiProcess.findAll();
		assertTrue(processes.size() == 1);
		
		assertTrue(processes.get(0).getID() != deleteProcess.getID());
	}
	
	@Test
	public void testProcessWithTimeCondition(){
		SushiEventType testEventType = new SushiEventType("KinoEventType");
		testEventType.save();
		TimeCondition timeCondition = new TimeCondition(testEventType, 1000, true, "Test=KinoTest");
		timeCondition.save();
		SushiProcess process = new SushiProcess("ProcessWithTimeCondition");
		process.addEventType(testEventType);
		process.setTimeCondition(timeCondition);
		process.save();
		assertTrue("Value should be 1, but was " + SushiProcess.findByName("ProcessWithTimeCondition").size(), 
				SushiProcess.findByName("ProcessWithTimeCondition").size() == 1);
		SushiProcess processFromDataBase = SushiProcess.findByName("ProcessWithTimeCondition").get(0);
		assertNotNull(processFromDataBase.getTimeCondition());
		TimeCondition timeConditionFromDatabase = processFromDataBase.getTimeCondition();
		assertTrue(timeCondition == timeConditionFromDatabase);
		
		timeConditionFromDatabase.remove();
		processFromDataBase = SushiProcess.findByName("ProcessWithTimeCondition").get(0);
		assertNull(processFromDataBase.getTimeCondition());
	}

}
