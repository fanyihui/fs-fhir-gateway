package com.fs.hc.fhir.routes;

import com.fs.hc.fhir.FsFhirGatewayProperties;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FsFhirRestRoute extends RouteBuilder {
    @Autowired
    FsFhirGatewayProperties config;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").
                contextPath("fhir").
                apiComponent("openapi").
                host(config.getHost()).
                port(config.getPort()).
                apiContextPath("api-docs").
                apiVendorExtension(true).
                dataFormatProperty("prettyPrint", "true").
                apiProperty("api.version", "1.0.0").
                apiProperty("api.title", "HL7 FHIR API").
                apiProperty("description", "The implementation of HL7 FHIR API").
                apiProperty("api.contact.name", "凡森医疗科技有限公司").
                enableCORS(true);
    }
}
