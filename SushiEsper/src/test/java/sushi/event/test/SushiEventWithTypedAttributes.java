package sushi.event.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.event.collection.SushiTree;
import sushi.eventhandling.Broker;
import sushi.persistence.Persistor;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

public class SushiEventWithTypedAttributes {

	private String rootElement1Key;
	private String rootElement1Child1Key;
	private String rootElement1Child2Key;
	private String rootElement2Key;
	
	private String rootElement1Child1Value;
	private int rootElement1Child2Value;
	private Date rootElement2Value;
	
	private SushiEventType type;
	private SushiEvent event;

	@Before
	public void setup() {
		Persistor.useTestEnviroment();
		rootElement1Key = "RootElement";
		rootElement1Child1Key = "RootElementStringChild";
		rootElement1Child2Key = "RootElementIntegerChild";
		rootElement2Key = "RootDateElement";
		rootElement1Child1Value = new String("Root Element 1");
		rootElement1Child2Value = 2;
		rootElement2Value = new Date(2011-1900,5-1,17);
	}
	
	@Test
	public void testTreeAdding() {
		SushiMapTree<String, Serializable> testMapTree = new SushiMapTree<String, Serializable>(rootElement1Key, null);
		assertFalse(testMapTree.isEmpty());
		assertTrue(testMapTree.getRootElementValues().size() == 1);
		assertTrue(testMapTree.getRootElementValues().get(0) == null);
		
		testMapTree.addRootElement(rootElement2Key, rootElement2Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child1Key, rootElement1Child1Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child2Key, rootElement1Child2Value);
	}
	
	@Test
	public void testCreateTypedEventType() {
		type = new SushiEventType("testEventTypeTyped", createTree(), "testTimestamp");
		Broker.send(type);
		
		SushiQuery liveTyped = new SushiQuery("testTypes", "SELECT * FROM testEventTypeTyped ", SushiQueryTypeEnum.LIVE);
		liveTyped.addToEsper(SushiEsper.getInstance());
		
		event = new SushiEvent(type, new Date(), createTreeMap());
		Broker.send(event);

		System.out.println(Arrays.asList((SushiEsper.getInstance().getAttributesOfEventType(type))));
		assertTrue(Arrays.asList(SushiEsper.getInstance().getAttributesOfEventType(type)).contains(rootElement1Key + "." + rootElement1Child2Key));
		
		assertTrue(SushiEsper.getInstance().eventTypeHasAttribute(type, rootElement1Key + "." + rootElement1Child2Key));
		
		assertTrue("type of eventtype " + rootElement2Key + " was " + SushiEsper.getInstance().getEventTypeInfo(type, rootElement2Key),
				SushiEsper.getInstance().getEventTypeInfo(type, rootElement2Key) == Date.class);
		assertTrue(SushiEsper.getInstance().getEventTypeInfo(type, "Timestamp") == Date.class);
		assertTrue("type of eventtype " + rootElement1Key + "." + rootElement1Child2Key + " was " + SushiEsper.getInstance().getEventTypeInfo(type, rootElement1Key + "." + rootElement1Child2Key),
				SushiEsper.getInstance().getEventTypeInfo(type, rootElement1Key + "." + rootElement1Child2Key) == Integer.class);
		
		SushiQuery testTyped = new SushiQuery("testTypes", "SELECT RootDateElement.getTime(), " 
		+ rootElement1Key + "." + rootElement1Child1Key + " , " + rootElement1Key + "." + rootElement1Child2Key
		+ " FROM testEventTypeTypedWindow "
				+ "WHERE "+ rootElement1Key + "." + rootElement1Child1Key +" = '"+ rootElement1Child1Value + "' " 
				+ " AND " + rootElement1Key + "." + rootElement1Child2Key + " > 0"
				, SushiQueryTypeEnum.ONDEMAND);
		String result = testTyped.execute(SushiEsper.getInstance());
		System.out.println(result);
		assertTrue(result.contains("Number of events found: 1"));
		
	}

	private SushiMapTree<String, Serializable> createTreeMap() {
		SushiMapTree<String, Serializable> testMapTree = new SushiMapTree<String, Serializable>(rootElement1Key, null);
		testMapTree.addRootElement(rootElement2Key, rootElement2Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child1Key, rootElement1Child1Value);
		testMapTree.addChild(rootElement1Key, rootElement1Child2Key, rootElement1Child2Value);

		return testMapTree;
	}

	private SushiAttributeTree createTree() {
		SushiAttribute rootElement1 = new SushiAttribute(rootElement1Key);
		new SushiAttribute(rootElement1, rootElement1Child1Key, SushiAttributeTypeEnum.STRING);
		new SushiAttribute(rootElement1, rootElement1Child2Key, SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootElement2 = new SushiAttribute(rootElement2Key, SushiAttributeTypeEnum.DATE);
		
		SushiAttributeTree tree = new SushiAttributeTree(rootElement1);
		tree.addRoot(rootElement2);
		return tree;
	}	
	
}
