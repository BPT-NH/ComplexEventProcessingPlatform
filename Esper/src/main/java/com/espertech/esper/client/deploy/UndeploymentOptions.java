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

package com.espertech.esper.client.deploy;

import java.io.Serializable;

/**
 * Options for use in undeployment of a module to control the behavior of the undeploy operation.
 */
public class UndeploymentOptions implements Serializable {

    private static final long serialVersionUID = 1003208614180737243L;
    private boolean destroyStatements = true;

    /**
     * Returns indicator whether undeploy will destroy any associated statements (true by default).
     * @return flag indicating whether undeploy also destroys associated statements
     */
    public boolean isDestroyStatements() {
        return destroyStatements;
    }

    /**
     * Sets indicator whether undeploy will destroy any associated statements.
     * @param destroyStatements flag indicating whether undeploy also destroys associated statements
     */
    public void setDestroyStatements(boolean destroyStatements) {
        this.destroyStatements = destroyStatements;
    }
}
