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
package de.hpi.bpmn2_0.model.callable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.CallableElement;
import de.hpi.bpmn2_0.model.activity.resource.HumanPerformer;
import de.hpi.bpmn2_0.model.activity.resource.Performer;
import de.hpi.bpmn2_0.model.activity.resource.PotentialOwner;
import de.hpi.bpmn2_0.model.activity.resource.ResourceRole;


/**
 * <p>Java class for tGlobalTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tGlobalTask">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tCallableElement">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}resourceRole" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tGlobalTask", propOrder = {
    "resourceRole"
})
@XmlSeeAlso({
    GlobalUserTask.class,
    GlobalBusinessRuleTask.class,
    GlobalScriptTask.class,
    GlobalManualTask.class
})
public class GlobalTask
    extends CallableElement
{
	
	/* Constructors */
	public GlobalTask() {
		super();
	}
	
	public GlobalTask(GlobalTask gt) {
		super(gt);
		
		this.getResourceRole().addAll(gt.getResourceRole());
	}

    @XmlElements({
    	@XmlElement(type = ResourceRole.class),
    	@XmlElement(type = Performer.class)
    })
    protected List<ResourceRole> resourceRole;

    /**
     * Gets the value of the resourceRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Performer }
     * {@link PotentialOwner}
     * {@link HumanPerformer}
     * {@link ResourceRole}
     * 
     * 
     */
    public List<ResourceRole> getResourceRole() {
        if (resourceRole == null) {
            resourceRole = new ArrayList<ResourceRole>();
        }
        return this.resourceRole;
    }

}
