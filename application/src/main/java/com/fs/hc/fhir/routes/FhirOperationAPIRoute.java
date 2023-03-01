package com.fs.hc.fhir.routes;


import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirOperationProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirOperationAPIRoute extends FsBaseRoute{

    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirOperationProcessor fhirOperationProcessor;
    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:operation").routeId("operation").
                bean(fhirAPIProcessor, "process").
                bean(fhirOperationProcessor, "operationProcess");
    }
}
