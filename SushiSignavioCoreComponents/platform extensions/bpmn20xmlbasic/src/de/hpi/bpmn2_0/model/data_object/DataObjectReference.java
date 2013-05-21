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
package de.hpi.bpmn2_0.model.data_object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.Process;
import de.hpi.bpmn2_0.transformation.Visitor;

/**
 * A DataObjectReference provides a reference to a globally defined {@link DataObject}.
 * 
 * @author Sven Wagner-Boysen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataObjectReference")
public class DataObjectReference extends AbstractDataObject {
	
	
	@XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
	protected DataObject dataObjectRef;
	
	public void setProcess(Process process) {
		super.setProcess(process);
		if(this.dataObjectRef != null)
			this.dataObjectRef.setProcessRef(process);
		
	}
	
	public void acceptVisitor(Visitor v){
		v.visitDataObjectReference(this);
	}
    
	/* Getter & Setter */
	
	/**
     * Gets the value of the dataObjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link DataObject }
     *     
     */
	public DataObject getDataObjectRef() {
		return dataObjectRef;
	}

	/**
     * Sets the value of the dataObjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link DataObject }
     *     
     */
	public void setDataObjectRef(DataObject dataObjectRef) {
		this.dataObjectRef = dataObjectRef;
	}

	
}
