package sushi.aggregation.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationListener;
import sushi.aggregation.SushiAggregationRule;
import sushi.aggregation.collection.SushiPatternTree;
import sushi.aggregation.element.EventTypeElement;
import sushi.aggregation.element.FilterExpressionElement;
import sushi.aggregation.element.FilterExpressionOperatorEnum;
import sushi.aggregation.element.PatternOperatorElement;
import sushi.aggregation.element.PatternOperatorEnum;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpressionSet;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;
import sushi.persistence.Persistor;

public class AdvancedSushiAggregationRuleTest {
	
	private ExcelNormalizer excelNormalizer;
	private String folder = System.getProperty("user.dir") + "/src/main/resources/truckUsageWithExternalKnowledgeTest/";
	private String firstObuEventsFile = folder + "OBU_Events_01.xls";
	private String secondObuEventsFile = folder + "OBU_Events_02.xls";
	private String firstTruckUsageFile = folder + "Truck_Usage_Plan_Advanced_01.xls";
	private String secondTruckUsageFile = folder + "Truck_Usage_Plan_Advanced_02.xls";
	
	@Before
	public void setup() {
		Persistor.useTestEnviroment();
		SushiEsper.clearInstance();
		excelNormalizer = new ExcelNormalizer();
	}
	
	@Test
	public void testTruckUsageFiles() {
		
		List<SushiAttribute> attributes;
		
		SushiAttribute rootAttribute5 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute6 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute7 = new SushiAttribute("Action", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute5, rootAttribute6, rootAttribute7);
		SushiEventType eventType1 = new SushiEventType("truckUsage", attributes, "Timestamp");
		assertTrue("Event type already registered", !SushiEsper.getInstance().isEventType(eventType1));
		Broker.send(eventType1);
		
		SushiAttribute rootAttribute8 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute9 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute8, rootAttribute9);
		SushiEventType eventType2 = new SushiEventType("obuEvent", attributes, "Timestamp");
		Broker.send(eventType2);
		
		SushiAttribute rootAttribute10 = new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute11 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute12 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute13 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		attributes = Arrays.asList(rootAttribute10, rootAttribute11, rootAttribute12, rootAttribute13);
		SushiEventType eventType3 = new SushiEventType("enrichedObuEvent", attributes, "Timestamp");
		Broker.send(eventType3);

		assertTrue("Number of event types should be 3 but was " + SushiEsper.getInstance().getWindowNames().length, SushiEsper.getInstance().getWindowNames().length == 3);
		
		SushiPatternTree patternTree = new SushiPatternTree();
		
		EventTypeElement eventTypeElementA = new EventTypeElement(getNextID(patternTree), eventType1);
		eventTypeElementA.setAlias("A");
		patternTree.addElement(eventTypeElementA);
		
		FilterExpressionElement filterExpressionElementA1 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.EQUALS);
		filterExpressionElementA1.setParent(eventTypeElementA);
		filterExpressionElementA1.setLeftHandSideExpression("A.Action");
		filterExpressionElementA1.setRightHandSideExpression("'Start'");
		patternTree.addElement(filterExpressionElementA1);
		
		EventTypeElement eventTypeElementB = new EventTypeElement(getNextID(patternTree), eventType2);
		eventTypeElementB.setAlias("B");
		patternTree.addElement(eventTypeElementB);
		
		FilterExpressionElement filterExpressionElementB1 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.EQUALS);
		filterExpressionElementB1.setParent(eventTypeElementB);
		filterExpressionElementB1.setLeftHandSideExpression("A.Driver");
		filterExpressionElementB1.setRightHandSideExpression("B.Driver");
		patternTree.addElement(filterExpressionElementB1);
		
		FilterExpressionElement filterExpressionElementB2 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.SMALLER_THAN);
		filterExpressionElementB2.setParent(eventTypeElementB);
		filterExpressionElementB2.setLeftHandSideExpression("A.Timestamp.getTime()");
		filterExpressionElementB2.setRightHandSideExpression("B.Timestamp.getTime()");
		patternTree.addElement(filterExpressionElementB2);
		
		EventTypeElement eventTypeElementC = new EventTypeElement(getNextID(patternTree), eventType1);
		eventTypeElementC.setAlias("C");
		patternTree.addElement(eventTypeElementC);
		
		FilterExpressionElement filterExpressionElementC1 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.EQUALS);
		filterExpressionElementC1.setParent(eventTypeElementC);
		filterExpressionElementC1.setLeftHandSideExpression("C.Action");
		filterExpressionElementC1.setRightHandSideExpression("'End'");
		patternTree.addElement(filterExpressionElementC1);
		
		FilterExpressionElement filterExpressionElementC2 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.SMALLER_THAN);
		filterExpressionElementC2.setParent(eventTypeElementC);
		filterExpressionElementC2.setLeftHandSideExpression("A.Timestamp.getTime()");
		filterExpressionElementC2.setRightHandSideExpression("C.Timestamp.getTime()");
		patternTree.addElement(filterExpressionElementC2);
		
		FilterExpressionElement filterExpressionElementC3 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.EQUALS);
		filterExpressionElementC3.setParent(eventTypeElementC);
		filterExpressionElementC3.setLeftHandSideExpression("A.Driver");
		filterExpressionElementC3.setRightHandSideExpression("C.Driver");
		patternTree.addElement(filterExpressionElementC3);
		
		FilterExpressionElement filterExpressionElementC4 = new FilterExpressionElement(getNextID(patternTree), FilterExpressionOperatorEnum.EQUALS);
		filterExpressionElementC4.setParent(eventTypeElementC);
		filterExpressionElementC4.setLeftHandSideExpression("A.Truck");
		filterExpressionElementC4.setRightHandSideExpression("C.Truck");
		patternTree.addElement(filterExpressionElementC4);
		
		PatternOperatorElement patternOperatorElement1 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.NOT);
		eventTypeElementC.setParent(patternOperatorElement1);
		patternTree.addElement(patternOperatorElement1);
		
		PatternOperatorElement patternOperatorElement2 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.AND);
		eventTypeElementB.setParent(patternOperatorElement2);
		patternOperatorElement1.setParent(patternOperatorElement2);
		patternTree.addElement(patternOperatorElement2);
		
		PatternOperatorElement patternOperatorElement3 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.EVERY);
		patternOperatorElement2.setParent(patternOperatorElement3);
		patternTree.addElement(patternOperatorElement3);
		
		PatternOperatorElement patternOperatorElement4 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.EVERY);
		eventTypeElementA.setParent(patternOperatorElement4);
		patternTree.addElement(patternOperatorElement4);
		
		PatternOperatorElement patternOperatorElement5 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.FOLLOWED_BY);
		patternOperatorElement4.setParent(patternOperatorElement5);
		patternOperatorElement3.setParent(patternOperatorElement5);
		patternTree.addElement(patternOperatorElement5);
		
		PatternOperatorElement patternOperatorElement6 = new PatternOperatorElement(getNextID(patternTree), PatternOperatorEnum.EVERY);
		patternOperatorElement5.setParent(patternOperatorElement6);
		patternTree.addElement(patternOperatorElement6);
		
		Map<String, String> attributeIdentifiersAndExpressions = new HashMap<String, String>();
		attributeIdentifiersAndExpressions.put("Timestamp", "B.Timestamp");
		attributeIdentifiersAndExpressions.put("Driver", "A.Driver");
		attributeIdentifiersAndExpressions.put("Truck", "A.Truck");
		attributeIdentifiersAndExpressions.put("Location", "B.Location");
		attributeIdentifiersAndExpressions.put("Truck_Usage_Start", "A.Timestamp");
		
		String query = SushiAggregationRule.generateAggregationRule(attributeIdentifiersAndExpressions, patternTree);
		assertTrue("Query was not build correctly.", query.equals("" + 
				"SELECT (A.Driver) AS Driver, (A.Timestamp) AS Truck_Usage_Start, " +
				"(A.Truck) AS Truck, (B.Location) AS Location, (B.Timestamp) AS Timestamp " +
				"FROM Pattern [(EVERY " +
					"((EVERY (A=truckUsage(((A.Action) = ('Start'))))) " +
					"-> " +
					"(EVERY ((B=obuEvent(((A.Driver) = (B.Driver)), ((A.Timestamp.getTime()) < (B.Timestamp.getTime())))) " +
						"AND (NOT (C=truckUsage(((C.Action) = ('End')), ((A.Timestamp.getTime()) < (C.Timestamp.getTime())), " +
							"((A.Driver) = (C.Driver)), ((A.Truck) = (C.Truck)))))))))]"));
		
		SushiAggregationRule firstRule = new SushiAggregationRule(eventType3, "someRuleName", query, patternTree, attributeIdentifiersAndExpressions, new HashMap<String, ExternalKnowledgeExpressionSet>());
		SushiAggregationListener firstListener = SushiAggregation.getInstance().addToEsper(firstRule.save());
		
		loadData(eventType1, firstTruckUsageFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 0, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 0);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 0, but was " + SushiEvent.findByEventType(eventType3).size(), SushiEvent.findByEventType(eventType3).size() == 0);
		
		loadData(eventType2, firstObuEventsFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 16, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 16);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 16, but was " + SushiEvent.findByEventType(eventType3).size(), SushiEvent.findByEventType(eventType3).size() == 16);
		
		loadData(eventType1, secondTruckUsageFile);
		assertTrue("Number of generated enrichedObuEvent events in the event processing engine should be 16, but was " + firstListener.getNumberOfEventsFired(), firstListener.getNumberOfEventsFired() == 16);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 16, but was " + SushiEvent.findByEventType(eventType3).size(), SushiEvent.findByEventType(eventType3).size() == 16);
		
		firstRule.remove();
		SushiAggregation.getInstance().removeFromEsper(firstRule);
		
		loadData(eventType2, secondObuEventsFile);
		assertTrue("Number of generated enrichedObuEvent events in the database should be 16, but was " + SushiEvent.findByEventType(eventType3).size(), SushiEvent.findByEventType(eventType3).size() == 16);
	}
	
	public void loadData(SushiEventType eventType, String filePath) {
		List<SushiAttribute> attributes = eventType.getValueTypes();
		String timestamp = eventType.getTimestampName();
		// insert events into event type via SushiEsper
		List<SushiEvent> events = excelNormalizer.importEventsFromFile(filePath, attributes, timestamp);
		for (SushiEvent event : events) {
			event.setEventType(eventType);
		}
		
		Broker.send(events);
	}
	
	public int getNextID(SushiPatternTree patternTree) {
		return patternTree.getElements().size() + 1;
	}
	
}