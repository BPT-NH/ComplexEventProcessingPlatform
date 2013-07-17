package sushi.bpmn.element;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * This enum contains the gateway types of an BPMN event-based gateway.
 * @author micha
 */
public enum BPMNEventBasedGatewayType {
	
	@Enumerated(EnumType.STRING)
	Exclusive, Parallel;

}
