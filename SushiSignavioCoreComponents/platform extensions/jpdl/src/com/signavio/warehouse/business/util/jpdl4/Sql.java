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

public class Sql extends Node {

	protected String var;
	protected Boolean unique;
	protected String query;
	protected Parameters parameters;

	public Sql(JSONObject sql) {

		this.name = JsonToJpdl.getAttribute(sql, "name");
		this.var = JsonToJpdl.getAttribute(sql, "var");
		this.unique = new Boolean(JsonToJpdl.getAttribute(sql, "unique"));
		this.query = JsonToJpdl.getAttribute(sql, "query");
		try {
			this.parameters = new Parameters(sql.getJSONObject("properties")
					.getJSONObject("parameters"));
		} catch (JSONException e) {
			this.parameters = null;
		}

		this.bounds = JsonToJpdl.getBounds(sql);

		this.outgoings = JsonToJpdl.getOutgoings(sql);

	}

	public Sql(org.w3c.dom.Node sql) {
		this.uuid = "oryx_" + UUID.randomUUID().toString();
		NamedNodeMap attributes = sql.getAttributes();
		this.name = JpdlToJson.getAttribute(attributes, "name");
		this.unique = Boolean.parseBoolean(JpdlToJson.getAttribute(attributes,
				"unique"));
		this.var = JpdlToJson.getAttribute(attributes, "var");

		this.bounds = JpdlToJson.getBounds(attributes.getNamedItem("g"));

		if (sql.hasChildNodes())
			for (org.w3c.dom.Node a = sql.getFirstChild(); a != null; a = a
					.getNextSibling()) {
				if (a.getNodeName().equals("query"))
					this.query = a.getTextContent();
				if (a.getNodeName().equals("parameters"))
					this.parameters = new Parameters(a);
			}
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("  <sql");

		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(JsonToJpdl.transformAttribute("var", var));
		if (unique != null)
			jpdl.write(JsonToJpdl.transformAttribute("unique", unique
					.toString()));

		if (bounds != null) {
			jpdl.write(bounds.toJpdl());
		} else {
			throw new InvalidModelException(
					"Invalid SQL activity. Bounds is missing.");
		}

		jpdl.write(" >\n");

		if (query != null) {
			jpdl.write("    <query>");
			jpdl.write(StringEscapeUtils.escapeXml(query));
			jpdl.write("</query>\n");
		} else {
			throw new InvalidModelException(
					"Invalid SQL activity. Query is missing.");
		}

		if (parameters != null) {
			jpdl.write(parameters.toJpdl());
		}

		for (Transition t : outgoings) {
			jpdl.write(t.toJpdl());
		}

		jpdl.write("  </sql>\n\n");

		return jpdl.toString();
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject stencil = new JSONObject();
		stencil.put("id", "sql");

		JSONArray outgoing = JpdlToJson.getTransitions(outgoings);

		JSONObject properties = new JSONObject();
		properties.put("bgcolor", "#ffffcc");
		if (name != null)
			properties.put("name", name);
		if (var != null)
			properties.put("var", var);
		if (unique != null)
			properties.put("unique", unique.toString());
		if (query != null)
			properties.put("query", query);
		if(parameters != null)
			properties.put("parameters", parameters.toJson());
		
		JSONArray childShapes = new JSONArray();

		return JpdlToJson.createJsonObject(uuid, stencil, outgoing, properties,
				childShapes, bounds.toJson());
	}

}
