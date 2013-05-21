package sushi.event.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sushi.event.collection.SushiTree;

public class SushiTreeTest {
	
	private String rootElement1;
	private String rootElement1Child1;
	private String rootElement1Child1Child1;
	private String rootElement2;

	@Before
	public void setup(){
		rootElement1 = new String("Root Element 1");
		rootElement1Child1 = new String("Root Element 1 Child 1");
		rootElement1Child1Child1 = new String("Root Element 1 Child 1 Child 1");
		rootElement2 = new String("Root Element 2");
	}
	
	@Test
	public void testTreeAdding(){
		SushiTree<String> testTree = new SushiTree<String>(rootElement1);
		assertFalse(testTree.isEmpty());
		assertTrue(testTree.getRootElements().size() == 1);
		assertTrue(testTree.getRootElements().get(0) == rootElement1);
		
		testTree.addRootElement(rootElement2);
		assertTrue(testTree.getRootElements().size() == 2);
		assertTrue(testTree.getRootElements().get(1) == rootElement2);
		
		testTree.addChild(rootElement1, rootElement1Child1);
		assertTrue(testTree.getChildren(rootElement1).size() == 1);
		assertTrue(testTree.getChildren(rootElement1).get(0) == rootElement1Child1);
		
		testTree.addChild(rootElement1Child1, rootElement1Child1Child1);
		assertTrue(testTree.getChildren(rootElement1Child1).size() == 1);
		assertTrue(testTree.getChildren(rootElement1Child1).get(0) == rootElement1Child1Child1);
	}
	
	@Test
	public void testTreeRemoving(){
		SushiTree<String> testTree = buildTestTree();
		assertTrue(testTree.size() == 4);
		
		testTree.remove(rootElement1Child1Child1);
		assertTrue(testTree.size() == 3);
		assertNull(testTree.findElement(rootElement1Child1Child1));
		
		testTree.remove(rootElement1Child1);
		assertTrue(testTree.size() == 2);
		assertNull(testTree.findElement(rootElement1Child1));
	}
	
	@Test
	public void testTreeChildRemoving(){
		SushiTree<String> testTree = buildTestTree();
		assertTrue(testTree.size() == 4);
		
		testTree.remove(rootElement1);
		assertTrue(testTree.size() == 1);
		assertTrue(testTree.findElement(rootElement2) == rootElement2);
	}
	
	private SushiTree<String> buildTestTree(){
		SushiTree<String> testTree = new SushiTree<String>(rootElement1);

		testTree.addRootElement(rootElement2);
		testTree.addChild(rootElement1, rootElement1Child1);
		testTree.addChild(rootElement1Child1, rootElement1Child1Child1);
		
		return testTree;
	}

}
