/*******************************************************************************
 * Signavio Core Components
 * Copyright (C) 2012  Signavio GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.signavio.warehouse.business.util.jpdl4;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToJpdl {

	private static JsonToJpdl instance = null;
	private HashMap<String, JSONObject> children;
	private Set<String> uniqueNames;
	private JSONObject processData;

	public static JsonToJpdl getInstance() {
		return instance;
	}

	public static JsonToJpdl createInstance(JSONObject process) {
		instance = new JsonToJpdl(process);
		return instance;
	}

	private JsonToJpdl(JSONObject process) {
		this.processData = process;
		uniqueNames = new HashSet<String>();
		this.children = new HashMap<String, JSONObject>();
		try {
			JSONArray processElements = process.getJSONArray("childShapes");
			// Collect all children for direct access
			for (int i = 0; i < processElements.length(); i++) {
				JSONObject currentElement = processElements.getJSONObject(i);
				
				String name = currentElement.getJSONObject("properties").getString("name");
				if (name.length() == 0 || uniqueNames.contains(name)) {
					String newName = name;
					String type = currentElement.getJSONObject("stencil").getString("id");
					do {
						newName = createName(newName, type);
					} while (uniqueNames.contains(newName));
					uniqueNames.add(newName);
					currentElement.getJSONObject("properties").put("name", newName);
				} else {
					uniqueNames.add(name);
				}
				
				
				this.children.put(currentElement.getString("resourceId"),
						currentElement);
			}
		} catch (JSONException e) {
		}
	}

	private String createName(String name, String type) {
		if (name.length() == 0) {
			return (type == null || type.length() == 0) ? "element_1" : type + "_1";
		} else {
			if (name.contains("_")) {
				String prefix = name.substring(0, name.lastIndexOf("_"));
				String suffix = name.substring(name.lastIndexOf("_") + 1);
				try {
					int i = Integer.parseInt(suffix);
					return prefix + "_" + (i+1);
				} catch (NumberFormatException e) {
					return name + "_1";
				}
				
			} else {
				return name + "_1";
			}
		}
		
	}

	public String transform() throws InvalidModelException {
		// trigger for transformation
		
		// Check if model is of type BPMN 1.2 + jBPM OR of the jbpm4 stencilsset
		
		boolean start = false;
		try {
			String ns = this.processData.getJSONObject("stencilset").getString("namespace");
			start = ns.equals("http://b3mn.org/stencilset/jbpm4#");
		} catch (JSONException e) {
			// nothing
		}

		try {
			JSONArray extensions = this.processData.getJSONArray("ssextensions");
			for (int i = 0; i < extensions.length(); i++){
				if (extensions.getString(i).equals("http://oryx-editor.org/stencilsets/extensions/jbpm#")) {
					start = true;
				}
			}
		} catch (JSONException e) {
			// nothing
		}
		
		if (start) {
			Process process = new Process(this.processData);
			return process.toJpdl();
		} else {
			throw new InvalidModelException("Invalid model type. BPMN 1.2 with jBPM extension is required.");
		}
	}

	public static String transformAttribute(String name, String value) {
		if (value == null)
			return "";
		if (value.equals(""))
			return "";
		
		value = StringEscapeUtils.escapeXml(value);
		
		StringWriter jpdl = new StringWriter();

		jpdl.write(" ");
		jpdl.write(name);
		jpdl.write("=\"");
		jpdl.write(value);
		jpdl.write("\"");

		return jpdl.toString();
	}

	public static String transformRequieredAttribute(String name, String value)
			throws InvalidModelException {
		if (value == null)
			throw new InvalidModelException("Attribute " + name
					+ " is missing.");

		value = StringEscapeUtils.escapeXml(value);
		
		StringWriter jpdl = new StringWriter();

		jpdl.write(" ");
		jpdl.write(name);
		jpdl.write("=\"");
		jpdl.write(value);
		jpdl.write("\"");

		return jpdl.toString();
	}

	public static String getAttribute(JSONObject node, String name) {
		try {
			return node.getJSONObject("properties").optString(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public static Bounds getBounds(JSONObject node) {
		try {
			return new Bounds(node.getJSONObject("bounds"));
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Transition> getOutgoings(JSONObject node) {
		List<Transition> outgoings = new ArrayList<Transition>();
		try {
			JSONArray outs = node.getJSONArray("outgoing");
			for (int i = 0; i < outs.length(); i++) {
				String id = outs.getJSONObject(i).getString("resourceId");
				JSONObject out = JsonToJpdl.getInstance().getChild(id);
				outgoings.add(new Transition(out));
			}
		} catch (JSONException e) {
		}
		return outgoings;
	}

	public String getTargetName(String targetId) {
		JSONObject target = children.get(targetId);
		try {
			String name = target.getJSONObject("properties").getString("name");
			return name;
		} catch (JSONException e) {
			return null;
		}
	}

	public JSONObject getChild(String childId) {
		return children.get(childId);
	}
	
	

}
