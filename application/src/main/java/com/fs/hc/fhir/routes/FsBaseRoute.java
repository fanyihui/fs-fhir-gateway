package com.fs.hc.fhir.routes;

import ca.uhn.fhir.parser.DataFormatException;
import com.fs.hc.fhir.FsFhirGatewayProperties;
import com.fs.hc.fhir.core.exception.*;
import com.fs.hc.fhir.core.exceptionhandler.*;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FsBaseRoute extends RouteBuilder {
    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;
    @Autowired
    FhirOperationExceptionHandler fhirOperationExceptionHandler;
    @Autowired
    FhirValidationExceptionHandler fhirValidationExceptionHandler;
    @Autowired
    FhirReadDeletedResourceExceptionHandler fhirReadDeletedResourceExceptionHandler;
    @Autowired
    FhirUnknowResourceExceptionHandler fhirUnknowResourceExceptionHandler;
    @Autowired
    FhirSearchExceptionHandler fhirSearchExceptionHandler;


    @Override
    public void configure() throws Exception {
        onException(FhirAPIException.class).handled(true).
                process(fhirOperationExceptionHandler);

        onException(DataFormatException.class).handled(true).
                bean(fhirValidationExceptionHandler, "handleFhirDataFormatException");

        onException(FhirResourceValidationException.class).handled(true).
                bean(fhirValidationExceptionHandler, "handleFhirProfileValidationException");

        onException(FhirReadDeletedResourceException.class).handled(true).process(fhirReadDeletedResourceExceptionHandler);

        onException(FhirUnknowResourceException.class).handled(true).process(fhirUnknowResourceExceptionHandler);

        onException(FhirSearchException.class).handled(true).process(fhirSearchExceptionHandler);
    }
}
