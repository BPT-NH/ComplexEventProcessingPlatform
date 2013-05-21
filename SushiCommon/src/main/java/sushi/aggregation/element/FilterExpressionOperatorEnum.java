package sushi.aggregation.element;

public enum FilterExpressionOperatorEnum {
	
	EQUALS ("="),
	NOT_EQUALS ("!="),
	SMALLER_THAN ("<"),
	GREATER_THAN (">"),
	SMALLER_OR_EQUALS ("<="),
	GREATER_OR_EQUALS (">="),
	IN ("IN"),
	NOT_IN ("NOT IN");
	
	private final String value;
	
	private FilterExpressionOperatorEnum(String value) {
	    this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
