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
package com.signavio.warehouse.business.util.jpdl4;

import org.json.JSONException;
import org.json.JSONObject;

public class EndCancelEvent extends EndEvent{

	public EndCancelEvent(JSONObject endEvent) {
		super(endEvent);
	}
	
	public EndCancelEvent(org.w3c.dom.Node endEvent) {
		super(endEvent);
	}
	
	@Override
	public String toJpdl() throws InvalidModelException {
		String id = "end-cancel";
		return writeJpdlAttributes(id).toString();

	}
	
	@Override
	public JSONObject toJson() throws JSONException {
		String id = "EndCancelEvent";
		
		return writeJsonAttributes(id);
	}

}
