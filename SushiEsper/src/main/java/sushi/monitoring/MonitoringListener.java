package sushi.monitoring;

import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.query.SushiLiveQueryListener;

public class MonitoringListener {
	
	private String elementId;
	private String condition;
	private MonitoringPointStateTransition type;
	private SushiLiveQueryListener listener;
	
	public MonitoringListener(String elementId, String condition, MonitoringPointStateTransition type, SushiLiveQueryListener listener) {
		this.elementId = elementId;
		this.condition = condition;
		this.type = type;
		this.listener = listener;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public MonitoringPointStateTransition getType() {
		return type;
	}

	public void setType(MonitoringPointStateTransition type) {
		this.type = type;
	}

	public SushiLiveQueryListener getListener() {
		return listener;
	}

	public void setListener(SushiLiveQueryListener listener) {
		this.listener = listener;
	}
	
	public String toString() {
		return this.condition;
	}

}
