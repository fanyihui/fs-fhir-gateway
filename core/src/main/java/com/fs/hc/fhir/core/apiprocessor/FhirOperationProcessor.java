package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.exception.InternalSystemException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirResourceBuilderStrategy;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirOperationProcessor {
    @Autowired
    FhirResourceBuilderStrategy fhirResourceBuilderStrategy;

    public void operationProcess(Exchange exchange) throws InternalSystemException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String uriElements[] = uri.split("/");
        String resourceType = uriElements[1];

        if (resourceType != null){
            try{
                fhirUtil.validateFhirResourceType(exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class), resourceType);
            } catch (InternalSystemException ise){
                throw ise;
            }
            //Put the resourceType to the header, so other routes or process can use this header to determine which FHIR Resource is processing
            exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }

        //
    }
}
