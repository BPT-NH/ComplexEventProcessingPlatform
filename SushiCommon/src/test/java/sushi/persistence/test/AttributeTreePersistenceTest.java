package sushi.persistence.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistor;

/**
 * This class tests the saving, finding and removing of {@link SushiAttributeTree}.
 */
public class AttributeTreePersistenceTest implements PersistenceTest {
	
	private SushiAttributeTree testTree;

	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	@Override
	public void testStoreAndRetrieve(){
		storeExampleTree();
		assertTrue("Value should be 1, but was " + SushiAttributeTree.findAll().size(), SushiAttributeTree.findAll().size()==1);
		SushiAttributeTree loadedTree = SushiAttributeTree.findAll().get(0);
		assertTrue(loadedTree.getAttributes().size() == 4);
		SushiAttributeTree.removeAll();
		assertTrue("Value should be 0, but was " + SushiAttributeTree.findAll().size(), SushiAttributeTree.findAll().size()==0);
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
		SushiAttribute rootElement1 = new SushiAttribute("Root Element 1");
		SushiAttribute rootElement1Child1 = new SushiAttribute(rootElement1, "Root Element 1 Child 1", SushiAttributeTypeEnum.INTEGER);
		new SushiAttribute(rootElement1Child1, "Root Element 1 Child 1 Child 1", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootElement2 = new SushiAttribute("Root Element 2", SushiAttributeTypeEnum.STRING);
		
		testTree = new SushiAttributeTree();
		testTree.addRoot(rootElement1);
		testTree.addRoot(rootElement2);
		testTree.save();
		System.out.println(testTree.toString());
	}

	

}