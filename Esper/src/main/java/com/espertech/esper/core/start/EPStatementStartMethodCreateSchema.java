/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.start;

import com.espertech.esper.client.ConfigurationVariantStream;
import com.espertech.esper.client.EventType;
import com.espertech.esper.core.service.EPServicesContext;
import com.espertech.esper.core.service.StatementContext;
import com.espertech.esper.epl.expression.ExprValidationException;
import com.espertech.esper.epl.spec.CreateSchemaDesc;
import com.espertech.esper.epl.spec.StatementSpecCompiled;
import com.espertech.esper.event.EventTypeUtility;
import com.espertech.esper.view.ViewProcessingException;
import com.espertech.esper.view.Viewable;
import com.espertech.esper.view.ViewableDefaultImpl;

/**
 * Starts and provides the stop method for EPL statements.
 */
public class EPStatementStartMethodCreateSchema extends EPStatementStartMethodBase
{
    public EPStatementStartMethodCreateSchema(StatementSpecCompiled statementSpec) {
        super(statementSpec);
    }

    public EPStatementStartResult startInternal(final EPServicesContext services, final StatementContext statementContext, boolean isNewStatement, boolean isRecoveringStatement, boolean isRecoveringResilient) throws ExprValidationException, ViewProcessingException {
        final CreateSchemaDesc spec = statementSpec.getCreateSchemaDesc();

        EventType eventType = handleCreateSchema(services, statementContext, spec);

        // enter a reference
        services.getStatementEventTypeRefService().addReferences(statementContext.getStatementName(), new String[] {spec.getSchemaName()});

        final EventType allocatedEventType = eventType;
        EPStatementStopMethod stopMethod = new EPStatementStopMethod() {
            public void stop()
            {
                services.getStatementEventTypeRefService().removeReferencesStatement(statementContext.getStatementName());
                if (services.getStatementEventTypeRefService().getStatementNamesForType(spec.getSchemaName()).isEmpty()) {
                    services.getEventAdapterService().removeType(allocatedEventType.getName());
                    services.getFilterService().removeType(allocatedEventType);
                }
            }
        };
        Viewable viewable = new ViewableDefaultImpl(eventType);
        return new EPStatementStartResult(viewable, stopMethod, null);
    }

    private EventType handleCreateSchema(EPServicesContext services, StatementContext statementContext, CreateSchemaDesc spec)
        throws ExprValidationException {

        EventType eventType;

        try {
            if (spec.getAssignedType() != CreateSchemaDesc.AssignedType.VARIANT) {
                eventType = EventTypeUtility.createNonVariantType(false, spec, statementContext.getAnnotations(), services.getConfigSnapshot(), services.getEventAdapterService(), services.getEngineImportService());
            }
            else {
                if (spec.getCopyFrom() != null && !spec.getCopyFrom().isEmpty()) {
                    throw new ExprValidationException("Copy-from types are not allowed with variant types");
                }

                boolean isAny = false;
                ConfigurationVariantStream config = new ConfigurationVariantStream();
                for (String typeName : spec.getTypes()) {
                    if (typeName.trim().equals("*")) {
                        isAny = true;
                        break;
                    }
                    config.addEventTypeName(typeName);
                }
                if (!isAny) {
                    config.setTypeVariance(ConfigurationVariantStream.TypeVariance.PREDEFINED);
                }
                else {
                    config.setTypeVariance(ConfigurationVariantStream.TypeVariance.ANY);
                }
                services.getValueAddEventService().addVariantStream(spec.getSchemaName(), config, services.getEventAdapterService(), services.getEventTypeIdGenerator());
                eventType = services.getValueAddEventService().getValueAddProcessor(spec.getSchemaName()).getValueAddEventType();
            }
        }
        catch (RuntimeException ex) {
            throw new ExprValidationException(ex.getMessage(), ex);
        }

        return eventType;
    }
}
