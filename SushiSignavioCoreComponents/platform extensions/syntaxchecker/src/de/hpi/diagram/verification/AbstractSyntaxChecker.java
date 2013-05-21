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
package de.hpi.diagram.verification;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

abstract public class AbstractSyntaxChecker implements SyntaxChecker {
	protected Map<String,String> errors;
	
	public AbstractSyntaxChecker(){
		this.errors = new HashMap<String,String>();
	}

	abstract public boolean checkSyntax();

	public Map<String, String> getErrors() {
		return errors;
	}
	
	public void clearErrors(){
		errors.clear();
	}
	
	public boolean errorsFound(){
		return errors.size() > 0;
	}

	public JSONObject getErrorsAsJson() {
		JSONObject jsonObject = new JSONObject();
		
		for (Entry<String,String> error: this.getErrors().entrySet()) {
			try {
				jsonObject.put(error.getKey(), error.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return jsonObject;
	}
}
