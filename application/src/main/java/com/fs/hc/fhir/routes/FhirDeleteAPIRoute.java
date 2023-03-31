package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.FsFhirGatewayProperties;
import com.fs.hc.fhir.core.apiprocessor.FhirAPIProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirDeleteProcessor;
import com.fs.hc.fhir.core.apiprocessor.FhirSuccessResponseProcessor;
import com.fs.hc.fhir.core.bs.IBusinessService;
import com.fs.hc.fhir.core.model.FhirConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirDeleteAPIRoute extends FsBaseRoute {
    @Autowired
    FhirAPIProcessor fhirAPIProcessor;
    @Autowired
    FhirDeleteProcessor fhirDeleteProcessor;
    @Autowired
    IBusinessService businessService;
    @Autowired
    FhirSuccessResponseProcessor fhirSuccessResponseProcessor;
    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;

    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:deleteResourceById").routeId("delete-resource-by-id").
                bean(fhirAPIProcessor, "process").setHeader(FhirConstant.INCLUDEPAYLOADFORDELETE, constant(fsFhirGatewayProperties.isResponsePayload4Delete())).
                bean(fhirDeleteProcessor, "deleteProcess").
                bean(businessService, "deleteResource('${header.resourceType}', '${header.id}')").
                bean(fhirSuccessResponseProcessor, "deleteSuccessfully");
    }
}
