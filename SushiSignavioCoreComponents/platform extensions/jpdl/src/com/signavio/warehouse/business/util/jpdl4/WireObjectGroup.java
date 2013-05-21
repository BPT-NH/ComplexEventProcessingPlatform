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

public class WireObjectGroup implements IWireObjectGroup {
	
	protected String name;
	protected String value;
	protected String elementName;
	
	protected WireObjectGroup(String name, String value, String elementName) {
		this.name = name;
		this.value = value;
		this.elementName = elementName;
	}
	
	public WireObjectGroup(String name, String value) {
		throw new RuntimeException("Not allowed! Use WireObjectGroup(String name, String value, String elementName)");
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see com.signavio.warehouse.business.util.jpdl4.IWireObjectGroup#toJpdl()
	 */
	public String toJpdl() {
		
		StringWriter jpdl = new StringWriter();
		jpdl.write("<" + elementName + " ");
		
		if(name != null && !name.isEmpty()) {
			jpdl.write(JsonToJpdl.transformAttribute("name", name));
		}
		
		if(value != null && !value.isEmpty()) {
			jpdl.write(JsonToJpdl.transformAttribute("value", value));
		}
		
		jpdl.write(" />");
		
		return jpdl.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.signavio.warehouse.business.util.jpdl4.IWireObjectGroup#toJson()
	 */
	public JSONObject toJson() throws JSONException {
		JSONObject string = new JSONObject();
		if (name != null)
			string.put("name", name);
		if (value != null)
			string.put("value", value);
		string.put("type", elementName);
		return string;
	}
}
