package com.fs.hc.fhir.routes;


import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirResourceValidation;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirUpdateProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirUpdateAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirResourceValidation fhirResourceValidation;
    @Autowired
    FhirUpdateProcessor fhirUpdateProcessor;
    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:updateResource").routeId("update-resource-by-id").
                bean(fhirAPIProcessor, "process").
                bean(fhirUpdateProcessor,"updateProcess").
                bean(fhirResourceValidation, "validateResource").
                to("bean:businessService?updateResource('${body}', '${header.id}')").
                bean(fhirSuccessResponseProcessor, "updateSuccessfully");
    }
}
