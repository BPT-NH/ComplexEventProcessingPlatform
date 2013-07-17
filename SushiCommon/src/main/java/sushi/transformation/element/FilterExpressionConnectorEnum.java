package sushi.transformation.element;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeration of supported filter expression connectors.
 */
public enum FilterExpressionConnectorEnum {
	
	AND,
	OR,
	NOT;
	
	/**
	 * Method to retrieve possible connectors for one filter expression (unary connectors).
	 * 
	 * @return list of unary connectors
	 */
	public static List<FilterExpressionConnectorEnum> getUnaryOperators() {
		return Arrays.asList(NOT);
	}
	
	/**
	 * Method to retrieve possible connectors for two filter expressions (binary connectors).
	 * 
	 * @return list of binary connectors
	 */
	public static List<FilterExpressionConnectorEnum> getBinaryOperators() {
		return Arrays.asList(AND, OR);
	}
	
}
