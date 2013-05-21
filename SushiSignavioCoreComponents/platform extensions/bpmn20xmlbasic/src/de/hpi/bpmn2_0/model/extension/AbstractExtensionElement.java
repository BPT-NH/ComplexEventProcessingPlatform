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
package de.hpi.bpmn2_0.model.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import de.hpi.bpmn2_0.model.extension.signavio.SignavioLabel;
import de.hpi.bpmn2_0.model.extension.signavio.SignavioMessageName;
import de.hpi.bpmn2_0.model.extension.signavio.SignavioMetaData;
import de.hpi.bpmn2_0.model.extension.signavio.SignavioType;
import sushi.bpmn2_0.model.extension.monitoring.MonitoringPoints;
import sushi.bpmn2_0.model.extension.monitoring.Transition;


/**
 * Abstract BPMN 2.0 extension element
 * 
 * @author Sven Wagner-Boysen
 *
 */
@XmlSeeAlso({
	SignavioMetaData.class,
	SignavioType.class,
	SignavioLabel.class,
	SignavioMessageName.class,
	MonitoringPoints.class,
	Transition.class
})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractExtensionElement {
	
}
