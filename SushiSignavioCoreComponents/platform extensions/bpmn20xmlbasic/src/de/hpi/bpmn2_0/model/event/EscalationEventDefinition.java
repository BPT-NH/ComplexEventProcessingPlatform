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
package de.hpi.bpmn2_0.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tEscalationEventDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tEscalationEventDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/bpmn20}tEventDefinition">
 *       &lt;attribute name="escalationCode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="escalationRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEscalationEventDefinition")
public class EscalationEventDefinition
    extends EventDefinition
{

	/* Attributes */
    
    @XmlAttribute
    @XmlIDREF
    protected Escalation escalationRef;
    
    /* Constructors */
    
    /**
     * Default constructor
     */
    public EscalationEventDefinition() {}
    
    /**
     * Copy constructor
     * @param escEventDefinition
     */
    public EscalationEventDefinition(EscalationEventDefinition escEventDefinition) {
    	super(escEventDefinition);
    	
    	this.setEscalationRef(escEventDefinition.getEscalationRef());
    }
    
    /* Getter & Setter */

    /**
     * Gets the value of the escalationRef property.
     * 
     * @return
     *     possible object is
     *     {@link Escalation }
     *     
     */
    public Escalation getEscalationRef() {
        return escalationRef;
    }

    /**
     * Sets the value of the escalationRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Escalation }
     *     
     */
    public void setEscalationRef(Escalation value) {
        this.escalationRef = value;
    }

}
