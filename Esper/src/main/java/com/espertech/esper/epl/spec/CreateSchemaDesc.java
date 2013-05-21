/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.spec;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.soda.CreateSchemaClauseTypeDef;
import com.espertech.esper.util.MetaDefItem;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Specification for creating an event type/schema.
 */
public class CreateSchemaDesc implements MetaDefItem, Serializable
{
    private static final long serialVersionUID = 8400789369907593190L;
    
    private final String schemaName;
    private final Set<String> types;
    private final List<ColumnDesc> columns;
    private final Set<String> inherits;
    private final AssignedType assignedType;
    private final String startTimestampProperty;
    private final String endTimestampProperty;
    private final Set<String> copyFrom;

    /**
     * Ctor.
     * @param schemaName name
     * @param types event type name(s)
     * @param columns column definition
     * @param inherits supertypes
     * @param assignedType any type assignment such as Map, Object-array or variant or none-specified
     */
    public CreateSchemaDesc(String schemaName, Set<String> types, List<ColumnDesc> columns, Set<String> inherits, AssignedType assignedType, String startTimestampProperty, String endTimestampProperty, Set<String> copyFrom) {
        this.schemaName = schemaName;
        this.types = types;
        this.columns = columns;
        this.inherits = inherits;
        this.assignedType = assignedType;
        this.startTimestampProperty = startTimestampProperty;
        this.endTimestampProperty = endTimestampProperty;
        this.copyFrom = copyFrom;
    }

    /**
     * Returns schema name.
     * @return schema name
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * Returns column definitions.
     * @return column defs
     */
    public List<ColumnDesc> getColumns()
    {
        return columns;
    }

    /**
     * Returns supertypes.
     * @return supertypes
     */
    public Set<String> getInherits()
    {
        return inherits;
    }

    /**
     * Returns type name(s).
     * @return types
     */
    public Set<String> getTypes()
    {
        return types;
    }

    public AssignedType getAssignedType() {
        return assignedType;
    }

    public String getStartTimestampProperty() {
        return startTimestampProperty;
    }

    public String getEndTimestampProperty() {
        return endTimestampProperty;
    }

    public Set<String> getCopyFrom() {
        return copyFrom;
    }

    public static enum AssignedType {
        VARIANT,
        MAP,
        OBJECTARRAY,
        NONE;

        public CreateSchemaClauseTypeDef mapToSoda() {
            if (this == VARIANT) {
                return CreateSchemaClauseTypeDef.VARIANT;
            }
            else if (this == MAP) {
                return CreateSchemaClauseTypeDef.MAP;
            }
            else if (this == OBJECTARRAY) {
                return CreateSchemaClauseTypeDef.OBJECTARRAY;
            }
            else {
                return CreateSchemaClauseTypeDef.NONE;
            }
        }

        public static AssignedType parseKeyword(String keywordNodeText) {
            if (keywordNodeText.toLowerCase().equals("variant")) {
                return VARIANT;
            }
            if (keywordNodeText.toLowerCase().equals("map")) {
                return MAP;
            }
            if (keywordNodeText.toLowerCase().equals("objectarray")) {
                return OBJECTARRAY;
            }
            throw new EPException("Expected 'variant', 'map' or 'objectarray' keyword after create-schema clause but encountered '" + keywordNodeText + "'");
        }

        public static AssignedType mapFrom(CreateSchemaClauseTypeDef typeDefinition) {
            if (typeDefinition == null || CreateSchemaClauseTypeDef.NONE == typeDefinition) {
                return NONE;
            }
            if (CreateSchemaClauseTypeDef.MAP == typeDefinition) {
                return MAP;
            }
            if (CreateSchemaClauseTypeDef.OBJECTARRAY == typeDefinition) {
                return OBJECTARRAY;
            }
            return VARIANT;
        }
    }
}
