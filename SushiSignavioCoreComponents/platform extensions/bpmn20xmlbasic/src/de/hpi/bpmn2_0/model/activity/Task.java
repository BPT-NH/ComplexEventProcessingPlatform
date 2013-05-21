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
package de.hpi.bpmn2_0.model.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.activity.type.BusinessRuleTask;
import de.hpi.bpmn2_0.model.activity.type.ManualTask;
import de.hpi.bpmn2_0.model.activity.type.ReceiveTask;
import de.hpi.bpmn2_0.model.activity.type.ScriptTask;
import de.hpi.bpmn2_0.model.activity.type.SendTask;
import de.hpi.bpmn2_0.model.activity.type.ServiceTask;
import de.hpi.bpmn2_0.model.activity.type.UserTask;
import de.hpi.bpmn2_0.model.callable.GlobalTask;
import de.hpi.bpmn2_0.transformation.Visitor;


/**
 * <p>Java class for tTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTask">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/bpmn20}tActivity">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTask")
@XmlSeeAlso({
    ManualTask.class,
    ServiceTask.class,
    ScriptTask.class,
    ReceiveTask.class,
    BusinessRuleTask.class,
    SendTask.class,
    UserTask.class
})
public class Task
    extends Activity
{
	
	/**
	 * Default constructor
	 */
	public Task() {
		
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param task
	 * 		The {@link Task} to copy
	 */
	public Task(Task task) {
		super(task);
	}
	
	
	
	public void acceptVisitor(Visitor v){
		v.visitTask(this);
	}
	
	public GlobalTask getAsGlobalTask() {
		GlobalTask gt = new GlobalTask();
		gt.getDocumentation().addAll(this.getDocumentation());
		gt.setExtensionElements(this.getExtensionElements());
		gt.setIoSpecification(this.getIoSpecification());
		gt.setLane(this.getLane());
		gt.setName(this.getName());
		
		return gt;
	}
}
