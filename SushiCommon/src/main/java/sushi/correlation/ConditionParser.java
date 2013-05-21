package sushi.correlation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import sushi.event.collection.SushiMapTree;

/**
 * @author micha
 *
 */
public class ConditionParser {
	
	/**
	 * Attribute und Attributwerte aus dem Conditionstring extrahieren.
	 * Attribute sollten im String die Form haben:
	 * attribute1=attributevalue1[;attribute2=attributevalue2] 
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
