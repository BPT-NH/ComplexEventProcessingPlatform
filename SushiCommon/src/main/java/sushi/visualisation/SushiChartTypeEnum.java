package sushi.visualisation;

public enum SushiChartTypeEnum {
	BAR ("distribution chart"),
	SPLATTER ("point chart");
	
	private String type;
	
	SushiChartTypeEnum(String type){
		this.type = type;
	}

	public String toString() {
		return type;
	}
	
}
