package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sushi.event.collection.SushiMapElement;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistor;

public class MapTreePersistenceTest implements PersistenceTest {
	
	private SushiMapTree<String, String> testMapTree;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void testMapElementStoreAndRetrieve(){
		SushiMapElement<String, String> mapElement = new SushiMapElement<String, String>("Key", "Value");
		mapElement.save();
		assertTrue("Value should be 1, but was " + SushiMapElement.findAll().size(), SushiMapElement.findAll().size()==1);
		SushiMapElement.removeAll();
		assertTrue("Value should be 0, but was " + SushiMapElement.findAll().size(), SushiMapElement.findAll().size()==0);
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve(){
		storeExampleMapTree();
		assertTrue("Value should be 1, but was " + SushiMapTree.findAll().size(), SushiMapTree.findAll().size()==1);
		SushiMapTree<String, String> loadedMapTree = SushiMapTree.findAll().get(0);
		assertTrue(loadedMapTree.keySet().size() == 4);
		SushiMapTree.removeAll();
		assertTrue("Value should be 0, but was " + SushiMapTree.findAll().size(), SushiMapTree.findAll().size()==0);
	}
	
	@Test
	public void testSaveDifferentKindsOfSushiMaptree(){
		// it should be possible to save different types of sushimaptrees
		
		// SushiMapTree with String Values
		storeExampleMapTree();
		List<SushiMapTree> allSushiMapTrees = SushiMapTree.findAll();
		assertTrue("Expected "+ String.class + " but got " +allSushiMapTrees.get(0).get("RootElement1").getClass(),allSushiMapTrees.get(0).get("RootElement1").getClass() == String.class);
		
		// SushiMapTree with Integer Values
		String rootElement1Key = "RootElement1";
		String rootElement1Child1Key = "RootElement1Child1";
		String rootElement1Child1Child1Key = "RootElement1Child1Child1";
		String rootElement2Key = "RootElement2";
		int rootElement1Value = 1;
		int rootElement1Child1Value = 2;
		int rootElement1Child1Child1Value = 3;
		int rootElement2Value = 4;
		
		SushiMapTree<String, Integer> testMapTree2 = new SushiMapTree<String, Integer>(rootElement1Key, rootElement1Value);
		testMapTree2.addRootElement(rootElement2Key, rootElement2Value);
		testMapTree2.addChild(rootElement1Key, rootElement1Child1Key, rootElement1Child1Value);
		testMapTree2.addChild(rootElement1Child1Key, rootElement1Child1Child1Key, rootElement1Child1Child1Value);
		testMapTree2.save();
		allSushiMapTrees = SushiMapTree.findAll();
		assertTrue("Expected "+ Integer.class + " but got " +allSushiMapTrees.get(1).get("RootElement1").getClass(),allSushiMapTrees.get(1).get("RootElement1").getClass() == Integer.class);
		
		// SushiMapTree with Date Values
		String rootElement1Key2 = "RootElement1";
		String rootElement1Child1Key2 = "RootElement1Child1";
		String rootElement1Child1Child1Key2 = "RootElement1Child1Child1";
		String rootElement2Key2 = "RootElement2";
		Date rootElement1Value2 = new Date();
		Date rootElement1Child1Value2 = new Date();
		Date rootElement1Child1Child1Value2 = new Date();
		Date rootElement2Value2 = new Date();

		SushiMapTree<String, Date> testMapTree3 = new SushiMapTree<String, Date>(rootElement1Key2, rootElement1Value2);
		testMapTree3.save();
		allSushiMapTrees = SushiMapTree.findAll();		
		assertTrue("Expected "+ Date.class + " but got " +allSushiMapTrees.get(2).get("RootElement1").getClass(),allSushiMapTrees.get(2).get("RootElement1").getClass() == Date.class);
	}
	
	@Test
	public void testObjectValueType(){
		String rootElement1Key = "RootElement1";
		String rootElement1Child1Key = "RootElement1Child1";
		String rootElement1Child1Child1Key = "RootElement1Child1Child1";
		String rootElement2Key = "RootElement2";
		Date rootElement1Value = new Date();
		Date rootElement1Child1Value = new Date();
		int rootElement1Child1Child1Value = 0;
		String rootElement2Value = "0";

		SushiMapTree<String, Serializable> testMapTree = new SushiMapTree<String, Serializable>(rootElement1Key, rootElement1Value);
		testMapTree.addRootElement(rootElement2Key, rootElement2Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child1Key, rootElement1Child1Value);
		testMapTree.addChild(rootElement1Child1Key, rootElement1Child1Child1Key, rootElement1Child1Child1Value);
		testMapTree.save();
		List<SushiMapTree> allSushiMapTrees = SushiMapTree.findAll();		
		assertTrue("Expected "+ Date.class + " but got " +allSushiMapTrees.get(0).get("RootElement1").getClass(),allSushiMapTrees.get(0).get("RootElement1").getClass() == Date.class);
		assertTrue("Expected "+ Date.class + " but got " +allSushiMapTrees.get(0).get("RootElement1").getClass(),allSushiMapTrees.get(0).get("RootElement1").getClass() != String.class);
		assertTrue("Expected "+ Integer.class + " but got " +allSushiMapTrees.get(0).get("RootElement1Child1Child1").getClass(),allSushiMapTrees.get(0).get("RootElement1Child1Child1").getClass() == Integer.class);
		assertTrue("Expected "+ String.class + " but got " +allSushiMapTrees.get(0).get("RootElement2").getClass(),allSushiMapTrees.get(0).get("RootElement2").getClass() == String.class);

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
	
	private void storeExampleMapTree() {
		String rootElement1Key = "RootElement1";
		String rootElement1Child1Key = "RootElement1Child1";
		String rootElement1Child1Child1Key = "RootElement1Child1Child1";
		String rootElement2Key = "RootElement2";
		String rootElement1Value = new String("Root Element 1");
		String rootElement1Child1Value = new String("Root Element 1 Child 1");
		String rootElement1Child1Child1Value = new String("Root Element 1 Child 1 Child 1");
		String rootElement2Value = new String("Root Element 2");
		
		testMapTree = new SushiMapTree<String, String>(rootElement1Key, rootElement1Value);
		
		testMapTree.addRootElement(rootElement2Key, rootElement2Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child1Key, rootElement1Child1Value);
		testMapTree.addChild(rootElement1Child1Key, rootElement1Child1Child1Key, rootElement1Child1Child1Value);
		testMapTree.save();
	}

	

}
