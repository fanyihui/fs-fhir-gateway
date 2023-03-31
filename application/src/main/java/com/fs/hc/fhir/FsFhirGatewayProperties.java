package com.fs.hc.fhir;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties (prefix = "com.fs.hc.fhir.gateway")
@PropertySource(value = "application.properties")
public class FsFhirGatewayProperties {
    private List<String> fhirVersions;
    private String host;
    private int port = 8084;
    private boolean usingClientGeneratedId = false;
    private boolean responsePayload4Delete = true;
    private boolean defaultProfileValidation = false;


    public List<String> getFhirVersions() {
        return fhirVersions;
    }

    public void setFhirVersions(List<String> fhirVersions) {
        this.fhirVersions = fhirVersions;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isUsingClientGeneratedId() {
        return usingClientGeneratedId;
    }

    public void setUsingClientGeneratedId(boolean usingClientGeneratedId) {
        this.usingClientGeneratedId = usingClientGeneratedId;
    }

    public boolean isResponsePayload4Delete() {
        return responsePayload4Delete;
    }

    public void setResponsePayload4Delete(boolean responsePayload4Delete) {
        this.responsePayload4Delete = responsePayload4Delete;
    }

    public boolean isDefaultProfileValidation() {
        return defaultProfileValidation;
    }

    public void setDefaultProfileValidation(boolean defaultProfileValidation) {
        this.defaultProfileValidation = defaultProfileValidation;
    }
}
