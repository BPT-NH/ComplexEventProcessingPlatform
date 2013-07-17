package sushi.correlation;

import java.io.Serializable;

import sushi.event.collection.SushiMapTree;

/**
 * Parses attribute-value pairs of events from a string.
 * 
 * @author micha
 *
 */
public class ConditionParser {
	
	/**
	 * Parses attribute-value pairs of events from a string.
	 * Syntax of the string must be: attribute1=attributevalue1[;attribute2=attributevalue2] 
	 */
	public static SushiMapTree<String, Serializable> extractEventAttributes(String conditionString) {
		SushiMapTree<String, Serializable> eventAttributes = new SushiMapTree<String, Serializable>();
		String[] attributes = conditionString.split(";");
		for(String attribute : attributes){
			String[] attributePair = attribute.split("=");
			if (attributePair.length == 2) {
			eventAttributes.put(attributePair[0], attributePair[1]);
			}
		}
		return eventAttributes;
	}
	

}
