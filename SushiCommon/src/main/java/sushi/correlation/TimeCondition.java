package sushi.correlation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;

/**
 * @author Micha
 * Die Klasse soll als Container und übergabeobjekt für die erweiterte zeitliche Korrelation dienen.
 * Eine TimeCondition-Instanz gehört zu einer Prozess-Instanz.
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
	
	@ManyToMany
	private List<SushiEvent> timerEvents;
	
	@ElementCollection
    @CollectionTable(name="TimeCondition_EventAttributes", joinColumns=@JoinColumn(name="Id"))
	@MapKeyColumn(name="EventAttributeName", length = 100)
    @Column(name="EventAttributeValue", length = 100)
	private Map<String, Serializable> eventAttributes = new HashMap<String, Serializable>();
	
	public TimeCondition(){
		this.ID = 0;
		this.selectedEventType = new SushiEventType("EventType");
		this.conditionString = null;
		this.timePeriod = 0;
		this.timerEvents = new ArrayList<SushiEvent>();
	}
	
	public TimeCondition(SushiEventType eventType, int timePeriod, boolean isTimePeriodAfterEvent, String conditionString){
		this.ID = 0;
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
	
	public List<SushiEvent> getTimerEvents() {
		if(timerEvents.isEmpty()){
			findTimerEvents();
		}
		return timerEvents;
	}
	
	private void findTimerEvents() {
		eventAttributes = ConditionParser.extractEventAttributes(conditionString);
		timerEvents = new ArrayList<SushiEvent>(SushiEvent.findByValues(eventAttributes));
	}
	
	// Funktioniert das richtig, wenn die TimerEvents zu nahe zusammmenliegen?
	public SushiEvent getTimerEventForEvent(SushiEvent event, List<SushiAttribute> correlationAttributes){
		long timerEventTime;
		long eventTime = event.getTimestamp().getTime();
		long timePeriodInMillis = timePeriod * 60 * 1000;
		for(SushiEvent timerEvent : getTimerEvents()){
			boolean processInstanceAndEventMatch = true;
			// Test, ob TimerEvent und Event die gleichen Werte bei den Korrelationsattributen haben
			for(SushiAttribute actualCorrelationAttribute : correlationAttributes){
				if(!timerEvent.getValues().get(actualCorrelationAttribute.getAttributeExpression()).equals(event.getValues().get(actualCorrelationAttribute.getAttributeExpression()))){
					processInstanceAndEventMatch = false;
					break;
				}
			}
			// Test, ob Event in der richtigen Zeitspanne liegt, nur falls Event und TimerEvent zusammenpassen
			if(processInstanceAndEventMatch){
				timerEventTime = timerEvent.getTimestamp().getTime();
				if(isTimePeriodAfterEvent){
					if(timerEventTime <= eventTime && eventTime <= timerEventTime + timePeriodInMillis){
						return timerEvent;
					}
				}
				else{
					if(timerEventTime - timePeriodInMillis <= eventTime && eventTime <= timerEventTime){
						return timerEvent;
					}
				}
			}
		}
		return null;
	}

	public boolean belongsEventToTimerEvent(SushiEvent event, SushiEvent timerEvent) {
		long timerEventTime = timerEvent.getTimestamp().getTime();
		long eventTime = event.getTimestamp().getTime();
		long timePeriodInMillis = timePeriod * 60 * 1000;
		if(isTimePeriodAfterEvent){
			if(timerEventTime <= eventTime && eventTime <= timerEventTime + timePeriodInMillis){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(timerEventTime - timePeriodInMillis <= eventTime && eventTime <= timerEventTime){
				return true;
			}
			else{
				return false;
			}
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	@Override
	public TimeCondition save() {
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistor.getEntityManager().persist(this);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TimeCondition remove() {
		//TODO: Das per Hand zu löschen, ist irgendwie Mist
		List<SushiProcess> processes = SushiProcess.findByTimeCondition(this);
		for(SushiProcess process : processes){
			process.setTimeCondition(null);
			process.save();
		}
		try {
			Persistor.getEntityManager().getTransaction().begin();
			Persistor.getEntityManager().remove(this);
			Persistor.getEntityManager().getTransaction().commit();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void removeTimerEvent(SushiEvent timerEvent){
		this.timerEvents.remove(timerEvent);
	}

}
