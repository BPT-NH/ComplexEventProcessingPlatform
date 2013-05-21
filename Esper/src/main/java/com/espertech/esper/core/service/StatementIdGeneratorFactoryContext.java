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

package com.espertech.esper.core.service;

/**
 * Statement id generator factory context.
 */
public class StatementIdGeneratorFactoryContext
{
    private final String engineURI;

    public StatementIdGeneratorFactoryContext(String engineURI) {
        this.engineURI = engineURI;
    }

    public String getEngineURI() {
        return engineURI;
    }
}
