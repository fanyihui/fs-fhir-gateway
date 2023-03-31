package com.fs.hc.fhir.core.exceptionhandler;

import ca.uhn.fhir.parser.DataFormatException;
import com.fs.hc.fhir.core.exception.FhirResourceValidationException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.FhirIssueType;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import com.fs.hc.fhir.core.resprocessor.AbstractFhirResourceBuilder;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FhirValidationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(FhirValidationExceptionHandler.class);

    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    public void handleFhirDataFormatException(Exchange exchange){
        DataFormatException dataFormatException = null;
        //Object object = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, DataFormatException.class);
        dataFormatException = throwable instanceof DataFormatException ? ((DataFormatException) throwable) : null;

        if (dataFormatException == null){
            logger.error("The Exception is null or not instance of DataFormatException");
            return;
        }

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);
        AbstractFhirResourceBuilder fhirResourceBuilder = fhirVersionStrategy.getFhirResourceBuilder(supportedFhirVersionEnum);

        try {
            IBaseOperationOutcome operationOutcome = fhirResourceBuilder.createOperationOutcomeForException(dataFormatException.getMessage(), FhirIssueType.NOTSUPPORTED);

            String body = fhirResourceBuilder.encodeResource(mimeType, operationOutcome);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
            exchange.getMessage().setBody(body);
        } catch (FHIRException fe){
            logger.error("", fe);
        }
    }

    public void handleFhirProfileValidationException(Exchange exchange){
        String mimeType = exchange.getIn().getHeader(FhirConstant.FHIR_MIMETYPE_HEADER, String.class);

        SupportedFhirVersionEnum supportedFhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);
        AbstractFhirResourceBuilder fhirResourceBuilder = fhirVersionStrategy.getFhirResourceBuilder(supportedFhirVersionEnum);

        Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CamelExecutionException.class).getCause();
        FhirResourceValidationException fhirResourceValidationException =
                throwable instanceof FhirResourceValidationException ? ((FhirResourceValidationException) throwable) : null;

        IBaseOperationOutcome operationOutcome = fhirResourceValidationException.getOperationOutcome();

        String body = fhirResourceBuilder.encodeResource(mimeType, operationOutcome);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, mimeType);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "422");
        exchange.getMessage().setBody(body);
    }

}
