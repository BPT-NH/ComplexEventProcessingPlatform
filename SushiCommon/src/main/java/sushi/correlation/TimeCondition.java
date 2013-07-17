package sushi.correlation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.persistence.Persistable;
import sushi.process.SushiProcess;

/**
 * 
 * Container object for advanced correlation over time. Used for event correlation.
 * Related to a process.
 * 
 * @author Micha
 * @author Tsun
 */
@Entity
@Table(name = "TimeCondition")
public class TimeCondition extends Persistable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@ManyToOne
	private SushiEventType selectedEventType;
	
	@Column(name = "ConditionString")
	private String conditionString;
	
	@Column(name = "TimePeriod")
	private int timePeriod;
	
	@Column(name = "IsTimePeriodAfter")
	private boolean isTimePeriodAfterEvent;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "timeCondition")
	private SushiProcess process;
	
	@Transient
	private Set<SushiEvent> timerEvents;
	
	@ElementCollection
    @CollectionTable(name="TimeCondition_EventAttributes", joinColumns=@JoinColumn(name="Id"))
	@MapKeyColumn(name="EventAttributeName", length = 100)
    @Column(name="EventAttributeValue", length = 100)
	private Map<String, Serializable> attributeExpressionsAndValues = new HashMap<String, Serializable>();
	
	public TimeCondition() {
		this.ID = 0;
		this.selectedEventType = null;
		this.conditionString = null;
		this.isTimePeriodAfterEvent = true;
		this.timePeriod = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param eventType
	 * @param timePeriod in minutes
	 * @param isTimePeriodAfterEvent
	 * @param conditionString Pair(s) of attributes and values that narrow down the choice of events to which events from other types can be related to. These events are called timer events.
	 */
	public TimeCondition(SushiEventType eventType, int timePeriod, boolean isTimePeriodAfterEvent, String conditionString) {
		this();
		this.selectedEventType = eventType;
		this.timePeriod = timePeriod;
		this.isTimePeriodAfterEvent = isTimePeriodAfterEvent;
		this.conditionString = conditionString;
	}
	
	public SushiEventType getSelectedEventType() {
		return selectedEventType;
	}
	
	public void setSelectedEventType(SushiEventType selectedEventType) {
		this.selectedEventType = selectedEventType;
	}
	
	public String getConditionString() {
		return conditionString;
	}
	
	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}
	
	public int getTimePeriod() {
		return timePeriod;
	}
	
	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public boolean isTimePeriodAfterEvent() {
		return isTimePeriodAfterEvent;
	}
	
	public void setTimePeriodAfterEvent(boolean isTimePeriodAfterEvent) {
		this.isTimePeriodAfterEvent = isTimePeriodAfterEvent;
	}
	
	public Set<SushiEvent> getTimerEvents() {
		attributeExpressionsAndValues = ConditionParser.extractEventAttributes(conditionString);
		timerEvents = new HashSet<SushiEvent>(SushiEvent.findByEventTypeAndAttributeExpressionsAndValues(selectedEventType, attributeExpressionsAndValues));
		return timerEvents;
	}
	
	/**
	 * Fetches the timer event to be correlated to from the choice of timer events.
	 * Timer event and given event must have the same values for the given correlation attributes.
	 * Timestamp of given event must be in the time period related to the timer event.
	 *
	 * @param event the event for which the timer event is searched
	 * @param correlationAttributes list of single event type attributes
	 * @return the timer event closest to the given event
	 */
	public SushiEvent getTimerEventForEvent(SushiEvent event, List<SushiAttribute> correlationAttributes) {
		long timerEventTime;
		long eventTime = event.getTimestamp().getTime();
		long timePeriodInMillis = timePeriod * 60 * 1000;
		Map<SushiEvent, Long> possibleTimerEventsAndTimeDifferences = new HashMap<SushiEvent, Long>();
		for (SushiEvent timerEvent : getTimerEvents()) {
			boolean processInstanceAndEventMatch = true;
			for (SushiAttribute actualCorrelationAttribute : correlationAttributes) {
				if (!timerEvent.getValues().get(actualCorrelationAttribute.getAttributeExpression()).equals(event.getValues().get(actualCorrelationAttribute.getAttributeExpression()))) {
					processInstanceAndEventMatch = false;
					break;
				}
			}
			if (processInstanceAndEventMatch) {
				timerEventTime = timerEvent.getTimestamp().getTime();
				if (isTimePeriodAfterEvent) {
					if (timerEventTime <= eventTime && eventTime <= timerEventTime + timePeriodInMillis) {
						possibleTimerEventsAndTimeDifferences.put(timerEvent, eventTime - timerEventTime);
					}
				}
				else {
					if (timerEventTime - timePeriodInMillis <= eventTime && eventTime <= timerEventTime) {
						possibleTimerEventsAndTimeDifferences.put(timerEvent, timerEventTime - eventTime);
					}
				}
			}
		}
		if (possibleTimerEventsAndTimeDifferences.isEmpty()) {
			return null;
		} else {
			SushiEvent closestTimerEvent = null;
			for (SushiEvent timerEvent : possibleTimerEventsAndTimeDifferences.keySet()) {
				if (closestTimerEvent == null || possibleTimerEventsAndTimeDifferences.get(timerEvent) < possibleTimerEventsAndTimeDifferences.get(closestTimerEvent)) {
					closestTimerEvent = timerEvent;
				}
			}
			return closestTimerEvent;
		}
	}
	
	/**
	 * Fetches the timer event to be correlated to from the choice of timer events.
	 * Timer event and given event must have the same values for the attributes of the given correlation rules.
	 * Timestamp of given event must be in the time period related to the timer event.
	 *
	 * @param event the event for which the timer event is searched
	 * @param correlationRules set of correlation rules
	 * @return the timer event closest to the given event
	 */
	public SushiEvent getTimerEventForEvent(SushiEvent actualEvent, Set<CorrelationRule> correlationRules) {
		long timerEventTime;
		long eventTime = actualEvent.getTimestamp().getTime();
		long timePeriodInMillis = timePeriod * 60 * 1000;
		Map<SushiEvent, Long> possibleTimerEventsAndTimeDifferences = new HashMap<SushiEvent, Long>();
		for (SushiEvent timerEvent : getTimerEvents()) {
			boolean processInstanceAndEventMatch = true;
			for (CorrelationRule actualCorrelationRule : correlationRules) {
				if (actualCorrelationRule.getFirstAttribute().getEventType().equals(timerEvent.getEventType())
					&& actualCorrelationRule.getSecondAttribute().getEventType().equals(actualEvent.getEventType())) {
					String attributeExpressionForTimerEvent = actualCorrelationRule.getFirstAttribute().getAttributeExpression();
					String attributeExpressionForActualEvent = actualCorrelationRule.getSecondAttribute().getAttributeExpression();
					if(!timerEvent.getValues().get(attributeExpressionForTimerEvent).equals(actualEvent.getValues().get(attributeExpressionForActualEvent))) {
						processInstanceAndEventMatch = false;
						break;
					}
				}
				if (actualCorrelationRule.getSecondAttribute().getEventType().equals(timerEvent.getEventType())
					&& actualCorrelationRule.getFirstAttribute().getEventType().equals(actualEvent.getEventType())) {
					String attributeExpressionForTimerEvent = actualCorrelationRule.getSecondAttribute().getAttributeExpression();
					String attributeExpressionForActualEvent = actualCorrelationRule.getFirstAttribute().getAttributeExpression();
					if(!timerEvent.getValues().get(attributeExpressionForTimerEvent).equals(actualEvent.getValues().get(attributeExpressionForActualEvent))) {
						processInstanceAndEventMatch = false;
						break;
					}
				}
			}
			/**
			 * If there are attributes in the timer event that are used in the correlation rules but not for the determination of timer events, 
			 * a check is required whether its values are equal to the values from the given event.
			 */
			if (timerEvent.getEventType().equals(actualEvent.getEventType())) {
				Set<SushiAttribute> relatedAttributes = new HashSet<SushiAttribute>();
				for (CorrelationRule actualCorrelationRule : correlationRules) {
					if (actualCorrelationRule.getEventTypeOfFirstAttribute().equals(timerEvent.getEventType())) {
						relatedAttributes.add(actualCorrelationRule.getFirstAttribute());
					}
					if (actualCorrelationRule.getEventTypeOfSecondAttribute().equals(timerEvent.getEventType())) {
						relatedAttributes.add(actualCorrelationRule.getSecondAttribute());
					}
				}
				for (SushiAttribute attribute : relatedAttributes) {
					String attributeExpression = attribute.getAttributeExpression();
					if(!timerEvent.getValues().get(attributeExpression).equals(actualEvent.getValues().get(attributeExpression))) {
						processInstanceAndEventMatch = false;
					}
				}
			}
			
			if (processInstanceAndEventMatch) {
				timerEventTime = timerEvent.getTimestamp().getTime();
				if (isTimePeriodAfterEvent) {
					if (timerEventTime <= eventTime && eventTime <= timerEventTime + timePeriodInMillis) {
						possibleTimerEventsAndTimeDifferences.put(timerEvent, eventTime - timerEventTime);
					}
				}
				else {
					if (timerEventTime - timePeriodInMillis <= eventTime && eventTime <= timerEventTime) {
						possibleTimerEventsAndTimeDifferences.put(timerEvent, timerEventTime - eventTime);
					}
				}
			}
		}
		if (possibleTimerEventsAndTimeDifferences.isEmpty()) {
			return null;
		} else {
			SushiEvent closestTimerEvent = null;
			for (SushiEvent timerEvent : possibleTimerEventsAndTimeDifferences.keySet()) {
				if (closestTimerEvent == null || possibleTimerEventsAndTimeDifferences.get(timerEvent) < possibleTimerEventsAndTimeDifferences.get(closestTimerEvent)) {
					closestTimerEvent = timerEvent;
				}
			}
			return closestTimerEvent;
		}
	}

	public boolean belongsEventToTimerEvent(SushiEvent event, SushiEvent timerEvent) {
		long timerEventTime = timerEvent.getTimestamp().getTime();
		long eventTime = event.getTimestamp().getTime();
		long timePeriodInMillis = timePeriod * 60 * 1000;
		if (isTimePeriodAfterEvent) {
			return (timerEventTime <= eventTime && eventTime <= timerEventTime + timePeriodInMillis) ? true : false;
		} else {
			return (timerEventTime - timePeriodInMillis <= eventTime && eventTime <= timerEventTime) ? true : false;
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public TimeCondition remove() {
		if(process != null){
			process.setTimeCondition(null);
			process.save();
			process = null;
		}
		return (TimeCondition) super.remove();
	}
	
	public void removeTimerEvent(SushiEvent timerEvent){
		this.timerEvents.remove(timerEvent);
	}

	public SushiProcess getProcess() {
		return process;
	}

	public void setProcess(SushiProcess process) {
		this.process = process;
	}

}
