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
package de.hpi.bpmn2_0.model.participant;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import de.hpi.bpmn2_0.model.BaseElement;
import de.hpi.bpmn2_0.model.FlowNode;
import de.hpi.bpmn2_0.model.Process;
import de.hpi.bpmn2_0.model.conversation.ConversationElement;
import de.hpi.bpmn2_0.model.data_object.Message;
import de.hpi.bpmn2_0.transformation.Visitor;
import de.hpi.diagram.SignavioUUID;


/**
 * <p>Java class for tParticipant complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tParticipant">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/bpmn20}tBaseElement">
 *       &lt;sequence>
 *         &lt;element name="interfaceRef" type="{http://www.w3.org/2001/XMLSchema}QName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="endPointRef" type="{http://www.w3.org/2001/XMLSchema}QName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.omg.org/bpmn20}participantMultiplicity" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="partnerRoleRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="partnerEntityRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="processRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tParticipant", propOrder = {
    "interfaceRef",
    "endPointRef",
    "participantMultiplicity"
})
public class Participant
    extends FlowNode implements ConversationElement
{

    @XmlElement
	protected List<QName> interfaceRef;
    @XmlElement
    protected List<QName> endPointRef;
    @XmlElement(type = ParticipantMultiplicity.class)
    protected ParticipantMultiplicity participantMultiplicity;
    
    @XmlAttribute
    @XmlIDREF
    protected Process processRef;
    
    @XmlAttribute
    protected QName partnerRoleRef;
    @XmlAttribute
    protected QName partnerEntityRef;
    
    @XmlTransient
    protected boolean isInitiating;
    
    @XmlTransient
    private LaneSet laneSet;
    
    @XmlTransient
	public String _processType;
	@XmlTransient
	public String _isClosed;
	@XmlTransient
	public String _isExecutable;
	@XmlTransient
	public boolean _isChoreographyParticipant = false;
	@XmlTransient
	public Message _msgRef;
    
    /*
     * Constructors
     */
	
	/**
	 * Default constructor
	 */
	public Participant() {
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param p 
	 * 		template {@link Participant}
	 */
    public Participant(Participant p) {
		super(p);
		
		this.getInterfaceRef().addAll(p.getInterfaceRef());
		this.getEndPointRef().addAll(p.getEndPointRef());
		
		this.setParticipantMultiplicity(p.getParticipantMultiplicity());
		this.setProcessRef(p.getProcessRef());
		
		this.setPartnerRoleRef(p.getPartnerRoleRef());
		this.setPartnerEntityRef(p.getPartnerEntityRef());
		
		this.setInitiating(p.isInitiating());
		this.setLaneSet(p.getLaneSet());
		
		this._processType = p._processType;
		this._isClosed = p._isClosed;
		this._isExecutable = p._isExecutable;
		this._isChoreographyParticipant = p._isChoreographyParticipant;
	}
    
    /* Business logic methods */

	// @Override
    public void addChild(BaseElement child) {
    	if(child instanceof Lane) {
    		if(laneSet == null) {
    			laneSet = new LaneSet();
    			laneSet.setId(SignavioUUID.generate());
    		}
    		
    		getLaneSet().addChild(child);
    	}
    }
    
	public void acceptVisitor(Visitor v){
		v.visitParticipant(this);
	}
    
    /* Getter & Setter */
    /**
     * Gets the value of the interfaceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interfaceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterfaceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QName }
     * 
     * 
     */
    public List<QName> getInterfaceRef() {
        if (interfaceRef == null) {
            interfaceRef = new ArrayList<QName>();
        }
        return this.interfaceRef;
    }

    /**
	 * @return the processRef
	 */
	public Process getProcessRef() {
		return this.processRef;
	}

	/**
	 * @param processRef the processRef to set
	 */
	public void setProcessRef(Process processRef) {
		this.processRef = processRef;
	}

	/**
     * Gets the value of the endPointRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endPointRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndPointRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QName }
     * 
     * 
     */
    public List<QName> getEndPointRef() {
        if (endPointRef == null) {
            endPointRef = new ArrayList<QName>();
        }
        return this.endPointRef;
    }

    /**
     * Gets the value of the participantMultiplicity property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantMultiplicity }
     *     
     */
    public ParticipantMultiplicity getParticipantMultiplicity() {
        return participantMultiplicity;
    }

    /**
     * Sets the value of the participantMultiplicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantMultiplicity }
     *     
     */
    public void setParticipantMultiplicity(ParticipantMultiplicity value) {
        this.participantMultiplicity = value;
    }

    /**
	 * @return the isInitiating
	 */
	public boolean isInitiating() {
		return isInitiating;
	}

	/**
	 * @param isInitiating the isInitiating to set
	 */
	public void setInitiating(boolean isInitiating) {
		this.isInitiating = isInitiating;
	}

    /**
     * Gets the value of the partnerRoleRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getPartnerRoleRef() {
        return partnerRoleRef;
    }

    /**
     * Sets the value of the partnerRoleRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setPartnerRoleRef(QName value) {
        this.partnerRoleRef = value;
    }

    /**
     * Gets the value of the partnerEntityRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getPartnerEntityRef() {
        return partnerEntityRef;
    }

    /**
     * Sets the value of the partnerEntityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setPartnerEntityRef(QName value) {
        this.partnerEntityRef = value;
    }

	public LaneSet getLaneSet() {
		return laneSet;
	}

	public void setLaneSet(LaneSet laneSet) {
		this.laneSet = laneSet;
	}
}
