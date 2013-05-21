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

package com.espertech.esper.dataflow.util;

public class LogicalChannelProducingPortCompiled {
    private final int producingOpNum;
    private final String producingOpPrettyPrint;
    private final String streamName;
    private final int streamNumber;
    private final GraphTypeDesc graphTypeDesc;
    private final boolean hasPunctuation;

    public LogicalChannelProducingPortCompiled(int producingOpNum, String producingOpPrettyPrint, String streamName, int streamNumber, GraphTypeDesc graphTypeDesc, boolean hasPunctuation) {
        this.producingOpNum = producingOpNum;
        this.producingOpPrettyPrint = producingOpPrettyPrint;
        this.streamName = streamName;
        this.streamNumber = streamNumber;
        this.graphTypeDesc = graphTypeDesc;
        this.hasPunctuation = hasPunctuation;
    }

    public String getProducingOpPrettyPrint() {
        return producingOpPrettyPrint;
    }

    public int getProducingOpNum() {
        return producingOpNum;
    }

    public String getStreamName() {
        return streamName;
    }

    public int getStreamNumber() {
        return streamNumber;
    }

    public boolean isHasPunctuation() {
        return hasPunctuation;
    }

    public GraphTypeDesc getGraphTypeDesc() {
        return graphTypeDesc;
    }

    public String toString() {
        return "LogicalChannelProducingPort{" +
                "op=" + producingOpPrettyPrint + '\'' +
                ", streamName='" + streamName + '\'' +
                ", portNumber=" + streamNumber +
                ", hasPunctuation=" + hasPunctuation +
                '}';
    }
}
