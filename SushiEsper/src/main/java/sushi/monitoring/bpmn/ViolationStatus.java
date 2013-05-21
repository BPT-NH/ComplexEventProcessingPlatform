package sushi.monitoring.bpmn;

/**
 * The status of violation a monitored element can adopt.
 * @author micha
 */
public enum ViolationStatus {
	None("None"), Exclusiveness("Exclusiveness"), Order("Order"), Missing("Missing"), Duplication("Duplication");
	
	private String textValue;
	
	private ViolationStatus(String text){
		this.textValue = text;
	}

	public String getTextValue() {
		return textValue;
	}
}
