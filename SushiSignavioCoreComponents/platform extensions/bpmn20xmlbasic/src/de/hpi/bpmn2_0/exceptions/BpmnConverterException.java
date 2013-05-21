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
package de.hpi.bpmn2_0.exceptions;

/**
 * This exception class encapsulates exceptions that occurs during the 
 * transformation to or from a BPMN 2.0 model.
 * 
 * @author Sven Wagner-Boysen
 *
 */
public class BpmnConverterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8385535771020951694L;

	public BpmnConverterException() {
	}

	/**
	 * @param message
	 */
	public BpmnConverterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BpmnConverterException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BpmnConverterException(String message, Throwable cause) {
		super(message, cause);
	}

}
