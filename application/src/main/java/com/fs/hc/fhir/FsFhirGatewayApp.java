package com.fs.hc.fhir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fs.hc.fhir")
public class FsFhirGatewayApp {
    public static void main(String[] args){
        SpringApplication.run(FsFhirGatewayApp.class, args);
    }
}
