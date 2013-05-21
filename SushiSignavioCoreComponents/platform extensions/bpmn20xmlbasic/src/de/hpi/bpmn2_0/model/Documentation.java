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
package de.hpi.bpmn2_0.model;

import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.hpi.bpmn2_0.util.EscapingStringAdapter;
import de.hpi.diagram.SignavioUUID;


/**
 * <p>Java class for tDocumentation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDocumentation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDocumentation", propOrder = {
		"text"
//		"content"
})
public class Documentation extends BaseElement {
	
	/**
	 * Default constructor
	 */
	public Documentation() {
		
	}
	
	/**
	 * Constructor including documentation's text parameter
	 * @param text
	 */
	public Documentation(String text) {
		this.setText(text);
		this.setId(SignavioUUID.generate());
	}
	
	@XmlMixed
	@XmlJavaTypeAdapter(EscapingStringAdapter.class)
    protected List<String> text;
    
    /* Getter & Setter */
    
	/**
	 * @return the text
	 */
	public String getText() {
		if(this.text == null)
			return "";
		if(text.size() > 0)
			return text.get(0);
		
		return "";
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
//		this.text = text;
		
		this.text = new ArrayList<String>();
		this.text.add(text);
	}
    
//	@XmlMixed
//    @XmlAnyElement(lax = true)
//    protected List<Object> content;
//
//    /**
//     * Gets the value of the content property.
//     * 
//     * <p>
//     * This accessor method returns a reference to the live list,
//     * not a snapshot. Therefore any modification you make to the
//     * returned list will be present inside the JAXB object.
//     * This is why there is not a <CODE>set</CODE> method for the content property.
//     * 
//     * <p>
//     * For example, to add a new item, do as follows:
//     * <pre>
//     *    getContent().add(newItem);
//     * </pre>
//     * 
//     * 
//     * <p>
//     * Objects of the following type(s) are allowed in the list
//     * {@link Object }
//     * {@link String }
//     * {@link Element }
//     * 
//     * 
//     */
//    public List<Object> getContent() {
//        if (content == null) {
//            content = new ArrayList<Object>();
//        }
//        return this.content;
//    }

}
