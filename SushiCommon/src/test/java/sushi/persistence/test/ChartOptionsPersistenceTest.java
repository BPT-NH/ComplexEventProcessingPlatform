package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistor;
import sushi.visualisation.SushiChartTypeEnum;
import sushi.visualisation.SushiSimpleChartOptions;

public class ChartOptionsPersistenceTest implements PersistenceTest {

	private SushiEventType eventType;
	private SushiSimpleChartOptions options;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}

	private void storeExampleOptions() {
		eventType = new SushiEventType("Tsun");
		eventType.save();
		options = new SushiSimpleChartOptions(eventType, "attribute", SushiAttributeTypeEnum.STRING, "chartTitle", SushiChartTypeEnum.BAR, 1);
		options.save();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve() {
		storeExampleOptions();
		assertTrue("Value should be 1, but was " + SushiSimpleChartOptions.findAll().size(),SushiSimpleChartOptions.findAll().size()==1);
		SushiSimpleChartOptions.removeAll();
		assertTrue("Value should be 0, but was " + SushiSimpleChartOptions.findAll().size(),SushiSimpleChartOptions.findAll().size()==0);
	}

	@Override
	@Test
	public void testFind() {
		storeExampleOptions();
		assertTrue(SushiSimpleChartOptions.findByID(options.getID()) == options);
		
	}

	@Override
	@Test
	public void testRemove() {
		storeExampleOptions();
		options.remove();
		assertTrue(SushiSimpleChartOptions.findByID(options.getID()) != options);
		
	}
	
	@Test
	public void testDeleteEventType() {
		storeExampleOptions();
		eventType.remove();
		assertTrue( SushiEventType.findByID(eventType.getID()) == null ||  ! SushiEventType.findByID(eventType.getID()).getTypeName().equals(eventType.getTypeName()));
	}

}
