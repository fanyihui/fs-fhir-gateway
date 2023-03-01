package com.fs.hc.fhir.core.exceptionhandler;

import com.fs.hc.fhir.gateway.exception.FhirUnknowResourceException;
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
public class FhirUnknowResourceExceptionHandler implements Processor {
    @Autowired
    FhirUtil fhirUtil;

    @Override
    public void process(Exchange exchange) throws Exception {
        FhirUnknowResourceException fhirUnknowResourceException = (FhirUnknowResourceException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, "404");


        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        exchange.getOut().setHeader(Exchange.CONTENT_TYPE, mimeType);

        IBaseOperationOutcome iBaseOperationOutcome = fhirUtil.createOperationOutcome(
                supportedFhirVersionEnum,
                "The "+
                        exchange.getIn().getHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER) + " with id "+
                        exchange.getIn().getHeader("id").toString()+" cannot be found!",
                FhirIssueType.NOTFOUND);

        exchange.getOut().setBody(fhirUtil.encodeResource(
                mimeType,
                iBaseOperationOutcome));
    }
}
