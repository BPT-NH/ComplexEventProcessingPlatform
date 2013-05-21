package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTree;
import sushi.persistence.Persistor;

public class TreePersistenceTest implements PersistenceTest {
	
	private SushiTree<String> testTree;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve(){
		storeExampleTree();
		assertTrue("Value should be 1, but was " + SushiTree.findAll().size(), SushiTree.findAll().size()==1);
		SushiTree<String> loadedTree = SushiTree.findAll().get(0);
		assertTrue(loadedTree.getElements().size() == 4);
		SushiTree.removeAll();
		assertTrue("Value should be 0, b" +
				"ut was " + SushiTree.findAll().size(), SushiTree.findAll().size()==0);
	}
	
	@Test
	@Override
	public void testFind() {
		// TODO Auto-generated method stub
		
	}

	@Test
	@Override
	public void testRemove() {
		// TODO Auto-generated method stub
		
	}
	
	private void storeExampleTree() {
		String rootElement1 = new String("Root Element 1");
		String rootElement1Child1 = new String("Root Element 1 Child 1");
		String rootElement1Child1Child1 = new String("Root Element 1 Child 1 Child 1");
		String rootElement2 = new String("Root Element 2");
		
		testTree = new SushiTree<String>(rootElement1);
		
		testTree.addRootElement(rootElement2);
		testTree.addChild(rootElement1, rootElement1Child1);
		testTree.addChild(rootElement1Child1, rootElement1Child1Child1);
		testTree.save();
	}

	

}
