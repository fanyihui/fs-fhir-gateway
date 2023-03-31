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
public class FhirDeleteProcessor {
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    public void deleteProcess(Exchange exchange) throws InternalSystemException {
        String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        String basePath = exchange.getIn().getHeader(Exchange.HTTP_BASE_URI, String.class);
        String uriElements[] = uri.split("/");
        String resourceType = uriElements[1];

        SupportedFhirVersionEnum fhirVersion = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

        if (resourceType != null){
            try{
                fhirVersionStrategy.getFhirResourceBuilder(fhirVersion).validateFhirResourceType(resourceType);
            } catch (FHIRException fhirException){
                throw fhirException;
            }
            //Put the resourceType to the header, so other routes or process can use this header to determine which FHIR Resource is processing
            exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }

        String id = exchange.getIn().getHeader("id", String.class);
        if (id == null){
            throw new InternalSystemException("The incoming request is not a delete API. It doesn't include id value in the url. The uri should be follwo the format http://base/[resoureType]/[id]");
        }
    }
}
