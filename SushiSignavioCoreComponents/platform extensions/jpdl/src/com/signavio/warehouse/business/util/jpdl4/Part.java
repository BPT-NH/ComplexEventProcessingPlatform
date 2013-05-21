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

import org.json.JSONException;
import org.json.JSONObject;

public class Part {
	private String expression = null;
	private String name = null;
	private IWireObjectGroup child = null;

	public Part(JSONObject part) {
		try {
			this.name = part.getString("p_name");
		} catch (JSONException e) {}
		
		try {
			this.expression = part.getString("expr");
		} catch (JSONException e) {}
		
		try {
			if(part.getString("type").toLowerCase().equals("string")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireString(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("int")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireInt(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("long")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireLong(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("float")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireFloat(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("double")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireDouble(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("true")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireTrue(sName, sValue);
			}
			if(part.getString("type").toLowerCase().equals("false")) {
				String sName = part.getString("name");
				String sValue = part.getString("value");
				this.child = new WireFalse(sName, sValue);
			}
			
			if(part.getString("type").toLowerCase().equals("object")) {
				String oName = part.getString("value");
				this.child = new WireObjectType(oName);
			}
		} catch (JSONException e) {}
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IWireObjectGroup getChild() {
		return child;
	}

	public void setChild(IWireObjectGroup child) {
		this.child = child;
	}
	
	public String toJpdl() {
		StringWriter jpdl = new StringWriter();
		jpdl.write("    <part ");
		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(JsonToJpdl.transformAttribute("expr", expression));
		if(child != null) {
			jpdl.write(" >");
			jpdl.write(child.toJpdl());
			jpdl.write("</part>\n");
		} else {
			jpdl.write(" />\n");
		}
		return jpdl.toString();
	}

}
