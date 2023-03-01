package com.fs.hc.fhir.core.exceptionhandler;


import ca.uhn.fhir.context.FhirContext;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.springframework.beans.factory.annotation.Autowired;

public class FhirExceptionHandler implements Processor {
    @Autowired
    FhirUtil fhirUtil;

    @Override
    public void process(Exchange exchange) throws Exception {
        FHIRException fhirException = (FHIRException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        //exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 403);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 403);
        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);

        if (mimeType == null){
            mimeType = "application/fhir+json";
        }

        IBaseOperationOutcome iBaseOperationOutcome = fhirUtil.createOperationOutcome(supportedFhirVersionEnum, fhirException.getMessage(), FhirIssueType.EXCEPTION);
        exchange.getOut().setBody(fhirUtil.encodeResource(
                mimeType,
                iBaseOperationOutcome));

        exchange.getOut().setHeader(Exchange.CONTENT_TYPE, mimeType);
    }
}
