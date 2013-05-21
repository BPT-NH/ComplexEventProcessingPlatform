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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JpdlToJson {
	private static Process process;
	
	public static String transform(Document doc) {
		// trigger for transformation
		Node root = getRootNode(doc);
		if (root == null)
			return "";
			
		process = new Process(root);
		process.createTransitions();
		
		try {
			return process.toJson();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";

	}

	public static JSONObject createJsonObject(String uuid, JSONObject stencil,
			JSONArray outgoing, JSONObject properties, JSONArray childShapes,
			JSONObject bounds) throws JSONException {
		// create Oryx compliant JSONObject for Node
		JSONObject node = new JSONObject();

		node.put("bounds", bounds);
		node.put("resourceId", uuid);
		node.put("stencil", stencil);
		node.put("outgoing", outgoing);
		node.put("properties", properties);
		node.put("childShapes", childShapes);
		return node;
	}

	private static Node getRootNode(Document doc) {
		Node node = doc.getDocumentElement();
		if (node == null || !node.getNodeName().equals("process"))
			return null;
		return node;
	}
	
	public static Bounds getBounds(Node node) {
		if (node != null) {
			String bounds = node.getNodeValue();
			return new Bounds(bounds.split(","));
		} else {
			return new Bounds();
		}
	}
	
	public static String getAttribute(NamedNodeMap attributes, String name) {
		if(attributes.getNamedItem(name) != null)
			return attributes.getNamedItem(name).getNodeValue();
		return null;
	}
	
	public static Process getProcess() {
		return process;
	}
	
	public static JSONArray getTransitions(List<Transition> outgoings) throws JSONException {
		JSONArray outgoing = new JSONArray();

		for(Transition t : outgoings) {
			JSONObject tt = new JSONObject();
			tt.put("resourceId", t.getUuid());
			outgoing.put(tt);
		}
		return outgoing;
	}
}
