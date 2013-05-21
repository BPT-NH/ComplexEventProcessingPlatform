package sushi.query.bpmn;

/**
 * @author micha
 */
public enum EsperPatternOperators {
	
	AND("AND"), XOR("OR"), LOOP("UNTIL"), SEQUENCE("->");
	
	public String operator;

	EsperPatternOperators(String operator) {
		this.operator = operator;
	}

}
