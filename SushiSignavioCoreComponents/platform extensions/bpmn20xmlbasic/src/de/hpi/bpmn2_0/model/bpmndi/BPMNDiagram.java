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
package de.hpi.bpmn2_0.model.bpmndi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.bpmndi.di.Diagram;
import de.hpi.bpmn2_0.model.bpmndi.di.DiagramElement;
import de.hpi.bpmn2_0.model.bpmndi.di.Shape;
import de.hpi.bpmn2_0.model.participant.Lane;
import de.hpi.bpmn2_0.model.participant.Participant;
import de.hpi.diagram.SignavioUUID;


/**
 * <p>Java class for BPMNDiagram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BPMNDiagram">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/DD/20100524/DI}Diagram">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/DI}BPMNPlane"/>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/DI}BPMNLabelStyle" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "BPMNDiagram")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "bpmnPlane",
    "bpmnLabelStyle"
})
public class BPMNDiagram
    extends Diagram
{

    @XmlElement(name = "BPMNPlane", required = true)
    protected BPMNPlane bpmnPlane;
    @XmlElement(name = "BPMNLabelStyle")
    protected List<BPMNLabelStyle> bpmnLabelStyle;

    /* Constructor */
    public BPMNDiagram() {
    	super();
    	id = SignavioUUID.generate();
    	bpmnPlane = new BPMNPlane();
    }
    
    /* Public methods */
    /**
     * Returns the orientation of the diagram depending on the pool and lane
     * elements included.
     */
    public String getOrientation() {
    	int countH = 0;
    	int countV = 0;
    	
    	for(DiagramElement de : this.getBPMNPlane().getDiagramElement()) {
    		if(de instanceof BPMNShape) {
    			BPMNShape s = (BPMNShape) de;
    			
    			if(((s.getBpmnElement() instanceof Lane) 
    					|| (s.getBpmnElement() instanceof Participant)) 
    				&& s.isIsHorizontalNoNull()) {
    					countH++;
    			} else {
    				countV++;
    			}
    		}
    	}
    	
    	return (countV > countH ? "vertical" : "horizontal");
    }
    
    /* Getter & Setter */
    
    /**
     * Gets the value of the bpmnPlane property.
     * 
     * @return
     *     possible object is
     *     {@link BPMNPlane }
     *     
     */
    public BPMNPlane getBPMNPlane() {
        return bpmnPlane;
    }

    /**
     * Sets the value of the bpmnPlane property.
     * 
     * @param value
     *     allowed object is
     *     {@link BPMNPlane }
     *     
     */
    public void setBPMNPlane(BPMNPlane value) {
        this.bpmnPlane = value;
    }

    /**
     * Gets the value of the bpmnLabelStyle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bpmnLabelStyle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBPMNLabelStyle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BPMNLabelStyle }
     * 
     * 
     */
    public List<BPMNLabelStyle> getBPMNLabelStyle() {
        if (bpmnLabelStyle == null) {
            bpmnLabelStyle = new ArrayList<BPMNLabelStyle>();
        }
        return this.bpmnLabelStyle;
    }

}
