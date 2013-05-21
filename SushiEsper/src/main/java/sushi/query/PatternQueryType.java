package sushi.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for various patterns of well-structured BPMN components.
 * @author micha
 */
public enum PatternQueryType {
	
	AND("AND"), XOR("XOR"), SEQUENCE("SEQUENCE"), LOOP("LOOP"), SUBPROCESS("SUBPROCESS"), TIMER("TIMER"), STATETRANSITION("STATETRANSITION");
	
	public String value;

	private PatternQueryType(String value) {
		this.value = value;
	}
	
	public static boolean contains(String value){
		for(PatternQueryType pattern : PatternQueryType.values()){
			if(pattern.value.equals(value)){
				return true;
			}
		}
		return false;
	}
	
	public static List<String> getValues(){
		List<String> values = new ArrayList<String>();
		for(PatternQueryType pattern : PatternQueryType.values()){
			values.add(pattern.value);
		}
		return values;
	}

}
