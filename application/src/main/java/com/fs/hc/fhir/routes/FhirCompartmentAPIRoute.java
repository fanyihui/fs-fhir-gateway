package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirReadCompartmentProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirCompartmentAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirReadCompartmentProcessor fhirReadCompartmentProcessor;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;
    @Autowired
    IBusinessService businessService;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:searchCompartment").id("search-compartment").
                bean(fhirAPIProcessor, "process").
                bean(fhirReadCompartmentProcessor, "readCompartmentProcessor").
                bean(businessService, "searchCompartment('${header.compartmentType}', '${header.id}', '${header.type}', '${header.resourceParams}','${header.searchConditions}')").
                bean(fhirSuccessResponseProcessor, "readCompartmentSuccessfully");;
    }
}
