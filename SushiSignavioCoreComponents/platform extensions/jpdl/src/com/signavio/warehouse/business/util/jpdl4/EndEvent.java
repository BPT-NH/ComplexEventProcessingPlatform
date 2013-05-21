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
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;

public class EndEvent extends Node {

	protected String state;
	protected String ends;

	public EndEvent(JSONObject endEvent) {

		this.name = JsonToJpdl.getAttribute(endEvent, "name");
		this.ends = JsonToJpdl.getAttribute(endEvent, "ends");
		this.state = JsonToJpdl.getAttribute(endEvent, "state");
		this.bounds = JsonToJpdl.getBounds(endEvent);
		
		this.bounds.setUlx(this.bounds.getUlx() - 10);
		this.bounds.setUly(this.bounds.getUly() - 10);
		this.bounds.setWidth(48);
		this.bounds.setHeight(48);
		

	}
	
	public EndEvent(org.w3c.dom.Node endEvent) {
		this.uuid = "oryx_" + UUID.randomUUID().toString();
		NamedNodeMap attributes = endEvent.getAttributes();
		this.name = JpdlToJson.getAttribute(attributes, "name");
		this.ends = JpdlToJson.getAttribute(attributes, "ends");
		this.state = JpdlToJson.getAttribute(attributes, "state");
		this.bounds = JpdlToJson.getBounds(attributes.getNamedItem("g"));
		
		this.bounds.setUlx(this.bounds.getUlx() + 10);
		this.bounds.setUly(this.bounds.getUly() + 10);
		this.bounds.setWidth(28);
		this.bounds.setHeight(28);
		
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEnds() {
		return ends;
	}

	public void setEnds(String ends) {
		this.ends = ends;
	}

	@Override
	public String toJpdl() throws InvalidModelException {
		String id = "end";
		return writeJpdlAttributes(id).toString();

	}

	@Override
	public JSONObject toJson() throws JSONException {
		String id = "EndEvent";

		return writeJsonAttributes(id);
	}

	protected JSONObject writeJsonAttributes(String id) throws JSONException {
		JSONObject stencil = new JSONObject();
		stencil.put("id", id);

		JSONArray outgoing = new JSONArray();

		JSONObject properties = new JSONObject();
		properties.put("bgcolor", "#ffffff");
		if (name != null)
			properties.put("name", name);
		if (state != null)
			properties.put("state", state);
		if (ends != null)
			properties.put("ends", ends);
		else
			properties.put("ends", "processinstance"); // default value

		JSONArray childShapes = new JSONArray();

		return JpdlToJson.createJsonObject(uuid, stencil, outgoing, properties,
				childShapes, bounds.toJson());
	}

	protected String writeJpdlAttributes(String id)
			throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("  <" + id);
		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		//if (!ends.equals("process-instance")) // processinstance is default value
			jpdl.write(JsonToJpdl.transformAttribute("ends", ends));
		jpdl.write(JsonToJpdl.transformAttribute("state", state));

		if (bounds != null) {
			jpdl.write(bounds.toJpdl());
		} else {
			throw new InvalidModelException(
					"Invalid End Event. Bounds is missing.");
		}

		jpdl.write(" />\n\n");

		return jpdl.toString();
	}
}
