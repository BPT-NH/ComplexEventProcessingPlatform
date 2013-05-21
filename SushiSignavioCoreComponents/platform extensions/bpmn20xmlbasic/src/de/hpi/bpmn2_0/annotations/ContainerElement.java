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
package de.hpi.bpmn2_0.annotations;

import java.util.List;

import de.hpi.bpmn2_0.model.FlowElement;
import de.hpi.bpmn2_0.model.bpmndi.di.DiagramElement;
import de.hpi.bpmn2_0.model.connector.Edge;

/**
 * @author Sven Wagner-Boysen
 *
 * Annotaion to identify container elements such as subprocesses and 
 * sub choreographies.
 * 
 */
public interface ContainerElement {
	
	public List<FlowElement> getFlowElement();
	
	public List<DiagramElement> _getDiagramElements();
	
	public List<Edge> getChildEdges();
}
