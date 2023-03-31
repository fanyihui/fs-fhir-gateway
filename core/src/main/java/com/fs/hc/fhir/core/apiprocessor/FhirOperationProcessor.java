package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.exception.InternalSystemException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import org.apache.camel.Exchange;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirOperationProcessor {
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;
    @Autowired
    FhirAPIUtil fhirAPIUtil;

    public void operationProcess(Exchange exchange) throws InternalSystemException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String resourceType = fhirAPIUtil.getResourceTypeFromUri(uri);
        SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

        if (resourceType != null){
            try{
                fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).validateFhirResourceType(resourceType);
            } catch (FHIRException ise){
                throw ise;
            }
            //Put the resourceType to the header, so other routes or process can use this header to determine which FHIR Resource is processing
            exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }

        //
    }
}
