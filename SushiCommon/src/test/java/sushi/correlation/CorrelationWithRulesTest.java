package sushi.correlation;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;
import sushi.util.SushiTestHelper;

public class CorrelationWithRulesTest {
	
	@Before
	public void setup(){
		Persistor.useTestEnviroment();
	}
	
	@Test
	public void testCorrelationWithOneRule() {
		assertNumberOfDataSets(0, 0, 0, 0);
		
		List<SushiEventType> correlationEventTypes = new ArrayList<SushiEventType>();
		
		SushiAttribute kinoRatingAttribute1 = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute kinoRatingAttribute2 = new SushiAttribute("Rating", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> kinoRatingAttributes = Arrays.asList(kinoRatingAttribute1, kinoRatingAttribute2);
		SushiEventType kinoRatingEventType = new SushiEventType("KinoRating", kinoRatingAttributes);
		correlationEventTypes.add(kinoRatingEventType.save());
		
		SushiAttribute kinoFilmeAttribute1 = new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute kinoFilmeAttribute2 = new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING);
		SushiAttribute kinoFilmeAttribute3 = new SushiAttribute("Action", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> kinoFilmeAttributes = Arrays.asList(kinoFilmeAttribute1, kinoFilmeAttribute2, kinoFilmeAttribute3);
		SushiEventType kinoFilmeEventType = new SushiEventType("KinoFilme", kinoFilmeAttributes);
		correlationEventTypes.add(kinoFilmeEventType.save());
		
		assertNumberOfDataSets(0, 2, 0, 0);
		
		for (SushiEventType eventType : correlationEventTypes) {
			List<SushiEvent> events = SushiTestHelper.createEvents(eventType);
			for (SushiEvent event : events) {
				event.save();
			}	
		}
		
		assertNumberOfDataSets(6, 2, 0, 0);
		
		SushiProcess process = new SushiProcess("Kino");
		process.save();
		assertNumberOfDataSets(6, 2, 1, 0);
		
		RuleCorrelator.correlate(new HashSet<CorrelationRule>(Arrays.asList(new CorrelationRule(kinoRatingAttribute1, kinoFilmeAttribute1))), process, null);
		
		//Prüfen, gleiche Anzahl Events, EventTypen, Prozesse und richtige Anzahl Prozessinstanzen
		assertNumberOfDataSets(6, 2, 1, 3);
	}
	
	@Test
	public void testCorrelationWithMultipleRules() {
		assertNumberOfDataSets(0, 0, 0, 0);
		
		List<SushiEventType> correlationEventTypes = new ArrayList<SushiEventType>();
		
		SushiAttribute firstEventAttribute1 = new SushiAttribute("FirstEventAttributeOne", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute firstEventAttribute2 = new SushiAttribute("FirstEventAttributeTwo", SushiAttributeTypeEnum.STRING);
		List<SushiAttribute> firstEventAttributes = Arrays.asList(firstEventAttribute1, firstEventAttribute2);
		SushiEventType firstEventType = (new SushiEventType("FirstEvent", firstEventAttributes)).save();
		correlationEventTypes.add(firstEventType);
		
		SushiAttribute secondEventAttribute1 = new SushiAttribute("SecondEventAttributeOne", SushiAttributeTypeEnum.STRING);
		SushiAttribute secondEventAttribute2 = new SushiAttribute("SecondEventAttributeTwo", SushiAttributeTypeEnum.DATE);
		SushiAttribute secondEventAttribute3 = new SushiAttribute("SecondEventAttributeThree");
		new SushiAttribute(secondEventAttribute3, "SecondEventAttributeThreeOne", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute secondEventAttribute4 = new SushiAttribute("SecondEventAttributeFour", SushiAttributeTypeEnum.INTEGER);
		SushiAttributeTree secondEventAttributeTree = new SushiAttributeTree(Arrays.asList(secondEventAttribute1, secondEventAttribute2, secondEventAttribute3, secondEventAttribute4));
		SushiEventType secondEventType = (new SushiEventType("SecondEvent", secondEventAttributeTree)).save();
		correlationEventTypes.add(secondEventType);
		
		SushiAttribute thirdEventAttribute1 = new SushiAttribute("ThirdEventAttributeOne", SushiAttributeTypeEnum.INTEGER);
		List<SushiAttribute> thirdEventAttributes = Arrays.asList(thirdEventAttribute1);
		SushiEventType thirdEventType = (new SushiEventType("ThirdEvent", thirdEventAttributes)).save();
		correlationEventTypes.add(thirdEventType);
		
		assertNumberOfDataSets(0, 3, 0, 0);
		
		Set<CorrelationRule> correlationRules = new HashSet<CorrelationRule>();
		correlationRules.add(new CorrelationRule(firstEventType.getValueTypeTree().getAttributeByExpression("FirstEventAttributeTwo"), secondEventType.getValueTypeTree().getAttributeByExpression("SecondEventAttributeOne")));
		correlationRules.add(new CorrelationRule(secondEventType.getValueTypeTree().getAttributeByExpression("SecondEventAttributeThree.SecondEventAttributeThreeOne"), thirdEventType.getValueTypeTree().getAttributeByExpression("ThirdEventAttributeOne")));
		
		SushiProcess process = new SushiProcess("SomeProcess");
		process.save();
		
		assertNumberOfDataSets(0, 3, 1, 0);
		
		RuleCorrelator.correlate(correlationRules, process, null);
		
		assertNumberOfDataSets(0, 3, 1, 0);

		for (SushiEventType eventType : correlationEventTypes) {
			List<SushiEvent> eventsToCorrelate = createEventsForCorrelation(eventType);
			Correlator.correlate(SushiEvent.save(eventsToCorrelate));
		}
		
		assertNumberOfDataSets(9, 3, 1, 3);
	}

	@Test(expected = RuntimeException.class)
	public void testCorrelationRuleWithAttributesOfDifferentTypes() {
		SushiAttribute attribute1 = new SushiAttribute("One", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute attribute2 = new SushiAttribute("Two", SushiAttributeTypeEnum.STRING);
		new CorrelationRule(attribute1, attribute2);
	}
	
	private void assertNumberOfDataSets(int events, int eventTypes, int processes, int processInstances) {
		assertTrue("Number of events must be " + events + " but was " + SushiEvent.findAll().size(), SushiEvent.findAll().size() == events);
		assertTrue("Number of event types must be " + eventTypes + " but was " + SushiEventType.findAll().size(), SushiEventType.findAll().size() == eventTypes);
		assertTrue("Number of processes must be " + processes + " but was " + SushiProcess.findAll().size(), SushiProcess.findAll().size() == processes);
		assertTrue("Number of process instances must be " + processInstances + " but was " + SushiProcessInstance.findAll().size(), SushiProcessInstance.findAll().size() == processInstances);
	}
	
	private List<SushiEvent> createEventsForCorrelation(SushiEventType eventType) {
		List<SushiEvent> events = new ArrayList<SushiEvent>();
		for (int i = 1; i < 4; i++) {
			SushiMapTree<String, Serializable> mapTree = new SushiMapTree<String, Serializable>();
			SushiEvent event = new SushiEvent(eventType, new Date(), mapTree);
			for (SushiAttribute valueType : eventType.getValueTypes()) {
				String attributeName = valueType.getAttributeExpression();
				if (valueType.getType() == SushiAttributeTypeEnum.STRING) {
					// values: A, B, C ...
					mapTree.put(attributeName, String.valueOf((char)(64 + i)));
				} else if (valueType.getType() == SushiAttributeTypeEnum.INTEGER) {
					mapTree.put(attributeName, i);
				} else if (valueType.getType() == SushiAttributeTypeEnum.DATE) {
					mapTree.put(attributeName, new Date());
				}
			}
			events.add(event);
		}
		return events;
	}
}
