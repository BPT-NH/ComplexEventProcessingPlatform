package sushi.bpmn.decomposition;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for various patterns of well-structured BPMN components.
 * @author micha
 */
public enum IPattern {
	
	AND("AND"), XOR("XOR"), SEQUENCE("SEQUENCE"), LOOP("LOOP"), SUBPROCESS("SUBPROCESS");
	
	public String value;

	private IPattern(String value) {
		this.value = value;
	}
	
	public static boolean contains(String value){
		for(IPattern pattern : IPattern.values()){
			if(pattern.value.equals(value)){
				return true;
			}
		}
		return false;
	}
	
	public static List<String> getValues(){
		List<String> values = new ArrayList<String>();
		for(IPattern pattern : IPattern.values()){
			values.add(pattern.value);
		}
		return values;
	}

}
