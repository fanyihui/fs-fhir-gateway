package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirReadProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirReadAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirReadProcessor fhirReadProcessor;

    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:readResourceById").routeId("read-resource-by-id").
                bean(fhirAPIProcessor, "process").
                bean(fhirReadProcessor, "readProcessor").
                bean(businessService, "readResourceById('${header.resourceType}', '${header.id}')").
                bean(fhirSuccessResponseProcessor, "readSuccessfully");
    }
}
