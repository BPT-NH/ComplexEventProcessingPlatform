package sushi.bpmn2_0.model.extension.monitoring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpmn2_0.model.extension.AbstractExtensionElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Transition extends AbstractExtensionElement {

	@XmlAttribute
	private TransitionType type;
	@XmlAttribute
	private String regularExpression;
	
	public Transition() {
		super();
	}
	
	public Transition(TransitionType type, String regularExpression){
		super();
		this.type = type;
		this.regularExpression = regularExpression;
	}
	
	public TransitionType getType() {
		return type;
	}

	public void setType(TransitionType type) {
		this.type = type;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

}
