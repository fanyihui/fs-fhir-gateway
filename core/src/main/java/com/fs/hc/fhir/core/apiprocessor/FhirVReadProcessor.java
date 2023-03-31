package com.fs.hc.fhir.core.apiprocessor;

import com.fs.hc.fhir.core.exception.InternalSystemException;
import com.fs.hc.fhir.core.model.FhirConstant;
import com.fs.hc.fhir.core.model.SupportedFhirVersionEnum;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirVReadProcessor {
    @Autowired
    FhirAPIUtil fhirAPIUtil;
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;

    public void vReadProcessor(Exchange exchange) throws InternalSystemException {
        String resourceType = fhirAPIUtil.getResourceTypeFromUri(exchange.getIn().getHeader(Exchange.HTTP_URI, String.class));
        SupportedFhirVersionEnum fhirVersionEnum = exchange.getIn().getHeader(FhirConstant.FHIR_VERSION_HEADER, SupportedFhirVersionEnum.class);

        if (resourceType != null){
            fhirVersionStrategy.getFhirResourceBuilder(fhirVersionEnum).validateFhirResourceType(resourceType);
            //Put the resourceType to the header, so other routes or process can use this header to determine which FHIR Resource is processing
            exchange.getIn().setHeader(FhirConstant.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }

        String id = exchange.getIn().getHeader("id", String.class);
        if (id == null){
            throw new InternalSystemException("The incoming request is not a read API. It doesn't include id value in the url. The uri should be follow the format http://base/[resourceType]/[id]");
        }

        String vid = exchange.getIn().getHeader("vid", String.class);
        if (vid == null){
            throw new InternalSystemException("版本号为空，API遵循： http://base/[resourceType]/[id]/_history/[vid]");
        }
    }
}
