package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.persistence.Persistor;
import sushi.visualisation.SushiEventView;

/**
 * This class tests the saving, finding and removing of {@link SushiEventView}.
 */
public class EventViewPersistenceTest implements PersistenceTest {

	private SushiEventType eventType;
	private SushiEventView view;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}

	private void storeExampleView() {
		eventType = new SushiEventType("Tsun");
		eventType.save();
		ArrayList<SushiEventType> types = new ArrayList<SushiEventType>();
		types.add(eventType);
		view = new SushiEventView(null, types, null);;
		view.save();
	}
	
	@Override
	@Test
	public void testStoreAndRetrieve() {
		storeExampleView();
		assertTrue("Value should be 1, but was " + SushiEventView.findAll().size(),SushiEventView.findAll().size()==1);
		SushiEventView.removeAll();
		assertTrue("Value should be 0, but was " + SushiEventView.findAll().size(),SushiEventView.findAll().size()==0);
	}

	@Override
	@Test
	public void testFind() {
		storeExampleView();
		assertTrue(SushiEventView.findByID(view.getID()) == view);
	}

	@Test
	public void testFindByEventType() {
		storeExampleView();
		assertTrue("should have been 1, but was " + SushiEventView.findByEventType(eventType).size(), (SushiEventView.findByEventType(eventType)).size() == 1);
		assertTrue(SushiEventView.findByEventType(eventType).toString(), (SushiEventView.findByEventType(eventType)).get(0).getID() == view.getID());		
	}
	
	@Override
	@Test
	public void testRemove() {
		storeExampleView();
		view.remove();
		assertTrue(SushiEventView.findByID(view.getID()) != view);	
	}
	
	@Test
	public void testDeleteEventType() {
		storeExampleView();
		eventType.remove();
		assertTrue( SushiEventType.findByID(eventType.getID()) == null ||  ! SushiEventType.findByID(eventType.getID()).getTypeName().equals(eventType.getTypeName()));
	}

}
