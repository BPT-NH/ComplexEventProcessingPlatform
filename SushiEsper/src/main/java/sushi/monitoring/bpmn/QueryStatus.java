package sushi.monitoring.bpmn;

/**
 * @author micha
 */
public enum QueryStatus {
	Started("Started"), Finished("Finished"), Skipped("Skipped"), NotExisting("Not Existing"), Aborted("Aborted");
	
	private String textValue;
	
	private QueryStatus(String text){
		this.textValue = text;
	}

	public String getTextValue() {
		return textValue;
	}
}
