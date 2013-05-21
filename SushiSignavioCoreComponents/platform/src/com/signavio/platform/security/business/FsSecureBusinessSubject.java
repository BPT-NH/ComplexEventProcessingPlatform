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
package com.signavio.platform.security.business;

/**
 * Implementation of a SecureBusinessSubject for filesystem-accessing Oryx
 * 
 * @author Stefan Krumnow
 *
 */
public abstract class FsSecureBusinessSubject extends FsSecureBusinessObject {
	
	@Override
	public String toString() {
		if (this.getTenant() != null) {
			return "SBSubject: " + this.getClass().getName() + " (ID= " + this.getId() 
				+ ", TenantName=" + this.getTenant().getName() + ") ";
		} else {
			return "SBSubject: " + this.getClass().getName() + " (ID= " + this.getId() 
			+ ",  NO Tenant) ";			
		}
	}

}
