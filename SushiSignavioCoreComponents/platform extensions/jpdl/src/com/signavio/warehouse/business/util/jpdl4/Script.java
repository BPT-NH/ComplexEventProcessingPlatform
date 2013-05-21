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

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;

public class Script extends Node {

	private String expression;
	private String language;
	private String variable;
	private String text;

	public Script(JSONObject script) {

		this.name = JsonToJpdl.getAttribute(script, "name");
		this.expression = JsonToJpdl.getAttribute(script, "expr");
		this.language = JsonToJpdl.getAttribute(script, "lang");
		this.variable = JsonToJpdl.getAttribute(script, "var");
		this.text = JsonToJpdl.getAttribute(script, "text");
		this.bounds = JsonToJpdl.getBounds(script);

		this.outgoings = JsonToJpdl.getOutgoings(script);
	}
	
	public Script(org.w3c.dom.Node script) {
		this.uuid = "oryx_" + UUID.randomUUID().toString();
		NamedNodeMap attributes = script.getAttributes();
		this.name = JpdlToJson.getAttribute(attributes, "name");
		this.expression = JpdlToJson.getAttribute(attributes, "expr");
		this.language = JpdlToJson.getAttribute(attributes, "lang");
		this.variable = JpdlToJson.getAttribute(attributes, "var");
		if (script.hasChildNodes())
			for (org.w3c.dom.Node a = script.getFirstChild(); a != null; a = a.getNextSibling())
				if(a.getNodeName().equals("text")) {
					this.text = a.getTextContent();
					break;
				}
		this.bounds = JpdlToJson.getBounds(attributes.getNamedItem("g"));
	}

	@Override
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("  <script");

		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(JsonToJpdl.transformAttribute("expr", expression));
		jpdl.write(JsonToJpdl.transformAttribute("lang", language));
		jpdl.write(JsonToJpdl.transformAttribute("var", variable));

		if (bounds != null) {
			jpdl.write(bounds.toJpdl());
		} else {
			throw new InvalidModelException(
					"Invalid Script activity. Bounds is missing.");
		}

		jpdl.write(" >\n");

		if (text != null && text.length() > 0) {
			jpdl.write("    <text>");
			jpdl.write(StringEscapeUtils.escapeXml(text));
			jpdl.write("</text>\n");
		}

		for (Transition t : outgoings) {
			jpdl.write(t.toJpdl());
		}

		jpdl.write("  </script>\n\n");

		return jpdl.toString();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject stencil = new JSONObject();
		stencil.put("id", "script");

		JSONArray outgoing = JpdlToJson.getTransitions(outgoings);

		JSONObject properties = new JSONObject();
		properties.put("bgcolor", "#ffffcc");
		if (name != null)
			properties.put("name", name);
		if (expression != null)
			properties.put("expr", expression);
		if (language != null)
			properties.put("lang", language);
		if (variable != null)
			properties.put("var", variable);
		if (text != null)
			properties.put("text", text);

		JSONArray childShapes = new JSONArray();

		return JpdlToJson.createJsonObject(uuid, stencil, outgoing, properties,
				childShapes, bounds.toJson());
	}

}
