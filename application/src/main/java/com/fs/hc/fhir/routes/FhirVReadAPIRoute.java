package com.fs.hc.fhir.routes;


import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirVReadProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirVReadAPIRoute extends FsBaseRoute{
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirVReadProcessor fhirVReadProcessor;

    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:vReadResourceById").routeId("vread-resource-by-id").
                bean(fhirAPIProcessor, "process").
                bean(fhirVReadProcessor, "vReadProcessor").
                bean(businessService, "readResourceHistoryById('${header.resourceType}', '${header.id}', '${header.vid}')").
                bean(fhirSuccessResponseProcessor, "readSuccessfully");
    }
}
