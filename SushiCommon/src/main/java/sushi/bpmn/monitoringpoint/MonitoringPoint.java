package sushi.bpmn.monitoringpoint;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

@Entity
@Table(name = "MonitoringPoint")
public class MonitoringPoint extends Persistable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	protected int ID;
	
	@Column(name = "stateTransitionType")
	private MonitoringPointStateTransition monitoringPointstateTransitionType;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SushiEventType eventType;
	
	@Column(name = "eventTypeCondition")
	private String condition;
	
	
	public MonitoringPoint(){
		ID = 0;
	}
	
	public MonitoringPoint(SushiEventType eventType, MonitoringPointStateTransition stateTransitionType, String condition){
		this.eventType = eventType;
		this.monitoringPointstateTransitionType = stateTransitionType;
		this.condition = condition;
	}
	
	
	public MonitoringPointStateTransition getStateTransitionType() {
		return monitoringPointstateTransitionType;
	}
	
	public void setStateTransitionType(MonitoringPointStateTransition stateTransitionType) {
		this.monitoringPointstateTransitionType = stateTransitionType;
	}

	public SushiEventType getEventType() {
		return eventType;
	}

	public void setEventType(SushiEventType eventType) {
		this.eventType = eventType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getID() {
		return ID;
	}
	
	@Override
	public String toString(){
		//TODO: Implement
		return "MonitoringPoint: " + getStateTransitionType();
	}
	
	public static List<MonitoringPoint> findByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM MonitoringPoint WHERE EVENTTYPE_ID = '" + eventType.getID() + "'", MonitoringPoint.class);
		return query.getResultList();
	}
	
}
