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

public class Field {
	private IWireObjectGroup child = null;
	private String name;
	
	protected String elementName = "field";
	
	public Field(String name) {
		this.name = name;
	}
	
	public Field (JSONObject field) {
		try {
			this.name = field.getString("f_name");
		} catch (JSONException e) {}
		
		try {
			if(field.getString("type").toLowerCase().equals("string")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireString(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("int")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireInt(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("long")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireLong(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("float")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireFloat(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("double")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireDouble(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("true")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireTrue(sName, sValue);
			}
			if(field.getString("type").toLowerCase().equals("false")) {
				String sName = field.getString("name");
				String sValue = field.getString("value");
				this.child = new WireFalse(sName, sValue);
			}
			
			if(field.getString("type").toLowerCase().equals("object")) {
				String oName = field.getString("value");
				this.child = new WireObjectType(oName);
			}
		} catch (JSONException e) {}
	}
	
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("    <" + elementName + " ");
		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(" >\n");
		if(child != null) {
			jpdl.write(child.toJpdl());
		} else {
			throw new InvalidModelException("Invalid Field. Object or String is missing");
		}
		jpdl.write("    </" + elementName + ">\n");
		return jpdl.toString();
	}

	public IWireObjectGroup getChild() {
		return child;
	}

	public void setChild(IWireObjectGroup child) {
		this.child = child;
	}
}
