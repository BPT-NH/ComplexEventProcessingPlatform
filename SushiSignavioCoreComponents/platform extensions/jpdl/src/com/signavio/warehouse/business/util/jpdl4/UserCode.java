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
import java.util.List;

public class UserCode {
	private String name;
	private String clazz;
	private List<Field> fields;
	private List<Property> properties;
	
	public UserCode() {
		
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("<" + name);
		jpdl.write(JsonToJpdl.transformAttribute("class", clazz));
		jpdl.write(" >\n");
		
		for(Property p : properties)
			jpdl.write(p.toJpdl());
		
		for(Field f : fields)
			jpdl.write(f.toJpdl());
		
		jpdl.write("</" + name + ">\n");
		
		return jpdl.toString();
	}
	
	
}
