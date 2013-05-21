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
package de.hpi.bpmn2_0.factory;

import org.oryxeditor.server.diagram.Bounds;
import org.oryxeditor.server.diagram.generic.GenericShape;

import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.model.BaseElement;
import de.hpi.bpmn2_0.model.bpmndi.BPMNShape;
import de.hpi.bpmn2_0.model.extension.ExtensionElements;
import de.hpi.bpmn2_0.model.extension.signavio.SignavioMetaData;
import de.hpi.bpmn2_0.util.DiagramHelper;
import sushi.bpmn2_0.model.extension.monitoring.Transition;
import sushi.bpmn2_0.model.extension.monitoring.TransitionType;

/**
 * Abstract factory to handle all types {@link BPMNShape} objects.
 * 
 * @author Sven Wagner-Boysen
 *
 */
public abstract class AbstractShapeFactory extends AbstractBpmnFactory {

	/* (non-Javadoc)
	 * @see de.hpi.bpmn2_0.factory.common.AbstractBpmnFactory#createBpmnElement(org.oryxeditor.server.diagram.Shape, de.hpi.bpmn2_0.factory.BPMNElement)
	 */
	// @Override
	public BPMNElement createBpmnElement(GenericShape shape, BPMNElement parent)
			throws BpmnConverterException {
		
		BPMNShape diaElement = this.createDiagramElement(shape);
		BaseElement processElement = this.createProcessElement(shape);
		diaElement.setBpmnElement(processElement);
		
		super.setLabelPositionInfo(shape, processElement);
		
		setBgColor(shape, processElement);
		setMonitoringPoints(shape, processElement);
		
		BPMNElement bpmnElement = new BPMNElement(diaElement, processElement, shape.getResourceId());

		// handle external extension elements like from Activiti
		try {
			super.reinsertExternalExtensionElements(shape, bpmnElement);
		} catch (Exception e) {
			
		} 
		
		return bpmnElement;
	}

	/* (non-Javadoc)
	 * @see de.hpi.bpmn2_0.factory.common.AbstractBpmnFactory#createDiagramElement(org.oryxeditor.server.diagram.Shape)
	 */
	// @Override
	protected BPMNShape createDiagramElement(GenericShape shape) {
		BPMNShape bpmnShape = new BPMNShape();
		super.setVisualAttributes(bpmnShape, shape);
		
		/* Bounds */
		bpmnShape.setBounds(createBounds(shape));
		
		return bpmnShape;
	}
	
	/* Helper methods */
	
	/**
	 * Generates the BPMN Bounds out of a Shape.
	 */
	private de.hpi.bpmn2_0.model.bpmndi.dc.Bounds createBounds(GenericShape shape) {
		Bounds absBounds = shape.getAbsoluteBounds();
		
		de.hpi.bpmn2_0.model.bpmndi.dc.Bounds bpmnBounds = new de.hpi.bpmn2_0.model.bpmndi.dc.Bounds();
		bpmnBounds.setX(absBounds.getUpperLeft().getX());
		bpmnBounds.setY(absBounds.getUpperLeft().getY());
		bpmnBounds.setHeight(shape.getHeight());
		bpmnBounds.setWidth(shape.getWidth());
		
		return bpmnBounds;
	}
	
	/**
	 * Sets the bgcolor property as a {@link SignavioMetaData} extension
	 * element.
	 * 
	 * @param node
	 * @param element
	 */
	private void setBgColor(GenericShape node, BaseElement element) {
		String bgColor = node.getProperty("bgcolor");
		if(bgColor != null) {
			ExtensionElements extElements = element.getOrCreateExtensionElements();
			extElements.add(new SignavioMetaData("bgcolor", bgColor));
		}
	}
	
	/**
	 * Sets the monitoring points properties as a {@link Sushi} extension
	 * elements. 
	 * 
	 * @param node
	 * @param element
	 */
	private void setMonitoringPoints(GenericShape node, BaseElement element) {
		String enableRegExpr = node.getProperty("enable");
		String beginRegExpr = node.getProperty("begin");
		String terminateRegExpr = node.getProperty("terminate");
		String skipRegExpr = node.getProperty("skip");
		ExtensionElements extElements = element.getOrCreateExtensionElements();
		if (enableRegExpr != null)
			if (!enableRegExpr.trim().isEmpty()) {
			extElements.add(new Transition(TransitionType.enable, enableRegExpr));
		}
		if (beginRegExpr != null)
			if (!beginRegExpr.trim().isEmpty()) {
			extElements.add(new Transition(TransitionType.begin, beginRegExpr));
		}
		if(terminateRegExpr != null)
			if(!terminateRegExpr.trim().isEmpty()) {
			extElements.add(new Transition(TransitionType.terminate, terminateRegExpr));
		}
		if(skipRegExpr != null)
			if(!skipRegExpr.trim().isEmpty()) {
			extElements.add(new Transition(TransitionType.skip, skipRegExpr));
		}
	}
	

}
