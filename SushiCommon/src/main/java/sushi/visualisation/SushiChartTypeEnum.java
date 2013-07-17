package sushi.visualisation;

/**
 * This enumeration encapsulates the types of attribute charts ( @see SushiChartConfiguration).
 * COLUMN : attribute chart for the frequency of values for a certain attribute.
 * SPLATTER: attribute chart visualizing the single values of a certain attribute
 */
public enum SushiChartTypeEnum {
	COLUMN ("distribution chart"),
	SPLATTER ("point chart");
	
	private String type;
	
	SushiChartTypeEnum(String type){
		this.type = type;
	}

	public String toString() {
		return type;
	}
	
}
