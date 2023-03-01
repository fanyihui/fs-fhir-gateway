package com.fs.hc.fhir.core.model;

import java.util.List;

public class FhirSearchCondition {
    private String path;
    private String modifier;
    private String prefix;
    private String value;
    private FhirSearchParameter fhirSearchParameter;
    private List<FhirSearchCondition> chainedConditions;

    public FhirSearchCondition(){}

    public FhirSearchCondition(String path, String modifier, String prefix, String value, FhirSearchParameter fhirSearchParameter) {
        this.path = path;
        this.modifier = modifier;
        this.prefix = prefix;
        this.value = value;
        this.fhirSearchParameter = fhirSearchParameter;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FhirSearchParameter getFhirSearchParameter() {
        return fhirSearchParameter;
    }

    public void setFhirSearchParameter(FhirSearchParameter fhirSearchParameter) {
        this.fhirSearchParameter = fhirSearchParameter;
    }

    public List<FhirSearchCondition> getChainedCondition() {
        return chainedConditions;
    }

    public void setChainedCondition(List<FhirSearchCondition> chainedConditions) {
        this.chainedConditions
                = chainedConditions;
    }
}
