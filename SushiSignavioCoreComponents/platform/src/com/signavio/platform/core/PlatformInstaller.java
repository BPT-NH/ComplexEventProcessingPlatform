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
 * The installer is created and called whenever a PlatformInstance is started for the first time.
 * @author Bjoern Wagner
 *
 */
public interface PlatformInstaller {

	/**
	 * This method is called by the Platform instance during the first bootstrapping
	 * @param parameters
	 */
	public void install(PlatformInstance platformInstance);
}
