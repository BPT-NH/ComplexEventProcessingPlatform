package sushi.aggregation.element;

import java.util.Arrays;
import java.util.List;

public enum FilterExpressionConnectorEnum {
	
	AND,
	OR,
	NOT;
	
	public static List<FilterExpressionConnectorEnum> getUnaryOperators() {
		return Arrays.asList(NOT);
	}
	
	public static List<FilterExpressionConnectorEnum> getBinaryOperators() {
		return Arrays.asList(AND, OR);
	}
	
}
