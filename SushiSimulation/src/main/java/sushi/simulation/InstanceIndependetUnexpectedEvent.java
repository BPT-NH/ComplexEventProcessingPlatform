package sushi.simulation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.attribute.SushiAttribute;

public class InstanceIndependetUnexpectedEvent extends PathSimulator{

	public InstanceIndependetUnexpectedEvent(AbstractBPMNElement startElement, InstanceSimulator parentSimulator, Date currentSimulationDate) {
		super(startElement, parentSimulator, currentSimulationDate);
	}



}
