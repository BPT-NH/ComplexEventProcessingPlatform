/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.client.soda;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Fire-and-forget (on-demand) update DML.
 */
public class FireAndForgetUpdate implements FireAndForgetClause
{
    private static final long serialVersionUID = 1335566236342281539L;
    private List<AssignmentPair> assignments = new ArrayList<AssignmentPair>();

    /**
     * Ctor.
     */
    public FireAndForgetUpdate() {
    }

    /**
     * Returns the set-assignments.
     * @return assignments
     */
    public List<AssignmentPair> getAssignments() {
        return assignments;
    }

    /**
     * Add an assignment
     * @param assignment to add
     * @return assignment
     */
    public List<AssignmentPair> addAssignment(AssignmentPair assignment) {
        assignments.add(assignment);
        return assignments;
    }

    /**
     * Sets the assignments.
     * @param assignments to set
     */
    public void setAssignments(List<AssignmentPair> assignments) {
        this.assignments = assignments;
    }

    public void toEPLBeforeFrom(StringWriter writer) {
        writer.append("update ");
    }

    public void toEPLAfterFrom(StringWriter writer) {
        writer.append(" ");
        UpdateClause.renderEPLAssignments(writer, assignments);
    }
}
