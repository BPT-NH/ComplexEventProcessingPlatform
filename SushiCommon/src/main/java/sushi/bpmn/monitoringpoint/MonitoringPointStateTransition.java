package sushi.bpmn.monitoringpoint;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


/**
 * @author micha
 *
 */
public enum MonitoringPointStateTransition {
	
	@Enumerated(EnumType.STRING)
	enable("enable"),begin("begin"),terminate("terminate"),skip("skip");
	
	private String name;

	MonitoringPointStateTransition(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
