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

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hql extends Sql {

	public Hql(JSONObject hql) {
		super(hql);
	}
	
	public Hql(org.w3c.dom.Node hql) {
		super(hql);
	}

	@Override
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("  <hql");

		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(JsonToJpdl.transformAttribute("var", var));
		if (unique != null)
			jpdl.write(JsonToJpdl.transformAttribute("unique", unique
					.toString()));

		if (bounds != null) {
			jpdl.write(bounds.toJpdl());
		} else {
			throw new InvalidModelException(
					"Invalid HQL activity. Bounds is missing.");
		}

		jpdl.write(" >\n");

		if (query != null) {
			jpdl.write("    <query>");
			jpdl.write(StringEscapeUtils.escapeXml(query));
			jpdl.write("</query>\n");
		} else {
			throw new InvalidModelException(
					"Invalid HQL activity. Query is missing.");
		}

		if (parameters != null) {
			jpdl.write(parameters.toJpdl());
		}

		for (Transition t : outgoings) {
			jpdl.write(t.toJpdl());
		}

		jpdl.write("  </hql>\n");
		return jpdl.toString();
	}
	
	
	
	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject stencil = new JSONObject();
		stencil.put("id", "hql");

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
