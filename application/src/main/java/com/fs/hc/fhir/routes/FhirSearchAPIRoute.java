package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSearchParametersProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirSearchAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirSearchParametersProcessor fhirSearchParametersProcessor;
    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;


    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:searchResources").routeId("search-resources").
                bean(fhirAPIProcessor, "process").
                bean(fhirSearchParametersProcessor, "processParameters").
                bean(businessService, "searchResources('${header.resourceType}', '${body}')").
                bean(fhirSuccessResponseProcessor, "searchSuccessfully");
    }
}
