package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.FsFhirGatewayProperties;
import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirCreateProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR4BBuilder;
import com.fs.hc.fhir.core.resprocessor.FhirVersionStrategy;
import com.fs.hc.fhir.core.resprocessor.ResourceIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirCreateAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirCreateProcessor fhirCreateProcessor;
    @Autowired
    FhirVersionStrategy fhirVersionStrategy;
    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;

    @Autowired
    ResourceIDGenerator resourceIDGenerator;
    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:createNewResource").id("create-new-resource-route").
                bean(fhirAPIProcessor, "process").
                bean(fhirCreateProcessor, "createProcess").
                choice().when().simple(""+!fsFhirGatewayProperties.isUsingClientGeneratedId()).
                bean(resourceIDGenerator, "generateResourceId").end().
                bean(fhirVersionStrategy, "validateFhirResource(${header.FHIR_VERSION}, ${body})").
                bean(businessService, "createResource(${body})").
                bean(fhirSuccessResponseProcessor, "createSuccessful");
    }
}
