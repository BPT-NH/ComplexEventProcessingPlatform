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
package de.hpi.bpmn2_0.model.extension.signavio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpmn2_0.model.extension.AbstractExtensionElement;

/**
 * The BPMN 2.0 Spec Version 2010-06-04 does not offer to store the name of 
 * a message visible on a choreography participant. Therefore the name property
 * is stored as an signavio extension element.
 * 
 * @author Sven Wagner-Boysen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SignavioMessageName extends AbstractExtensionElement {
	@XmlAttribute
	private String name;
	
	public SignavioMessageName() {
		super();
	}
	
	public SignavioMessageName(String name) {
		super();
		this.name = name;
	}
	
	/* Getter & Setter */

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
