package com.fs.hc.fhir.core.exceptionhandler;

import com.fs.hc.fhir.core.exception.FhirReadDeletedResourceException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirIssueType;
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
public class FhirReadDeletedResourceExceptionHandler implements Processor {
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirReadDeletedResourceException fhirReadDeletedResourceException = (FhirReadDeletedResourceException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "401");

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        AbstractFhirResourceBuilder fhirResourceBuilder = fhirVersionStrategy.getFhirResourceBuilder(supportedFhirVersionEnum);
        IBaseOperationOutcome iBaseOperationOutcome = fhirResourceBuilder.createOperationOutcomeForInfo(
                "The "+
                        exchange.getIn().getHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER) + " with id "+
                        exchange.getIn().getHeader("id").toString()+" has been deleted",
                FhirIssueType.DELETED);

        exchange.getMessage().setBody(fhirResourceBuilder.encodeResource(
                mimeType,
                iBaseOperationOutcome));

        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
    }
}
