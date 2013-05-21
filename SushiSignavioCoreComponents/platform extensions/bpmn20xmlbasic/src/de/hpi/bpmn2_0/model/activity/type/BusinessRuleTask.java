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
package de.hpi.bpmn2_0.model.activity.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.activity.Task;
import de.hpi.bpmn2_0.model.activity.misc.BusinessRuleTaskImplementation;
import de.hpi.bpmn2_0.model.callable.GlobalBusinessRuleTask;
import de.hpi.bpmn2_0.model.callable.GlobalTask;
import de.hpi.bpmn2_0.transformation.Visitor;


/**
 * <p>Java class for tBusinessRuleTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tBusinessRuleTask">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/bpmn20}tTask">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBusinessRuleTask")
public class BusinessRuleTask
    extends Task
{
	@XmlAttribute
	BusinessRuleTaskImplementation implementation;
	
	/* Constructors */
	
	/**
	 * Default constructor
	 */
	public BusinessRuleTask() {	}
	
	/**
	 * Copy constructor
	 * 
	 * @param brTask
	 * 		Template {@link BusinessRuleTask}
	 */
	public BusinessRuleTask(BusinessRuleTask brTask) {
		super(brTask);
		this.setImplementation(brTask.getImplementation());
	}
	
	public void acceptVisitor(Visitor v){
		v.visitBusinessRuleTask(this);
	}
	
	public GlobalTask getAsGlobalTask() {
		GlobalBusinessRuleTask brGt = new GlobalBusinessRuleTask(super.getAsGlobalTask());
		brGt.setImplementation(this.getImplementation());
		
		return brGt;
	}
	
	

	/* Getter & Setter */
	
	/**
	 * @return the implementation
	 */
	public BusinessRuleTaskImplementation getImplementation() {
		return implementation;
	}

	/**
	 * @param implementation the implementation to set
	 */
	public void setImplementation(BusinessRuleTaskImplementation implementation) {
		this.implementation = implementation;
	}

}
