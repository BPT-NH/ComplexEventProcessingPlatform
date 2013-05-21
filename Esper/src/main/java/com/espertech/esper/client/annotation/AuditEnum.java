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

package com.espertech.esper.client.annotation;

import com.espertech.esper.epl.annotation.AnnotationUtil;

import java.lang.annotation.Annotation;

/**
 * Enumeration of audit values. Since audits may be a comma-separate list in a single @Audit annotation
 * they are listed as enumeration values here.
 */
public enum AuditEnum
{
    /**
     * For use with property audit.
     */
    PROPERTY("PROPERTY"),

    /**
     * For use with expression audit.
     */
    EXPRESSION("EXPRESSION"),

    /**
     * For use with expression audit.
     */
    EXPRESSION_NESTED("EXPRESSION-NESTED"),

    /**
     * For use with expression-definition audit.
     */
    EXPRDEF("EXPRDEF"),

    /**
     * For use with view audit.
     */
    VIEW("VIEW"),

    /**
     * For use with pattern audit.
     */
    PATTERN("PATTERN"),

    /**
     * For use with pattern audit.
     */
    PATTERNINSTANCES("PATTERN-INSTANCES"),

    /**
     * For use with stream-audit.
     */
    STREAM("STREAM"),

    /**
     * For use with schedule-audit.
     */
    SCHEDULE("SCHEDULE"),

    /**
     * For use with insert-into audit.
     */
    INSERT("INSERT"),

    /**
     * For use with data flow source operators.
     */
    DATAFLOW_SOURCE("DATAFLOW-SOURCE"),

    /**
     * For use with data flow (non-source and source) operators.
     */
    DATAFLOW_OP("DATAFLOW-OP"),

    /**
     * For use with data flows specifically for transitions.
     */
    DATAFLOW_TRANSITION("DATAFLOW-TRANSITION");

    private final String value;
    private final String prettyPrintText;

    private AuditEnum(String value)
    {
        this.value = value.toUpperCase();
        this.prettyPrintText = value.toLowerCase();
    }

    /**
     * Returns the constant.
     * @return constant
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Returns text used for the category of the audit log item.
     * @return category name
     */
    public String getPrettyPrintText() {
        return prettyPrintText;
    }

    /**
     * Check if the hint is present in the annotations provided.
     * @param annotations the annotations to inspect
     * @return indicator
     */
    public Audit getAudit(Annotation[] annotations)
    {
        if (annotations == null)
        {
            return null;
        }

        for (Annotation annotation : annotations)
        {
            if (!(annotation instanceof Audit))
            {
                continue;
            }

            Audit auditAnnotation = (Audit) annotation;
            String auditAnnoValue = auditAnnotation.value();
            if (auditAnnoValue.equals("*")) {
                return auditAnnotation;
            }

            boolean isListed = isListed(auditAnnoValue, value);
            if (isListed) {
                return auditAnnotation;
            }
        }
        return null;
    }

    private static boolean isListed(String list, String lookedForValue) {
        if (list == null) {
            return false;
        }

        lookedForValue = lookedForValue.trim().toUpperCase();
        list = list.trim().toUpperCase();

        if (list.toUpperCase().equals(lookedForValue)) {
            return true;
        }

        String[] items = list.split(",");
        for (String item : items) {
            String listItem = item.trim().toUpperCase();
            if (listItem.equals(lookedForValue))
            {
                return true;
            }
        }
        return false;
    }
}