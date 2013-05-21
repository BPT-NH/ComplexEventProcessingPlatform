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
/**
 * 
 */
package com.signavio.platform.exceptions;

/**
 * @author Bjoern Wagner
 *
 */
public class InconsistentDataException extends LoggedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7493113581371643871L;
	
	public InconsistentDataException() {
		super();
	}

	public InconsistentDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InconsistentDataException(String message) {
		super(message);
	}

	public InconsistentDataException(Throwable cause) {
		super(cause);
	}
	
	
}
