package com.fs.hc.fhir.core.exceptionhandler;

import com.fs.hc.fhir.gateway.exception.FhirSearchException;
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
public class FhirSearchExceptionHandler implements Processor {
    @Autowired
    FhirUtil fhirUtil;

    @Override
    public void process(Exchange exchange) throws Exception {
        //Handle FhirSearchException
        FhirSearchException fhirSearchException = (FhirSearchException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        int code = fhirSearchException.getErrorCode();

        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, code);

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        IBaseOperationOutcome iBaseOperationOutcome = fhirUtil.createOperationOutcome(
                supportedFhirVersionEnum,
                fhirSearchException.getMessage(),
                FhirIssueType.INVALID);

        exchange.getOut().setBody(fhirUtil.encodeResource(
                mimeType,
                iBaseOperationOutcome));

        exchange.getOut().setHeader(Exchange.CONTENT_TYPE, mimeType);

    }
}
