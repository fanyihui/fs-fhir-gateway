package com.fs.hc.fhir.core.exceptionhandler;

import com.fs.hc.fhir.core.exception.FhirAPIException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import com.fs.hc.fhir.core.resprocessor.AbstractFhirResourceBuilder;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirOperationExceptionHandler implements Processor {
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirAPIException fhirAPIException = (FhirAPIException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, fhirAPIException.getHttpStatusCode());

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);

        if (mimeType == null){
            mimeType = "application/fhir+json";
        }

        AbstractFhirResourceBuilder fhirResourceBuilder = fhirVersionStrategy.getFhirResourceBuilder(supportedFhirVersionEnum);

        IBaseOperationOutcome iBaseOperationOutcome = fhirResourceBuilder.createOperationOutcomeForException(fhirAPIException.getMessage(), fhirAPIException.getFhirIssueType());
        exchange.getMessage().setBody(fhirResourceBuilder.encodeResource(
                mimeType,
                iBaseOperationOutcome));

        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
    }
}
