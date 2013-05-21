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
package de.hpi.bpmn2_0.model.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.hpi.bpmn2_0.model.RootElement;
import de.hpi.bpmn2_0.util.EscapingStringAdapter;

/**
 * Signal details. Used by signal events.
 * 
 * @author Sven Wagner-Boysen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Signal extends RootElement {
	
	/* Attributes */
	@XmlAttribute
	@XmlJavaTypeAdapter(EscapingStringAdapter.class)
	protected String name;
	
	/* Getter & Setter */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
