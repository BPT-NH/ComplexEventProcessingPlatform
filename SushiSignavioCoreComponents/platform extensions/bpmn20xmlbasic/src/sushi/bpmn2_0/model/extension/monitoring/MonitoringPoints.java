package sushi.bpmn2_0.model.extension.monitoring;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.bpmndi.BPMNLabelStyle;
import de.hpi.bpmn2_0.model.extension.AbstractExtensionElement;

@XmlRootElement(name = "MonitoringPoints")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType

public class MonitoringPoints extends AbstractExtensionElement{

	@XmlElement(name = "transition")
    protected List<Transition> transition;
    
	public MonitoringPoints() {
		super();
	}
    
    
    public List<Transition> getTransitions() {
        if (transition == null) {
            transition = new ArrayList<Transition>();
        }
        return this.transition;
    }

}
