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
package com.signavio.platform.core;

/**
 * Interface for all Platform directories 
 * @author Bjoern Wagner
 *
 */
public interface Directory {

	/**
	 * This method is called during the bootstrapping of the platform and can be used to
	 * initialize the directory.
	 */
	public void start();
	
	/**
	 * This method is called during the shutdown of the platform and can be used to 
	 * free resources.
	 */
	public void stop();
}
