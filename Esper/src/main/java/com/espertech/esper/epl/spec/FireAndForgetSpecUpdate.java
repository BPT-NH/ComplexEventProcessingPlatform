/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.epl.spec;

import java.util.List;

public class FireAndForgetSpecUpdate extends FireAndForgetSpec {
    private static final long serialVersionUID = -2633119130798557349L;
    private final List<OnTriggerSetAssignment> assignments;

    public FireAndForgetSpecUpdate(List<OnTriggerSetAssignment> assignments) {
        this.assignments = assignments;
    }

    public List<OnTriggerSetAssignment> getAssignments() {
        return assignments;
    }
}
