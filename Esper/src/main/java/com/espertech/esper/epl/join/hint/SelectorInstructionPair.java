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

package com.espertech.esper.epl.join.hint;

import java.util.List;

public class SelectorInstructionPair {
    private final List<IndexHintSelector> selector;
    private final List<IndexHintInstruction> instructions;

    public SelectorInstructionPair(List<IndexHintSelector> selector, List<IndexHintInstruction> instructions) {
        this.selector = selector;
        this.instructions = instructions;
    }

    public List<IndexHintSelector> getSelector() {
        return selector;
    }

    public List<IndexHintInstruction> getInstructions() {
        return instructions;
    }
}
