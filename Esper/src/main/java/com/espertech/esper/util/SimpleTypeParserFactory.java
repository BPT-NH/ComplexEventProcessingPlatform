/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.espertech.esper.type.*;

/**
 * A factory for creating an instance of a parser that parses a String and returns a target type. 
 */
public class SimpleTypeParserFactory
{
    /**
     * Returns a parsers for the String value using the given Java built-in class for parsing.
     * @param clazz is the class to parse the value to
     * @return value matching the type passed in
     */
    public static SimpleTypeParser getParser(Class clazz)
    {
        Class classBoxed = JavaClassHelper.getBoxedType(clazz);

        if (classBoxed == String.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String value)
                {
                    return value;
                }
            };
        }
        if (classBoxed == Character.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String value)
                {
                    return value.charAt(0);
                }
            };
        }
        if (classBoxed == Boolean.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return BoolValue.parseString(text.toLowerCase().trim());
                }
            };
        }
        if (classBoxed == Byte.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return ByteValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Short.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return ShortValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Long.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return LongValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Float.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return FloatValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Double.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return DoubleValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Integer.class)
        {
            return new SimpleTypeParser() {
                public Object parse(String text)
                {
                    return IntValue.parseString(text.trim());
                }
            };
        }
        if (classBoxed == Date.class) {
        	return new SimpleTypeParser() {
        		public Object parse(String text) {
        			SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        			try {
        				return parser.parse(text);
        			} catch (Exception ex) { 
        				ex.printStackTrace();
        				return null;
        			}
        		}
        	};
        }
        
        if (classBoxed == List.class) {
        	return new SimpleTypeParser() {
        		public Object parse(String text) {
        			try {
        				String listString = text.substring(1, text.length() - 1).replaceAll("\\s", "");
            			String[] listArray = listString.split(",");
            			ArrayList<Integer> integerList = new ArrayList<Integer>();
            			for (String element : listArray) {
            				integerList.add(Integer.parseInt(element));
            			}
            			return integerList;
        			} catch (Exception ex) {
        				ex.printStackTrace();
        				return new ArrayList();
        			}
        		}
        	};
        }
        
//        if (classBoxed == Map.class) {
//        	return new SimpleTypeParser() {
//        		public Object parse(String text) {
//        			try {
//        				String[] listArray = text.substring(1, text.length() - 1).replaceAll("\\s", "").split(",");
//            			Map<String, Serializable> attributeExpressionsAndValues = new HashMap<String, Serializable>();
//            			for (int i = 0; i < listArray.length; i = i+2) {
//            				String[] elements = listArray[i].substring(1, listArray[i].length() - 1).split(":");
//            				assert(elements.length == 2);
//            				String type = elements[0];
//            				String attributeExpression = elements[1];
//            				String value = listArray[i+1];
//            				if (type.equals("Double") || type.equals("Integer")) {
//            					attributeExpressionsAndValues.put(attributeExpression, new Double(Double.parseDouble(value)));
//            				} else if (type.equals("Date")) {
//                				attributeExpressionsAndValues.put(attributeExpression, (new SimpleDateFormat()).parse(value));
//            				} else {
//            					attributeExpressionsAndValues.put(attributeExpression, value);
//            				}
//            			}
//            			return attributeExpressionsAndValues;
//        			} catch (Exception ex) {
//        				ex.printStackTrace();
//        				return new HashMap<String, Serializable>();
//        			}
//        		}
//        	};
//        }
        
        return null;
    }    
}
