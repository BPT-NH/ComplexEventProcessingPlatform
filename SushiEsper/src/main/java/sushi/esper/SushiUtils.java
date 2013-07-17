package sushi.esper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

/**
 * This class contains methods that have been registered to Esper.
 * This registration takes place in the @see SushiStreamProcessingAdapter.
 * These methods can be used in Esper queries.  
 */
public class SushiUtils {
	
	public static Date currentDate() {
		return new Date();
	}
	
	/**
	 * Formats a date as defined in the format string.
	 * @param date
	 * @param format
	 * @return formatted date
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat dfmt = new SimpleDateFormat(format);
		return dfmt.format(date);
	}
	
	/**
	 * Checks whether several Integer-Lists have common elements
	 * @param collectionOfIDLists
	 * @return true if intersection is not empty
	 */
	public static boolean isIntersectionNotEmpty(List<Integer>... collectionOfIDLists) {
		if(collectionOfIDLists == null || collectionOfIDLists.length == 0){
			return false;
		}
		List<List<Integer>> copyListOfIDLists = new ArrayList<List<Integer>>();
		for(List<Integer> list : collectionOfIDLists){
			copyListOfIDLists.add(new ArrayList<Integer>(list));
		}
		List<Integer> retainedIDs = copyListOfIDLists.get(0);
		if(retainedIDs.isEmpty()){
			return false;
		}
		for(List<Integer> list : copyListOfIDLists){
			if(list != null){
				retainedIDs.retainAll(list);
			}
		}
		return !retainedIDs.isEmpty();
	}
	
	/**
	 * Returns the common elements of several Integer-Lists.
	 * @param collectionOfIDLists
	 * @return common elements of lists
	 */
	public static List<Integer> getIntersection(List<Integer>... collectionOfIDLists) {
		if(collectionOfIDLists == null || collectionOfIDLists.length == 0){
			return new ArrayList<Integer>();
		}
		List<List<Integer>> copyListOfIDLists = new ArrayList<List<Integer>>();
		for(List<Integer> list : collectionOfIDLists){
			copyListOfIDLists.add(new ArrayList<Integer>(list));
		}
		List<Integer> retainedIDs = copyListOfIDLists.get(0);
		if(retainedIDs.isEmpty()){
			return retainedIDs;
		}
		for(List<Integer> list : copyListOfIDLists){
			retainedIDs.retainAll(list);
		}
		return retainedIDs;
	}
	
	/**
	 * Transforms an attribute-value of an event to an integer value.
	 * @param eventTypeName
	 * @param attributeExpression
	 * @param array
	 * @return integer value
	 */
	public static Integer integerValueFromEvent(String eventTypeName, String attributeExpression, Object[] array) {
		Serializable value = findValueByEventTypeAndAttributeExpressionsAndValues(eventTypeName, attributeExpression, array);
		if (value != null) {
			try {
				return (Integer) value;
			} catch (ClassCastException cce) {
				return new Integer(value.toString());
			}
		}
		return null;
	}

	/**
	 * Transforms an attribute value of an event to a double value.
	 * @param eventTypeName
	 * @param attributeExpression
	 * @param array
	 * @return
	 */
	public static Double doubleValueFromEvent(String eventTypeName, String attributeExpression, Object[] array) {
		Serializable value = findValueByEventTypeAndAttributeExpressionsAndValues(eventTypeName, attributeExpression, array);
		if (value != null) {
			try {
				return (Double) value;
			} catch (ClassCastException cce) {
				return new Double(value.toString());
			}
		}
		return null;
	}
	
	/**
	 * Transforms an attribute value of an event to a string value.
	 * @param eventTypeName
	 * @param attributeExpression
	 * @param array
	 * @return
	 */
	public static String stringValueFromEvent(String eventTypeName, String attributeExpression, Object[] array) {
		Serializable value = findValueByEventTypeAndAttributeExpressionsAndValues(eventTypeName, attributeExpression, array);
		if (value != null) {
			return value.toString();
		}
		return null;
	}
	
	/**
	 * Transforms an attribute value of an event to a date value.
	 * @param eventTypeName
	 * @param attributeExpression
	 * @param array
	 * @return
	 */
	public static Date dateValueFromEvent(String eventTypeName, String attributeExpression, Object[] array) {
		Serializable value = findValueByEventTypeAndAttributeExpressionsAndValues(eventTypeName, attributeExpression, array);
		if (value != null) {
			try {
				return (Date) value;
			} catch (ClassCastException cce) {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					return sdfToDate.parse(value.toString());
				} catch (ParseException pe) {
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns attribute values of events with a certain event type and attribute expressions. 
	 * @param eventTypeName
	 * @param attributeExpression
	 * @param array
	 * @return
	 */
	private static Serializable findValueByEventTypeAndAttributeExpressionsAndValues(String eventTypeName, String attributeExpression, Object[] array) {
		Map<String, Serializable> attributeExpressionsAndValues = new HashMap<String, Serializable>();
		for (int i = 0; i < array.length; i = i+2) {
			attributeExpressionsAndValues.put((String) array[i], (Serializable) array[i+1]);
		}
		return SushiEvent.findValueByEventTypeAndAttributeExpressionsAndValues(SushiEventType.findByTypeName(eventTypeName), attributeExpression, attributeExpressionsAndValues);
	}
	
	/**
	 * Sums up the attribute values of certain attribute from each event of an event list.
	 * @param events
	 * @param attributeName
	 * @return
	 */
	public static Integer sumFromEventList(Node[] events, String attributeName) {
		String[] attributeNameByLevels = {attributeName};
		if (attributeName.contains(".")) {
			attributeNameByLevels = attributeName.split(".");
		}
		Integer result = 0;
		if (events != null) {
			for (Node event : events) {
				Node currentAttribute = event;
				for (int i = 0; i < attributeNameByLevels.length; i++) {
					for (int j = 0; j < currentAttribute.getChildNodes().getLength(); j++) {
						Node childNode = currentAttribute.getChildNodes().item(j);
						if (childNode.getNodeName().equals(attributeNameByLevels[i])) {
							currentAttribute = childNode;
							break;
						}
					}
				}
				try {
					result += new Integer(currentAttribute.getFirstChild().getTextContent());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
