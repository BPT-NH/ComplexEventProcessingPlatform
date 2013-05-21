/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.pattern;

/**
 * Followed-by operator types in the evaluation tree representing any event expressions.
 */
public enum EvalFollowedByNodeOpType {
    NOMAX_PLAIN,
    MAX_PLAIN,
    NOMAX_POOL,
    MAX_POOL
}
