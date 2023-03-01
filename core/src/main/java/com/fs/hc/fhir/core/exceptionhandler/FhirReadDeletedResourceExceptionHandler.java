package com.fs.hc.fhir.core.exceptionhandler;

import com.fs.hc.fhir.gateway.exception.FhirReadDeletedResourceException;
import com.fs.hc.fhir.gateway.fhirprocessor.FhirConstant;
import com.fs.hc.fhir.gateway.model.FhirIssueType;
import com.fs.hc.fhir.gateway.util.FhirUtil;
import com.fs.hc.fhir.gateway.util.SupportedFhirVersionEnum;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirReadDeletedResourceExceptionHandler implements Processor {
    @Autowired
    FhirUtil fhirUtil;

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirReadDeletedResourceException fhirReadDeletedResourceException = (FhirReadDeletedResourceException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, "401");

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        IBaseOperationOutcome iBaseOperationOutcome = fhirUtil.createOperationOutcome(
                supportedFhirVersionEnum,
                "The "+
                        exchange.getIn().getHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER) + " with id "+
                        exchange.getIn().getHeader("id").toString()+" has been deleted",
                FhirIssueType.DELETED);

        exchange.getOut().setBody(fhirUtil.encodeResource(
                mimeType,
                iBaseOperationOutcome));

        exchange.getOut().setHeader(Exchange.CONTENT_TYPE, mimeType);
    }
}
