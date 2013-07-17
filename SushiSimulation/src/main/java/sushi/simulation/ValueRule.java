package sushi.simulation;

import java.io.Serializable;

import sushi.event.attribute.SushiAttribute;

public class ValueRule implements Serializable{

	private SushiAttribute attribute;
	private ValueRuleType ruleType; 
	public ValueRule(){
	}
	public SushiAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(SushiAttribute attribute) {
		this.attribute = attribute;
	}
	public ValueRuleType getRuleType() {
		return ruleType;
	}
	public void setRuleType(ValueRuleType ruleType) {
		this.ruleType = ruleType;
	}
	
}
