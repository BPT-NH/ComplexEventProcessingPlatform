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

import java.io.StringWriter;

import org.json.JSONObject;

public class Swimlane {
	private String name;
	private String assignee;
	private String candidateGroups;
	private String candidateUsers;
	
	// TODO Integrate Swimline in transformation.
	// Swimlane as child of process.
	// Swimlane is not parent of any Task.
	
	public Swimlane(JSONObject swimlane) {
		this.name = JsonToJpdl.getAttribute(swimlane, "name");
		this.assignee = JsonToJpdl.getAttribute(swimlane, "assignee");
		if(assignee == null) {
			this.candidateGroups = JsonToJpdl.getAttribute(swimlane, "candidate-groups");
			this.candidateUsers = JsonToJpdl.getAttribute(swimlane, "candidate-users");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(String candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public String getCandidateUsers() {
		return candidateUsers;
	}

	public void setCandidateUsers(String candidateUsers) {
		this.candidateUsers = candidateUsers;
	}
	
	public String toJpdl() throws InvalidModelException {
		StringWriter jpdl = new StringWriter();
		jpdl.write("<swimlane");
		
		jpdl.write(JsonToJpdl.transformAttribute("name", name));
		jpdl.write(JsonToJpdl.transformAttribute("assignee", assignee));
		jpdl.write(JsonToJpdl.transformAttribute("candidate-groups", candidateGroups));
		jpdl.write(JsonToJpdl.transformAttribute("candidate-users", candidateUsers));

		jpdl.write(" />\n");
		return jpdl.toString();
	}
	
}
