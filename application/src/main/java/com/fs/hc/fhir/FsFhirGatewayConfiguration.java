package com.fs.hc.fhir;

import ca.uhn.fhir.context.FhirContext;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR4BBuilder;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR4Builder;
import com.fs.hc.fhir.core.resprocessor.FhirResourceR5Builder;
import com.fs.hc.fhir.core.resprocessor.IFhirResourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FsFhirGatewayConfiguration {
    @Autowired
    FsFhirGatewayProperties fsFhirGatewayProperties;

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0",
            matchIfMissing = true
    )
    FhirContext fhirR4Context(){
        return FhirContext.forR4();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.0"
    )
    FhirResourceR4Builder fhirResourceR4Builder(){
        return new FhirResourceR4Builder(fhirR4Context());
    }


    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    FhirContext fhirR5Context(){
        return FhirContext.forR5();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "5.0"
    )
    FhirResourceR5Builder fhirResourceR5Builder(){
        return new FhirResourceR5Builder(fhirR5Context());
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    FhirContext fhirR4BContext(){
        return FhirContext.forR4B();
    }

    @Bean
    @ConditionalOnProperty(
            value = "com.fs.hc.fhir.gateway.fhirVersions",
            havingValue = "4.3"
    )
    FhirResourceR4BBuilder fhirResourceR4BBuilder(){
        return new FhirResourceR4BBuilder(fhirR4BContext());
    }
}
