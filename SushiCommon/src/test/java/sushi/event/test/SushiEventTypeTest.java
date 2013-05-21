package sushi.event.test;

import static org.junit.Assert.*;

import org.junit.Test;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTree;

public class SushiEventTypeTest {


	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException1() {
		new SushiEventType("abcdéf");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException2() {
		new SushiEventType("abcdef!");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException3() {
		new SushiEventType("abcd?");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException4() {
		new SushiEventType("abcdef/");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException5() {
		new SushiEventType("abcß");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException6() {
		new SushiEventType("abcdef()");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException7() {
		new SushiEventType("faâ");
	}
	
	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException8() {
		new SushiEventType("");
	}

	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException11() {
		SushiAttribute rootElement1 = new SushiAttribute("Root Element 1");
		SushiAttribute rootElement1Child1 = new SushiAttribute(rootElement1, "Root Element 1 Child 1", SushiAttributeTypeEnum.INTEGER);
		new SushiAttribute(rootElement1Child1, "Root Element 1 Child 1 Child 1", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootElement2 = new SushiAttribute("Root Element 2", SushiAttributeTypeEnum.STRING);
		SushiAttributeTree testTree = new SushiAttributeTree();
		testTree.addRoot(rootElement1);
		testTree.addRoot(rootElement2);
		testTree.save();

		new SushiEventType("faâ", testTree);
	}
	
	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException12() {
		new SushiEventType("fa/f");
	}

	
	@Test(expected = RuntimeException.class)  
	public void testForbiddenCharactersInNameThrowException13() {
		new SushiEventType("");
	}
	
	@Test
	public void testAllowedCharacterInNameThrowNoException(){
		String[] names = {"ab c", "abc", "abc0", "abc_", "abc_s", "abc01_2", "abc-d", "abc-0-1", "___"};
		SushiEventType type;
		String testedName = null;
		try {
			for (String name : names){
				testedName = name;
				type = new SushiEventType(name);
			}
		} catch (RuntimeException e) {
			fail(testedName);
		}
	}
}
