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

/**
 * Sub types of this interface are used to identify a specific vendor and 
 * define the used namespace prefixes. This kind of configuration is required to
 * determine unnecessary namespace declaration used by vendor extensions. 
 * 
 * @author Sven Wagner-Boysen
 *
 */
public interface CustomExtensionSpecification {
	String[] namespacePrefixes = {};
}
