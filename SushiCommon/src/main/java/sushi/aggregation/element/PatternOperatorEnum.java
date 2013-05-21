package sushi.aggregation.element;

import java.util.Arrays;
import java.util.List;

public enum PatternOperatorEnum {
	
	EVERY, 
	EVERY_DISTINCT, 
	REPEAT,
	UNTIL,
	AND,
	OR,
	NOT,
	FOLLOWED_BY;
	
	public static List<PatternOperatorEnum> getUnaryOperators() {
		return Arrays.asList(EVERY, EVERY_DISTINCT, REPEAT, NOT);
	}
	
	public static List<PatternOperatorEnum> getBinaryOperators() {
		return Arrays.asList(UNTIL, AND, OR, FOLLOWED_BY);
	}
	
}
