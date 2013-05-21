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
import org.json.JSONException;
import org.json.JSONObject;

public class Arg {
	private IWireObjectGroup child = null;
	private String type;
	private String a_type = null;
	
	public Arg() {
		super();
	}
	
	public void setA_type(String aType) {
		a_type = aType;
	}

	public Arg (JSONObject arg) {
		try {
			type = arg.getString("type");
			if(type.toLowerCase().equals("string")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireString(sName, sValue);
			}
			if(type.toLowerCase().equals("int")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireInt(sName, sValue);
			}
			if(type.toLowerCase().equals("long")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireLong(sName, sValue);
			}
			if(type.toLowerCase().equals("float")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireFloat(sName, sValue);
			}
			if(type.toLowerCase().equals("double")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireDouble(sName, sValue);
			}
			if(type.toLowerCase().equals("true")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireTrue(sName, sValue);
			}
			if(type.toLowerCase().equals("false")) {
				String sName = arg.getString("name");
				String sValue = arg.getString("value");
				this.child = new WireFalse(sName, sValue);
			}
			
			if(type.toLowerCase().equals("object")) {
				String oName = arg.getString("value");
				this.child = new WireObjectType(oName);
			}
			if(arg.has("a_type")) {
				a_type = arg.getString("a_type");
			}
		} catch (JSONException e) {}
	}
	
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("    <arg");
		if(a_type != null && a_type.length() > 0) {
			jpdl.write(" type=\"" + StringEscapeUtils.escapeXml(a_type) + "\">");
		} else {
			jpdl.write(">");
		}
		if(child != null) {
			jpdl.write(child.toJpdl());
		} else {
			throw new InvalidModelException("Invalid Arg. Object or String is missing");
		}
		jpdl.write("</arg>\n");
		return jpdl.toString();
	}

	public IWireObjectGroup getChild() {
		return child;
	}

	public void setChild(IWireObjectGroup child) {
		this.child = child;
	}
}
